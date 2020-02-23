package com.learnbind.ai.model.iotbean.iot.auth;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class LoginReq {

	private String appId;
	private String secret;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}

	public static LoginReq fromJson(String json) {
		return JSON.parseObject(json, LoginReq.class);
	}
	public static Map<String, String> toMap(LoginReq loginReq) {
		Map<String, String> result = new HashMap<>();
		
		result.put("appId", loginReq.getAppId());
		result.put("secret", loginReq.getSecret());
		
		return result;
	}
	
}
