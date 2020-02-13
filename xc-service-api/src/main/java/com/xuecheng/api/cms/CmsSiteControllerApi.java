package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

//Swagger中的注解：描述整个类的意思
@Api(value="cms站点管理接口",description = "cms站点管理接口，提供页面的增、删、改、查")
public interface CmsSiteControllerApi {
    @ApiOperation("查询所有站点信息")
    //查询所有站点
    QueryResponseResult findSite();
}
