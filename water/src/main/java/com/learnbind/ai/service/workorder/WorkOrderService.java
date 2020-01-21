package com.learnbind.ai.service.workorder;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.WorkOrder;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.workorder
 *
 * @Title: WorkOrderService.java
 * @Description: 工单
 *
 * @author Thinkpad
 * @date 2019年7月20日 下午3:28:54
 * @version V1.0 
 *
 */
public interface WorkOrderService extends IBaseService<WorkOrder, Long> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<WorkOrder> searchWorkOrder(String searchCond, Integer status);
	
	public List<WorkOrder> searchWorkOrderSale(String searchCond, Long userId, Integer status);
	
	/**
	 * @Title: getListAll
	 * @Description: 查询所有分组
	 * @return map集合
	 */
	public List<Map<String, Object>> getListAll();
	
	
}
