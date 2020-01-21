package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: UploadRedInfo.java
 * @Description: 红字信息表上传返回报文-业务主体对象
 *
 * @author lenovo
 * @date 2019年10月19日 下午5:41:46
 * @version V1.0 
 *
 */
public class UploadRedInfo {
	/*
		XXBLSH	信息表流水号	字符	24		
		XXBBH	信息表编号	字符			
		XXBZTDM	信息表状态代码	字符			TZD0000、TZD1000表示可以填开红票
		XXBZTMS	信息表状态描述	字符
	*/
	
	private String XXBLSH;  //	信息表流水号	字符	24		
	private String XXBBH ;//	信息表编号	字符			
	private String XXBZTDM; //	信息表状态代码	字符			TZD0000、TZD1000表示可以填开红票
	private String XXBZTMS;//	信息表状态描述	字符
	public String getXXBLSH() {
		return XXBLSH;
	}
	public void setXXBLSH(String xXBLSH) {
		XXBLSH = xXBLSH;
	}
	public String getXXBBH() {
		return XXBBH;
	}
	public void setXXBBH(String xXBBH) {
		XXBBH = xXBBH;
	}
	public String getXXBZTDM() {
		return XXBZTDM;
	}
	public void setXXBZTDM(String xXBZTDM) {
		XXBZTDM = xXBZTDM;
	}
	public String getXXBZTMS() {
		return XXBZTMS;
	}
	public void setXXBZTMS(String xXBZTMS) {
		XXBZTMS = xXBZTMS;
	}
	
	

}
