package com.xuecheng.framework.domain.media.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


@ToString
public enum MediaCode implements ResultCode {
    UPLOAD_FILE_REGISTER_FAIL(false,22001,"上传文件在系统注册失败，请刷新页面重试！"),
    UPLOAD_FILE_REGISTER_EXIST(false,22002,"上传文件在系统已存在！"),
    CHUNK_FILE_EXIST_CHECK(true,22003,"分块文件在系统已存在！"),
    MERGE_FILE_FAIL(false,22004,"合并文件失败，文件在系统已存在！"),
    MERGE_FILE_FAIL2(false,22004,"合并文件失败"),
    MERGE_FILE_FAIL3(false,22004,"文件在数据库中不存在"),
    MERGE_FILE_FAIL1(false,22004,"上传分块文件失败"),
    MERGE_FILE_CHECKFAIL(false,22005,"合并文件校验失败！");

    //操作代码
    @ApiModelProperty(value = "媒资系统操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "媒资系统操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "媒资系统操作提示", example = "文件在系统已存在！", required = true)
    String message;
    private MediaCode(boolean success,int code, String message){
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
