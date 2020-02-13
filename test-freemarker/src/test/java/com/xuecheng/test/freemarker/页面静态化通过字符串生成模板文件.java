package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 页面静态化通过字符串生成模板文件 {
    @Test
    public void test() throws Exception {
        //1:定义配置类
        Configuration configuration=new Configuration(Configuration.getVersion());
        //2:获取模板(通过字符串生成模板)
        String templateString="" +
                "<html>\n" +
                "    <head></head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" +
                "</html>";
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template",templateString);
        configuration.setTemplateLoader(stringTemplateLoader);
        Template template = configuration.getTemplate("template","utf-8");
        //3:获取数据
        Map map = getMap();
        //4:静态化
        String string = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        //5:输出html文件
        InputStream inputStream = IOUtils.toInputStream(string);
        FileOutputStream outputStream=new FileOutputStream(new File("F:\\IDEA\\静态化页面\\test2.html"));
        IOUtils.copy(inputStream,outputStream);
        inputStream.close();
        outputStream.close();
    }

    public Map getMap(){
        Map<String,Object> map=new HashMap<>();
        map.put("name","郭峻江");
        return map;
    }
}
