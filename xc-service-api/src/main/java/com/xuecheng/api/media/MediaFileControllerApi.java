package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;


@Api(value = "媒资文件接口",description = "媒资文件管理接口",tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {

    @ApiOperation("分页查询所有媒资文件")
    QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);
}
