package com.xuecheng.ucenter;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    private XcMenuMapper xcMenuMapper;
    @Test
    public void test(){
        List<XcMenu> xcMenus = xcMenuMapper.selectMenuByUserId("48");
        for (XcMenu xx:xcMenus){
            System.out.println(xx);
        }
    }
}
