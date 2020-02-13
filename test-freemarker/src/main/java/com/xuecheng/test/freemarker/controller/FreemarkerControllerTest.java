package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/freemarker")
public class FreemarkerControllerTest {
    @RequestMapping("/test1")
    public String test(Map<String,Object> map){
        map.put("name","郭峻江");
        return "test1";
    }

    @RequestMapping("/test2")
    //存放list类型数据
    public String test2(Map<String,Object> map){
        //第一个学生
        Student student=new Student();
        student.setName("郭峻江");
        student.setAge(18);
        //第二个学生
        Student student1=new Student();
        student1.setName("郝靖蓉");
        student1.setAge(18);
        //设置第一个学生的朋友列表
        List<Student> friends=new ArrayList<>();
        friends.add(student1);
        student.setFriends(friends);
        student.setBestFriend(student1);

        List<Student> list=new ArrayList<>();
        list.add(student);
        list.add(student1);
        map.put("stus",list);
        return "遍历list";
    }

    @RequestMapping("/test3")
    //存放Map类型数据
    public String test3(Map<String,Object> map){
        //第一个学生
        Student student=new Student();
        student.setName("郭峻江");
        student.setAge(18);
        //第二个学生
        Student student1=new Student();
        student1.setName("郝靖蓉");
        student1.setAge(18);
        //设置第一个学生的朋友列表
        List<Student> friends=new ArrayList<>();
        friends.add(student1);
        student.setFriends(friends);
        student.setBestFriend(student1);

        Map<String,Student> map1=new HashMap<>();
        map1.put("stu1",student);
        map1.put("stu2",student1);
        map.put("stuMap",map1);
        return "遍历map";
    }
}
