package com.learnbind.ai.service.overduefine.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.SysOverdueFineMapper;
import com.learnbind.ai.model.SysOverdueFine;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.overduefine.OverdueFineService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.overduefine.impl
 *
 * @Title: OverdueFineServiceImpl.java
 * @Description: 每日违约金比例配置服务实现类
 *
 * @author Administrator
 * @date 2019年5月15日 下午4:28:38
 * @version V1.0 
 *
 */
@Service
public class OverdueFineServiceImpl extends AbstractBaseService<SysOverdueFine, Long> implements  OverdueFineService {
	
	@Autowired
	public SysOverdueFineMapper sysOverdueFineMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public OverdueFineServiceImpl(SysOverdueFineMapper mapper) {
		this.sysOverdueFineMapper=mapper;
		this.setMapper(mapper);
	}

}
