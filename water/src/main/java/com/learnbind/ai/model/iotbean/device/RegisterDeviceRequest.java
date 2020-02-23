package com.learnbind.ai.model.iotbean.device;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.model.iotbean.common.BaseRequest;

public class RegisterDeviceRequest extends BaseRequest{
	
	private String appId;//应用ID（无需传入该参数）
	private String verifyCode;//设备编号，nodeId和verifyCode需要填写相同的值
	private String nodeId;//设备唯一标识
	private String psk;//psk码，用于生成设备鉴权参数（无需传入该参数）
	private Integer timeout;//单位秒，不填使用默认值(180s), 填写0则永不过期，非0表示在指定时间内设备进行绑定，超过时间验证码无效
	
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

	public static RegisterDeviceRequest fromJson(String json) {
		return JSON.parseObject(json, RegisterDeviceRequest.class);
	}
	public static Map<String, Object> toMap(RegisterDeviceRequest registerReq) {
		Map<String, Object> result = new HashMap<>();
		
		result.put("appId", registerReq.getAppId());
		result.put("verifyCode", registerReq.getVerifyCode());
		result.put("nodeId", registerReq.getNodeId());
		result.put("psk", registerReq.getPsk());
		result.put("timeout", registerReq.getTimeout());
		
		return result;
	}
}
