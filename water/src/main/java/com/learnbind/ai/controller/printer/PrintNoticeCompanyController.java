package com.learnbind.ai.controller.printer;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.DocumentException;
import com.learnbind.ai.base.common.DateUtil;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.EnumWaterNotifyStatus;
import com.learnbind.ai.common.enumclass.EnumNotifyUseLocation;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBillInfo;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.WaterNotify;
import com.learnbind.ai.model.WaterNotifyDetail;
import com.learnbind.ai.service.customers.BillService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.notify.WaterNotifyDetailService;
import com.learnbind.ai.service.notify.WaterNotifyService;
import com.learnbind.ai.service.printer.PrinterService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.util.pdf.CreateLabel;
import com.learnbind.ai.util.pdf.PdfPathUtil;
import com.learnbind.ai.util.pdf.PrintFile;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meterbook
 *
 * @Title: PrintNoticeDocumentController.java
 * @Description: 打印水费通知单（单位）
 *
 * @author Thinkpad
 * @date 2019年8月7日 下午8:35:25
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/print-company")
public class PrintNoticeCompanyController {

	// private static Log log =
	// LogFactory.getLog(PrintNoticeCompanyController.class);
	private static final String TEMPLATE_PATH = "printer/printer_notice_company/"; // 页面目录
	private static final String TEMPLATE_CUSTOMER_PATH = "printer/printer_notice_company/customer/"; // 页面目录

	@Autowired
	private LocationService locationService;// 地理位置
	@Autowired
	private MeterRecordService meterRecordService;// 抄表记录
	@Autowired
	private PrinterService printConfigService;// 打印机配置
	@Autowired
	private PrintFile printFile;
	@Autowired
	public CustomerMeterService customerMeterService;// 客户-表计关系服务
	@Autowired
	private CreateLabel createLabel;
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private UploadFileConfig uploadFileConfig;// 文件上传配置信息
	@Autowired
	private CustomersService customersService;// 客户档案信息
	@Autowired
	private WaterPriceService waterPriceService;// 水价
	@Autowired
	private PartitionWaterService partitionWaterService;// 分水量信息
	@Autowired
	private MetersService metersService;// 表计档案信息
	@Autowired
	private LocationMeterService locationMeterService;// 表计-地理位置关联表
	@Autowired
	private LocationCustomerService locationCustomerService;// 地理位置-客户
	@Autowired
	private BillService billService;// 开票信息
	@Autowired
	private WaterNotifyService waterNotifyService;// 开票信息
	@Autowired
	private WaterNotifyDetailService waterNotifyDetailService;// 开票信息

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
	 * @Title: tabs
	 * @Description: 加载选项卡页面
	 * @param model
	 * @param functionModule
	 * @return
	 */
	@RequestMapping(value = "/tabs")
	public String tabs(Model model) {
		return TEMPLATE_PATH + "tabs";
	}

	// **********************************************按表计打印选项卡****************************************

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/meter-main")
	public String main(Model model) {
		// 查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);

		// 查询水价
		List<SysWaterPrice> waterPriceList = waterPriceService.selectAll();
		model.addAttribute("waterPriceList", waterPriceList);
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
	@RequestMapping(value = "/meter-table")
	public String table(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond,
			String period, String waterPrice) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();
		List<Meters> metersList = metersService.getDefaultMeterList(traceIds, searchCond, customerType, waterPrice);

		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		PageInfo<Meters> pageInfo = new PageInfo<>(metersList);// (使用了拦截器或是AOP进行查询的再次处理)
		for (Meters meter : metersList) {
			LocationMeter lm = locationMeterService.getLocationByMeterId(meter.getId());
			String meterPlace = locationService.getPlace(lm.getTraceIds());
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("period", period);

			// 查询表计的所有账单
			List<CustomerAccountItem> accountItemList = customerAccountItemService
					.getAllCustomerAccountItemByMeter(meter.getId(), period);
			BigDecimal oweAmount = new BigDecimal("0");
			BigDecimal overdueOweAmount = new BigDecimal("0");
			for (CustomerAccountItem account : accountItemList) {
				// 计算账单总金额
				BigDecimal oweAmountTemp = BigDecimalUtils.subtract(account.getCreditAmount(),
						account.getDebitAmount());
				// 计算违约金欠费总金额
				BigDecimal overdueOweAmountTemp = customerAccountItemService
						.getOverdueBillOweAmountSum(account.getId());
				oweAmount = BigDecimalUtils.add(oweAmount, oweAmountTemp);
				overdueOweAmount = BigDecimalUtils.add(overdueOweAmount, overdueOweAmountTemp);
			}

			// 查询该水表往期欠费金额（水费+违约金+分账单）
			List<CustomerAccountItem> pastAccountItemList = customerAccountItemService.getPastOweAmountByMeter(period,
					meter.getId());
			BigDecimal pastOweAmount = new BigDecimal("0");
			// 创建一个数组存储欠费的期间
			List<String> list = new ArrayList<>();
			for (CustomerAccountItem item : pastAccountItemList) {
				// 望去往期账单的欠费金额
				BigDecimal pastOweAmountTemp = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
				pastOweAmount = BigDecimalUtils.add(pastOweAmount, pastOweAmountTemp);
				String pastPeriod = item.getPeriod();
				if (!list.contains(pastPeriod)) {// 判断数组中是否包含重复元素
					list.add(pastPeriod);
				}
			}
			if (BigDecimalUtils.greaterThan(oweAmount, new BigDecimal("0"))) {
				list.add(period);
			}
			// 获取往期欠费月份字符串
			pastOweAmount = BigDecimalUtils.add(overdueOweAmount, pastOweAmount);
			meterMap.put("oweAmount", oweAmount);
			meterMap.put("overdueOweAmount", overdueOweAmount);
			meterMap.put("pastOweAmount", pastOweAmount);
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastOweAmount);
			meterMap.put("meterPlace", meterPlace);
			meterMap.put("pastPeriodList", list);
			meterMap.put("totalOweAmount", totalOweAmount);
			accountItemMapList.add(meterMap);
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
	 * @Title: previewPrepare
	 * @Description: 生成水费通知单
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/previewPrepare")
	@ResponseBody
	public Object previewPrepare(Model model, Long meterId, String period, String noticeDate, String chargePeople) throws Exception {
		BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型0，仅用于判断

		final String PRINTER_NAME = "Canon";
		final String TEMPLATE_FILE_NAME = "companySecondTemplate"; // 模板名称.单位

		final String TEMPLATE_PREFIX = "templates/bottlelabel/labeltemplate/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在

		List<Map<String, Object>> recordMapList = new ArrayList<>();
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		Map<String, Object> customerMap = new HashMap<>();
		// 获取表计的默认客户
		CustomerMeter cm = customerMeterService.getCustomerByMeterId(meterId);
		Customers customer = customersService.selectByPrimaryKey(cm.getCustomerId());
		// 获取开票信息
		Map<String, Object> billMap = this.getCustomerBillName(cm.getCustomerId());
		String customerName = billMap.get("billName").toString();
		customerMap.put("customerName", customerName);
		customerMap.put("billNo", billMap.get("billNo"));

		List<MeterRecord> recordList = meterRecordService.getMeterRecordByMeter(period, meterId);
		Meters meter = metersService.selectByPrimaryKey(meterId);
		String meterPlace = meter.getDescription();
		if (recordList.size() > 0) {
			MeterRecord record = recordList.get(0);

			Map<String, Object> recordMap = new HashMap<>();
			recordMap.put("meterPlace", meterPlace);
			recordMap.put("preRead", record.getPreRead());
			recordMap.put("currRead", record.getCurrRead());
			recordMap.put("currAmount", record.getCurrAmount());
			recordMapList.add(recordMap);
		}
		
//		// 水费欠费金额
//		BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
//		// 查询往期欠费
//		BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
//		// 查询客户余额
//		BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customer.getId());
//		// 查询本月水费欠费金额（本期欠费+往期欠费）
//		BigDecimal waterFeeOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
//		// 应缴金额
//		BigDecimal payAmount = BigDecimalUtils.subtract(customerBalance, waterFeeOweAmount);
//		if (BigDecimalUtils.lessThan(payAmount, zero)) {
//			payAmount = BigDecimalUtils.subtract(zero, payAmount);
//		}
		
		List<PartitionWater> partList =  partitionWaterService.getPartitionWaterByMeterList(meter.getId(), period);
		Map<String, Object> partwaterMap = this.getNewList(partList);
		//获取水量、基础水价、污水处理费单价、基础水费、污水处理费字符串
		String basePriceStr = partwaterMap.get("basePriceStr").toString();
		String treatmentStr = partwaterMap.get("treatmentStr").toString();
		String sumBasePriceFee = partwaterMap.get("sumBasePriceFee").toString();
		String sumTreatmentFee = partwaterMap.get("sumTreatmentFee").toString();
		String totalBasePriceFee = partwaterMap.get("totalBasePriceFee").toString();//基础水费合计
		String totaltreatmentFee = partwaterMap.get("totaltreatmentFee").toString();//污水处理费合计
		String totalFee = partwaterMap.get("totalFee").toString();//合计
		String cardMeter = "CARD_METER";
		if(StringUtils.equals(meter.getMeterType(), cardMeter)) {
			basePriceStr = "";
			treatmentStr = "";
			sumBasePriceFee = "";
			sumTreatmentFee = "";
			totalBasePriceFee = "";
			totaltreatmentFee = "";
			totalFee = "";
		} 
		
		//获取水量、基础水价、污水处理费单价、基础水费、污水处理费字符串
		customerMap.put("waterAmountStr", partwaterMap.get("waterAmountStr"));//水量字符串
		customerMap.put("basePriceStr", partwaterMap.get("basePriceStr"));//基础水价字符串
		customerMap.put("treatmentStr", partwaterMap.get("treatmentStr"));//污水处理费单价字符串
		customerMap.put("sumBasePriceFee", partwaterMap.get("sumBasePriceFee"));//基础水费字符串
		customerMap.put("sumTreatmentFee", partwaterMap.get("sumTreatmentFee"));//污水处理费字符串
		customerMap.put("totalBasePriceFee", partwaterMap.get("totalBasePriceFee"));//基础水费合计
		customerMap.put("totaltreatmentFee", partwaterMap.get("totaltreatmentFee"));//污水处理费合计
		customerMap.put("totalFee", partwaterMap.get("totalFee"));//总合计
		
//		// 本期欠费
//		customerMap.put("oweAmount", oweAmount);
//		// 往期欠费
//		customerMap.put("pastOweAmount", pastOweAmount);
//		// 余额代扣
//		customerMap.put("customerBalance", customerBalance);
//		// 应缴水费
//		customerMap.put("payAmount", payAmount);
		customerMap.put("basePriceArrears", zero);
		customerMap.put("treatmentArrears", zero);
		customerMap.put("recordMapList", recordMapList);
		//获取通知单日期
		DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月");
		if(StringUtils.isNotBlank(noticeDate)) {
			Date time=stringToDate.parse(noticeDate);
			String noticeDateStr = sDateFormat.format(time);
			customerMap.put("noticeDateStr", noticeDateStr);
		} else {
			Date sysdate = new Date();
			customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
		}
		//收费人名称
		if(chargePeople == null) {
			chargePeople ="";
		}
		customerMap.put("chargePeople",chargePeople);
		customerMapList.add(customerMap);

		String pdfFileName = createLabel.createSingleBottleLabelPDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
				customerMapList, customerName, period, meter.getPlace());
		return RequestResultUtil.getResultUpdateSuccess("生成PDF成功！");
	}

