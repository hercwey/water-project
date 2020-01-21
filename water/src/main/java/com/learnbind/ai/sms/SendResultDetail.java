package com.learnbind.ai.sms;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: CallbackRequestParams.java
 * @Description: SMS Server请求回调的参数
 *
 * @author lenovo
 * @date 2019年8月20日 下午12:30:40
 * @version V1.0 
 *
 */
public class SendResultDetail {
	
	/* 
	 	user_receive_time	是	string	用户实际接收到短信的时间
		nationcode			是	string	国家（或地区）码
		mobile				是	string	手机号码
		report_status		是	string	实际是否收到短信接收状态，SUCCESS（成功）、FAIL（失败）
		errmsg				是	string	用户接收短信状态码错误信息，参考 状态回执错误码		
		description			是	string	用户接收短信状态描述
		sid					是	string	本次发送标识 ID
	 */
	
	private String user_receive_time;
	private String nationcode;
	private String mobile;
	private String report_status;
	private String errmsg;
	private String description;
	private String sid;
	
	
	public String getUser_receive_time() {
		return user_receive_time;
	}
	public void setUser_receive_time(String user_receive_time) {
		this.user_receive_time = user_receive_time;
	}
	public String getNationcode() {
		return nationcode;
	}
	public void setNationcode(String nationcode) {
		this.nationcode = nationcode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getReport_status() {
		return report_status;
	}
	public void setReport_status(String report_status) {
		this.report_status = report_status;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	
	
}
