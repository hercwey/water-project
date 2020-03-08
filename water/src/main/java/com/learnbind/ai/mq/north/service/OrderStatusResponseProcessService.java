package com.learnbind.ai.mq.north.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.iot.WmCommand;
import com.learnbind.ai.model.iotbean.command.OrderStatusResponse;
import com.learnbind.ai.model.iotbean.common.CommandCallbackConstants;
import com.learnbind.ai.service.iot.ICommandService;
import com.learnbind.ai.service.iot.WmCommandService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north.service
 *
 * @Title: OrderStatusResponseProcessService.java
 * @Description: 命令执行状态响应数据处理
 *
 * @author SRD
 * @date 2020年2月23日 上午11:49:25
 * @version V1.0
 *
 */
@Service
public class OrderStatusResponseProcessService {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(OrderStatusResponseProcessService.class);
	
	@Autowired
    private ICommandService commandService;
	@Autowired
    private WmCommandService wmCommandService;
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(OrderStatusResponse orderStatusRsp) {

		log.debug("----------命令执行状态响应数据处理");
		
		Long id = orderStatusRsp.getId();//营收子系统数据库中，指令对应的ID
		String commandId = orderStatusRsp.getCommandId();//iot平台指令ID
		String commandHex = orderStatusRsp.getCommandHex();//生成的设备指令
		int status = orderStatusRsp.getStatus();//指令状态
		Date time = orderStatusRsp.getTime();//时间
		
		log.debug("----------指令状态："+status);
		
		//CommandCallbackConstants
		//FIXME G11 判断指令执行结果，逻辑待优化
//        if (orderStatusRsp.contains(TestCommandCallbackBean.COMMAND_CALLBACK_STATUS_DELIVERED) || data.contains(TestCommandCallbackBean.COMMAND_CALLBACK_STATUS_SENT) || data.contains(TestCommandCallbackBean.COMMAND_CALLBACK_STATUS_SUCCESS)) {
//        	commandBean.setDatabaseStatus(2);
//		} else {
//            commandBean.setDatabaseStatus(-1);
//		}
		
		if(id==null) {
//			TestCommandBean commandBean = new TestCommandBean();
//	        //commandBean.setDeviceId(deviceId);
//	        commandBean.setCommandId(commandId);
//	        commandBean.setDatabaseStatus(status);
//	        commandService.updateByDeviceCommand(commandBean);
			Example example = new Example(WmCommand.class);
			example.createCriteria().andEqualTo("commandId", commandId);
			WmCommand wmCommand = new WmCommand();
			wmCommand.setStatus(status);
			if(status==CommandCallbackConstants.COMMAND_STATUS_SENT) {//如果状态为已发送状态时更新下发时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				wmCommand.setPlatformIssuedTime(sdf.format(time));
			}
			wmCommandService.updateByExampleSelective(wmCommand, example);
		}else {
			WmCommand wmCommand = new WmCommand();
			wmCommand.setId(id);
			wmCommand.setCommandId(commandId);
			wmCommand.setMethodParams(commandHex);
			wmCommand.setStatus(status);
			if(status==CommandCallbackConstants.COMMAND_STATUS_SENT) {//如果状态为已发送状态时更新下发时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				wmCommand.setPlatformIssuedTime(sdf.format(time));
			}
			wmCommandService.updateByPrimaryKeySelective(wmCommand);
		}
		
	}
	
}
