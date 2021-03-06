package com.learnbind.ai.mq.north.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: DeviceQueryResponseProcessService.java
 * @Description: 查询设备响应数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class DeviceQueryResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(DeviceQueryResponseProcessService.class);
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData() {

		log.debug("----------查询设备响应数据处理");
		
	}
	
}
