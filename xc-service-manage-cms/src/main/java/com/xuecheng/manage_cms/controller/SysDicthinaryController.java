package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.sys.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDithinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//查询数据字典接口
@RestController
@RequestMapping("/sys/dictionary")
public class SysDicthinaryController implements SysDicthinaryControllerApi {
    @Autowired
    private SysDithinaryService sysDithinaryService;
    @Override
    @GetMapping("/get/{type}")
    public SysDictionary getByType(@PathVariable("type") String type) {
        return sysDithinaryService.getByType(type);
    }
}
