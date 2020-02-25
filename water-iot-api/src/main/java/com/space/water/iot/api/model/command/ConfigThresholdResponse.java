package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;

public class ConfigThresholdResponse extends BaseCommandResponse{
	public static String toJsonString(ConfigThresholdResponse request) {
		return JSON.toJSONString(request);
	}

	public static ConfigThresholdResponse fromJson(String json) {
		return JSON.parseObject(json, ConfigThresholdResponse.class);
	}
}
