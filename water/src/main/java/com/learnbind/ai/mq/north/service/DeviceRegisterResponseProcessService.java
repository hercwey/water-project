package com.learnbind.ai.mq.north.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.DeviceRegisterRspBean;
import com.learnbind.ai.model.iotbean.device.RegisterDeviceResponse;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

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
	
	@Autowired
	private MetersService meterService;
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(RegisterDeviceResponse registerDeviceRsp) {

		log.debug("----------注册设备响应数据处理");
		
		if (registerDeviceRsp!=null) {
			String deviceId = registerDeviceRsp.getDeviceId();
			String verifyCode = registerDeviceRsp.getVerifyCode();
			if(StringUtils.isNotBlank(deviceId) && StringUtils.isNotBlank(verifyCode)) {
				this.updateDeviceId(verifyCode, deviceId);
			}else {
				log.debug("注册设备响应数据异常："+JSON.toJSONString(registerDeviceRsp));
			}
        }else {
        	log.debug("注册设备响应数据为空："+JSON.toJSONString(registerDeviceRsp));
        }
		
	}
	
	/**
	 * @Title: updateDeviceId
	 * @Description: 根据meters表中meterNo更新deviceId
	 * @param verifyCode
	 * @param deviceId 
	 */
	private void updateDeviceId(String verifyCode, String deviceId) {
		
		Example example = new Example(Meters.class);
		example.createCriteria().andEqualTo("meterNo", verifyCode);
		Meters meter = new Meters();
		meter.setDeviceId(deviceId);
		int rows = meterService.updateByExampleSelective(meter, example);
		if(rows>0) {
			log.debug("更新表计中deviceId成功，IOT deviceId:"+deviceId+"，verifyCode:"+verifyCode);
		}else {
			log.debug("更新表计中deviceId异常，IOT deviceId:"+deviceId+"，verifyCode:"+verifyCode);
		}
	}
	
}
