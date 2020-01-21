package com.learnbind.ai.service.loginlog.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.LoginLogMapper;
import com.learnbind.ai.dao.SysPeopleAdjustMapper;
import com.learnbind.ai.model.LoginLog;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.loginlog.LoginLogService;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.loginlog.impl
 *
 * @Title: LoginLogServiceImpl.java
 * @Description: 登录日志
 *
 * @author Thinkpad
 * @date 2019年7月6日 下午5:38:25
 * @version V1.0 
 *
 */
@Service
public class LoginLogServiceImpl extends AbstractBaseService<LoginLog, Long> implements  LoginLogService {
	
	@Autowired
	public LoginLogMapper loginMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public LoginLogServiceImpl(LoginLogMapper mapper) {
		this.loginMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<LoginLog> search(String searchCond) {
		return loginMapper.search(searchCond);
	}

}
