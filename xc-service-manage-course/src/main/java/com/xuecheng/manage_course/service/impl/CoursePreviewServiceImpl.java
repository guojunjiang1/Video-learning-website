package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.service.CoursePreviewService;
import com.xuecheng.manage_course.service.CourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class CoursePreviewServiceImpl implements CoursePreviewService {
    @Value("${course-publish.dataUrl}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;

    @Autowired
    private CmsPageClient cmsPageClient;
    @Autowired
    private CourseService courseService;
    @Override
    //课程预览
    /*
    * 一：为当前课程创建一个Cms页面，二：远程调用Cms保存该页面，三：返回Cms中页面预览的Url
    * */
    public CoursePublishResult preview(String courseId) {
        if (!StringUtils.isNotEmpty(courseId)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        //一：为该课程创建一个CMS页面
        CmsPage cmsPage=createPage(courseId);
        //二:通过Eureka+Feign远程调用CMS中的保存页面功能,并返回页面id
        String pageId =  getCmsPageResult(cmsPage);
        if (!StringUtils.isNotEmpty(pageId)){
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_NAMEISNULL4);
        }
        //三:为前端返回课程预览的URL
        //这个URL就是CMS工程中页面预览的接口，传递一个页面id即可
        String previewUrl="http://www.xuecheng.com:81/cms/preview/"+pageId;
        CoursePublishResult coursePublishResult=new CoursePublishResult(previewUrl,CommonCode.SUCCESS);
        return coursePublishResult;
    }



    //一：创建页面
    private CmsPage createPage(String courseId) {
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setDataUrl(publish_dataUrlPre+courseId);//这个DataUrl是Course中课程视图查询的接口
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageName(courseId+".html");
        cmsPage.setPageCreateTime(new Date());
        CourseBase course = courseService.getCourseBaseByid(courseId);
        cmsPage.setPageAliase(course.getName());
        return cmsPage;
    }

    //二：远程调用
    private String getCmsPageResult(CmsPage cmsPage) {
        CmsPageResult cmsPageResult = cmsPageClient.up(cmsPage);
        if (cmsPageResult.isSuccess()){
            return cmsPageResult.getCmsPage().getPageId();
        }else {
            return null;
        }
    }
}
