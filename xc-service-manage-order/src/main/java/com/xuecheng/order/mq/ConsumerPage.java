package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.service.TaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//监听Learning发送的选课成功
@Component
public class ConsumerPage {
    @Autowired
    private TaskService taskService;
    @RabbitListener(queues = {"${xc-service-manage-order.mq.Queue}"})
    public void guo(XcTask xcTask){
        System.out.println("订单工程监听到一个");
        taskService.delXc(xcTask);
    }
}
