package com.xuecheng.api.cms;

import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value="cms页面管理接口",description = "页面发布")
public interface CmsPagePostControllerApi {
    @ApiOperation("页面发布")//修饰方法的意思
    @ApiImplicitParams({
            //描述参数:paramType表通过什么方式获取的，path表url路径
            @ApiImplicitParam(name = "pageId",value = "页面id",required = true,paramType = "path",dataType = "String"),
    })
    //页面发布
    ResponseResult post(String pageId);
}
