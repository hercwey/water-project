package com.learnbind.ai.mq.north.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.command.AccountStatusWriteResponse;
import com.learnbind.ai.model.iotbean.common.CommandCallbackConstants;
import com.learnbind.ai.model.iotbean.common.ReportDataType;
import com.learnbind.ai.service.iot.WmCommandService;
import com.learnbind.ai.service.iot.WmMeterService;
import com.learnbind.ai.service.meters.MetersService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: AccountStatusReadResponseProcessService.java
 * @Description: 读开户状态响应处理服务
 *
 * @author SRD
 * @date 2020年3月3日 下午11:04:59
 * @version V1.0 
 *
 */
@Service
public class AccountStatusWriteResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AccountStatusWriteResponseProcessService.class);
	
	@Autowired
	private MetersService metersService;
	@Autowired
	private WmCommandService wmCommandService;
	@Autowired
	private WmMeterService wmMeterService;
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(AccountStatusWriteResponse accountStatusWriteRsp) {

		log.debug("----------写开户状态响应数据处理");
		
		// 1、保存设备上报数据到数据库
		this.saveReportData(accountStatusWriteRsp);
		
		String deviceId = accountStatusWriteRsp.getDeviceId();// IOT电信平台设备ID
		Integer dataType = accountStatusWriteRsp.getDataType();// 数据类型
		Integer sequence = accountStatusWriteRsp.getSequence();//序号
		
		// 2、收到上报数据后更新水表设备开户状态
		if (dataType == ReportDataType.RSP_ACCOUNT_STATUS_WRITE) {// 如果数据类型是 写开户状态响应数据
			// 水表配置信息，更新数据库device表account_status字段
			log.info("----------写开户状态响应数据data参数："+accountStatusWriteRsp.getData());
			this.updateCommandStatus(dataType, deviceId, sequence);//更新指令状态为成功
		} else {
			log.debug("----------其他数据类型，不做处理，数据类型："+dataType);
		}
		
	}
	
	// --------------------------------保存设备上报数据--------------------------------------------------------------------------------------------------
	/**
	 * @Title: save
	 * @Description: 保存设备上报数据
	 * @param accountStatusWriteRsp
	 * @return
	 */
	private int saveReportData(AccountStatusWriteResponse accountStatusWriteRsp) {

		Date sysDate = new Date();// 系统时间
		Long deviceId = this.getDeviceId(accountStatusWriteRsp.getDeviceId());// 通过IOT电信平台ID查询本地库设备表主键ID

		WmMeter meter = new WmMeter();
		meter.setCreateTime(sysDate);
		meter.setCtrlCode(accountStatusWriteRsp.getCtrlCode());
		meter.setDataDi(String.valueOf(accountStatusWriteRsp.getDataDI()));
		meter.setDeviceId(deviceId);
		meter.setEventTime(accountStatusWriteRsp.getEventTime());
		meter.setFactoryCode(accountStatusWriteRsp.getFactoryCode());
		meter.setGatewayId(accountStatusWriteRsp.getGatewayId());
		meter.setJsonData(accountStatusWriteRsp.getJsonData());
		meter.setMeterAddr(accountStatusWriteRsp.getMeterAddr());
		meter.setMeterChecksum(accountStatusWriteRsp.getChecksum().longValue());
		meter.setMeterData(accountStatusWriteRsp.getData());
		meter.setMeterDataBasic(accountStatusWriteRsp.getDataBasic());
		meter.setMeterDataType(accountStatusWriteRsp.getDataType());
		meter.setMeterSequence(accountStatusWriteRsp.getSequence().longValue());
		meter.setMeterType(accountStatusWriteRsp.getMeterType().longValue());
		meter.setRequestId(accountStatusWriteRsp.getRequestId());
		meter.setServiceId(accountStatusWriteRsp.getServiceId());
		meter.setServiceType(accountStatusWriteRsp.getServiceType());
		meter.setUpdateTime(sysDate);
		return wmMeterService.insertSelective(meter);
	}
	
	// -------------------------------- 更新指令状态为成功 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: updateCommandStatus
	 * @Description: 更新指令状态为成功
	 * @param dataType
	 * @param iotDeviceId
	 * @param sequence
	 * @return 
	 */
	private int updateCommandStatus(Integer dataType, String iotDeviceId, Integer sequence) {
			
		Long deviceId = this.getDeviceId(iotDeviceId);
		if(deviceId!=null) {
			int status = CommandCallbackConstants.COMMAND_STATUS_SUCCESS;//4=执行成功
			return wmCommandService.updateWmCommandStatus(deviceId, sequence, status);
		}
		return 0;
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
