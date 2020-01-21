package com.learnbind.ai.service.trace;

import java.math.BigDecimal;

import com.learnbind.ai.model.AccountItemTrace;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.trace
 *
 * @Title: AccountItemTraceService.java
 * @Description: 账目日志service服务接口类
 *
 * @author Administrator
 * @date 2019年9月8日 上午7:11:15
 * @version V1.0 
 *
 */
public interface AccountItemTraceService extends IBaseService<AccountItemTrace, Long> {
	
	/**
	 * @Title: insert
	 * @Description: 增加账目日志（水费/充值）
	 * @param primaryId
	 * @param primarySubject
	 * @param operate
	 * @param amount
	 * @return 
	 */
	public int insert(Long primaryId, String primarySubject, String operate, BigDecimal amount);
	
	/**
	 * @Title: insert
	 * @Description: 增加账目日志（）
	 * @param primaryId
	 * @param primarySubject
	 * @param secondaryId
	 * @param secondarySubject
	 * @param operate
	 * @param amount
	 * @return 
	 */
	public int insert(Long primaryId, String primarySubject, Long secondaryId, String secondarySubject, String operate, BigDecimal amount);
	
}
