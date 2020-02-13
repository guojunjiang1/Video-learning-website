package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

//自定义异常处理类
public class MyException extends RuntimeException{
    private ResultCode resultCode;
    public MyException(ResultCode resultCode){
        this.resultCode=resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
