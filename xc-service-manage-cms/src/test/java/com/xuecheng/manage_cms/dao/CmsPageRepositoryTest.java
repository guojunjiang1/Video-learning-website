package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Test
    public void test(){
        List<CmsPage> all = cmsPageRepository.findAll();
        System.out.println(all);
    }
    @Test
    public void test1(){
        Pageable pageable=PageRequest.of(0,10);//第一页，一页10条
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }
    //动态查询
    @Test
    public void test2(){
        //默认情况对所有条件都是精准匹配，可以手动设置为其他的查询
        Pageable pageable=PageRequest.of(0,10);//第一页，一页10条
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        cmsPage.setTemplateId("5a962b52b00ffc514038faf7");
        cmsPage.setPageAliase("首");
        //匹配器
        ExampleMatcher matching = ExampleMatcher.matching();
        matching = matching.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());//设置PageAliase为模糊查询
        //构建查询器，传入要查询的实体对象条件和匹配器
        Example<CmsPage> example=Example.of(cmsPage,matching);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        List<CmsPage> content = all.getContent();
        System.out.println(content);
    }
}
