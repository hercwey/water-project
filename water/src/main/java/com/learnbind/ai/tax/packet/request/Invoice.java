package com.learnbind.ai.tax.packet.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: Invoice.java
 * @Description: 开据发票---发票
 *
 * @author lenovo
 * @date 2019年10月17日 上午2:26:38
 * @version V1.0 
 *
 */
public class Invoice {
	/**
	 * @Fields invoiceHead：发票头
	 */
	private InvoiceHead invoiceHead;
	/**
	 * @Fields invoiceDetailList：发票明细
	 */
	private List<InvoiceDetail> invoiceDetailList;
	
	/**
	 * 创建一个新的实例 Invoice.
	 */
	public Invoice() {
		invoiceHead=new InvoiceHead();
		invoiceDetailList=new ArrayList<InvoiceDetail>();
	}
	
	/**
	 * @Title: addDetail
	 * @Description: 增加明细
	 * @param invoiceDetail 
	 */
	public void addDetail(InvoiceDetail invoiceDetail) {		
		invoiceDetailList.add(invoiceDetail);
	}
	
	/**
	 * @Title: getDetailSize
	 * @Description: 获取明细长度
	 * @return 
	 */
	public int getDetailSize() {
		return invoiceDetailList.size();
	}

	//------------getter and setter-----------
	public InvoiceHead getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(InvoiceHead invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	public List<InvoiceDetail> getInvoiceDetailList() {
		return invoiceDetailList;
	}

	
	 public void setInvoiceDetailList(List<InvoiceDetail> invoiceDetailList) {
	 this.invoiceDetailList = invoiceDetailList; }
	 
	
	
	
}
