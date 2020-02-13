package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AuthController implements AuthControllerApi {
    @Autowired
    private AuthService authService;
    @Value("${auth.clientId}")
    private String clientId;//要申请令牌所属的工程id
    @Value("${auth.clientSecret}")
    private String clientSecret;//要申请令牌所属的工程密码
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;

    @Override
    @PostMapping("/userlogin")
    //用户登录
    public LoginResult login(LoginRequest loginRequest) {
        if(loginRequest == null || StringUtils.isEmpty(loginRequest.getUsername())){
            ExceptionCast.cast(AuthCode.AUTH_USERNAME_NONE);
        }
        if(StringUtils.isEmpty(loginRequest.getPassword())){
            ExceptionCast.cast(AuthCode.AUTH_PASSWORD_NONE);
        }
        String username = loginRequest.getUsername();//用户名
        String password = loginRequest.getPassword();//密码
        //String verifycode = loginRequest.getVerifycode();//验证码
        //一：获取令牌并存入redis
        AuthToken authToken=authService.login(username,password,clientId,clientSecret);
        String access_token = authToken.getAccess_token();//身份令牌
        //二：将身份令牌存入cookie
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",access_token,cookieMaxAge,false);
        return new LoginResult(CommonCode.SUCCESS,access_token);
    }

    @Override
    @PostMapping("/userlogout")
    //用户退出
    public ResponseResult logout() {
        //一：清除redis里的信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        String access_token=null;//获取Cookie里的用户令牌作为Redis的Key
        if(map!=null && map.get("uid")!=null){
            access_token = map.get("uid");
        }
        if (access_token==null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        authService.clearRedis(access_token);//清除redis里的信息
        //二：清除Cookie
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response,cookieDomain,"/","uid",access_token,0,false);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    @GetMapping("/userjwt")
    //根据cookie信息从Redis获取JWT令牌
    public JwtResult userjwt() {
        //一：获取Cookie中的信息
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> map = CookieUtil.readCookie(request, "uid");
        String access_token=null;
        if(map!=null && map.get("uid")!=null){
            access_token = map.get("uid");
        }
        if (access_token==null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        //二：从Redis获取令牌信息
        AuthToken authToken=authService.getUserToken(access_token);
        if (authToken==null){
            return new JwtResult(CommonCode.FAIL,null);
        }
        String jwt_token = authToken.getJwt_token();//获取JWT令牌
        return new JwtResult(CommonCode.SUCCESS,jwt_token);
    }
}
