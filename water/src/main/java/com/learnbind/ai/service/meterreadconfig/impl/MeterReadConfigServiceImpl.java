package com.learnbind.ai.service.meterreadconfig.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.dao.SysMeterReadConfigMapper;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysMeterReadConfig;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meterreadconfig.MeterReadConfigService;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.meterreadconfig.impl
 *
 * @Title: MeterReadConfigServiceImpl.java
 * @Description: 抄表配置
 *
 * @author Thinkpad
 * @date 2019年7月4日 上午11:22:57
 * @version V1.0 
 *
 */
@Service
public class MeterReadConfigServiceImpl extends AbstractBaseService<SysMeterReadConfig, Long> implements  MeterReadConfigService {
	
	@Autowired
	public SysMeterReadConfigMapper sysMeterReadConfigMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterReadConfigServiceImpl(SysMeterReadConfigMapper mapper) {
		this.sysMeterReadConfigMapper=mapper;
		this.setMapper(mapper);
	}

	

}
