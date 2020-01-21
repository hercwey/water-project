package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: FailInvoice.java
 * @Description: 发票状态更新响应报文-失败发票列表(业务主体)
 *
 * @author lenovo
 * @date 2019年10月19日 上午10:35:04
 * @version V1.0 
 *
 */
public class FailInvoice {
	/*
		FPDM	发票代码	字符	20		
		FPHM	发票号码	字符	20		
		XLH	序列号	字符			
		CODE	错误代码	字符			
		MESS	错误信息	字符
	*/			
	
	private String FPDM;//	发票代码	字符	20		
	private String FPHM; //	发票号码	字符	20		
	private String XLH;//	序列号	字符			
	private String CODE;//	错误代码	字符			
	private String MESS;//	错误信息	字符			
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
	public String getXLH() {
		return XLH;
	}
	public void setXLH(String xLH) {
		XLH = xLH;
	}
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getMESS() {
		return MESS;
	}
	public void setMESS(String mESS) {
		MESS = mESS;
	}

	

}
