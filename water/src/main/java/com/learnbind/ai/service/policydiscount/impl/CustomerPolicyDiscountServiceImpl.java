package com.learnbind.ai.service.policydiscount.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.CustomerPolicyDiscountMapper;
import com.learnbind.ai.model.CustomerPolicyDiscount;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.policydiscount.CustomerPolicyDiscountService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.policydiscount.impl
 *
 * @Title: CustomerPolicyDiscountServiceImpl.java
 * @Description: 客户的政策减免额度
 *
 * @author Thinkpad
 * @date 2019年6月5日 下午12:11:22
 * @version V1.0 
 *
 */
@Service
public class CustomerPolicyDiscountServiceImpl extends AbstractBaseService<CustomerPolicyDiscount, Long> implements  CustomerPolicyDiscountService {
	
	@Autowired
	public CustomerPolicyDiscountMapper customerPolicyDiscountMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerPolicyDiscountServiceImpl(CustomerPolicyDiscountMapper mapper) {
		this.customerPolicyDiscountMapper=mapper;
		this.setMapper(mapper);
	}

}
