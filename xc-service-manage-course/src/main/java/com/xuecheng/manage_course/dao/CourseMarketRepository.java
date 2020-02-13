package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

//课程营销接口
public interface CourseMarketRepository extends JpaRepository<CourseMarket,String> {
}
