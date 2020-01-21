package com.learnbind.ai.tax.entity;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.entity
 *
 * @Title: IssueInvoiceInfo.java
 * @Description: 销方,购方开票信息(实体)
 *
 * @author lenovo
 * @date 2019年12月16日 下午3:51:58
 * @version V1.0 
 *
 */
public class IssueInvoiceInfo {

	private String customerId="";  //客户id
	private String name = "";  //开票单们名称
	private String customerName="";  //客户信息
	private String taxNo = ""; //税号
	private String address = "";  //地址
	private String tel = "";  //电话
	private String bank = "";  //银行 
	private String accountNo = "";  //账号
	private String checker="";  //复核人
	private String payee="";    //收款人
	private String operator="";  //开票人
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaxNo() {
		return taxNo;
	}
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getChecker() {
		return checker;
	}
	public void setChecker(String checker) {
		this.checker = checker;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	
}
