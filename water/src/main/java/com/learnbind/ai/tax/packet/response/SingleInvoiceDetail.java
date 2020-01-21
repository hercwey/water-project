package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: SingleInvoiceDetail.java
 * @Description: 单张发票查询---INFOXML
 *
 * @author lenovo
 * @date 2019年10月21日 上午12:31:01
 * @version V1.0 
 *
 */
public class SingleInvoiceDetail {
	/*
	<!-- 商品名称 -->
	<SPMC/>
	<!-- 规格型号 -->
	<GGXH/>
	<!-- 计量单位 -->
	<JLDW/>
	<!-- 数量 -->
	<SL/>
	<!-- 单价 -->
	<DJ/>
	<!-- 含税价格标志 -->
	<HSJBZ/>
	<!-- 发票行性质， 0-：普通商品行（包括明细和清单中的商品行） 1-：清单行（即明细表中的“（详见销货清单）”行） 3-：带折扣的商品行（包括明细和清单中的商品行） 4-：折扣行（包括明细和清单中的折扣行） 5-：合计行（对于带折扣的销货清单，包括明细表中保存的折扣行和清单表中的“原价合计”和“折扣额合计”两行清单） -->
	<FPHXZ/>
	<!-- 金额 -->
	<JE/>
	<!-- 税率 -->
	<SLV/>
	<!-- 税额 -->
	<SE/>
	<!-- 分类编码 -->
	<FLBM/>
	<!-- 零税率标识，空：非零税率，0：出口退税，1：免税，2：不征收，3：普通零税率 -->
	<LSLVBS/>
	<!-- 是否享受优惠，0：不享受，1：享受 -->
	<XSYH/>
	<!-- 优惠说明 -->
	<YHSM/>	
	*/
	
	/*<!-- 商品名称 -->*/
	private String SPMC;
	/*<!-- 规格型号 -->*/
	private String GGXH;
	/*<!-- 计量单位 -->*/
	private String JLDW;
	/*<!-- 数量 -->*/
	private String SL;
	/*<!-- 单价 -->*/
	private String DJ;
	/*<!-- 含税价格标志 -->*/
	private String HSJBZ;
	/*<!-- 发票行性质， 0-：普通商品行（包括明细和清单中的商品行） 1-：清单行（即明细表中的“（详见销货清单）”行） 3-：带折扣的商品行（包括明细和清单中的商品行） 4-：折扣行（包括明细和清单中的折扣行） 5-：合计行（对于带折扣的销货清单，包括明细表中保存的折扣行和清单表中的“原价合计”和“折扣额合计”两行清单） -->*/
	private String FPHXZ;
	/*<!-- 金额 -->*/
	private String JE;
	/*<!-- 税率 -->*/
	private String SLV;
	/*<!-- 税额 -->*/
	private String SE;
	/*<!-- 分类编码 -->*/
	private String FLBM;
	/*<!-- 零税率标识，空：非零税率，0：出口退税，1：免税，2：不征收，3：普通零税率 -->*/
	private String LSLVBS;
	/*<!-- 是否享受优惠，0：不享受，1：享受 -->*/
	private String XSYH;
	/*<!-- 优惠说明 -->*/
	private String YHSM;
	public String getSPMC() {
		return SPMC;
	}
	public void setSPMC(String sPMC) {
		SPMC = sPMC;
	}
	public String getGGXH() {
		return GGXH;
	}
	public void setGGXH(String gGXH) {
		GGXH = gGXH;
	}
	public String getJLDW() {
		return JLDW;
	}
	public void setJLDW(String jLDW) {
		JLDW = jLDW;
	}
	public String getSL() {
		return SL;
	}
	public void setSL(String sL) {
		SL = sL;
	}
	public String getDJ() {
		return DJ;
	}
	public void setDJ(String dJ) {
		DJ = dJ;
	}
	public String getHSJBZ() {
		return HSJBZ;
	}
	public void setHSJBZ(String hSJBZ) {
		HSJBZ = hSJBZ;
	}
	public String getFPHXZ() {
		return FPHXZ;
	}
	public void setFPHXZ(String fPHXZ) {
		FPHXZ = fPHXZ;
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
	public String getFLBM() {
		return FLBM;
	}
	public void setFLBM(String fLBM) {
		FLBM = fLBM;
	}
	public String getLSLVBS() {
		return LSLVBS;
	}
	public void setLSLVBS(String lSLVBS) {
		LSLVBS = lSLVBS;
	}
	public String getXSYH() {
		return XSYH;
	}
	public void setXSYH(String xSYH) {
		XSYH = xSYH;
	}
	public String getYHSM() {
		return YHSM;
	}
	public void setYHSM(String yHSM) {
		YHSM = yHSM;
	}	

	
	
}
