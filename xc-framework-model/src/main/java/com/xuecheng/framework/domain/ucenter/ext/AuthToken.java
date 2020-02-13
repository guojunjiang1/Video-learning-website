package com.xuecheng.framework.domain.ucenter.ext;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
public class AuthToken {
    String access_token;//身份token(用户)
    String refresh_token;//刷新token
    String jwt_token;//jwt令牌
}
