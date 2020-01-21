package com.learnbind.ai.tax.interfacex;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.learnbind.ai.controller.tax.TaxController;
import com.learnbind.ai.tax.cxfclient.CxfClient;
import com.learnbind.ai.tax.processor.EncodeUtils;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax
 *
 * @Title: TaxServiceImplement.java
 * @Description: 	(1)税务服务接口实现(TaxService用于连接Tax Web Service)
 * 					(2)上层业务逻辑可以封装到一个新的上层CLASS中
 *
 * @author lenovo
 * @date 2019年9月29日 下午4:23:45
 * @version V1.0 
 *
 */
@Service
public class TaxServiceImplement implements TaxServiceInterface {
	private static Log log = LogFactory.getLog(TaxServiceImplement.class);
	
	/**
	 * @Fields url：web service服务器地址
	 */
	private static final String WEB_SERVICE_SERVER_IP="192.168.55.1:8080";   //web service 服务器地址(IP及端口号)
	private static final String TAX_WEB_SERVICE_URL = "http://"+WEB_SERVICE_SERVER_IP+"/TaxKPServer/services/KpWebserviceC?wsdl";
	
	//声明:方法名称常量
	private static final String  METHOD_QUERYINVENTORY="queryInventory"  ;   //获取库存信息

	private static final String METHOD_ISSUEINVOICE="issueInvoice";   //发票开具

	private static final  String METHOD_PRINTINVOICE="printInvoice";   //发票打印

	private static final String METHOD_INVALIDINVOICE="invalidInvoice";  //发票作废

	private static final String METHOD_PRINTLIST="printList";   //清单打印

	private static final  String METHOD_UPLOADINVOICE="uploadInvoice";   //发票上传

	private static final String METHOD_UPDATEINVOICESTATUS="updateInvoiceStatus";   //发票状态更新

	private static final  String METHOD_INVALIDBLANKINVOICE="invalidBlankInvoice";   //空白发票作废

	private static final  String METHOD_QUERYJSPINFO="queryJSPInfo";   //金税盘状态查询

	private static final String METHOD_PRINTPARAMS="printParams";   //打印参数设置

	private static final String METHOD_UPLOADREDINFO="uploadRedInfo";   //红字信息表上传

	private static final String METHOD_COPYTAX="copyTax";   //汇总抄报

	private static final String METHOD_CLEARCARD="clearCard";   //远程清卡

	private static final String METHOD_DOWNLOADREDINFO="downloadRedInfo";   //红字信息表下载

	private static final  String METHOD_QUERYINVOICEINFO="queryInvoiceInfo";  //发票单张查询

	private static final String METHOD_CLOSEINVOICESERVICE="closeInvoiceService";  //关闭控制台服务

	private static final  String METHOD_REPAIRSINGLEINVOICE="repairSingleInvoice";  //单张发票修复

	private static final  String METHOD_ISSUECONTROL="issueControl";  //开票控制

	private static final  String METHOD_QUERYBATCHINVOICEINFO="queryBatchInvoiceInfo";  //发票批量查询
	
	/**
	 * @Title: sendRequestToWebServie
	 * @Description: 向WebService发送请求
	 * @param methodName	请求方法名称
	 * @param requestPackage	请求报文
	 * @param reqPackIsGBK	请求报文是否需要转换为GBK?  TRUE:转为GBK码; FALSE:保持 UTF-8编码
	 * @return 
	 * 		返回响应报文
	 */
	private String sendRequestToWebServie(String methodName,String requestPackage,boolean reqPackIsGBK) {
		String plain=requestPackage;  //init request package,default encode is:utf-8
		if(reqPackIsGBK) {
			plain=EncodeUtils.utf2gbk(requestPackage);  //GBK编码
		}						
		Object[] parameters = new Object[]{plain};
		String resultXML=null;
		try {
			resultXML=(String) CxfClient.invokeRemoteMethod(TAX_WEB_SERVICE_URL, methodName,parameters)[0];
		}
		catch(Exception e) {
			//e.printStackTrace();
			log.debug("call web service error!");
		}
		
		return resultXML;
	}

