package com.learnbind.ai.tax.business;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnbind.ai.controller.tax.TaxController;
import com.learnbind.ai.tax.interfacex.TaxServiceInterface;
import com.learnbind.ai.tax.packet.request.BatchQueryCond;
import com.learnbind.ai.tax.packet.request.Invoice;
import com.learnbind.ai.tax.packet.request.InvoiceEmpty;
import com.learnbind.ai.tax.packet.request.InvoiceInvalid;
import com.learnbind.ai.tax.packet.request.InvoiceListPrint;
import com.learnbind.ai.tax.packet.request.InvoicePrint;
import com.learnbind.ai.tax.packet.request.InvoiceRepair;
import com.learnbind.ai.tax.packet.request.InvoiceSingle;
import com.learnbind.ai.tax.packet.request.InvoiceUpload;
import com.learnbind.ai.tax.packet.request.PrintParam;
import com.learnbind.ai.tax.packet.request.RedInfo;
import com.learnbind.ai.tax.packet.request.RedInfoDownload;
import com.learnbind.ai.tax.packet.response.InvalidateEmptyInvoice;
import com.learnbind.ai.tax.packet.response.InvalidateInvoice;
import com.learnbind.ai.tax.packet.response.InvoiceInventory;
import com.learnbind.ai.tax.packet.response.OpenInvoice;
import com.learnbind.ai.tax.packet.response.PrintInvoice;
import com.learnbind.ai.tax.packet.response.PrintList;
import com.learnbind.ai.tax.packet.response.QuerySingleInvoice;
import com.learnbind.ai.tax.packet.response.RepairSingleInvoice;
import com.learnbind.ai.tax.packet.response.ResponseResult;
import com.learnbind.ai.tax.packet.response.UploadInvoice;
import com.learnbind.ai.tax.packet.response.UploadRedInfo;
import com.learnbind.ai.tax.processor.TaxDownPacketProcessor;
import com.learnbind.ai.tax.processor.TaxUpPacketProcessor;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.business
 *
 * @Title: TaxBusiness.java
 * @Description: 
 * 		(A)税务开票业务层.主要过程如下:
  
  			(1)下行报文生成----->生成请求报文
			(2)采用TaxServiceImplement 发送请求    
			(3)上午报文处理------->返回对象
			
		(B)	与开发文档中代码示例函数相对应.
			
		(C)	 函数的参数跟随下行报文生成
			函数的返回值跟随上行报文解析
			
		(D)此类用于上层业务逻辑(对于业务逻辑均为业务对象)			
			xml--->java object--->上层业务逻辑可采用面向对象的方式处理业务
			
 *	
 * @author lenovo
 * @date 2019年10月21日 下午2:50:55
 * @version V1.0 
 *
 */

@Component
public class TaxBusiness {
	private static Log log = LogFactory.getLog(TaxBusiness.class);
	
	/**
	 * @Fields taxService：下行报文发送并接收(均为XML格式报文)
	 */
	@Autowired
	TaxServiceInterface taxService;
	
