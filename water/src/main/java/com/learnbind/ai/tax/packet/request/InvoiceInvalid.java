package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: InvoiceInvalid.java
 * @Description: 发票作废
 *
 * @author lenovo
 * @date 2019年10月17日 上午9:05:20
 * @version V1.0 
 *
 */
public class InvoiceInvalid {
	/*
	 
		FPZL	发票种类	字符	2	是	固定值：
						0：专用发票 
						2：普通发票
						12：机动车票
						51：电子发票
		FPHM	发票号码	字符	20	是	
		FPDM	发票代码	字符	20	是
	 */
	
	
	private String FPZL;
	private String FPHM;
	private String FPDM;
	
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
