package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="课程页面管理接口",description = "课程发布")
public interface CoursePostControllerApi {
    @ApiOperation("课程发布")
    CoursePublishResult publish(String courseId);
}
