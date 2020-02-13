package com.xuecheng.auth.service;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Data
@ToString
public class UserJwt extends User {

    private String id;//用户id
    private String name;//用户名称
    private String userpic;//用户头像
    private String utype;//用户类型
    private String companyId;//用户所属公司

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

}
