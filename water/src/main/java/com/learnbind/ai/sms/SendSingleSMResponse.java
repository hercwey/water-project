package com.learnbind.ai.sms;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: SendSingleSMResponse.java
 * @Description: 响应-单发
 *
 * @author lenovo
 * @date 2019年8月20日 下午12:04:51
 * @version V1.0 
 *  
 */
public class SendSingleSMResponse {
	/**
      	result	是	number	错误码，0表示成功（计费依据），非0表示失败，更多详情请参见 错误码
		errmsg	是	string	错误消息，result 非0时的具体错误信息
		ext		否	string	用户的 session 内容，腾讯 server 回包中会原样返回
		fee		否	number	短信计费的条数，计费规则请参考 国内短信内容长度计算规则 或 国际/港澳台短信内容长度计算规则		
		sid		否	string	本次发送标识 ID，标识一次短信下发记录
	 */
	
	private int result;
	private String errmsg;
	private String ext;
	private int fee;
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
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	
	
}
