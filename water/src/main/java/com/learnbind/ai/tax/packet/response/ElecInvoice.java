package com.learnbind.ai.tax.packet.response;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: ElecInvoice.java
 * @Description: 金税盘状态查询返回报文-电子发票(业务实体)
 *
 * @author lenovo
 * @date 2019年10月19日 下午4:20:37
 * @version V1.0 
 *
 */
public class ElecInvoice {
	/*<!--是否到抄税期 -->*/
	private String Sfdcsq;
	/*<!--是否到锁死期 -->*/
	private String Sfdssq;
	/*<!--离线限额 -->*/
	private String Lxxe;
	/*<!--离线剩余金额 -->*/
	private String Lxsyje;
	/*<!--单张开票限额 -->*/
	private String Dzkpxe;
	/*<!--锁死日期 -->*/
	private String Ssrq;
	/*<!--上次报税日期 -->*/
	private String Sccbrq;
	/*<!--抄税起始日期 -->*/
	private String Csqsrq;
	/*<!--报税资料 -->*/
	private String Bszl;
	/*<!--报税成功标志 -->*/
	private String Bscgbz;
	/*<!--购入发票信息 -->*/
	private String Grfpxx;
	/*<!--退回发票信息 -->*/
	private String Thfpxx;
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
	public String getLxxe() {
		return Lxxe;
	}
	public void setLxxe(String lxxe) {
		Lxxe = lxxe;
	}
	public String getLxsyje() {
		return Lxsyje;
	}
	public void setLxsyje(String lxsyje) {
		Lxsyje = lxsyje;
	}
	public String getDzkpxe() {
		return Dzkpxe;
	}
	public void setDzkpxe(String dzkpxe) {
		Dzkpxe = dzkpxe;
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
