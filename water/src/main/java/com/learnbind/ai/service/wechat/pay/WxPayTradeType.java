package com.learnbind.ai.service.wechat.pay;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechat.pay
 *
 * @Title: WxPayTradeType.java
 * @Description: 微信支付方式
 *
 * @author Administrator
 * @date 2019年8月6日 上午1:07:45
 * @version V1.0 
 *
 */
public enum WxPayTradeType {
	
	/**
	 * @Fields JSAPI：微信公众号支付
	 */
	JSAPI("JSAPI",1),//微信公众号支付
	/**
	 * @Fields NATIVE：微信原生扫码支付
	 */
	NATIVE("NATIVE",2),//微信原生扫码支付
	/**
	 * @Fields APP：app支付
	 */
	APP("APP",3);//app支付
	
	private String value;
	
	private Integer index;

	WxPayTradeType(String value, int index) {
		this.value = value;
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public Integer getIndex() {
		return index;
	}
	
	public static WxPayTradeType getValue(int index){
		
		switch (index) {
		case 1:
			return JSAPI;
		case 2:
			return NATIVE;
		case 3:
			return APP;
		default:
			return null;
		}
		
	}
	
	
}
