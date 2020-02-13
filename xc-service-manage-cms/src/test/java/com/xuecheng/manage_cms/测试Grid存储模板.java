package com.xuecheng.manage_cms;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 测试Grid存储模板 {
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Test
    public void test() throws Exception{
        File file=new File("F:\\IDEA\\学成在线\\模板\\课程模板\\course.ftl");
        FileInputStream fileInputStream=new FileInputStream(file);
        ObjectId store = gridFsTemplate.store(fileInputStream, "course.ftl");
        System.out.println(store);
    }
}
