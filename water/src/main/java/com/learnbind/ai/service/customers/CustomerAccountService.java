package com.learnbind.ai.service.customers;

import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.customers
 *
 * @Title: CustomerAccountService.java
 * @Description: 客户-账户关系服务接口类
 *
 * @author Administrator
 * @date 2019年5月29日 下午3:30:10
 * @version V1.0 
 *
 */
public interface CustomerAccountService extends IBaseService<CustomerAccount, Long> {
	
	/**
	 * @Title: getAccountIdByCustomerName
	 * @Description: 根据客户id获取账户id
	 * @param customerId
	 * @return 
	 */
	public Long getAccountIdByCustomerName(Long customerId);
}
