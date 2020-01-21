package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: InvoiceHead.java
 * @Description: 发票头信息(发票开具)
 *
 * @author lenovo
 * @date 2019年10月17日 上午1:39:22
 * @version V1.0 
 *
 */
public class InvoiceHead {
	/* 
	 * FPZL	发票种类	字符	2	是	固定值：
							0：专用发票 
							2：普通发票
							12：机动车票
							51：电子发票
		GFMC	购方名称	字符	100	是	
		GFSH	购方税号	字符	20	是	
		GFDZDH	购方地址及电话	字符	100	否	
		GFYHZH	购方银行及账户	字符	100	否	
		BZ	备注	字符	230	否	专票开具红字发票时，该字段需要填写如下内容：
							开具红字增值税专用发票信息表编号XXXXXXXXXXXXXXXX
		
							XXXX表示信息表编号，备注内容为以上那句话，不能有差错，否则无法开具红字发票。
		
							普票开具负数发票时，该字段需要填写如下内容：
							对应正数发票代码:YYYYYYYY号码:ZZZZZZ
		
							YYYYY：是发票代码
							ZZZZ:是发票号码
							普票负数票备注除这句内容外可以有其他内容
		SKR	收款人	字符	8	是	8个字节，4个汉字
		FHR	复核人	字符	8	是	8个字节，4个汉字
		KPR	开票人	字符	8	是	8个字节，4个汉字
		XFYHZH	销方银行及账户	字符	100	否	
		XFDZDH	销方地址及电话	字符	100	否	
		QDBZ	清单标志	字符	2	是	固定值
								0：不开具清单
								1：开具清单
		XSDJBH	销售单据编号	字符	50	否	该字段的内容保存于开票客户端中，不做逻辑处理，内容不做限制，由接口调用方根据需要填写内容
		KPBZ	开票标志	字符	2	是	固定值
									0：开票
									1：校验
		JPGG	卷票规格	字符	2	否	卷票时，该节点必填；
									固定值：
									76：76mm*177mm；
									57：57mm*177mm。
									卷票时，若该节点不填或填其他值，则默认为76mm*177mm规格的卷票。
	 */	
	private String FPZL;	
	private String GFMC;	
	private String GFSH;	
	private String GFDZDH;	
	private String GFYHZH;	
	private String BZ;	
	private String SKR;	
	private String FHR;	
	private String KPR;	
	private String XFYHZH;	
	private String XFDZDH;	
	private String QDBZ;	
	private String XSDJBH;	
	private String KPBZ;	
	private String JPGG;
	
	public String getFPZL() {
		return FPZL;
	}
	public void setFPZL(String fPZL) {
		FPZL = fPZL;
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
	public String getXFYHZH() {
		return XFYHZH;
	}
	public void setXFYHZH(String xFYHZH) {
		XFYHZH = xFYHZH;
	}
	public String getXFDZDH() {
		return XFDZDH;
	}
	public void setXFDZDH(String xFDZDH) {
		XFDZDH = xFDZDH;
	}
	public String getQDBZ() {
		return QDBZ;
	}
	public void setQDBZ(String qDBZ) {
		QDBZ = qDBZ;
	}
	public String getXSDJBH() {
		return XSDJBH;
	}
	public void setXSDJBH(String xSDJBH) {
		XSDJBH = xSDJBH;
	}
	public String getKPBZ() {
		return KPBZ;
	}
	public void setKPBZ(String kPBZ) {
		KPBZ = kPBZ;
	}
	public String getJPGG() {
		return JPGG;
	}
	public void setJPGG(String jPGG) {
		JPGG = jPGG;
	}
	

}
