package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class CourseView {
    private CourseBase courseBase;//课程基本信息
    private CoursePic coursePic;//课程图片
    private CourseMarket courseMarket;//课程营销
    private TeachplanNode teachplanNode;//课程计划
}
