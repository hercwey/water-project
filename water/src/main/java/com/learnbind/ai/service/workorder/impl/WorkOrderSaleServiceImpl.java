package com.learnbind.ai.service.workorder.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WorkOrderSaleMapper;
import com.learnbind.ai.model.WorkOrderSale;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.workorder.WorkOrderSaleService;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.workorder.impl
 *
 * @Title: WorkOrderServiceImpl.java
 * @Description: 工单
 *
 * @author Thinkpad
 * @date 2019年7月20日 下午3:36:09
 * @version V1.0 
 *
 */
@Service
public class WorkOrderSaleServiceImpl extends AbstractBaseService<WorkOrderSale, Long> implements  WorkOrderSaleService {
	
	@Autowired
	public WorkOrderSaleMapper workOrderSaleMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public WorkOrderSaleServiceImpl(WorkOrderSaleMapper  mapper) {
		this.workOrderSaleMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: selectRealnameByWorkOrderId
	 * @Description: 通过工单id查询工单分配人员
	 * @param workOrderId
	 * @return 
	 * @see com.learnbind.ai.service.workorder.WorkOrderSaleService#selectRealnameByWorkOrderId(java.lang.Long)
	 */
	@Override
	public String selectRealnameByWorkOrderId(Long workOrderId) {
		return workOrderSaleMapper.selectRealnameByWorkOrderId(workOrderId);
	}
	
	
}
