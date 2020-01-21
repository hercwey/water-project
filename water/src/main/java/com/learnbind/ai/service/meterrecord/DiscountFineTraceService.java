package com.learnbind.ai.service.meterrecord;

import java.math.BigDecimal;
import java.util.List;

import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.overduefine
 *
 * @Title: OverdueFineService.java
 * @Description: 每日违约金比例配置服务接口类
 *
 * @author Administrator
 * @date 2019年5月15日 下午4:27:03
 * @version V1.0 
 *
 */
public interface DiscountFineTraceService extends IBaseService<DiscountFineTrace, Long> {
	
	/**
	 * @Title: saveList
	 * @Description: 在违约金减免日志表当中添加数据
	 * @param customerId
	 * @param accountItemId
	 * @param dfList
	 * @return 
	 */
	public int saveList(Long customerId, Long accountItemId, List<DiscountFineTrace> dfList);
	
	/**
	 * @Title: getDiscountAmountSum
	 * @Description: 根据减免违约金id查询已减免违约金总金额
	 * @param overdueFineId
	 * @return 
	 */
	public BigDecimal getDiscountAmountSum(Long overdueFineId);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<DiscountFineTrace> search(String searchCond);
}
