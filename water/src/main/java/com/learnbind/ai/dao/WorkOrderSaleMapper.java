package com.learnbind.ai.dao;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.WorkOrderSale;
import tk.mybatis.mapper.common.Mapper;

public interface WorkOrderSaleMapper extends Mapper<WorkOrderSale> {
	/**
	 * @Title: selectRealnameByWorkOrderId
	 * @Description: 通过工单id查询工单分配人员
	 * @param workOrderId
	 * @return 
	 */
	public String selectRealnameByWorkOrderId(@Param("workOrderId")Long workOrderId);
	
}