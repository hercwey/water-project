package com.space.water.iot.api.model.command;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.BaseRequest;

public class BaseCommandRequest extends BaseRequest{
	
	private String deviceId;//设备ID（电信平台）
    private String serviceId;//服务ID（电信平台）
    private String method;//方法（电信平台）
	
	byte meterType;//表类型
	String meterAddress;//表地址
	String meterFactoryCode;//表厂商代码
	byte sequence;//指令序号
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public byte getMeterType() {
		return meterType;
	}
	public void setMeterType(byte meterType) {
		this.meterType = meterType;
	}
	public String getMeterAddress() {
		return meterAddress;
	}
	public void setMeterAddress(String meterAddress) {
		this.meterAddress = meterAddress;
	}
	public String getMeterFactoryCode() {
		return meterFactoryCode;
	}
	public void setMeterFactoryCode(String meterFactoryCode) {
		this.meterFactoryCode = meterFactoryCode;
	}
	public byte getSequence() {
		return sequence;
	}
	public void setSequence(byte sequence) {
		this.sequence = sequence;
	}

	public static String toJsonString(BaseCommandRequest request) {
		return JSON.toJSONString(request);
	}

	public static BaseCommandRequest fromJson(String json) {
		return JSON.parseObject(json, BaseCommandRequest.class);
	}
}
