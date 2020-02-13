package com.xuecheng.manage_media_process.service;

import org.springframework.stereotype.Service;

@Service
public interface MediaFileProcessTaks {
    void taks(String mediaId);//处理视屏，将视屏转为m3u8和ts文件
}
