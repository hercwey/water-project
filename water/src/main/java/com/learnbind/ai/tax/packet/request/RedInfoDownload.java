package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: RedInfoDownload.java
 * @Description: 红字信息表下载-请求报文业务主体
 *
 * @author lenovo
 * @date 2019年10月18日 上午10:28:24
 * @version V1.0 
 *
 */
public class RedInfoDownload {
	
	/* 
	 
	 NSRSBH	纳税人识别号	字符	2	是	
	JSPH	金税盘号	字符	100	是	
	KPDH	开票点号	字符	20	是	
	YQZT	逾期状态	字符	2	是	固定值：
	N：非逾期
	Y：逾期
	TKRQQ	填开日期起	日期	10	否	格式：
	yyyy-MM-dd
	TKRQZ	填开日期止	日期	10	否	格式：
	yyyy-MM-dd
	GFSH	购方税号	字符	20	否	
	XFSH	销方税号	字符	20	否	
	XXBBH	信息表编号	字符	100	否	
	XXBFW	信息表下载范围	字符	2	是	固定值：
	0：全部
	1：本企业申请
	2：其他企业申请
	YH	页号	字符	10	是	从1开始
	MYJLS	每页记录数	字符	10	是  	   
	 *  */	
	
	private String NSRSBH;  //	纳税人识别号	字符	2	是	
	private String JSPH;	 //金税盘号	字符	100	是	
	private String  KPDH;	 //	开票点号	字符	20	是	
	private String  YQZT;   //	逾期状态	字符	2	是	固定值：N：非逾期  Y：逾期
	private String TKRQQ; 	 //	填开日期起	日期	10	否	格式：	yyyy-MM-dd
	private String TKRQZ;  //	填开日期止	日期	10	否	格式：	yyyy-MM-dd
	private String  GFSH;  //	购方税号	字符	20	否	
	private String  XFSH;	//	销方税号	字符	20	否	
	private String XXBBH;  //	信息表编号	字符	100	否	
	private String XXBFW;	//	信息表下载范围	字符	2	是	固定值：	0：全部	1：本企业申请	2：其他企业申请
	private String  YH;  	//	页号	字符	10	是	从1开始
	private String  MYJLS;  //	每页记录数	字符	10	是
	public String getNSRSBH() {
		return NSRSBH;
	}
	public void setNSRSBH(String nSRSBH) {
		NSRSBH = nSRSBH;
	}
	public String getJSPH() {
		return JSPH;
	}
	public void setJSPH(String jSPH) {
		JSPH = jSPH;
	}
	public String getKPDH() {
		return KPDH;
	}
	public void setKPDH(String kPDH) {
		KPDH = kPDH;
	}
	public String getYQZT() {
		return YQZT;
	}
	public void setYQZT(String yQZT) {
		YQZT = yQZT;
	}
	public String getTKRQQ() {
		return TKRQQ;
	}
	public void setTKRQQ(String tKRQQ) {
		TKRQQ = tKRQQ;
	}
	public String getTKRQZ() {
		return TKRQZ;
	}
	public void setTKRQZ(String tKRQZ) {
		TKRQZ = tKRQZ;
	}
	public String getGFSH() {
		return GFSH;
	}
	public void setGFSH(String gFSH) {
		GFSH = gFSH;
	}
	public String getXFSH() {
		return XFSH;
	}
	public void setXFSH(String xFSH) {
		XFSH = xFSH;
	}
	public String getXXBBH() {
		return XXBBH;
	}
	public void setXXBBH(String xXBBH) {
		XXBBH = xXBBH;
	}
	public String getXXBFW() {
		return XXBFW;
	}
	public void setXXBFW(String xXBFW) {
		XXBFW = xXBFW;
	}
	public String getYH() {
		return YH;
	}
	public void setYH(String yH) {
		YH = yH;
	}
	public String getMYJLS() {
		return MYJLS;
	}
	public void setMYJLS(String mYJLS) {
		MYJLS = mYJLS;
	}
	
	

}
