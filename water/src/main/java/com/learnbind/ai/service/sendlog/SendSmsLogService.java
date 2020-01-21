package com.learnbind.ai.service.sendlog;

import com.learnbind.ai.model.SendSmsLog;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.sendlog
 *
 * @Title: SendSmsLogService.java
 * @Description:发送短信日志
 *
 * @author Thinkpad
 * @date 2020年1月18日 下午4:34:00
 * @version V1.0 
 *
 */
public interface SendSmsLogService extends IBaseService<SendSmsLog, Long> {

	/**
	 * @Title: getSmsLog
	 * @Description: 获取短信发送日志（发送成功）
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public SendSmsLog getSmsLog(Long customerId, String period);
}
