package com.xuecheng.manage_media_process.service.impl;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import com.xuecheng.manage_media_process.service.MediaFileProcessTaks;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaFileProcessTasksImpl implements MediaFileProcessTaks {
    @Autowired
    private MediaFileRepository mediaFileRepository;
    @Value("${xc-service-manage-media.video-location}")
    private String video_location;//视屏存放的目录
    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpeg_path;//ffmpeg存放的路径
    @Override
    //处理视屏将视屏转为ts和m3u8文件
    public void taks(String mediaId) {
        if (!StringUtils.isNotEmpty(mediaId)){
            return;
        }
        //一：根据文件id获取视屏的信息
        Optional<MediaFile> byId = mediaFileRepository.findById(mediaId);
        if (!byId.isPresent()){
            return;
        }
        MediaFile mediaFile = byId.get();
        //二：查看视屏的类型(只能处理avi类型和mp4类型)
        String fileType = mediaFile.getFileType();
        if (fileType.equals("mp4")){
            //如果是mp4则不需要再转换了
            mediaFile.setProcessStatus("303001");//处理中
            mediaFileRepository.save(mediaFile);
        }else if (fileType.equals("avi")){
            //三：将视屏avi转为mp4类型(通过ProcessBuilder调用第三方工具ffmpeg进行转换)
            mediaFile.setProcessStatus("303001");//处理中
            mediaFileRepository.save(mediaFile);
            String ffmpeg_path1 = ffmpeg_path;//指定ffmpeg地址
            String video_path = video_location+mediaFile.getFilePath()+mediaFile.getFileName();//源视屏地址
            String mp4_name = mediaFile.getFileId()+".mp4";//目标视屏名称
            String mp4folder_path = video_location+mediaFile.getFilePath();//目标视屏目录
            //调用执行转换
            Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path1,video_path,mp4_name,mp4folder_path);
            //生成mp4
            String result = mp4VideoUtil.generateMp4();
            if (result==null||(!"success".equals(result))){
                //处理失败
                mediaFile.setProcessStatus("303003");
                //定义mediaFileProcess_m3u8
                MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
                //记录失败原因
                mediaFileProcess_m3u8.setErrormsg(result);
                mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
                mediaFileRepository.save(mediaFile);
                return ;
            }
        }else {
            //不是avi和mp4类型
            mediaFile.setProcessStatus("303004");//无法处理
            mediaFileRepository.save(mediaFile);
            return ;
        }
        //第四步：将mp4类型转为m3u8和ts文件，并将信息保存到数据库
        String mp4_video_path=video_location+mediaFile.getFilePath()+mediaFile.getFileId()+".mp4";//源视屏地址
        String m3u8_name=mediaFile.getFileId()+".m3u8";//目标视屏名称
        String m3u8folder_path=video_location+mediaFile.getFilePath()+"hls/";//目标视屏目录
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg_path,mp4_video_path,m3u8_name,m3u8folder_path);
        //生成m3u8和ts文件
        String tsResult = hlsVideoUtil.generateM3u8();
        if (tsResult==null||(!"success".equals(tsResult))){
            //处理失败
            mediaFile.setProcessStatus("303003");
            //定义mediaFileProcess_m3u8
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            //记录失败原因
            mediaFileProcess_m3u8.setErrormsg(tsResult);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return ;
        }
        mediaFile.setProcessStatus("303002");//处理成功

        MediaFileProcess_m3u8 mediaFileProcess_m3u8=new MediaFileProcess_m3u8();
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);//ts列表信息

        String fileUrl=mediaFile.getFilePath()+"hls/"+mediaFile.getFileId()+".m3u8";
        mediaFile.setFileUrl(fileUrl);//m3u8文件的相对路径
        mediaFileRepository.save(mediaFile);
    }
}
