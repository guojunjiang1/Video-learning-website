package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.model.response.ResponseResult;

public interface UcenterService {
    XcUserExt getUserext(String username);//根据用户名，获取用户信息

    ResponseResult rgUser(String username, String password);//注册用户
}
