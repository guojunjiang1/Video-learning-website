package com.xuecheng.framework.domain.ucenter.ext;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@ToString
@NoArgsConstructor
public class XcUserExt extends XcUser {

    //权限信息
    private List<XcMenu> permissions;

    //企业id
    private String companyId;
}
