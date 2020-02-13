package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;

public interface CmsPagePostOneService {
    CmsPostPageResult postPageQuick(CmsPage cmsPage);//页面一键发布
}
