package com.learnbind.ai.service.meterrecord.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.MeterRecordTempPhotoMapper;
import com.learnbind.ai.model.MeterRecordTempPhoto;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempPhotoService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meterrecord.impl
 *
 * @Title: MeterRecordTempPhotoServiceImpl.java
 * @Description: 抄表记录临时照片服务实现类
 *
 * @author Administrator
 * @date 2019年7月24日 上午6:42:27
 * @version V1.0 
 *
 */
@Service
public class MeterRecordTempPhotoServiceImpl extends AbstractBaseService<MeterRecordTempPhoto, Long> implements  MeterRecordTempPhotoService {
	
	@Autowired
	public MeterRecordTempPhotoMapper meterRecordTempPhotoMapper;
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterRecordTempPhotoServiceImpl(MeterRecordTempPhotoMapper mapper) {
		this.meterRecordTempPhotoMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<MeterRecordTempPhoto> getList(Long customerId, Long meterId, Long operatorId) {
		MeterRecordTempPhoto tempPhoto = new MeterRecordTempPhoto();
		tempPhoto.setCustomerId(customerId);
		tempPhoto.setMeterId(meterId);
		tempPhoto.setOperatorId(operatorId);
		return meterRecordTempPhotoMapper.select(tempPhoto);
	}

}
