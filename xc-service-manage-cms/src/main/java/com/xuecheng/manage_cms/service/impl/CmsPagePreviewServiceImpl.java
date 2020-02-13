package com.xuecheng.manage_cms.service.impl;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.CmsPagePreviewService;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
@Service
public class CmsPagePreviewServiceImpl implements CmsPagePreviewService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Autowired
    private RestTemplate restTemplate;//第三方http组件
    @Autowired
    private CmsTemplateService cmsTemplateService;
    //从GridFs中获取文件的对象
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    /*
    *一：获取页面的DataUrl(获取数据的路径)
    *二：获取页面的数据
    *三：获取页面的模板
    *四：执行静态化页面
    * */
    @Override
    //生成静态化页面
    public String getPageHtml(String pageId){
        if (StringUtils.isEmpty(pageId)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        Map map = getModelBypageId(pageId);//获取数据
        if (map==null){
            //数据不存在
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        String template= getTemplateByPageId(pageId);//获取模板
        if (StringUtils.isEmpty(template)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        String s = generateHtml(map, template);//静态化
        if (StringUtils.isEmpty(s)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return s;
    }
    //一二:获取dataUrl并获取数据
    private Map getModelBypageId(String pageId){
        CmsPage cmsPage = this.findById(pageId);
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)){
            //要操作的页面中没有DataUrl
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //通过第三方http组件访问得到数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }
    //三:获取模板
    private String getTemplateByPageId(String pageId) {
        CmsPage cmsPage = this.findById(pageId);
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)){
            //要操作的页面中没有模板id
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //通过模板id查询模板信息
        CmsTemplate cmsTemplate = cmsTemplateService.findById(templateId);
        if (cmsTemplate==null){
            //模板为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //获取模板文件id
        String templateFileId = cmsTemplate.getTemplateFileId();
        if (StringUtils.isEmpty(templateFileId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //获取模板
        GridFSFile one = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(one.getObjectId());
        GridFsResource gridFsResource = new GridFsResource(one, gridFSDownloadStream);
        String string = null;
        try {
            string = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }
    //四：静态化页面
    private String generateHtml(Map model,String templateContent) {
        Configuration configuration=new Configuration(Configuration.getVersion());
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateContent);
        configuration.setTemplateLoader(stringTemplateLoader);
        String string=null;
        try {
            Template template = configuration.getTemplate("template","utf-8");
            string = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }
    private CmsPage findById(String pageId){
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (!byId.isPresent()){
            //要操作的页面不存在
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSEA);
        }
        CmsPage cmsPage = byId.get();
        return cmsPage;
    }
}
