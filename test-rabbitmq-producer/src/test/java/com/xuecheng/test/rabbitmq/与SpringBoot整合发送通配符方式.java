package com.xuecheng.test.rabbitmq;

import com.xuecheng.test.rabbitmq.config.RabbitMqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 与SpringBoot整合发送通配符方式 {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    public void sendEmail(){
        String message="send email message to user";
        /*
        * 发送消息
        * 参数1：交换机名称
        * 参数2：routingKey名称
        * 参数3：消息内容
        * */
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
    }
}
