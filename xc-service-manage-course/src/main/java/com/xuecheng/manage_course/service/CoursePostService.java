package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.response.CoursePublishResult;

public interface CoursePostService {
    CoursePublishResult publish(String courseId);//一键发布课程
}
