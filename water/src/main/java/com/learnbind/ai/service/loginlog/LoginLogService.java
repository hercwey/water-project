package com.learnbind.ai.service.loginlog;

import java.util.List;

import com.learnbind.ai.model.LoginLog;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.loginlog
 *
 * @Title: LoginLogService.java
 * @Description: 登陆日志
 *
 * @author Thinkpad
 * @date 2019年7月5日 下午6:11:38
 * @version V1.0 
 *
 */
public interface LoginLogService extends IBaseService<LoginLog, Long> {
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<LoginLog> search(String searchCond);
}
