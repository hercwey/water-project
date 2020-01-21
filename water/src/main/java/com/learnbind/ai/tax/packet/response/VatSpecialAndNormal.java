package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: VatSpecialAndNormal.java
 * @Description: 增值税专用发票及普通发票(业务实体)-金税盘状态查询响应报文
 *
 * @author lenovo
 * @date 2019年10月19日 下午3:04:52
 * @version V1.0 
 *
 */
public class VatSpecialAndNormal {
	/*
	Sfdcsq	是否到抄税期	字符	1		固定值
									0：未到抄税期
									1：已到抄税期
	Sfdssq	是否到锁死期	字符	1		固定值
									0：未到锁死期
									1：已到锁死期
	Zyfplxxe	专用发票离线限额	数值	16,2		
	Ptfplxxe	普通发票离线限额	数值	16,2		
	Zyfplxsyje	专用发票离线剩余金额	数值	16,2		
	Ptfplxsyje	普通发票离线剩余金额	数值	16,2		
	Zyfpkpxe	专用发票开票限额	数值	16,2		
	Ptfpkpxe	普通发票开票限额	数值	16,2		
	Ssrq	锁死日期	日期	8		格式为yyyy-MM-dd
	Sccbrq	上次报税日期	日期	12		格式为yyyy-MM-dd HH:mm
	Csqsrq	抄税起始日期	日期	8		格式为yyyy-MM-dd
	Bszl	报税资料	字符	1		固定值
									0：无
									1：有
	Bscgbz	报税成功标志	字符	1		固定值
									0：无
									1：有
	Grfpxx	购入发票信息	字符	1		固定值
									0：无
									1：有
	Thfpxx	退回发票信息	字符	1		固定值
									0：无
									1：有
	*/
	
	private String Sfdcsq;//	是否到抄税期	字符	1		固定值
								//0：未到抄税期
								//1：已到抄税期
	private String Sfdssq;//	是否到锁死期	字符	1		固定值
		//0：未到锁死期
		//1：已到锁死期
	private String Zyfplxxe; //	专用发票离线限额	数值	16,2		
	private String Ptfplxxe;//	普通发票离线限额	数值	16,2		
	private String Zyfplxsyje;//	专用发票离线剩余金额	数值	16,2		
	private String Ptfplxsyje;//	普通发票离线剩余金额	数值	16,2		
	private String Zyfpkpxe;//	专用发票开票限额	数值	16,2		
	private String Ptfpkpxe;//	普通发票开票限额	数值	16,2		
	private String Ssrq;//	锁死日期	日期	8		格式为yyyy-MM-dd
	private String Sccbrq;//	上次报税日期	日期	12		格式为yyyy-MM-dd HH:mm
	private String Csqsrq;//	抄税起始日期	日期	8		格式为yyyy-MM-dd
	private String Bszl;//	报税资料	字符	1		固定值
		//0：无
		//1：有
	private String Bscgbz;//	报税成功标志	字符	1		固定值
		//0：无
		//1：有
	private String Grfpxx;//	购入发票信息	字符	1		固定值
		//0：无
		//1：有
	private String Thfpxx;//	退回发票信息	字符	1		固定值
		//0：无
		//1：有
	public String getSfdcsq() {
		return Sfdcsq;
	}
	public void setSfdcsq(String sfdcsq) {
		Sfdcsq = sfdcsq;
	}
	public String getSfdssq() {
		return Sfdssq;
	}
	public void setSfdssq(String sfdssq) {
		Sfdssq = sfdssq;
	}
	public String getZyfplxxe() {
		return Zyfplxxe;
	}
	public void setZyfplxxe(String zyfplxxe) {
		Zyfplxxe = zyfplxxe;
	}
	public String getPtfplxxe() {
		return Ptfplxxe;
	}
	public void setPtfplxxe(String ptfplxxe) {
		Ptfplxxe = ptfplxxe;
	}
	public String getZyfplxsyje() {
		return Zyfplxsyje;
	}
	public void setZyfplxsyje(String zyfplxsyje) {
		Zyfplxsyje = zyfplxsyje;
	}
	public String getPtfplxsyje() {
		return Ptfplxsyje;
	}
	public void setPtfplxsyje(String ptfplxsyje) {
		Ptfplxsyje = ptfplxsyje;
	}
	public String getZyfpkpxe() {
		return Zyfpkpxe;
	}
	public void setZyfpkpxe(String zyfpkpxe) {
		Zyfpkpxe = zyfpkpxe;
	}
	public String getPtfpkpxe() {
		return Ptfpkpxe;
	}
	public void setPtfpkpxe(String ptfpkpxe) {
		Ptfpkpxe = ptfpkpxe;
	}
	public String getSsrq() {
		return Ssrq;
	}
	public void setSsrq(String ssrq) {
		Ssrq = ssrq;
	}
	public String getSccbrq() {
		return Sccbrq;
	}
	public void setSccbrq(String sccbrq) {
		Sccbrq = sccbrq;
	}
	public String getCsqsrq() {
		return Csqsrq;
	}
	public void setCsqsrq(String csqsrq) {
		Csqsrq = csqsrq;
	}
	public String getBszl() {
		return Bszl;
	}
	public void setBszl(String bszl) {
		Bszl = bszl;
	}
	public String getBscgbz() {
		return Bscgbz;
	}
	public void setBscgbz(String bscgbz) {
		Bscgbz = bscgbz;
	}
	public String getGrfpxx() {
		return Grfpxx;
	}
	public void setGrfpxx(String grfpxx) {
		Grfpxx = grfpxx;
	}
	public String getThfpxx() {
		return Thfpxx;
	}
	public void setThfpxx(String thfpxx) {
		Thfpxx = thfpxx;
	}
	
}
