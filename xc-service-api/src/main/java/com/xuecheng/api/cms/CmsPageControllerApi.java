package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

//Swagger中的注解：描述整个类的意思
@Api(value="cms页面管理接口",description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
    @ApiOperation("分页查询加条件查询页面列表")//修饰方法的意思
    @ApiImplicitParams({
            //描述参数:paramType表通过什么方式获取的，path表url路径
            @ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",value = "每页记录",required = true,paramType = "path",dataType = "int")
    })
    //分页查询页面
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("新建页面")
    //新建页面
    CmsPageResult save(CmsPage cmsPage);

    @ApiOperation("根据页面Id查询页面")
    //根据id查询页面
    CmsPage findById(String id);

    @ApiOperation("修改页面")
    //修改页面
    CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("删除页面")
    //删除页面
    ResponseResult delete(String id);

    @ApiOperation("Course工程调用保存页面")
    CmsPageResult up(CmsPage cmspage);
}
