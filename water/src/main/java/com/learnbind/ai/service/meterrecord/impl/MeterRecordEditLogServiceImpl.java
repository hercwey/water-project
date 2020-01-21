package com.learnbind.ai.service.meterrecord.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumOverdueFineStatus;
import com.learnbind.ai.constant.AccountItemConstant;
import com.learnbind.ai.dao.DiscountFineTraceMapper;
import com.learnbind.ai.dao.MeterRecordEditLogMapper;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.model.MeterRecordEditLog;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meterrecord.MeterRecordEditLogService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meterrecord.impl
 *
 * @Title: MeterRecordEditLogServiceImpl.java
 * @Description: 抄表记录修改日志
 *
 * @author Thinkpad
 * @date 2019年11月20日 下午8:38:27
 * @version V1.0 
 *
 */
@Service
public class MeterRecordEditLogServiceImpl extends AbstractBaseService<MeterRecordEditLog, Long> implements  MeterRecordEditLogService {
	
	@Autowired
	public MeterRecordEditLogMapper meterRecordEditLogMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterRecordEditLogServiceImpl(MeterRecordEditLogMapper mapper) {
		this.meterRecordEditLogMapper=mapper;
		this.setMapper(mapper);
	}
	
}
