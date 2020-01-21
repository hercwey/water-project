package com.learnbind.ai.tax.distributeinvoice;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.distributeinvoice
 *
 * @Title: ResultAccountItems.java
 * @Description: 发票分配后结果对象-封装(列表)---一张发票中的账目列表.
 *
 * @author lenovo
 * @date 2019年12月11日 下午10:00:06
 * @version V1.0 
 *
 */
public class ResultAccountItems {
	private List<ResultAccountItem> invoiceAccountItems=null;

	public ResultAccountItems() {
		invoiceAccountItems=new ArrayList<>();
	}
	
	public void addAccountItem(ResultAccountItem accountItem) {
		invoiceAccountItems.add(accountItem);
	}
	
	//getter and setter
	public List<ResultAccountItem> getInvoiceAccountItems() {
		return invoiceAccountItems;
	}

	public void setInvoiceAccountItems(List<ResultAccountItem> invoiceAccountItems) {
		this.invoiceAccountItems = invoiceAccountItems;
	}
	
}
