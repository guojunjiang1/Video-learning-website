package com.xuecheng.manage_cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 测试第三方组件访问http {
    @Autowired
    private RestTemplate restTemplate;
    @Test
    public void test(){
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }

}