	/**
	 * @Title loadPrinterDialog
	 * @Description 动态加载打印机对话框
	 * @param model
	 * @return 打印机选择对话框视图
	 * @Return String 返回类型
	 *
	 * @Date 2019年1月1日 下午9:54:38
	 */
	@RequestMapping(value = "/loadPrinterDialog")
	public String loadPrinterDialog(Model model, Long meterId) {
		final String VIEW_NAME = "printer_dialog"; // 视图名称
		// 查询打印机配置. 并加入到视图中.
		List<PrinterConfig> printerConfigList = printConfigService.selectAll();
		model.addAttribute("printerConfigList", printerConfigList);
		model.addAttribute("meterId", meterId);
		return TEMPLATE_PATH + VIEW_NAME;
	}

	/**
	 * @Title sendPDFToPrinter
	 * @Description 将PDF文件发送到打印机
	 * 
	 * @param pdfFileList PDF文件列晴
	 * @param printerId   printer ID
	 *
	 * @throws IOException
	 * @throws PrinterException
	 * 
	 * @Return void 返回类型
	 *
	 * @Date 2019年1月2日 上午1:26:42
	 */
	private void sendPDFToPrinter(List<String> pdfFileList, Long printerId) throws IOException, PrinterException {
		PrinterConfig printerConfig = printConfigService.selectByPrimaryKey(printerId);
		// 打印PDF文件
		for (int i = 0; i < pdfFileList.size(); i++) {
			System.out.println(pdfFileList.get(i));

			printFile.printPDF(pdfFileList.get(i), printerConfig.getPrinterName());
		}
	}

