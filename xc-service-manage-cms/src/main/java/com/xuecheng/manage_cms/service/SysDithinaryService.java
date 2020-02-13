package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;

public interface SysDithinaryService {
    SysDictionary getByType(String type);//获取数据字典
}
