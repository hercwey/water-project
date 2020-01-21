package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: InvoiceEmpty.java
 * @Description: 空白发票-业务实体
 *
 * @author lenovo
 * @date 2019年10月17日 下午11:57:40
 * @version V1.0 
 *
 */
public class InvoiceEmpty {
	/* 
	 	FPZL	发票种类	字符	2	固定值：
							0：专用发票 
							2：普通发票
							12：机动车票
							51：电子发票		
	 */
	
	private String FPZL;

	public String getFPZL() {
		return FPZL;
	}

	public void setFPZL(String fPZL) {
		FPZL = fPZL;
	}
	
	
}
