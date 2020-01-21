package com.learnbind.ai.ccbwechat.entity;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.ccbwechat
 *
 * @Title: CCBResponseOrderParam.java
 * @Description: 向CCB请求下单:响应参数实体
 *
 * @author lenovo
 * @date 2019年12月27日 下午2:03:23
 * @version V1.0 
 *
 */
public class CCBResponseOrderParam {	

	
	private String SUCCESS="";
	private String PAYURL="";
	
	public String getPAYURL() {
		return PAYURL;
	}
	public void setPAYURL(String pAYURL) {
		PAYURL = pAYURL;
	}
	public String getSUCCESS() {
		return SUCCESS;
	}
	public void setSUCCESS(String sUCCESS) {
		SUCCESS = sUCCESS;
	}
	
	
}
