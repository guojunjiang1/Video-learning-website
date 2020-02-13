package com.xuecheng.manage_course.client;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


//Feign的接口（实现远程调用+客户端负载均衡（自动集成负载均衡））
@FeignClient(value = "XC-SERVICE-MANAGE-CMS") //指定远程调用的服务名（在Eureka主界面中找到）
public interface CmsPageClient {
    //根据页面id查询页面信息，远程调用cms请求数据
    @GetMapping("/cms/page/get/{id}")//用GetMapping标识远程调用的http的方法类型并添加访问路径
    CmsPage findCmsPageById(@PathVariable("id") String id);

    //远程调用cms的保存页面功能
    @PostMapping("/cms/page/save")
    CmsPageResult up(@RequestBody CmsPage cmspage);

    //远程调用cms的页面一键发布功能
    @PostMapping("/cms/page/postPageQuick")
    CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage);
}
