package com.xuecheng.learning.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.respoonse.LearningCode;
import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.domain.task.XcTaskHis;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.dao.XcLearningCourseRepository;
import com.xuecheng.learning.dao.XcTaskHisRepository;
import com.xuecheng.learning.service.CourseMQService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CourseMQServiceImpl implements CourseMQService {
    @Autowired
    private XcLearningCourseRepository xcLearningCourseRepository;
    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;
    @Override
    //查看之前是否保存过该数据的课程信息和完成任务信息
    public ResponseResult saveCourseAndHis(XcTask xcTask) {
        if (xcTask==null||StringUtils.isEmpty(xcTask.getId())){
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_TASKISNULL);
        }
        Optional<XcTaskHis> byId = xcTaskHisRepository.findById(xcTask.getId());
        if (byId.isPresent()){
            //如果在xc_task_his中已存在这个任务的成功记录，则直接返回
            return new ResponseResult(CommonCode.SUCCESS);
        }
        String requestBody = xcTask.getRequestBody();
        if (StringUtils.isEmpty(requestBody)){
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_TASKISNULL);
        }
        //将任务中的参数信息转为对象
        Map map = JSON.parseObject(requestBody, Map.class);
        if (!map.containsKey("userId")||!map.containsKey("courseId")){
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_TASKISNULL);
        }
        String userId =(String) map.get("userId");
        String courseId =(String) map.get("courseId");
        XcLearningCourse xcLearningCourse = xcLearningCourseRepository.findByCourseIdAndUserId(courseId, userId);
        if (xcLearningCourse!=null){
            //之前添加过该用户和课程信息，进行更新
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setUserId(userId);
            xcLearningCourseRepository.save(xcLearningCourse);
            System.out.println("更新选课信息成功");
        }else {
            //添加选课信息
            XcLearningCourse xcLearningCourse1=new XcLearningCourse();
            xcLearningCourse1.setUserId(userId);
            xcLearningCourse1.setCourseId(courseId);
            xcLearningCourse1.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse1);
            System.out.println("保存选课信息成功！");
        }
        //添加任务成功记录
        XcTaskHis xcTaskHis = new XcTaskHis();
        BeanUtils.copyProperties(xcTask,xcTaskHis);
        xcTaskHisRepository.save(xcTaskHis);
        System.out.println("保存任务成功信息成功！");
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
