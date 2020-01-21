package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: InvoiceUpload.java
 * @Description: 发票上传-报文主体
 *
 * @author lenovo
 * @date 2019年10月17日 下午11:29:32
 * @version V1.0 
 *
 */
public class InvoiceUpload {
	/* 
	  	FPHM	发票号码	字符	20	是	
		FPDM	发票代码	字符	20	是  
	 */
	
	private String FPHM;
	private String  FPDM;
	
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
