package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: DownloadRedInfoDetail.java
 * @Description: 解析-红字信息表下载-响应报文
 *
 * @author lenovo
 * @date 2019年10月20日 下午3:26:35
 * @version V1.0 
 *
 */
public class DownloadRedInfoDetail {
	/*
		SPMC	商品名称	字符	92	是	
		JLDW	计量单位	字符	22	否	
		GGXH	规格型号	字符	40	否	
		DJ	单价	数值	36,15	否	
		SL	数量	数值	36,15	否	
		JE	金额	数值	16,2	是	
		SLV	税率	数值	10,6	是	
		SE	税额	数值	16,2	是	
		HSBZ	含税标志	字符	2	是	固定值
		N：不含税
		Y：含税
	*/
	
	private String SPMC;//	商品名称	字符	92	是	
	private String JLDW;//	计量单位	字符	22	否	
	private String GGXH;//	规格型号	字符	40	否	
	private String DJ;//	单价	数值	36,15	否	
	private String SL;//	数量	数值	36,15	否	
	private String JE;//	金额	数值	16,2	是	
	private String SLV;//	税率	数值	10,6	是	
	private String SE;//	税额	数值	16,2	是	
	private String HSBZ;//	含税标志	字符	2	是	固定值 N：不含税 Y：含税
	
	public String getSPMC() {
		return SPMC;
	}
	public void setSPMC(String sPMC) {
		SPMC = sPMC;
	}
	public String getJLDW() {
		return JLDW;
	}
	public void setJLDW(String jLDW) {
		JLDW = jLDW;
	}
	public String getGGXH() {
		return GGXH;
	}
	public void setGGXH(String gGXH) {
		GGXH = gGXH;
	}
	public String getDJ() {
		return DJ;
	}
	public void setDJ(String dJ) {
		DJ = dJ;
	}
	public String getSL() {
		return SL;
	}
	public void setSL(String sL) {
		SL = sL;
	}
	public String getJE() {
		return JE;
	}
	public void setJE(String jE) {
		JE = jE;
	}
	public String getSLV() {
		return SLV;
	}
	public void setSLV(String sLV) {
		SLV = sLV;
	}
	public String getSE() {
		return SE;
	}
	public void setSE(String sE) {
		SE = sE;
	}
	public String getHSBZ() {
		return HSBZ;
	}
	public void setHSBZ(String hSBZ) {
		HSBZ = hSBZ;
	}
	
	


}
