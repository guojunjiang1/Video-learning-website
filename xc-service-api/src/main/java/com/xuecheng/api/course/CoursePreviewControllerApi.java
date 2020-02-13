package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="课程页面管理接口",description = "课程详情预览")
public interface CoursePreviewControllerApi {
    @ApiOperation("课程预览")
    CoursePublishResult preview(String courseId);
}
