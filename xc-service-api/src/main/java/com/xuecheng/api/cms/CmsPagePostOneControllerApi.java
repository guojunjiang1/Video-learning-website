package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description = "页面一键发布")
public interface CmsPagePostOneControllerApi {
    @ApiOperation("页面一键发布")//修饰方法的意思
    //页面一键发布
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
