package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//课程管理接口
@Repository
public interface CourseBaseMapper {
    CourseBase findCourseBaseById(String id);//查询课程基本信息

    @Select("<script>select * from course_base <if test=\"companyId !=null \">where company_id = #{companyId} </if></script>")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "pic",column = "id",javaType = String.class,one = @One(select = "com.xuecheng.manage_course.dao.CourseBaseMapper.findPic"))
    })
    List<CourseInfo> findCourseList(CourseListRequest courseListRequest);//查询我的课程

    @Select("select pic from course_pic where courseid=#{courseid}")
    String findPic(String courseId);
}
