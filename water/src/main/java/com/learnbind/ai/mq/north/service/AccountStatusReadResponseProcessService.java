package com.learnbind.ai.mq.north.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iotbean.command.AccountStatusReadResponse;
import com.learnbind.ai.model.iotbean.common.AccountStatus;
import com.learnbind.ai.model.iotbean.common.CommandCallbackConstants;
import com.learnbind.ai.model.iotbean.common.ReportDataType;
import com.learnbind.ai.service.iot.WmCommandService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

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
public class AccountStatusReadResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AccountStatusReadResponseProcessService.class);
	
	@Autowired
	private MetersService metersService;
	@Autowired
	private WmCommandService wmCommandService;
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(AccountStatusReadResponse accountStatusReadRsp) {

		log.debug("----------读开户状态响应数据处理");
		
		String deviceId = accountStatusReadRsp.getDeviceId();// IOT电信平台设备ID
		Integer dataType = accountStatusReadRsp.getDataType();// 数据类型
		Integer sequence = accountStatusReadRsp.getSequence();//序号
		
		// 2、收到上报数据后更新水表设备开户状态
		if (dataType == ReportDataType.RSP_ACCOUNT_STATUS_READ) {// 如果数据类型是 读开户状态响应数据
			// 水表配置信息，更新数据库device表account_status字段
			byte status = accountStatusReadRsp.getStatus();
			log.info("----------读开户状态响应数据data参数："+accountStatusReadRsp.getData());
			this.processReadStatusResponseData(deviceId, status);
			this.updateCommandStatus(dataType, deviceId, sequence);//更新指令状态为成功
		} else {
			log.debug("----------其他数据类型，不做处理，数据类型："+dataType);
		}
		
	}
	
	// --------------------------------数据类型是 获取开户状态响应数据 的业务处理部分--------------------------------------------------------------------------------------------------
	/**
	 * @Title: processReadStatusResponseData
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param iotDeviceId	IOT电信平台设备ID
	 * @param status	对应AccountStatus.java类状态
	 * @return 
	 */
	private int processReadStatusResponseData(String iotDeviceId, byte status) {
		
		int statusInt = this.getAccountStatus(status);//获取水表开户状态：0=未开户（默认值）；1=已开户；
		
		// 开户状态，更新数据库meters表account_status字段
		Example example = new Example(Meters.class);
		example.createCriteria().andEqualTo("deviceId", iotDeviceId);
		Meters meter = new Meters();
		meter.setAccountStatus(statusInt);
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
	
	/**
	 * @Title: getAccountStatus
	 * @Description: 获取水表开户状态
	 * @param status
	 * @return 
	 */
	private int getAccountStatus(byte status) {
		//开户状态：0=未开户（默认值）；1=已开户；
		Integer statusInt = 0;
		switch (status) {
		case AccountStatus.OPEN:
			statusInt = 0;//0=未开户（默认值）；
			break;
		case AccountStatus.CLOSE:
			statusInt = 1;//1=已开户；
			break;

		default:
			statusInt = 0;//0=未开户（默认值）；
			break;
		}
		return statusInt;
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
