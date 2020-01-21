package com.learnbind.ai.sms;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: ResultDetail.java
 * @Description: 群发短信结果详情  (数组detail元素参数-参见群发响应)
 *
 * @author lenovo
 * @date 2019年8月20日 下午12:24:32
 * @version V1.0 
 *
 */
public class ResultDetail {

	/*
	 	result	是	number	错误码，0表示成功（计费依据），非0表示失败，参考 错误码
		errmsg	是	string	错误消息，result 非0时的具体错误信息
		fee		否	number	短信计费的条数，计费规则请参考 国内短信内容长度计算规则 或 国际/港澳台短信内容长度计算规则
		mobile	是	string	手机号码
		nationcode	是	string	国家码
		sid		否	string	本次发送标识 ID，标识一次短信下发记录
	 */
	
	private int result;
	private String errmsg;
	private int fee;
	private String mobile;
	private String nationcode;
	private String sid;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
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
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	
	
	
	
}
