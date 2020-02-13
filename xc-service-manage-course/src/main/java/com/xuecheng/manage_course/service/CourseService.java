package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

public interface CourseService {
    TeachplanNode findTeachplanList(String courseId);//查询课程计划

    ResponseResult addTeachplan(Teachplan teachplan);//添加课程计划

    QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest,String companyId);//查询我的课程

    ResponseResult addCourseBase(CourseBase courseBase);//新增课程

    CourseBase getCourseBaseByid(String courseId);//根据id查询课程基本信息

    ResponseResult updateCourseBase(String courseId, CourseBase courseBase);//修改课程基本信息

    CourseMarket getCourseMarketById(String courseId);//查询课程营销

    ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket);//修改或新建课程营销

    ResponseResult addCoursePic(String courseId, String pic);//保存图片

    CoursePic findCoursePic(String courseId);//查询图片

    ResponseResult deleteCoursePic(String courseId);//删除图片

    CourseView courseview(String courseId);//课程视图查询(课程预览的DataUrl)

    ResponseResult savemedia(TeachplanMedia teachplanMedia);//课程计划和媒资视频关联
}
