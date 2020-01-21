package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: InvoiceRepair.java
 * @Description: 单张发票修复-报文业务主体
 *
 * @author lenovo
 * @date 2019年10月18日 上午11:37:31
 * @version V1.0 
 *
 */
public class InvoiceRepair {
	/*
	 * FPDM 发票代码 字符 20 是 
	 * FPHM 发票号码 字符 20 是
	 */
	
	private String  FPDM;
	private String FPHM;
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
