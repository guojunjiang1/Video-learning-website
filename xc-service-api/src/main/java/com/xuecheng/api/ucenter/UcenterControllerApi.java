package com.xuecheng.api.ucenter;


import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value="用户中心",description = "管理用户中心")
public interface UcenterControllerApi {
    @ApiOperation(value = "根据用户名称获取用户信息")
    XcUserExt getUserext(String username);

    @ApiOperation(value = "注册用户")
    ResponseResult rgUser(String username,String password);
}
