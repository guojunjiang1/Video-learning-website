package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;
//页面具体信息
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
    //根据页面名称，站点Id，页面webPath查询，确保页面的唯一性
    CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName,String siteId,String pageWebPath);
}
