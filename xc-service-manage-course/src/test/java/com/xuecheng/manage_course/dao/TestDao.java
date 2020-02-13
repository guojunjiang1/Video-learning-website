package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseBaseMapper courseBaseMapper;
    @Autowired
    TeachPlanMapper teachPlanMapper;
    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
        CourseBase courseBase = courseBaseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
        System.out.println(courseBase);

    }
    @Test
    public void test(){
        TeachplanNode a = teachPlanMapper.selectList("4028e581617f945f01617f9dabc40000");
        System.out.println(a);
    }
    @Test
    public void test1(){
        List<CourseInfo> courseList = courseBaseMapper.findCourseList(null);
        for (CourseInfo xx:courseList){
            System.out.println(xx);
        }
    }
}
