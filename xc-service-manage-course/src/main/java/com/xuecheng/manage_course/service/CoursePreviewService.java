package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.response.CoursePublishResult;

public interface CoursePreviewService {
    CoursePublishResult preview(String courseId);//课程预览
}
