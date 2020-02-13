package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.model.response.QueryResponseResult;
import org.springframework.stereotype.Service;

public interface CmsTemplateService {
   QueryResponseResult findTemplate();//查询所有模板信息
   CmsTemplate findById(String templateId);//根据id查询模板信息
}
