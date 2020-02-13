package com.xuecheng.learning.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//Feign的接口（实现远程调用+客户端负载均衡（自动集成负载均衡））
@FeignClient(value = "XC-SERVICE-SEARCH") //指定远程调用的服务名（在Eureka主界面中找到）
public interface SearchClient {
    //根据页面id查询页面信息，远程调用cms请求数据
    @GetMapping("/search/course/getmedia/{teachplanId}")//用GetMapping标识远程调用的http的方法类型并添加访问路径
    TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
