package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;

public interface CmsPageService {
    QueryResponseResult findList(int page,int size, QueryPageRequest queryPageRequest);//分页条件查询所有页面
    CmsPageResult save(CmsPage cmsPage);//新建页面
    CmsPage findById(String id);//根据id查询页面
    CmsPageResult edit(String id, CmsPage cmsPage);//修改页面
    ResponseResult delete(String id);//删除页面
    CmsPageResult up(CmsPage cmspage);//保存页面
}
