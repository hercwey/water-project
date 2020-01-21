package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: SuccessInvoice.java
 * @Description: 发票状态更新响应报文-更新成功部分(业务实体)
 *
 * @author lenovo
 * @date 2019年10月19日 上午10:29:18
 * @version V1.0 
 *
 */
public class SuccessInvoice {
	/*
		FPDM	发票代码	字符	20		
		FPHM	发票号码	字符	20		
		XLH	序列号	字符
	*/
	
	private String FPDM; 	//	发票代码	字符	20		
	private String FPHM;  	//	发票号码	字符	20		
	private String XLH; 	//	序列号	字符
	
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
	

}
