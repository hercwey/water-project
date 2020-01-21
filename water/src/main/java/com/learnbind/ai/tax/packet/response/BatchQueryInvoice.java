package com.learnbind.ai.tax.packet.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: BatchQueryInvoice.java
 * @Description: 发票批量查询-响应报文
 *
 * @author lenovo
 * @date 2019年10月20日 下午11:44:37
 * @version V1.0 
 *
 */
public class BatchQueryInvoice {
	BatchQueryInvoiceHead invoiceHead;
	List<BatchQueryInvoiceDetail> invoiceDetailList;
	
	
	/**
	 * 创建一个新的实例 BatchQueryInvoice.
	 */
	public BatchQueryInvoice() {
		invoiceHead=new BatchQueryInvoiceHead();
		invoiceDetailList=new ArrayList<BatchQueryInvoiceDetail>();
	}
	
	/**
	 * @Title: addDetail
	 * @Description: 增加发票详情
	 * @param invoiceDetail 
	 */
	public void addDetail(BatchQueryInvoiceDetail invoiceDetail) {
		invoiceDetailList.add(invoiceDetail);
	}
	
	/**
	 * @Title: getDetailNum
	 * @Description: 读取详情列表长度
	 * @return 
	 */
	public int getDetailNum() {
		return invoiceDetailList.size();
	}

	public BatchQueryInvoiceHead getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(BatchQueryInvoiceHead invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	public List<BatchQueryInvoiceDetail> getInvoiceDetailList() {
		return invoiceDetailList;
	}

	public void setInvoiceDetailList(List<BatchQueryInvoiceDetail> invoiceDetailList) {
		this.invoiceDetailList = invoiceDetailList;
	}
	
	
	
}
