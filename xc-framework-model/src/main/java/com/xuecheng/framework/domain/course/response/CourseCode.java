package com.xuecheng.framework.domain.course.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2018/3/5.
 */
@ToString
public enum CourseCode implements ResultCode {
    COURSE_DENIED_DELETE(false,31001,"删除课程失败，只允许删除本机构的课程！"),
    COURSE_PUBLISH_PERVIEWISNULL(false,31002,"还没有进行课程预览！"),
    COURSE_PUBLISH_CDETAILERROR(false,31003,"创建课程详情页面出错！"),
    COURSE_PUBLISH_COURSEIDISNULL(false,31004,"课程Id为空！"),
    COURSE_PUBLISH_COURSEIDISNULL1(false,31008,"发布课程页面失败！"),
    COURSE_PUBLISH_COURSEIDISNULL2(false,31009,"该课程页面的站点id错误！"),
    COURSE_PUBLISH_VIEWERROR(false,31005,"发布课程视图出错！"),
    COURSE_MEDIS_URLISNULL(false,31101,"选择的媒资文件访问地址为空！"),
    COURSE_MEDIS_NAMEISNULL(false,31102,"选择的媒资文件名称为空！"),
    COURSE_MEDIS_NAMEISNULL2(false,31105,"课程已有图片，不能继续添加！"),
    COURSE_MEDIS_NAMEISNULL3(false,31106,"当前课程还没有图片！"),
    COURSE_MEDIS_NAMEISNULL4(false,31107,"远程调用CMS保存课程页面失败！"),
    COURSE_MEDIA_TEACHPLAN_GRADEERROR(false,31108,"只能为三级课程计划添加视频"),
    COURSE_MEDIS_NAMEISNULL1(false,31104,"要修改的课程不存在！");
    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private CourseCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, CourseCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, CourseCode> builder = ImmutableMap.builder();
        for (CourseCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
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
