package com.learnbind.ai.controller.tax;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import com.google.gson.Gson;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.tax.EnumTaxDybz;
import com.learnbind.ai.common.enumclass.tax.EnumTaxZfbz;
import com.learnbind.ai.model.CompanyInvoice;
import com.learnbind.ai.model.TaxInvoice;
import com.learnbind.ai.model.TaxInvoiceAccountItem;
import com.learnbind.ai.model.TaxInvoiceDetail;
import com.learnbind.ai.service.company.CompanyInvoiceService;
import com.learnbind.ai.service.tax.TaxInvoiceAccountItemService;
import com.learnbind.ai.service.tax.TaxInvoiceDetailService;
import com.learnbind.ai.service.tax.TaxInvoiceService;
import com.learnbind.ai.service.tax.TaxRedInfoService;
import com.learnbind.ai.tax.business.TaxBusiness;
import com.learnbind.ai.tax.distributeinvoice.DistributeInvoice;
import com.learnbind.ai.tax.distributeinvoice.DistributeResult;
import com.learnbind.ai.tax.distributeinvoice.SourceAccountItems;
import com.learnbind.ai.tax.entity.IssueInvoiceInfo;
import com.learnbind.ai.tax.entity.PreDistAccountItem;
import com.learnbind.ai.tax.entity.PreDistInvoice;
import com.learnbind.ai.tax.entity.PreDistInvoiceList;
import com.learnbind.ai.tax.packet.request.BatchQueryCond;
import com.learnbind.ai.tax.packet.request.Invoice;
import com.learnbind.ai.tax.packet.request.InvoiceDetail;
import com.learnbind.ai.tax.packet.request.InvoiceEmpty;
import com.learnbind.ai.tax.packet.request.InvoiceHead;
import com.learnbind.ai.tax.packet.request.InvoiceInvalid;
import com.learnbind.ai.tax.packet.request.InvoiceListPrint;
import com.learnbind.ai.tax.packet.request.InvoicePrint;
import com.learnbind.ai.tax.packet.request.InvoiceRepair;
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
import com.learnbind.ai.tax.packet.response.RepairSingleInvoice;
import com.learnbind.ai.tax.packet.response.ResponseResult;
import com.learnbind.ai.tax.packet.response.UploadInvoice;
import com.learnbind.ai.tax.packet.response.UploadRedInfo;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.tax
 *
 * @Title: TaxController.java
 * @Description: 税务开票前端控制器
 * @author lenovo
 * @date 2019年10月22日 上午10:53:29
 * @version V1.0 
 * 		编码约定:(1)前台--->后台  参数传递一般采用JSON格式
 * 				(2)访问路径均为小写,路径来自于接口函数名称. 				
 */
@Controller
@RequestMapping(value = "/tax")
public class TaxController {
	private static Log log = LogFactory.getLog(TaxController.class);	 
	private static final String TEMPLATE_PATH = "tax"; // 页面目录
	
	private static final boolean DEBUG_FLAG=true;  //调试标志
	
	/**
	 * @Fields taxBusiness：webService业务对象
	 */
	@Autowired 
	TaxBusiness taxBusiness;
	
	/**
	 * @Fields salerInvoiceService：销售方开票信息读取,需要置于内嵌
	 */
	@Autowired
	CompanyInvoiceService salerInvoiceService;
	
	/**
	 * @Fields taxInvoiceService：发票对象服务
	 */
	@Autowired
	TaxInvoiceService taxInvoiceService;
	
	/**
	 * @Fields taxInvoiceDetailService：发票详情对象-服务
	 */
	@Autowired
	TaxInvoiceDetailService taxInvoiceDetailService;
	
	/**
	 * @Fields taxRedInfoService：红字信息表对象-服务
	 */
	@Autowired
	TaxRedInfoService taxRedInfoService;
	
	/**
	 * @Fields taxInvoiceAccountItem：[发票-账单明细]关系-服务
	 */
	@Autowired
	TaxInvoiceAccountItemService taxInvoiceAccountItemService;
	
	/**
	 * @Fields distributeInvoice：分配发票服务对象
	 */
	@Autowired DistributeInvoice  distributeInvoice;
	
	//-------------------根据账单分配发票----------------------
	@RequestMapping(value = "/distinvoice")
	@ResponseBody
	public Object distribute_invoice(String accountItemListJson,Model model) {
		
		log.debug("待分配发票账单列表为:"+accountItemListJson);
		
		//(1)将接收到的参数转换为JAVA对象   JSON--->JAVA OBJECT
		SourceAccountItems sourceAccountItems=JSON.parseObject(accountItemListJson, SourceAccountItems.class);
		//(2)分配发票
		DistributeResult distributeResult= distributeInvoice.distributeMethod(sourceAccountItems);
		//(3)以JSON格式返回对象
		String jsonResult=JSON.toJSONString(distributeResult);  
		
		return jsonResult;
	}	
	
	//-------------1.1.1  获取库存信息-------已经测试-----------------
	
	/**
	 * @Title: queryInventory_starter
	 * @Description: 起始页
	 * @return 
	 * 	注:此处函数名称与航天提供的接口文档相同.
	 */
	@RequestMapping(value = "/queryinventory/starter")
	public String queryInventory_starter() {
		return TEMPLATE_PATH+"/queryinventory/queryinventory_starter";
	}	
	
	/**
	 * @Title: queryInventory
	 * @Description: 1.1.1  获取库存信息
	 * 				获取指定发票类型库存信息
	 * @param invoiceTypeList  发票类型列表  List<String>
	 * 			参见:InvoiceType 
	 * @param model
	 * @return
	 * 			发票库存结果页面 
	 */
	@RequestMapping(value = "/queryinventory/query")		
	public String queryInventory_result(@RequestParam(value = "invoiceTypeList[]", required = false,defaultValue="")	List<String> invoiceTypeList, 
								Model model) {
		
		List<InvoiceInventory>  inventoryList= taxBusiness.queryInventory(invoiceTypeList);  //自金税盘获取发票库存
		
		//暂时将查询结果以JSON格式显示
		//后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON="";
		if(inventoryList!=null) {
			resultJSON=gson.toJson(inventoryList);  
		}
		
		//log.debug("test data is:"+resultJSON);
		model.addAttribute("inventoryList", inventoryList);  	//查询结果列表
		model.addAttribute("resultJSON", resultJSON);  			//JSON格式返回的数据.
		
		//向前台打包用于显示的数据.
		// 发票类型---1:1---结果列表
				//model.addAttribute("invoiceTypeList",invoiceTypeList);  //发票类型
		List<Map<String,Object>> resultList=new ArrayList<>();
		
		
		if(inventoryList!=null) {
			for(int i=0;i<inventoryList.size();i++) {
				Map<String,Object> map=new HashMap<String,Object>();
				
				InvoiceInventory inventory=inventoryList.get(i);
				String type=invoiceTypeList.get(i);
				
				map.put("inventory", inventory);
				map.put("invoiceType", type);
				
				resultList.add(map);
			}
		}
		
		
		/*
		 *   [
		 *   	{
		 *   		inventory:Inventory对象
		 *   		invoiceType:""  //字符串类型,参见InvoiceType对象
		 *   	},
		 *   	{}
		 *   ]
		 */
		model.addAttribute("resultList",resultList);  //返回结果列表.
		
				
		String page=TEMPLATE_PATH+"/queryinventory/queryinventory_result";
		return page;
	}
	
