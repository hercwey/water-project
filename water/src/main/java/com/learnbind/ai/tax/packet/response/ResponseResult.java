package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: ResonseResult.java
 * @Description: 响应结果-业务实体.公共实体.
 *
 * @author lenovo
 * @date 2019年10月19日 上午10:25:14
 * @version V1.0 
 *
 */
public class ResponseResult {	
	/*
		RETCODE	错误代码	字符	100	是	
		RETMSG	错误信息	字符	100	是	
	 */
	
	private String  RETCODE;
	private String  RETMSG;
	
	public String getRETCODE() {
		return RETCODE;
	}
	public void setRETCODE(String rETCODE) {
		RETCODE = rETCODE;
	}
	public String getRETMSG() {
		return RETMSG;
	}
	public void setRETMSG(String rETMSG) {
		RETMSG = rETMSG;
	}
	

}
