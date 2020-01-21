package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: UploadInvoice.java
 * @Description: 发票上传返回报文
 *
 * @author lenovo
 * @date 2019年10月19日 上午10:01:35
 * @version V1.0 
 *
 */
public class UploadInvoice {

	/*
		RETCODE	错误代码	字符	100	是	
		RETMSG	错误信息	字符	100	是
		FPHM	发票号码	字符	20		
		FPDM	发票代码	字符	20
	*/
	
	private String RETCODE; //	错误代码	字符	100	是	
	private String RETMSG;  //	错误信息	字符	100	是
	private String FPHM; //	发票号码	字符	20		
	private String FPDM;	//发票代码	字符	20
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
	public String getFPHM() {
		return FPHM;
	}
	public void setFPHM(String fPHM) {
		FPHM = fPHM;
	}
	public String getFPDM() {
		return FPDM;
	}
	public void setFPDM(String fPDM) {
		FPDM = fPDM;
	}
	
	
	

}
