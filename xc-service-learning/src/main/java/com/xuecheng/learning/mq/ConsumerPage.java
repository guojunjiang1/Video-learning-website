package com.xuecheng.learning.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.config.RabbitMQConfig;
import com.xuecheng.learning.service.CourseMQService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


//监听Order发送的添加选课
@Component
public class ConsumerPage {
    private String ex= RabbitMQConfig.EX_LEARNING_ADDCHOOSECOURSE;//要发送的交换机名称
    @Value("${xc-service-manage-order.mq.routingkey-order-LEARNING1}")
    private String routingKey;//要发送的RoutingKey

    @Autowired
    private CourseMQService courseMQService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {"${xc-service-manage-order.mq.Queue}"})
    public void guo(XcTask xcTask){
        System.out.println("学习工程监听到一个");
        //查看之前是否保存过该数据的课程信息和完成任务信息(没有则保存，存在则更新)
        ResponseResult responseResult=courseMQService.saveCourseAndHis(xcTask);
        if (responseResult.isSuccess()){
            //保存成功，向订单工程发送成功信息
            rabbitTemplate.convertAndSend(ex,routingKey,xcTask);
            System.out.println("处理成功，返回信息");
        }
    }
}
