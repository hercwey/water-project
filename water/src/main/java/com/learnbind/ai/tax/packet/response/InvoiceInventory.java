package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: InvoiceInventory.java
 * @Description: 响应数据包-发票库存-业务主体
 *
 * @author lenovo
 * @date 2019年10月18日 下午1:24:35
 * @version V1.0 
 *
 */
public class InvoiceInventory {
	/*
	RETCODE	错误代码	字符	100	是	
	RETMSG	错误信息	字符	100	是
	FPHM	发票号码	字符	20		
	FPDM	发票代码	字符	20		
	KCFPSL	发票库存数量	数值			
	JSSBRQ	金税设备日期	日期	19		格式yyyy-MM-dd hh:mm:ss	
									年月日的格式以本地短时间格式为准
	XFSH	销方税号	字符	20		
	SCFS	上传方式	字符	2		固定值
										0：手动上传
										1：自动上传
	KPDH	开票点号	字符	3		
	CSQBZ	是否已到抄税期	字符	2		固定值
									0：未到抄税期
									1：已到抄税期
	SSQBZ	是否已到锁死期	字符	2		固定值
									0：未到抄税期
									1：已到锁死期
	KPFS	开票方式	字符	2		固定值
									0：单机开票
									1：服务器开票
	KPFWQH	开票服务器号	字符	3		
	JSPH	金税盘号	字符	20		
	XFMC	销方名称	字符	100		
	*/	
	
	private String RETCODE;//	错误代码	字符	100	是	
	private String RETMSG;	 //错误信息	字符	100	是
	private String FPDM;//	发票代码	字符	20
	private String FPHM; //	发票号码	字符	20		
	private String KCFPSL;//	发票库存数量	数值			
	private String JSSBRQ; //	金税设备日期	日期	19		格式yyyy-MM-dd hh:mm:ss	
							//				年月日的格式以本地短时间格式为准
	private String XFSH;  //	销方税号	字符	20		
	private String SCFS;	//	上传方式	字符	2		固定值
									//	0：手动上传
								//		1：自动上传
	private String  KPDH;	//开票点号	字符	3		
	private String  CSQBZ;  //	是否已到抄税期	字符	2		固定值
							//		0：未到抄税期
							//		1：已到抄税期
	private String  SSQBZ;	//是否已到锁死期	字符	2		固定值
								//	0：未到抄税期
								//	1：已到锁死期
	private String KPFS;	//开票方式	字符	2		固定值
							//		0：单机开票
							//		1：服务器开票
	private String  KPFWQH;  //	开票服务器号	字符	3		
	private String  JSPH;  //	金税盘号	字符	20		
	private String  XFMC; 	//	销方名称	字符	100
	
	
	public String getRETCODE() {
		return RETCODE;
	}
	public void setRETCODE(String RETCODE) {
		this.RETCODE = RETCODE;
	}
	public String getRETMSG() {
		return RETMSG;
	}
	public void setRETMSG(String RETMSG) {
		this.RETMSG = RETMSG;
	}
	public String getFPHM() {
		return FPHM;
	}
	public void setFPHM(String FPHM) {
		this.FPHM = FPHM;
	}
	public String getFPDM() {
		return FPDM;
	}
	public void setFPDM(String FPDM) {
		this.FPDM = FPDM;
	}
	public String getKCFPSL() {
		return KCFPSL;
	}
	public void setKCFPSL(String KCFPSL) {
		this.KCFPSL = KCFPSL;
	}
	public String getJSSBRQ() {
		return JSSBRQ;
	}
	public void setJSSBRQ(String JSSBRQ) {
		this.JSSBRQ = JSSBRQ;
	}
	public String getXFSH() {
		return XFSH;
	}
	public void setXFSH(String XFSH) {
		this.XFSH = XFSH;
	}
	public String getSCFS() {
		return SCFS;
	}
	public void setSCFS(String SCFS) {
		this.SCFS = SCFS;
	}
	public String getKPDH() {
		return KPDH;
	}
	public void setKPDH(String KPDH) {
		this.KPDH = KPDH;
	}
	public String getCSQBZ() {
		return CSQBZ;
	}
	public void setCSQBZ(String CSQBZ) {
		this.CSQBZ = CSQBZ;
	}
	public String getSSQBZ() {
		return SSQBZ;
	}
	public void setSSQBZ(String SSQBZ) {
		this.SSQBZ = SSQBZ;
	}
	public String getKPFS() {
		return KPFS;
	}
	public void setKPFS(String KPFS) {
		this.KPFS = KPFS;
	}
	public String getKPFWQH() {
		return KPFWQH;
	}
	public void setKPFWQH(String KPFWQH) {
		this.KPFWQH = KPFWQH;
	}
	public String getJSPH() {
		return JSPH;
	}
	public void setJSPH(String JSPH) {
		this.JSPH = JSPH;
	}
	public String getXFMC() {
		return XFMC;
	}
	public void setXFMC(String XFMC) {
		this.XFMC = XFMC;
	}	
	

}
