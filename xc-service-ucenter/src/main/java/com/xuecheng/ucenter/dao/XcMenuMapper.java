package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XcMenuMapper {
    @Results({
            @Result(id=true,property = "id",column = "id"),
            @Result(property = "pId",column = "p_id"),
            @Result(property = "menuName",column = "menu_name"),
            @Result(property = "isMenu",column = "is_menu"),
            @Result(property = "createTime",column = "create_time"),
            @Result(property = "updateTime",column = "update_time")
    })
    @Select("select b.* from xc_menu b where b.id in (SELECT c.menu_id from xc_permission c WHERE c.role_id in ( select a.role_id from xc_user_role a WHERE a.user_id=#{courseId}\n" +
            "))")
    List<XcMenu> selectMenuByUserId(String userId);
}
