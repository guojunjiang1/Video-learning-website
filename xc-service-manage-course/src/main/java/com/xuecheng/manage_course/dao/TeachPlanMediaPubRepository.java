package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


//课程计划和媒资视频的关联(传入logstash)
public interface TeachPlanMediaPubRepository extends JpaRepository<TeachplanMediaPub,String> {
    long deleteByCourseId(String courseId);
}
