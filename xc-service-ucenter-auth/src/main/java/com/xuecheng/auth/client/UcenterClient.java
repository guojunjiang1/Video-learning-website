package com.xuecheng.auth.client;


import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//Feign的接口（实现远程调用+客户端负载均衡（自动集成负载均衡））
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER) //指定远程调用的服务名（在Eureka主界面中找到）
public interface UcenterClient {
    @GetMapping("/ucenter/getuserext")
    XcUserExt getUserext(@RequestParam("username") String username);
}
