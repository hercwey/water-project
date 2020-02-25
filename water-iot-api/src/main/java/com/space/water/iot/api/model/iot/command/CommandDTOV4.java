package com.space.water.iot.api.model.iot.command;

import com.alibaba.fastjson.JSONObject;

public class CommandDTOV4 {
	/**
	 * 	serviceId	必选	String(1-64)	命令对应的服务ID，不能为空字符串
	 *	method	必选	String(1-128)	命令服务下具体的命令名称，服务属性等，不能为空字符串
	 *	paras	可选	ObjectNode	命令参数的jsonString，具体格式需要应用和设备约定。
	 */
	
	private String serviceId;
	private String method;
	private JSONObject paras;
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
	public JSONObject getParas() {
		return paras;
	}
	public void setParas(JSONObject paras) {
		this.paras = paras;
	}
	
	
}
