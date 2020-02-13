package com.xuecheng.framework.model.response;

import lombok.ToString;


@ToString
public enum CommonCode implements ResultCode{
    INVALID_PAPAM(false,10003,"非法参数！"),
    SUCCESS(true,10000,"操作成功！"),
    FAIL(false,11111,"操作失败！"),
    FAIL1(false,11111,"保存课程页面失败！"),
    FAIL2(false,11111,"一键发布课程页面失败！"),
    FAIL3(false,11111,"请完善课程计划！"),
    FAIL4(false,11111,"请添加课程计划对应的视频！"),
    UNAUTHENTICATED(false,10001,"请先登录！"),
    UNAUTHORISE(false,10002,"权限不足，无权操作！"),
    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！");
//    private static ImmutableMap<Integer, CommonCode> codes ;
    //操作是否成功
    boolean success;
    //操作代码
    int code;
    //提示信息
    String message;
    private CommonCode(boolean success,int code, String message){
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
