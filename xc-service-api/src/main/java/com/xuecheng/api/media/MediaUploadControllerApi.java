package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;


@Api(value = "媒资管理接口",description = "媒资管理接口，提供文件上传、处理")
public interface MediaUploadControllerApi {

    @ApiOperation("校验文件是否存在")
    ResponseResult register(String fileMd5,
                            String fileName,
                            Long fileSize,
                            String mimetype,
                            String fileExt);

    @ApiOperation("校验分块文件是否存在")
    CheckChunkResult checkchunk(String fileMd5,
                                Integer chunk,
                                Integer chunkSize);

    @ApiOperation("上传分块")
    ResponseResult uploadchunk(MultipartFile file,
                               String fileMd5,
                               Integer chunk);

    @ApiOperation("合并分块")
    ResponseResult mergechunks(String fileMd5,
                               String fileName,
                               Long fileSize,
                               String mimetype,
                               String fileExt);
}
