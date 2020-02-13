package com.xuecheng.order;

import com.xuecheng.order.dao.XcTaskRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSpringTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSpringTask.class);

    //定义任务调试策略
    @Scheduled(cron = "0/3 * * * * *")//每隔3秒去执行
    @Test
    public void task1() {
        LOGGER.info("===============测试定时任务1开始===============");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("===============测试定时任务1结束===============");

    }

    //定义任务调试策略
    @Scheduled(fixedRate = 3000) //在任务开始后3秒执行下一次调度
    @Test
    public void task2() {
        LOGGER.info("===============测试定时任务2开始===============");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("===============测试定时任务2结束===============");
    }
    @Autowired
    XcTaskRepository xcTaskRepository;
    @Transactional
    @Test
    public  void tassas(){
        xcTaskRepository.updateTaskVersion("9",1);
    }
}
