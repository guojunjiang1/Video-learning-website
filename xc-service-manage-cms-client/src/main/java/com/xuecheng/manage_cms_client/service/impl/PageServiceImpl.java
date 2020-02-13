package com.xuecheng.manage_cms_client.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import com.xuecheng.manage_cms_client.service.PageService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@Transactional

//页面发布的消费者，从GridFs下载页面保存到服务器
public class PageServiceImpl implements PageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    //从GridFs中获取文件的对象
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Override
    //从gridFs中下载页面保存到服务器中
    public void savePageToServerPath(String pageId) {
        if (!StringUtils.isNotEmpty(pageId)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        //一：查询页面
        CmsPage byId = this.findById(pageId);
        if (byId==null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSEA);
        }
        //二：查询页面文件id,htmlFileId
        String htmlFileId = byId.getHtmlFileId();
        if (!StringUtils.isNotEmpty(htmlFileId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //三：从GridFs中获取页面的输入流
        InputStream inputStream = this.getFileById(htmlFileId);
        //四：获取要保存的服务器路径
        //保存的服务器路径=站点路径+页面路径+页面名称
        String siteId = byId.getSiteId();
        if (!StringUtils.isNotEmpty(siteId)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        CmsSite cmsSite = this.findById1(siteId);
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();//站点路径
        String pagePhysicalPath = byId.getPagePhysicalPath();//页面路径
        String pageName = byId.getPageName();//页面名称
        String pagePath=sitePhysicalPath+pagePhysicalPath+pageName;
        //五：将页面保存到服务器中
        FileOutputStream fileOutputStream=null;
        try {
             File file=new File(sitePhysicalPath+pagePhysicalPath);
             if (!file.exists()){
                 file.mkdirs();
             }
             fileOutputStream=new FileOutputStream(new File(pagePath));
             IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    //根据id查询页面
    public CmsPage findById(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    @Override
    //根据id查询站点
    public CmsSite findById1(String siteId) {
        Optional<CmsSite> byId = cmsSiteRepository.findById(siteId);
        if (byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    @Override
    //获取要发布页面的输入流
    public InputStream getFileById(String htmlFileId) {
        GridFSFile one = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(one.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(one, gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
