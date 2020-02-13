package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//统一管理异常
@ControllerAdvice//增强的控制器
public class ExceptionCatch {
    //日志对象
    private static final Logger LOGGER= LoggerFactory.getLogger(ExceptionCatch.class);

    //定义一个处理框架,程序异常的map集合
    private static ImmutableMap<Class<? extends Throwable>,ResultCode> MAP;
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder=ImmutableMap.builder();

    //捕获MyException自定义异常(程序员自己抛出的异常)
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public ResponseResult myException(MyException myException){
        LOGGER.error("catch Exception:{}",myException.getMessage());
        ResultCode resultCode = myException.getResultCode();
        return new ResponseResult(resultCode);
    }

    //捕获Exception异常(框架抛出的异常或程序抛出的异常)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception exception){
        LOGGER.error("catch Exception:{}",exception.getMessage());
        if (MAP==null){
            MAP=builder.build();//加载builder，将builder中的数据传递给MAP
        }
        ResultCode resultCode = MAP.get(exception.getClass());
        if (resultCode!=null){
            return new ResponseResult(resultCode);//如果Map中包含此异常，则返回Map中存放的异常对应的错误提示
        }else {
            return new ResponseResult(CommonCode.SERVER_ERROR);//如果Map中没有记录该异常，则返回系统繁忙
        }
    }
    static {
        //定义程序异常对应的错误代码
        builder.put(HttpMediaTypeNotSupportedException.class, CommonCode.INVALID_PAPAM);//添加空数据转换RequestBody对应的异常
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PAPAM);//添加RequestBody对应的异常
    }
}
