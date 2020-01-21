package com.learnbind.ai.service.meterrecord.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.MeterRecordPhotoMapper;
import com.learnbind.ai.model.MeterRecordPhoto;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meterrecord.MeterRecordPhotoService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.meterrecord.impl
 *
 * @Title: MeterRecordPhotoServiceImpl.java
 * @Description: 抄表记录照片上传
 *
 * @author Thinkpad
 * @date 2019年7月24日 下午6:05:28
 * @version V1.0 
 *
 */
@Service
public class MeterRecordPhotoServiceImpl extends AbstractBaseService<MeterRecordPhoto, Long> implements  MeterRecordPhotoService {
	
	@Autowired
	public MeterRecordPhotoMapper meterRecordPhotoMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterRecordPhotoServiceImpl(MeterRecordPhotoMapper mapper) {
		this.meterRecordPhotoMapper=mapper;
		this.setMapper(mapper);
	}

}
