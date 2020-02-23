package com.learnbind.ai.model.iotbean.device;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.iotbean.common.BaseResponse;

public class RegisterDeviceResponse extends BaseResponse{
	private String deviceId;//电信平台生成的设备ID
	private String verifyCode;//设备编码
	private Integer timeout;//超时时间
	private String psk;//psk码，用于生成设备鉴权参数（目前不使用该参数）
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public String getPsk() {
		return psk;
	}

	public void setPsk(String psk) {
		this.psk = psk;
	}

	public static RegisterDeviceResponse fromJson(String json) {
		return JSON.parseObject(json, RegisterDeviceResponse.class);
	}
}
