package com.learnbind.ai.service.customers;

import java.util.List;

import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.model.DiscountWaterFeeTrace;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers
 *
 * @Title: DiscountWaterFeeTraceService.java
 * @Description: 减免水费日志
 *
 * @author Thinkpad
 * @date 2019年7月8日 下午4:30:30
 * @version V1.0 
 *
 */
public interface DiscountWaterFeeTraceService extends IBaseService<DiscountWaterFeeTrace, Long> {
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<DiscountWaterFeeTrace> search(String searchCond);
	
	/**
	 * @Title: saveList
	 * @Description: 在水费减免日志表当中添加数据
	 * @param customerId
	 * @param accountItemId
	 * @param dfList
	 * @return 
	 */
	public int saveList(Long customerId, Long accountItemId, List<DiscountWaterFeeTrace> dfList);
}
