package com.learnbind.ai.service.interfaceconfig.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.SysInterfaceConfigMapper;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.interfaceconfig.impl
 *
 * @Title: DataDictServiceImpl.java
 * @Description: 接口配置服务实现类
 *
 * @author Administrator
 * @date 2019年7月8日 下午6:13:03
 * @version V1.0 
 *
 */
@Service
public class InterfaceConfigServiceImpl extends AbstractBaseService<SysInterfaceConfig, Long> implements  InterfaceConfigService {
	
	@Autowired
	public SysInterfaceConfigMapper sysInterfaceConfigMapper;
		
	/**
	 * 	创建一个新的实例 InterfaceConfigServiceImpl.
	 * 		采用构造函数注入
	 * @param mapper
	 */
	public InterfaceConfigServiceImpl(SysInterfaceConfigMapper mapper) {
		this.sysInterfaceConfigMapper=mapper;
		this.setMapper(mapper);
	}

}
