package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

//课程计划
@Repository
public interface TeachPlanMapper {
   @Select("SELECT a.pname a_name,a.id a_id from teachplan a WHERE a.parentid='0' AND a.courseid=#{courseId};")
   @Results({
           @Result(id=true,property = "id",column = "a_id"),
           @Result(property = "pname",column = "a_name"),
           @Result(property = "children",column = "a_id",javaType = java.util.List.class,many = @Many(select = "com.xuecheng.manage_course.dao.TeachPlanMapper.b"))
   })
   //查询课程计划中一二三级课程
   TeachplanNode selectList(String courseId);

   @Select("SELECT b.pname b_name,b.id b_id from teachplan b WHERE b.parentid=#{aId}")
   @Results({
           @Result(id=true,property = "id",column = "b_id"),
           @Result(property = "pname",column = "b_name"),
           @Result(property = "children",column = "b_id",javaType = java.util.List.class,many = @Many(select = "com.xuecheng.manage_course.dao.TeachPlanMapper.c"))
   })
   List<TeachplanNode> b(String aId);

   @Select("SELECT c.pname c_name,c.id c_id from teachplan c WHERE c.parentid=#{bId}")
   @Results({
           @Result(id=true,property = "id",column = "c_id"),
           @Result(property = "pname",column = "c_name"),
          // @Result(property = "teachplanMedia",column = "c_id",one = @One(select = "com.xuecheng.manage_course.dao.TeachPlanMapper.g")),
           @Result(property = "mediaId",column = "c_id",one = @One(select = "com.xuecheng.manage_course.dao.TeachPlanMapper.d")),
           @Result(property = "mediaFileOriginalName",column = "c_id",one = @One(select = "com.xuecheng.manage_course.dao.TeachPlanMapper.f"))
   })
   List<TeachplanNode> c(String bId);

   @Select("select media_id from teachplan_media where teachplan_id=#{cId}")
   String d(String cId);

   @Select("select media_fileoriginalname from teachplan_media where teachplan_id=#{cId}")
   String f(String cId);

//   @Results({
//           @Result(id = true,property = "teachplanId",column = "teachplan_id"),
//           @Result(property = "mediaId",column = "media_id"),
//           @Result(property = "mediaFileOriginalName",column = "media_fileoriginalname"),
//           @Result(property = "mediaUrl",column = "media_url"),
//           @Result(property = "courseId",column = "courseid")
//   })
//   @Select("select * from teachplan_media where teachplan_id=#{cId}")
//   TeachplanMedia g(String cId);
}
