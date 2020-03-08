package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;

public class AccountStatusReadResponse extends BaseCommandResponse {
	byte status;//开户状态
	
	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public static String toJsonString(AccountStatusReadResponse request) {
		return JSON.toJSONString(request);
	}

	public static AccountStatusReadResponse fromJson(String json) {
		return JSON.parseObject(json, AccountStatusReadResponse.class);
	}
}
