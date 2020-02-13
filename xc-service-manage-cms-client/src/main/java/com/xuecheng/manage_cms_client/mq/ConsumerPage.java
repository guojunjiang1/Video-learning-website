package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

//消费方，自动监听是否有消息
@Component
public class ConsumerPage {
    @Autowired
    private PageService pageService;

    //监听队列，队列名为application.yml中配置的
    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void guo(String msg){
        Map map = JSON.parseObject(msg, Map.class);
        if (map.containsKey("pageId")){
            Object pageId = map.get("pageId");
            pageService.savePageToServerPath((String)pageId);
        }
    }
}