	//生成库存测试数据-专用发票
	private List<InvoiceInventory> genTestDataInventory(){
		List<InvoiceInventory> inventoryList=new ArrayList<InvoiceInventory>();
		InvoiceInventory inventory=new InvoiceInventory();
		
		/*
		<refp>
			<RETCODE>3011</RETCODE>
			<RETMSG>3011-金税盘状态信息读取成功 [TCD_0_15,]</RETMSG>
			<FPHM>14907748</FPHM>
			<FPDM>1300141140</FPDM>
			<KCFPSL>1000</KCFPSL>
			<JSSBRQ>2019-10-21 11:55:23</JSSBRQ>
			<XFSH>130100999999220</XFSH>
			<SCFS>1</SCFS>
			<KPDH>1</KPDH>
			<CSQBZ>0</CSQBZ>
			<SSQBZ>0</SSQBZ>
			<KPFS>0</KPFS>
			<KPFWQH></KPFWQH>
			<JSPH>661555297643</JSPH>
			<XFMC>航信培训企业</XFMC>
		</refp>
		*/
		
		inventory.setRETCODE("3011");
		inventory.setRETMSG("3011-金税盘状态信息读取成功 [TCD_0_15,]");
		inventory.setFPDM("1300141140");
		inventory.setFPHM("14907748");
		inventory.setKCFPSL("1000");
		inventory.setJSSBRQ("2019-10-21 11:55:23");
		inventory.setXFSH("130100999999220");
		inventory.setSCFS("1");
		inventory.setKPDH("1");
		inventory.setCSQBZ("0");
		inventory.setSSQBZ("0");
		inventory.setKPFS("0");
		inventory.setKPFWQH("");
		inventory.setJSPH("661555297643");
		inventory.setXFMC("航信培训企业");
		
		inventoryList.add(inventory);
		
		return inventoryList; 
		
	}
	
	/**
	 * @Title: queryInventory
	 * @Description: 查询指定发票类型库存.
	 * @param invoiceTypeList  发票类型列表  参见:InvoiceType	 
	 * @return 返回指定的发票类型库存列表,
	 * 			如果web service时没有响应时,则返回"" 空串
	 * 			[
	 * 				{
	 * 					x1:InvoiceInventory
	 * 				},
	 * 				{
	 * 					x2:InvoiceInventory
	 * 				}
	 * 			]
	 */
	@RequestMapping(value = "/inventory/query")
	@ResponseBody
	public String queryInventry(@RequestParam(value = "invoiceTypeList[]",	required = false,defaultValue="")	List<String> invoiceTypeList) {
		log.debug("开始请求发票库存..........");
		//TODO 查询发票库存生产环境打开
		//**********************temp mask start********************		
		List<InvoiceInventory>  inventoryList= taxBusiness.queryInventory(invoiceTypeList);  //查询结果列表
		//*********************temp mask end*********************
		
		//TEST CODE  2019/12/14
		//TODO 查询发票库存测试时使用,正式测试时屏蔽
		//----------------test code start---------------------		
		//List<InvoiceInventory> inventoryList=genTestDataInventory();
		//----------------test code end----------------------
		
		
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON="";
		if(inventoryList!=null) {
			resultJSON=gson.toJson(inventoryList);  
			log.debug("库存查询结果:"+resultJSON);
		}
		else {
			log.debug("未查询到发票库存!");
		}
		
		return resultJSON;		
	}
	
	//-----------------------1.1.2  专用发票开具----------------------
	/**
	 * @Title: issueInvoice_starter
	 * @Description: 专用发票开具起始页(start page)	 
	 * @return 专用发票起始页
	 */
	@RequestMapping(value = "/issueinvoice/special/starter")
	public String issueInvoice_special_starter(Model model) {
		//读取销方默认的开票信息.
		CompanyInvoice companyInvoice=getSalerInvoiceInfo();
		model.addAttribute("salerInvoiceInfo", companyInvoice);
		
		//操作员姓名
		UserBean userBean=getLoginUser();
		if(userBean!=null) {
			model.addAttribute("loginUserName",userBean.getRealname());
		}
		else {
			model.addAttribute("loginUserName","");
		}
		
		return TEMPLATE_PATH+"/issueinvoice/special/issueinvoice_special_starter";
	}
	
	/**
	 * @Title: getSalerInvoiceInfo
	 * @Description: 获取销方默认开票信息
	 * @return  销方默认开票信息
	 * 			如果有则返回相应的对象,否则返回null
	 */
	private CompanyInvoice getSalerInvoiceInfo() {
		return salerInvoiceService.getDefaultCompanyInvoice();
	}	
	
	/**
	 * @Title: getLoginUser
	 * @Description: 获取当前登录用户信息(操作员信息)
	 * @return 
	 * 		登录的操作员对象.
	 */
	private UserBean getLoginUser() {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userBean;
	}
	
	/**
	 * 
	 * @Title: issueInvoice_loadinvoice
	 * @Description: 加载发票-专用发票
	 * @return 发票组件
	 * 可以将发票封装成一个发票组件,此为发票样式设计测试
	 * 发票组件两个:专用发票组件,普通发票组件.
	 * 注: 此请求已经无效
	 */
	//@RequestMapping(value = "/issueinvoice/loadinvoice/special")
	private String issueInvoice_loadinvoice() {
		String componentInvoiceSpecialPath="components";
		return componentInvoiceSpecialPath+"/issueinvoice_special";
	}
	
	/**
	 * @Title: genInvoiceDetailHTML
	 * @Description: 生成专用发票一商品行.
	 * @param rowId  行ID
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/issueinvoice/special/genrow")	
	public String genInvoiceDetailHTML(String rowId,Model model) {	
		log.debug("rowId is :"+rowId);
		model.addAttribute("rowId", rowId);
		String componentInvoiceSpecialPath="tax/components";
		return componentInvoiceSpecialPath+"/special_detail_row";
	}
	
	//-------------------发票保存:(1)保存到金税盘 (2)保存到营收系统--------------------------
	//TODO: 保存到金税盘及保保存到营收系统,全部需要重构处理.
	
	/**
	 * @Title: issueInvoice_issue
	 * @Description: 开票
	 * @param invoiceListJSON  需开具的发票信息
	 * 		
	 * @param model
	 * @return 返回结果页面.
	 */
	@RequestMapping(value = "/issueinvoice/issue")	
	public String issueInvoice_issue(String invoiceListJSON,Model model) {
		//(1)json--->java list object
		List<Invoice> invoiceList=JSONObject.parseArray(invoiceListJSON, Invoice.class);
		//(2)request to issue invoice
		List<OpenInvoice> issueResultList=taxBusiness.issueInvoice(invoiceList);  //开票结果列表.  
		
		//暂时将查询结果以JSON格式显示		
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(issueResultList);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("issueResultList", issueResultList);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/issueinvoice/issueinvoice_result";
		return page;
	}
	
	/**
	 * @Title: issueInvoice_issue_json
	 * @Description: 发票开具
	 * @param invoiceListJSON  
	 * 			[{},
	 * 			{}]
	 * @param model
	 * @return JSON数据类型
	 * 		[
				-{
					"RETCODE": "4011",
					"RETMSG": "4011-开票成功 [0000,]",
					"JE": "14",
					"SE": "0.84",
					"KPRQ": "2019-12-02 13:58:52",
					"FPDM": "1300141140",
					"FPHM": "14974048",
					"JYM": "",
					"MW": "",
					"SIGN": ""
				}
				]
	 */
	//TODO 发票开具原有代码.
	//@RequestMapping(value = "/issueinvoice/issuejson")
	//@ResponseBody
	public Map<String,Object> issueInvoice_issue_json_FORMAL_CODE(String invoiceListJSON) {
		final String ISSUE_INVOICE_STATUS_SUCCESS="4011";  //发票开具状态-成功
		final String ISSUE_INVOICE_MSG_SUCCESS="开票成功";
		final String ISSUE_INVOICE_MSG_FAIL="开票失败";		
		//(1)json--->java list object
		List<Invoice> invoiceList=JSONObject.parseArray(invoiceListJSON, Invoice.class);
		//(2)request to issue invoice
		List<OpenInvoice> issueResultList=taxBusiness.issueInvoice(invoiceList);  //开票结果列表.
		
		boolean issueFlag=true;  //发票开具标志为
		String issueMsg=ISSUE_INVOICE_MSG_SUCCESS;  //发票开具
		
		//判定发票开具情况.可同时开具多张发票.需迭代判定.
		if(issueResultList!=null) {
			for(OpenInvoice invoiceResult:issueResultList) {
				if(!invoiceResult.getRETCODE().equalsIgnoreCase(ISSUE_INVOICE_STATUS_SUCCESS)) {
					issueFlag=false;
					issueMsg="错误代码:"+invoiceResult.getRETCODE()+";错误信息:"+ invoiceResult.getRETMSG();
					break;
				}				
			}
		}
		else {  //如果返回列表对象为空时,则认为发票开具失败.
			log.debug("开具发票时发生错误.....");
			issueFlag=false;
			issueMsg=ISSUE_INVOICE_MSG_FAIL;
		}
		
		Map<String, Object> respMap=null;
		if(issueFlag) {  //如果成功
			respMap = RequestResultUtil.getResultAddSuccess(issueMsg);
		}
		else {
			respMap = RequestResultUtil.getResultAddWarn(issueMsg);
		}
		
		return respMap;
	}
	
