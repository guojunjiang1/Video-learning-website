package com.xuecheng.framework.domain.course.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CoursePublishResult extends ResponseResult {
    String previewUrl;//页面预览的url||页面发布后访问课程的url
    public CoursePublishResult(String previewUrl, ResultCode resultCode) {
        super(resultCode);
        this.previewUrl = previewUrl;
    }
}
