package com.xuecheng.api.learning;


import com.xuecheng.framework.domain.learning.respoonse.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="我的学习管理",description = "我的学习管理")
public interface CourseLearningControllerApi {
    @ApiOperation("通过课程计划ID获取对应的视频地址")
    GetMediaResult getmedia(String courseId,String teachplanId);
}
