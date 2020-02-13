package com.xuecheng.govern.gateway.service;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
    String getJwtHeader(HttpServletRequest request);//判断请求头是否有jwt令牌

    String getCookie(HttpServletRequest request);//判断Cookie中是否有用户令牌

    long getExpire(String user);//判断Redis中令牌信息是否过期
}
