package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPagePreviewControllerApi;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPagePreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

//页面预览的Controller，预览的是静态化后的页面
@Controller
@RequestMapping("/cms/preview")
public class CmsPagePreviewController extends BaseController implements CmsPagePreviewControllerApi {
    @Autowired
    private CmsPagePreviewService cmsPagePreviewService;
    @Override
    @RequestMapping(value = "/{pageId}",method = RequestMethod.GET)
    public void preview(@PathVariable(value = "pageId") String pageId) throws IOException {
        String pageHtml = cmsPagePreviewService.getPageHtml(pageId);
        response.setHeader("Content-type","text/html;charset=utf-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pageHtml.getBytes("utf-8"));
    }
}
