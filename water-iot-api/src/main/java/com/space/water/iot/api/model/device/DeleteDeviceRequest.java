package com.space.water.iot.api.model.device;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.config.Constants;
import com.space.water.iot.api.model.common.BaseRequest;
import com.space.water.iot.api.model.iot.auth.LoginReq;

public class DeleteDeviceRequest extends BaseRequest{

	private String deviceId;//设备ID
	private String appId = Constants.APP_ID;//应用ID（不用传该参数）
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}

	public static DeleteDeviceRequest fromJson(String json) {
		return JSON.parseObject(json, DeleteDeviceRequest.class);
	}
	public static Map<String, Object> toMap(DeleteDeviceRequest deleteReq) {
		Map<String, Object> result = new HashMap<>();

		result.put("deviceId", deleteReq.getDeviceId());
		result.put("appId", Constants.APP_ID);
		
		return result;
	}
}
