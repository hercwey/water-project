package com.space.water.iot.api.model.device;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.space.water.iot.api.model.common.BaseRequest;

public class RegisterDeviceRequest extends BaseRequest{
	
	private String appId;//应用ID（无需传入该参数）
	private String verifyCode;//设备编号，nodeId和verifyCode需要填写相同的值
	private String nodeId;//设备唯一标识
	private String psk;//psk码，用于生成设备鉴权参数（无需传入该参数）
	private Integer timeout;//单位秒，不填使用默认值(180s), 填写0则永不过期，非0表示在指定时间内设备进行绑定，超过时间验证码无效
	
    private String manufacturerId = "JR0912X";//厂商ID（电信平台）
    private String manufacturerName = "JRIWA";//厂商名（电信平台）
    private String deviceType = "JRNBWaterMeter";//设备类型（电信平台）
    private String model = "JR0912Y";//型号（电信平台）
    private String protocolType = "CoAP";//设备协议类型（电信平台）
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getPsk() {
		return psk;
	}

	public void setPsk(String psk) {
		this.psk = psk;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
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

	public static RegisterDeviceRequest fromJson(String json) {
		return JSON.parseObject(json, RegisterDeviceRequest.class);
	}
	public static Map<String, Object> toRegisterParamsMap(RegisterDeviceRequest registerReq) {
		Map<String, Object> result = new HashMap<>();
		
		result.put("appId", registerReq.getAppId());
		result.put("verifyCode", registerReq.getVerifyCode());
		result.put("nodeId", registerReq.getNodeId());
		result.put("psk", registerReq.getPsk());
		result.put("timeout", registerReq.getTimeout());
		
		return result;
	}
}
