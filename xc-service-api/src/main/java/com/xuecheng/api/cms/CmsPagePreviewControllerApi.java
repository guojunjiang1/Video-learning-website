package com.xuecheng.api.cms;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;

@Api(value="cms页面管理接口",description = "页面预览")
public interface CmsPagePreviewControllerApi {
    @ApiOperation("页面预览功能，显示静态化页面")//修饰方法的意思
    @ApiImplicitParams({
            //描述参数:paramType表通过什么方式获取的，path表url路径
            @ApiImplicitParam(name = "pageId",value = "页面id",required = true,paramType = "path",dataType = "String"),
    })
    //页面预览
    void preview(String pageId) throws IOException;
}
