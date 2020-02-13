package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

//数据字典
public interface SysDichinaryRepository extends MongoRepository<SysDictionary,String> {
    SysDictionary findByDType(String dType);
}
