package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_media_process.service.MediaFileProcessTaks;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MediaProcessTask {
    @Autowired
    private MediaFileProcessTaks mediaFileProcessTaks;
    //指定监听的队列，并指定一个工厂(并发工厂，可以同时监听多个消息)
    @RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}",containerFactory = "customContainerFactory")
    public void receiveMediaProcessTaks(String msg) {
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId =(String)map.get("mediaId");
        mediaFileProcessTaks.taks(mediaId);
    }
}
