package com.learnbind.ai.service.sendlog.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.SendWechatLogMapper;
import com.learnbind.ai.model.SendWechatLog;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.sendlog.SendWechatLogService;
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.sendlog.impl
 *
 * @Title: SendWechatLogServiceImpl.java
 * @Description: 发送微信日志
 *
 * @author Thinkpad
 * @date 2020年1月18日 下午4:38:02
 * @version V1.0 
 *
 */
@Service
public class SendWechatLogServiceImpl extends AbstractBaseService<SendWechatLog, Long> implements SendWechatLogService {
	@Autowired
	public SendWechatLogMapper sendWechatLogMapper;

	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public SendWechatLogServiceImpl(SendWechatLogMapper mapper) {
		this.sendWechatLogMapper = mapper;
		this.setMapper(mapper);
	}


}
