package com.xuecheng.manage_cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.CmsPagePostService;
import com.xuecheng.manage_cms.service.CmsPagePreviewService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CmsPagePostServiceImpl implements CmsPagePostService {
    @Autowired
    private CmsPagePreviewService cmsPagePreviewService;
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private GridFsTemplate gridFsTemplate;//保存页面到GridFs
    @Autowired
    private RabbitTemplate rabbitTemplate;//生产方发送消息
    @Override
    //页面发布
    //一：将静态化页面存放到GridFs中
    //二：更新页面的HtmlFilePath
    //三：生产方通知消费方下载页面
    public ResponseResult post(String pageId) {
        //判断参数是否有误
        if (!StringUtils.isNotEmpty(pageId)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        //取出页面信息
        CmsPage cmsPage = this.find(pageId);
        if (cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSEA);
        }
        //一：将静态化页面存放到GridFs中
        String pageHtml = cmsPagePreviewService.getPageHtml(pageId);
        ObjectId htmlFileId=null;
        try {
            InputStream inputStream = IOUtils.toInputStream(pageHtml, "utf-8");
            htmlFileId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //二：更新页面的HtmlFileId
        cmsPage.setHtmlFileId(htmlFileId.toHexString());
        cmsPageRepository.save(cmsPage);
        //三：生产方通知消费方下载页面
        Map<String,String> map=new HashMap<>();
        map.put("pageId",pageId);
        String string = JSON.toJSONString(map);
        //第一个参数是交换机，第二个参数是routingKey：站点id，第三个是发送内容，页面id
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE,cmsPage.getSiteId(),string);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //根据id查询页面信息
    public CmsPage find(String pageId){
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (byId.isPresent()){
            CmsPage cmsPage = byId.get();
            return cmsPage;
        }
        return null;
    }
}
