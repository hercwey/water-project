package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.iotbean.common.DeviceParams;

public class ConfigParamsRequest extends BaseCommandRequest {
	DeviceParams deviceParams;//设备详细参数

	public DeviceParams getDeviceParams() {
		return deviceParams;
	}

	public void setDeviceParams(DeviceParams deviceParams) {
		this.deviceParams = deviceParams;
	}

	public static String toJsonString(ConfigParamsRequest configBean) {
		return JSON.toJSONString(configBean);
	}

	public static ConfigParamsRequest fromJson(String json) {
		return JSON.parseObject(json, ConfigParamsRequest.class);
	}

	public static void main(String[] args) {
		System.out.println(ConfigParamsRequest.toJsonString(new ConfigParamsRequest()));
	}
}
