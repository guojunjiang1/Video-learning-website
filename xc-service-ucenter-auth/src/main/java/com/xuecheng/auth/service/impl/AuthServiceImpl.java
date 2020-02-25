package com.xuecheng.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.xuecheng.auth.client.UcenterClient;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.learning.respoonse.LearningCode;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.AuthCode;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.domain.ucenter.response.UcenterCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UcenterClient ucenterClient;
    @Value("${auth.tokenValiditySeconds}")
    private int tokenValiditySeconds;
    @Override
    //用户登录
    //获取令牌，将令牌存入redis
    public AuthToken login(String username, String password, String clientId, String clientSecret) {
        //一：密码模式获取令牌
        AuthToken authToken = this.getAuthToken(username, password, clientId, clientSecret);
        if (authToken==null){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        //二：将令牌存入redis
        String access_token = authToken.getAccess_token();//身份令牌(用户的)
        String value = JSON.toJSONString(authToken);//所有令牌信息转为JSON格式
        String key="user_token:"+access_token;
        //存储到redis，key是用户的身份令牌，value是所有令牌信息，存储时间为配置文件中配置的(单位是秒)
        stringRedisTemplate.boundValueOps(key).set(value,tokenValiditySeconds, TimeUnit.SECONDS);
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        if (expire<0){
            ExceptionCast.cast(AuthCode.AUTH_LOGIN_TOKEN_SAVEFAIL);
        }
        return authToken;
    }
    //密码模式获取令牌（顺便校验账号密码）
    private AuthToken getAuthToken(String username, String password, String clientId, String clientSecret) {
        //一：获取远程调用的地址
        //从eureka中获取认证服务的地址（因为spring security在认证服务中）
        ServiceInstance serviceInstance = loadBalancerClient.choose(XcServiceList.XC_SERVICE_UCENTER_AUTH);
        //此地址就是http://ip:port
        URI uri = serviceInstance.getUri();
        //令牌申请的地址 http://localhost:40400/auth/oauth/token（也就是UserDetailsServiceImpl）
        String authUrl = uri + "/auth/oauth/token";
        //二：配置远程调用的请求头和请求体
        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId,clientSecret);
        header.add("Authorization", httpBasic);
        //定义body
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);

        //设置restTemplate远程调用时候
        //对400和401不让报错（当账号密码输入错误时，springSecurityOauth2返回400或401），正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });
        //三：获取令牌信息,调用UserDetailServiceImpl
        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);
        Map bodyMap = exchange.getBody();
        //解析UserDetailServiceImpl返回的错误信息
        if(bodyMap!=null && bodyMap.containsKey("error_description")){
            String error_description = (String) bodyMap.get("error_description");
            if(error_description.contains("UserDetailsService returned null")){
                ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);//账号不存在
            }else if(error_description.contains("坏的凭证")){
                ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);//密码错误
            }
        }
        //账号密码没错，系统自动获取令牌失败
        if(bodyMap == null ||
                bodyMap.get("access_token") == null ||
                bodyMap.get("refresh_token") == null ||
                bodyMap.get("jti") == null){
            return null;
        }
        //账号密码正确，获取令牌成功
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) bodyMap.get("jti"));//用户身份令牌
        authToken.setRefresh_token((String) bodyMap.get("refresh_token"));//刷新令牌
        authToken.setJwt_token((String) bodyMap.get("access_token"));//jwt令牌
        return authToken;
    }

    //获取请求头httpbasic的串
    private String getHttpBasic(String clientId,String clientSecret){
        String string = clientId+":"+clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }

    @Override
    //根据用户令牌，获取redis中的令牌信息
    public AuthToken getUserToken(String access_token) {
        //从redis中取到令牌信息
        String key = "user_token:" + access_token;
        String value = stringRedisTemplate.opsForValue().get(key);
        try{
            AuthToken authToken = JSON.parseObject(value, AuthToken.class);
            return authToken;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    //根据用户令牌，清除redis中的令牌信息
    public void clearRedis(String access_token) {
        String key = "user_token:" + access_token;
        stringRedisTemplate.delete(key);
    }

    @Override
    //注册用户
    public ResponseResult userrg(LoginRequest loginRequest) {
        if (loginRequest==null|| StringUtils.isEmpty(loginRequest.getUsername())||StringUtils.isEmpty(loginRequest.getPassword())){
            return new ResponseResult(CommonCode.INVALID_PAPAM);
        }
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        ResponseResult responseResult = ucenterClient.rgUser(username, password);
        return responseResult;
    }
}
