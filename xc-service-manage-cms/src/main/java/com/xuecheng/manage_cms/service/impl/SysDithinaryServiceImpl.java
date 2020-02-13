package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms.dao.SysDichinaryRepository;
import com.xuecheng.manage_cms.service.SysDithinaryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDithinaryServiceImpl implements SysDithinaryService {
    @Autowired
    private SysDichinaryRepository sysDichinaryRepository;
    @Override
    //获取数据字典
    public SysDictionary getByType(String type) {
        if (!StringUtils.isNotEmpty(type)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        SysDictionary byDType = sysDichinaryRepository.findByDType(type);
        return byDType;
    }
}
