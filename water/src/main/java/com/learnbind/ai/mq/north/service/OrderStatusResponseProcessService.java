package com.learnbind.ai.mq.north.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.model.iot.TestCommandBean;
import com.learnbind.ai.model.iotbean.command.OrderStatusResponse;
import com.learnbind.ai.service.iot.ICommandService;

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
    ICommandService commandService;
	
	/**
	 * @Title: processResponseData
	 * @Description: 处理响应数据
	 */
	public void processResponseData(OrderStatusResponse orderStatusRsp) {

		log.debug("----------命令执行状态响应数据处理");
		
		String commandId = orderStatusRsp.getCommandId();//指令ID
		int status = orderStatusRsp.getStatus();//指令状态
		
		log.debug("----------指令状态："+status);
		
		//CommandCallbackConstants
		//FIXME G11 判断指令执行结果，逻辑待优化
//        if (orderStatusRsp.contains(TestCommandCallbackBean.COMMAND_CALLBACK_STATUS_DELIVERED) || data.contains(TestCommandCallbackBean.COMMAND_CALLBACK_STATUS_SENT) || data.contains(TestCommandCallbackBean.COMMAND_CALLBACK_STATUS_SUCCESS)) {
//        	commandBean.setDatabaseStatus(2);
//		} else {
//            commandBean.setDatabaseStatus(-1);
//		}
		TestCommandBean commandBean = new TestCommandBean();
        //commandBean.setDeviceId(deviceId);
        commandBean.setCommandId(commandId);
        commandBean.setDatabaseStatus(status);
        commandService.updateByDeviceCommand(commandBean);
		
	}
	
}
