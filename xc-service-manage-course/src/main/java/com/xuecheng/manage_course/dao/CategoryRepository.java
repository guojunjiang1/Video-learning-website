package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Category;
import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;

//课程分类
public interface CategoryRepository extends JpaRepository<Category,String> {
}
