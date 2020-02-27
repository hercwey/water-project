package com.learnbind.ai.service.moduleproductno.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.ModuleProductNoMapper;
import com.learnbind.ai.model.ModuleProductNo;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.moduleproductno.ModuleProductNoService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.moduleproductno.impl
 *
 * @Title: ModuleProductNoServiceImpl.java
 * @Description: 模组号-出厂编号对照表服务实现类
 *
 * @author SRD
 * @date 2020年2月27日 下午5:35:20
 * @version V1.0 
 *
 */
@Service
public class ModuleProductNoServiceImpl extends AbstractBaseService<ModuleProductNo, Long> implements  ModuleProductNoService {
	
	@Autowired
	public ModuleProductNoMapper moduleProductNoMapper;

	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public ModuleProductNoServiceImpl(ModuleProductNoMapper mapper) {
		this.moduleProductNoMapper=mapper;
		this.setMapper(mapper);
	}
	
}
