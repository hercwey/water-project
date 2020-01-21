package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: BatchQueryInvoiceHead.java
 * @Description: 发票批量查询-响应报文-发票头
 *
 * @author lenovo
 * @date 2019年10月20日 下午5:40:33
 * @version V1.0 
 *
 */
public class BatchQueryInvoiceHead {
	/*
	KPJH	开票机号	字符	3	是	金税盘主分机编号
	FPZL	发票种类	字符	2	是	固定值：
	0：专用发票
	2：普通发票
	51：电子发票
	FPDM	发票代码	字符	20	是	
	FPHM	发票号码	字符	20	是	
	JYM	检验码	字符	20	是	
	XFMC	销方名称	字符	100	是	
	XFSH	销方税号	字符	20	是	
	XFDZDH	销方地址及电话	字符	100	否	
	XFYHZH	销方银行及账户	字符	100	否	
	GFMC	购方名称	字符	100	否	
	GFSH	购方税号	字符	20	否	
	GFDZDH	购方地址及电话	字符	100	否	
	GFYHZH	购方银行及账户	字符	100	否	
	BZ	备注	字符	230	否	
	SKR	收款人	字符	8	否	8个字节，4个汉字
	FHR	复核人	字符	8	否	8个字节，4个汉字
	KPR	开票人	字符	8	是	8个字节，4个汉字
	HJJE	合计金额	数值	16,2	是	
	HJSE	合计税额	数值	16,2	是	
	KPRQ	开票时间	日期	19	是	格式：yyyy-MM-dd HH:mm:ss
	XSDJBH	销售单据编号	字符	50	否	
	QDBZ	清单标志	字符	2	是	固定值
	0：无清单
	1：有清单
	DYBZ	打印标志	字符	2	是	固定值
	0：未打印
	1：已打印
	ZFBZ	作废标志	字符	2	是	固定值
	0：未作废
	1：已作废
	ZFRQ	作废时间	日期	19	否	格式：yyyy-MM-dd HH:mm:ss
	BSBZ	报送标志	字符	2	是	固定值
	0：未报送
	1：已报送
	2：报送失败
	3：报送中
	4：验签失败
	BMBBBH	税收分类编码版本号	字符	5	是
	*/
	
	private String KPJH;   //	开票机号	字符	3	是	金税盘主分机编号
	private String FPZL; //	发票种类	字符	2	是	固定值：
												//0：专用发票
												//2：普通发票
												//51：电子发票
	private String FPDM;  //	发票代码	字符	20	是	
	private String FPHM; //	发票号码	字符	20	是	
	private String JYM;  //	检验码	字符	20	是	
	private String XFMC; //	销方名称	字符	100	是	
	private String XFSH; //	销方税号	字符	20	是	
	private String XFDZDH;//	销方地址及电话	字符	100	否	
	private String XFYHZH; //	销方银行及账户	字符	100	否	
	private String  GFMC;//	购方名称	字符	100	否	
	private String  GFSH;//	购方税号	字符	20	否	
	private String  GFDZDH;//	购方地址及电话	字符	100	否	
	private String  GFYHZH;//	购方银行及账户	字符	100	否	
	private String BZ;//	备注	字符	230	否	
	private String SKR;//	收款人	字符	8	否	8个字节，4个汉字
	private String FHR;//	复核人	字符	8	否	8个字节，4个汉字
	private String KPR;//	开票人	字符	8	是	8个字节，4个汉字
	private String HJJE;//	合计金额	数值	16,2	是	
	private String HJSE;//	合计税额	数值	16,2	是	
	private String KPRQ;//	开票时间	日期	19	是	格式：yyyy-MM-dd HH:mm:ss
	private String XSDJBH;//	销售单据编号	字符	50	否	
	private String QDBZ;//	清单标志	字符	2	是	固定值
											//0：无清单
											//1：有清单
	private String DYBZ;//	打印标志	字符	2	是	固定值
											//0：未打印
											//1：已打印
	private String ZFBZ;//	作废标志	字符	2	是	固定值
											//0：未作废
											//1：已作废
	private String ZFRQ; //	作废时间	日期	19	否	格式：yyyy-MM-dd HH:mm:ss
	private String BSBZ; //	报送标志	字符	2	是	固定值
											//0：未报送
											//1：已报送
											//2：报送失败
											//3：报送中
											//4：验签失败
	private String BMBBBH;//	税收分类编码版本号	字符	5	是	
	public String getKPJH() {
		return KPJH;
	}
	public void setKPJH(String kPJH) {
		KPJH = kPJH;
	}
	public String getFPZL() {
		return FPZL;
	}
	public void setFPZL(String fPZL) {
		FPZL = fPZL;
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
	public String getBZ() {
		return BZ;
	}
	public void setBZ(String bZ) {
		BZ = bZ;
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
	public String getKPR() {
		return KPR;
	}
	public void setKPR(String kPR) {
		KPR = kPR;
	}
	public String getHJJE() {
		return HJJE;
	}
	public void setHJJE(String hJJE) {
		HJJE = hJJE;
	}
	public String getHJSE() {
		return HJSE;
	}
	public void setHJSE(String hJSE) {
		HJSE = hJSE;
	}
	public String getKPRQ() {
		return KPRQ;
	}
	public void setKPRQ(String kPRQ) {
		KPRQ = kPRQ;
	}
	public String getXSDJBH() {
		return XSDJBH;
	}
	public void setXSDJBH(String xSDJBH) {
		XSDJBH = xSDJBH;
	}
	public String getQDBZ() {
		return QDBZ;
	}
	public void setQDBZ(String qDBZ) {
		QDBZ = qDBZ;
	}
	public String getDYBZ() {
		return DYBZ;
	}
	public void setDYBZ(String dYBZ) {
		DYBZ = dYBZ;
	}
	public String getZFBZ() {
		return ZFBZ;
	}
	public void setZFBZ(String zFBZ) {
		ZFBZ = zFBZ;
	}
	public String getZFRQ() {
		return ZFRQ;
	}
	public void setZFRQ(String zFRQ) {
		ZFRQ = zFRQ;
	}
	public String getBSBZ() {
		return BSBZ;
	}
	public void setBSBZ(String bSBZ) {
		BSBZ = bSBZ;
	}
	public String getBMBBBH() {
		return BMBBBH;
	}
	public void setBMBBBH(String bMBBBH) {
		BMBBBH = bMBBBH;
	}

	

}
