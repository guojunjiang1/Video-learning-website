package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import lombok.Data;
import lombok.ToString;


@Data
public class CourseInfo extends CourseBase {

    //课程图片
    private String pic;

    @Override
    public String toString() {
        return super.toString()+"CourseInfo{" +
                "pic='" + pic + '\'' +
                '}';
    }
}
