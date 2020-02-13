package com.xuecheng.govern.gateway.service.impl;

import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    //判断请求头是否有jwt令牌
    public String getJwtHeader(HttpServletRequest request) {
        String s = request.getHeader("Authorization");
        if (StringUtils.isEmpty(s)){
            return null;
        }
        if (!s.startsWith("Bearer ")){
            return null;
        }
        return s.substring(7);
    }

    @Override
    //判断Cookie中是否有用户令牌
    public String getCookie(HttpServletRequest request) {
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        String access_token=null;
        if(map!=null && map.get("uid")!=null){
            access_token = map.get("uid");
        }
        return access_token;
    }

    @Override
    //判断Redis中令牌信息是否过期
    public long getExpire(String user) {
        String key="user_token:"+user;
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire;
    }
}
