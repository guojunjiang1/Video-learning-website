package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.service.CmsPagePostOneService;
import com.xuecheng.manage_cms.service.CmsPagePostService;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CmsPagePostOneServiceImpl implements CmsPagePostOneService {
    @Autowired
    private CmsPageService cmsPageService;
    @Autowired
    private CmsPagePostService cmsPagePostService;
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Override
    //页面一键发布
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        if (cmsPage==null){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        //一：保存页面信息，有则更新，无则添加
        CmsPageResult cmsPageResult = cmsPageService.up(cmsPage);
        if (!cmsPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL1);
        }
        //二：调用页面发布
        CmsPage cmsPageSave = cmsPageResult.getCmsPage();
        ResponseResult post = cmsPagePostService.post(cmsPageSave.getPageId());
        if (!post.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL1);
        }
        //三：访问页面发布后访问课程页面的URL
        //URL为站点域名+站点WebPath+页面WebPath+页面名称
        String siteId = cmsPageSave.getSiteId();
        if (!StringUtils.isNotEmpty(siteId)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL2);
        }
        Optional<CmsSite> byId = cmsSiteRepository.findById(siteId);
        if (!byId.isPresent()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL2);
        }
        CmsSite cmsSite = byId.get();
        String siteDomain = cmsSite.getSiteDomain();//站点域名
        String siteWebPath = cmsSite.getSiteWebPath();//站点WebPath
        String pageWebPath = cmsPageSave.getPageWebPath();//页面WebPath
        String pageName = cmsPageSave.getPageName();//页面名称
        String pageUrl=siteDomain+siteWebPath+pageWebPath+pageName;
        return new CmsPostPageResult(CommonCode.SUCCESS,pageUrl);
    }
}
