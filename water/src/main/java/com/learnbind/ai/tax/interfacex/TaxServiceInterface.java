package com.learnbind.ai.tax.interfacex;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax
 *
 * @Title: InterfaceTaxService.java
 * @Description: 税务接口类(发票操作接口)
 *
 * @author lenovo
 * @date 2019年9月29日 下午4:11:55
 * @version V1.0 
 *
 */
public interface TaxServiceInterface {
	
public String queryInventory(String xml);   //获取库存信息

public String issueInvoice(String xml);   //发票开具

public String printInvoice(String xml);   //发票打印

public String invalidInvoice(String xml);  //发票作废

public String printList (String xml);   //清单打印

public String uploadInvoice (String xml);   //发票上传

public String updateInvoiceStatus (String xml);   //发票状态更新

public String invalidBlankInvoice (String xml);   //空白发票作废

public String queryJSPInfo (String xml);   //金税盘状态查询

public String printParams (String xml);   //打印参数设置

public String uploadRedInfo (String xml);   //红字信息表上传

public String copyTax (String xml);   //汇总抄报

public String clearCard (String xml);   //远程清卡

public String downloadRedInfo(String xml);   //红字信息表下载

public String queryInvoiceInfo(String xml);  //发票单张查询

public String closeInvoiceService(String xml);  //关闭控制台服务

public String repairSingleInvoice(String xml);  //单张发票修复

public String issueControl(String xml);  //开票控制

public String queryBatchInvoiceInfo(String xml);  //发票批量查询

}
