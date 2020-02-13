package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPagePostControllerApi;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPagePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//页面发布
@RestController//相当于Controller和ResponseBody
@RequestMapping("/cms/page")
public class CmsPagePostController implements CmsPagePostControllerApi {
    @Autowired
    private CmsPagePostService cmsPagePostService;
    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable(value = "pageId") String pageId) {
        return cmsPagePostService.post(pageId);
    }
}