	/**
	 * @Title: printCompanySingle
	 * @Description: 单个打印单位用户的水费通知单
	 * @param accountItemId
	 * @param printerId
	 * @param period
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/printCompanySingle", produces = "application/json")
	@ResponseBody
	public Object printCompanySingle(Long meterId, Long printerId, String period, String noticeDate, String chargePeople, Integer paperSize)
			throws DocumentException, IOException, PrinterException, ParseException {
		BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型0，仅用于判断

		final String PRINTER_NAME = "Canon";
		//final String TEMPLATE_FILE_NAME = "companySecondTemplate"; // 模板名称.单位
		String TEMPLATE_FILE_NAME="";  //模板名称.
		if(paperSize == 1) {//选择的是A4
			TEMPLATE_FILE_NAME = "companyCombineTemplate";
		} else if (paperSize == 2){//选择的是A5
			TEMPLATE_FILE_NAME = "companySecondTemplate";
		}
		final String TEMPLATE_PREFIX = "templates/bottlelabel/labeltemplate/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在
		List<String> pdfFileList = new ArrayList<>();

		List<Map<String, Object>> recordMapList = new ArrayList<>();
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		Map<String, Object> customerMap = new HashMap<>();
		// 获取表计的默认客户
		CustomerMeter cm = customerMeterService.getCustomerByMeterId(meterId);
		Customers customer = customersService.selectByPrimaryKey(cm.getCustomerId());
		// 获取开票信息
		Map<String, Object> billMap = this.getCustomerBillName(cm.getCustomerId());
		String customerName = billMap.get("billName").toString();
		customerMap.put("customerName", customerName);
		customerMap.put("billNo", billMap.get("billNo"));

		List<MeterRecord> recordList = meterRecordService.getMeterRecordByMeter(period, meterId);

		Map<String, Object> recordMap = new HashMap<>();
		Meters meter = metersService.selectByPrimaryKey(meterId);
		String meterPlace = meter.getDescription();
		if (recordList.size() > 0) {
			MeterRecord record = recordList.get(0);
			

			recordMap.put("meterPlace", meterPlace);
			recordMap.put("preRead", record.getPreRead());
			recordMap.put("currRead", record.getCurrRead());
			recordMap.put("currAmount", record.getCurrAmount());
			recordMapList.add(recordMap);
		}
//		// 水费欠费金额
//		BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
//		// 查询往期欠费
//		BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
//		// 查询客户余额
//		BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customer.getId());
//		// 查询本月水费欠费金额（本期欠费+往期欠费）
//		BigDecimal waterFeeOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
//		// 应缴金额
//		BigDecimal payAmount = BigDecimalUtils.subtract(customerBalance, waterFeeOweAmount);
//		if (BigDecimalUtils.lessThan(payAmount, zero)) {
//			payAmount = BigDecimalUtils.subtract(zero, payAmount);
//		}
		
		List<PartitionWater> partList =  partitionWaterService.getPartitionWaterByMeterList(meter.getId(), period);
		Map<String, Object> partwaterMap = this.getNewList(partList);
		//获取水量、基础水价、污水处理费单价、基础水费、污水处理费字符串
		String basePriceStr = partwaterMap.get("basePriceStr").toString();
		String treatmentStr = partwaterMap.get("treatmentStr").toString();
		String sumBasePriceFee = partwaterMap.get("sumBasePriceFee").toString();
		String sumTreatmentFee = partwaterMap.get("sumTreatmentFee").toString();
		String totalBasePriceFee = partwaterMap.get("totalBasePriceFee").toString();//基础水费合计
		String totaltreatmentFee = partwaterMap.get("totaltreatmentFee").toString();//污水处理费合计
		String totalFee = partwaterMap.get("totalFee").toString();//合计
		String cardMeter = "CARD_METER";
		if(StringUtils.equals(meter.getMeterType(), cardMeter)) {
			basePriceStr = "";
			treatmentStr = "";
			sumBasePriceFee = "";
			sumTreatmentFee = "";
			totalBasePriceFee = "";
			totaltreatmentFee = "";
			totalFee = "";
		} 
		
		//获取水量、基础水价、污水处理费单价、基础水费、污水处理费字符串
		customerMap.put("waterAmountStr", partwaterMap.get("waterAmountStr"));//水量字符串
		customerMap.put("basePriceStr", partwaterMap.get("basePriceStr"));//基础水价字符串
		customerMap.put("treatmentStr", partwaterMap.get("treatmentStr"));//污水处理费单价字符串
		customerMap.put("sumBasePriceFee", partwaterMap.get("sumBasePriceFee"));//基础水费字符串
		customerMap.put("sumTreatmentFee", partwaterMap.get("sumTreatmentFee"));//污水处理费字符串
		customerMap.put("totalBasePriceFee", partwaterMap.get("totalBasePriceFee"));//基础水费合计
		customerMap.put("totaltreatmentFee", partwaterMap.get("totaltreatmentFee"));//污水处理费合计
		customerMap.put("totalFee", partwaterMap.get("totalFee"));//总合计
		customerMap.put("basePriceArrears", zero);
		customerMap.put("treatmentArrears", zero);
		customerMap.put("recordMapList", recordMapList);
		//获取通知单日期
		DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		if(StringUtils.isNotBlank(noticeDate)) {
			Date time=stringToDate.parse(noticeDate);
			String noticeDateStr = sDateFormat.format(time);
			customerMap.put("noticeDateStr", noticeDateStr);
		} else {
			Date sysdate = new Date();
			customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
		}
		//收费人名称
		if(chargePeople == null) {
			chargePeople ="";
		}
		customerMap.put("chargePeople", chargePeople);
		customerMapList.add(customerMap);

		String pdfFileName = createLabel.createSingleBottleLabelPDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
				customerMapList, customerName, period, meter.getPlace());
		pdfFileList.add(pdfFileName);

		// 查询打印小区编码
		sendPDFToPrinter(pdfFileList, printerId);
		return RequestResultUtil.getResultUpdateSuccess("打印通知单成功成功！");
	}

	/**
	 * @Title: printCompanyBatch
	 * @Description: 批量打印大表通知单
	 * @param searchCond
	 * @param traceIds
	 * @param printerId
	 * @param period
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/printCompanyBatch", produces = "application/json")
	@ResponseBody
	public Object printCompanyBatch(String searchCond, String traceIds, Long printerId, String period,
			String meterIdStr, String waterPrice, String noticeDate, String chargePeople, Integer paperSize) throws DocumentException, IOException, PrinterException, ParseException {
		BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型0，仅用于判断

		final String PRINTER_NAME = "Canon";
		//final String TEMPLATE_FILE_NAME = "companySecondTemplate"; // 模板名称.单位
		String TEMPLATE_FILE_NAME="";  //模板名称.
		if(paperSize == 1) {//选择的是A4
			TEMPLATE_FILE_NAME = "companyCombineTemplate";
		} else if (paperSize == 2){//选择的是A5
			TEMPLATE_FILE_NAME = "companySecondTemplate";
		}
		final String TEMPLATE_PREFIX = "templates/bottlelabel/labeltemplate/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在

		List<String> pdfFileList = new ArrayList<>();
		Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();
		List<Meters> metersList = new ArrayList<>();
		if (StringUtils.isBlank(meterIdStr)) {// 批量打印时根据查询条件打印
			metersList = metersService.getDefaultMeterList(traceIds, searchCond, customerType, waterPrice);
		} else {// 批量打印时勾选进行打印
			String[] s1 = meterIdStr.split(",");
			for (String meterId : s1) {
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(meterId));
				metersList.add(meter);
			}
		}

		for (Meters meter : metersList) {
			List<Map<String, Object>> customerMapList = new ArrayList<>();
			List<Map<String, Object>> recordMapList = new ArrayList<>();
			Map<String, Object> customerMap = new HashMap<>();
			// 获取表计的默认客户
			CustomerMeter cm = customerMeterService.getCustomerByMeterId(meter.getId());
			Customers customer = customersService.selectByPrimaryKey(cm.getCustomerId());
			// 获取开票信息
			Map<String, Object> billMap = this.getCustomerBillName(cm.getCustomerId());
			String customerName = billMap.get("billName").toString();
			customerMap.put("customerName", customerName);
			customerMap.put("billNo", billMap.get("billNo"));

			List<MeterRecord> recordList = meterRecordService.getMeterRecordByMeter(period, meter.getId());
			// 获取第一条抄表记录的时间
			SimpleDateFormat f = new SimpleDateFormat("MM月dd日");
			if (recordList.size() > 0) {
				MeterRecord record = recordList.get(0);
				Map<String, Object> recordMap = new HashMap<>();
				String meterPlace = meter.getDescription();
				recordMap.put("meterPlace", meterPlace);
				recordMap.put("preRead", record.getPreRead());
				recordMap.put("currRead", record.getCurrRead());
				//recordMap.put("currAmount", record.getCurrAmount());
				recordMapList.add(recordMap);
			}

			// 水费欠费金额
//			BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
//			// 查询往期欠费
//			BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
//			// 查询客户余额
//			BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customer.getId());
//			// 查询本月水费欠费金额（本期欠费+往期欠费）
//			BigDecimal waterFeeOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
//			// 应缴金额
//			BigDecimal payAmount = BigDecimalUtils.subtract(customerBalance, waterFeeOweAmount);
//			if (BigDecimalUtils.lessThan(payAmount, zero)) {
//				payAmount = BigDecimalUtils.subtract(zero, payAmount);
//			}
			
			List<PartitionWater> partList =  partitionWaterService.getPartitionWaterByMeterList(meter.getId(), period);
			Map<String, Object> partwaterMap = this.getNewList(partList);
			//获取水量、基础水价、污水处理费单价、基础水费、污水处理费字符串
			String basePriceStr = partwaterMap.get("basePriceStr").toString();
			String treatmentStr = partwaterMap.get("treatmentStr").toString();
			String sumBasePriceFee = partwaterMap.get("sumBasePriceFee").toString();
			String sumTreatmentFee = partwaterMap.get("sumTreatmentFee").toString();
			String totalBasePriceFee = partwaterMap.get("totalBasePriceFee").toString();//基础水费合计
			String totaltreatmentFee = partwaterMap.get("totaltreatmentFee").toString();//污水处理费合计
			String totalFee = partwaterMap.get("totalFee").toString();//合计
			String cardMeter = "CARD_METER";
			if(StringUtils.equals(meter.getMeterType(), cardMeter)) {
				basePriceStr = "";
				treatmentStr = "";
				sumBasePriceFee = "";
				sumTreatmentFee = "";
				totalBasePriceFee = "";
				totaltreatmentFee = "";
				totaltreatmentFee = "";
			} 
			
			//获取水量、基础水价、污水处理费单价、基础水费、污水处理费字符串
			customerMap.put("waterAmountStr", partwaterMap.get("waterAmountStr"));//水量字符串
			customerMap.put("basePriceStr", partwaterMap.get("basePriceStr"));//基础水价字符串
			customerMap.put("treatmentStr", partwaterMap.get("treatmentStr"));//污水处理费单价字符串
			customerMap.put("sumBasePriceFee", partwaterMap.get("sumBasePriceFee"));//基础水费字符串
			customerMap.put("sumTreatmentFee", partwaterMap.get("sumTreatmentFee"));//污水处理费字符串
			customerMap.put("totalBasePriceFee", partwaterMap.get("totalBasePriceFee"));//基础水费合计
			customerMap.put("totaltreatmentFee", partwaterMap.get("totaltreatmentFee"));//污水处理费合计
			customerMap.put("totalFee", partwaterMap.get("totalFee"));//总合计
			
			
			customerMap.put("recordMapList", recordMapList);
			//获取通知单日期
			DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
			if(StringUtils.isNotBlank(noticeDate)) {
				Date time=stringToDate.parse(noticeDate);
				String noticeDateStr = sDateFormat.format(time);
				customerMap.put("noticeDateStr", noticeDateStr);
			} else {
				Date sysdate = new Date();
				customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
			}
			//收费人名称
			if(chargePeople == null) {
				chargePeople ="";
			}
			customerMap.put("chargePeople", chargePeople);
			customerMapList.add(customerMap);

			String pdfFileName = createLabel.createSingleBottleLabelPDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
					customerMapList, customerName, period, meter.getPlace());
			pdfFileList.add(pdfFileName);

		}

		// 查询打印小区编码

		sendPDFToPrinter(pdfFileList, printerId);
		return RequestResultUtil.getResultUpdateSuccess("打印通知单成功成功！");
	}

	/**
	 * @Title: printCompanyCombine
	 * @Description: 将同一用户的多个水表合并打印
	 * @param searchCond
	 * @param traceIds
	 * @param printerId
	 * @param period
	 * @param meterIdStr
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/printCompanyCombine", produces = "application/json")
	@ResponseBody
	public Object printCompanyCombine(String searchCond, String traceIds, Long printerId, String period,
			String meterIdStr, String waterPrice, String noticeDate, String chargePeople, Integer paperSize) throws DocumentException, IOException, PrinterException, ParseException {
		BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型0，仅用于判断

		final String PRINTER_NAME = "Canon";
		//final String TEMPLATE_FILE_NAME = "companyCombineTemplate"; // 模板名称.单位
		String TEMPLATE_FILE_NAME="";  //模板名称.
		if(paperSize == 1) {//选择的A4纸
			TEMPLATE_FILE_NAME = "companyCombineTemplate";
		} else if (paperSize == 2){//选择的是A5纸
			TEMPLATE_FILE_NAME = "companySecondTemplate";
		}
		final String TEMPLATE_PREFIX = "templates/bottlelabel/labeltemplate/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在

		List<String> pdfFileList = new ArrayList<>();
		Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();
		List<Meters> metersList = new ArrayList<>();
		if (StringUtils.isBlank(meterIdStr)) {// 批量打印时根据查询条件打印
			metersList = metersService.getDefaultMeterList(traceIds, searchCond, customerType, waterPrice);
		} else {// 批量打印时勾选进行打印
			String[] s1 = meterIdStr.split(",");
			for (String meterId : s1) {
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(meterId));
				metersList.add(meter);
			}
		}

		Meters firstMeter = new Meters();
		if (metersList.size() > 0) {
			firstMeter = metersList.get(0);
		}

		List<Map<String, Object>> customerMapList = new ArrayList<>();

		Map<String, Object> customerMap = new HashMap<>();
		// 获取表计的默认客户
		CustomerMeter cm = customerMeterService.getCustomerByMeterId(firstMeter.getId());
		Customers customer = customersService.selectByPrimaryKey(cm.getCustomerId());
		// 获取开票信息
		Map<String, Object> billMap = this.getCustomerBillName(cm.getCustomerId());
		String pdfName = customer.getCustomerName();
		String customerName = billMap.get("billName").toString();
		customerMap.put("customerName", customerName);
		customerMap.put("billNo", billMap.get("billNo"));

		List<PartitionWater> newList = new ArrayList<>();
		List<Map<String, Object>> recordMapList = new ArrayList<>();
		for (Meters meter : metersList) {

			CustomerMeter temp = customerMeterService.getCustomerByMeterId(meter.getId());

			if (!temp.getCustomerId().equals(cm.getCustomerId())) {
				return RequestResultUtil.getResultUpdateSuccess("所选水表不是同一客户，不能合并！");
			}

			List<MeterRecord> recordList = meterRecordService.getMeterRecordByMeter(period, meter.getId());
			// 获取第一条抄表记录的时间
			SimpleDateFormat f = new SimpleDateFormat("MM月dd日");
			if (recordList.size() > 0) {
				MeterRecord record = recordList.get(0);
				
				Map<String, Object> recordMap = new HashMap<>();
				String meterPlace = meter.getDescription();
				recordMap.put("meterPlace", meterPlace);
				recordMap.put("preRead", record.getPreRead());
				recordMap.put("currRead", record.getCurrRead());
				recordMap.put("currAmount", record.getCurrAmount());
				recordMapList.add(recordMap);
			}

			// 获取基础水价和污水处理费信息
			List<PartitionWater> partList = partitionWaterService.getPartitionWaterByMeterList(meter.getId(), period);
			for (PartitionWater part : partList) {
				newList.add(part);
			}

		}
		customerMap.put("recordMapList", recordMapList);
		int size = recordMapList.size();
		customerMap.put("size", size);
		Map<String, Object> partwaterMap = this.getNewList(newList);
		customerMap.put("waterAmountStr", partwaterMap.get("waterAmountStr"));//水量字符串
		customerMap.put("basePriceStr", partwaterMap.get("basePriceStr"));//基础水价字符串
		customerMap.put("treatmentStr", partwaterMap.get("treatmentStr"));//污水处理费单价字符串
		customerMap.put("sumBasePriceFee", partwaterMap.get("sumBasePriceFee"));//基础水费字符串
		customerMap.put("sumTreatmentFee", partwaterMap.get("sumTreatmentFee"));//污水处理费字符串
		customerMap.put("totalBasePriceFee", partwaterMap.get("totalBasePriceFee"));//基础水费合计
		customerMap.put("totaltreatmentFee", partwaterMap.get("totaltreatmentFee"));//污水处理费合计

		// 本期欠费
		//customerMap.put("oweAmount", partwaterMap.get("totalFee"));
		// 往期欠费
		customerMap.put("pastOweAmount", zero);
		// 余额代扣
		customerMap.put("customerBalance", zero);
		// 应缴水费
		customerMap.put("totalFee", partwaterMap.get("totalFee"));

		customerMap.put("recordMapList", recordMapList);
		//获取通知单日期
		DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月");
		if(StringUtils.isNotBlank(noticeDate)) {
			Date time=stringToDate.parse(noticeDate);
			String noticeDateStr = sDateFormat.format(time);
			customerMap.put("noticeDateStr", noticeDateStr);
		} else {
			Date sysdate = new Date();
			customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
		}
		if(chargePeople == null) {
			chargePeople ="";
		}
		//收费人名称
		customerMap.put("chargePeople", chargePeople);
		customerMapList.add(customerMap);

		String pdfFileName = createLabel.createSingleBottleLabelPDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
				customerMapList, pdfName, period, customerName);
		pdfFileList.add(pdfFileName);

		// 查询打印小区编码

		sendPDFToPrinter(pdfFileList, printerId);
		return RequestResultUtil.getResultUpdateSuccess("打印通知单成功！");
	}

	// **********************************************按客户打印选项卡****************************************

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer-main")
	public String customerMain(Model model) {
		// 查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);

		return TEMPLATE_CUSTOMER_PATH + "customer_printer_main";
	}

	@RequestMapping(value = "/customer-table")
	public String customerTable(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond,
			String period) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();
		List<Customers> customersList = customersService.getDefaultUnitCustomerList(traceIds, searchCond, customerType);
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		PageInfo<Customers> pageInfo = new PageInfo<>(customersList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Customers customer : customersList) {
			String traceId = locationCustomerService.getTraceIds(customer.getId());
			String customerPlace = locationService.getPlace(traceId);
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("period", period);

			// 查询客户的所有账单
			List<CustomerAccountItem> accountItemList = customerAccountItemService
					.getAllCustomerAccountItemByPeriod(customer.getId(), period);
			BigDecimal oweAmount = new BigDecimal("0");
			BigDecimal overdueOweAmount = new BigDecimal("0");
			for (CustomerAccountItem account : accountItemList) {
				// 计算账单总金额
				BigDecimal oweAmountTemp = BigDecimalUtils.subtract(account.getCreditAmount(),
						account.getDebitAmount());
				// 计算违约金欠费总金额
				BigDecimal overdueOweAmountTemp = customerAccountItemService
						.getOverdueBillOweAmountSum(account.getId());
				oweAmount = BigDecimalUtils.add(oweAmount, oweAmountTemp);
				overdueOweAmount = BigDecimalUtils.add(overdueOweAmount, overdueOweAmountTemp);
			}

			// 查询往期欠费金额（水费+违约金+分账单）
			List<CustomerAccountItem> pastAccountItemList = customerAccountItemService.getPastOweAmount(period,
					customer.getId());
			BigDecimal pastOweAmount = new BigDecimal("0");
			// 创建一个数组存储欠费的期间
			List<String> list = new ArrayList<>();
			for (CustomerAccountItem item : pastAccountItemList) {
				// 望去往期账单的欠费金额
				BigDecimal pastOweAmountTemp = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
				pastOweAmount = BigDecimalUtils.add(pastOweAmount, pastOweAmountTemp);
				String pastPeriod = item.getPeriod();
				if (!list.contains(pastPeriod)) {// 判断数组中是否包含重复元素
					list.add(pastPeriod);
				}
			}
			if (BigDecimalUtils.greaterThan(oweAmount, new BigDecimal("0"))) {
				list.add(period);
			}
			pastOweAmount = BigDecimalUtils.add(overdueOweAmount, pastOweAmount);
			customerMap.put("oweAmount", oweAmount);
			customerMap.put("overdueOweAmount", overdueOweAmount);
			customerMap.put("pastOweAmount", pastOweAmount);
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			totalOweAmount = BigDecimalUtils.add(totalOweAmount, pastOweAmount);
			customerMap.put("customerPlace", customerPlace);
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

		return TEMPLATE_CUSTOMER_PATH + "customer_printer_table";
	}

	/**
	 * @Title: previewCustomerPrepare
	 * @Description: 预览通知单-客户选项卡
	 * @param model
	 * @param customerId
	 * @param period
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/previewCustomerPrepare")
	@ResponseBody
	public Object previewCustomerPrepare(Model model, Long customerId, String period, String traceIds, String noticeDate, String chargePeople) throws Exception {

		BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型0，仅用于判断

		final String PRINTER_NAME = "Canon";
		final String TEMPLATE_FILE_NAME = "companySecondTemplate"; // 模板名称.单位

		final String TEMPLATE_PREFIX = "templates/bottlelabel/labeltemplate/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在

		List<Map<String, Object>> recordMapList = new ArrayList<>();
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		Map<String, Object> customerMap = new HashMap<>();
		Customers customer = customersService.selectByPrimaryKey(customerId);
		// 获取开票信息
		Map<String, Object> billMap = this.getCustomerBillName(customerId);
		String pdfName = customer.getCustomerName();
		String customerName = billMap.get("billName").toString();
		customerMap.put("customerName", customerName);
		customerMap.put("billNo", billMap.get("billNo"));
		
		WaterNotify notify = new WaterNotify();
		notify.setCustomerId(customerId);
		notify.setCreateDate(new Date());
		notify.setPeriod(period);
		notify.setMeterReader("超级管理员");
		notify.setUseLocation(EnumNotifyUseLocation.LOCATION_YES.getValue());
		notify.setStatus(EnumWaterNotifyStatus.NORMAL.getValue());
		List<MeterRecord> meterRecordList = meterRecordService.getListByCustomerIdAndPeriod(customerId, period, traceIds);
		List<Long> meterIdList = new ArrayList<>();
		List<PartitionWater> newList = new ArrayList<>();
		List<Long> partIdList = new ArrayList<>();
		List<Map<String, Object>> tempMapList = new ArrayList<>();
		if (meterRecordList.size() > 0) {

			for (MeterRecord record : meterRecordList) {
				Map<String, Object> recordMap = new HashMap<>();
				Meters meter = metersService.selectByPrimaryKey(record.getMeterId());
				meterIdList.add(meter.getId());
				String meterPlace = meter.getDescription();
				if(StringUtils.equals("CARD_METER", meter.getMeterType())) {//如果水表类型是卡表
					continue;
				}
				Map<String, Object> temp = new HashMap<>();
				temp.put("meterId", record.getMeterId());
				temp.put("description", meterPlace);
				temp.put("preRead", record.getPreRead());
				temp.put("currRead", record.getCurrRead());
				temp.put("currAmount", record.getCurrAmount());
				tempMapList.add(temp);
				recordMap.put("meterPlace", meterPlace);
				recordMap.put("preRead", record.getPreRead());
				recordMap.put("currRead", record.getCurrRead());
				recordMap.put("currAmount", record.getCurrAmount());
				recordMapList.add(recordMap);
			}
		}
		notify.setMeterIds(meterIdList.toString());
		notify.setMeterRecord(tempMapList.toString());
		// 获取基础水价和污水处理费信息
		List<PartitionWater> partList = partitionWaterService.getPartWaterMessage(period, customerId, traceIds);
		for (PartitionWater part : partList) {
			partIdList.add(part.getId());
			newList.add(part);
		}

		int size = recordMapList.size();
		customerMap.put("size", size);
		Map<String, Object> partwaterMap = this.getNewList(newList);
		customerMap.put("waterAmountStr", partwaterMap.get("waterAmountStr"));//水量字符串
		customerMap.put("basePriceStr", partwaterMap.get("basePriceStr"));//基础水价字符串
		customerMap.put("treatmentStr", partwaterMap.get("treatmentStr"));//污水处理费单价字符串
		customerMap.put("sumBasePriceFee", partwaterMap.get("sumBasePriceFee"));//基础水费字符串
		customerMap.put("sumTreatmentFee", partwaterMap.get("sumTreatmentFee"));//污水处理费字符串
		customerMap.put("totalBasePriceFee", partwaterMap.get("totalBasePriceFee"));//基础水费合计
		customerMap.put("totaltreatmentFee", partwaterMap.get("totaltreatmentFee"));//污水处理费合计
		customerMap.put("totalFee", partwaterMap.get("totalFee"));//总合计
		
		notify.setSumBaseAmount(new BigDecimal(partwaterMap.get("totalBasePriceFee").toString()));
		notify.setSumSewageAmount(new BigDecimal(partwaterMap.get("totaltreatmentFee").toString()));
		//waterNotifyService.insertSelective(notify);
		WaterNotifyDetail detail = new WaterNotifyDetail();
		detail.setWaterNotifyId(notify.getId());
		detail.setPartWaterId(partIdList.toString());
		//waterNotifyDetailService.insertSelective(detail);
//		// 水费欠费金额
//		BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
//		// 查询往期欠费
//		BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
//		// 查询客户余额
//		BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customer.getId());
//		// 查询本月水费欠费金额（本期欠费+往期欠费）
//		BigDecimal waterFeeOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
//		// 应缴金额
//		BigDecimal payAmount = BigDecimalUtils.subtract(customerBalance, waterFeeOweAmount);
//		if (BigDecimalUtils.lessThan(payAmount, zero)) {
//			payAmount = BigDecimalUtils.subtract(zero, payAmount);
//		}
//		// 本期欠费
//		customerMap.put("oweAmount", oweAmount);
//		// 往期欠费
//		customerMap.put("pastOweAmount", pastOweAmount);
//		// 余额代扣
//		customerMap.put("customerBalance", customerBalance);
//		// 应缴水费
//		customerMap.put("payAmount", payAmount);
		customerMap.put("basePriceArrears", zero);
		customerMap.put("treatmentArrears", zero);
		customerMap.put("recordMapList", recordMapList);
		//获取通知单日期
		DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月");
		if(StringUtils.isNotBlank(noticeDate)) {
			Date time=stringToDate.parse(noticeDate);
			String noticeDateStr = sDateFormat.format(time);
			customerMap.put("noticeDateStr", noticeDateStr);
		} else {
			Date sysdate = new Date();
			customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
		}
		if(chargePeople == null) {
			chargePeople ="";
		}
		//收费人名称
		customerMap.put("chargePeople", chargePeople);
		customerMapList.add(customerMap);

		String pdfFileName = createLabel.createCustomerNoticePDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
				customerMapList, pdfName, period);
		return RequestResultUtil.getResultUpdateSuccess("生成PDF成功！");
	}

	/**
	 * @Title: getCustomerBillName
	 * @Description: 获取客户的开票信息
	 * @param customerId
	 * @return
	 */
	public Map<String, Object> getCustomerBillName(Long customerId) {
		Map<String, Object> billMap = new HashMap<>();
		Customers customer = customersService.selectByPrimaryKey(customerId);
		String billName = "";
		String billNo = "";
		Example example = new Example(CustomerBillInfo.class);
		example.createCriteria().andEqualTo("enabled", EnumEnabledStatus.ENABLED_NO.getValue()).andEqualTo("customerId",
				customerId);
		List<CustomerBillInfo> billList = billService.selectByExample(example);
		if (billList.size() <= 0 || billList.size() > 1) {
			billName = customer.getCustomerName();
		} else {
			billName = billList.get(0).getCustomerName();
			if(!StringUtils.equals(billName, customer.getCustomerName())) {
				billName = billName+"("+customer.getCustomerName()+")";
			}
			billNo = billList.get(0).getBillNo();
		}
		billMap.put("billName", billName);// 开票名称
		billMap.put("billNo", billNo);// 客户的开票编号
		return billMap;
	}

