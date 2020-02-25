package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.DeviceParams;
import com.space.water.iot.api.model.common.DeviceParamsFlags;

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
		ConfigParamsRequest request = new ConfigParamsRequest();
		request.setDeviceId("b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb");
		DeviceParams deviceParams = new DeviceParams();
		DeviceParamsFlags flags = new DeviceParamsFlags();
		deviceParams.setConfigFlag(flags);
		request.setDeviceParams(deviceParams);
		System.out.println(ConfigParamsRequest.toJsonString(request));
	}
}
