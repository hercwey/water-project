package com.learnbind.ai.model.iotbean.command;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.iotbean.common.DeviceParams;

public class QueryParamsResponse extends BaseCommandResponse{
	DeviceParams deviceParams;//设备详细配置信息
	
	public DeviceParams getDeviceParams() {
		return deviceParams;
	}

	public void setDeviceParams(DeviceParams deviceParams) {
		this.deviceParams = deviceParams;
	}

	public static String toJsonString(QueryParamsResponse configBean) {
		return JSON.toJSONString(configBean);
	}
	
	public static QueryParamsResponse fromJson(String json) {
		return JSON.parseObject(json, QueryParamsResponse.class);
	}
}
