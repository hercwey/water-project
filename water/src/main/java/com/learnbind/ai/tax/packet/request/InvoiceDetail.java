package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: InvoiceDetail.java
 * @Description: 发票明细-条目
 *
 * @author lenovo
 * @date 2019年10月17日 上午2:05:11
 * @version V1.0 
 *
 */
public class InvoiceDetail {
	/*  
	  	SPMC	商品名称	字符	92	是	
		HSBZ	含税标志	字符	2	是	固定值
								0：不含税
								1：含税
		SLV		税率		数值	10,6	是	
		JE		金额		数值	16,2	是	
		DJ		单价		数值	36,15	否	
		JLDW	计量单位	字符	22	否	
		GGXH	规格型号	字符	40	否	
		SE		税额		数值	16,2	是	
		SL		数量		数值	36,15	否	
		BMBBH	编码版本号	字符		是	详见国税局网站发布的分类编码表格
		SSFLBM	税收分类编码	字符		是	详见国税局网站发布的分类编码表格
		YHZC	是否享受优惠政策	字符		是	固定值
											0：不享受
											1：享受
		YHZCNR	享受优惠政策内容	字符		否	如果不享受优惠政策，该字段为空；
				如果享受优惠政策，该字段不能为空，内容填写为对应的优惠政策
		LSLBS	零税率标识	字符		否	固定值
									空：非零税率
									0：出口退税
									1：免税
									2：不征税
									3：普通零税率
		QYZBM	企业自编码	字符		否	企业内部自编的商品编码，可以为空
		KCE		扣除额		数值		否	差额税使用，非差额税可以为空
	 */
	
	
	private String SPMC;	
	private String HSBZ;
	private String SLV;
	private String JE;
	private String DJ;
	private String JLDW;
	private String  GGXH;
	private String SE;
	private String SL;
	private String  BMBBH;
	private String SSFLBM;
	private String  YHZC;
	private String  YHZCNR;
	private String LSLBS;
	private String QYZBM;
	private String KCE;
	
	public String getSPMC() {
		return SPMC;
	}
	public void setSPMC(String sPMC) {
		SPMC = sPMC;
	}
	public String getHSBZ() {
		return HSBZ;
	}
	public void setHSBZ(String hSBZ) {
		HSBZ = hSBZ;
	}
	public String getSLV() {
		return SLV;
	}
	public void setSLV(String sLV) {
		SLV = sLV;
	}
	public String getJE() {
		return JE;
	}
	public void setJE(String jE) {
		JE = jE;
	}
	public String getDJ() {
		return DJ;
	}
	public void setDJ(String dJ) {
		DJ = dJ;
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
	public String getSE() {
		return SE;
	}
	public void setSE(String sE) {
		SE = sE;
	}
	public String getSL() {
		return SL;
	}
	public void setSL(String sL) {
		SL = sL;
	}
	public String getBMBBH() {
		return BMBBH;
	}
	public void setBMBBH(String bMBBH) {
		BMBBH = bMBBH;
	}
	public String getSSFLBM() {
		return SSFLBM;
	}
	public void setSSFLBM(String sSFLBM) {
		SSFLBM = sSFLBM;
	}
	public String getYHZC() {
		return YHZC;
	}
	public void setYHZC(String yHZC) {
		YHZC = yHZC;
	}
	public String getYHZCNR() {
		return YHZCNR;
	}
	public void setYHZCNR(String yHZCNR) {
		YHZCNR = yHZCNR;
	}
	public String getLSLBS() {
		return LSLBS;
	}
	public void setLSLBS(String lSLBS) {
		LSLBS = lSLBS;
	}
	public String getQYZBM() {
		return QYZBM;
	}
	public void setQYZBM(String qYZBM) {
		QYZBM = qYZBM;
	}
	public String getKCE() {
		return KCE;
	}
	public void setKCE(String kCE) {
		KCE = kCE;
	}
	
	

}
