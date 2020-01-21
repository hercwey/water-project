package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: InvalidateInvoice.java
 * @Description: 作废发票业务实体-响应数据包
 *
 * @author lenovo
 * @date 2019年10月19日 上午9:31:55
 * @version V1.0 
 *
 */
public class InvalidateInvoice {
	/*
	
	RETCODE	错误代码	字符	100	是	
	RETMSG	错误信息	字符	100	是	
	
	FPZL	发票种类	字符	2		固定值：
							0：专用发票 
							2：普通发票
							12：机动车票
							51：电子发票
	FPHM	发票号码	字符	20		
	FPDM	发票代码	字符	20
		
	*/
	
	private String RETCODE;  //	错误代码	字符	100	是	
	private String RETMSG;  //	错误信息	字符	100	是	
	
	private String FPZL;  //	发票种类	字符	2		固定值：
						//	0：专用发票 
						//	2：普通发票
						//	12：机动车票
						//	51：电子发票
	private String FPHM;  //	发票号码	字符	20		
	private String FPDM;  //	发票代码	字符	20
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
	public String getFPZL() {
		return FPZL;
	}
	public void setFPZL(String fPZL) {
		FPZL = fPZL;
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
