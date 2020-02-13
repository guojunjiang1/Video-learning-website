package com.xuecheng.test.freemarker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping("/banner")
    public String test1(Map<String,Object> map){
        //使用第三方http组件获取到数据，将数据存放到模板中
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return "index_banner";
    }

    @RequestMapping("/course")
    public String test2(Map<String,Object> map){
        //使用第三方http组件获取到数据，将数据存放到模板中
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseview/402869816fa27fe7016fa28132740000", Map.class);
        Map body = forEntity.getBody();
        map.putAll(body);
        return "course";
    }

}
