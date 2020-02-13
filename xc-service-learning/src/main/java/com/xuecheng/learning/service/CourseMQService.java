package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.task.XcTask;
import com.xuecheng.framework.model.response.ResponseResult;

public interface CourseMQService {
    //查看之前是否保存过该数据的课程信息和完成任务信息
    ResponseResult saveCourseAndHis(XcTask xcTask);
}
