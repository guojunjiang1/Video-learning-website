package com.xuecheng.order.mq;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.order.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class CourseTask {
    @Autowired
    private TaskService taskService;
    //定时任务SpringTask
    @Scheduled(cron = "0/3 * * * * *")//每隔3秒去执行
    public void task(){
        Date date = new Date();//获取当前时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,-1);//设置查询的时间是一分钟之前的任务
        //扫描数据库，查询到一分钟前未完成的任务
        List<XcTask> list = taskService.findByUpDate(calendar.getTime(), 10);
        for(XcTask xx:list){
            //通过乐观锁，查看当前任务是否被其他结点执行
            if (taskService.updateVersion(xx)>0) {
                //没有被其他结点执行，向MQ发送任务
                taskService.publish(xx);
            }
        }
    }
}
