package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;

public class AccountStatusReadRequest extends BaseCommandRequest {
	public static String toJsonString(AccountStatusReadRequest request) {
		return JSON.toJSONString(request);
	}

	public static AccountStatusReadRequest fromJson(String json) {
		return JSON.parseObject(json, AccountStatusReadRequest.class);
	}
}