	//TODO 发票开具模拟 TEST CODE
	@RequestMapping(value = "/issueinvoice/issuejson")
	@ResponseBody
	public Object issueInvoice_issue_json_TEST_CODE(String invoiceListJSON) {
		final String ISSUE_INVOICE_STATUS_SUCCESS="4011";  //发票开具状态-成功
		final String ISSUE_INVOICE_MSG_SUCCESS="开票成功";
		final String ISSUE_INVOICE_MSG_FAIL="开票失败";		
		
		boolean issueFlag=true;  //发票开具标志为
		String issueMsg=ISSUE_INVOICE_MSG_SUCCESS;  //发票开具	
		
		List<Invoice> invoiceList=JSONObject.parseArray(invoiceListJSON, Invoice.class);
		log.debug("模拟发票开具到金税盘:"+invoiceListJSON);
		
		
		Map<String, Object> respMap=null;
		if(issueFlag) {  //如果成功
			respMap = RequestResultUtil.getResultAddSuccess(issueMsg);
		}
		else {
			respMap = RequestResultUtil.getResultAddWarn(issueMsg);
		}
		
		return respMap;
	} 
	
	
	//------------------保存发票到营收系统中------------------
		
	/**
	 * @Title: issueInvoice_save_json
	 * @Description: 
	 * @param invoicePatternJSON 开票样板(保存到金税盘时使用)
	 * @param salerIssueInvoiceInfoJSON  销方开票信息
	 * @param buyerIssueInvoiceInfoJSON  购方开票信息
	 * @param preInvoiceListJSON         预开发票列表
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/issueinvoice/saveinvoice")
	@ResponseBody
	public Object issueInvoice_save_json(	String invoicePatternJSON,
											String salerIssueInvoiceInfoJSON, 
											String buyerIssueInvoiceInfoJSON, 
											String preInvoiceListJSON,
											Model model) {
		
		final String SAVE_INVOICE_MSG_SUCCESS="发票保存成功(A:营收系统B:金税盘)";			
		
		
		String issueMsg=SAVE_INVOICE_MSG_SUCCESS;  	//保存成功标志
		
		log.debug("发票样板:"+invoicePatternJSON);
		log.debug("销方开票信息:"+salerIssueInvoiceInfoJSON);
		log.debug("购方开票信息:"+buyerIssueInvoiceInfoJSON);
		log.debug("预开发票列表:"+preInvoiceListJSON);
		
		
		
		//解析JSON参数 转换为JAVA对象.
		Invoice invoicePattern=JSONObject.parseObject(invoicePatternJSON, Invoice.class);  //保存金税盘时使用.
		IssueInvoiceInfo buyerIssueInvoiceInfo=JSONObject.parseObject(buyerIssueInvoiceInfoJSON, IssueInvoiceInfo.class);
		IssueInvoiceInfo salerIssueInvoiceInfo=JSONObject.parseObject(salerIssueInvoiceInfoJSON, IssueInvoiceInfo.class);
		PreDistInvoiceList preDistInvoiceList=JSONObject.parseObject(preInvoiceListJSON, PreDistInvoiceList.class);
		log.debug("预分配发票张数是:"+preDistInvoiceList.getInvoiceList().size());
		
		//TODO: 保存发票信息到金税盘
		//step1:保存发票信息到金税盘
		boolean save2DiskFlag=true;  //保存发票到金税盘状态
		for(PreDistInvoice  preDistInvoice:preDistInvoiceList.getInvoiceList()) {
			Map<String,Object> restResult=saveInvoice2Disk(preDistInvoice, invoicePattern, buyerIssueInvoiceInfo, salerIssueInvoiceInfo);
			if(!restResult.get(RequestResultUtil.RESULT_CODE).equals(RequestResultUtil.RESULT_CODE_SUCCESS)) {
				log.debug("在开具发票时发生错误:"+restResult.get(RequestResultUtil.RESULT_MSG));				
				save2DiskFlag=false;  //保存到金税盘是否成功的标志
				break;
			}
			else {
				log.debug("发票开具成功.");
			}
			
		}		
		
		//step2:保存发票信息到数据库
		//(2)将数据保存到对象中.迭代预分配的发票
		boolean save2DB=true;
		if(save2DiskFlag)  //如果保存到金税盘成功时再保存到营收系统中.
		{
			try {
				for(PreDistInvoice  preDistInvoice:preDistInvoiceList.getInvoiceList()) {			
					saveInvoice2Business(preDistInvoice, invoicePattern, buyerIssueInvoiceInfo, salerIssueInvoiceInfo);			
				}
			}
			catch(Exception e) {
				save2DB=false;
			}
		}
		
		
		//判定发票保存情况.可同时保存多张发票.
		log.info("SAVE INVOICE INTO BUSINESS SYSTEM");				
		
		Map<String, Object> respMap=null;
		if(save2DiskFlag && save2DB) {  //如果成功
			respMap = RequestResultUtil.getResultAddSuccess(issueMsg);
		}
		else {
			respMap = RequestResultUtil.getResultAddWarn(issueMsg);
		}
		
		return respMap;
	}
	
	//保存发票到金税盘
	private Map<String,Object> saveInvoice2Disk(PreDistInvoice  preDistInvoice,
								Invoice invoicePattern,
								IssueInvoiceInfo buyerIssueInvoiceInfo,
								IssueInvoiceInfo salerIssueInvoiceInfo) {
		
			List<Invoice> invoiceList=new ArrayList<>();
			
			Invoice pendingInvoice=new Invoice();			
			invoiceList.add(pendingInvoice); //设置请求

			//设置请求发票头.
			InvoiceHead pendingInvoiceHead=pendingInvoice.getInvoiceHead();
			pendingInvoiceHead.setFPZL(invoicePattern.getInvoiceHead().getFPZL()); //(1)发票种类  0:专用发票 2:普通发票
			
			pendingInvoiceHead.setGFMC(buyerIssueInvoiceInfo.getName());  //(2)购方名称
			pendingInvoiceHead.setGFSH(buyerIssueInvoiceInfo.getTaxNo());  //(3)购方税号
			pendingInvoiceHead.setGFDZDH(buyerIssueInvoiceInfo.getAddress()+" "+buyerIssueInvoiceInfo.getTel());  //(4)购方地址,电话
			pendingInvoiceHead.setGFDZDH(buyerIssueInvoiceInfo.getBank()+" "+buyerIssueInvoiceInfo.getAccountNo());  //(5)购方银行,账号
			pendingInvoiceHead.setBZ(preDistInvoice.getBZ());  //(6)备注
			
			pendingInvoiceHead.setSKR(salerIssueInvoiceInfo.getPayee());  	//(7)收款人
			pendingInvoiceHead.setFHR(salerIssueInvoiceInfo.getChecker());  //(8)复核人
			pendingInvoiceHead.setKPR(salerIssueInvoiceInfo.getOperator());  //(9)开票人
			
			pendingInvoiceHead.setXFDZDH(salerIssueInvoiceInfo.getBank()+" "+salerIssueInvoiceInfo.getAccountNo());  //(10)销方银行,账号
			pendingInvoiceHead.setXFYHZH(salerIssueInvoiceInfo.getAddress()+" "+salerIssueInvoiceInfo.getTel());  //(11)销方地址,电话
			
			pendingInvoiceHead.setQDBZ(invoicePattern.getInvoiceHead().getQDBZ());  //(12)清单标志
			pendingInvoiceHead.setXSDJBH(invoicePattern.getInvoiceHead().getXSDJBH());  //(13)销方单据编号
			pendingInvoiceHead.setKPBZ(invoicePattern.getInvoiceHead().getKPBZ());  //(14)开票标志
			pendingInvoiceHead.setJPGG(invoicePattern.getInvoiceHead().getJPGG());  //(15)卷票规则
			
			//设置发票明细
			for (PreDistAccountItem invoiceItem : preDistInvoice.getInvoiceAccountItems()) {
				InvoiceDetail detailPattern = invoicePattern.getInvoiceDetailList().get(0);
				InvoiceDetail invoiceDetail = new InvoiceDetail();				
				invoiceDetail.setSPMC(detailPattern.getSPMC()); // (1)商品名称
				invoiceDetail.setHSBZ(detailPattern.getHSBZ()); // (2)含税标志
				invoiceDetail.setSLV(detailPattern.getSLV()); // (3)税率
				invoiceDetail.setJE(invoiceItem.getInvoiceAmount()); // (4)金额
				invoiceDetail.setDJ(invoiceItem.getInvoicePrice()); // (5)单价
				invoiceDetail.setJLDW(detailPattern.getJLDW()); // (6)计量单位
				invoiceDetail.setGGXH(detailPattern.getGGXH()); // (7)规格型号
				invoiceDetail.setSE(invoiceItem.getInvoiceTaxAmount()); // (8)税额
				invoiceDetail.setSL(invoiceItem.getInvoiceWaterAmount()); // (9)数量 此处为水量
				invoiceDetail.setBMBBH(detailPattern.getBMBBH()); // (10)编码版本号
				invoiceDetail.setSSFLBM(detailPattern.getSSFLBM()); // (11)税收分类编码
				invoiceDetail.setYHZC(detailPattern.getYHZC()); // (12)是否享受优惠政策
				invoiceDetail.setYHZCNR(detailPattern.getYHZCNR()); // (13)优惠政策内容
				invoiceDetail.setLSLBS(detailPattern.getLSLBS()); // (14)零税率标识
				invoiceDetail.setQYZBM(detailPattern.getQYZBM()); // (15)企业自编码
				invoiceDetail.setKCE(detailPattern.getKCE()); //(16) 扣除额
				
				pendingInvoice.addDetail(invoiceDetail);  
				
			}
			
			Gson gson = new Gson();
			//String invoiceListJsonStr= JSON.toJSONString(invoiceList);
			String invoiceListJsonStr= gson.toJson(invoiceList);
			log.debug("开具发票请求GSON字符串:"+invoiceListJsonStr);
			
			//TODO 实际测试时使用如下代码. 
			Map<String, Object> respMap= issueInvoice_issue_json_FORMAL_CODE(invoiceListJsonStr);
			
			//TODO:  金税盘发票开具-测试时使用   生产环境需要屏蔽以下测试代码.			
			//Map<String, Object> respMap= (Map<String,Object>)issueInvoice_issue_json_TEST_CODE(invoiceListJsonStr);
			
			return respMap;
		
	}
	
	
	//保存预生成发票到营收系统中
	@Transactional
	private void saveInvoice2Business(PreDistInvoice  preDistInvoice,
										Invoice invoicePattern,
										IssueInvoiceInfo buyerIssueInvoiceInfo,
										IssueInvoiceInfo salerIssueInvoiceInfo) {
		
		//step1 保存发票
		TaxInvoice taxInvoice=new TaxInvoice();
		taxInvoice.setBz(preDistInvoice.getBZ());  //(1)备注
		
		taxInvoice.setFpzl(invoicePattern.getInvoiceHead().getFPZL()); //(2)发票种类  0:专用发票 2:普通发票
		
		taxInvoice.setFpdm(preDistInvoice.getFPDM());  //(3)发票代码
		taxInvoice.setFphm(preDistInvoice.getFPHM());  //(4)发票号码
		
		taxInvoice.setXfmc(salerIssueInvoiceInfo.getName());  //(5)销方名称
		taxInvoice.setXfsh(salerIssueInvoiceInfo.getTaxNo());  //(6)销方税号
		taxInvoice.setXfdzdh(salerIssueInvoiceInfo.getAddress()+" "+salerIssueInvoiceInfo.getTel());  //(7)销方地址,电话
		taxInvoice.setXfdzdh(salerIssueInvoiceInfo.getBank()+" "+salerIssueInvoiceInfo.getAccountNo());  //(8)销方银行,账号
		
		taxInvoice.setGfmc(buyerIssueInvoiceInfo.getName());  //(9)购方名称
		taxInvoice.setGfsh(buyerIssueInvoiceInfo.getTaxNo());  //(10)购方税号
		taxInvoice.setGfdzdh(buyerIssueInvoiceInfo.getAddress()+" "+buyerIssueInvoiceInfo.getTel());  //(11)购方地址,电话
		taxInvoice.setGfdzdh(buyerIssueInvoiceInfo.getBank()+" "+buyerIssueInvoiceInfo.getAccountNo());  //(12)购方银行,账号
		
		taxInvoice.setSkr(salerIssueInvoiceInfo.getPayee());  	//(13)收款人
		taxInvoice.setFhr(salerIssueInvoiceInfo.getChecker());  //(14)复核人
		taxInvoice.setKpr(salerIssueInvoiceInfo.getOperator());  //(15)开票人
		
		taxInvoice.setHjje(calcHJJE(preDistInvoice));  	//(16)合计金额
		taxInvoice.setHjse(calcHJSE(preDistInvoice));	//(17)合计税额
		
		taxInvoice.setKprq(new Date());  //(18)开票日期
		
		taxInvoice.setJqgg(invoicePattern.getInvoiceHead().getJPGG());  //(19)卷票规则
		
		//增加客户ID号  added by hz at 2019/12/26		
		if(!StringUtils.isEmpty(buyerIssueInvoiceInfo.getCustomerId())) {
			long customerId= Long.parseLong(buyerIssueInvoiceInfo.getCustomerId());
			taxInvoice.setCustomerId(customerId);
		}		
		
		int rows=taxInvoiceService.insertSelective(taxInvoice);  //插入数据库
		Long invoiceId=-1l;
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();			
		}
		else {
			 invoiceId=taxInvoice.getId();
			log.debug("保存发票成功,发票ID: "+taxInvoice.getId());
		}
		
		//step2:保存发票详情(数据库)
		if (invoiceId > 0) {
			InvoiceDetail detailPattern = invoicePattern.getInvoiceDetailList().get(0);
			for (PreDistAccountItem invoiceItem : preDistInvoice.getInvoiceAccountItems()) {
				// invoiceItem.get
				TaxInvoiceDetail invoiceDetail = new TaxInvoiceDetail();
				invoiceDetail.setInvoiceId(invoiceId); // (1)发票ID
				invoiceDetail.setSpmc(detailPattern.getSPMC()); // (2)商品名称
				invoiceDetail.setHsbz(detailPattern.getHSBZ()); // (3)含税标志
				invoiceDetail.setSlv(new BigDecimal(detailPattern.getSLV())); // (4)税率
				invoiceDetail.setJe(new BigDecimal(invoiceItem.getInvoiceAmount())); // (5)金额
				invoiceDetail.setDj(new BigDecimal(invoiceItem.getInvoicePrice())); // (6)单价
				invoiceDetail.setJldw(detailPattern.getJLDW()); // (7)计量单位
				invoiceDetail.setGgxh(detailPattern.getGGXH()); // (8)规格型号
				invoiceDetail.setSe(new BigDecimal(invoiceItem.getInvoiceTaxAmount())); // (9)税额
				invoiceDetail.setSl(new BigDecimal(invoiceItem.getInvoiceWaterAmount())); // (10)数量 此处为水量
				invoiceDetail.setBmbbh(detailPattern.getBMBBH()); // (11)编码版本号
				invoiceDetail.setSsflbm(detailPattern.getSSFLBM()); // (12)税收分类编码
				invoiceDetail.setYhzc(detailPattern.getYHZC()); // (13)是否享受优惠政策
				invoiceDetail.setYhzcnr(detailPattern.getYHZCNR()); // (14)优惠政策内容
				invoiceDetail.setLslbs(detailPattern.getLSLBS()); // (15)零税率标识
				invoiceDetail.setQyzbm(detailPattern.getQYZBM()); // (16)企业自编码
				// invoiceDetail.setKce(null); //(17) 扣除额

				rows = taxInvoiceDetailService.insertSelective(invoiceDetail); // 保存
				if (rows <= 0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}
		
		//step3:保存与账单相关的信息(数据库)
		if (invoiceId > 0) {			
			for (PreDistAccountItem invoiceItem : preDistInvoice.getInvoiceAccountItems()) {				
				TaxInvoiceAccountItem taxInvoiceAccountItem=new TaxInvoiceAccountItem();
				taxInvoiceAccountItem.setCombinAccountItem(invoiceItem.getAccountItemIds());  	//账单ID
				taxInvoiceAccountItem.setInvoices(invoiceId.toString());  						//发票ID
				//TODO 加入其它字段.
				
				rows = taxInvoiceAccountItemService.insertSelective(taxInvoiceAccountItem); // 保存
				if (rows <= 0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				}
			}
		}
		
	}
	
	
	//计算发票合计金额
	private BigDecimal calcHJJE(PreDistInvoice  preDistInvoice) {
		BigDecimal sum=new BigDecimal("0.00");
		for(PreDistAccountItem accountItem:preDistInvoice.getInvoiceAccountItems()) {
			sum=sum.add(new BigDecimal(accountItem.getInvoiceAmount()));
		}
		sum=sum.setScale(2,RoundingMode.HALF_UP);  //只保留两个小数		
		return sum;		
	}
	//计算发票合计税额
	private BigDecimal calcHJSE(PreDistInvoice  preDistInvoice) {
		BigDecimal sum=new BigDecimal("0.00");
		for(PreDistAccountItem accountItem:preDistInvoice.getInvoiceAccountItems()) {
			sum=sum.add(new BigDecimal(accountItem.getInvoiceTaxAmount()));
		}
		sum=sum.setScale(2,RoundingMode.HALF_UP);  //只保留两个小数		
		return sum;		
	}
	
	
	
	//----------------------普通发票开具----------------------
	/**
	 * @Title: issueInvoice_starter
	 * @Description: 普通发票开具起始页(start page)	 
	 * @return 普通发票起始页
	 */
	@RequestMapping(value = "/issueinvoice/normal/starter")
	public String issueInvoice_normal_starter(Model model) {
		//读取销方默认的开票信息.
		CompanyInvoice companyInvoice=getSalerInvoiceInfo();
		model.addAttribute("salerInvoiceInfo", companyInvoice);
		
		//操作员姓名
		UserBean userBean=getLoginUser();
		if(userBean!=null) {
			model.addAttribute("loginUserName",userBean.getRealname());
		}
		else {
			model.addAttribute("loginUserName","");
		}
		
		return TEMPLATE_PATH+"/issueinvoice/normal/issueinvoice_normal_starter";
	}
	
