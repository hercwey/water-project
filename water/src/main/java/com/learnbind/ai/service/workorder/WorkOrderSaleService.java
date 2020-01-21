package com.learnbind.ai.service.workorder;

import java.util.List;

import com.learnbind.ai.model.WorkOrder;
import com.learnbind.ai.model.WorkOrderSale;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.workorder
 *
 * @Title: WorkOrderService.java
 * @Description: 工单-售后人员
 *
 * @author Thinkpad
 * @date 2019年7月20日 下午3:28:54
 * @version V1.0 
 *
 */
public interface WorkOrderSaleService extends IBaseService<WorkOrderSale, Long> {
	
	/**
	 * @Title: selectRealnameByWorkOrderId
	 * @Description: 通过工单id查询工单分配人员
	 * @param workOrderId
	 * @return 
	 */
	public String selectRealnameByWorkOrderId(Long workOrderId);
	
	
}
