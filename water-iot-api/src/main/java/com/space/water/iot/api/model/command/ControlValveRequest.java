package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;

public class ControlValveRequest extends BaseCommandRequest{
	byte action;//开关阀动作，对应ControlValveType类中的参数

	public byte getAction() {
		return action;
	}

	public void setAction(byte action) {
		this.action = action;
	}

	public static String toJsonString(ControlValveRequest configBean) {
		return JSON.toJSONString(configBean);
	}
	
	public static ControlValveRequest fromJson(String json) {
		return JSON.parseObject(json, ControlValveRequest.class);
	}
}
