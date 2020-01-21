package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterStockTrace;
import tk.mybatis.mapper.common.Mapper;

public interface MeterStockTraceMapper extends Mapper<MeterStockTrace> {
	
	/**
	 * @Title: searchCond
	 * @Description: 条件查找
	 * @param searchCond
	 * @return 
	 */
	public List<MeterStockTrace> searchCond(@Param("searchCond")String searchCond, @Param("operationType")Integer operationType);
}