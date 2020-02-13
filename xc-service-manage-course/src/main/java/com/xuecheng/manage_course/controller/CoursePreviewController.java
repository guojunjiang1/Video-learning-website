package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePreviewControllerApi;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.manage_course.service.CoursePreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CoursePreviewController implements CoursePreviewControllerApi {
    @Autowired
    private CoursePreviewService coursePreviewService;
    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String courseId) {
        return coursePreviewService.preview(courseId);
    }
}
