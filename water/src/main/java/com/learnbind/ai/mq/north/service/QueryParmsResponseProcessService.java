package com.learnbind.ai.mq.north.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.command.QueryParamsResponse;
import com.learnbind.ai.model.iotbean.common.CommandCallbackConstants;
import com.learnbind.ai.model.iotbean.common.ReportDataType;
import com.learnbind.ai.service.iot.WmCommandService;
import com.learnbind.ai.service.iot.WmMeterService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: QueryParmsResponseProcessService.java
 * @Description: 查询表参数数据响应数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class QueryParmsResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(QueryParmsResponseProcessService.class);
	
	@Autowired
	private WmMeterService wmMeterService;
	@Autowired
	private MetersService metersService;
	@Autowired
	private WmCommandService wmCommandService;
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(QueryParamsResponse queryParamsRsp) {

		log.debug("----------查询表参数数据响应数据处理");
		// 1、保存设备上报数据到数据库
		this.saveResponseData(queryParamsRsp);
		
		String iotDeviceId = queryParamsRsp.getDeviceId();// IOT电信平台设备ID
		Integer dataType = queryParamsRsp.getDataType();// 数据类型
		Integer sequence = queryParamsRsp.getSequence();//序号
		
		// 2、收到上报数据后更新表配置
//		if (dataType == ReportDataType.METER_DATA_TYPE_RSP_READ_CONFIG
//				|| dataType == ReportDataType.METER_DATA_TYPE_RSP_WRITE_CONFIG) {// 如果数据类型是 设备配置信息数据 或 写配置指令返回信息
		if (dataType == ReportDataType.METER_DATA_TYPE_RSP_READ_CONFIG) {// 如果数据类型是 读表参数配置指令返回信息
			// 水表配置信息，更新数据库device表meter_config内容
			this.processReadConfigResponseData(iotDeviceId, queryParamsRsp.getData());
			this.updateCommandStatus(dataType, iotDeviceId, sequence);
		} else {
			log.debug("----------其他数据类型，不做处理，数据类型："+dataType);
		}
		
	}
	
	// --------------------------------数据类型是 设备配置信息数据 或 写配置指令返回信息 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processReadConfigOrWriteConfigData
	 * @Description: 数据类型是 设备配置信息数据 或 写配置指令返回信息 的业务处理，更新meter_config
	 * @param iotDeviceId
	 * @param data	原始HEX数据中数据域部分解析后的json数据
	 * @return 
	 */
	private int processReadConfigResponseData(String iotDeviceId, String data) {
		log.info("----------读表配置响应数据data参数："+data);
		// 水表配置信息，更新数据库meters表meter_config内容
		Example example = new Example(Meters.class);
		example.createCriteria().andEqualTo("deviceId", iotDeviceId);
		Meters meter = new Meters();
		meter.setMeterConfig(data);
		return metersService.updateByExampleSelective(meter, example);
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
	
	// --------------------------------保存设备上报数据--------------------------------------------------------------------------------------------------
	/**
	 * @Title: save
	 * @Description: 保存设备上报数据
	 * @param queryParamsRsp
	 * @return
	 */
	private int saveResponseData(QueryParamsResponse queryParamsRsp) {

		Date sysDate = new Date();// 系统时间
		Long deviceId = this.getDeviceId(queryParamsRsp.getDeviceId());// 通过IOT电信平台ID查询本地库设备表主键ID

		WmMeter meter = new WmMeter();
		meter.setCreateTime(sysDate);
		meter.setCtrlCode(queryParamsRsp.getCtrlCode());
		meter.setDataDi(String.valueOf(queryParamsRsp.getDataDI()));
		meter.setDeviceId(deviceId);
		meter.setEventTime(queryParamsRsp.getEventTime());
		meter.setFactoryCode(queryParamsRsp.getFactoryCode());
		meter.setGatewayId(queryParamsRsp.getGatewayId());
		meter.setJsonData(queryParamsRsp.getJsonData());
		meter.setMeterAddr(queryParamsRsp.getMeterAddr());
		meter.setMeterChecksum(queryParamsRsp.getChecksum().longValue());
		meter.setMeterData(queryParamsRsp.getData());
		meter.setMeterDataBasic(queryParamsRsp.getDataBasic());
		meter.setMeterDataType(queryParamsRsp.getDataType());
		meter.setMeterSequence(queryParamsRsp.getSequence().longValue());
		meter.setMeterType(queryParamsRsp.getMeterType().longValue());
		meter.setRequestId(queryParamsRsp.getRequestId());
		meter.setServiceId(queryParamsRsp.getServiceId());
		meter.setServiceType(queryParamsRsp.getServiceType());
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
