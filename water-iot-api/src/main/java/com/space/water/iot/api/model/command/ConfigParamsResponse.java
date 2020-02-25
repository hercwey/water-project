package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.DeviceParams;

public class ConfigParamsResponse extends BaseCommandResponse{
	DeviceParams deviceParams;//设备详细参数
	
	public DeviceParams getDeviceParams() {
		return deviceParams;
	}

	public void setDeviceParams(DeviceParams deviceParams) {
		this.deviceParams = deviceParams;
	}

	public static String toJsonString(ConfigParamsResponse configBean) {
		return JSON.toJSONString(configBean);
	}
	
	public static ConfigParamsResponse fromJson(String json) {
		return JSON.parseObject(json, ConfigParamsResponse.class);
	}
}
