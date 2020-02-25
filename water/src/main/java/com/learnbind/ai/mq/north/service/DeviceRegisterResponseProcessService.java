package com.learnbind.ai.mq.north.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.iotbean.device.RegisterDeviceResponse;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: DeviceRegisterResponseProcessService.java
 * @Description: 注册设备响应数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class DeviceRegisterResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(DeviceRegisterResponseProcessService.class);
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(RegisterDeviceResponse registerDeviceRsp) {

		log.debug("----------注册设备响应数据处理");
		
	}
	
}
