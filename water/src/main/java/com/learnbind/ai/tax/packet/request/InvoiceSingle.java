package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: InvoiceSingle.java
 * @Description: 单张发票查询
 *
 * @author lenovo
 * @date 2019年10月18日 上午10:55:37
 * @version V1.0 
 *
 */
public class InvoiceSingle {
	/* 
	 *  FPZL	发票种类	字符	2	是	固定值：	0：专用发票		2：普通发票		12：机动车票		51：电子发票
		FPHM	发票号码	字符	20	是	
		FPDM	发票代码	字符	20	是	
		XSDJBH	销售单编号	字符	50	否	与发票开具报文中的销售单据编号的内容一致

	 *  */
	
	
	private String FPZL;  //	发票种类	字符	2	是	固定值：	0：专用发票		2：普通发票		12：机动车票		51：电子发票
	private String FPHM;  //	发票号码	字符	20	是	
	private String  FPDM;//	发票代码	字符	20	是	
	private String XSDJBH;  //	销售单编号	字符	50	否	与发票开具报文中的销售单据编号的内容一致
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
	public String getXSDJBH() {
		return XSDJBH;
	}
	public void setXSDJBH(String xSDJBH) {
		XSDJBH = xSDJBH;
	}
	
	
}
