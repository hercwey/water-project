package com.learnbind.ai.sms;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.sms
 *
 * @Title: CallbackResponseParams.java
 * @Description: SMS server回调时响应参数
 *
 * @author lenovo
 * @date 2019年8月20日 下午12:39:36
 * @version V1.0 
 *
 */
public class CallbackResponseParams {
	public static int SUCCESS=0;
	public static int FAIL=1;  //非0即可

	/*
		result	是	number	错误码，0表示成功（计费依据），非0表示失败
		errmsg	是	string	错误消息，result 非0时的具体错误信息
	 */
	
	private int result;
	private String errmsg;
	
	public static int getSUCCESS() {
		return SUCCESS;
	}
	public static void setSUCCESS(int sUCCESS) {
		SUCCESS = sUCCESS;
	}
	public static int getFAIL() {
		return FAIL;
	}
	public static void setFAIL(int fAIL) {
		FAIL = fAIL;
	}
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
	
	
	
}
