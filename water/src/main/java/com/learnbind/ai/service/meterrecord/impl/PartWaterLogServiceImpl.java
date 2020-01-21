package com.learnbind.ai.service.meterrecord.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.MeterRecordEditLogMapper;
import com.learnbind.ai.dao.PartWaterLogMapper;
import com.learnbind.ai.model.PartWaterLog;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meterrecord.PartWaterLogService;


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
public class PartWaterLogServiceImpl extends AbstractBaseService<PartWaterLog, Long> implements  PartWaterLogService {
	
	@Autowired
	public PartWaterLogMapper partWaterLogMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public PartWaterLogServiceImpl(PartWaterLogMapper mapper) {
		this.partWaterLogMapper=mapper;
		this.setMapper(mapper);
	}
	
}
