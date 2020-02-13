package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

import java.util.Map;

public interface EsCourseService {
    QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);//课程搜索列表

    Map<String, CoursePub> getall(String id);//根据课程id查询课程信息(learning前端调用)

    QueryResponseResult<TeachplanMediaPub> getmedia(String[] strings);//根据多个课程计划id查询对应的视频信息（learning后端工程调用）
}
