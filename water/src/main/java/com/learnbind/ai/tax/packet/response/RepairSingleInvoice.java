package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: RepairSingleInvoice.java
 * @Description: 单张发票修复-响应报文
 *
 * @author lenovo
 * @date 2019年10月20日 上午12:43:42
 * @version V1.0 
 *
 */
public class RepairSingleInvoice {
	
	/*
	  RETCODE	错误代码	字符	100	是	
	  RETMSG	错误信息	字符	100	是
	  FPDM 发票代码 字符 20 
	  FPHM 发票号码 字符 20	  
	 */		

	private String RETCODE;	//	错误代码	字符	100	是	
	private String RETMSG; 	//	错误信息	字符	100	是
	private String FPDM;	// 发票代码 字符 20 
	private String FPHM;	// 发票号码 字符 20
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
	public String getFPDM() {
		return FPDM;
	}
	public void setFPDM(String fPDM) {
		FPDM = fPDM;
	}
	public String getFPHM() {
		return FPHM;
	}
	public void setFPHM(String fPHM) {
		FPHM = fPHM;
	}
	
	
	

}
