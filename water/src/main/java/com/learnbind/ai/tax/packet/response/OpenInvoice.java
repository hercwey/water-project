package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: OpenInvoice.java
 * @Description: 开据发票响应包-主体
 *
 * @author lenovo
 * @date 2019年10月19日 上午1:32:20
 * @version V1.0 
 *
 */
public class OpenInvoice {
	/*
	RETCODE	错误代码	字符	100	是	
	RETMSG	错误信息	字符	100	是  
	  
	JE		金额	数值	16,2		
	SE		税额	数值	16,2		
	KPRQ	开票日期	日期	10		格式		yyyy-MM-dd hh:mm:ss
	FPDM	发票代码	字符	20		
	FPHM	发票号码	字符	20		
	JYM		校验码	字符	20	使用JP60ES注册文件开具的电子发票的返回报文中，该节点有值
	MW		密文	字符	200		使用JP60ES注册文件开具的电子发票的返回报文中，该节点有值，密文中的“<”和“>”转义为“&lt;”和“&gt;”
	SIGN	全票面签名	字符	600		使用JP60ES注册文件开具的电子发票的返回报文中，该节点有值
	SYZFPDM	上一张发票代码	字符	20		
	SYZFPHM	上一张发票号码	字符	20		
	SYZFPZL	上一张发票种类	字符	2		固定值：
										0：专用发票 
										2：普通发票
										12：机动车票
										51：电子发票
										-1：如果上一张发票的代码、号码为空，则该字段返回为-1，表示没有上一张发票的信息
	*/
	
	private String RETCODE;  //返回代码
	private String RETMSG;  //返回代码所对应消息
	
	private String JE;  	//	金额	  数值	16,2		
	private String SE;  	//	税额	  数值	16,2		
	private String KPRQ;  	//	开票日期	日期	10	格式  	yyyy-MM-dd hh:mm:ss
	private String FPDM;  	//	发票代码	字符	20		
	private String FPHM;  	//	发票号码	字符	20		
	private String JYM;		//	校验码	字符	20	使用JP60ES注册文件开具的电子发票的返回报文中，该节点有值
	private String MW;  	//	密文	字符	200		使用JP60ES注册文件开具的电子发票的返回报文中，该节点有值，密文中的“<”和“>”转义为“&lt;”和“&gt;”
	private String SIGN;	//	全票面签名	字符	600		使用JP60ES注册文件开具的电子发票的返回报文中，该节点有值
	private String SYZFPDM;  	//	上一张发票代码	字符	20		
	private String SYZFPHM;		//	上一张发票号码	字符	20		
	private String SYZFPZL; 	//	上一张发票种类	字符	2		固定值：
	
	public String getJE() {
		return JE;
	}
	public void setJE(String jE) {
		JE = jE;
	}
	public String getSE() {
		return SE;
	}
	public void setSE(String sE) {
		SE = sE;
	}
	public String getKPRQ() {
		return KPRQ;
	}
	public void setKPRQ(String kPRQ) {
		KPRQ = kPRQ;
	}
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
	public String getJYM() {
		return JYM;
	}
	public void setJYM(String jYM) {
		JYM = jYM;
	}
	public String getMW() {
		return MW;
	}
	public void setMW(String mW) {
		MW = mW;
	}
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
	public String getSYZFPDM() {
		return SYZFPDM;
	}
	public void setSYZFPDM(String sYZFPDM) {
		SYZFPDM = sYZFPDM;
	}
	public String getSYZFPHM() {
		return SYZFPHM;
	}
	public void setSYZFPHM(String sYZFPHM) {
		SYZFPHM = sYZFPHM;
	}
	public String getSYZFPZL() {
		return SYZFPZL;
	}
	public void setSYZFPZL(String sYZFPZL) {
		SYZFPZL = sYZFPZL;
	}
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
	

}
