package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;

public class AccountStatusWriteResponse extends BaseCommandResponse {
	public static String toJsonString(AccountStatusWriteResponse request) {
		return JSON.toJSONString(request);
	}

	public static AccountStatusWriteResponse fromJson(String json) {
		return JSON.parseObject(json, AccountStatusWriteResponse.class);
	}
}
