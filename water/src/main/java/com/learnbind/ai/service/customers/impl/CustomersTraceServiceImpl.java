package com.learnbind.ai.service.customers.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.CustomersTraceMapper;
import com.learnbind.ai.dao.LoginLogMapper;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.LoginLog;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomersTraceService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomersTraceServiceImpl.java
 * @Description: 客户档案维护日志
 *
 * @author Thinkpad
 * @date 2019年7月7日 上午9:41:54
 * @version V1.0 
 *
 */
@Service
public class CustomersTraceServiceImpl extends AbstractBaseService<CustomersTrace, Long> implements  CustomersTraceService {
	
	@Autowired
	public CustomersTraceMapper traceMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomersTraceServiceImpl(CustomersTraceMapper mapper) {
		this.traceMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<CustomersTrace> search(String searchCond) {
		return traceMapper.search(searchCond);
	}

	@Override
	public CustomersTrace getTraceByOperationTime(String dateString) {
		return traceMapper.getTraceByOperationTime(dateString);
	}

}
