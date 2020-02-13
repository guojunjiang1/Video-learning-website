package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//课程计划和媒资视频的关联
public interface TeachPlanMediaRepository extends JpaRepository<TeachplanMedia,String> {
    List<TeachplanMedia> findByCourseId(String courseId);
}
