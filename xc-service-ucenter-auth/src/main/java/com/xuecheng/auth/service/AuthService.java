package com.xuecheng.auth.service;

import com.xuecheng.framework.domain.ucenter.ext.AuthToken;

public interface AuthService {
    //获取令牌，将令牌存入redis
    AuthToken login(String username, String password, String clientId, String clientSecret);
    //根据用户令牌，获取redis中的令牌信息
    AuthToken getUserToken(String access_token);
    //根据用户令牌，清除redis中的令牌信息
    void clearRedis(String access_token);
}
