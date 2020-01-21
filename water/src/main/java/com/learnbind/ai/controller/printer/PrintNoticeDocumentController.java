package com.learnbind.ai.controller.printer;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.DocumentException;
import com.learnbind.ai.base.common.DateUtil;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumLocalNodeType;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.printer.PrinterService;
import com.learnbind.ai.util.pdf.CreateLabel;
import com.learnbind.ai.util.pdf.PdfPathUtil;
import com.learnbind.ai.util.pdf.PrintFile;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meterbook
 *
 * @Title: PrintNoticeDocumentController.java
 * @Description: 打印水费通知单（小区）
 *
 * @author Thinkpad
 * @date 2019年8月7日 下午8:35:25
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/print-notice")
public class PrintNoticeDocumentController {

	//private static Log log = LogFactory.getLog(PrintNoticeDocumentController.class);
	private static final String TEMPLATE_PATH = "printer/printer_notice_document/"; // 页面目录
	
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录
	@Autowired
	private PrinterService printConfigService;//打印机配置
	@Autowired
	private PrintFile printFile;
	@Autowired
	public CustomerMeterService customerMeterService;//客户-表计关系服务
	@Autowired
	private CreateLabel createLabel;
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	@Autowired
	private CustomersService customersService;//客户档案信息
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量信息
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "print_notice_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "print_notice_main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model      ModelView中传递数据的对象
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件
	 * @return
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond, String period, Integer oweMonth) {
		//Long operatorId = this.getOperatorId();
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		//查询所有结算成功的账单
		Integer settlementStatus = EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue();
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
		List<Customers> customersList = customersService.getDefaultCustomerList(traceIds, searchCond, customerType, oweMonth);
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		PageInfo<Customers> pageInfo = new PageInfo<>(customersList);// (使用了拦截器或是AOP进行查询的再次处理)
		for(Customers customer : customersList) {
			String traceId = locationCustomerService.getTraceIds(customer.getId());
			String place = locationService.getPlace(traceId);
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("period", period);
			//查询客户的所有账单
			List<CustomerAccountItem> accountItemList = customerAccountItemService.getAllCustomerAccountItemByPeriod(customer.getId(), period);
			BigDecimal oweAmount = new BigDecimal("0");
			BigDecimal overdueOweAmount = new BigDecimal("0");
			for(CustomerAccountItem account : accountItemList) {
				//计算账单总金额
				BigDecimal oweAmountTemp = BigDecimalUtils.subtract(account.getCreditAmount(), account.getDebitAmount());
				//计算违约金欠费总金额
				BigDecimal overdueOweAmountTemp = customerAccountItemService.getOverdueBillOweAmountSum(account.getId());
				oweAmount= BigDecimalUtils.add(oweAmount, oweAmountTemp);
				overdueOweAmount= BigDecimalUtils.add(overdueOweAmount, overdueOweAmountTemp);
				//customerMap.put("accountDate", account.getAccountDate());
			}
			//查询往期欠费金额（水费+违约金+分账单）
			List<CustomerAccountItem> pastAccountItemList =customerAccountItemService.getPastOweAmount(period, customer.getId());
			BigDecimal pastOweAmount = new BigDecimal("0");
			//创建一个数组存储欠费的期间
			List<String> list = new ArrayList<>();
			for(CustomerAccountItem item : pastAccountItemList) {
				//望去往期账单的欠费金额
				BigDecimal pastOweAmountTemp = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
				pastOweAmount = BigDecimalUtils.add(pastOweAmount, pastOweAmountTemp);
				String pastPeriod = item.getPeriod();
				if(!list.contains(pastPeriod)){//判断数组中是否包含重复元素
					list.add(pastPeriod);
				}
			}
			if(BigDecimalUtils.greaterThan(oweAmount, new BigDecimal("0"))) {
				list.add(period);
			}
			
			pastOweAmount = BigDecimalUtils.add(overdueOweAmount, pastOweAmount);
			customerMap.put("oweAmount", oweAmount);
			customerMap.put("overdueOweAmount", overdueOweAmount);
			customerMap.put("pastOweAmount", pastOweAmount);
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
			//totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastOweAmount);
			customerMap.put("place", place);
			customerMap.put("pastPeriodList", list);
			customerMap.put("totalOweAmount", totalOweAmount);
			accountItemMapList.add(customerMap);
			
			
		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "print_notice_table";
	}
	
	/**
	 * @Title: loadDialog
	 * @Description: 预览通知单对话框
	 * @param itemId
	 * @param period
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-single-previe-notice-dialog")
	public String loadDialog(Long itemId, String period, String place, Model model) {
		Customers currItem = customersService.selectByPrimaryKey(itemId);
		
		model.addAttribute("period", period);
		model.addAttribute("currItem",currItem);
		model.addAttribute("place",place);
		return TEMPLATE_PATH + "single_preview_dialog";
	}
	
	/**
	 * @Title: previewPrepare
	 * @Description: 预览水费通知单
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/single-preview-notice")
	@ResponseBody
	public Object singlePreviewPrepare(Model model,Long customerId, String period, Integer noticeType, String place, String deductDate, String endDate) throws Exception {
		String TEMPLATE_FILE_NAME="";  //模板名称.
		final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
		if(noticeType == 1) {//选择的是欠费通知单模板
			TEMPLATE_FILE_NAME = "arrearsNoticeTemplate";
		} else if (noticeType == 2){//选择的是停水通知单模板
			TEMPLATE_FILE_NAME = "waterStopNoticeTemplate";
		}
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());//TODO //PDF所在
		//获取选择的客户集合
		List<Map<String, Object>> recordMapList = new ArrayList<>();
		
		Map<String, Object> customerMap = new HashMap<>();
		Customers customer = customersService.selectByPrimaryKey(customerId);
		
		// 水费欠费金额
		BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
		// 查询往期欠费
		BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
		String traceId = locationCustomerService.getTraceIds(customerId);
		String customerPlace = locationService.getPlace(traceId);
		place = customerPlace.replaceFirst("-", "");
		
		
		// 查询本月水费欠费金额（本期欠费+往期欠费）
		BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
		String pastPeriod = this.getPastPeriod(period);
		customerMap.put("totalOweAmount", totalOweAmount);
		customerMap.put("period", period);
		customerMap.put("place", place);
		customerMap.put("pastPeriod", pastPeriod);
		customerMap.put("customerName", customer.getCustomerName());
		
		//判断是否需要加回车换行 added by srd on 2019/12/26
		String isEnterStr = place+totalOweAmount.toString();
		int byteLength = isEnterStr.getBytes().length;
		System.out.println("----------字符串："+isEnterStr+"，字节："+byteLength);
		boolean isEnter = true;
		if(byteLength>28) {
			isEnter = false;
		}
		customerMap.put("isEnter", isEnter);
		
		Date d=new Date();
		
		//获取日期
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy年MM月dd日");   
		SimpleDateFormat sysDateFormat=new SimpleDateFormat("yyyy年MM月");   
		//水费期间字符串
		String periodStr = period.replace("-", "年");
		customerMap.put("periodStr", periodStr);
		//获取本月扣费日期
		DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
		Date sysdate = new Date();
		//***********************扣费日期*********************
		if(StringUtils.isNotBlank(deductDate)) {
			Date deductTime=stringToDate.parse(deductDate);
			customerMap.put("deductDateStr", sDateFormat.format(deductTime));
			//获取下月扣费日期
			customerMap.put("nextDeductDateStr", sysDateFormat.format(deductTime.getTime() + (long)30 * 24 * 60 * 60 * 1000));
		} else {
			customerMap.put("deductDateStr", sDateFormat.format(sysdate));
			//获取下月扣费日期
			customerMap.put("nextDeductDateStr", sysDateFormat.format(sysdate.getTime() + (long)30 * 24 * 60 * 60 * 1000));
		}
		
		//***********************缴费截止日期*********************
		if(StringUtils.isNotBlank(endDate)) {
			Date endTime=stringToDate.parse(endDate);
			customerMap.put("endDateStr", sDateFormat.format(endTime));
		} else {
			customerMap.put("endDateStr", sysDateFormat.format(sysdate.getTime() + (long)7 * 24 * 60 * 60 * 1000));
		}
		//将期间转换成时间
		DateFormat stringToDate2= new SimpleDateFormat("yyyy-MM");
		Date currPeriod = stringToDate2.parse(period);
		customerMap.put("currPeriodStr", sysDateFormat.format(currPeriod));
		String perPeriod = sysDateFormat.format(currPeriod.getTime() - (long)30 * 24 * 60 * 60 * 1000);
		customerMap.put("perPeriodStr", perPeriod);
		customerMap.put("sysdate", sDateFormat.format(d));
		recordMapList.add(customerMap);
		
		
		String pdfFileName = createLabel.createSinglePriviewNoticePDF(FILE_DIR,TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,recordMapList, customer.getCustomerName(), period);
		return RequestResultUtil.getResultUpdateSuccess("生成PDF成功！");
	}
	
	/**
	 * @Title: getPastPeriod
	 * @Description: 获取上个期间
	 * @param period
	 * @return 
	 * @throws ParseException 
	 */
	public String getPastPeriod(String period) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Date dt=sdf.parse(period);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH,-1);//日期减1个月
        Date dt1=rightNow.getTime();
        String reStr = sdf.format(dt1);
		return reStr;
	}
	
	/**
	 * @Title: getPastPeriod
	 * @Description: 获取下个期间
	 * @param period
	 * @return 
	 * @throws ParseException 
	 */
	public String getNextPeriod(String period) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Date dt=sdf.parse(period);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.MONTH,+1);//日期减1个月
        Date dt1=rightNow.getTime();
        String reStr = sdf.format(dt1);
		return reStr;
	}

	
	
	/**
	 * @Title: main
	 * @Description: 小区列表
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/unit-main")
	public String unitMain(Model model) {
		
		return TEMPLATE_PATH + "unit_main";
	}
	
	/**
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param traceIds
	 * @param period
	 * @param locaBlockTraceIds
	 * @return
	 * 单元详情
	 */
	@RequestMapping(value = "/detail-table")
	public String detailTable(Model model, Integer pageNum, Integer pageSize, String traceIds, String period, String locaBlockTraceIds) {
		//Long operatorId = this.getOperatorId();
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		//查询小区-楼号-单元列表
		String unit = EnumLocalNodeType.LOCAL_NODE_TYPE_UNIT.getCode();
		accountItemMapList = locationService.getUnitList(traceIds, unit);
		//查询小区名称
		String blockName = locationService.getBlockNameByTraceIds(locaBlockTraceIds);
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			accountItemMap.put("period", period);
			accountItemMap.put("blockName", blockName);
		}
		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);


		return TEMPLATE_PATH + "detail_table";
	}
	
	

	/**
	 * @Title: previewPrepare
	 * @Description: 预览水费通知单
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/previewPrepare")
	@ResponseBody
	public Object previewPrepare(Model model, String traceIds, String period, String blockName, String pycode) throws Exception {
		final String TEMPLATE_FILE_NAME="blockTemplate";  //模板名称.
		final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
		
		//final String FILE_DIR = "src/main/resources/templates/bottlelabel/";  //PDF所在
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());//TODO //PDF所在
		Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
		List<Map<String, Object>> recordMapList = new ArrayList<>();
		String preDate = "";
		String currDate = "";
		List<Customers> customersList = customersService.getDefaultCustomerList(traceIds, null, null, null);
		//水费通知单缴费日期
		String nextPeriod = this.getNextPeriod(period);
		for(Customers customer : customersList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			String traceId = locationCustomerService.getTraceIds(customer.getId());
			String place = locationService.getPlace(traceId);
			//查询客户的所有账单
			List<CustomerAccountItem> accountItemList = customerAccountItemService.getAllCustomerAccountItemByPeriod(customer.getId(), period);
			BigDecimal oweAmount = new BigDecimal("0");
			BigDecimal overdueOweAmount = new BigDecimal("0");
			String preRead = "";
			String currRead = "";
			
			SimpleDateFormat f = new SimpleDateFormat("MM.dd");
			BigDecimal waterAmount = new BigDecimal("0");
			List<MeterRecord> meterRecordList = meterRecordService.getListByCustomerIdAndPeriod(customer.getId(), period, traceIds);
			if (meterRecordList.size() > 0) {
				for (MeterRecord record : meterRecordList) {
					//获取本期抄表底数、上期抄表底数、水量
					preRead = record.getPreRead()+","+preRead;
					currRead = record.getCurrRead()+","+currRead;
					preDate = f.format(record.getPreDate());
					currDate = f.format(record.getCurrDate());
					waterAmount = BigDecimalUtils.add(record.getCurrAmount(), waterAmount);
				}
				if (preRead.endsWith(",")) {
					 preRead = preRead.substring(0,preRead.length() - 1);
			     }
				 if (currRead.endsWith(",")) {
					 currRead = currRead.substring(0,currRead.length() - 1);
			     }
			}
			if(accountItemList.size() > 0 ) {
				for(CustomerAccountItem account : accountItemList) {
					
					//计算账单总金额
					BigDecimal oweAmountTemp = BigDecimalUtils.subtract(account.getCreditAmount(), account.getDebitAmount());
					//计算违约金欠费总金额
					BigDecimal overdueOweAmountTemp = customerAccountItemService.getOverdueBillOweAmountSum(account.getId());
					oweAmount= BigDecimalUtils.add(oweAmount, oweAmountTemp);
					overdueOweAmount= BigDecimalUtils.add(overdueOweAmount, overdueOweAmountTemp);
				}
				
			}
			//查询往期欠费金额（水费+违约金+分账单）
			List<CustomerAccountItem> pastAccountItemList =customerAccountItemService.getPastOweAmount(period, customer.getId());
			BigDecimal pastOweAmount = new BigDecimal("0");
			for(CustomerAccountItem item : pastAccountItemList) {
				//望去往期账单的欠费金额
				BigDecimal pastOweAmountTemp = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
				pastOweAmount = BigDecimalUtils.add(pastOweAmount, pastOweAmountTemp);
			}
			pastOweAmount = BigDecimalUtils.add(overdueOweAmount, pastOweAmount);
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
			//totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastOweAmount);
			customerMap.put("place", customer.getRoom());
			customerMap.put("preRead", preRead);
			customerMap.put("currRead", currRead);
			customerMap.put("preDate", preDate);
			customerMap.put("currDate", currDate);
			customerMap.put("waterAmount", waterAmount);
			customerMap.put("oweAmount", oweAmount);
			customerMap.put("pastOweAmount", pastOweAmount);
			customerMap.put("totalOweAmount", totalOweAmount);
			recordMapList.add(customerMap);
		}
		String pdfFileName = createLabel.createCustomerPeoplePDF(FILE_DIR,TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,recordMapList, blockName, period, pycode, preDate, currDate, nextPeriod);
		return RequestResultUtil.getResultUpdateSuccess("生成PDF成功！");
	}

	/** 
	 *	@Title loadPrinterDialog 
	 *	@Description 动态加载打印机对话框 
	 *	@param model
	 *	@return		打印机选择对话框视图
	 *	@Return String    返回类型 
	 *
	 *	@Date 2019年1月1日 下午9:54:38
	*/
	@RequestMapping(value = "/loadPrinterDialog")
	public String loadPrinterDialog(Model model, String traceId) {
		final String VIEW_NAME="printer_dialog";  //视图名称
		//查询打印机配置. 并加入到视图中.
		List<PrinterConfig> printerConfigList=printConfigService.selectAll();		
		model.addAttribute("printerConfigList", printerConfigList);
		model.addAttribute("traceId", traceId);
		return  TEMPLATE_PATH+VIEW_NAME;
	}
	
	/** 
	 *	@Title sendPDFToPrinter 
	 *	@Description 将PDF文件发送到打印机
	 * 
	 *	@param pdfFileList  PDF文件列晴
	 *	@param printerId	printer ID
	 *
	 *	@throws IOException
	 *	@throws PrinterException
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2019年1月2日 上午1:26:42
	*/
	private void sendPDFToPrinter(List<String> pdfFileList,Long printerId) throws IOException, PrinterException {
		PrinterConfig printerConfig=printConfigService.selectByPrimaryKey(printerId);
		//打印PDF文件
		for(int i=0;i<pdfFileList.size();i++) {
			System.out.println(pdfFileList.get(i));
			
			printFile.printPDF(pdfFileList.get(i), printerConfig.getPrinterName());
		}
	}
	
	
	
	/** 
	 *	@Title printBottleLabel 
	 *	@Description 打印水费通知单
	 * 
	 *	@param searchParms  
	 *	@param searchMode
	 *	@param model
	 *	@return
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws PrinterException 
	 * @throws ParseException 
	 *     
	 *	@Return String    返回类型 
	 *
	 *	@Date 2018年12月31日 上午5:35:43
	*/
	@RequestMapping(value = "/printBottleLabel", produces = "application/json")
	@ResponseBody
	public Object printBottleLabel(String traceIdJSON, Long printerId, String period, String blockName) throws DocumentException, IOException, PrinterException, ParseException {
		final String TEMPLATE_FILE_NAME="blockTemplate";  //模板名称.
		
		final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
		//String FILE_DIR = "src/main/resources/templates/bottlelabel/"; 
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());//TODO  //PDF所在
		
		
		List<String> traceIdList = JSON.parseArray(traceIdJSON, String.class);
		List<String> pdfFileList = new ArrayList<>();
		
		
		for(String traceIds : traceIdList) {
			List<Map<String, Object>> recordMapList = new ArrayList<>();
			String preDate = "";
			String currDate = "";
			BigDecimal zero = new BigDecimal("0");
			Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
			List<Customers> customersList = customersService.getDefaultCustomerList(traceIds, null, null, null);
			//水费通知单缴费日期
			String nextPeriod = this.getNextPeriod(period);
			for(Customers customer : customersList) {
				Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
				String traceId = locationCustomerService.getTraceIds(customer.getId());
				String place = locationService.getPlace(traceId);
				//查询客户的所有账单
				List<CustomerAccountItem> accountItemList = customerAccountItemService.getAllCustomerAccountItemByPeriod(customer.getId(), period);
				BigDecimal oweAmount = new BigDecimal("0");
				BigDecimal overdueOweAmount = new BigDecimal("0");
				String preRead = "";
				String currRead = "";
				
				SimpleDateFormat f = new SimpleDateFormat("MM.dd");
				BigDecimal waterAmount = new BigDecimal("0");
				List<MeterRecord> meterRecordList = meterRecordService.getListByCustomerIdAndPeriod(customer.getId(), period, traceIds);
				if (meterRecordList.size() > 0) {
					for (MeterRecord record : meterRecordList) {
						//获取本期抄表底数、上期抄表底数、水量
						preRead = record.getPreRead()+","+preRead;
						currRead = record.getCurrRead()+","+currRead;
						preDate = f.format(record.getPreDate());
						currDate = f.format(record.getCurrDate());
						waterAmount = BigDecimalUtils.add(record.getCurrAmount(), waterAmount);
					}
					if (preRead.endsWith(",")) {
						 preRead = preRead.substring(0,preRead.length() - 1);
				     }
					 if (currRead.endsWith(",")) {
						 currRead = currRead.substring(0,currRead.length() - 1);
				     }
				}
				if(accountItemList.size() > 0 ) {
					for(CustomerAccountItem account : accountItemList) {
						
						//计算账单总金额
						BigDecimal oweAmountTemp = BigDecimalUtils.subtract(account.getCreditAmount(), account.getDebitAmount());
						//计算违约金欠费总金额
						BigDecimal overdueOweAmountTemp = customerAccountItemService.getOverdueBillOweAmountSum(account.getId());
						oweAmount= BigDecimalUtils.add(oweAmount, oweAmountTemp);
						overdueOweAmount= BigDecimalUtils.add(overdueOweAmount, overdueOweAmountTemp);
					}
					
				}
				//查询往期欠费金额（水费+违约金+分账单）
				List<CustomerAccountItem> pastAccountItemList =customerAccountItemService.getPastOweAmount(period, customer.getId());
				BigDecimal pastOweAmount = new BigDecimal("0");
				for(CustomerAccountItem item : pastAccountItemList) {
					//望去往期账单的欠费金额
					BigDecimal pastOweAmountTemp = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
					pastOweAmount = BigDecimalUtils.add(pastOweAmount, pastOweAmountTemp);
				}
				pastOweAmount = BigDecimalUtils.add(overdueOweAmount, pastOweAmount);
				BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
				//totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastOweAmount);
				customerMap.put("place", customer.getRoom());
				customerMap.put("preRead", preRead);
				customerMap.put("currRead", currRead);
				customerMap.put("preDate", preDate);
				customerMap.put("currDate", currDate);
				customerMap.put("waterAmount", waterAmount);
				customerMap.put("oweAmount", oweAmount);
				customerMap.put("pastOweAmount", pastOweAmount);
				customerMap.put("totalOweAmount", totalOweAmount);
				recordMapList.add(customerMap);
			}
			//查询用户地址
			String pycode = locationService.getUnitLongCode(traceIds);
			String pdfFileName = createLabel.createCustomerPeoplePDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,recordMapList, blockName, period, pycode, preDate, currDate, nextPeriod);
			pdfFileList.add(pdfFileName);
			
		}
		sendPDFToPrinter(pdfFileList,printerId);
		return RequestResultUtil.getResultUpdateSuccess("打印通知单成功！");
	}
	
	@RequestMapping(value = "/load-single-printer-dialog")
	public String loadSinglePrinterDialog(Model model, Long customerId, String place) {
		final String VIEW_NAME="single_printer_dialog";  //视图名称
		//查询打印机配置. 并加入到视图中.
		List<PrinterConfig> printerConfigList=printConfigService.selectAll();		
		model.addAttribute("printerConfigList", printerConfigList);
		model.addAttribute("customerId", customerId);
		model.addAttribute("place", place);
		return  TEMPLATE_PATH+VIEW_NAME;
	}
	
	/**
	 * @Title: printSingle
	 * @Description: 单印单个用户通知单
	 * @param printerId
	 * @param period
	 * @param customerId
	 * @param noticeType
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/print-single-notice", produces = "application/json")
	@ResponseBody
	public Object printSingle(String customerIdJSON,Long printerId, String period,  Integer noticeType, String place, 
			String deductDate, String endDate, String traceIds, Integer oweMonth, String searchCond) throws DocumentException, IOException, PrinterException, ParseException {
		String TEMPLATE_FILE_NAME="blockTemplate";  //模板名称.
		if(noticeType == 1) {//选择的是欠费通知单模板
			TEMPLATE_FILE_NAME = "arrearsNoticeTemplate";
		} else if (noticeType == 2){//选择的是停水通知单模板
			TEMPLATE_FILE_NAME = "waterStopNoticeTemplate";
		}
		final String TEMPLATE_PREFIX="templates/bottlelabel/labeltemplate/";		//模板文件所在的目录
		List<String> pdfFileList = new ArrayList<>();
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());//TODO  //PDF所在
		List<Long> customerIdList = new ArrayList<>();
		Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
		
		if (StringUtils.isBlank(customerIdJSON)) {// 批量打印时根据查询条件打印
			List<Customers> customersList = customersService.getDefaultCustomerList(traceIds, searchCond, customerType, oweMonth);
			for(Customers customer : customersList) {
				customerIdList.add(customer.getId());
			}
		} else {// 批量打印时勾选进行打印
			if(!customerIdJSON.contains(",")) {
				customerIdList.add(Long.valueOf(customerIdJSON));
			} else {
				String[] s1 = customerIdJSON.split(",");
				for (String customerId : s1) {
					customerIdList.add(Long.valueOf(customerId));
				}
				customerIdList = JSON.parseArray(customerIdJSON, Long.class);
			}
			
		}
		
		List<Map<String, Object>> recordMapList = new ArrayList<>();
		
		for(Long customerId : customerIdList) {
			String traceId = locationCustomerService.getTraceIds(customerId);
			String customerPlace = locationService.getPlace(traceId);
			Map<String, Object> customerMap = new HashMap<>();
			Customers customer = customersService.selectByPrimaryKey(customerId);
			// 水费欠费金额
			BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
			// 查询往期欠费
			BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
			
			// 查询本月水费欠费金额（本期欠费+往期欠费）
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
			String pastPeriod = this.getPastPeriod(period);
			customerMap.put("totalOweAmount", totalOweAmount);
			customerMap.put("period", period);
			place = customerPlace.replaceFirst("-", "");
			customerMap.put("place", place);
			customerMap.put("pastPeriod", pastPeriod);
			customerMap.put("customerName", customer.getCustomerName());
			
			//判断是否需要加回车换行 added by srd on 2019/12/26
			String isEnterStr = place+totalOweAmount.toString();
			int byteLength = isEnterStr.getBytes().length;
			System.out.println("----------字符串："+isEnterStr+"，字节："+byteLength);
			boolean isEnter = true;
			if(byteLength>28) {
				isEnter = false;
			}
			customerMap.put("isEnter", isEnter);
			
			Date d=new Date();
			//获取日期
			SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy年MM月dd日");   
			SimpleDateFormat sysDateFormat=new SimpleDateFormat("yyyy年MM月");   
			//水费期间字符串
			String periodStr = period.replace("-", "年");
			customerMap.put("periodStr", periodStr);
			//获取本月扣费日期
			DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
			
			Date sysdate = new Date();
			//***********************扣费日期*********************
			if(StringUtils.isNotBlank(deductDate)) {
				Date deductTime=stringToDate.parse(deductDate);
				customerMap.put("deductDateStr", sDateFormat.format(deductTime));
				//获取下月扣费日期
				customerMap.put("nextDeductDateStr", sysDateFormat.format(deductTime.getTime() + (long)30 * 24 * 60 * 60 * 1000));
			} else {
				customerMap.put("deductDateStr", sDateFormat.format(sysdate));
				//获取下月扣费日期
				customerMap.put("nextDeductDateStr", sysDateFormat.format(sysdate.getTime() + (long)30 * 24 * 60 * 60 * 1000));
			}
			
			//***********************缴费截止日期*********************
			if(StringUtils.isNotBlank(endDate)) {
				Date endTime=stringToDate.parse(endDate);
				customerMap.put("endDateStr", sDateFormat.format(endTime));
			} else {
				customerMap.put("endDateStr", sysDateFormat.format(sysdate.getTime() + (long)7 * 24 * 60 * 60 * 1000));
			}
			//将期间转换成时间
			DateFormat stringToDate2= new SimpleDateFormat("yyyy-MM");
			Date currPeriod = stringToDate2.parse(period);
			customerMap.put("currPeriodStr", sysDateFormat.format(currPeriod));
			String perPeriod = sysDateFormat.format(currPeriod.getTime() - (long)30 * 24 * 60 * 60 * 1000);
			customerMap.put("perPeriodStr", perPeriod);
			customerMap.put("sysdate", sDateFormat.format(d));
			recordMapList.add(customerMap);
			
		}
		
		//将通知单4个一组发送给打印机
		List<List<Map<String, Object>>> recordMapListList = this.groupList(recordMapList);
		for(List<Map<String, Object>>  temp : recordMapListList) {
			String location = temp.get(0).get("place").toString();
			String pdfFileName = createLabel.createSinglePriviewNoticePDF(FILE_DIR,TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,temp, location, period);
			pdfFileList.add(pdfFileName);
		}
		
		sendPDFToPrinter(pdfFileList,printerId);
		return RequestResultUtil.getResultUpdateSuccess("打印通知单成功！");
	}
	
	//*****************************************将通知单分组，4个一组************************
	private  List<List<Map<String, Object>>> groupList(List<Map<String, Object>> recordMapList) {
		// 将要构造的集合
		List<List<Map<String, Object>>> recordMapListList = new ArrayList<>();
		// 如果原始集合数小于4，就直接返回了
		if(recordMapList.size() <= 4){
			recordMapListList.add(recordMapList);
		}else{
			// 循环原有集合
			this.groupGreater(recordMapList, recordMapListList);
		}
		return  recordMapListList;
	}
	
	
	/**
	 * 对大于4个元素集合进行分组
	 * @param waterList
	 * @param waterListList
	 */
	private static void groupGreater(List<Map<String, Object>> recordMapList, List<List<Map<String, Object>>> recordMapListList) {
		// 总共分组数
		int groupNum = recordMapList.size()/4 +1 ;
		for(int i = 0 ;i < recordMapList.size();i++) {
			// 将要存放数据的，4个一组，最大size为4
			List<Map<String, Object>> newMapList = new ArrayList();
			// 如果碰到4的倍数，就将当前和前边3个元素放入集合中
			if((i+1)%4 == 0) {
				newMapList.add(recordMapList.get(i));
				newMapList.add(recordMapList.get(i-1));
				newMapList.add(recordMapList.get(i-2));
				newMapList.add(recordMapList.get(i-3));
				// 将临时构建的list放回
				groupNum --;
				recordMapListList.add(newMapList);
				continue;
			}else { 
				// 如果剩余分组数是1
				if(groupNum == 1) {
					for(int j = i ;j < recordMapList.size();j++) {
						newMapList.add(recordMapList.get(j));
					}
					// 将临时构建的list放回
					recordMapListList.add(newMapList);
					break;
				}
			}
		}
	}
	
	//------------------------------	导出Excel	------------------------------
	/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-owe-notice-excel")
	public void exportMeterRecordExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer oweMonth, String traceIds, String period) {
		
		//String period = DateUtils.getYearMonth();//期间默认本月
		//Long operatorId = this.getOperatorId();
		//excel标题
		String[] titles = { "地理位置", "客户姓名", "期间", "账单金额", "往期欠费金额", "欠费月份", "欠费期数", "欠费总金额"};
		//excel列名
		String[] columnNames = { "place", "customerName", "period", "oweAmount", "pastOweAmount", "pastPeriodList", "oweMonth", "totalOweAmount"};
		//sheet名
		String sheetName = period+"抄表记录";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, oweMonth, traceIds, period);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//获取导出EXCEL的文件名
		String fileName = this.getFileName(period, traceIds);
		
		File file = new File(realPath+fileName);
		
		//文件输出流
	    FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    try {
			wb.write(outStream);
			outStream.flush();
			outStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    System.out.println("导出文件成功！文件导出路径：--"+file);
	    
	    try {
			DownLoadFileUtil.downLoad(fileName, "xls", realPath+fileName, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * @Title: getExportExcelData
	 * @Description: 获取导出excel
	 * @return 
	 */
	private List<Map<String, Object>> getExportExcelData(String searchCond, Integer oweMonth, String traceIds, String period){
		Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
		List<Customers> customersList = customersService.getDefaultCustomerList(traceIds, searchCond, customerType, oweMonth);
		//List<Map<String, Object>> recordMapList = meterRecordService.getExportMeterRecordData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate);
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		PageInfo<Customers> pageInfo = new PageInfo<>(customersList);// (使用了拦截器或是AOP进行查询的再次处理)
		for(Customers customer : customersList) {
			String traceId = locationCustomerService.getTraceIds(customer.getId());
			String place = locationService.getPlace(traceId);
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			//查询客户的所有账单
			List<CustomerAccountItem> accountItemList = customerAccountItemService.getAllCustomerAccountItemByPeriod(customer.getId(), period);
			BigDecimal oweAmount = new BigDecimal("0");
			BigDecimal overdueOweAmount = new BigDecimal("0");
			for(CustomerAccountItem account : accountItemList) {
				//计算账单总金额
				BigDecimal oweAmountTemp = BigDecimalUtils.subtract(account.getCreditAmount(), account.getDebitAmount());
				//计算违约金欠费总金额
				BigDecimal overdueOweAmountTemp = customerAccountItemService.getOverdueBillOweAmountSum(account.getId());
				oweAmount= BigDecimalUtils.add(oweAmount, oweAmountTemp);
				overdueOweAmount= BigDecimalUtils.add(overdueOweAmount, overdueOweAmountTemp);
				//customerMap.put("accountDate", account.getAccountDate());
			}
			//查询往期欠费金额（水费+违约金+分账单）
			List<CustomerAccountItem> pastAccountItemList =customerAccountItemService.getPastOweAmount(period, customer.getId());
			BigDecimal pastOweAmount = new BigDecimal("0");
			//创建一个数组存储欠费的期间
			List<String> list = new ArrayList<>();
			for(CustomerAccountItem item : pastAccountItemList) {
				//望去往期账单的欠费金额
				BigDecimal pastOweAmountTemp = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
				pastOweAmount = BigDecimalUtils.add(pastOweAmount, pastOweAmountTemp);
				String pastPeriod = item.getPeriod();
				if(!list.contains(pastPeriod)){//判断数组中是否包含重复元素
					list.add(pastPeriod);
				}
			}
			if(BigDecimalUtils.greaterThan(oweAmount, new BigDecimal("0"))) {
				list.add(period);
			}
			
			pastOweAmount = BigDecimalUtils.add(overdueOweAmount, pastOweAmount);
			customerMap.put("period", period);
			customerMap.put("oweAmount", oweAmount);
			customerMap.put("overdueOweAmount", overdueOweAmount);
			customerMap.put("pastOweAmount", pastOweAmount);
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
			//totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastOweAmount);
			customerMap.put("place", place);
			customerMap.put("pastPeriodList", list);
			customerMap.put("oweMonth", list.size());
			customerMap.put("totalOweAmount", totalOweAmount);
			accountItemMapList.add(customerMap);
			
			
		}
		//TODO 导出数据进行排序
		//this.sort(recordMapList);
		
//		for(Map<String, Object> recordMap : recordMapList) {
//			String room = recordMap.get("ROOM").toString();//ROOM
//			//String currTraceIds = recordMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
//			String customerId = recordMap.get("CUSTOMER_ID").toString();//客户ID
//			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
//			
//			//String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
//			recordMap.put("room", room);
//			recordMap.put("customerName", customer.getCustomerName());
//			recordMap.put("period", period);
//			if(recordMap.get("PRE_DATE") != null) {
//				recordMap.put("preDateStr", recordMap.get("PRE_DATE").toString());
//			}
//			if(recordMap.get("PRE_READ") != null) {
//				recordMap.put("preRead", recordMap.get("PRE_READ").toString());
//			}
//			if(recordMap.get("CURR_DATE") != null) {
//				recordMap.put("currDateStr",recordMap.get("CURR_DATE").toString());
//			}
//			if(recordMap.get("CURR_READ") != null) {
//				recordMap.put("currRead",recordMap.get("CURR_READ").toString());
//			}
//			if(recordMap.get("CURR_AMOUNT") != null) {
//				recordMap.put("currAmount",recordMap.get("CURR_AMOUNT").toString());
//			}
//			if(recordMap.get("READ_MODE") != null) {
//				recordMap.put("readModeStr",EnumReadMode.getName(recordMap.get("READ_MODE").toString()));
//			}
//			
//			if(recordMap.get("REMARK") != null) {
//				recordMap.put("remark",recordMap.get("REMARK").toString());
//			}
//			
//		}
		
		return accountItemMapList;
	}
	
	/**
	 * 获取文件路径
	 * @param request
	 * @return
	 */
	private String getRealPath(HttpServletRequest request){
		String realPath = this.getPath();
		realPath = realPath+File.separator+"export excel"+File.separator;
		
		File temp = new File(realPath);
		//如果文件路径不存在，则创建
		if(!temp.exists()){
			temp.mkdirs();
		}
		return realPath;
	}
	/**
	 * 获取导出EXCEL文件名
	 * @return
	 */
	private String getFileName(String period, String traceIds){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = sdf.format(new Date());
		String place = locationService.getPlace(traceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
		// excel文件名
		String fileName = period;
		
		if(StringUtils.isNotBlank(place)) {
			fileName = fileName+"-"+place;
		}
		
		fileName = fileName+"-"+times+"-"+"欠费通知"+".xls";
		
		return fileName;
	}
	
	
	//------------------------------	上传文件通用方法	------------------------------
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getLinuxUploadFolder();
		if(this.isWindowsOS()) {
			path = uploadFileConfig.getWindowsUploadFolder();
		}
		System.out.println("----------"+path);
		return path;
	}
	
	/**
	 * @Title: isWindowsOS
	 * @Description: 判断操作系统是否是Windows
	 * @return 
	 * 		true=windows;false=其他
	 */
	private boolean isWindowsOS() {
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		System.out.println("==============================操作系统："+os);
    		return true;
    	}else {
    		System.out.println("==============================操作系统："+os);
    		return false;
    	}
	}
	
	/**
	 * @return	
	 */
	private String getFTPPath() {
		
		String ymd = DateUtil.getYYYYMMDDDate(new Date());//返回YYYYMMDD格式的日期
		
		String path = uploadFileConfig.getUploadFolder();
		path = path + File.separator+"pdf"+File.separator+ymd+File.separator;
		return path;
	}

	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;// 操作员ID
		if (userBean != null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for (SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}

			if (roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_METER_READER) != -1) {
				operatorId = userBean.getId();// 操作员ID
			}

		}
		return operatorId;
	}
}