	/**
	 * @Title: getNewList
	 * @Description: 获取分水量信息，合并基础水费和污水处理费相同的数据
	 * @param partWaterList
	 * @return
	 */
	public Map<String, Object> getNewList(List<PartitionWater> partWaterList) {
		List<PartitionWater> resultPwList = new ArrayList<>();
		for (int i = 0; i < partWaterList.size(); i++) {
			PartitionWater pw1 = partWaterList.get(i);
			BigDecimal realWaterAmount = pw1.getRealWaterAmount();
			List<PartitionWater> pwList = new ArrayList<>();
			pwList.addAll(partWaterList);
			int removeCount = 0;
			for (int j = 0; j < pwList.size(); j++) {
				PartitionWater pw2 = pwList.get(j);
				if (!pw1.getId().equals(pw2.getId()) && BigDecimalUtils.equals(pw1.getBasePrice(), pw2.getBasePrice())
						&& BigDecimalUtils.equals(pw1.getTreatmentFee(), pw2.getTreatmentFee())) {
					realWaterAmount = BigDecimalUtils.add(realWaterAmount, pw2.getRealWaterAmount());
					partWaterList.remove((j - removeCount));
					removeCount = removeCount + 1;
				}

			}
			pw1.setRealWaterAmount(realWaterAmount);
			resultPwList.add(pw1);
		}

		Map<String, Object> recordMap = new HashMap<>();
		String realWaterAmountStr = "";
		String basePriceStr = "";
		String treatmentStr = "";
		String sumBasePriceFee  = "";
		String sumTreatmentFee  = "";
		BigDecimal totalBasePriceFee = new BigDecimal("0");//基础水费合计
		BigDecimal totaltreatmentFee = new BigDecimal("0");//污水处理费合计
		for (PartitionWater water : resultPwList) {
			Meters meter = metersService.selectByPrimaryKey(Long.valueOf(water.getMeterId()));
			if(!StringUtils.equals("CARD_METER", meter.getMeterType())) {//如果水表类型不是卡表，则计算水费和污水处理费
				BigDecimal basePriceFee = BigDecimalUtils.multiply(water.getRealWaterAmount(), water.getBasePrice());
				BigDecimal treatment = BigDecimalUtils.multiply(water.getRealWaterAmount(), water.getTreatmentFee());
				sumBasePriceFee = basePriceFee + "/" + sumBasePriceFee;
				sumTreatmentFee = treatment + "/" + sumTreatmentFee;
				basePriceStr = water.getBasePrice() + "/" + basePriceStr;
				treatmentStr = water.getTreatmentFee() + "/" + treatmentStr;
				
				totalBasePriceFee = BigDecimalUtils.add(basePriceFee, totalBasePriceFee);
				totaltreatmentFee = BigDecimalUtils.add(treatment, totaltreatmentFee);
				realWaterAmountStr = water.getRealWaterAmount() + "/" + realWaterAmountStr;
			} 
			
			
		}
		realWaterAmountStr = this.getStr("/", realWaterAmountStr);
		basePriceStr = this.getStr("/", basePriceStr);
		treatmentStr = this.getStr("/", treatmentStr);
		sumBasePriceFee = this.getStr("/", sumBasePriceFee);
		sumTreatmentFee = this.getStr("/", sumTreatmentFee);
		recordMap.put("waterAmountStr", realWaterAmountStr);// 水量
		
		recordMap.put("basePriceStr", basePriceStr);// 基础水费价格字符串
		recordMap.put("sumBasePriceFee", sumBasePriceFee);// 基础水费字符串
		recordMap.put("totalBasePriceFee", totalBasePriceFee);// 基础水费合计
		
		recordMap.put("treatmentStr", treatmentStr);// 污水处理费价格字符串
		recordMap.put("sumTreatmentFee", sumTreatmentFee);// 污水处理费字符串
		recordMap.put("totaltreatmentFee", totaltreatmentFee);// 污水处理费合计
		recordMap.put("totalFee", BigDecimalUtils.add(totalBasePriceFee, totaltreatmentFee));// 污水处理费合计
		return recordMap;
	}
	
