package com.xuecheng.filesystem.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.apache.commons.lang3.StringUtils;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
//关于系统文件的(上传图片)
public class FileSystemServiceImpl implements FileSystemService {
    //注入FastDFS连接信息
    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;
    @Autowired
    private FileSystemRepository fileSystemRepository;

    @Override
    //上传图片，并保存图片信息
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
        /*
        * 分为两步：第一步上传图片，第二步保存图片信息
        * */
        if (multipartFile==null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }
        //第一步上传图片到FastDFS，并返回FileId
        String fileId=fdfs_upload(multipartFile);
        if (!StringUtils.isNotEmpty(fileId)){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        //第二步保存图片信息
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        if (StringUtils.isNotEmpty(filetag)) {
            fileSystem.setFiletag(filetag);
        }
        if (StringUtils.isNotEmpty(businesskey)) {
            fileSystem.setBusinesskey(businesskey);
        }
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        if(StringUtils.isNotEmpty(metadata)){
            try {
                //metedata传递的是一个JSON字符串
                Map map = JSON.parseObject(metadata, Map.class);
                fileSystem.setMetadata(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        fileSystemRepository.save(fileSystem);
        return new UploadFileResult(CommonCode.SUCCESS,fileSystem);
    }


    //上传图片
    private String fdfs_upload(MultipartFile multipartFile) {
        //初始化FastDFS环境
        initFdfsConfig();
        //创建trackerClient
        TrackerClient trackerClient = new TrackerClient();
        try {
            TrackerServer trackerServer = trackerClient.getConnection();
            //得到storage服务器
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建storageClient来上传文件
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            //上传文件
            //得到文件字节
            byte[] bytes = multipartFile.getBytes();
            //得到文件的原始名称
            String originalFilename = multipartFile.getOriginalFilename();
            //得到文件扩展名
            String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            String fileId = storageClient1.upload_file1(bytes, ext, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //初始化FastDFS环境
    private void initFdfsConfig() {
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
        } catch (Exception e) {
            e.printStackTrace();
            //抛出异常
            ExceptionCast.cast(FileSystemCode.FS_INITFDFSERROR);
        }
    }
}
