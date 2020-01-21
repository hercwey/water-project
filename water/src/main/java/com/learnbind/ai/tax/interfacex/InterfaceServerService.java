package com.learnbind.ai.tax.interfacex;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax
 *
 * @Title: InterfaceInvoiceServerService.java
 * @Description: 开票服务器接口
 *
 * @author lenovo
 * @date 2019年9月30日 上午11:04:54
 * @version V1.0 
 *
 */
public interface InterfaceServerService {
	public String queryInvoice(String xml);  //开票服务器已开具发票明细查询接口	实现开票服务器已开发票的查询功能	
	public String trackSource(String xml);  //开票服务器发票卷分配退回历史信息查询接口	实现开票服务器发票卷分配退回历史信息查询功能	
	public String queryStock(String xml);  //开票服务器发票卷库存查询接口	实现开票服务器发票卷库存查询功能	
	public String wasteInvoice(String xml); //开票服务器发票作废接口	实现开票服务发票作废功能	
	public String distributeInvoice(String xml);  //开票服务器发票分配接口	实现开票服务器往终端分配发票的功能	
	public String returnInvoice(String xml);  //开票服务器发票退回接口	实现终端往开票服务器退回发票的功能

}
