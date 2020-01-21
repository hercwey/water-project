package com.learnbind.ai.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomersTrace;
import tk.mybatis.mapper.common.Mapper;

public interface CustomersTraceMapper extends Mapper<CustomersTrace> {
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomersTrace> search(@Param("searchCond")String searchCond);
	
	public CustomersTrace getTraceByOperationTime(@Param("dateString")String dateString);
}