package com.xuecheng.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;


@EnableFeignClients//开启Feign，客户端负载均衡
@EnableDiscoveryClient//表示该工程是一个Eureka客户端，向Eureka服务端上报状态
@EnableScheduling//开启SpringTask定时任务
@EntityScan(value={"com.xuecheng.framework.domain.order","com.xuecheng.framework.domain.task"})//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描framework中通用类
@ComponentScan(basePackages={"com.xuecheng.order"})//扫描本项目下的所有类
@SpringBootApplication
public class ManageOrderApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManageOrderApplication.class, args);
    }

    //注入一个第三方访问http的组件(模拟发送请求http请求)
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}