package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 文件分块和合并的练习 {
    //文件分块
    @Test
    public void test1() throws Exception {
        String name1="F:\\WebStorm\\static\\video\\lucene.mp4";//源文件
        String name2="F:\\WebStorm\\static\\video\\test";//目标文件夹
        File a=new File(name1);
        File b=new File(name2);
        if (!b.exists()){
            b.mkdirs();
        }
        byte[] bytes=new byte[1024*1024];
        FileInputStream fileInputStream=new FileInputStream(a);
        int i=-1;
        int count=0;
        while((i=fileInputStream.read(bytes))!=-1){
            FileOutputStream fileOutputStream=new FileOutputStream(b+"\\"+count++);
            fileOutputStream.write(bytes,0,i);
            fileOutputStream.close();
        }
        fileInputStream.close();;
    }
    //合并文件
    @Test
    public void test2() throws Exception {
        String name1="F:\\WebStorm\\static\\video\\test";//源文件
        String name2="F:\\WebStorm\\static\\video\\lucene1.mp4";//目标文件夹
        File a=new File(name1);
        File b=new File(name2);
        List<FileInputStream> list=new ArrayList<>();
        for (int i=0;i<28;i++){
            list.add(new FileInputStream(a+"\\"+i));
        }
        Enumeration<FileInputStream> v= Collections.enumeration(list);
        //源
        SequenceInputStream z=new SequenceInputStream(v);
        //目的
        FileOutputStream k=new FileOutputStream(b);
        byte []bytes=new byte[100];
        int i=-1;
        while((i=z.read(bytes))!=-1){
            k.write(bytes,0,i);
        }
        k.close();
        z.close();
    }
}
