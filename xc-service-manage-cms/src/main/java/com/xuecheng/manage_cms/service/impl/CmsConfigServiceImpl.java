package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.service.CmsConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CmsConfigServiceImpl implements CmsConfigService {
    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    @Override
    //根据id查询Cms页面的数据
    public CmsConfig getConfigById(String id) {
        if (!StringUtils.isNotEmpty(id)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        Optional<CmsConfig> cmsConfig= cmsConfigRepository.findById(id);
        if (cmsConfig.isPresent()){
            CmsConfig cmsConfig1 = cmsConfig.get();
            return cmsConfig1;
        }
        return null;
    }
}
