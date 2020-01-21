package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: RedInfoHead.java
 * @Description: 红字信息表头
 *
 * @author lenovo
 * @date 2019年10月18日 上午9:32:27
 * @version V1.0 
 *
 */
public class RedInfoHead {
	/*  
	 *  SQFSH	申请方税号	字符	20		
		KPDH	申请方开票点号	字符			
		SBBH	申请方设备编号	字符	12		
		XXBLSH	信息表流水号		字符	24	   ******注:此流水号无法在业务系统中生成.	
		XXBLX	信息表类型		字符	2		固定值：
											0：正常
		DYLPDM	对应蓝票代码	字符			
		DYLPHM	对应蓝票号码	字符			
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
		SLV		税率	数值	10,6		多税率时为空
		HJSE	合计税额	数值	16,2		
		SQSM	申请说明	字符			十位数字表示的申请说明，含正常和逾期，固定值，详见说明。

	 * */
	
	private String SQFSH;	//申请方税号	字符	20		
	private String KPDH;  //	申请方开票点号	字符			
	private String SBBH;   //	申请方设备编号	字符	12		
	private String XXBLSH;//	信息表流水号	字符	24		
	private String XXBLX; //信息表类型	字符	2		固定值：	0：正常
	private String DYLPDM; //对应蓝票代码	字符			
	private String  DYLPHM;	//对应蓝票号码	字符			
	private String SZLB;   //	税种类别	字符	2		固定值
													//1：增值税
	private String DSLBZ;  //多税率标志	字符	2		固定值
												//0：一票一税率；
												//1：一票多税率
	private String TKRQ;  //填开日期	日期	10		格式：yyyy-MM-dd
	private String GFMC;  //购方名称	字符			
	private String GFSH;	//购方税号	字符	20		
	private String XFMC;	//销方名称	字符			
	private String XFSH;  	//销方税号	字符	20		
	private String HJJE;  	//合计金额	数值	16,2		
	private String SLV;  	//	税率	数值	10,6		多税率时为空
	private String  HJSE;	//	合计税额	数值	16,2		
	private String SQSM;	//	申请说明	字符			十位数字表示的申请说明，含正常和逾期，固定值，详见说明。(参见P22)
	
	public String getSQFSH() {
		return SQFSH;
	}
	public void setSQFSH(String sQFSH) {
		SQFSH = sQFSH;
	}
	public String getKPDH() {
		return KPDH;
	}
	public void setKPDH(String kPDH) {
		KPDH = kPDH;
	}
	public String getSBBH() {
		return SBBH;
	}
	public void setSBBH(String sBBH) {
		SBBH = sBBH;
	}
	public String getXXBLSH() {
		return XXBLSH;
	}
	public void setXXBLSH(String xXBLSH) {
		XXBLSH = xXBLSH;
	}
	public String getXXBLX() {
		return XXBLX;
	}
	public void setXXBLX(String xXBLX) {
		XXBLX = xXBLX;
	}
	public String getDYLPDM() {
		return DYLPDM;
	}
	public void setDYLPDM(String dYLPDM) {
		DYLPDM = dYLPDM;
	}
	public String getDYLPHM() {
		return DYLPHM;
	}
	public void setDYLPHM(String dYLPHM) {
		DYLPHM = dYLPHM;
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
