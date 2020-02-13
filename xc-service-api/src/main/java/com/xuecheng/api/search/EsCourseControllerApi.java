package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

@Api(value = "课程搜索",description = "课程搜索",tags = {"课程搜索"})
public interface EsCourseControllerApi {
    @ApiOperation("课程综合搜索")
    QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);
    @ApiOperation("根据课程id查询课程详细信息")
    Map<String,CoursePub> getall(String id);
    @ApiOperation("根据课程计划id查询其对应的媒资视频")
    TeachplanMediaPub getmedia(String teachplanId);
}
