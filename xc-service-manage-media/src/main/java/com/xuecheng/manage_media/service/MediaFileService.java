package com.xuecheng.manage_media.service;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;

public interface MediaFileService {
    //分页查询所有媒资文件
    QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest);
}
