package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;

public class AccountStatusWriteRequest extends BaseCommandRequest {
	byte status;//开户状态
	
	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public static String toJsonString(AccountStatusWriteRequest request) {
		return JSON.toJSONString(request);
	}

	public static AccountStatusWriteRequest fromJson(String json) {
		return JSON.parseObject(json, AccountStatusWriteRequest.class);
	}
}
