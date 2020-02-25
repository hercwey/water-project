package com.learnbind.ai.mq.north.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.command.QueryMonthDataResponse;
import com.learnbind.ai.model.iotbean.common.ReportDataType;
import com.learnbind.ai.service.iot.WmMeterService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: QueryMonthDataResponseProcessService.java
 * @Description: 查询月冻结数据响应数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class QueryMonthDataResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(QueryMonthDataResponseProcessService.class);
	
	@Autowired
	private WmMeterService wmMeterService;
	@Autowired
	private MetersService metersService;
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(QueryMonthDataResponse queryMonthDataRsp) {

		log.debug("----------查询月冻结数据响应数据处理");
		
		// 1、保存设备上报数据到数据库
		this.saveResponseData(queryMonthDataRsp);
		
		String deviceId = queryMonthDataRsp.getDeviceId();// IOT电信平台设备ID
		Integer dataType = queryMonthDataRsp.getDataType();// 数据类型
		
		// 2、收到上报数据后更新表配置与月冻结数据
		if (dataType == ReportDataType.METER_DATA_TYPE_MONTH_FREEZE) {// 如果数据类型是 设备月冻结数据
			// 水表月冻结信息，更新数据库device表meter_freeze内容
			this.processMonthFreezeData(deviceId, queryMonthDataRsp.getData());
		} else {
			log.debug("----------其他数据类型，不做处理，数据类型："+dataType);
		}
		
	}
	
	// --------------------------------数据类型是 设备月冻结数据 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processMonthFreezeData
	 * @Description: 数据类型是 设备月冻结数据 的业务处理，更新meter_freeze
	 * @param deviceId
	 * @param data	原始HEX数据中数据域部分解析后的json数据
	 * @return 
	 */
	private int processMonthFreezeData(String deviceId, String data) {
		// 水表月冻结信息，更新数据库meters表meter_freeze内容
		Example example = new Example(Meters.class);
		example.createCriteria().andEqualTo("deviceId", deviceId);
		Meters meter = new Meters();
		meter.setMeterFreeze(data);
		return metersService.updateByExampleSelective(meter, example);
	}
	
	// --------------------------------保存设备上报数据--------------------------------------------------------------------------------------------------
	/**
	 * @Title: save
	 * @Description: 保存设备上报数据
	 * @param queryMonthDataRsp
	 * @return
	 */
	private int saveResponseData(QueryMonthDataResponse queryMonthDataRsp) {

		Date sysDate = new Date();// 系统时间
		Long deviceId = this.getDeviceId(queryMonthDataRsp.getDeviceId());// 通过IOT电信平台ID查询本地库设备表主键ID

		WmMeter meter = new WmMeter();
		meter.setCreateTime(sysDate);
		meter.setCtrlCode(queryMonthDataRsp.getCtrlCode());
		meter.setDataDi(String.valueOf(queryMonthDataRsp.getDataDI()));
		meter.setDeviceId(deviceId);
		meter.setEventTime(queryMonthDataRsp.getEventTime());
		meter.setFactoryCode(queryMonthDataRsp.getFactoryCode());
		meter.setGatewayId(queryMonthDataRsp.getGatewayId());
		meter.setJsonData(queryMonthDataRsp.getJsonData());
		meter.setMeterAddr(queryMonthDataRsp.getMeterAddr());
		meter.setMeterChecksum(queryMonthDataRsp.getChecksum().longValue());
		meter.setMeterData(queryMonthDataRsp.getData());
		meter.setMeterDataBasic(queryMonthDataRsp.getDataBasic());
		meter.setMeterDataType(queryMonthDataRsp.getDataType());
		meter.setMeterSequence(queryMonthDataRsp.getSequence().longValue());
		meter.setMeterType(queryMonthDataRsp.getMeterType().longValue());
		meter.setRequestId(queryMonthDataRsp.getRequestId());
		meter.setServiceId(queryMonthDataRsp.getServiceId());
		meter.setServiceType(queryMonthDataRsp.getServiceType());
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
