package com.xuecheng.manage_course.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.CoursePubRepository;
import com.xuecheng.manage_course.dao.TeachPlanMediaPubRepository;
import com.xuecheng.manage_course.dao.TeachPlanMediaRepository;
import com.xuecheng.manage_course.service.CoursePostService;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CoursePostServiceImpl implements CoursePostService {
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
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private CoursePubRepository coursePubRepository;
    @Autowired
    private TeachPlanMediaRepository teachPlanMediaRepository;
    @Autowired
    private TeachPlanMediaPubRepository teachPlanMediaPubRepository;
    @Override
    //一键发布课程
    public CoursePublishResult publish(String courseId) {
        //〇：校验当前课程是否存在课程计划并有对应的视频
        TeachplanNode teachplanList = courseService.findTeachplanList(courseId);
        if (teachplanList==null||teachplanList.getChildren().size()==0){
            ExceptionCast.cast(CommonCode.FAIL3);
        }
        List<TeachplanNode> children = teachplanList.getChildren();
        for (TeachplanNode xx:children){
            List<TeachplanNode> children1 = xx.getChildren();
            if (children1.size()==0){
                ExceptionCast.cast(CommonCode.FAIL3);
            }
            for (TeachplanNode xxx:children1){
                String id = xxx.getId();
                Optional<TeachplanMedia> byId = teachPlanMediaRepository.findById(id);
                if (!byId.isPresent()){
                    ExceptionCast.cast(CommonCode.FAIL4);
                }
            }
        }
        //一：为该课程创建一个CMS页面对象
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
        //二：远程调用CMS一键发布
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL2);
        }
        //三：将课程状态设置为已发布
        Optional<CourseBase> byId = courseBaseRepository.findById(courseId);
        if (!byId.isPresent()){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        CourseBase courseBase = byId.get();
        courseBase.setStatus("202002");
        courseBaseRepository.save(courseBase);

        //四：将课程所有信息保存到course_pub
        //创建CoursePub
        CoursePub coursePub = setCoursePub(courseId);
        //将CoursePub保存到数据库
        saveCoursePub(coursePub, courseId);
        //五：将课程计划与其对应的视频信息存到teachplan_media_pub中
        setTeachPlanMediaPub(courseId);
        //向前端返回结果，课程发布后的访问课程详情页面的URL
        return new CoursePublishResult(cmsPostPageResult.getPageUrl(),CommonCode.SUCCESS);
    }

    //创建CoursePub
    private CoursePub setCoursePub(String courseid){
        CoursePub coursePub=new CoursePub();
        CourseView courseview = courseService.courseview(courseid);
        CourseBase courseBase = courseview.getCourseBase();
        if (courseBase!=null) {
            BeanUtils.copyProperties(courseBase, coursePub);
        }
        CourseMarket courseMarket = courseview.getCourseMarket();
        if (courseMarket!=null) {
            BeanUtils.copyProperties(courseMarket, coursePub);
        }
        CoursePic coursePic = courseview.getCoursePic();
        if (coursePic!=null){
            BeanUtils.copyProperties(coursePic, coursePub);
        }
        TeachplanNode teachplanNode = courseview.getTeachplanNode();
        if (teachplanNode!=null){
            String string = JSON.toJSONString(teachplanNode);
            coursePub.setTeachplan(string);
        }
        return coursePub;
    }
    //将数据保存到数据库
    private CoursePub saveCoursePub(CoursePub coursePub,String courseId){
        coursePub.setId(courseId);
        //时间戳,给logstach使用
        coursePub.setTimestamp(new Date());
        //发布时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        coursePub.setPubTime(date);
        CoursePub save = coursePubRepository.save(coursePub);
        return save;
    }

    //保存teachPlanMediaPub
    private void setTeachPlanMediaPub(String courseId) {
        //查询出当前课程计划对应的最新视频信息
        List<TeachplanMedia> list = teachPlanMediaRepository.findByCourseId(courseId);
        //删除掉Pub表中该课程的课程计划与对应视频的信息
        teachPlanMediaPubRepository.deleteByCourseId(courseId);
        //将查询出的最新课程计划视频信息插入到pub表中
        List<TeachplanMediaPub> list1=new ArrayList<>();
        for (TeachplanMedia xx:list){
            TeachplanMediaPub teachplanMediaPub=new TeachplanMediaPub();
            BeanUtils.copyProperties(xx,teachplanMediaPub);
            teachplanMediaPub.setTimeStamp(new Date());
            list1.add(teachplanMediaPub);
        }
        teachPlanMediaPubRepository.saveAll(list1);
    }
}
