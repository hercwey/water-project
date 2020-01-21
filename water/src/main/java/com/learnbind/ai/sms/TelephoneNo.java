package com.learnbind.ai.sms;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: TelephoneNo.java
 * @Description: 电话号码POJO
 *
 * @author lenovo
 * @date 2019年8月20日 上午11:31:12
 * @version V1.0 
 *
 */
public class TelephoneNo {
	/**
	 * @Fields mobile：电话号码
	 */
	private String mobile;
	/**
	 * @Fields nationcode：国家（或地区）码
	 */
	private String nationcode="86";
	
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getNationcode() {
		return nationcode;
	}
	public void setNationcode(String nationcode) {
		this.nationcode = nationcode;
	}
	
	
	
	
}
