package com.learnbind.ai.service.customers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.CustomerAccountItemLogMapper;
import com.learnbind.ai.dao.CustomersTraceMapper;
import com.learnbind.ai.model.CustomerAccountItemLog;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemLogService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomerAccountItemLogServiceImpl.java
 * @Description:客户账单日志
 *
 * @author Thinkpad
 * @date 2019年11月20日 下午8:33:10
 * @version V1.0 
 *
 */
@Service
public class CustomerAccountItemLogServiceImpl extends AbstractBaseService<CustomerAccountItemLog, Long> implements  CustomerAccountItemLogService {
	
	@Autowired
	public CustomerAccountItemLogMapper customerAccountItemLogMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerAccountItemLogServiceImpl(CustomerAccountItemLogMapper mapper) {
		this.customerAccountItemLogMapper=mapper;
		this.setMapper(mapper);
	}


}
