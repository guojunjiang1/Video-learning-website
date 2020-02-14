package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController//相当于Controller和ResponseBody
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private CmsPageService cmsPageService;

    //当用户拥有该权限时才可以访问该方法
    @PreAuthorize("hasAuthority('cms_find_list')")
    @Override
    @GetMapping("/list/{page}/{size}")//相当于get请求的requestMapping
    //分页查询页面
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return cmsPageService.findList(page,size,queryPageRequest);
    }

    //当用户拥有该权限时才可以访问该方法
    @PreAuthorize("hasAuthority('cms_add')")
    @Override
    @PostMapping("/add")
    //新建页面
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return cmsPageService.save(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    //根据id查询页面
    public CmsPage findById(@PathVariable("id") String id) {
        return cmsPageService.findById(id);
    }

    @Override
    @PutMapping("/edit/{id}")
    //修改页面
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return cmsPageService.edit(id,cmsPage);
    }

    @Override
    @DeleteMapping("/del/{id}")
    //删除页面
    public ResponseResult delete(@PathVariable("id") String id) {
        return cmsPageService.delete(id);
    }

    @Override
    @PostMapping("/save")
    //保存页面
    public CmsPageResult up(@RequestBody CmsPage cmspage) {
        return cmsPageService.up(cmspage);
    }
}
