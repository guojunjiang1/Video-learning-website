package com.xuecheng.test.rabbitmq.mq;

import com.rabbitmq.client.Channel;
import com.xuecheng.test.rabbitmq.config.RabbitMqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
//当项目启动后，他会自动接收消息
@Component
public class 与SpringBoot整合接收通配符方式 {
    //接收方的注解：指定监听的队列名
    @RabbitListener(queues = {RabbitMqConfig.QUEUE_INFORM_EMAIL})
    public void show_email(String msg, Message message,Channel channel){
        System.out.println("接收到了消息是:"+msg);
    }
}