	/**
	 * @Title: queryInventory
	 * @Description: 获取库存信息
	 * @param invoiceTypeList
	 * 			发票类型编码列表 	
	 * 			发票类型编码参见:InvoiceType 		
	 * @return 
	 * 		发票库存列表,其中的每个对象:
	 * 
	 * 		发票类型编码--1:1-->库存信息列表
	 * 		如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<InvoiceInventory> queryInventory(List<String> invoiceTypeList) {
		String reqPackage= TaxDownPacketProcessor.genPackage_0(invoiceTypeList);
		String respPackage=taxService.queryInventory(reqPackage);
		
		List<InvoiceInventory> inventoryList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			inventoryList=TaxUpPacketProcessor.parseInvoiceInventory(respPackage);
		}
		
		return inventoryList;
		
	}
	
	/**
	 * @Title: issueInvoice
	 * @Description:  发票开具
	 * @param invoiceList   发票列表
	 * @return 
	 * 		发票开具结果列表
	 * 		如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<OpenInvoice> issueInvoice(List<Invoice> invoiceList) {
		String reqPackage= TaxDownPacketProcessor.genPackage_1(invoiceList);
		String respPackage=taxService.issueInvoice(reqPackage);
		
		List<OpenInvoice> openInvoiceList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			openInvoiceList=TaxUpPacketProcessor. parseOpenInvoice(respPackage);
		}
		
		return openInvoiceList;
	}
	
	/**
	 * @Title: printInvoice
	 * @Description:  发票打印
	 * @param invoicePrintList  需要打印的发票列表
	 * @return 发票打印结果列表
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<PrintInvoice> printInvoice(List<InvoicePrint> invoicePrintList) {
		String reqPackage= TaxDownPacketProcessor.genPackage_2(invoicePrintList);
		String respPackage=taxService.printInvoice(reqPackage);
		
		List<PrintInvoice> printInvoiceList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			printInvoiceList=TaxUpPacketProcessor. parsePrintInvoice(respPackage);
		}		
		
		return printInvoiceList;
	}	
	
	/**
	 * @Title: invalidInvoice
	 * @Description: 发票作废
	 * @param invoiceInvalidList  作废发票列表
	 * @return
	 * 		发票作废结果列表   
	 * 		如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<InvalidateInvoice> invalidInvoice(List<InvoiceInvalid> invoiceInvalidList) {		
		String reqPackage=TaxDownPacketProcessor.genPackage_4(invoiceInvalidList);
		String respPackage=taxService.invalidInvoice(reqPackage);
		
		List<InvalidateInvoice> invalidateInvoiceList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			invalidateInvoiceList=TaxUpPacketProcessor.parseInvalidateInvoice(respPackage);
		}
		
		return invalidateInvoiceList;
	}
	
	  
	/**
	 * @Title: printList
	 * @Description: 清单打印
	 * @param invoiceListPrintList  需打印的清单列表
	 * @return 打印结果列表
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<PrintList> printList(List<InvoiceListPrint> invoiceListPrintList) {
		String reqPackage=TaxDownPacketProcessor.genPackage_3(invoiceListPrintList);
		String respPackage=taxService.printList(reqPackage);
		
		List<PrintList> printListList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			printListList= TaxUpPacketProcessor.parsePrintList(respPackage);
		}
		
		return printListList;
	}
	
	/**
	 * @Title: uploadInvoice
	 * @Description: 发票上传
	 * @param invoiceUploadList  需上传的发票列表
	 * @return 发票上传结果列表
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<UploadInvoice> uploadInvoice (List<InvoiceUpload> invoiceUploadList) {
		String reqPackage=TaxDownPacketProcessor.genPackage_15(invoiceUploadList);
		String respPackage=taxService.uploadInvoice(reqPackage);
		
		List<UploadInvoice> uploadInvoiceList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			uploadInvoiceList= TaxUpPacketProcessor.parseUploadInvoice(respPackage);
		}
		
		return uploadInvoiceList;
	}
	
	/**
	 * @Title: updateInvoiceStatus
	 * @Description: 发票状态更新
	 * @return	发票状态更新返回结果
	 * 	 
	 * 			"result"		--->ResponseResult Object
	 * 			"successList"	--->List<SuccessInvoice>
	 * 			"failList"		--->List<FailInvoice>
	 * 
	 * 		如果插入金税盘或是web service无法访问时,返回null
	 */
	public Map<String,Object> updateInvoiceStatus () {
		String reqPackage=TaxDownPacketProcessor.genPackage_16();
		String respPackage=taxService.updateInvoiceStatus(reqPackage);
		
		Map<String, Object> retMap=null;
		if(StringUtils.isNotBlank(respPackage)) {
			retMap= TaxUpPacketProcessor.parseUpdateInvoiceStatus(respPackage);
		}
		
		return retMap;
	}
	
