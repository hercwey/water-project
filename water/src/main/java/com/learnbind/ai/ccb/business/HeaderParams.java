package com.learnbind.ai.ccb.business;

/**
 * 报文头参数
 * @author lenovo
 * 其值均在CCB配置中
 */
public class HeaderParams {
	
	/**
	 * 合约号  自CCB配置获取
	 */
	private String chanl_cust_no;
	
	/**
	 * 交易员编号  需要与CCB工程师咨询  自行在CCB配置中加入条目,或自CCB工程师获取(沟通后确认是333333)
	 */
	private String txn_stff_id;
	
	/**
	 * 本地IP地址  自CCB配置获取
	 */
	private String txn_itt_ip_adr;	

	public String getChanl_cust_no() {
		return chanl_cust_no;
	}

	public void setChanl_cust_no(String chanl_cust_no) {
		this.chanl_cust_no = chanl_cust_no;
	}

	public String getTxn_stff_id() {
		return txn_stff_id;
	}

	public void setTxn_stff_id(String txn_stff_id) {
		this.txn_stff_id = txn_stff_id;
	}

	public String getTxn_itt_ip_adr() {
		return txn_itt_ip_adr;
	}

	public void setTxn_itt_ip_adr(String txn_itt_ip_adr) {
		this.txn_itt_ip_adr = txn_itt_ip_adr;
	}
	
	
	
}
