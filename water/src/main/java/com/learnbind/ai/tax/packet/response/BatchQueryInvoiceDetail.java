package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: BatchQueryInvoiceDetail.java
 * @Description: 
 *
 * @author lenovo
 * @date 2019年10月20日 下午5:47:05
 * @version V1.0 
 *
 */
public class BatchQueryInvoiceDetail {
	/*
	FPMXXH	发票明细序号	字符		是	
	FPHXZ	发票行性质	字符	2	是	固定值
	0：普通明细行
	1：清单行
	3：被折扣行
	4：折扣行
	SPMC	商品名称	字符	92	是	
	JLDW	计量单位	字符	22	否	
	GGXH	规格型号	字符	40	否	
	DJ	单价	数值	36,15	否	
	SL	数量	数值	36,15	否	
	JE	金额	数值	16,2	是	
	SLV	税率	数值	10,6	是	
	SE	税额	数值	16,2	是	
	HSBZ	含税标志	字符	2	是	固定值
	0：不含税
	1：含税
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
	QYZBM	商品编号	字符		否	
	*/
	
	private String FPMXXH;//	发票明细序号	字符		是	
	private String FPHXZ;//	发票行性质	字符	2	是	固定值
							//	0：普通明细行
							//	1：清单行
							//	3：被折扣行
							//	4：折扣行
	private String  SPMC;//	商品名称	字符	92	是	
	private String JLDW;//	计量单位	字符	22	否	
	private String GGXH;//	规格型号	字符	40	否	
	private String DJ;//	单价	数值	36,15	否	
	private String SL;//	数量	数值	36,15	否	
	private String JE;//	金额	数值	16,2	是	
	private String SLV;//	税率	数值	10,6	是	
	private String SE;//	税额	数值	16,2	是	
	private String HSBZ;//	含税标志	字符	2	是	固定值
						//	0：不含税
						//	1：含税
	private String SSFLBM;//	税收分类编码	字符		是	详见国税局网站发布的分类编码表格
	private String YHZC;//	是否享受优惠政策	字符		是	固定值
							//		0：不享受
							//		1：享受
	private String YHZCNR;//	享受优惠政策内容	字符		否	如果不享受优惠政策，该字段为空；
								///如果享受优惠政策，该字段不能为空，内容填写为对应的优惠政策
	private String LSLBS;//	零税率标识	字符		否	固定值
							//		空：非零税率
							//		0：出口退税
							//		1：免税
							//		2：不征税
							//		3：普通零税率
	private String QYZBM; //	商品编号	字符		否
	public String getFPMXXH() {
		return FPMXXH;
	}
	public void setFPMXXH(String fPMXXH) {
		FPMXXH = fPMXXH;
	}
	public String getFPHXZ() {
		return FPHXZ;
	}
	public void setFPHXZ(String fPHXZ) {
		FPHXZ = fPHXZ;
	}
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
	
	
	

}
