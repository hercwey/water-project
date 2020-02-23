package com.learnbind.ai.mq.north.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.EnumReadType;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.common.ReportDataType;
import com.learnbind.ai.model.iotbean.report.AutoReport;
import com.learnbind.ai.model.iotbean.report.MeterReportBean;
import com.learnbind.ai.service.business.MeterRecordBusiness;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.iot.WmDeviceService;
import com.learnbind.ai.service.iot.WmMeterService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: AutoReportDataProcessService.java
 * @Description: 自动上报数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class AutoReportDataProcessService {

	@Autowired
	private WmDeviceService wmDeviceService;
	@Autowired
	private WmMeterService wmMeterService;
	@Autowired
	private CustomerMeterService customerMeterService;
	@Autowired
	private MetersService metersService;
	@Autowired
	private MeterRecordService meterRecordService;
	@Autowired
	private MeterRecordBusiness meterRecordBusiness;

	/**
	 * @Title: processAutoReportData
	 * @Description: 处理设备上报数据
	 * @param reportData
	 */
	public void processAutoReportData(AutoReport reportData) {

		// 1、保存设备上报数据到数据库
		this.saveReportData(reportData);

		String deviceId = reportData.getDeviceId();// IOT电信平台设备ID
		Integer dataType = reportData.getDataType();// 数据类型
		
		// 2、收到上报数据后更新表配置与月冻结数据
		if (dataType == ReportDataType.METER_DATA_TYPE_RSP_READ_CONFIG
				|| dataType == ReportDataType.METER_DATA_TYPE_RSP_WRITE_CONFIG) {// 如果数据类型是 设备配置信息数据 或 写配置指令返回信息
			// 水表配置信息，更新数据库device表meter_config内容
			this.processReadConfigOrWriteConfigData(deviceId, reportData);
		} else if (dataType == ReportDataType.METER_DATA_TYPE_MONTH_FREEZE) {// 如果数据类型是 设备月冻结数据
			// 水表月冻结信息，更新数据库device表meter_freeze内容
			this.processMonthFreezeData(deviceId, reportData);
		} else if (dataType == ReportDataType.METER_DATA_TYPE_REPORT) {// 如果数据类型是 设备主动上报数据
			// 水表数据封装信息保存（更新MeterConfig信息）
			this.processAutoReportData(deviceId, reportData);
		} else {
			// 其他类型数据不做特殊处理
		}
		
		// 3、保存到抄表记录
		
		
	}
	
	// --------------------------------保存到抄表记录--------------------------------------------------------------------------------------------------
	private int saveMeterRecord(String deviceId, MeterReportBean meterReport) {
		
		Date sysDate = new Date();//系统日期
		Long meterId = metersService.getMeterId(deviceId);//获取表计ID		
		CustomerMeter cm = customerMeterService.getCustomerByMeterId(meterId);//查询客户表计关系表
		Long customerId = cm.getCustomerId();//客户ID
		//查询最后一次抄表记录
		MeterRecord lastMeterRecord = meterRecordService.getLastMeterRecord(customerId, null, meterId);
		//获取本次表底
		BigDecimal currReadMeter = meterRecordBusiness.getCurrReadMeter(meterReport);
		
		//增加抄表记录
		MeterRecord record = new MeterRecord();
		record.setCustomerId(customerId);
		record.setMeterId(meterId);
		record.setCurrRead(currReadMeter.toString());
		record.setReadMode(EnumReadMode.READ_REMOTE.getCode());
		record.setReadType(EnumReadType.NORMAL_READ.getValue());
		int rows = meterRecordService.insertMeterRecord(record, null, null);
		return rows;
	}

	// --------------------------------数据类型是 设备配置信息数据 或 写配置指令返回信息 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processReadConfigOrWriteConfigData
	 * @Description: 数据类型是 设备配置信息数据 或 写配置指令返回信息 的业务处理，更新meter_config
	 * @param deviceId
	 * @param reportData
	 * @return
	 */
	private int processReadConfigOrWriteConfigData(String deviceId, AutoReport reportData) {
		// 水表配置信息，更新数据库device表meter_config内容
		Example example = new Example(WmDevice.class);
		example.createCriteria().andEqualTo("deviceId", deviceId);
		WmDevice wmDevice = new WmDevice();
		wmDevice.setMeterConfig(reportData.getData());
		return wmDeviceService.updateByExampleSelective(wmDevice, example);
	}

	// --------------------------------数据类型是 设备月冻结数据 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processMonthFreezeData
	 * @Description: 数据类型是 设备月冻结数据 的业务处理，更新meter_freeze
	 * @param deviceId
	 * @param reportData
	 * @return
	 */
	private int processMonthFreezeData(String deviceId, AutoReport reportData) {
		// 水表月冻结信息，更新数据库device表meter_freeze内容
		Example example = new Example(WmDevice.class);
		example.createCriteria().andEqualTo("deviceId", deviceId);
		WmDevice wmDevice = new WmDevice();
		wmDevice.setMeterFreeze(reportData.getData());
		return wmDeviceService.updateByExampleSelective(wmDevice, example);
	}

	// --------------------------------数据类型是 设备主动上报数据 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processAutoReportData
	 * @Description: 主动上报数据处理，更新meter_config
	 * @param deviceId
	 * @param reportData
	 * @return 
	 */
	private int processAutoReportData(String deviceId, AutoReport reportData) {
		// TODO 水表数据封装信息保存（更新MeterConfig信息）
		// TODO 需要重点测试
		MeterReportBean reportBean = MeterReportBean.fromJson(reportData.getData());
		if (reportBean != null) {

			String meterReportJSON = reportBean.toJsonString(reportBean);// 转成JSON

			// 水表月冻结信息，更新数据库device表meter_freeze内容
			Example example = new Example(WmDevice.class);
			example.createCriteria().andEqualTo("deviceId", deviceId);
			WmDevice wmDevice = new WmDevice();
			wmDevice.setMeterConfig(meterReportJSON);
			return wmDeviceService.updateByExampleSelective(wmDevice, example);
		}
		return 0;
	}

	// --------------------------------保存设备上报数据--------------------------------------------------------------------------------------------------
	/**
	 * @Title: save
	 * @Description: 保存设备上报数据
	 * @param reportData
	 * @return
	 */
	private int saveReportData(AutoReport reportData) {

		Date sysDate = new Date();// 系统时间
		Long deviceId = this.getDeviceId(reportData.getDeviceId());// 通过IOT电信平台ID查询本地库设备表主键ID

		WmMeter meter = new WmMeter();
		meter.setCreateTime(sysDate);
		meter.setCtrlCode(reportData.getCtrlCode());
		meter.setDataDi(String.valueOf(reportData.getDataDI()));
		meter.setDeviceId(deviceId);
		meter.setEventTime(reportData.getEventTime());
		meter.setFactoryCode(reportData.getFactoryCode());
		meter.setGatewayId(reportData.getGatewayId());
		meter.setJsonData(reportData.getJsonData());
		meter.setMeterAddr(reportData.getMeterAddr());
		meter.setMeterChecksum(reportData.getChecksum().longValue());
		meter.setMeterData(reportData.getData());
		meter.setMeterDataBasic(reportData.getDataBasic());
		meter.setMeterDataType(reportData.getDataType());
		meter.setMeterSequence(reportData.getSequence().longValue());
		meter.setMeterType(reportData.getMeterType().longValue());
		meter.setRequestId(reportData.getRequestId());
		meter.setServiceId(reportData.getServiceId());
		meter.setServiceType(reportData.getServiceType());
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

		WmDevice device = new WmDevice();
		device.setDeviceId(deviceId);
		List<WmDevice> deviceList = wmDeviceService.select(device);
		if (deviceList != null && deviceList.size() > 0) {
			return deviceList.get(0).getId();
		}
		return null;
	}

}
