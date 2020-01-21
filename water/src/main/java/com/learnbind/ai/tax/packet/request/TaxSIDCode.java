package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax
 *
 * @Title: TaxInterfaceFuncCode.java
 * @Description: 税务接口功能编码常量
 *
 * @author lenovo
 * @date 2019年9月29日 下午3:55:52
 * @version V1.0 
 *
 */
public class TaxSIDCode {
	public static final String SID_0="0";	//获取库存信息	SID为“0”，查询金税盘库存信息。  queryInventory
	public static final String SID_1="1";	//发票开具	SID为“1”，开具发票。 issueInvoice
	public static final String SID_2="2";	//发票打印	SID为“2”，打印发票。  printInvoice
	public static final String SID_4="4";	//发票作废	SID为“4”，作废发票。 invalidInvoice
	public static final String SID_3="3";	//清单打印	SID为“3”，清单打印。 printList 		
	public static final String SID_15="15";		//发票上传	SID为“15”，发票上传。 uploadInvoice 
	public static final String SID_16="16";    //发票状态更新	SID为“16”，发票状态更新。 updateInvoiceStatus 
	public static final String SID_KBZF="KBZF";  //空白发票作废	SID为“KBZ”，作废空白发票。invalidBlankInvoice 
	public static final String SID_JSPXX="JSPXX"; //金税盘状态查询	SID为“JSPXX”，设置打印参数。  queryJSPInfo 
	public static final String SID_DYCS="DYCS";  //	打印参数设置	SID为“DYCS”，查询金税盘状态。 printParams 
	public static final String SID_HZXXSC="HZXXSC";  //	红字信息表上传	SID为“HZXXSC”，上传红字信息表。 uploadRedInfo 
	public static final String SID_HZCB="HZCB";	  //汇总抄报	SID为“HZCB”，用于抄报税。  copyTax 
	public static final String SID_YCQK="YCQK";  //远程清卡	SID为“YCQK”，用于远程清卡。   clearCard 
	public static final String SID_HZXXXZ="HZXXXZ";	//红字信息表下载	SID为“HXXXXZ”，下载红字信息表。  downloadRedInfo
	public static final String SID_FPCX="FPCX";	  	//发票单张查询	SID为“FPCX”，发票查询   queryInvoiceInfo
	public static final String SID_GBKZT="GBKZT";  //关闭控制台服务	SID 为“GBKZT”，关闭控制台  closeInvoiceService
	public static final String SID_FPXF="FPXF";  	//单张发票修复	SID为“FPXF”，单张发票修复  repairSingleInvoice
	public static final String SID_KPKZ="KPKZ";  	//	开票控制	SID为“KPKZ”，开票控制   issueControl	
	public static final String SID_5="5";   //发票批量查询	SID为“5”，发票批量查询   queryBatchInvoiceInfo

}
