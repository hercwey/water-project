package com.learnbind.ai.service.trace;

import com.learnbind.ai.model.UseWaterPriceTrace;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.trace
 *
 * @Title: UseWaterPriceTraceService.java
 * @Description: 使用水价日志服务接口类
 *
 * @author Administrator
 * @date 2019年8月28日 上午10:31:16
 * @version V1.0 
 *
 */
public interface UseWaterPriceTraceService extends IBaseService<UseWaterPriceTrace, Long> {
	
	/**
	 * @Title: updateAccountItemId
	 * @Description: 根据分水量ID更新账目ID
	 * @param partitionWaterId
	 * @param accountItemId
	 * @return 
	 */
	public int updateAccountItemId(Long partitionWaterId, Long accountItemId);
	
	
	/**
	 * @Title: inserTrace
	 * @Description: 添加水价使用日志
	 * @param waterPriceId
	 * @return 
	 */
	public int inserTrace(Long waterPriceId, Long accountItemId);
}
