package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.exception.MyException;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CmsPageServiceImpl implements CmsPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;
    @Override
    //查询所有页面
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest==null){
            queryPageRequest=new QueryPageRequest();
        }
        if (page<=0){
            page=1;
        }
        page=page-1;
        if (size<=0){
            size=10;
        }
        CmsPage cmsPage=new CmsPage();
        //匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        //站点id
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模板id
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //别名:模糊查询
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
            matching = matching.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());//设置PageAliase为模糊查询
        }
        //动态查询对象
        Example<CmsPage> example=Example.of(cmsPage,matching);
        //分页查询对象
        Pageable pageable= PageRequest.of(page, size);

        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        QueryResult<CmsPage> queryResult=new QueryResult<>();
        queryResult.setList(all.getContent());//数据
        queryResult.setTotal(all.getTotalElements());//总记录数
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }

    @Override
    //新建页面
    public CmsPageResult save(CmsPage cmsPage) {
        if (cmsPage ==null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        //在Page这个集合中唯一标识由三个属性组成,分别是pageName，pageWebPath，siteId
        //如果要新建的数据中这三个属性已经存在则视为该页面已经存在
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1!=null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //可以保存的情况
        cmsPage.setPageId(null);
        cmsPageRepository.save(cmsPage);
        CmsPageResult cmsPageResult=new CmsPageResult(CommonCode.SUCCESS,cmsPage);
        return cmsPageResult;
    }

    @Override
    //根据id查询页面
    public CmsPage findById(String id) {
        if (!StringUtils.isNotEmpty(id)){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if (byId.isPresent()){
            CmsPage cmsPage = byId.get();
            return cmsPage;
        }
        return null;
    }

    @Override
    //修改页面
    public CmsPageResult edit(String id, CmsPage cmsPage) {
        if (cmsPage==null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSE);
        }
        CmsPage byId = this.findById(id);
        if (byId==null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSEA);
        }
        byId.setSiteId(cmsPage.getSiteId());
        byId.setTemplateId(cmsPage.getTemplateId());
        byId.setPageAliase(cmsPage.getPageAliase());
        byId.setPageName(cmsPage.getPageName());
        byId.setPageWebPath(cmsPage.getPageWebPath());
        byId.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        byId.setDataUrl(cmsPage.getDataUrl());
        byId.setPageStatus(cmsPage.getPageStatus());
        byId.setPageType(cmsPage.getPageType());
        CmsPage save = cmsPageRepository.save(byId);
        CmsPageResult cmsPageResult=new CmsPageResult(CommonCode.SUCCESS,save);
        return cmsPageResult;
    }

    @Override
    //删除页面
    public ResponseResult delete(String id) {
        CmsPage byId = this.findById(id);
        if (byId==null){
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_FALSEA);
        }
        cmsPageRepository.delete(byId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Override
    //Course调用Cms保存页面
    public CmsPageResult up(CmsPage cmspage) {
        CmsPage cmspage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmspage.getPageName(), cmspage.getSiteId(), cmspage.getPageWebPath());
        if (cmspage1!=null){
            //当前页面已存在，则更新
            return this.edit(cmspage1.getPageId(),cmspage);
        }
        //当前课程页面还不存在，直接保存
        return this.save(cmspage);
    }
}
