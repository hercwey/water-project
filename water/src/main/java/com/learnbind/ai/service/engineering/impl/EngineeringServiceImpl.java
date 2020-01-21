package com.learnbind.ai.service.engineering.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.InstallEngineeringMapper;
import com.learnbind.ai.model.InstallEngineering;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.engineering.EngineeringService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.engineering.impl
 *
 * @Title: EngineeringDocServiceImpl.java
 * @Description: 安装工程服务实现类
 *
 * @author Administrator
 * @date 2019年8月4日 上午9:59:47
 * @version V1.0 
 *
 */
@Service
public class EngineeringServiceImpl extends AbstractBaseService<InstallEngineering, Long> implements EngineeringService {
	
	@Autowired
	public InstallEngineeringMapper installEngineeringMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public EngineeringServiceImpl(InstallEngineeringMapper mapper) {
		this.installEngineeringMapper=mapper;
		this.setMapper(mapper);
	}

}
