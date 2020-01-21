package com.learnbind.ai.service.customers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.CustomerInfoCollectMapper;
import com.learnbind.ai.model.CustomerInfoCollect;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerInfoCollectService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.CustomerBanks.impl
 *
 * @Title: CustomerBanksServiceImpl.java
 * @Description: 客户档案
 *
 * @author Thinkpad
 * @date 2019年5月20日 下午12:29:27
 * @version V1.0 
 *
 */
/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomerInfoCollectServiceImpl.java
 * @Description: 客户信息汇总
 *
 * @author Thinkpad
 * @date 2019年8月2日 上午10:11:32
 * @version V1.0 
 *
 */
@Service
public class CustomerInfoCollectServiceImpl extends AbstractBaseService<CustomerInfoCollect, Long> implements  CustomerInfoCollectService {
	
	@Autowired
	public CustomerInfoCollectMapper customerInfoCollectMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerInfoCollectServiceImpl(CustomerInfoCollectMapper mapper) {
		this.customerInfoCollectMapper=mapper;
		this.setMapper(mapper);
	}


	/**
	 * @Title: searchCond
	 * @Description: 条件查询
	 * @param searchCond
	 * @return 
	 */
	@Override
	public List<CustomerInfoCollect> searchCond(String searchCond) {
		return customerInfoCollectMapper.searchCond(searchCond);
	}


}
