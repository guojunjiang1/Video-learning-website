package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface MediaUploadService {
    /*
    * 前端执行第一步，如果文件存在直接退出
    * 如果文件不存在执行第二步，如果块文件存在，执行第四步
    * 如果块文件不存在，执行第三步，第四步
    * */
    //一：查看文件是否存在
    ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);
    //二：查看分块文件是否存在
    CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize);
    //三：上传分块文件
    ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk);
    //四：合并分块文件并将数据写入MongoDB
    ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);
}
