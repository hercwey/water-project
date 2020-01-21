package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: BatchQueryCond.java
 * @Description: 发票批量查询-业务主体对象
 *
 * @author lenovo
 * @date 2019年10月18日 下午12:03:30
 * @version V1.0 
 *
 */
public class BatchQueryCond {
	/*  
	  	KPJH	开票机号	字符	2	否	金税盘主分机编号；空为全部
		XSDJBH	销售单据编号	字符	50	否	与发票开具报文中的销售单据编号的内容一致；空为全部
		GFMC	购方名称	字符	100	否	空为全部
		GFSH	购方税号	字符	20	否	空为全部
		XFMC	销方名称	字符	100	否	空为全部
		XFSH	销方税号	字符	20	否	空为全部
		FPDM	发票代码	字符	20	否	空为全部
		FPHM	发票号码	字符	20	否	空为全部，如果发票号码是以“0”开头，需要把开头的“0”去除
		FPZL	发票种类	字符	2	否	固定值：0：专用发票  2：普通发票  12：机动车票  51：电子发票	空为全部
		QDBZ	清单标志	字符	2	否	固定值
											0：无清单
											1：有清单
											空为全部
		ZFBZ	作废标志	字符	2	否	固定值
											0：未作废
											1：已作废
											空为全部
		DYBZ	打印标志	字符	2	否	固定值
											0：未打印
											1：已打印
											空为全部
		BSBZ	报送标志	字符	2	否	固定值
											0：未报送
											1：已报送
											2：报送失败
											3：报送中
											4：验签失败
											空为全部
		KPRQQ	开票日期起	字符	10	否	格式		yyyy-MM-dd
		KPRQZ	开票日期止	字符	10	否	格式	yyyy-MM-dd
	 * */
	
	private String   KPJH;  //	开票机号	字符	2	否	金税盘主分机编号；空为全部
	private String  XSDJBH;	//销售单据编号	字符	50	否	与发票开具报文中的销售单据编号的内容一致；空为全部
	private String GFMC;  	//购方名称	字符	100	否	空为全部
	private String GFSH;  //	购方税号	字符	20	否	空为全部
	private String  XFMC;  //	销方名称	字符	100	否	空为全部
	private String  XFSH;  //	销方税号	字符	20	否	空为全部
	private String  FPDM;	//	发票代码	字符	20	否	空为全部
	private String FPHM; //	发票号码	字符	20	否	空为全部，如果发票号码是以“0”开头，需要把开头的“0”去除
	private String  FPZL;  //	发票种类	字符	2	否	固定值：0：专用发票  2：普通发票  12：机动车票  51：电子发票	空为全部
	private String  QDBZ; //	清单标志	字符	2	否	固定值
							//			0：无清单
						//				1：有清单
						//				空为全部
	private String  ZFBZ;  //	作废标志	字符	2	否	固定值
							//			0：未作废
							//			1：已作废
						//				空为全部
	private String DYBZ;  //	打印标志	字符	2	否	固定值
							//			0：未打印
								//		1：已打印
								//		空为全部
	private String BSBZ;  //	报送标志	字符	2	否	固定值
							//			0：未报送
							//			1：已报送
							//			2：报送失败
							//			3：报送中
							//			4：验签失败
							//			空为全部
	private String KPRQQ;   //	开票日期起	字符	10	否	格式		yyyy-MM-dd
	private String  KPRQZ;  //	开票日期止	字符	10	否	格式	yyyy-MM-dd
	public String getKPJH() {
		return KPJH;
	}
	public void setKPJH(String kPJH) {
		KPJH = kPJH;
	}
	public String getXSDJBH() {
		return XSDJBH;
	}
	public void setXSDJBH(String xSDJBH) {
		XSDJBH = xSDJBH;
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
	public String getFPZL() {
		return FPZL;
	}
	public void setFPZL(String fPZL) {
		FPZL = fPZL;
	}
	public String getQDBZ() {
		return QDBZ;
	}
	public void setQDBZ(String qDBZ) {
		QDBZ = qDBZ;
	}
	public String getZFBZ() {
		return ZFBZ;
	}
	public void setZFBZ(String zFBZ) {
		ZFBZ = zFBZ;
	}
	public String getDYBZ() {
		return DYBZ;
	}
	public void setDYBZ(String dYBZ) {
		DYBZ = dYBZ;
	}
	public String getBSBZ() {
		return BSBZ;
	}
	public void setBSBZ(String bSBZ) {
		BSBZ = bSBZ;
	}
	public String getKPRQQ() {
		return KPRQQ;
	}
	public void setKPRQQ(String kPRQQ) {
		KPRQQ = kPRQQ;
	}
	public String getKPRQZ() {
		return KPRQZ;
	}
	public void setKPRQZ(String kPRQZ) {
		KPRQZ = kPRQZ;
	}
	
	
	
}
