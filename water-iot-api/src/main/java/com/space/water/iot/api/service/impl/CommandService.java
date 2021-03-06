package com.space.water.iot.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.space.water.iot.api.common.JsonResult;
import com.space.water.iot.api.config.Constants;
import com.space.water.iot.api.config.MQTags;
import com.space.water.iot.api.model.command.OrderStatusResponse;
import com.space.water.iot.api.model.common.CommandCallbackConstants;
import com.space.water.iot.api.model.iot.command.CommandBean;
import com.space.water.iot.api.model.iot.command.DeviceCommandResp;
import com.space.water.iot.api.rocketmq.Producer;
import com.space.water.iot.api.service.ICommandService;
import com.space.water.iot.api.util.IoTRequestUtil;
import com.space.water.iot.api.util.JsonUtil;

@Service
public class CommandService implements ICommandService {
	@Autowired
	Producer producer;

	@Override
	public JsonResult postCommand(CommandBean commandBean) {

		JsonResult jsonResult = JsonResult.fail(0, "Unknown Error");

		System.out.println("| postCommand 下发控制指令：" + commandBean.getDeviceId() + "==" + commandBean.getCommandId());
		try {
			String urlPostAsyncCmd = Constants.POST_ASYNC_CMD;
			String callbackUrl = Constants.REPORT_CMD_EXEC_RESULT_CALLBACK_URL;

			String deviceId = commandBean.getDeviceId();
			String serviceId = commandBean.getServiceId();
			String method = commandBean.getMethod();

			ObjectNode paras = JsonUtil.convertObject2ObjectNode(commandBean.getParas().toJSONString());

			Map<String, Object> paramCommand = new HashMap<>();
			paramCommand.put("serviceId", serviceId);
			paramCommand.put("method", method);
			paramCommand.put("paras", paras);

			Map<String, Object> paramPostAsyncCmd = new HashMap<>();
			paramPostAsyncCmd.put("deviceId", deviceId);
			paramPostAsyncCmd.put("command", paramCommand);
			paramPostAsyncCmd.put("callbackUrl", callbackUrl);
			paramPostAsyncCmd.put("expireTime", 0);

			jsonResult = IoTRequestUtil.doPostJson(urlPostAsyncCmd, paramPostAsyncCmd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonResult;
	}

	@Async
	@Override
	public void postAsynCommand(CommandBean commandBean) {
		JsonResult jsonResult = postCommand(commandBean);

		String result = jsonResult.getData();
		System.out.println("---------------------------");
		System.out.println("| 控制指令异步发送到IoT平台：" + commandBean.getDeviceId() + "==" + commandBean.getCommandId());
		System.out.println("| 控制指令异步发送完成：" + result);
		System.out.println("---------------------------");

		DeviceCommandResp response = JSON.parseObject(result, DeviceCommandResp.class);

		// TODO G11 调用命令执行状态改变方法，返回给“营收子系统”，状态为，已发送
		OrderStatusResponse orderSatusResponse = new OrderStatusResponse();
		orderSatusResponse.setCommandId(commandBean.getCommandId());
		if (response == null) {
			orderSatusResponse.setStatus(CommandCallbackConstants.COMMAND_STATUS_FAILED);
		} else {
			orderSatusResponse.setCommandId(response.getCommandId());
			orderSatusResponse.setStatus(CommandCallbackConstants.COMMAND_STATUS_SENT);
		}

		producer.sendNorth(OrderStatusResponse.toJsonString(orderSatusResponse), MQTags.ORDER_STATUS);
	}
}
