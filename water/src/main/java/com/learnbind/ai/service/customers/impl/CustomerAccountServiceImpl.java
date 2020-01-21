package com.learnbind.ai.service.customers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.CustomerAccountMapper;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomerAccountServiceImpl.java
 * @Description: 客户-账户关系服务实现类
 *
 * @author Administrator
 * @date 2019年5月29日 下午3:31:46
 * @version V1.0 
 *
 */
@Service
public class CustomerAccountServiceImpl extends AbstractBaseService<CustomerAccount, Long> implements  CustomerAccountService {
	
	@Autowired
	public CustomerAccountMapper customerAccountMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerAccountServiceImpl(CustomerAccountMapper mapper) {
		this.customerAccountMapper=mapper;
		this.setMapper(mapper);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getAccountIdByCustomerName
	 * @Description: 根据客户id获取账户id
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerAccountService#getAccountIdByCustomerName(java.lang.Long)
	 */
	@Override
	public Long getAccountIdByCustomerName(Long customerId) {
		return customerAccountMapper.getAccountIdByCustomerName(customerId);
	}

}
