package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: SingleInvoiceHead.java
 * @Description: 发票单张查询-INFOXML  发票头
 *
 * @author lenovo
 * @date 2019年10月21日 上午12:23:24
 * @version V1.0 
 *
 */
public class SingleInvoiceHead {
	/*
		<!-- 购方名称 -->
		<GFMC/>
		<!-- 购方税号 -->
		<GFSH/>
		<!-- 购方地址电话 -->
		<GFDZDH/>
		<!-- 购方银行账号 -->
		<GFYHZH/>
		<!-- 销方名称 -->
		<XFMC/>
		<!-- 销方税号 -->
		<XFSH/>
		<!-- 销方地址电话 -->
		<XFDZDH/>
		<!-- 销方银行账号 -->
		<XFYHZH/>
		<!-- 备注 -->
		<BZ/>
		<!-- 开票人 -->
		<KPR/>
		<!-- 收款人 -->
		<SKR/>
		<!-- 复核人 -->
		<FHR/>
		<!-- 开票顺序号 -->
		<KPSXH/>
		<!-- 地址索引号 -->
		<DZSYH/>
		<!-- 清单标志，0：非清单，1：清单 -->
		<QDBZ/>
		<!-- 编码版本号 -->
		<BMBBBH/>
	*/
	
	/*<!-- 购方名称 -->*/
	private String GFMC;
	/*<!-- 购方税号 -->*/
	private String GFSH;
	/*<!-- 购方地址电话 -->*/
	private String GFDZDH;
	/*<!-- 购方银行账号 -->*/
	private String GFYHZH;
	/*<!-- 销方名称 -->*/
	private String XFMC;
	/*<!-- 销方税号 -->*/
	private String XFSH;
	/*<!-- 销方地址电话 -->*/
	private String XFDZDH;
	/*<!-- 销方银行账号 -->*/
	private String XFYHZH;
	/*<!-- 备注 -->*/
	private String BZ;
	/*<!-- 开票人 -->*/
	private String KPR;
	/*<!-- 收款人 -->*/
	private String SKR;
	/*<!-- 复核人 -->*/
	private String FHR;
	/*<!-- 开票顺序号 -->*/
	private String KPSXH;
	/*<!-- 地址索引号 -->*/
	private String DZSYH;
	/*<!-- 清单标志，0：非清单，1：清单 -->*/
	private String QDBZ;
	/*<!-- 编码版本号 -->*/
	private String BMBBBH;
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
	public String getGFDZDH() {
		return GFDZDH;
	}
	public void setGFDZDH(String gFDZDH) {
		GFDZDH = gFDZDH;
	}
	public String getGFYHZH() {
		return GFYHZH;
	}
	public void setGFYHZH(String gFYHZH) {
		GFYHZH = gFYHZH;
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
	public String getXFDZDH() {
		return XFDZDH;
	}
	public void setXFDZDH(String xFDZDH) {
		XFDZDH = xFDZDH;
	}
	public String getXFYHZH() {
		return XFYHZH;
	}
	public void setXFYHZH(String xFYHZH) {
		XFYHZH = xFYHZH;
	}
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
	}
	public String getKPR() {
		return KPR;
	}
	public void setKPR(String kPR) {
		KPR = kPR;
	}
	public String getSKR() {
		return SKR;
	}
	public void setSKR(String sKR) {
		SKR = sKR;
	}
	public String getFHR() {
		return FHR;
	}
	public void setFHR(String fHR) {
		FHR = fHR;
	}
	public String getKPSXH() {
		return KPSXH;
	}
	public void setKPSXH(String kPSXH) {
		KPSXH = kPSXH;
	}
	public String getDZSYH() {
		return DZSYH;
	}
	public void setDZSYH(String dZSYH) {
		DZSYH = dZSYH;
	}
	public String getQDBZ() {
		return QDBZ;
	}
	public void setQDBZ(String qDBZ) {
		QDBZ = qDBZ;
	}
	public String getBMBBBH() {
		return BMBBBH;
	}
	public void setBMBBBH(String bMBBBH) {
		BMBBBH = bMBBBH;
	}
	
	

}
