package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.service.CmsSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CmsSiteServiceImpl implements CmsSiteService {
    @Autowired
    private CmsSiteRepository cmsSiteRepository;
    @Override
    //查询所有站点信息
    public QueryResponseResult findSite() {
        List<CmsSite> all = cmsSiteRepository.findAll();
        long count = cmsSiteRepository.count();
        QueryResult<CmsSite> queryResult=new QueryResult<>();
        queryResult.setList(all);
        queryResult.setTotal(count);
        QueryResponseResult queryResponseResult=new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
