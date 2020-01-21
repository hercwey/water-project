package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: QuerySingleInvoice.java
 * @Description: 查询单张发票-响应报文
 *
 * @author lenovo
 * @date 2019年10月19日 下午11:17:56
 * @version V1.0 
 *
 */
public class QuerySingleInvoice {
	
	/*

		RETCODE	错误代码	字符	100	是	
		RETMSG	错误信息	字符	100	是  
	
		FPZL	发票号码	字符	2		
		FPHM	发票号码	字符	20		
		FPDM	发票代码	字符	20		
		XSDJBH	销售单据编号	字符	50		
		BJBHSJE	合计不含税金额	数值	16,2		
		HJSE	合计税额	数值	16,2		
		KPRQ	开票日期	日期	10		格式：yyyy-MM-dd hh:mm:ss
		DYBZ	打印标志	字符	2		固定值
		0：未打印
		1：已打印
		FPBSZT	发票报送状态	字符	2		固定值
		0：未报送
		1：已报送
		2：报送失败
		3：报送中
		4：验签失败
		ZFBZ	作废标志	字符	2		固定值
		0：未作废
		1：已作废
		INFOXML	发票信息的XML	字符	  
	 */
	
	private String RETCODE;//	错误代码	字符	100	是	
	private String RETMSG;//	错误信息	字符	100	是  

	private String FPZL;//	发票号码	字符	2		
	private String  FPHM;//	发票号码	字符	20		
	private String FPDM;//	发票代码	字符	20		
	private String XSDJBH;//	销售单据编号	字符	50		
	private String BJBHSJE;//	合计不含税金额	数值	16,2		
	private String HJSE;//	合计税额	数值	16,2		
	private String KPRQ;//	开票日期	日期	10		格式：yyyy-MM-dd hh:mm:ss
	private String DYBZ;//	打印标志	字符	2		固定值
												//0：未打印
												//1：已打印
	private String FPBSZT;//	发票报送状态	字符	2		固定值
												//0：未报送
												//1：已报送
												//2：报送失败
												//3：报送中
												//4：验签失败
	private String ZFBZ;	//作废标志	字符	2		固定值
												//0：未作废
												//1：已作废
	private String INFOXML; //发票信息的XML	字符	
	
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
	public String getXSDJBH() {
		return XSDJBH;
	}
	public void setXSDJBH(String xSDJBH) {
		XSDJBH = xSDJBH;
	}
	public String getBJBHSJE() {
		return BJBHSJE;
	}
	public void setBJBHSJE(String bJBHSJE) {
		BJBHSJE = bJBHSJE;
	}
	public String getHJSE() {
		return HJSE;
	}
	public void setHJSE(String hJSE) {
		HJSE = hJSE;
	}
	public String getKPRQ() {
		return KPRQ;
	}
	public void setKPRQ(String kPRQ) {
		KPRQ = kPRQ;
	}
	public String getDYBZ() {
		return DYBZ;
	}
	public void setDYBZ(String dYBZ) {
		DYBZ = dYBZ;
	}
	public String getFPBSZT() {
		return FPBSZT;
	}
	public void setFPBSZT(String fPBSZT) {
		FPBSZT = fPBSZT;
	}
	public String getZFBZ() {
		return ZFBZ;
	}
	public void setZFBZ(String zFBZ) {
		ZFBZ = zFBZ;
	}
	public String getINFOXML() {
		return INFOXML;
	}
	public void setINFOXML(String iNFOXML) {
		INFOXML = iNFOXML;
	}
	
	
	
}
