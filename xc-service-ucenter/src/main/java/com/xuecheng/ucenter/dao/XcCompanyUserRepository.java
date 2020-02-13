package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {
    XcCompanyUser findByUserId(String userId);//根据用户id查询对应的企业id
}
