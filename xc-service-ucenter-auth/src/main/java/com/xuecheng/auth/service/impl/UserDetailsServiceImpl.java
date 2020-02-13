package com.xuecheng.auth.service.impl;

import com.xuecheng.auth.client.UcenterClient;
import com.xuecheng.auth.service.UserJwt;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//处理接收到申请令牌请求，自动生成令牌(密码授权方式，先验证用户信息)
//验证用户登录信息(SpringSecurity框架根据用户名自动验证)
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    UcenterClient ucenterClient;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //密码
                String clientSecret = clientDetails.getClientSecret();
                return new User(username,clientSecret,AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        //远程调用Ucenter，根据用户名从数据库获取用户信息
        XcUserExt userext = ucenterClient.getUserext(username);
        if(userext == null){
            //用户不存在
            return null;
        }
        //取出正确密码（hash值）
        String password = userext.getPassword();

        //从数据库获取权限
        List<XcMenu> permissions = userext.getPermissions();
        if (permissions==null){
            permissions=new ArrayList<>();
        }
        List<String> user_permission = new ArrayList<>();
        permissions.forEach(item-> user_permission.add(item.getCode()));
        String user_permission_string  = StringUtils.join(user_permission.toArray(), ",");

        //SpringSecurity框架自动校验用户密码是否正确，并赋权限
        UserJwt userDetails = new UserJwt(username,//账号
                password,//密码
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));//权限

        userDetails.setId(userext.getId());
        userDetails.setUtype(userext.getUtype());//用户类型
        userDetails.setCompanyId(userext.getCompanyId());//所属企业
        userDetails.setName(userext.getName());//用户名称
        userDetails.setUserpic(userext.getUserpic());//用户头像
        //当用户密码校验正确时，该返回值会自动生成令牌，其中jwt令牌包含了用户的信息及权限
        return userDetails;
    }
}
