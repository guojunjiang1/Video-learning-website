package com.xuecheng.test.freemarker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//扫描本项目下的所有类
public class 启动类 {
    public static void main(String[] args) {
        SpringApplication.run(启动类.class,args);
    }
    //注入一个第三方访问http的组件(模拟发送请求http请求)
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }
}
