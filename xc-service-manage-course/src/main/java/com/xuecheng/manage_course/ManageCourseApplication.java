package com.xuecheng.manage_course;

import com.xuecheng.framework.interceptor.FeignClientInterceptor;
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
import org.springframework.web.client.RestTemplate;

@EnableFeignClients//开启Feign，客户端负载均衡
@EnableDiscoveryClient//表示该工程是一个Eureka客户端，向Eureka服务端上报状态
@SpringBootApplication
@MapperScan("com.xuecheng.manage_course.dao")
@EntityScan("com.xuecheng.framework.domain.course")//扫描实体类
@ComponentScan(basePackages={"com.xuecheng.api"})//扫描接口
@ComponentScan(basePackages={"com.xuecheng.manage_course"})
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common下的所有类
public class ManageCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManageCourseApplication.class, args);
    }

    //注入一个第三方访问http的组件(模拟发送请求http请求)
    @Bean
    @LoadBalanced//自动开启Ribbon，客户端负载均衡
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    //注入一个Feign拦截器，在用Feign调用CMS工程时，将JWT令牌传入Feign
    @Bean
    public FeignClientInterceptor getFeignClientInterceptor(){
        return new FeignClientInterceptor();
    }
}
