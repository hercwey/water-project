package com.learnbind.ai.service.sendlog.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.common.enumclass.EnumSendSmsStatus;
import com.learnbind.ai.dao.SendSmsLogMapper;
import com.learnbind.ai.model.SendSmsLog;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.sendlog.SendSmsLogService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.sendlog.impl
 *
 * @Title: SendSmsLogServiceImpl.java
 * @Description: 发送短信日志
 *
 * @author Thinkpad
 * @date 2020年1月18日 下午4:36:48
 * @version V1.0 
 *
 */
@Service
public class SendSmsLogServiceImpl extends AbstractBaseService<SendSmsLog, Long> implements SendSmsLogService {
	@Autowired
	public SendSmsLogMapper sendSmsLogMapper;

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
	public SendSmsLogServiceImpl(SendSmsLogMapper mapper) {
		this.sendSmsLogMapper = mapper;
		this.setMapper(mapper);
	}

	@Override
	public SendSmsLog getSmsLog(Long customerId, String period) {
		Example example = new Example(SendSmsLog.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("period", period).andEqualTo("result", EnumSendSmsStatus.FAIL.getValue());
		List<SendSmsLog> tempList = this.selectByExample(example);
		if(tempList.size() > 0) {
			return tempList.get(0);
		}
		return null;
	}


}
