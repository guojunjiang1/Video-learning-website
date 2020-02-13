package com.xuecheng.manage_course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.CourseBaseMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 通过Eureka注册中心方式获取到信息
 * 并且调用时通过Ribbon实现负载均衡(开启Eureka服务器和两个Cms服务器)
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class 测试远程调用Course调用Cms {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CmsPageClient cmsPageClient;
    @Test
    //第一种：通过RestTemplate+Ribbon方式实现
    public void test(){
        //在RestTemplate添加上@LoadBalanced注解后，自动执行Ribbon负责均衡
        String cmsName="XC-SERVICE-MANAGE-CMS";//在Eureka主界面中获取CMS的名称
        //远程调用Cms，端口号那里用CMS名称代替
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://" + cmsName + "/cms/page/get/5dfee0a0f94c402e0095b772", Map.class);
        System.out.println(forEntity);
    }

    //第二种：通过Ribbon+Feign方式实现
    @Test
    public void test1(){
        CmsPage cmsPageById = cmsPageClient.findCmsPageById("5dfee0a0f94c402e0095b772");
        System.out.println(cmsPageById);
    }
}
