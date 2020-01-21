package com.learnbind.ai.tax.distributeinvoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.distributeinvoice
 *
 * @Title: DistributeResult.java
 * @Description: 发票分配结果,其中的每个元素为一张发票(所对应的账目列表)
 *
 * @author lenovo
 * @date 2019年12月11日 下午4:39:14
 * @version V1.0 
 *
 */
public class DistributeResult {
	private List<ResultAccountItems> invoiceList=null; 
	
	public List<ResultAccountItems> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<ResultAccountItems> invoiceList) {
		this.invoiceList = invoiceList;
	}

	//构造函数
	public DistributeResult() {
		invoiceList=new ArrayList<>();
	}
	
	//生成一张发票
	public ResultAccountItems  genOneInvoice() {
		ResultAccountItems oneInvoice=new ResultAccountItems();
		invoiceList.add(oneInvoice);
		return oneInvoice;
	}
	
	public int getInvoiceNum() {
		return invoiceList.size();
	}
	
	public ResultAccountItems getOneInvoiceByIndex(int idx) {
		if(idx<0 || idx>=invoiceList.size()) {
			return null;
		}
		else {
			return invoiceList.get(idx);
		}
	}
	
	public void addOneInvoice(ResultAccountItems invoice) {
		invoiceList.add(invoice);
	}
	
	public static void main(String[] args) {
		List<ResultAccountItems> invoiceList=null;
		invoiceList=new ArrayList<>();
		
		ResultAccountItems items=new ResultAccountItems();
		ResultAccountItem item=new ResultAccountItem();
		item.setAccountItemIds("100L");
		item.setInvoiceAmount(new BigDecimal("6.55"));
		item.setInvoicePrice(new BigDecimal("123"));
		//item.setTaxRate("0.03");
		item.setInvoiceWaterAmount(new BigDecimal(100));
		item.setInvoiceTaxAmount(new BigDecimal("0.03"));
		
		items.addAccountItem(item);
		System.out.println("分配结果JSON字符串为:"+JSON.toJSONString(item));
		System.out.println("分配结果JSON字符串为:"+JSON.toJSONString(items));
		
		
		invoiceList.add(items);	
		
		String json=JSONArray.toJSONString(invoiceList);
		System.out.println("分配结果JSON字符串为:"+json);
	}
	
	
	
	
	
}
