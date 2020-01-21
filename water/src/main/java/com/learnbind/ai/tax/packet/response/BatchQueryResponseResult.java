package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: BatchQueryResponeResult.java
 * @Description: 发票批量查询-响应报文-响应结果
 *
 * @author lenovo
 * @date 2019年10月20日 下午5:36:48
 * @version V1.0 
 *
 */
public class BatchQueryResponseResult {
	
	/*
		UPLOADMODE	上传方式	字符	2	是	固定值
									0：手动上传
									1：自动上传
		RETCODE	错误代码	字符	100	是	
		RETMSG	错误信息	字符	100	是
	*/
	
	private String UPLOADMODE;//	上传方式	字符	2	是	固定值
	//0：手动上传
	//1：自动上传
	private String RETCODE;//	错误代码	字符	100	是	
	private String RETMSG;//	错误信息	字符	100	是
	public String getUPLOADMODE() {
		return UPLOADMODE;
	}
	public void setUPLOADMODE(String uPLOADMODE) {
		UPLOADMODE = uPLOADMODE;
	}
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
