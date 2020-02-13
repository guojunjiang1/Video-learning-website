package com.xuecheng.order.service;

import com.xuecheng.framework.domain.task.XcTask;

import java.util.Date;
import java.util.List;

public interface TaskService {
    List<XcTask> findByUpDate(Date date, int size);//分页查询指定时间之前的数据

    void publish(XcTask xx);//向MQ发送任务

    int updateVersion(XcTask xcTask);//乐观锁，更新version(看当前任务是否被其他工程执行了)

    void delXc(XcTask xcTask);//添加课程完成，删除任务并添加一个任务完成记录
}
