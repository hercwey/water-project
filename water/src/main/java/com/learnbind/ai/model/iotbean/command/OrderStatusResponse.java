package com.learnbind.ai.model.iotbean.command;

import com.learnbind.ai.model.iotbean.common.BaseResponse;

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
	
}
