package com.learnbind.ai.tax.distributeinvoice;

import java.math.BigDecimal;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.distributeinvoice
 *
 * @Title: DistributeResultAccountItem.java
 * @Description: 分配结果-账目对象
 *
 * @author lenovo
 * @date 2019年12月11日 下午4:38:02
 * @version V1.0 
 *
 */
public class ResultAccountItem {
	
	private String accountItemIds="";       		//账目ID(有可能是合并后的账单ID字符串,以逗号进行分隔)
	private BigDecimal invoiceWaterAmount=new BigDecimal("0.00");  	//发票水量	
	private BigDecimal invoicePrice=new BigDecimal("0.00");  		//水价	
	private BigDecimal invoiceAmount=new BigDecimal("0.00");; 		//发票金额	
	private BigDecimal taxRate=new BigDecimal("0.00");;			//税率	
	private BigDecimal invoiceTaxAmount=new BigDecimal("0.00");;  	//税额
	
	public String getAccountItemIds() {
		return accountItemIds;
	}
	public void setAccountItemIds(String accountItemIds) {
		this.accountItemIds = accountItemIds;
	}
	public BigDecimal getInvoiceWaterAmount() {
		return invoiceWaterAmount;
	}
	public void setInvoiceWaterAmount(BigDecimal invoiceWaterAmount) {
		this.invoiceWaterAmount = invoiceWaterAmount;
	}
	public BigDecimal getInvoicePrice() {
		return invoicePrice;
	}
	public void setInvoicePrice(BigDecimal invoicePrice) {
		this.invoicePrice = invoicePrice;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getInvoiceTaxAmount() {
		return invoiceTaxAmount;
	}
	public void setInvoiceTaxAmount(BigDecimal invoiceTaxAmount) {
		this.invoiceTaxAmount = invoiceTaxAmount;
	}
	
	
	
	
}
