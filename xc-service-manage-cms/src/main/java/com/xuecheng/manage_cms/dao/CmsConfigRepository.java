package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
//查询页面数据
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
