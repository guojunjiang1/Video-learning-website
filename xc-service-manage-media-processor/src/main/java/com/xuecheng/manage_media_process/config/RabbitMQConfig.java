package com.xuecheng.manage_media_process.config;

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
    //设置可以并发处理生产方数据(如：连续上传多个视频，可以并发处理)
    public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConcurrentConsumers(DEFAULT_CONCURRENT);
        factory.setMaxConcurrentConsumers(DEFAULT_CONCURRENT);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    //交换机名称
    public static final String EX_MEDIA_PROCESSTASK = "ex_media_processor";

    //视频处理队列
    @Value("${xc-service-manage-media.mq.queue-media-video-processor}")
    public  String queue_media_video_processtask;

    //视频处理路由routingKey
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    public  String routingkey_media_video;

    /**
     * 交换机配置
     * @return the exchange
     */
    //声明交换机
    @Bean(EX_MEDIA_PROCESSTASK)
    public Exchange EX_MEDIA_VIDEOTASK() {
        return ExchangeBuilder.directExchange(EX_MEDIA_PROCESSTASK).durable(true).build();
    }
    //声明队列
    @Bean("queue_media_video_processtask")
    public Queue QUEUE_PROCESSTASK() {
        Queue queue = new Queue(queue_media_video_processtask,true,false,true);
        return queue;
    }
    /**
     * 绑定队列到交换机 .
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    //绑定队列交换机
    @Bean
    public Binding binding_queue_media_processtask(@Qualifier("queue_media_video_processtask") Queue queue, @Qualifier(EX_MEDIA_PROCESSTASK) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingkey_media_video).noargs();
    }
}