	/**
	 * @Title: invalidBlankInvoice
	 * @Description:  空白发票作废
	 * @param invoiceEmptyList 需作废的发票列表
	 * @return 发票作废结果列表 
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<InvalidateEmptyInvoice> invalidBlankInvoice (List<InvoiceEmpty> invoiceEmptyList) {
		String reqPackage=TaxDownPacketProcessor.genPackage_KBZF(invoiceEmptyList);
		String respPackage=taxService.invalidBlankInvoice(reqPackage);
		
		List<InvalidateEmptyInvoice> invalidBlankInvoiceList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			invalidBlankInvoiceList= TaxUpPacketProcessor.parseInvalidateEmptyInvoice(respPackage);
		}
		
		return invalidBlankInvoiceList;
	}
	
	/**
	 * @Title: queryJSPInfo
	 * @Description: 金税盘状态查询
	 * @return
	 * 		 map对象: 
	 * 		(1)"result"				--->ResponseResult Object		//请求响应状态对象
	 * 		(2)"taxEquipInfo"		--->TaxEquipInfo Object   		//设备信息对明
	 * 		(3)"vatSpecialAndNormal"--->VatSpecialAndNormal Object  //增值税专用发票及增值税普通发票
	 * 		(4)"transportVatSpecial"--->TransportVatSpecial Object 	//货物运输业增值税专用发票
	 * 		(5)"motorVehicleSale"	--->MotorVehicleSale Object  	//机动车销售统一发票
	 * 		(6)"elecInvoice"		--->ElecInvoice Object  		//电子发票
	 * 
	 * 		如果没有插金税盘或是web service故障时,返回null对象
	 */
	public Map<String,Object> queryJSPInfo () {
		String reqPackage=TaxDownPacketProcessor.genPackage_JSPXX();
		String respPackage=taxService.queryJSPInfo(reqPackage);
		
		Map<String,Object> retMap=null;
		if(StringUtils.isNotBlank(respPackage))
		{
			retMap= TaxUpPacketProcessor.parseQueryTaxEquipStatus(respPackage);
		}
		else {
			log.debug("error,response package is blank!");
		}
		
		return retMap;
	}
	
	/**
	 * @Title: printParams
	 * @Description: 打印参数设置
	 * @param printParam 打印参数
	 * @return 设置打印参数结果
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 */
	public ResponseResult printParams (PrintParam printParam) {
		String reqPackage=TaxDownPacketProcessor.genPackage_DYCS(printParam);
		String respPackage=taxService.printParams(reqPackage);
		
		ResponseResult responseResult=null;
		if(StringUtils.isNotBlank(respPackage)) {
			responseResult= TaxUpPacketProcessor.parseConfigPrintParam(respPackage);
		}
		
		return responseResult;
	}
	
	/**
	 * @Title: uploadRedInfo
	 * @Description:  红字信息表上传
	 * @param redInfoList  红字信息表列表
	 * @return  上传结果列表 
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<UploadRedInfo> uploadRedInfo (List<RedInfo> redInfoList) {
		String reqPackage=TaxDownPacketProcessor.genPackage_HZXXSC(redInfoList);
		String respPackage=taxService.uploadRedInfo(reqPackage);
		
		List<UploadRedInfo> uploadRedInfoList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			 uploadRedInfoList= TaxUpPacketProcessor.parseUploadRedInfo(respPackage);
		}
		
		return uploadRedInfoList;
	}
	
	/**
	 * @Title: copyTax
	 * @Description:  汇总抄报
	 * @return	汇总抄报响应结果
	 * 		如果插入金税盘或是web service无法访问时,返回null 
	 * 		注: 在此版本的Web service中无法调用,未生成相应的stub模块	 
	 */	
	public ResponseResult copyTax () {
		String reqPackage=TaxDownPacketProcessor.genPackage_HZCB();		
		String respPackage=taxService.copyTax(reqPackage);
		
		ResponseResult responseResult=null;
		if(StringUtils.isNotBlank(respPackage)) {
			responseResult= TaxUpPacketProcessor.parseSummaryReport(respPackage);
		}
		
		return responseResult;
	}
	
	/**
	 * @Title: clearCard
	 * @Description:  远程清卡
	 * @return  远程清卡响应结果
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 * 			注:此接口在此版本的web service中无法使用,无stub模块生成 
	 */	
	public ResponseResult clearCard () {
		String reqPackage=TaxDownPacketProcessor.genPackage_YCQK();
		String respPackage=taxService.clearCard(reqPackage);
		
		ResponseResult responseResult=null;
		if(StringUtils.isNotBlank(respPackage)) {
			responseResult= TaxUpPacketProcessor.parseRemoteClearCard(respPackage);
		}
		
		return responseResult;
	}
	
