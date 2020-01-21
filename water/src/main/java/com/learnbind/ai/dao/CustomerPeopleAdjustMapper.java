package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerPeopleAdjust;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerPeopleAdjustMapper extends Mapper<CustomerPeopleAdjust> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */							
	public List<CustomerPeopleAdjust> searchCustomerPeopleAdjust(@Param("customerId") Long customerId, @Param("searchCond") String searchCond);
}