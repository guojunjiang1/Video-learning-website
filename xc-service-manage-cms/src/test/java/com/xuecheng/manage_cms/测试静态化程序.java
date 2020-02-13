package com.xuecheng.manage_cms;

import com.xuecheng.manage_cms.service.CmsPagePreviewService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class 测试静态化程序 {
    @Autowired
    private CmsPagePreviewService cmsPagePreviewService;
    @Test
    public void test() throws Exception{
        String pageHtml = cmsPagePreviewService.getPageHtml("5dfee0a0f94c402e0095b772");
        System.out.println(pageHtml);
    }
}
