package com.xuecheng.manage_cms.controller;

import com.netflix.discovery.converters.Auto;
import com.xuecheng.api.cms.CmsPagePostOneControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.manage_cms.service.CmsPagePostOneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cms/page")
public class CmsPagePostOneController implements CmsPagePostOneControllerApi {
    @Autowired
    private CmsPagePostOneService cmsPagePostOneService;
    @Override
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return cmsPagePostOneService.postPageQuick(cmsPage);
    }
}
