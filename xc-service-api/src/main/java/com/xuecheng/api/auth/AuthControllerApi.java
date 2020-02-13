package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "用户登录，认证",description = "用户登录，认证接口")
public interface AuthControllerApi {
    @ApiOperation("登录")
    LoginResult login(LoginRequest loginRequest);
    @ApiOperation("退出")
    ResponseResult logout();
    @ApiOperation("根据Cookie信息获取jwt令牌")
    JwtResult userjwt();
}
