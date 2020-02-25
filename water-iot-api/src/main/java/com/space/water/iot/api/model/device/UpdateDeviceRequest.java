package com.space.water.iot.api.model.device;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.BaseRequest;
import com.space.water.iot.api.model.iot.device.UpdateDeviceInfoReqDTO;

public class UpdateDeviceRequest extends BaseRequest{
	
	private String appId;//应用ID（无需传入）
	private String deviceId;//设备ID（电信平台）
    private String manufacturerId;//厂商ID（电信平台）
    private String manufacturerName;//厂商名（电信平台）
    private String deviceType;//设备类型（电信平台）
    private String model;//型号（电信平台）
    private String protocolType;//设备协议类型（电信平台）
    
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getProtocolType() {
		return protocolType;
	}
	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public static UpdateDeviceRequest fromJson(String json) {
		return JSON.parseObject(json, UpdateDeviceRequest.class);
	}
	public static Map<String, Object> toMap(UpdateDeviceRequest modifyReq) {
		Map<String, Object> result = new HashMap<>();
		
		result.put("appId", modifyReq.getAppId());
		result.put("deviceId", modifyReq.getDeviceId());
		result.put("manufacturerId", modifyReq.getManufacturerId());
		result.put("manufacturerName", modifyReq.getManufacturerName());
		result.put("deviceType", modifyReq.getDeviceType());
		result.put("model", modifyReq.getModel());
		result.put("protocolType", modifyReq.getProtocolType());
		
		return result;
	}
    
}
