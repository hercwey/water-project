package com.learnbind.ai.tax.packet.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.response
 *
 * @Title: SingleInvoice.java
 * @Description: 发票单张查询-INFOXML
 *
 * @author lenovo
 * @date 2019年10月21日 上午12:36:23
 * @version V1.0 
 *
 */
public class SingleInvoice {
	private SingleInvoiceHead invoiceHead;
	private List<SingleInvoiceDetail> listDetail;
	
	/**
	 * 创建一个新的实例 SingleInvoice.
	 */
	public SingleInvoice() {
		invoiceHead=new SingleInvoiceHead();
		listDetail=new ArrayList<SingleInvoiceDetail>();
	}
	
	/**
	 * @Title: addDetail
	 * @Description: 加入一条明细
	 * @param detail 
	 */
	public void addDetail(SingleInvoiceDetail detail) {
		listDetail.add(detail);
	}
	
	/**
	 * @Title: getDetailNum
	 * @Description: 获取明细条数
	 * @return 
	 */
	public int getDetailNum() {
		return listDetail.size();
	}

	public SingleInvoiceHead getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(SingleInvoiceHead invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	public List<SingleInvoiceDetail> getListDetail() {
		return listDetail;
	}

	public void setListDetail(List<SingleInvoiceDetail> listDetail) {
		this.listDetail = listDetail;
	}
	
	
	
	
}
