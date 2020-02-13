package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="课程管理接口",description = "课程管理接口，提供课程的管理、查询接口")
public interface CourseControllerApi {
    @ApiOperation("根据id查询课程计划")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程")
    QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程基本信息")
    ResponseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("查询课程基本信息")
    CourseBase getCourseBaseById(String courseId);

    @ApiOperation("修改课程信息")
    ResponseResult updateCourseBase(String courseId,CourseBase courseBase);

    @ApiOperation("查询课程营销")
    CourseMarket getCourseMarketById(String courseId);

    @ApiOperation("修改或添加课程营销")
    ResponseResult updateCourseMarket(String courseId,CourseMarket courseMarket);

    @ApiOperation("保存上传的图片")
    ResponseResult addCoursePic(String courseId,String pic);

    @ApiOperation("查询课程的图片")
    CoursePic findCoursePic(String courseId);

    @ApiOperation("删除图片")
    ResponseResult deleteCoursePic(String courseId);

    @ApiOperation("课程视图查询")
    CourseView courseview(String courseId);

    @ApiOperation("保存课程计划和媒资视频关联")
    ResponseResult savemedia(TeachplanMedia teachplanMedia);
}
