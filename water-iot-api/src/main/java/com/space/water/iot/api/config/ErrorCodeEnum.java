package com.space.water.iot.api.config;

public enum ErrorCodeEnum {
	PARAM_ERROR(100000, "参数错误");

	private int code;

	private String msg;

	ErrorCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}