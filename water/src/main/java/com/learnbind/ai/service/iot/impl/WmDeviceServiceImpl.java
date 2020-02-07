package com.learnbind.ai.service.iot.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WmDeviceMapper;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.iot.WmDeviceService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.iot.impl
 *
 * @Title: WmDeviceServiceImpl.java
 * @Description: 智慧水务平台测试-水表设备服务实现类
 *
 * @author SRD
 * @date 2020年2月6日 下午8:45:31
 * @version V1.0 
 *
 */
@Service
public class WmDeviceServiceImpl extends AbstractBaseService<WmDevice, Long> implements  WmDeviceService {
	
	@Autowired
	public WmDeviceMapper wmDeviceMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public WmDeviceServiceImpl(WmDeviceMapper mapper) {
		this.wmDeviceMapper=mapper;
		this.setMapper(mapper);
	}

}
