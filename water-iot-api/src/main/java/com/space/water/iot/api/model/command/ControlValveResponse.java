package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;

public class ControlValveResponse extends BaseCommandResponse{
	public static String toJsonString(ControlValveResponse request) {
		return JSON.toJSONString(request);
	}

	public static ControlValveResponse fromJson(String json) {
		return JSON.parseObject(json, ControlValveResponse.class);
	}
}
