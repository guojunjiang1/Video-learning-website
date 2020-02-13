package com.xuecheng.learning.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //监听发(消费方)并发数量
    public static final int DEFAULT_CONCURRENT = 10;
    @Bean("customContainerFactory")
    //设置可以并发处理生产方数据(如：同时处理多个选课的信息)
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    //交换机
    public static final String EX_LEARNING_ADDCHOOSECOURSE="ex_learning_addchoosecourse";

    //完成添加选课消息队列
    @Value("${xc-service-manage-order.mq.Queue}")
    public String XC_LEARNING_FINISHADDCHOOSECOURSE;
    //队列Bean的名称
    public static final String XC_LEARNING_FINISHADDCHOOSECOURSE1="Queue";

    //添加选课路由key(发送选课消息的RoutingKey)
    @Value("${xc-service-manage-order.mq.routingkey-order-LEARNING}")
    public String XC_LEARNING_ADDCHOOSECOURSE_KEY;

    /**
     * 交换机配置
     * @return the exchange
     */
    @Bean(EX_LEARNING_ADDCHOOSECOURSE)
    public Exchange EX_DECLARE() {
        return ExchangeBuilder.directExchange(EX_LEARNING_ADDCHOOSECOURSE).durable(true).build();
    }
    //声明队列
    @Bean(XC_LEARNING_FINISHADDCHOOSECOURSE1)
    public Queue QUEUE_DECLARE() {
        Queue queue = new Queue(XC_LEARNING_FINISHADDCHOOSECOURSE,true,false,true);
        return queue;
    }
    /**
     * 绑定队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding_queue_media_processtask(@Qualifier(XC_LEARNING_FINISHADDCHOOSECOURSE1) Queue queue, @Qualifier(EX_LEARNING_ADDCHOOSECOURSE) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(XC_LEARNING_ADDCHOOSECOURSE_KEY).noargs();
    }

}
