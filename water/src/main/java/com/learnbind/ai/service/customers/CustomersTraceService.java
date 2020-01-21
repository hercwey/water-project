package com.learnbind.ai.service.customers;

import java.util.Date;
import java.util.List;

import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers
 *
 * @Title: CustomersTraceService.java
 * @Description: 客户档案维护日志
 *
 * @author Thinkpad
 * @date 2019年7月7日 上午9:40:36
 * @version V1.0 
 *
 */
public interface CustomersTraceService extends IBaseService<CustomersTrace, Long> {
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomersTrace> search(String searchCond);
	
	public CustomersTrace getTraceByOperationTime(String dateString);
}
