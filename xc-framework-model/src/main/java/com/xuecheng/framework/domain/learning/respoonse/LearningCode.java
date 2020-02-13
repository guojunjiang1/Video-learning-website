package com.xuecheng.framework.domain.learning.respoonse;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.ToString;


@ToString
public enum LearningCode implements ResultCode {
    CHOOSECOURSE_TASKISNULL(false,23002,"添加选课任务参数错误"),
    CHOOSECOURSE_TASKISNULL1(false,23003,"删除任务参数错误"),
    LEARNING_GETMEDIA_ERROR(false,23001,"当前课程计划还未添加视频！");
    //操作代码
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
