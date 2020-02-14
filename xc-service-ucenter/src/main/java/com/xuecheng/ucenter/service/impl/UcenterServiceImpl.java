package com.xuecheng.ucenter.service.impl;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.framework.domain.ucenter.response.UcenterCode;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import com.xuecheng.ucenter.service.UcenterService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UcenterServiceImpl implements UcenterService {
    @Autowired
    private XcUserRepository xcUserRepository;
    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;
    @Autowired
    private XcMenuMapper xcMenuMapper;
    @Override
    //根据用户名，获取用户信息
    public XcUserExt getUserext(String username) {
        XcUser xcUser = xcUserRepository.findByUsername(username);
        if (xcUser==null){
            return null;
        }
        String userId = xcUser.getId();
        String companyId=null;
        //获取用户对应的企业
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
        if (xcCompanyUser!=null){
            companyId=xcCompanyUser.getCompanyId();
        }
        XcUserExt xcUserExt=new XcUserExt();
        //获取用户对应的权限
        List<XcMenu> xcMenus = xcMenuMapper.selectMenuByUserId(userId);
        BeanUtils.copyProperties(xcUser,xcUserExt);
        xcUserExt.setCompanyId(companyId);
        xcUserExt.setPermissions(xcMenus);
        return xcUserExt;
    }

    @Override
    //注册用户
    public ResponseResult rgUser(String username, String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        XcUser xcUser = xcUserRepository.findByUsername(username);
        if (xcUser!=null){
            return new ResponseResult(UcenterCode.UCENTER_USERNAME_Exits);
        }
        String encode = bCryptPasswordEncoder.encode(password);//对密码进行编码
        XcUser xcUser1=new XcUser();
        xcUser1.setUsername(username);
        xcUser1.setPassword(encode);
        xcUser1.setCreateTime(new Date());
        xcUser1.setStatus("1");
        xcUser1.setName("游客一");
        xcUser1.setUtype("99999");
        xcUserRepository.save(xcUser1);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
