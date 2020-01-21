package com.learnbind.ai.ccb.business;

/**
 * added by hz 2019/07/11
 * 请求BODY参数
 * @author lenovo
 * 其值均在CCB配置中.
 */
public class BodyParams {
	private String entrst_prj_id;   //委托项目编号
	private String prj_use_id;		//项目用途编号
	private String etrunt_accno;	//委托单位帐号
	
	public String getEntrst_prj_id() {
		return entrst_prj_id;
	}
	public void setEntrst_prj_id(String entrst_prj_id) {
		this.entrst_prj_id = entrst_prj_id;
	}
	public String getPrj_use_id() {
		return prj_use_id;
	}
	public void setPrj_use_id(String prj_use_id) {
		this.prj_use_id = prj_use_id;
	}
	public String getEtrunt_accno() {
		return etrunt_accno;
	}
	public void setEtrunt_accno(String etrunt_accno) {
		this.etrunt_accno = etrunt_accno;
	}
	
	
	
}