	/**
	 * @Title: isCardMeter
	 * @Description: 判断客户是否为一户一表的卡表用户
	 * @param customerId
	 * @return 
	 */
	public boolean isCardMeter(Long customerId) {
		Example example = new Example(CustomerMeter.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO);
		List<CustomerMeter> cmList = customerMeterService.getListByCustomerId(customerId);
		if(cmList.size() == 1) {
			CustomerMeter cm = cmList.get(0);
			Meters meter = metersService.selectByPrimaryKey(cm.getMeterId());
			if(StringUtils.equals("CARD_METER", meter.getMeterType())) {
				return true;
			}
			return false;
		} 
		return false;
	}

	/**
	 * @Title: getStr
	 * @Description: 去除字符串最后一位符号
	 * @param separtor
	 * @param str
	 * @return
	 */
	public String getStr(String separtor, String str) {
		if (str.endsWith(separtor)) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	/**
	 * @Title loadPrinterDialog
	 * @Description 动态加载打印机对话框
	 * @param model
	 * @return 打印机选择对话框视图
	 * @Return String 返回类型
	 *
	 * @Date 2019年1月1日 下午9:54:38
	 */
	@RequestMapping(value = "/loadPrinterCustomerDialog")
	public String loadPrinterCustomerDialog(Model model, Long customerId) {
		final String VIEW_NAME = "customer_printer_dialog"; // 视图名称
		// 查询打印机配置. 并加入到视图中.
		List<PrinterConfig> printerConfigList = printConfigService.selectAll();
		model.addAttribute("printerConfigList", printerConfigList);
		model.addAttribute("customerId", customerId);
		return TEMPLATE_CUSTOMER_PATH + VIEW_NAME;
	}

	/**
	 * @Title: printCustomerCompanySingle
	 * @Description:单个打印
	 * @param customerId
	 * @param printerId
	 * @param period
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/printCustomerCompanySingle", produces = "application/json")
	@ResponseBody
	public Object printCustomerCompanySingle(Long customerId, Long printerId, String period, String traceIds, String noticeDate, String chargePeople)
			throws DocumentException, IOException, PrinterException, ParseException {
		final String PRINTER_NAME = "Canon";
		final String TEMPLATE_FILE_NAME = "companySecondTemplate"; // 模板名称.单位

		final String TEMPLATE_PREFIX = "templates/bottlelabel/labeltemplate/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在

		List<String> pdfFileList = new ArrayList<>();
		BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型0，仅用于判断

		List<Map<String, Object>> recordMapList = new ArrayList<>();
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		Map<String, Object> customerMap = new HashMap<>();
		Customers customer = customersService.selectByPrimaryKey(customerId);
		// 获取开票信息
		Map<String, Object> billMap = this.getCustomerBillName(customerId);
		String pdfName = customer.getCustomerName();
		String customerName = billMap.get("billName").toString();
		customerMap.put("customerName", customerName);
		customerMap.put("billNo", billMap.get("billNo"));

		// 本期抄表日期和上期抄表日期
		List<MeterRecord> meterRecordList = meterRecordService.getListByCustomerIdAndPeriod(customerId, period, traceIds);
		List<PartitionWater> newList = new ArrayList<>();
		if (meterRecordList.size() > 0) {

			for (MeterRecord record : meterRecordList) {
				Map<String, Object> recordMap = new HashMap<>();
				Meters meter = metersService.selectByPrimaryKey(record.getMeterId());
				String meterPlace = meter.getDescription();
				if(StringUtils.equals("CARD_METER", meter.getMeterType())) {//如果水表类型是卡表
					continue;
				}
				recordMap.put("meterPlace", meterPlace);
				recordMap.put("preRead", record.getPreRead());
				recordMap.put("currRead", record.getCurrRead());
				recordMap.put("currAmount", record.getCurrAmount());

				recordMapList.add(recordMap);
			}
		}
		// 获取基础水价和污水处理费信息
		List<PartitionWater> partList = partitionWaterService.getPartWaterMessage(period, customerId, traceIds);
		for (PartitionWater part : partList) {
			newList.add(part);
		}
		Map<String, Object> partwaterMap = this.getNewList(newList);
		customerMap.put("waterAmountStr", partwaterMap.get("waterAmountStr"));//水量字符串
		customerMap.put("basePriceStr", partwaterMap.get("basePriceStr"));//基础水价字符串
		customerMap.put("treatmentStr", partwaterMap.get("treatmentStr"));//污水处理费单价字符串
		customerMap.put("sumBasePriceFee", partwaterMap.get("sumBasePriceFee"));//基础水费字符串
		customerMap.put("sumTreatmentFee", partwaterMap.get("sumTreatmentFee"));//污水处理费字符串
		customerMap.put("totalBasePriceFee", partwaterMap.get("totalBasePriceFee"));//基础水费合计
		customerMap.put("totaltreatmentFee", partwaterMap.get("totaltreatmentFee"));//污水处理费合计
		customerMap.put("totalFee", partwaterMap.get("totalFee"));//总合计
//		// 水费欠费金额
//		BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
//		// 查询往期欠费
//		BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
//		// 查询客户余额
//		BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customer.getId());
//		// 查询本月水费欠费金额（本期欠费+往期欠费）
//		BigDecimal waterFeeOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
//		// 应缴金额
//		BigDecimal payAmount = BigDecimalUtils.subtract(customerBalance, waterFeeOweAmount);
//		if (BigDecimalUtils.lessThan(payAmount, zero)) {
//			payAmount = BigDecimalUtils.subtract(zero, payAmount);
//		}
//		// 本期欠费
//		customerMap.put("oweAmount", oweAmount);
//		// 往期欠费
//		customerMap.put("pastOweAmount", pastOweAmount);
//		// 余额代扣
//		customerMap.put("customerBalance", customerBalance);
//		// 应缴水费
//		customerMap.put("payAmount", payAmount);
		customerMap.put("basePriceArrears", zero);
		customerMap.put("treatmentArrears", zero);
		customerMap.put("recordMapList", recordMapList);
		//获取通知单日期
		DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月");
		if(StringUtils.isNotBlank(noticeDate)) {
			Date time=stringToDate.parse(noticeDate);
			String noticeDateStr = sDateFormat.format(time);
			customerMap.put("noticeDateStr", noticeDateStr);
		} else {
			Date sysdate = new Date();
			customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
		}
		//收费人名称
		if(chargePeople == null) {
			chargePeople ="";
		}
		customerMap.put("chargePeople", chargePeople);
		customerMapList.add(customerMap);
		
		
		String pdfFileName = createLabel.createCustomerNoticePDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
				customerMapList, pdfName, period);
		pdfFileList.add(pdfFileName);

		// 查询打印小区编码
		sendCustomerPDFToPrinter(pdfFileList, printerId);
		return RequestResultUtil.getResultUpdateSuccess("打印通知单成功成功！");
	}

	/**
	 * @Title: printCompanyCustomerBatch
	 * @Description: 批量打印水费通知单
	 * @param searchCond
	 * @param traceIds
	 * @param printerId
	 * @param period
	 * @param customerIdStr
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws PrinterException
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/printCompanyCustomerBatch", produces = "application/json")
	@ResponseBody
	public Object printCompanyCustomerBatch(String searchCond, String traceIds, Long printerId, String period,
			String customerIdStr, String noticeDate, String chargePeople) throws DocumentException, IOException, PrinterException, ParseException {
		BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型0，仅用于判断

		final String PRINTER_NAME = "Canon";
		final String TEMPLATE_FILE_NAME = "companySecondTemplate"; // 模板名称.单位
		final String TEMPLATE_PREFIX = "templates/bottlelabel/labeltemplate/"; // 模板文件所在的目录
		String FILE_DIR = PdfPathUtil.getFTPPath(uploadFileConfig.getUploadFolder());// TODO //PDF所在

		List<String> pdfFileList = new ArrayList<>();
		Integer customerType = EnumCustomerType.CUSTOMER_UNIT.getValue();

		List<Customers> customersList = new ArrayList<>();
		if (StringUtils.isBlank(customerIdStr)) {// 批量打印时根据查询条件打印
			customersList = customersService.getDefaultUnitCustomerList(traceIds, searchCond, customerType);
		} else {// 批量打印时勾选进行打印
			String[] s1 = customerIdStr.split(",");
			for (String customerId : s1) {
				Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
				customersList.add(customer);
			}
		}

		for (Customers customer : customersList) {
			boolean isCardMeter = this.isCardMeter(customer.getId());
			if(isCardMeter) {//如果该客户是一户一表的卡表用户，则不打印通知单
				continue;
			}
			List<Map<String, Object>> customerMapList = new ArrayList<>();
			List<Map<String, Object>> recordMapList = new ArrayList<>();
			Map<String, Object> customerMap = new HashMap<>();
			// 获取开票信息
			Map<String, Object> billMap = this.getCustomerBillName(customer.getId());
			String pdfName = customer.getCustomerName();
			String customerName = billMap.get("billName").toString();
			customerMap.put("customerName", customerName);
			customerMap.put("billNo", billMap.get("billNo"));

			List<MeterRecord> meterRecordList = meterRecordService.getListByCustomerIdAndPeriod(customer.getId(), period, traceIds);
			List<PartitionWater> newList = new ArrayList<>();
			if (meterRecordList.size() > 0) {

				for (MeterRecord record : meterRecordList) {
					Map<String, Object> recordMap = new HashMap<>();
					Meters meter = metersService.selectByPrimaryKey(record.getMeterId());
					String meterPlace = meter.getDescription();
					if(StringUtils.equals("CARD_METER", meter.getMeterType())) {//如果水表类型是卡表
						continue;
					}
					recordMap.put("meterPlace", meterPlace);
					recordMap.put("preRead", record.getPreRead());
					recordMap.put("currRead", record.getCurrRead());
					recordMap.put("currAmount", record.getCurrAmount());

					recordMapList.add(recordMap);
				}
			}
			// 获取基础水价和污水处理费信息
			List<PartitionWater> partList = partitionWaterService.getPartWaterMessage(period, customer.getId(), traceIds);
			for (PartitionWater part : partList) {
				newList.add(part);
			}
			Map<String, Object> partwaterMap = this.getNewList(newList);
			customerMap.put("waterAmountStr", partwaterMap.get("waterAmountStr"));//水量字符串
			customerMap.put("basePriceStr", partwaterMap.get("basePriceStr"));//基础水价字符串
			customerMap.put("treatmentStr", partwaterMap.get("treatmentStr"));//污水处理费单价字符串
			customerMap.put("sumBasePriceFee", partwaterMap.get("sumBasePriceFee"));//基础水费字符串
			customerMap.put("sumTreatmentFee", partwaterMap.get("sumTreatmentFee"));//污水处理费字符串
			customerMap.put("totalBasePriceFee", partwaterMap.get("totalBasePriceFee"));//基础水费合计
			customerMap.put("totaltreatmentFee", partwaterMap.get("totaltreatmentFee"));//污水处理费合计
			customerMap.put("totalFee", partwaterMap.get("totalFee"));//总合计
//			// 水费欠费金额
//			BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customer.getId(), period);
//			// 查询往期欠费
//			BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customer.getId(), period);
//			// 查询客户余额
//			BigDecimal customerBalance = customerAccountItemService.getCustomerBalance(customer.getId());
//			// 查询本月水费欠费金额（本期欠费+往期欠费）
//			BigDecimal waterFeeOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
//			// 应缴金额
//			BigDecimal payAmount = BigDecimalUtils.subtract(customerBalance, waterFeeOweAmount);
//			if (BigDecimalUtils.lessThan(payAmount, zero)) {
//				payAmount = BigDecimalUtils.subtract(zero, payAmount);
//			}
//			// 本期欠费
//			customerMap.put("oweAmount", oweAmount);
//			// 往期欠费
//			customerMap.put("pastOweAmount", pastOweAmount);
//			// 余额代扣
//			customerMap.put("customerBalance", customerBalance);
//			// 应缴水费
//			customerMap.put("payAmount", payAmount);
			customerMap.put("basePriceArrears", zero);
			customerMap.put("treatmentArrears", zero);
			customerMap.put("recordMapList", recordMapList);
			//获取通知单日期
			DateFormat stringToDate= new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月");
			if(StringUtils.isNotBlank(noticeDate)) {
				Date time=stringToDate.parse(noticeDate);
				String noticeDateStr = sDateFormat.format(time);
				customerMap.put("noticeDateStr", noticeDateStr);
			} else {
				Date sysdate = new Date();
				customerMap.put("noticeDateStr", sDateFormat.format(sysdate));
			}
			//收费人名称
			if(chargePeople == null) {
				chargePeople ="";
			}
			customerMap.put("chargePeople", chargePeople);
			customerMapList.add(customerMap);

			String pdfFileName = createLabel.createCustomerNoticePDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
					customerMapList, pdfName, period);
			pdfFileList.add(pdfFileName);
		}

		// 查询打印小区编码

		sendCustomerPDFToPrinter(pdfFileList, printerId);
		return RequestResultUtil.getResultUpdateSuccess("打印通知单成功成功！");
	}

	private void sendCustomerPDFToPrinter(List<String> pdfFileList, Long printerId)
			throws IOException, PrinterException {
		PrinterConfig printerConfig = printConfigService.selectByPrimaryKey(printerId);
		// 打印PDF文件
		for (int i = 0; i < pdfFileList.size(); i++) {
			System.out.println(pdfFileList.get(i));

			printFile.printCustomerPDF(pdfFileList.get(i), printerConfig.getPrinterName());
		}
	}

	/**
	 * @return
	 */
	private String getFTPPath() {

		String ymd = DateUtil.getYYYYMMDDDate(new Date());// 返回YYYYMMDD格式的日期

		String path = uploadFileConfig.getUploadFolder();
		path = path + File.separator + "pdf" + File.separator + ymd + File.separator;
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