	//------------------1.1.3  发票打印----------------
	/**
	 * @Title: printInvoice_starter
	 * @Description: 打印发票起始页
	 * @return 
	 */
	@RequestMapping(value = "/printinvoice/starter")
	public String printInvoice_starter() {
		return TEMPLATE_PATH+"/printinvoice/printinvoice_starter";
	}
	
	
	/**
	 * @Title: issueInvoice_issue
	 * @Description: 打印发票
	 * @param invoiceListJSON  需打印的发票列表 JSON格式(参见InvoicePrint.java)
	 * 			[
	 * 				{
	 * 					FPZL:"",    //发票种类	
						FPHM:"",	//发票号码	
						FPDM:"",	//发票代码	
						TCBZ:""		//弹窗标志
	 * 				},
	 * 				{}
	 * 			]
	 * @param model
	 * @return   Result page
	 */
	//TODO  打印发票  此函数需要重构.
	@RequestMapping(value = "/printinvoice/print")
	@ResponseBody
	public Object printInvoice_print(String invoiceListJSON,Model model) {		
		final String PRINT_INVOICE_STATUS_SUCCESS="5011";  //发票打印状态返回码   
		final String PRINT_INVOICE_MSG_SUCCESS="打印成功";
		final String PRINT_INVOICE_MSG_FAIL="打印失败";
		
		log.debug("打印发票请求:"+invoiceListJSON);
		//(1)json--->java list object
		List<InvoicePrint> invoiceList=JSONObject.parseArray(invoiceListJSON, InvoicePrint.class);
		
		//(2)request to print invoice 发票打印结果列表		
		List<PrintInvoice> resultList=  taxBusiness.printInvoice(invoiceList);
		
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写,因此替换成GSON生成.
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultList);  //会保持字段的大小写.
		log.debug("test data is:"+resultJSON);
		
