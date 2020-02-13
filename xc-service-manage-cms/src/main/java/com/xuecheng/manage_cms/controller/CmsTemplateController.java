package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsTemplateControllerApi;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/template")
public class CmsTemplateController implements CmsTemplateControllerApi {
    @Autowired
    private CmsTemplateService  cmsTemplateService;
    @Override
    @GetMapping("/list")//相当于get请求的requestMapping
    public QueryResponseResult findTemplate() {
        return cmsTemplateService.findTemplate();
    }
}