	/**
	 * @Title: downloadRedInfo
	 * @Description:  红字信息表下载
	 * @param redInfoDownload  下载条件
	 * @return 
	 * 		"err"--->ResponseResult	响应结果
	 * 		"redInfoList"--->List<DownloadRedInfo>		红字信息列表
	 * 		如果插入金税盘或是web service无法访问时,返回null
	 */
	public Map<String,Object> downloadRedInfo(RedInfoDownload redInfoDownload) {
		String reqPackage=TaxDownPacketProcessor.genPackage_HZXXXZ(redInfoDownload);		
		String respPackage=taxService.downloadRedInfo(reqPackage);
		
		Map<String,Object>  retMap=null;
		if(StringUtils.isNotBlank(respPackage)) {
			retMap= TaxUpPacketProcessor.parseDownloadRedInfo(respPackage);
		}
				
		return retMap;
	}
	
	
	/**
	 * @Title: queryInvoiceInfo
	 * @Description: 发票单张查询
	 * @param InvoiceSingleList 查询条件列表
	 * @return 查询结果列表
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 * 			注:在当前版本的web service中无法使用,无对应的stub模块生成
	 */
	public List<QuerySingleInvoice> queryInvoiceInfo(List<InvoiceSingle> InvoiceSingleList) {			
		String reqPackage=TaxDownPacketProcessor.genPackage_FPCX(InvoiceSingleList);
		String respPackage=taxService.queryInvoiceInfo(reqPackage);
		
		List<QuerySingleInvoice> invoiceList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			 invoiceList= TaxUpPacketProcessor.parseQuerySingleInvoice(respPackage);
		}
		
		return invoiceList;
	}
	/**
	 * @Title: closeInvoiceService
	 * @Description: 关闭控制台服务
	 * @return	响应结果
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 * 		注:	 注:在当前版本的web service中无法使用,无对应的stub模块生成
	 */
	public ResponseResult closeInvoiceService() {
		String reqPackage=TaxDownPacketProcessor.genPackage_GBKZT();
		String respPackage=taxService.closeInvoiceService(reqPackage);
		
		ResponseResult responseResult=null;
		if(StringUtils.isNotBlank(respPackage)) {
			responseResult= TaxUpPacketProcessor.parseCloseControlService(respPackage);
		}
		
		return responseResult;
	}
	//1.1.17  单张发票修复
	/**
	 * @Title: repairSingleInvoice
	 * @Description: 单张发票修复
	 * @param invoiceRepairList  修复条件(一条发票修复的条件)
	 * @return 
	 * 		修复结果列表
	 * 		如果插入金税盘或是web service无法访问时,返回null
	 */
	public List<RepairSingleInvoice> repairSingleInvoice(List<InvoiceRepair> invoiceRepairList) {
		String reqPackage=TaxDownPacketProcessor.genPackage_FPXF(invoiceRepairList);
		String respPackage=taxService.repairSingleInvoice(reqPackage);
		
		List<RepairSingleInvoice> invoiceList=null;
		if(StringUtils.isNotBlank(respPackage)) {
			invoiceList= TaxUpPacketProcessor.parseRepairSingleInvoice(respPackage);
		}
		
		return invoiceList;
	}
	
	/**
	 * @Title: issueControl
	 * @Description: 开票控制
	 * @param kpkz
	 * @return	响应结果
	 * 			如果插入金税盘或是web service无法访问时,返回null
	 * 			注:在当前版本的web service中无法使用,无对应的stub模块生成
	 * 			固定值：
					0：允许该客户端开票
					1：不允许该客户端开票 
	 */
	public ResponseResult issueControl(String kpkz) {
	//private ResponseResult issueControl() {
		String reqPackage=TaxDownPacketProcessor.genPackage_KPKZ(kpkz);
		String respPackage=taxService.issueControl(reqPackage);
		
		ResponseResult responseResult=null;
		if(StringUtils.isNotBlank(respPackage)) {
			responseResult= TaxUpPacketProcessor.parseOpenInvoiceControl(respPackage);
		}
		
		return responseResult;
	}
	
	//1.1.19  发票批量查询
	/**
	 * @Title: queryBatchInvoiceInfo
	 * @Description: 发票批量查询
	 * @param batchQueryCond 查询条件
	 * @return 查询结果
	 * 		如果插入金税盘或是web service无法访问时,返回null	
	 */
	public Map<String, Object> queryBatchInvoiceInfo(BatchQueryCond batchQueryCond) {
		String reqPackage=TaxDownPacketProcessor.genPackage_5(batchQueryCond);
		String respPackage=taxService.queryBatchInvoiceInfo(reqPackage);
		
		Map<String, Object> retMap=null;
		if(StringUtils.isNotBlank(respPackage)) {
			retMap= TaxUpPacketProcessor.parseBatchQueryInvoice(respPackage);
		}
		
		return retMap;
	}


}
