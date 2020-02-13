package com.xuecheng.manage_cms_client.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;

import java.io.InputStream;

public interface PageService {
    void savePageToServerPath(String pageId);//从gridFs中下载页面保存到服务器中
    CmsPage findById(String pageId);//根据id查询页面
    CmsSite findById1(String siteId);//根据id查询站点
    InputStream getFileById(String htmlFileId);//根据页面文件id获取发布页面的输入流
}
