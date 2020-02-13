package com.xuecheng.learning.service;

import com.xuecheng.framework.domain.learning.respoonse.GetMediaResult;

public interface CourseLearningService {
    GetMediaResult getmedia(String courseId,String teachplanId);//根据课程计划id获取其对应的媒资视频信息
}