	//获取库存信息	
	@Override
	public String queryInventory(String xml) {		
		String method = METHOD_QUERYINVENTORY;		
		String responseXML=sendRequestToWebServie(method,xml,true);
		return responseXML;
	}

	
	//发票开具
	@Override
	public String issueInvoice(String xml) {
		String method = METHOD_ISSUEINVOICE;		
		String responseXML=sendRequestToWebServie(method,xml,true);
		return responseXML;
	}

	//发票打印
	@Override
	public String printInvoice(String xml) {
		String method = METHOD_PRINTINVOICE;		
		String responseXML=sendRequestToWebServie(method,xml,true);
		return responseXML;
		
	}

	//发票作废
	@Override
	public String invalidInvoice(String xml) {
		String method = METHOD_INVALIDINVOICE;		
		String responseXML=sendRequestToWebServie(method,xml,true);
		return responseXML;
	}

	//清单打印
	@Override
	public String printList(String xml) {
		String method = METHOD_PRINTLIST;		
		String responseXML=sendRequestToWebServie(method,xml,false);
		return responseXML;
	}

	//发票上传
	@Override
	public String uploadInvoice(String xml) {
		String method = METHOD_UPLOADINVOICE;		
		String responseXML=sendRequestToWebServie(method,xml,false);
		return responseXML;
	}

	//发票状态更新
	@Override
	public String updateInvoiceStatus(String xml) {
		String method = METHOD_UPDATEINVOICESTATUS;		
		String responseXML=sendRequestToWebServie(method,xml,false);
		return responseXML;
	}

	//空白发票作废
	@Override
	public String invalidBlankInvoice(String xml) {
		String method = METHOD_INVALIDBLANKINVOICE;		
		String responseXML=sendRequestToWebServie(method,xml,false);
		return responseXML;
	}

	//金税盘状态查询
	@Override
	public String queryJSPInfo(String xml) {
		String method = METHOD_QUERYJSPINFO;		
		String responseXML=sendRequestToWebServie(method,xml,false);
		return responseXML;		
	}

	//打印参数设置
	@Override
	public String printParams(String xml) {
		String method = METHOD_PRINTPARAMS;		
		String responseXML=sendRequestToWebServie(method,xml,true);
		return responseXML;
	}

	//红字信息表 上传
	@Override
	public String uploadRedInfo(String xml) {
		String method = METHOD_UPLOADREDINFO;		
		String responseXML=sendRequestToWebServie(method,xml,true);
		return responseXML;
	}

	//汇总抄报
	@Override
	public String copyTax(String xml) {
		String method = METHOD_COPYTAX;		
		String responseXML=sendRequestToWebServie(method,xml,true);  //GBK
		return responseXML;		
	}

	//远程清卡
	@Override
	public String clearCard(String xml) {
		String method = METHOD_CLEARCARD;		
		String responseXML=sendRequestToWebServie(method,xml,false);  //UTF-8
		return responseXML;		
	}

	//红字信息表下载
	@Override
	public String downloadRedInfo(String xml) {		
		String method = METHOD_DOWNLOADREDINFO;		
		String responseXML=sendRequestToWebServie(method,xml,false);  //UTF-8
		return responseXML;
	}

	//发票单张查询
	@Override
	public String queryInvoiceInfo(String xml) {		
		String method = METHOD_QUERYINVOICEINFO;		
		String responseXML=sendRequestToWebServie(method,xml,false);  //UTF-8
		return responseXML;
	}

	//关闭控制台服务
	@Override
	public String closeInvoiceService(String xml) {
		String method = METHOD_CLOSEINVOICESERVICE;		
		String responseXML=sendRequestToWebServie(method,xml,false);  //UTF-8
		return responseXML;
	}

	//单张发票修复
	@Override
	public String repairSingleInvoice(String xml) {
		String method = METHOD_REPAIRSINGLEINVOICE;		
		String responseXML=sendRequestToWebServie(method,xml,false);  //UTF-8
		return responseXML;
	}

	//开票控制
	@Override
	public String issueControl(String xml) {
		String method = METHOD_ISSUECONTROL;
		String responseXML=sendRequestToWebServie(method,xml,false);  //UTF-8
		return responseXML;
	}

	//发票批量查询
	@Override
	public String queryBatchInvoiceInfo(String xml) {
		String method = METHOD_QUERYBATCHINVOICEINFO;
		String responseXML=sendRequestToWebServie(method,xml,false);  //UTF-8
		return responseXML;
	}

}