		//TODO 保存打印状态到DB中.  2019/12/22		
		boolean reqFlag=true;  
		String msg=PRINT_INVOICE_MSG_SUCCESS;  
		
		//判定发票打印情况.可同时开具多张发票.需迭代判定.
		if(resultList!=null) {
			for(PrintInvoice result:resultList) {
				if(!result.getRETCODE().equalsIgnoreCase(PRINT_INVOICE_STATUS_SUCCESS)) {
					reqFlag=false;
					msg="错误代码:"+result.getRETCODE()+";错误信息:"+ result.getRETMSG();
					break;
				}
				else {	
					int row=savePrintInvoice2DB(result);
					if(row<0) {
						msg="保存打印到数据库时发票错误!";
						reqFlag=false;
						break;
					}
				}
			}
		}
		else {  //如果返回列表对象为空时,则认为发票作废失败.
			log.debug("打印发票时发生错误.....");
			reqFlag=false;
			msg=PRINT_INVOICE_MSG_FAIL;
		}
		
		Map<String, Object> respMap=null;
		if(reqFlag) {  //如果成功
			respMap = RequestResultUtil.getResultAddSuccess(msg);
		}
		else {
			respMap = RequestResultUtil.getResultAddWarn(msg);
		}
		
		return respMap;
	
