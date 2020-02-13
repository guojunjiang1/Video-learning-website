package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 页面静态化获取模板文件方式 {
    @Test
    public void test() throws Exception {
        //1:定义配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //2:得到classpath路径
        String path = this.getClass().getResource("/").getPath();
        //3:定义模板路径
        configuration.setDirectoryForTemplateLoading(new File(path+"/templates/"));
        //4:获取模板路径下的具体模板
        Template template = configuration.getTemplate("遍历list.ftl");
        //5:获取数据
        Map map = getMap();
        //6:静态化
        String string = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //7:输出html文件
        InputStream inputStream = IOUtils.toInputStream(string);
        FileOutputStream outputStream=new FileOutputStream(new File("F:\\IDEA\\学成在线\\测试\\test1.html"));
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    public Map getMap(){
        Map<String,Object> map=new HashMap<>();
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
        return map;
    }
}
