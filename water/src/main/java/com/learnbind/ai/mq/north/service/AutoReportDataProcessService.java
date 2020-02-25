package com.learnbind.ai.mq.north.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.EnumReadType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.common.ReportDataType;
import com.learnbind.ai.model.iotbean.report.AutoReport;
import com.learnbind.ai.model.iotbean.report.MeterReportBean;
import com.learnbind.ai.service.business.MeterRecordBusiness;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.iot.WmMeterService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: AutoReportDataProcessService.java
 * @Description: 设备主动上报数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class AutoReportDataProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AutoReportDataProcessService.class);
	
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
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量
	@Autowired
	private CustomerAccountItemService customerAccountItemService;//账单

	/**
	 * @Title: processAutoReportData
	 * @Description: 处理设备上报数据
	 * @param reportData
	 */
	public void processAutoReportData(AutoReport reportData) {

		log.info("----------设备主动上报数据处理");
		
		Long date1 = System.currentTimeMillis();
		// 1、保存设备上报数据到数据库
		this.saveReportData(reportData);

		Long date2 = System.currentTimeMillis();
		log.info("----------保存设备上报数据到数据库用时："+(date2-date1));
		log.info("----------总用时："+(date2-date1));
		
		String deviceId = reportData.getDeviceId();// IOT电信平台设备ID
		Integer dataType = reportData.getDataType();// 数据类型
		
		// 2、收到上报数据后更新表配置与月冻结数据
		if (dataType == ReportDataType.METER_DATA_TYPE_REPORT) {// 如果数据类型是 设备主动上报数据
			// 水表数据封装信息保存（更新MeterConfig信息）
			this.processAutoReportData(deviceId, reportData.getData());
		} else {
			log.info("----------其他数据类型，不做处理，数据类型："+dataType);
		}
		
		Long date3 = System.currentTimeMillis();
		log.info("----------更新表配置与月冻结数据用时："+(date3-date2));
		log.info("----------总用时："+(date3-date1));
		if(dataType == ReportDataType.METER_DATA_TYPE_REPORT) {// 如果数据类型是 设备主动上报数据 时，自动执行保存抄表记录、生成分水量、生成账单、余额自动销账
			// 3、保存到抄表记录
			MeterRecord meterRecord = this.saveMeterRecord(deviceId, reportData.getReportData());
			Long date4 = System.currentTimeMillis();
			log.info("----------保存抄表记录用时："+(date4-date3));
			log.info("----------总用时："+(date4-date1));
			if(meterRecord!=null) {
				// 4、生成分水量（生成分水量时会计算水费）
				List<Long> pwIdList = partitionWaterService.generatorPartitionWater(meterRecord);
				
				Long date5 = System.currentTimeMillis();
				log.info("----------生成分水量用时："+(date5-date4));
				log.info("----------总用时："+(date5-date1));
				
				// 5、生成账单，并用余额自动结算
				this.generatorBillAndSettleBill(pwIdList);
				Long date6 = System.currentTimeMillis();
				log.info("----------生成账单并销账用时："+(date6-date4));
				log.info("----------总用时："+(date6-date1));
			}else {
				log.info("----------保存抄表记录异常");
			}
		}
	}
	
	// --------------------------------保存到抄表记录--------------------------------------------------------------------------------------------------
	/**
	 * @Title: saveMeterRecord
	 * @Description: 保存抄表记录
	 * @param deviceId
	 * @param meterReport
	 * @return 
	 */
	private MeterRecord saveMeterRecord(String deviceId, MeterReportBean meterReport) {
		
		//Date sysDate = new Date();//系统日期
		Long meterId = metersService.getMeterId(deviceId);//获取表计ID		
		if(meterId==null) {
			log.info("未查询到对应的表计信息，设备ID："+deviceId);
			return null;
		}
		CustomerMeter cm = customerMeterService.getCustomerByMeterId(meterId);//查询客户表计关系表
		Long customerId = cm.getCustomerId();//客户ID
		//查询最后一次抄表记录
		//MeterRecord lastMeterRecord = meterRecordService.getLastMeterRecord(customerId, null, meterId);
		//获取本次表底
		BigDecimal currReadMeter = meterRecordBusiness.getCurrReadMeter(meterReport);
		
	    //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date meterTime = meterReport.getMeterTime();
		String period = sdf.format(meterTime);
		
		//保存抄表记录
		MeterRecord record = new MeterRecord();
		record.setCustomerId(customerId);
		record.setMeterId(meterId);
		record.setPeriod(period);
		record.setCurrRead(currReadMeter.toString());
		record.setReadMode(EnumReadMode.READ_REMOTE.getCode());
		record.setReadType(EnumReadType.NORMAL_READ.getValue());
		MeterRecord meterRecord = meterRecordService.saveMeterRecord(record, null, null);
//		if(rows>0) {
//			//查询最后一次抄表记录
//			MeterRecord lastMeterRecord = meterRecordService.getLastMeterRecord(customerId, null, meterId);
//			return lastMeterRecord;
//		}
		return meterRecord;
	}
	
	// --------------------------------保存到抄表记录--------------------------------------------------------------------------------------------------
	/**
	 * @Title: generatorBillAndSettleBill
	 * @Description: 生成账单并结算账单
	 * @param partitionWaterIdList 
	 */
	private void generatorBillAndSettleBill(List<Long> partitionWaterIdList) {
		for(Long pwId : partitionWaterIdList) {
			//查询生成的分水量
			PartitionWater pw = partitionWaterService.selectByPrimaryKey(pwId);
			//生成账单
			Long waterFeeBillId = customerAccountItemService.autoGeneratorWaterFeeBill(pw);
			if(waterFeeBillId!=null) {
				//查询水费账单
				CustomerAccountItem waterFeeBill = customerAccountItemService.selectByPrimaryKey(waterFeeBillId);
				BigDecimal oweAmount = BigDecimalUtils.subtract(waterFeeBill.getCreditAmount(), waterFeeBill.getDebitAmount());//计算水费账单欠费金额
				if(BigDecimalUtils.greaterThan(oweAmount, new BigDecimal(0.00))) {//如果水费账单欠费金额>0时，自动结算
					//结算账单
					int rows = customerAccountItemService.balanceAutoSettlement(waterFeeBill);
					if(rows>0) {//rows>0时余额自动结算成功
						log.info("----------余额自动结算账单成功");
					}else if(rows==0) {//rows=0时余额自动结算失败
						log.info("----------余额自动结算账单异常，请手动操作结算账单");
					}else {//rows<0时余额不足
						log.info("----------余额不足");
					}
				}else {
					log.info("当前账单欠费金额<=0，不需要自动结算，账单ID："+waterFeeBillId);
				}
				
			}else {
				log.info("----------生成账单异常");
			}
		}
		
	}

	// --------------------------------数据类型是 设备主动上报数据 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processAutoReportData
	 * @Description: 主动上报数据处理，更新meter_config
	 * @param deviceId
	 * @param data	原始HEX数据中数据域部分解析后的json数据
	 * @return 
	 */
	private int processAutoReportData(String deviceId, String data) {
		// TODO 水表数据封装信息保存（更新MeterConfig信息）
		// TODO 需要重点测试
		MeterReportBean reportBean = MeterReportBean.fromJson(data);
		if (reportBean != null) {

			String meterReportJSON = reportBean.toJsonString(reportBean);// 转成JSON

			// 水表月冻结信息，更新数据库meters表meter_freeze内容
			Example example = new Example(Meters.class);
			example.createCriteria().andEqualTo("deviceId", deviceId);
			Meters meter = new Meters();
			meter.setMeterConfig(meterReportJSON);
			return metersService.updateByExampleSelective(meter, example);
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
		Meters meter = new Meters();
		meter.setDeviceId(deviceId);
		List<Meters> meterList = metersService.select(meter);
		if (meterList != null && meterList.size() > 0) {
			return meterList.get(0).getId();
		}
		return null;
	}

}
