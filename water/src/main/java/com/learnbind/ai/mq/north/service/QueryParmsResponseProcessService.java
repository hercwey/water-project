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
import com.learnbind.ai.model.iotbean.common.ReportDataType;
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
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(QueryParamsResponse queryParamsRsp) {

		log.debug("----------查询表参数数据响应数据处理");
		// 1、保存设备上报数据到数据库
		this.saveResponseData(queryParamsRsp);
		
		String deviceId = queryParamsRsp.getDeviceId();// IOT电信平台设备ID
		Integer dataType = queryParamsRsp.getDataType();// 数据类型
		
		// 2、收到上报数据后更新表配置与月冻结数据
		if (dataType == ReportDataType.METER_DATA_TYPE_RSP_READ_CONFIG
				|| dataType == ReportDataType.METER_DATA_TYPE_RSP_WRITE_CONFIG) {// 如果数据类型是 设备配置信息数据 或 写配置指令返回信息
			// 水表配置信息，更新数据库device表meter_config内容
			this.processReadConfigOrWriteConfigData(deviceId, queryParamsRsp.getData());
		} else {
			log.debug("----------其他数据类型，不做处理，数据类型："+dataType);
		}
		
	}
	
	// --------------------------------数据类型是 设备配置信息数据 或 写配置指令返回信息 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processReadConfigOrWriteConfigData
	 * @Description: 数据类型是 设备配置信息数据 或 写配置指令返回信息 的业务处理，更新meter_config
	 * @param deviceId
	 * @param data	原始HEX数据中数据域部分解析后的json数据
	 * @return 
	 */
	private int processReadConfigOrWriteConfigData(String deviceId, String data) {
		// 水表配置信息，更新数据库meters表meter_config内容
		Example example = new Example(Meters.class);
		example.createCriteria().andEqualTo("deviceId", deviceId);
		Meters meter = new Meters();
		meter.setMeterConfig(data);
		return metersService.updateByExampleSelective(meter, example);
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
