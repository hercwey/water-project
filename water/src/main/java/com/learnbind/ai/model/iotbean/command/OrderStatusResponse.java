package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.iotbean.common.BaseResponse;

public class OrderStatusResponse extends BaseResponse{
	//TODO G11 command/callback中使用
	Long id;//“营收子系统”数据库中，指令对应id
	
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static String toJsonString(OrderStatusResponse response) {
		return JSON.toJSONString(response);
	}
	
	public static OrderStatusResponse fromJson(String json) {
		return JSON.parseObject(json, OrderStatusResponse.class);
	}
}