		//set data  to model
		//model.addAttribute("resultList", resultList);
		//model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		//String page=TEMPLATE_PATH+"/printinvoice/printinvoice_result";
		//return page;
		
	}
	
	//保存打印发票到数据库中.
	//参数说明: invoice-发票打印结果对象
	//TODO:  2019/12/23  此处返回的数据包中发票号码与发票代友对调,需要与航天进行沟通.
	private int savePrintInvoice2DB(PrintInvoice invoice) {
		int row=-1;
		TaxInvoice searchInvoice=new TaxInvoice();
//		searchInvoice.setFpdm(invoice.getFPDM());
//		searchInvoice.setFphm(invoice.getFPHM());
		
		searchInvoice.setFpdm(invoice.getFPHM());
		searchInvoice.setFphm(invoice.getFPDM());
		
		TaxInvoice searchResult=taxInvoiceService.selectOne(searchInvoice);
		
		if(searchResult!=null) {
			TaxInvoice taxInvoice=new TaxInvoice();
			taxInvoice.setId(searchResult.getId());
			//taxInvoice.setFpdm(invalidInvoice.getFPDM());
			//taxInvoice.setFphm(invalidInvoice.getFPHM());	
			
			taxInvoice.setDybz(EnumTaxDybz.YDY.getValue());
			taxInvoice.setDyrq(new Date());
			
			
			taxInvoice.setZfbz(1);  //作废标志
			taxInvoice.setZfrq(new Date());  //作废日期
			
			//操作员姓名
			UserBean userBean=getLoginUser();
			if(userBean!=null) {				
				taxInvoice.setDyr(userBean.getRealname());
			}
			
			try {
				row=taxInvoiceService.updateByPrimaryKeySelective(taxInvoice);				
			}
			catch(Exception e) {
				log.debug(e.getMessage());
				e.printStackTrace();
			}
						
		}
		return row;
	}
	
	//--------------1.1.4  发票作废----------------------------
	//web service只支持己开发票作废
	//TEST CODE
	@RequestMapping(value = "/invalidinvoice/starter")
	public String invalidInvoice_starter() {
		return TEMPLATE_PATH+"/invalidinvoice/invalidinvoice_starter";
	}
	
	//test code
	@RequestMapping(value = "/invalidinvoice/invalid")		
	public String invalidInvoice_invalid(String invoiceListJSON,Model model) {
		//(1)json--->java list object
		List<InvoiceInvalid> invoiceList=JSONObject.parseArray(invoiceListJSON, InvoiceInvalid.class);
		//(2)request to issue invoice		
		//List<PrintInvoice> resultList=  taxBusiness.printInvoice(invoiceList);
		List<InvalidateInvoice> resultList= taxBusiness.invalidInvoice(invoiceList);
		
		
		//List<OpenInvoice> issueResultList=taxBusiness.issueInvoice(invoiceList);  //开票结果列表.  
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultList);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultList", resultList);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/invalidinvoice/invalidinvoice_result";
		return page;
	}
	
	/**
	 * @Title: invalidInvoice_invalid_json
	 * @Description: 未使用发票作废
	 * @param invoiceListJSON  需要作废的未使用发票列表
	 * 		[
	 * 			{
	 * 				FPZL	发票种类	字符	2	是	固定值：
						0：专用发票 
						2：普通发票
						12：机动车票
						51：电子发票
					FPHM	发票号码	字符	20	是	
					FPDM	发票代码	字符	20	是
	 * 			},
	 * 			{} 
	 * 		]	
	 * @param model
	 * @return 返回作废的发票列表.JSON格式
	 * 		[
	 * 			{
		 * 			RETCODE	错误代码	字符	100	是	
					RETMSG	错误信息	字符	100	是	
		
					FPZL	发票种类	字符	2		固定值：
								0：专用发票 
								2：普通发票
								12：机动车票
								51：电子发票
					FPHM	发票号码	字符	20		
					FPDM	发票代码	字符	20
	 * 			},
	 * 			{} 
	 * 		]
	 * 
	 * 		如果在请求的过程中出现错误时则返回空字符串:  ""
	 * 	注:可以同时作废多张发票.  
	 *     此处为实际使用的代码
	 */
	@RequestMapping(value = "/invalidinvoice/invalid/json")		
	@ResponseBody
	public Object invalidInvoice_invalid_json(String invoiceListJSON,Model model) {
		final String CANCEL_INVOICE_STATUS_SUCCESS="6011";  //发票作废成功返回码 
		final String CANCEL_INVOICE_MSG_SUCCESS="作废成功";
		final String CANCEL_INVOICE_MSG_FAIL="作废失败";
		
		log.debug("received request: invalid invoice ---json");
		log.debug("request param is:"+ invoiceListJSON);		
		
		//(1)json--->java list object
		List<InvoiceInvalid> invoiceList=JSONObject.parseArray(invoiceListJSON, InvoiceInvalid.class);
		//(2)request 		
		List<InvalidateInvoice> resultList=taxBusiness.invalidInvoice(invoiceList);
		
		boolean reqFlag=true;  
		String msg=CANCEL_INVOICE_MSG_SUCCESS;  
		
		//判定发票作废情况.可同时开具多张发票.需迭代判定.
		if(resultList!=null) {
			for(InvalidateInvoice result:resultList) {
				if(!result.getRETCODE().equalsIgnoreCase(CANCEL_INVOICE_STATUS_SUCCESS)) {
					reqFlag=false;
					msg="错误代码:"+result.getRETCODE()+";错误信息:"+ result.getRETMSG();
					break;
				}
				else {
					//TODO 保存到数据库到,作废状态  2019/12/22
					int row=saveIInvalidInvoice2DB(result);
					if (row<0) {
						msg="保存数据库时发生错误";
						reqFlag=false;
						break;
					}
				}
			}
		}
		else {  //如果返回列表对象为空时,则认为发票作废失败.
			log.debug("作废发票时发生错误.....");
			reqFlag=false;
			msg=CANCEL_INVOICE_MSG_FAIL;
		}
		
		Map<String, Object> respMap=null;
		if(reqFlag) {  //如果成功
			respMap = RequestResultUtil.getResultAddSuccess(msg);
		}
		else {
			respMap = RequestResultUtil.getResultAddWarn(msg);
		}
		
		return respMap;
		
		//将查询结果以JSON格式返回		
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		
//		String resultJSON="";
//		if(resultList!=null) {
//			Gson gson = new Gson();
//			resultJSON=gson.toJson(resultList);  //会保持字段的大小写.
//		}
//		return resultJSON;
	}
	
	//将作废发票信息保存到DB中
	//2019/12/23
	private int saveIInvalidInvoice2DB(InvalidateInvoice invalidInvoice) {
		int row=-1;
		TaxInvoice searchInvoice=new TaxInvoice();
		searchInvoice.setFpdm(invalidInvoice.getFPDM());
		searchInvoice.setFphm(invalidInvoice.getFPHM());
		TaxInvoice searchResult=taxInvoiceService.selectOne(searchInvoice);
		
		if(searchResult!=null) {
			TaxInvoice taxInvoice=new TaxInvoice();
			taxInvoice.setId(searchResult.getId());
			//taxInvoice.setFpdm(invalidInvoice.getFPDM());
			//taxInvoice.setFphm(invalidInvoice.getFPHM());			
			
			taxInvoice.setZfbz(EnumTaxZfbz.YZF.getValue());  //作废标志
			taxInvoice.setZfrq(new Date());  //作废日期
			
			//操作员姓名
			UserBean userBean=getLoginUser();
			if(userBean!=null) {				
				taxInvoice.setZfr(userBean.getRealname());
			}
			
			try {
				row=taxInvoiceService.updateByPrimaryKeySelective(taxInvoice);
			}
			catch(Exception e) {
				log.debug(e.getMessage());
				e.printStackTrace();
			}
		}
		return row;
	}
	
	/**
	 * @Title: invalidInvoiceSpecialNoUseNum_starter
	 * @Description: 作废未使用发票-(专票)按指定份数  CASE1
	 * @return 
	 * 
	 * 注:web service不支持此功能  
	 */
	@RequestMapping(value = "/invalidinvoice/nouse/special/num/starter")
	public String invalidInvoiceNoUseSpecialNum_starter() {
		return TEMPLATE_PATH+"/invalidinvoice/invalid_invoice_nouse_special_num";
	}
	
	/**
	 * @Title: invalidInvoiceNoUseSpecialAll_starter
	 * @Description: 作废未使用发票-(专票)所有  CASE2
	 * @return 
	 * 
	 * 注:web service不支持此功能
	 */
	@RequestMapping(value = "/invalidinvoice/nouse/special/all/starter")
	public String invalidInvoiceNoUseSpecialAll_starter() {
		return TEMPLATE_PATH+"/invalidinvoice/invalid_invoice_nouse_special_all";
	}
	
	/**
	 * @Title: invalidInvoiceNoUseNormalNum_starter
	 * @Description: 作废未使用发票-(普票)按指定份数  CASE3
	 * @return 
	 * 注:web service不支持此功能
	 */
	@RequestMapping(value = "/invalidinvoice/nouse/normal/num/starter")
	public String invalidInvoiceNoUseNormalNum_starter() {
		return TEMPLATE_PATH+"/invalidinvoice/invalid_invoice_nouse_normal_num";
	}
	
	/**
	 * @Title: invalidInvoiceNoUseNormalAll_starter
	 * @Description: 作废未使用发票-(普票)所有  CASE4
	 * @return 
	 * 注:web service不支持此功能
	 */
	@RequestMapping(value = "/invalidinvoice/nouse/normal/all/starter")
	public String invalidInvoiceNoUseNormalAll_starter() {
		return TEMPLATE_PATH+"/invalidinvoice/invalid_invoice_nouse_normal_all";
	}

	//------------------1.1.5  清单打印---------------------------------
	//注:业务中无此需求.
	@RequestMapping(value = "/printlist/starter")
	public String printList_starter() {
		return TEMPLATE_PATH+"/printlist/printlist_starter";
	}
	
	@RequestMapping(value = "/printlist/print")		
	public String printList_print(String invoiceListJSON,Model model) {
		//(1)json--->java list object
		List<InvoiceListPrint> invoiceListPrintList =JSONObject.parseArray(invoiceListJSON, InvoiceListPrint.class);
		//(2)request to issue invoice		
		List<PrintList>  resultList=  taxBusiness.printList(invoiceListPrintList);  //结果列表
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultList);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultList", resultList);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/printlist/printlist_result";
		return page;
	}	
	
	//------------------1.1.6  发票上传----------------------------
	//注:开票客户端控制台支持自动上传功能.上层业务暂时不做处理.
	@RequestMapping(value = "/uploadinvoice/starter")
	public String uploadInvoice_starter() {
		return TEMPLATE_PATH+"/uploadinvoice/uploadinvoice_starter";
	}
	
	@RequestMapping(value = "/uploadinvoice/upload")		
	public String uploadInvoice_upload(String invoiceListJSON,Model model) {
		//(1)json--->java list object
		List<InvoiceUpload> invoiceUploadList =JSONObject.parseArray(invoiceListJSON, InvoiceUpload.class);
		//(2)request to issue invoice		
		List<UploadInvoice>  resultList =taxBusiness.uploadInvoice(invoiceUploadList);  //结果列表
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultList);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultList", resultList);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/uploadinvoice/uploadinvoice_result";
		return page;
	}
	
	//---------------1.1.7  发票状态更新------------------------
	//web service 不支持此功能
	@RequestMapping(value = "/updateinvoicestatus/starter")
	public String updateInvoiceStatus_starter() {
		return TEMPLATE_PATH+"/updateinvoicestatus/updateinvoicestatus_starter";
	}
	
	@RequestMapping(value = "/updateinvoicestatus/update")		
	public String updateInvoiceStatus_update(Model model) {
		//(1)json--->java list object
		//List<InvoiceUpload> invoiceUploadList =JSONObject.parseArray(invoiceListJSON, InvoiceUpload.class);
		
		//(2)request to issue invoice
		Map<String,Object>  resultObj=  taxBusiness.updateInvoiceStatus();
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultObj);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultObj", resultObj);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/updateinvoicestatus/updateinvoicestatus_result";
		return page;
	}
	
	//--------------------1.1.8  空白发票作废-----------------------------
	//web service不支持此功能
	@RequestMapping(value = "/invalidblankinvoice/starter")
	public String invalidBlankInvoice_starter() {
		return TEMPLATE_PATH+"/invalidblankinvoice/invalidblankinvoice_starter";
	}
	
	@RequestMapping(value = "/invalidblankinvoice/invalid")		
	public String invalidblankinvoice_invalid(String invoiceListJSON,Model model) {
		//(1)json--->java list object
		List<InvoiceEmpty> invoiceList =JSONObject.parseArray(invoiceListJSON, InvoiceEmpty.class);
		//(2)request to issue invoice
		List<InvalidateEmptyInvoice> resultObj= taxBusiness.invalidBlankInvoice(invoiceList);
				
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultObj);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultObj", resultObj);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/invalidblankinvoice/invalidblankinvoice_result";
		return page;
	}
	
	
	//---------------1.1.9  金税盘状态查询-----已经测试-------------------	 
	@RequestMapping(value = "/queryjspinfo/starter")
	public String queryJSPInfo_starter() {
		return TEMPLATE_PATH+"/queryjspinfo/queryjspinfo_starter";
	}
	
	@RequestMapping(value = "/queryjspinfo/query")
	public String queryJSPInfo_result(Model model) {
		
		/*
		 
			 (1)"result"				--->ResponseResult Object		//请求响应状态对象
			 (2)"taxEquipInfo"			--->TaxEquipInfo Object   		//设备信息对象
			 (3)"vatSpecialAndNormal"	--->VatSpecialAndNormal Object  //增值税专用发票及增值税普通发票
			 (4)"transportVatSpecial"	--->TransportVatSpecial Object 	//货物运输业增值税专用发票
			 (5)"motorVehicleSale"		--->MotorVehicleSale Object  	//机动车销售统一发票
			 (6)"elecInvoice"			--->ElecInvoice Object  		//电子发票
		*/
		
		log.debug("received request:  queryJSPInfo");
		Map<String,Object>  resultMap=  taxBusiness.queryJSPInfo();
		//TODO resultMap may be blank
		//TODO do something
		
		//将查询结果以JSON格式与table两种样式显示
		//JSON格式用于测试,TABLE样式用于业务
		//后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写  ali:  fastjson 
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultMap);  //会将第一个字母小写
		//log.debug("test data is:"+resultJSON);
		
		model.addAttribute("resultObj", resultMap);
		model.addAttribute("resultJSON", resultJSON);
				
		String page=TEMPLATE_PATH+"/queryjspinfo/queryjspinfo_result";
		return page;		
	}
	
	//----------------1.1.10  打印参数设置--------------------------
	@RequestMapping(value = "/printparams/starter")
	public String printParams_starter() {
		return TEMPLATE_PATH+"/printparams/printparams_starter";
	}
	
	@RequestMapping(value = "/printparams/config")		
	public String printParams_config(String printParamsJSON,Model model) {
		//(1)json--->java object		
		PrintParam printParams=JSONObject.parseObject(printParamsJSON, PrintParam.class);		
		//(2)request to issue invoice
		ResponseResult resultObj= taxBusiness.printParams(printParams);
				
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultObj);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultObj", resultObj);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/printparams/printparams_result";
		return page;
	}	
	
	
	//---------------1.1.11  红字信息表上传--------------------
	// web service不支持此功能
	@RequestMapping(value = "/uploadredinfo/starter")
	public String uploadRedInfo_starter() {
		return TEMPLATE_PATH+"/uploadredinfo/uploadredinfo_starter";
	}
	
	@RequestMapping(value = "/uploadredinfo/upload")		
	public String uploadRedInfo_upload(String redInfoListJSON,Model model) {
		//(1)json--->java object		
		List<RedInfo> redInfoList=JSONObject.parseArray(redInfoListJSON, RedInfo.class);
		//(2)request to issue invoice				
		List<UploadRedInfo> uploadRedInfoList  = taxBusiness.uploadRedInfo(redInfoList);
		
				
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(uploadRedInfoList);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultObj", uploadRedInfoList);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/uploadredinfo/uploadredinfo_result";
		return page;
	}
	
	//--------------1.1.12  汇总抄报----------------------
	//在现有版本的web service暂未提供此功能.
	//@RequestMapping(value = "/copytax/starter")
	public String copyTax_starter() {
		return TEMPLATE_PATH+"/copytax/copytax_starter";
	}
	
	//@RequestMapping(value = "/copytax/copy")
	public String copyTax_copy(Model model) {
		
		log.debug("received request:  copyTax");
		//Map<String,Object>  resultMap=  taxBusiness.queryJSPInfo();
		ResponseResult responseResult=taxBusiness.copyTax();
		//TODO resultMap may be blank
		//TODO do something
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写  ali:  fastjson 
		Gson gson = new Gson();
		String resultJSON=gson.toJson(responseResult);  //会将第一个字母小写
		//log.debug("test data is:"+resultJSON);
		
		model.addAttribute("resultObj", responseResult);
		model.addAttribute("resultJSON", resultJSON);
				
		String page=TEMPLATE_PATH+"/copytax/copytax_result";
		return page;		
	}
	
	//-------------1.1.13  远程清卡-------------------------
	//web service中不支持此功能
	//@RequestMapping(value = "/clearcard/starter")
	public String clearCard_starter() {
		return TEMPLATE_PATH+"/clearcard/clearcard_starter";
	}
	
	//@RequestMapping(value = "/clearcard/clear")
	public String clearCard_copy(Model model) {
		
		log.debug("received request:  clearCard");
		//Map<String,Object>  resultMap=  taxBusiness.queryJSPInfo();
		ResponseResult responseResult=taxBusiness.clearCard();
		//TODO resultMap may be blank
		//TODO do something
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写  ali:  fastjson 
		Gson gson = new Gson();
		String resultJSON=gson.toJson(responseResult);  //会将第一个字母小写
		//log.debug("test data is:"+resultJSON);
		
		model.addAttribute("resultObj", responseResult);
		model.addAttribute("resultJSON", resultJSON);
				
		String page=TEMPLATE_PATH+"/clearcard/clearcard_result";
		return page;		
	}
	
	//-----------------1.1.14  红字信息表下载----------------------
	//web service 不支持此功能
	/**
	 * @Title: downloadRedInfo_starter
	 * @Description: 红字信息表下载测试主页
	 * @return 红字信息表下载主页
	 */
	@RequestMapping(value = "/downloadredinfo/starter")
	public String downloadRedInfo_starter() {
		log.debug("received download redinfo request");
		return TEMPLATE_PATH+"/downloadredinfo/downloadredinfo_starter";
	}
	
	/**
	 * @Title: downloadRedInfo_result
	 * @Description: 下载红字信息表
	 * @param redInfoJSON 需要下载的红字信息表
	 * @param model
	 * @return 红字信息表结果页面
	 */
	@RequestMapping(value = "/downloadredinfo/download")		
	public String downloadRedInfo_result(String redInfoJSON,Model model) {
		//(1)json--->java object		
		RedInfoDownload redInfo=JSONObject.parseObject(redInfoJSON, RedInfoDownload.class);
		//(2)request to issue invoice				
		Map<String,Object>   resultObj= taxBusiness.  downloadRedInfo(redInfo);
		
				
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultObj);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultObj", resultObj);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/downloadredinfo/downloadredinfo_result";
		return page;
	}
	
	//-------------1.1.15  发票单张查询-----------------------
	//在此版本的web service中,此功能暂未提供.
	private String queryInvoiceInfo(String xml) {
		return null;
	}
	
	//------------------1.1.16  关闭控制台服务------------------------
	//在此版本的web service中,此功能暂未提供
	//@RequestMapping(value = "/closeinvoiceservice/starter")
	public String closeInvoiceService_starter() {
		return TEMPLATE_PATH+"/closeinvoiceservice/closeinvoiceservice_starter";
	}
	
	//@RequestMapping(value = "/closeinvoiceservice/close")
	//在此版本的web service中,此功能暂未提供
	public String closeInvoiceService_clear(Model model) {
		
		log.debug("received request:  closeInvoiceService");
		//Map<String,Object>  resultMap=  taxBusiness.queryJSPInfo();
		ResponseResult responseResult=taxBusiness.closeInvoiceService();
		//TODO resultMap may be blank
		//TODO do something
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写  ali:  fastjson 
		Gson gson = new Gson();
		String resultJSON=gson.toJson(responseResult);  //会将第一个字母小写
		//log.debug("test data is:"+resultJSON);
		
		model.addAttribute("resultObj", responseResult);
		model.addAttribute("resultJSON", resultJSON);
				
		String page=TEMPLATE_PATH+"/closeinvoiceservice/closeinvoiceservice_result";
		return page;		
	}
	
	//------------------1.1.17  单张发票修复-----------------------------
	//web service不支持此功能
	@RequestMapping(value = "/repairsingleinvoice/starter")
	public String repairSingleInvoice_starter() {
		return TEMPLATE_PATH+"/repairsingleinvoice/repairsingleinvoice_starter";
	}
	
	@RequestMapping(value = "/repairsingleinvoice/repair")		
	public String repairSingleInvoice_repair(String invoiceListJSON,Model model) {
		//(1)json--->java list object		
		List<InvoiceRepair> invoiceRepairList=JSONObject.parseArray(invoiceListJSON, InvoiceRepair.class);
		//(2)request to issue invoice		
		List<RepairSingleInvoice>   resultList=  taxBusiness.repairSingleInvoice(invoiceRepairList);
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultList);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultObj", resultList);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/repairsingleinvoice/repairsingleinvoice_result";
		return page;
	}
	
	
	//----------------1.1.18  开票控制----------------------
	//web service不支持此功能
	//开票控制接口不必实现.
	//@RequestMapping(value = "/issuecontrol/starter")
	public String issueControl_starter() {
		return TEMPLATE_PATH+"/issuecontrol/issueControl_starter";
	}
	
	/**
	 * @Title: issueControl_control
	 * @Description: 处理开票控制请求
	 * @param kpkz  开票控制参数
	 * 				固定值： 0：允许该客户端开票 1：不允许该客户端开票
	 * @param model
	 * @return 
	 */
	//@RequestMapping(value = "/issuecontrol/control")
	public String issueControl_control(String kpkz,Model model) {
		
		log.debug("received request:  issueControl");
		//Map<String,Object>  resultMap=  taxBusiness.queryJSPInfo();
		ResponseResult responseResult=taxBusiness.issueControl(kpkz);
		//TODO resultMap may be blank
		//TODO do something
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写  ali:  fastjson 
		Gson gson = new Gson();
		String resultJSON=gson.toJson(responseResult);  //会将第一个字母小写
		//log.debug("test data is:"+resultJSON);
		
		model.addAttribute("resultObj", responseResult);
		model.addAttribute("resultJSON", resultJSON);
				
		String page=TEMPLATE_PATH+"/issuecontrol/issuecontrol_result";
		return page;		
	}
	
	//--------------1.1.19  发票批量查询------------------------
	//支持
	@RequestMapping(value = "/querybatchinvoiceinfo/starter")
	public String queryBatchInvoiceInfo_starter() {
		return TEMPLATE_PATH+"/querybatchinvoiceinfo/querybatchinvoiceinfo_starter";
	}
	
	@RequestMapping(value = "/querybatchinvoiceinfo/query")		
	public String queryBatchInvoiceInfo_query(String batchQueryCondJSON,Model model) {
		//(1)json--->java object		
		BatchQueryCond batchQueryCond=JSONObject.parseObject(batchQueryCondJSON, BatchQueryCond.class);
		//(2)request to issue invoice		
		Map<String, Object> resultObj=taxBusiness.queryBatchInvoiceInfo(batchQueryCond);
		
		//暂时将查询结果以JSON格式显示
		//TODO 后续可以采用table等更加直观的方式展示
		//String resultJSON=JSON.toJSONString(inventoryList,true);  //会将第一个字母小写
		Gson gson = new Gson();
		String resultJSON=gson.toJson(resultObj);  //会保持字段的大小写.
		//log.debug("test data is:"+resultJSON);
	
		//set data  to model
		model.addAttribute("resultObj", resultObj);
		model.addAttribute("resultJSON", resultJSON);
		
		//return result view
		String page=TEMPLATE_PATH+"/querybatchinvoiceinfo/querybatchinvoiceinfo_result";
		return page;
	}
	
	
}
