package com.xuecheng.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.learning.respoonse.LearningCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.order.dao.XcTaskHisRepository;
import com.xuecheng.order.dao.XcTaskRepository;
import com.xuecheng.order.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
@Transactional
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private XcTaskRepository xcTaskRepository;
    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    //查询任务表中是否有任务
    public List<XcTask> findByUpDate(Date date, int size) {
        Pageable pageable= PageRequest.of(0,size);
        Page<XcTask> byUpdateTimeBefore = xcTaskRepository.findByUpdateTimeBefore(pageable,date);
        List<XcTask> content = byUpdateTimeBefore.getContent();
        return content;
    }

    @Override
    //向MQ发送任务
    public void publish(XcTask xx) {
        Optional<XcTask> byId = xcTaskRepository.findById(xx.getId());
        if (byId.isPresent()) {
            XcTask xcTask = byId.get();
            //向MQ发送消息
            String mqExchange = xcTask.getMqExchange();//要发送的交换机名
            String mqRoutingkey = xcTask.getMqRoutingkey();//要发送的routingKey
            rabbitTemplate.convertAndSend(mqExchange,mqRoutingkey,xcTask);
            //更新该任务的更新时间
            xcTask.setUpdateTime(new Date());
            xcTaskRepository.save(xcTask);
            }
    }

    @Override
    //乐观锁，更新version(看当前任务是否被其他工程执行了)
    public int updateVersion(XcTask xcTask) {
        int i = xcTaskRepository.updateTaskVersion(xcTask.getId(), xcTask.getVersion());
        return i;
    }

    @Override
    //添加课程完成，删除任务并添加一个任务完成记录
    public void delXc(XcTask xcTask) {
        if (xcTask==null|| StringUtils.isEmpty(xcTask.getId())){
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_TASKISNULL1);
        }
        String id = xcTask.getId();
        Optional<XcTask> byId = xcTaskRepository.findById(id);
        if (byId.isPresent()){
            XcTask xcTask1 = byId.get();
            //删除任务记录
            xcTaskRepository.delete(xcTask1);
            //添加任务完成记录
            XcTaskHis xcTaskHis=new XcTaskHis();
            BeanUtils.copyProperties(xcTask1,xcTaskHis);
            xcTaskHisRepository.save(xcTaskHis);
            System.out.println("删除任务记录，添加任务完成记录成功！");
        }
    }

}
