package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DiscountWaterFeeTrace;
import tk.mybatis.mapper.common.Mapper;

public interface DiscountWaterFeeTraceMapper extends Mapper<DiscountWaterFeeTrace> {
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<DiscountWaterFeeTrace> search(@Param("searchCond")String searchCond);
}