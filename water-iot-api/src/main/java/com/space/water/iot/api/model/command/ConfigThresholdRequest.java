package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;

public class ConfigThresholdRequest extends BaseCommandRequest{
	short threshold;//阈值

	public short getThreshold() {
		return threshold;
	}

	public void setThreshold(short threshold) {
		this.threshold = threshold;
	}
	public static String toJsonString(ConfigThresholdRequest configBean) {
		return JSON.toJSONString(configBean);
	}
	
	public static ConfigThresholdRequest fromJson(String json) {
		return JSON.parseObject(json, ConfigThresholdRequest.class);
	}
}
