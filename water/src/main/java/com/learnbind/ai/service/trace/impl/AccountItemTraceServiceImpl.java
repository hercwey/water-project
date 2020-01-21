package com.learnbind.ai.service.trace.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.AccountItemTraceMapper;
import com.learnbind.ai.model.AccountItemTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.trace.AccountItemTraceService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.trace.impl
 *
 * @Title: AccountItemTraceServiceImpl.java
 * @Description: 账目日志service服务实现类
 *
 * @author Administrator
 * @date 2019年9月8日 上午7:13:43
 * @version V1.0 
 *
 */
@Service
public class AccountItemTraceServiceImpl extends AbstractBaseService<AccountItemTrace, Long> implements  AccountItemTraceService {
	
	@Autowired
	public AccountItemTraceMapper accountItemTraceMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public AccountItemTraceServiceImpl(AccountItemTraceMapper mapper) {
		this.accountItemTraceMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public int insert(Long primaryId, String primarySubject, String operate, BigDecimal amount) {
		AccountItemTrace trace = new AccountItemTrace();
		trace.setPrimaryId(primaryId);
		trace.setPrimarySubject(primarySubject);
		trace.setOperate(operate);
		trace.setAmount(amount);
		return accountItemTraceMapper.insertSelective(trace);
	}

	@Override
	public int insert(Long primaryId, String primarySubject, Long secondaryId, String secondarySubject, String operate,
			BigDecimal amount) {
		AccountItemTrace trace = new AccountItemTrace();
		trace.setPrimaryId(primaryId);
		trace.setPrimarySubject(primarySubject);
		trace.setSecondaryId(secondaryId);
		trace.setSecondarySubject(secondarySubject);
		trace.setOperate(operate);
		trace.setAmount(amount);
		trace.setCreateTime(new Date());
		return accountItemTraceMapper.insertSelective(trace);
	}

}
