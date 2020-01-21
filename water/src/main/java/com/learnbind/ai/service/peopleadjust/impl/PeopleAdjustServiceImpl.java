package com.learnbind.ai.service.peopleadjust.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.SysPeopleAdjustMapper;
import com.learnbind.ai.model.SysPeopleAdjust;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.peopleadjust.PeopleAdjustService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.peopleadjust.impl
 *
 * @Title: PeopleAdjustServiceImpl.java
 * @Description: 多人口调整配置服务实现类
 *
 * @author Administrator
 * @date 2019年5月15日 上午11:41:29
 * @version V1.0 
 *
 */
@Service
public class PeopleAdjustServiceImpl extends AbstractBaseService<SysPeopleAdjust, Long> implements  PeopleAdjustService {
	
	@Autowired
	public SysPeopleAdjustMapper sysPeopleAdjustMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public PeopleAdjustServiceImpl(SysPeopleAdjustMapper mapper) {
		this.sysPeopleAdjustMapper=mapper;
		this.setMapper(mapper);
	}

}
