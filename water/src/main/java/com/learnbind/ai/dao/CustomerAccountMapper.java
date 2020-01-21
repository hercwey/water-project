package com.learnbind.ai.dao;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerAccount;
import tk.mybatis.mapper.common.Mapper;

public interface CustomerAccountMapper extends Mapper<CustomerAccount> {
	
	/**
	 * @Title: getAccountIdByCustomerName
	 * @Description: 根据客户id获取账户id
	 * @param customerId
	 * @return 
	 */
	public Long getAccountIdByCustomerName(@Param("customerId")Long customerId);
}