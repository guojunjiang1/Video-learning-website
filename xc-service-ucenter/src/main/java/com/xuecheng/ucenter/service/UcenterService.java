package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;

public interface UcenterService {
    XcUserExt getUserext(String username);//根据用户名，获取用户信息
}
