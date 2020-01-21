package com.learnbind.ai.service.meters;

import java.util.List;

import com.learnbind.ai.model.MeterChangeRecepit;
import com.learnbind.ai.model.MeterStockTrace;
import com.learnbind.ai.service.common.IBaseService;




/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterStockTraceService.java
 * @Description: 库管日志
 *
 * @author Thinkpad
 * @date 2019年10月27日 上午1:35:47
 * @version V1.0 
 *
 */
public interface MeterStockTraceService extends IBaseService<MeterStockTrace, Long> {
	
	/**
	 * @Title: searchCond
	 * @Description: 条件查找
	 * @param searchCond
	 * @return 
	 */
	public List<MeterStockTrace> searchCond(String searchCond, Integer operationType);
	
	/**
	 * @Title: insertTrace
	 * @Description: 增加库存日志
	 * @param meterId
	 * @return 
	 */
	public int insertTrace(Long meterId, Integer operatorType);
     
}
