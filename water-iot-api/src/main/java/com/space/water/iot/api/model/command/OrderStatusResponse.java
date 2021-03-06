package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.BaseResponse;

public class OrderStatusResponse extends BaseResponse{
	//TODO G11 command/callback中使用
	String commandId;//指令Id
	int status;//指令状态（对应CommandCallbackConstants）

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static String toJsonString(OrderStatusResponse response) {
		return JSON.toJSONString(response);
	}
	
	public static OrderStatusResponse fromJson(String json) {
		return JSON.parseObject(json, OrderStatusResponse.class);
	}
}
