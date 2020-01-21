package com.learnbind.ai.service.trace.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.UseDiscountTraceMapper;
import com.learnbind.ai.model.UseDiscountTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.trace.UseDiscountTraceService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.trace.impl
 *
 * @Title: UseDiscountTraceServiceImpl.java
 * @Description: 使用政策减免日志服务实现类
 *
 * @author Administrator
 * @date 2019年8月28日 上午10:33:15
 * @version V1.0 
 *
 */
@Service
public class UseDiscountTraceServiceImpl extends AbstractBaseService<UseDiscountTrace, Long> implements  UseDiscountTraceService {
	
	@Autowired
	public UseDiscountTraceMapper useDiscountTraceMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public UseDiscountTraceServiceImpl(UseDiscountTraceMapper mapper) {
		this.useDiscountTraceMapper=mapper;
		this.setMapper(mapper);
	}

}
