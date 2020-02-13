package com.xuecheng.manage_media_process;

import com.xuecheng.framework.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder调用第三方程序 {

    //使用processBuilder来调用第三方应用程序
    @Test
    public void testProcessBuilder() throws IOException {
        //创建processBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        //设置第三方应用程序的命令
//      processBuilder.command("ping","127.0.0.1");
        processBuilder.command("ipconfig");

        //将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        //启动一个进程
        Process process = processBuilder.start();

        //通过标准输入流来拿到正常和错误的信息
        InputStream inputStream = process.getInputStream();

        //转成字符流
        InputStreamReader reader = new InputStreamReader(inputStream,"gbk");
        //缓冲
        char[] chars = new char[1024];
        int len = -1;
        while ((len = reader.read(chars))!=-1){
            String string = new String(chars,0,len);
            System.out.println(string);
        }
        inputStream.close();
        reader.close();

    }

    @Test
    //调用第三方FFmpeg将avi视屏转为mp4
    public void testFFmpeg() throws IOException {
        //创建processBuilder对象
        ProcessBuilder processBuilder = new ProcessBuilder();
        //设置第三方应用程序的命令
        List<String> command = new ArrayList<>();
        command.add("F:\\FFmpeg\\bin\\ffmpeg.exe");//ffmpeg的路径
        command.add("-i");
        command.add("F:\\FFmpeg\\bin\\lucene.avi");//要修改视屏的源路径
        command.add("-y");//覆盖输出文件
        command.add("-c:v");
        command.add("libx264");
        command.add("-s");
        command.add("1280x720");
        command.add("-pix_fmt");
        command.add("yuv420p");
        command.add("-b:a");
        command.add("63k");
        command.add("-b:v");
        command.add("753k");
        command.add("-r");
        command.add("18");
        command.add("F:\\FFmpeg\\bin\\lucene.mp4");//要修改视屏的目标路径
        processBuilder.command(command);

        //将标准输入流和错误流合并
        processBuilder.redirectErrorStream(true);
        //启动一个进程
        Process process = processBuilder.start();

        //通过标准输入流来拿到正常和错误的信息
        InputStream inputStream = process.getInputStream();

        //转成字符流
        InputStreamReader reader = new InputStreamReader(inputStream,"gbk");
        //缓冲
        char[] chars = new char[1024];
        int len = -1;
        while ((len = reader.read(chars))!=-1){
            String string = new String(chars,0,len);
            System.out.println(string);
        }
        inputStream.close();
        reader.close();

    }

    @Test
    //使用工具类实现将avi视屏转为mp4(工具类内部封装了调用FFmpeg)
    public void testMp4VideoUtil(){
        String ffmpeg_path = "F:\\FFmpeg\\bin\\ffmpeg.exe";//指定ffmpeg地址
        String video_path = "F:\\FFmpeg\\bin\\lucene.avi";//源视屏地址
        String mp4_name = "1.mp4";//目标视屏名称
        String mp4folder_path = "F:\\FFmpeg\\bin\\";//目标视屏目录
        //调用执行转换
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        //生成mp4
        String result = mp4VideoUtil.generateMp4();
        System.out.println(result);
    }
}
