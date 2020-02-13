package com.xuecheng.manage_media.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 * @version 1.0
 **/
//配置RabbitMq消息队列
@Configuration
public class RabbitmqConfig {

    //交换机的名称
    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor";//交换机名称

    //配置交换机
    @Bean(EX_MEDIA_PROCESSTASK)
    public Exchange EXCHANGE_TOPICS_INFORM() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESSTASK).durable(true).build();
    }
}
