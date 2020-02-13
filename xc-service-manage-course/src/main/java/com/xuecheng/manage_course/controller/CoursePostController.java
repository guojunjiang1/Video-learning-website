package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CoursePostControllerApi;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.manage_course.service.CoursePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CoursePostController implements CoursePostControllerApi {
    @Autowired
    private CoursePostService coursePostService;
    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String courseId) {
        return coursePostService.publish(courseId);
    }
}
