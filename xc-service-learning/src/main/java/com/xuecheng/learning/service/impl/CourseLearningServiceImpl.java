package com.xuecheng.learning.service.impl;

import com.xuecheng.framework.domain.course.TeachplanMediaPub;
import com.xuecheng.framework.domain.learning.respoonse.GetMediaResult;
import com.xuecheng.framework.domain.learning.respoonse.LearningCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.learning.client.SearchClient;
import com.xuecheng.learning.service.CourseLearningService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseLearningServiceImpl implements CourseLearningService {
    @Autowired
    private SearchClient searchClient;
    @Override
    //根据课程计划id获取其对应的媒资视频信息
    public GetMediaResult getmedia(String courseId,String teachplanId) {
        //远程调用search工程获取课程计划对应的媒资视频
        if (StringUtils.isEmpty(teachplanId)){
            ExceptionCast.cast(CommonCode.INVALID_PAPAM);
        }
        TeachplanMediaPub getmedia = searchClient.getmedia(teachplanId);
        if (getmedia==null||StringUtils.isEmpty(getmedia.getMediaUrl())){
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        return new GetMediaResult(CommonCode.SUCCESS,getmedia.getMediaUrl());
    }
}
