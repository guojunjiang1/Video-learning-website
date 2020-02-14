package com.xuecheng.govern.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.govern.gateway.service.AuthService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

//判断是否登录的过滤器
@Component
public class LoginFilter extends ZuulFilter {
    @Override
    public String filterType() {
        /*
        * 配置过滤器的类型
        * pre：过滤器在被路由之前执行
        * routing：过滤器在路由时执行
        * post：过滤器在routing和error过滤器之后执行
        * error：过滤器在处理请求时发生错误执行
        * */
        return "pre";
    }

    //过滤器序号，越小越优先执行
    @Override
    public int filterOrder() {
        return 0;
    }

    //返回true表示开启此过滤器
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    private AuthService authService;
    //过滤器的内容
    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();//Zuul网关提供的
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        //一：判断请求头是否有jwt令牌
        String jwt=authService.getJwtHeader(request);
        if (jwt==null){
            //拒绝访问
            this.access_denied(requestContext,response);
            return null;
        }
        //二：判断Cookie中是否有用户令牌
        String user=authService.getCookie(request);
        if (user==null){
            this.access_denied(requestContext,response);
            return null;
        }
        //三：判断Redis中令牌信息是否过期
        long a=authService.getExpire(user);
        if (a<0){
            this.access_denied(requestContext,response);
            return null;
        }
        return null;
    }

    //拒绝访问
    private void access_denied(RequestContext requestContext,HttpServletResponse response){
        requestContext.setSendZuulResponse(false);//拒绝访问
        requestContext.setResponseStatusCode(200);//设置响应代码
        //响应信息
        ResponseResult responseResult=new ResponseResult(CommonCode.UNAUTHENTICATED);
        String s = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(s);
        //设置响应的类型
        response.setContentType("application/json;charset=utf-8");
    }
}
