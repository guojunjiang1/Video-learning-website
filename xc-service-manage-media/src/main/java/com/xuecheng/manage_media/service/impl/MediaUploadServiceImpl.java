package com.xuecheng.manage_media.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitmqConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class MediaUploadServiceImpl implements MediaUploadService {
    @Value("${xc-service-manage-media.upload-location}")
    private String upload_location;//文件上传子目录
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    String routingkey_media_video;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /*
     * 文件上传的目录规则：子目录+一级目录+二级目录+三级目录
     * 文件的路径：子目录+一级目录+二级目录+三级目录+文件名
     * 文件块的目录：子目录+一级目录+二级目录+三级目录+chunk
     * 文件块的路径：子目录+一级目录+二级目录+三级目录+chunk+块名称
     * 一级目录：fileMd5的第一个字母
     * 二级目录：fileMd5的第二个字母
     * 三级目录：fileMd5
     * 文件名：fileMd5+文件扩展名fileExt
     * */

    //得到文件上传的目录
    private String getFileFolderPath(String fileMd5){
        return upload_location + fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/";
    }
    //得到文件的路径
    private String getFilePath(String fileMd5,String fileExt){
        return this.getFileFolderPath(fileMd5)+ fileMd5 + "." +fileExt;
    }
    //得到块文件的目录
    private String getFilePath1(String fileMd5){
        return this.getFileFolderPath(fileMd5)+"chunk/";
    }

    @Override
    //一：查看文件是否存在
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //一：查看本地路径中是否存在
        String filePath = getFilePath(fileMd5, fileExt);//文件的路径
        File a=new File(filePath);
        //二：查看MongoDB数据库中是否存在
        Optional<MediaFile> byId = mediaFileRepository.findById(fileMd5);
        if (a.exists()&&byId.isPresent()){
           //文件存在
           ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }
        //文件不存在
        String fileFolderPath = getFileFolderPath(fileMd5);//上传文件的目录
        File b=new File(fileFolderPath);
        if (!b.exists()){
            //目录不存在，先创建目录
            b.mkdirs();
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //二：查看块文件是否存在
    public CheckChunkResult checkchunk(String fileMd5, Integer chunk, Integer chunkSize) {
        //一：查看块文件路径是否存在
        String filePath1 = this.getFilePath1(fileMd5);//块文件目录
        File file=new File(filePath1+chunk);//块文件路径
        if (file.exists()){
            //存在
            return new CheckChunkResult(CommonCode.SUCCESS,true);
        }else {
            //不存在
            //创建目录
            File file1=new File(filePath1);
            if (!file1.exists()){
                file1.mkdirs();
            }
            return new CheckChunkResult(CommonCode.SUCCESS,false);
        }
    }

    @Override
    //三：上传块文件
    public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
        InputStream inputStream=null;
        FileOutputStream outputStream=null;
        try {
            //获取块文件的输入流
            inputStream = file.getInputStream();
            //获取块文件的输出流
            outputStream=new FileOutputStream(new File(this.getFilePath1(fileMd5)+chunk));
            IOUtils.copy(inputStream,outputStream);
        } catch (Exception e) {
            return new ResponseResult(MediaCode.MERGE_FILE_FAIL1);
        } finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //四：合并分块文件
    public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        //一：合并分块文件
        String filePath1 = this.getFilePath1(fileMd5);//分块文件目录
        File file=new File(filePath1);
        File[] files = file.listFiles();//得到所有分块文件
        List<File> files1 = Arrays.asList(files);
        String filePath = this.getFilePath(fileMd5, fileExt);//得到合并文件的路径
        File file1=new File(filePath);
        File file2=this.mergeFile(files1, file1);//执行合并
        if (file2==null){
            //合并文件失败
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL2);
        }
        //二：校验合并后的文件的正确性
        boolean flag=checkFileMd5(file2,fileMd5);
        if (!flag){
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
        }
        //三：将文件信息写入MongoDB
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileOriginalName(fileName);
        mediaFile.setFileName(fileMd5 + "." +fileExt);
        //文件保存的目录
        String filePath2 = fileMd5.substring(0,1) + "/" + fileMd5.substring(1,2) + "/" + fileMd5 + "/";
        mediaFile.setFilePath(filePath2);
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        mediaFileRepository.save(mediaFile);
        //四：向rabbitMQ发送消息(处理视屏)
        goRabbitMq(fileMd5);
        return new ResponseResult(CommonCode.SUCCESS);
    }


    //执行合并
    private File mergeFile(List<File> list, File file1) {
        if (file1.exists()){
            //如果合并文件已存在则删除
            file1.delete();
        }
        //目标路径
        RandomAccessFile raf_write=null;
        //源路径
        RandomAccessFile raf_read=null;
        try {
            raf_write = new RandomAccessFile(file1,"rw");
            Collections.sort(list, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return Integer.parseInt(o1.getName())-Integer.parseInt(o2.getName());
                }
            });
            byte bytes[]=new byte[1024];
            for (File xx:list){
                raf_read = new RandomAccessFile(xx,"r");
                int i=-1;
                while ((i=raf_read.read(bytes))!=-1){
                    raf_write.write(bytes,0,i);
                }
                raf_read.close();
            }
            return file1;
        } catch (Exception e) {
            return null;
        }finally {
            if (raf_write!=null){
                try {
                    raf_write.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //校验文件的Md5
    private boolean checkFileMd5(File file2, String fileMd5) {
        InputStream inputStream=null;
        try {
            inputStream=new FileInputStream(file2);
            String s = DigestUtils.md5Hex(inputStream);
            if (s.equalsIgnoreCase(fileMd5)){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //向RabbitMQ发送消息，通知处理视屏文件
    private void goRabbitMq(String fileMd5) {
        Optional<MediaFile> byId = mediaFileRepository.findById(fileMd5);
        if (!byId.isPresent()){
            ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL3);
        }
        Map<String,String> map=new HashMap<>();
        map.put("mediaId",fileMd5);
        String s = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,s);
    }
}
