package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.CmsTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CmsTemplateServiceImpl implements CmsTemplateService {
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;
    @Override
    //查询所有模板信息
    public QueryResponseResult findTemplate() {
        List<CmsTemplate> all = cmsTemplateRepository.findAll();
        long count = cmsTemplateRepository.count();
        QueryResult<CmsTemplate> queryResult=new QueryResult<>();
        queryResult.setList(all);
        queryResult.setTotal(count);
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    @Override
    //根据id查询模板信息
    public CmsTemplate findById(String templateId) {
        if (StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        Optional<CmsTemplate> byId = cmsTemplateRepository.findById(templateId);
        if (byId.isPresent()){
            CmsTemplate cmsTemplate = byId.get();
            return cmsTemplate;
        }
        return null;
    }
}
