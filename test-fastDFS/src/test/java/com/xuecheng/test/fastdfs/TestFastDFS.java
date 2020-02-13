package com.xuecheng.test.fastdfs;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    //上传文件
    @Test
    public void testUpload(){

        try {
            //加载fastDfs-client.properties配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义TrackerClient，用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Stroage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建stroageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            //向stroage服务器上传文件
            //本地文件的路径
            String filePath = "d:/图片背景2.jpg";
            //上传成功后拿到文件Id
            String fileId = storageClient1.upload_file1(filePath, "jpg", null);
            //group1/M00/00/00/wKgBb14fwAOAX0ubAATJRtrzV70096.jpg
            System.out.println(fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //下载文件
    @Test
    public void testDownload(){
        FileOutputStream fileOutputStream=null;
        try {
            //加载fastdfs-client.properties配置文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            //定义TrackerClient，用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            //连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取Stroage
            StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
            //创建stroageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storeStorage);
            //下载文件
            //文件id
            String fileId="group1/M00/00/00/wKgBb14fmUmADWGSAAAABPr5c5o333.log";
            byte[] bytes = storageClient1.download_file1(fileId);
            //使用输出流保存文件
            fileOutputStream = new FileOutputStream(new File("d:/springboot1.log"));
            fileOutputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
