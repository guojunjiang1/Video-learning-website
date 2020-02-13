package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//课程计划
public interface TeachPlanRepository extends JpaRepository<Teachplan,String> {
    List<Teachplan> findByCourseidAndParentid(String courseId,String parentId);
}
