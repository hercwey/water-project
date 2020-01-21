package com.learnbind.ai.service.meters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.MeterUnusedReceiptMapper;
import com.learnbind.ai.dao.MetersTraceMapper;
import com.learnbind.ai.model.MetersTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meters.MetersTraceService;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MetersTraceServiceImpl.java
 * @Description: 表计日志
 *
 * @author Thinkpad
 * @date 2019年11月20日 下午8:35:40
 * @version V1.0 
 *
 */
@Service
public class MetersTraceServiceImpl extends AbstractBaseService<MetersTrace, Long> implements MetersTraceService {
	
	@Autowired
	public MetersTraceMapper metersTraceMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MetersTraceServiceImpl(MetersTraceMapper mapper) {
		this.metersTraceMapper=mapper;
		this.setMapper(mapper);
	}
	

}
