package com.learnbind.ai.mq.north.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.command.ConfigThresholdResponse;
import com.learnbind.ai.service.iot.WmMeterService;
import com.learnbind.ai.service.meters.MetersService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: ConfigThresholdResponseProcessService.java
 * @Description: 设置阈值响应数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class ConfigThresholdResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(ConfigThresholdResponseProcessService.class);
	
	@Autowired
	private WmMeterService wmMeterService;
	@Autowired
	private MetersService metersService;

	/**
	 * @Title: processAutoReportData
	 * @Description: 处理设备上报数据
	 * @param configThresholdRspData
	 */
	public void processResponseData(ConfigThresholdResponse configThresholdRspData) {

		log.debug("----------设置阈值响应数据处理");
		
		// 1、保存设备上报数据到数据库
		this.saveResponseData(configThresholdRspData);

		//String deviceId = configThresholdRspData.getDeviceId();// IOT电信平台设备ID
		Integer dataType = configThresholdRspData.getDataType();// 数据类型
		
		log.debug("----------其他数据类型，不做处理，数据类型："+dataType);
		
	}
	
	// --------------------------------保存设备上报数据--------------------------------------------------------------------------------------------------
	/**
	 * @Title: save
	 * @Description: 保存设备上报数据
	 * @param configThresholdRspData
	 * @return
	 */
	private int saveResponseData(ConfigThresholdResponse configThresholdRspData) {

		Date sysDate = new Date();// 系统时间
		Long deviceId = this.getDeviceId(configThresholdRspData.getDeviceId());// 通过IOT电信平台ID查询本地库设备表主键ID

		WmMeter meter = new WmMeter();
		meter.setCreateTime(sysDate);
		meter.setCtrlCode(configThresholdRspData.getCtrlCode());
		meter.setDataDi(String.valueOf(configThresholdRspData.getDataDI()));
		meter.setDeviceId(deviceId);
		meter.setEventTime(configThresholdRspData.getEventTime());
		meter.setFactoryCode(configThresholdRspData.getFactoryCode());
		meter.setGatewayId(configThresholdRspData.getGatewayId());
		meter.setJsonData(configThresholdRspData.getJsonData());
		meter.setMeterAddr(configThresholdRspData.getMeterAddr());
		meter.setMeterChecksum(configThresholdRspData.getChecksum().longValue());
		meter.setMeterData(configThresholdRspData.getData());
		meter.setMeterDataBasic(configThresholdRspData.getDataBasic());
		meter.setMeterDataType(configThresholdRspData.getDataType());
		meter.setMeterSequence(configThresholdRspData.getSequence().longValue());
		meter.setMeterType(configThresholdRspData.getMeterType().longValue());
		meter.setRequestId(configThresholdRspData.getRequestId());
		meter.setServiceId(configThresholdRspData.getServiceId());
		meter.setServiceType(configThresholdRspData.getServiceType());
		meter.setUpdateTime(sysDate);
		return wmMeterService.insertSelective(meter);
	}

	/**
	 * @Title: getDeviceId
	 * @Description: 获取设备表主键ID
	 * @param deviceId IOT电信平台设备ID
	 * @return 返回本地库设备表主键ID
	 */
	private Long getDeviceId(String deviceId) {
		Meters meter = new Meters();
		meter.setDeviceId(deviceId);
		List<Meters> meterList = metersService.select(meter);
		if (meterList != null && meterList.size() > 0) {
			return meterList.get(0).getId();
		}
		return null;
	}
	
}
