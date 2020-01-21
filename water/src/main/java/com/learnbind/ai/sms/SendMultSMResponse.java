package com.learnbind.ai.sms;

import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: SendMultSMResponse.java
 * @Description: 响应-群发短信
 *
 * @author lenovo
 * @date 2019年8月20日 下午12:22:40
 * @version V1.0 
 *
 */
public class SendMultSMResponse {
	/**
      	result	是	number	错误码，0表示成功（计费依据），非0表示失败
		errmsg	是	string	错误消息，result 非0时的具体错误信息
		ext		否	string	用户的 session 内容，腾讯 server 回包中会原样返回
		detail	否	array	结果详细
	 */
	
	private int result;
	private String errmsg;
	private String ext;
	private List<ResultDetail> detail;
		
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
	public List<ResultDetail> getDetail() {
		return detail;
	}
	public void setDetail(List<ResultDetail> detail) {
		this.detail = detail;
	}
	
	
	
}
