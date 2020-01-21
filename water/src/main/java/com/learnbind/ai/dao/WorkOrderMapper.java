package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.WorkOrder;

import tk.mybatis.mapper.common.Mapper;

public interface WorkOrderMapper extends Mapper<WorkOrder> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<WorkOrder> searchWorkOrder(@Param("searchCond")String searchCond, @Param("status")Integer status);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<WorkOrder> searchWorkOrderSale(@Param("searchCond")String searchCond, @Param("userId")Long userId, @Param("status")Integer status);
	
	/**
	 * @Title: getListAll
	 * @Description: 查询所有分组
	 * @return map集合
	 */
	public List<Map<String, Object>> getListAll();
}