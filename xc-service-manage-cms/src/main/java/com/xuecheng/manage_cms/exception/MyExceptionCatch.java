package com.xuecheng.manage_cms.exception;

import com.xuecheng.framework.exception.ExceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice//增强的控制器
public class MyExceptionCatch extends ExceptionCatch {
    //捕获系统抛出的异常
    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);//添加权限不足对应的异常
    }
}
