package com.learnbind.ai.service.trace.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.UsePeopleAdjustTraceMapper;
import com.learnbind.ai.model.UsePeopleAdjustTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.trace.UsePeopleAdjustTraceService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.trace.impl
 *
 * @Title: UsePeopleAdjustTraceServiceImpl.java
 * @Description: 使用多人口调整日志服务实现类
 *
 * @author Administrator
 * @date 2019年8月28日 上午10:33:15
 * @version V1.0 
 *
 */
@Service
public class UsePeopleAdjustTraceServiceImpl extends AbstractBaseService<UsePeopleAdjustTrace, Long> implements  UsePeopleAdjustTraceService {
	
	@Autowired
	public UsePeopleAdjustTraceMapper usePeopleAdjustTraceMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public UsePeopleAdjustTraceServiceImpl(UsePeopleAdjustTraceMapper mapper) {
		this.usePeopleAdjustTraceMapper=mapper;
		this.setMapper(mapper);
	}

}
