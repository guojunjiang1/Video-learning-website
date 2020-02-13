package com.xuecheng.manage_media.service.impl;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import com.xuecheng.manage_media.service.MediaFileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class MediaFileServiceImpl implements MediaFileService {
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Override
    //分页查询所有媒资文件
    public QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {
        if (page<=0){
            page=1;
        }
        page-=1;
        if (size<=0){
            size=10;
        }
        MediaFile mediaFile=new MediaFile();
        //匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        if (queryMediaFileRequest!=null){
            if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())){
                //精准匹配文件状态码
                mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
            }
            if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())){
                //模糊匹配页面原始名称
                mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
                matching=matching.withMatcher("fileOriginalName", ExampleMatcher.GenericPropertyMatchers.contains());
            }
            if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())){
                //模糊匹配页面标签
                mediaFile.setTag(queryMediaFileRequest.getTag());
                matching=matching.withMatcher("tag", ExampleMatcher.GenericPropertyMatchers.contains());
            }
        }
        //动态查询对象
        Example<MediaFile> example=Example.of(mediaFile,matching);
        //分页查询对象
        PageRequest of = PageRequest.of(page, size);
        //执行查询
        Page<MediaFile> all = mediaFileRepository.findAll(example, of);
        QueryResult<MediaFile> queryResult=new QueryResult<>();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
    }
}
