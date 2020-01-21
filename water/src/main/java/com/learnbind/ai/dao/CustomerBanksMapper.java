package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerBanks;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerBanksMapper extends Mapper<CustomerBanks> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomerBanks> searchCustomerBanks(@Param("customerId") Long customerId, @Param("searchCond") String searchCond);
	
	 /**
     * @Title: getCustomerBanks
     * @Description: 获取客户银行信息
     * @param customerId
     * @return 
     */
    public List<CustomerBanks> getCustomerBanks(@Param("customerId") Long customerId);
}