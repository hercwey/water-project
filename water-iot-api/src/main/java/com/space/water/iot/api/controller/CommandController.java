package com.space.water.iot.api.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.config.MQTags;
import com.space.water.iot.api.model.command.OrderStatusResponse;
import com.space.water.iot.api.model.common.CommandCallbackConstants;
import com.space.water.iot.api.model.iot.command.CommandBean;
import com.space.water.iot.api.model.iot.command.CommandCallbackBean;
import com.space.water.iot.api.rocketmq.Producer;
import com.space.water.iot.api.rocketmq.RocketTopicConfig;
import com.space.water.iot.api.service.ICommandService;
import com.space.water.iot.api.service.IDeviceService;
import com.space.water.iot.api.util.LogUtil;

@Controller
@RequestMapping("/cmd")
public class CommandController {

    @Autowired
    ICommandService commandService;
    @Autowired
    IDeviceService deviceService;
    @Autowired
    Producer producer;
    @Autowired
    RocketTopicConfig topicConfig;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> send(@RequestBody String data) {
    	LogUtil.info("-----------------------------");
    	LogUtil.info("| 调用发送指令接口");
    	LogUtil.info("| data:" + data);
    	LogUtil.info("-----------------------------");
    	
        CommandBean commandBean = CommandBean.parseJson(data);
        //TODO G11 将指令发送到电信平台
        commandService.postAsynCommand(commandBean);
        //因为调用异步发送指令，所以此处无电信平台反馈信息，callback中获得执行结果
        //TODO G11 此处需要向《营收子系统》反馈指令状态为已下发到电信平台
        return ResponseEntity.ok(JsonResult.success(JsonResult.SUCCESS, JsonResult.STATUS_SUCCESS).toString());
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> callback(@RequestBody String data) {
    	LogUtil.info("-----------------------------");
    	LogUtil.info("| 下发指令返回执行结果");
    	LogUtil.info("| data:" + data);
    	LogUtil.info("-----------------------------");

        CommandCallbackBean commandResultBean = CommandCallbackBean.parseJson(data);
        //TODO G11 直接将执行结果，返回给《营收子系统》
		OrderStatusResponse response = new OrderStatusResponse();
		response.setCommandId(commandResultBean.getCommandId());
		int status = CommandCallbackConstants.COMMAND_STATUS_SENT;
		if (data.contains(CommandCallbackConstants.COMMAND_CALLBACK_STATUS_SUCCESS)) {
			status = CommandCallbackConstants.COMMAND_STATUS_SUCCESS;
		} else if (data.contains(CommandCallbackConstants.COMMAND_CALLBACK_STATUS_DELIVERED)) {
			status = CommandCallbackConstants.COMMAND_STATUS_DELIVERED;
		} else if (data.contains(CommandCallbackConstants.COMMAND_CALLBACK_STATUS_FAILED)) {
			status = CommandCallbackConstants.COMMAND_STATUS_FAILED;
		} else if (data.contains(CommandCallbackConstants.COMMAND_CALLBACK_STATUS_PENDING)) {
			status = CommandCallbackConstants.COMMAND_STATUS_PENDING;
		} else if (data.contains(CommandCallbackConstants.COMMAND_CALLBACK_STATUS_TIMEOUT)) {
			status = CommandCallbackConstants.COMMAND_STATUS_TIMEOUT;
		}
		
		response.setStatus(status);
		response.setTime(new Date(System.currentTimeMillis()));
		producer.sendNorth(OrderStatusResponse.toJsonString(response), topicConfig.getTagOrderStatus());

        return ResponseEntity.ok(JsonResult.success(JsonResult.SUCCESS,data).toString());
    }
}
