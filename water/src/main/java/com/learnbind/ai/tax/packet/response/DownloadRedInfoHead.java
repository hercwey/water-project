package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: DownloadRedInfoHead.java
 * @Description: 红字信息表下载-响应报文
 *
 * @author lenovo
 * @date 2019年10月20日 上午11:08:41
 * @version V1.0 
 *
 */
public class DownloadRedInfoHead {
	/*
	XXBLSH	信息表流水号	字符	24		
	XXBBH	信息表编号	字符			
	XXBZTDM	信息表状态	字符			TZD0000、TZD1000表示可以填开红票
	XXBZTMS	信息表描述	字符			
	XXBLX	信息表类型	字符	2		固定值：
	0：正常；
	1：逾期(仅销方开具)
	LPDM	对应蓝票代码	字符			
	LPHM	对应蓝票号码	字符			
	SZLB	税种类别	字符	2		固定值
	1：增值税
	DSLBZ	多税率标志	字符	2		固定值
	0：一票一税率；
	1：一票多税率
	TKRQ	填开日期	日期	10		格式：yyyy-MM-dd
	GFMC	购方名称	字符			
	GFSH	购方税号	字符	20		
	XFMC	销方名称	字符			
	XFSH	销方税号	字符	20		
	HJJE	合计金额	数值	16,2		
	SLV	税率	数值	10,6		多税率时为空
	HJSE	合计税额	数值	16,2		
	SQSM	申请说明	字符			十位数字表示的申请说明，含正常和逾期，固定值，详见说明。
	*/
	
	private String XXBLSH;//	信息表流水号	字符	24		
	private String  XXBBH;//	信息表编号	字符			
	private String XXBZTDM;//	信息表状态	字符			TZD0000、TZD1000表示可以填开红票
	private String XXBZTMS; //	信息表描述	字符			
	private String XXBLX; //	信息表类型	字符	2		固定值：
														//0：正常；
														//1：逾期(仅销方开具)
	private String LPDM;  //	对应蓝票代码	字符			
	private String  LPHM; //	对应蓝票号码	字符			
	private String  SZLB;  //	税种类别	字符	2		固定值	1：增值税
	private String DSLBZ; //	多税率标志	字符	2		固定值
														//0：一票一税率；
														//1：一票多税率
	private String  TKRQ;  //	填开日期	日期	10		格式：yyyy-MM-dd
	private String  GFMC; //	购方名称	字符			
	private String GFSH; //	购方税号	字符	20		
	private String XFMC; //	销方名称	字符			
	private String  XFSH;//	销方税号	字符	20		
	private String  HJJE;//	合计金额	数值	16,2		
	private String SLV;//	税率	数值	10,6		多税率时为空
	private String HJSE;//	合计税额	数值	16,2		
	private String SQSM; //	申请说明	字符			十位数字表示的申请说明，含正常和逾期，固定值，详见说明。
	
	public String getXXBLSH() {
		return XXBLSH;
	}
	public void setXXBLSH(String xXBLSH) {
		XXBLSH = xXBLSH;
	}
	public String getXXBBH() {
		return XXBBH;
	}
	public void setXXBBH(String xXBBH) {
		XXBBH = xXBBH;
	}
	public String getXXBZTDM() {
		return XXBZTDM;
	}
	public void setXXBZTDM(String xXBZTDM) {
		XXBZTDM = xXBZTDM;
	}
	public String getXXBZTMS() {
		return XXBZTMS;
	}
	public void setXXBZTMS(String xXBZTMS) {
		XXBZTMS = xXBZTMS;
	}
	public String getXXBLX() {
		return XXBLX;
	}
	public void setXXBLX(String xXBLX) {
		XXBLX = xXBLX;
	}
	public String getLPDM() {
		return LPDM;
	}
	public void setLPDM(String lPDM) {
		LPDM = lPDM;
	}
	public String getLPHM() {
		return LPHM;
	}
	public void setLPHM(String lPHM) {
		LPHM = lPHM;
	}
	public String getSZLB() {
		return SZLB;
	}
	public void setSZLB(String sZLB) {
		SZLB = sZLB;
	}
	public String getDSLBZ() {
		return DSLBZ;
	}
	public void setDSLBZ(String dSLBZ) {
		DSLBZ = dSLBZ;
	}
	public String getTKRQ() {
		return TKRQ;
	}
	public void setTKRQ(String tKRQ) {
		TKRQ = tKRQ;
	}
	public String getGFMC() {
		return GFMC;
	}
	public void setGFMC(String gFMC) {
		GFMC = gFMC;
	}
	public String getGFSH() {
		return GFSH;
	}
	public void setGFSH(String gFSH) {
		GFSH = gFSH;
	}
	public String getXFMC() {
		return XFMC;
	}
	public void setXFMC(String xFMC) {
		XFMC = xFMC;
	}
	public String getXFSH() {
		return XFSH;
	}
	public void setXFSH(String xFSH) {
		XFSH = xFSH;
	}
	public String getHJJE() {
		return HJJE;
	}
	public void setHJJE(String hJJE) {
		HJJE = hJJE;
	}
	public String getSLV() {
		return SLV;
	}
	public void setSLV(String sLV) {
		SLV = sLV;
	}
	public String getHJSE() {
		return HJSE;
	}
	public void setHJSE(String hJSE) {
		HJSE = hJSE;
	}
	public String getSQSM() {
		return SQSM;
	}
	public void setSQSM(String sQSM) {
		SQSM = sQSM;
	}
	
	
	
}
