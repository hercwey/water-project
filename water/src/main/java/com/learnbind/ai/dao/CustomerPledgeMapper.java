package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerPledge;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerPledgeMapper extends Mapper<CustomerPledge> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */							
	public List<CustomerPledge> searchCustomerPledge(@Param("customerId") Long customerId, @Param("searchCond") String searchCond);
}