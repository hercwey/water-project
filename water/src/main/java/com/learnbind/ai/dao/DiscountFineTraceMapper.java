package com.learnbind.ai.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DiscountFineTrace;
import tk.mybatis.mapper.common.Mapper;

public interface DiscountFineTraceMapper extends Mapper<DiscountFineTrace> {
	
	/**
	 * @Title: getDiscountAmountSum
	 * @Description: 根据减免违约金id查询已减免违约金总金额
	 * @param overdueFineId
	 * @return 
	 */
	public BigDecimal getDiscountAmountSum(@Param("overdueFineId") Long overdueFineId);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<DiscountFineTrace> search(@Param("searchCond")String searchCond);
}