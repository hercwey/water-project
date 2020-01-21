package com.learnbind.ai.service.meters.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.MeterERecordMapper;
import com.learnbind.ai.model.MeterERecord;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meters.MeterERecordService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MeterERecordServiceImpl.java
 * @Description: 表计-电子档案服务实现类
 *
 * @author Administrator
 * @date 2019年8月9日 下午7:37:32
 * @version V1.0 
 *
 */
@Service
public class MeterERecordServiceImpl extends AbstractBaseService<MeterERecord, Long> implements  MeterERecordService {
	
	@Autowired
	public MeterERecordMapper meterERecordMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterERecordServiceImpl(MeterERecordMapper mapper) {
		this.meterERecordMapper=mapper;
		this.setMapper(mapper);
	}

}
