package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

//课程分类
@Repository
public interface CategoryMapper {
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "children",column = "id",javaType = java.util.List.class,many = @Many(select = "com.xuecheng.manage_course.dao.CategoryMapper.b"))
    })
    @Select("select * from category where parentid='0'")
    CategoryNode findList();//查询分类信息，树形结构

    @Select("select * from category where parentid=#{id}")
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "children",column = "id",javaType = java.util.List.class,many = @Many(select = "com.xuecheng.manage_course.dao.CategoryMapper.c"))
    })
    List<CategoryNode> b(String id);

    @Select("select * from category where parentid=#{id}")
    @Results({
            @Result(id=true,property = "id",column = "id"),
    })
    List<CategoryNode> c(String id);
}
