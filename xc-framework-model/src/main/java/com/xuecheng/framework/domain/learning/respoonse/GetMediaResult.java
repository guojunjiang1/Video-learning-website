package com.xuecheng.framework.domain.learning.respoonse;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class GetMediaResult extends ResponseResult {
    private String fileUrl;
    public GetMediaResult(ResultCode resultCode, String fileUrl) {
        super(resultCode);
        this.fileUrl = fileUrl;
    }
}
