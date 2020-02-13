package com.xuecheng.filesystem.service;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {
    UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata);//上传图片，并保存图片信息
}
