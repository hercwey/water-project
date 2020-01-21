package com.learnbind.ai.controller.bill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.AssistantBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.CMBCAutoDeductReceiptBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiTraceOperate;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DiscountWaterFeeTrace;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.addsubwater.AddSubWaterService;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.DiscountWaterFeeTraceService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.CustomerOverdueFineService;
import com.learnbind.ai.service.meterrecord.DiscountFineTraceService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.user.UsersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.sms.SMSConstants;
import com.learnbind.ai.sms.SMSService;
import com.learnbind.ai.sms.SendSingleSMResponse;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: BillController.java
 * @Description: 账单管理前端控制器
 *
 * @author Administrator
 * @date 2019年8月20日 下午6:11:35
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/account-item")
public class BillController {
	private static Log log = LogFactory.getLog(BillController.class);
	private static final String TEMPLATE_PATH = "bill/"; // 页面目录
	private static final String TEMPLATE_PATH_BILL = "bill/insert_bill/"; // 增加账单
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService; // 客户信息
	@Autowired
	private CustomerAccountService customerAccountService;// 客户-账户服务
	@Autowired
	private CustomerOverdueFineService customerOverdueFineService;// 客户-违约金
	@Autowired
	private DiscountFineTraceService discountFineTraceService;// 客户-违约金减免日志
	@Autowired
	private DiscountWaterFeeTraceService discountWaterFeeTraceService;// 客户-水费减免日志
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户
	@Autowired
	private BankService bankService;//客户银行信息
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计绑定关系表
	@Autowired
	private SMSService smsService;//发送短信
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量
	@Autowired
	private WaterPriceService waterPriceService;//用水性质
	@Autowired
	private MetersService metersService;//用水性质
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录信息
	@Autowired
	private UsersService usersService;//用户信息
	@Autowired
	private AddSubWaterService addSubWaterService;//追加减免水量

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		

		return TEMPLATE_PATH + "account_starter";
	}

	/**
	 * @Title: main
	 * @Description: 加载客户账单信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "account_main";
	}

	/**
	 * @Title: table
	 * @Description: 客户账单信息table
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param period
	 * @param locationCode
	 * @param settlementStatus
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table-old")
	public String tableOld(Model model, Integer pageNum, Integer pageSize, String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, String startDate, String endDate) {
		

		BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
		
		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		String billTotalPrice = "";//账单总金额
		String rechargeTotalPrice = "";//已充值总金额
		//BigDecimal overdueTotalPrice = new BigDecimal(0.00);//违约金总金额
		String oweTotalPrice = "";//欠费总金额
		//获取统计数据
		Map<String, Object> statisticMap = customerAccountItemService.getCustomerAccountItemStatistic(period, traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		if(statisticMap != null) {
			billTotalPrice = statisticMap.get("TOTAL_CREDIT_AMOUNT").toString();
			rechargeTotalPrice = statisticMap.get("TOTAL_DEBIT_AMOUNT").toString();
			oweTotalPrice = statisticMap.get("TOTAL_OWE_AMOUNT").toString();
		}
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String accountItemIdStr = accountItemMap.get("ID").toString();//账单ID
			Long accountItemId = Long.valueOf(accountItemIdStr);//账单ID
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
			//String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
			//String currTraceIds = accountItemMap.get("TRACE_IDS").toString();//地理位置traceIds
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			PartitionWater part = partitionWaterService.getPartitionWater(accountItemId);
			log.debug("----------分水量记录ID："+part.getId()+"，表计ID："+part.getMeterId());
			Meters meter = metersService.selectByPrimaryKey(Long.valueOf(part.getMeterId()));
			//获取客户类型
			Integer customerType = Integer.valueOf(accountItemMap.get("CUSTOMER_TYPE").toString());
			String customerName = "";
			if(customerType == EnumCustomerType.CUSTOMER_UNIT.getValue()) {//如果客户类型是单位用户
				customerName = meter.getPlace();
			} else {
				customerName = accountItemMap.get("CUSTOMER_NAME").toString();
			}
			//计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationCustomerService.getPlace(customerId);
			accountItemMap.put("place", place);//地理位置
			accountItemMap.put("meterPlace", customerName);//地理位置
			
			accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			accountItemMap.put("overdueValue", overdueValue.setScale(2));//违约金总金额
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，欠费金额+违约金欠款
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount.setScale(2));
			//获取贷方摘要
			String creditSubject = "";
			if(accountItemMap.get("CREDIT_SUBJECT") != null) {
				creditSubject = AiSubjectUtils.getAiSubjectStr(accountItemMap.get("CREDIT_SUBJECT").toString());

			}
			accountItemMap.put("creditSubject", creditSubject);
			
//			String billPrice = accountItemMap.get("CREDIT_AMOUNT").toString();//账单金额
//			String rechargePrice = accountItemMap.get("DEBIT_AMOUNT").toString();//已充值金额
//			billTotalPrice = BigDecimalUtils.add(billTotalPrice, new BigDecimal(billPrice));//统计账单总金额，用于列表统计
//			rechargeTotalPrice = BigDecimalUtils.add(rechargeTotalPrice, new BigDecimal(rechargePrice));//统计充值总金额，用于列表统计
//			//overdueTotalPrice = BigDecimalUtils.add(overdueTotalPrice, overdueValue.setScale(2));//统计违约金总金额，用于列表统计
//			oweTotalPrice = BigDecimalUtils.add(oweTotalPrice, totalOweAmount);//统计欠费总金额，用于列表统计
		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		//回传统计金额
		model.addAttribute("billTotalPrice", billTotalPrice);
		model.addAttribute("rechargeTotalPrice", rechargeTotalPrice);
		//model.addAttribute("overdueTotalPrice", overdueTotalPrice);
		model.addAttribute("oweTotalPrice", oweTotalPrice);

		return TEMPLATE_PATH + "account_table";
	}
	/**
	 * @Title: table
	 * @Description: 客户账单列表
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param period
	 * @param traceIds
	 * @param settlementStatus
	 * @param accountStatus
	 * @param searchCond
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, String startDate, String endDate) {
		

		BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
		
		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		//如果他是欠费管理，查询除交易成功的账单
		//accountItemMapList = customerAccountItemService.searchCustomerArrearsAccountItem(period, traceIds, settlementStatus, searchCond, operatorId, startDate, endDate);
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String accountItemIdStr = accountItemMap.get("ID").toString();//账单ID
			Long accountItemId = Long.valueOf(accountItemIdStr);//账单ID
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
			//String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
			//String currTraceIds = accountItemMap.get("TRACE_IDS").toString();//地理位置traceIds
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			PartitionWater part = partitionWaterService.getPartitionWater(accountItemId);
			String meterDesc = "";//表计描述
			if(part!=null) {
				log.debug("----------分水量记录ID："+part.getId()+"，表计ID："+part.getMeterId());
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(part.getMeterId()));
				meterDesc = meter.getDescription();
			}
			
			//获取客户类型
			Integer customerType = Integer.valueOf(accountItemMap.get("CUSTOMER_TYPE").toString());
			String customerName = (String)accountItemMap.get("CUSTOMER_NAME");
			
			if(customerType == EnumCustomerType.CUSTOMER_UNIT.getValue() && StringUtils.isNotBlank(meterDesc)) {//如果是单位用户，且表计描述不为空
				customerName = customerName+"（"+meterDesc+"）";
			}
			
			//计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationCustomerService.getPlace(customerId);
			accountItemMap.put("place", place);//地理位置
			accountItemMap.put("meterPlace", customerName);//地理位置
			
			accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			accountItemMap.put("overdueValue", overdueValue.setScale(2));//违约金总金额
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，欠费金额+违约金欠款
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount.setScale(2));
			//获取贷方摘要
			String creditSubject = "";
			if(accountItemMap.get("CREDIT_SUBJECT") != null) {
				creditSubject = AiSubjectUtils.getAiSubjectStr(accountItemMap.get("CREDIT_SUBJECT").toString());

			}
			accountItemMap.put("creditSubject", creditSubject);
			
//			String billPrice = accountItemMap.get("CREDIT_AMOUNT").toString();//账单金额
//			String rechargePrice = accountItemMap.get("DEBIT_AMOUNT").toString();//已充值金额
//			billTotalPrice = BigDecimalUtils.add(billTotalPrice, new BigDecimal(billPrice));//统计账单总金额，用于列表统计
//			rechargeTotalPrice = BigDecimalUtils.add(rechargeTotalPrice, new BigDecimal(rechargePrice));//统计充值总金额，用于列表统计
//			//overdueTotalPrice = BigDecimalUtils.add(overdueTotalPrice, overdueValue.setScale(2));//统计违约金总金额，用于列表统计
//			oweTotalPrice = BigDecimalUtils.add(oweTotalPrice, totalOweAmount);//统计欠费总金额，用于列表统计
			//是否追加减免水量
			boolean isAddSubWater = false;
			AddSubWater log = addSubWaterService.getAddSubLog(Long.valueOf(customerId), period, part.getId());
			if(log!=null) {
				isAddSubWater = true;
			}
			accountItemMap.put("isAddSubWater", isAddSubWater);
		}
		
		//统计本期账单
		String billTotalPrice = "";//账单总金额
		String rechargeTotalPrice = "";//已充值总金额
		//BigDecimal overdueTotalPrice = new BigDecimal(0.00);//违约金总金额
		String oweTotalPrice = "";//欠费总金额
		//获取统计数据
		Map<String, Object> statisticMap = customerAccountItemService.getCustomerAccountItemStatistic(period, traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		if(statisticMap != null) {
			billTotalPrice = statisticMap.get("TOTAL_CREDIT_AMOUNT").toString();
			rechargeTotalPrice = statisticMap.get("TOTAL_DEBIT_AMOUNT").toString();
			oweTotalPrice = statisticMap.get("TOTAL_OWE_AMOUNT").toString();
		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		//回传统计金额
		model.addAttribute("billTotalPrice", billTotalPrice);
		model.addAttribute("rechargeTotalPrice", rechargeTotalPrice);
		//model.addAttribute("overdueTotalPrice", overdueTotalPrice);
		model.addAttribute("oweTotalPrice", oweTotalPrice);

		return TEMPLATE_PATH + "account_table";
	}
	
	/**
	 * @Title: detail
	 * @Description: 加载客户账单详情
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/detail-table")
	public String detail(Model model, Long id) {

		Map<String, Object> customerAccountItemMap = null;
		if (id != null && id > 0) {
			CustomerAccountItem accountItem = customerAccountItemService.selectByPrimaryKey(id);
			//计算账单的欠费金额
			BigDecimal billOweAmount = BigDecimalUtils.subtract(accountItem.getCreditAmount(), accountItem.getDebitAmount());
			
			//计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(id);
			//计算已充值违约金总金额
			BigDecimal rechargeOverdueValue = customerAccountItemService.getRechargeOverdueValueSum(id);
			//计算违约金欠费金额 = 违约金总金额-已充值违约金总金额
			BigDecimal overdueOweAmount = BigDecimalUtils.subtract(overdueValue, rechargeOverdueValue);
			//计算欠费金额=账单欠费金额+违约金欠费金额
			BigDecimal owedAmountValue = BigDecimalUtils.add(billOweAmount, overdueOweAmount);
			String operatorName = "";
			if(accountItem.getAccounter() != null) {
				SysUsers user = usersService.selectByPrimaryKey(Long.valueOf(accountItem.getAccounter()));
				operatorName = user.getRealname();
			}
			
			customerAccountItemMap = EntityUtils.entityToMap(accountItem);
			customerAccountItemMap.put("operatorName", operatorName);// 记账人
			customerAccountItemMap.put("accountDateStr", accountItem.getAccountDateStr());// 记账日期
			customerAccountItemMap.put("overdueDateStr", accountItem.getOverdueDateStr());// 违约金开始计算日期
			customerAccountItemMap.put("startTimeStr", accountItem.getStartTimeStr());// 账单开始日期
			customerAccountItemMap.put("endTimeStr", accountItem.getEndTimeStr());// 账单结束日期
			customerAccountItemMap.put("overdueValue", overdueValue.setScale(2));// 违约金账单金额
			customerAccountItemMap.put("rechargeOverdueValue", rechargeOverdueValue.setScale(2));// 已充值违约金账单金额
			// 计算欠费金额，欠费金额=贷方金额（账单金额）+ 违约金金额 - 充值金额
			customerAccountItemMap.put("arrearsValue", owedAmountValue);
			// 获取客户名称
			Long customerId = accountItem.getCustomerId();
			Customers customer = customersService.selectByPrimaryKey(customerId);
			customerAccountItemMap.put("customerName", customer.getCustomerName());
			// 获取分水量信息
			PartitionWater partWater = partitionWaterService.getPartitionWater(id);
			
			BigDecimal basePrice = new BigDecimal("0");
			BigDecimal treatment = new BigDecimal("0");
			BigDecimal waterAmount = new BigDecimal("0");
			BigDecimal waterPrice = new BigDecimal("0");
			String preDateStr = "";
			String preRead = "";
			String currDateStr = "";
			String currRead = "";
			if(partWater != null) {
				//获取抄表记录信息
				MeterRecord record = meterRecordService.selectByPrimaryKey(Long.valueOf(partWater.getRecordId()));
				basePrice = partWater.getBasePrice();
				treatment = partWater.getTreatmentFee();
				waterAmount = partWater.getRealWaterAmount();
				waterPrice = BigDecimalUtils.add(partWater.getBasePrice(), partWater.getTreatmentFee());
				preDateStr = record.getPreDateStr();
				preRead = record.getPreRead();
				currDateStr = record.getCurrDateStr();
				currRead = record.getCurrRead();
				
			}
			customerAccountItemMap.put("basePrice", basePrice);
			customerAccountItemMap.put("treatment", treatment);
			customerAccountItemMap.put("waterAmount", waterAmount);
			customerAccountItemMap.put("waterPrice", waterPrice);
			
			customerAccountItemMap.put("preDate", preDateStr);
			customerAccountItemMap.put("preRead", preRead);
			customerAccountItemMap.put("currDate", currDateStr);
			customerAccountItemMap.put("currRead", currRead);
		}

		model.addAttribute("accountItem", customerAccountItemMap);

		return TEMPLATE_PATH + "account_detail_table";
	}

	/**
	 * @Title: loadInsertAccountItem
	 * @Description: 加载增加账单选项卡
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-account-item-insert")
	public String loadInsertAccountItem(Model model) {
		List<SysWaterPrice> waterPriceList = waterPriceService.selectAll();
		model.addAttribute("waterPriceList", waterPriceList);//初始化水价信息
		return TEMPLATE_PATH_BILL + "insert_account_item";
	}
	
	/**
	 * @Title: customerItemMain
	 * @Description: 加载客户列表
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/customer-item-main")
	public String customerItemMain(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		model.addAttribute("customer", null);//初始化客户信息
		
		return TEMPLATE_PATH_BILL + "customer_item_main";
	}
	
	@RequestMapping(value = "/customer-item-table")
	public String customerItemTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds) {
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> customerList = customersService.getInsertAccountCustomerList(searchCond, traceIds);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		
//		C.ID AS CUSTOMER_ID,
//		LM.TRACE_IDS
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(Map<String, Object> map : customerList) {
			String customerId = map.get("CUSTOMER_ID").toString();
			String currTraceIds = map.get("TRACE_IDS").toString();
			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
			String place = locationService.getPlace(currTraceIds);
			
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("place", place);
			customerMapList.add(customerMap);
			//customer.put("waterUseValue", this.getPriceTypeName(customer.get("WATER_USE").toString()));//
			
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH_BILL + "customer_item_table";
	}
	
	/**
	 * @Title: getDiscountName
	 * @Description: 获取用水性质名称
	 * @param discountId
	 * @return 
	 */
	private String getPriceTypeName(String priceTypeCode) {
		String priceTypeName = waterPriceService.getPriceTypeName(priceTypeCode);
		if(priceTypeName!=null) {
			return priceTypeName;
		}
		return null;
	}
	
	/**
	 * @Title: getLocationMessage
	 * @Description: 根据客户ID查询地理位置信息
	 * @param model
	 * @param meterId
	 * @param place
	 * @return 
	 */
	@RequestMapping(value = "/get-customer-message")
	@ResponseBody
	public Object getLocationMessage(Model model, Long customerId) {
		LocationCustomer lm = locationCustomerService.getLocationByCustomerId(customerId);
		if(lm!=null) {
			Customers customer = customersService.selectByPrimaryKey(lm.getCustomerId());
			return customer;
		}
		return null;
	}
	

	/**
	 * @Title: addAccountItem
	 * @Description: 增加账单
	 * @param accountItem
	 * @return
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addAccountItem(Long customerId, String period, Integer debitCredit, String digest, Long waterPriceId, BigDecimal amount) {
		
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int row = customerAccountItemService.insertCustomerAccountItem(customerId, period, debitCredit, digest, waterPriceId, amount, userBean.getId());
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	/**
	 * @Title: deleteAccountItem
	 * @Description: 删除账单信息
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteAccountItem(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			
			boolean isDelete = true;
			String msg = "删除失败，请重新尝试！";
			for (Long id : ids) {
				
				int rows = customerAccountItemService.delete(id);
				if(rows==0) {
					isDelete = false;
					CustomerAccountItem bill = customerAccountItemService.selectByPrimaryKey(id);
					Customers customer = customersService.selectByPrimaryKey(bill.getCustomerId());
					String customerName = customer.getCustomerName();
					msg = "客户【"+customerName+"】的账单已充值，不允许删除！";
					break;
				}else if(rows==-1){
					msg = "删除失败，请手动撤销分账后再删除账单！";
				}
				
			}
			if(!isDelete) {
				return RequestResultUtil.getResultFail(msg);
			}
			return RequestResultUtil.getResultDeleteSuccess();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultDeleteWarn();

	}

	/**
	 * @Title: loadModiAccountItemDialog
	 * @Description: 加载修改账单信息对话框
	 * @param accountItemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadmodiaccountitemdialog")
	public String loadModiAccountItemDialog(Long accountItemId, Model model) {

		// 读取需要编辑的条目
		CustomerAccountItem currItem = customerAccountItemService.selectByPrimaryKey(accountItemId);
		model.addAttribute("currItem", currItem);

		return TEMPLATE_PATH + "account_dialog_edit";
	}

	/**
	 * @Title: updateAccountItem
	 * @Description: 编辑账单信息
	 * @param accountItem
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateAccountItem(CustomerAccountItem accountItem) throws Exception {

		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}

		accountItem.setAccounter(userBean.getRealname());
		accountItem.setAccountDate(new Date());

		customerAccountItemService.updateByPrimaryKeySelective(accountItem);
		return RequestResultUtil.getResultUpdateSuccess();
	}

	// ------------------------生成违约金选项卡-------------------

	/**
	 * @Title: generateOverdueFine
	 * @Description: 生成违约金
	 * @param model
	 * @param customerId
	 * @param accountItemId
	 * @param overdueFineJSON
	 * @return
	 */
	@RequestMapping(value = "/generate-overdue-fine", produces = "application/json")
	@ResponseBody
	public Object generateOverdueFine(Model model, Long customerId, Long accountItemId, String period) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}

		int rows = customerAccountItemService.generateOverdueBill(accountItemId, userBean.getId());
		if (rows > 0) {
			return RequestResultUtil.getResultSaveSuccess("创建违约金账单成功！");
		}
		return RequestResultUtil.getResultSaveWarn("创建违约金账单异常，请重新尝试！");

	}

	// ------------------------违约金减免选项卡-------------------

	/**
	 * @Title: loadOverdueReduceDialog
	 * @Description: 加载违约金减免对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadoverduereducedialog")
	public String loadOverdueReduceDialog(Model model, Long customerId, String period, Long accountItemId) {

		// 查询客户违约金账单
		CustomerAccountItem item = new CustomerAccountItem();
		item.setPid(accountItemId);
		List<CustomerAccountItem> accountItemList = customerAccountItemService.select(item);
		
		List<Long> accountIdIdList = new ArrayList<Long>();
		for (CustomerAccountItem ca : accountItemList) {
			Long accountId = ca.getId();
			accountIdIdList.add(accountId);
		}
		List<CustomerOverdueFine> overdueFineList = new ArrayList<>();
		
		if(accountIdIdList != null && accountIdIdList.size()>0) {
			overdueFineList = customerOverdueFineService.selectIdByAccountIdList(accountIdIdList);
		}
		
		List<Map<String, Object>> overdueFineMapList = new ArrayList<>();
		for (CustomerOverdueFine overdueFineItem : overdueFineList) {
			// 查询已减免金额
			CustomerAccountItem accountItem = customerAccountItemService.selectByPrimaryKey(overdueFineItem.getOverdueAccountId());

			Map<String, Object> accountItemMap = EntityUtils.entityToMap(overdueFineItem);

			Customers customers = customersService.selectByPrimaryKey(customerId);
			accountItemMap.put("customerName", customers.getCustomerName());
			accountItemMap.put("discountAmountSum", accountItem.getDebitAmount());
			overdueFineMapList.add(accountItemMap);

		}

		model.addAttribute("overdueFineList", overdueFineMapList);
		return TEMPLATE_PATH + "overdue_reduce_dialog";
	}

	/**
	 * @Title: saveOverdueFine
	 * @Description: 保存减免违约金
	 * @param model
	 * @param customerId
	 * @param accountItemId
	 * @param overdueFineJSON
	 * @return
	 */
	@RequestMapping(value = "/save-overdue-fine", produces = "application/json")
	@ResponseBody
	public Object saveOverdueFine(Model model, String overdueFineJSON) {
		if(overdueFineJSON == null || overdueFineJSON == "") {
			return RequestResultUtil.getResultAddWarn("请选择需要减免的违约金");
		}
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}
		List<CustomerOverdueFine> dfList = JSON.parseArray(overdueFineJSON, CustomerOverdueFine.class);
		int rows = customerOverdueFineService.saveList(dfList);
		if (rows > 0) {
			return RequestResultUtil.getResultSaveSuccess("减免违约金成功！");
		}
		return RequestResultUtil.getResultSaveWarn("减免违约金数据异常，请重新尝试！");

	}
	
	// ------------------------水费减免选项卡-------------------

	/**
	 * @Title: loadWaterFeeReduceDialog
	 * @Description: 加载水费减免对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadwaterfeereducedialog")
	public String loadWaterFeeReduceDialog(Model model, Long customerId, String period, Long accountItemId) {
		// 查询客户账单
		CustomerAccountItem waterFee = customerAccountItemService.selectByPrimaryKey(accountItemId);

		List<Map<String, Object>> waterFeeMapList = new ArrayList<>();

		Map<String, Object> accountItemMap = EntityUtils.entityToMap(waterFee);

		Customers customers = customersService.selectByPrimaryKey(customerId);
		accountItemMap.put("customerName", customers.getCustomerName());
		accountItemMap.put("discountAmountSum", waterFee.getDebitAmount());
		waterFeeMapList.add(accountItemMap);

		model.addAttribute("waterFeeList", waterFeeMapList);
		return TEMPLATE_PATH + "water_fee_reduce_dialog";
	}

	/**
	 * @Title: saveWaterFeeReduce
	 * @Description: 保存减免水费
	 * @param model
	 * @param customerId
	 * @param accountItemId
	 * @param overdueFineJSON
	 * @return
	 */
	@RequestMapping(value = "/save-water-fee-reduce", produces = "application/json")
	@ResponseBody
	public Object saveWaterFeeReduce(Model model, Long customerId, Long accountItemId, String overdueFineJSON) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}
		List<DiscountWaterFeeTrace> dfList = JSON.parseArray(overdueFineJSON, DiscountWaterFeeTrace.class);
		int rows = discountWaterFeeTraceService.saveList(customerId, accountItemId, dfList);
		if (rows > 0) {
			return RequestResultUtil.getResultSaveSuccess("减免水费成功！");
		}
		return RequestResultUtil.getResultSaveWarn("减免水费数据异常，请重新尝试！");

	}

	// ------------------------结算账单-------------------

	/**
	 * @Title: loadSettleAccountDialog
	 * @Description: 结算账单对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadsettleaccountdialog")
	public String loadSettleAccountDialog(Long accountItemId, Model model) {
		// 读取需要编辑的条目
		CustomerAccountItem currItem = customerAccountItemService.selectByPrimaryKey(accountItemId);
		//获取账单欠费金额
		BigDecimal billOweAmount = BigDecimalUtils.subtract(currItem.getCreditAmount(), currItem.getDebitAmount());
		//获取违约金金额之和
		BigDecimal overdueOweAmountSum = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
		//计算总欠费金额=账单欠费金额+违约金金额之和
		BigDecimal totalOweAmount = BigDecimalUtils.add(billOweAmount, overdueOweAmountSum);
		
		//查询分水量
		PartitionWater pw = partitionWaterService.getPartitionWater(currItem.getId());
		BigDecimal basePriceOweAmount = new BigDecimal(0.00);//账单基础水费欠费金额
		BigDecimal treatmentFeeOweAmount = new BigDecimal(0.00);//账单污水处理费欠费金额
		if(pw!=null) {
			//获取账单基础水费欠费金额
			basePriceOweAmount = this.getBasePriceOweAmount(currItem.getDebitAssistant(), pw.getRealWaterAmount(), pw.getBasePrice());
			//获取账单污水处理费欠费金额
			treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(currItem.getDebitAssistant(), pw.getRealWaterAmount(), pw.getTreatmentFee());
		}
		
		//查询客户
		Customers customer = customersService.selectByPrimaryKey(currItem.getCustomerId());
		
		//List<EnumAiDebitSubjectAction> subjectActionList = EnumAiDebitSubjectAction.getSettleType();;//缴费时缴费类型
		List<EnumAiDebitSubjectAction> subjectActionList = new ArrayList<>();
		if(customer.getCustomerType()==EnumCustomerType.CUSTOMER_PEOPLE.getValue() || pw==null) {//如果客户类型为个人用户，或分水量为空时缴费类型只有水费（综价）
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_WATER_FEE);
		}else {//如果客户类型为非个人用户时缴费类型为基础水费和污水处理费
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_BASE_FEE);
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_TREATMENT_FEE);
		}
		
		model.addAttribute("subjectActionList", subjectActionList);//缴费时缴费类型
		List<EnumAiDebitSubjectPayment> subjectPaymentList = EnumAiDebitSubjectPayment.getSettlePayment();//缴费时的支付方式
		model.addAttribute("subjectPaymentList", subjectPaymentList);//缴费时的支付方式
		
		model.addAttribute("customer", customer);//当前账单对应的客户信息
		model.addAttribute("period", currItem.getPeriod());//当前账单对应的客户信息
		model.addAttribute("totalOweAmount", totalOweAmount);//当前账单总欠费金额
		model.addAttribute("basePriceOweAmount", basePriceOweAmount);//当前账单基础水费欠费金额
		model.addAttribute("treatmentFeeOweAmount", treatmentFeeOweAmount);//账单污水处理费欠费金额
		
		return TEMPLATE_PATH + "settle_account_dialog";
	}
	
	/**
	 * @Title: getBasePriceOweAmount
	 * @Description: 获取账单基础水费欠费金额
	 * @param assistant
	 * @param waterAmount
	 * @param basePrice
	 * @return 
	 */
	private BigDecimal getBasePriceOweAmount(String assistant, BigDecimal waterAmount, BigDecimal basePrice) {
		
		//基础水价科目
		String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_BASE_FEE.getKey();
		
		//计算基础水价总金额=水量*基础水价单价
		BigDecimal basePriceTotalAmount = BigDecimalUtils.multiply(waterAmount, basePrice);
		
		BigDecimal payAmount = new BigDecimal(0.00);//基础水价已支付金额变量
		
		//assistant
		if(StringUtils.isNotBlank(assistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);
			
			for(AssistantBean assistantBean : assistantBeanList) {
				String subject = assistantBean.getSubject();
				if(subject.indexOf(subjectBase)!=-1) {
					BigDecimal amount = assistantBean.getAmount();
					payAmount = BigDecimalUtils.add(payAmount, amount); 
				}
			}
		}
		//计算基础水费欠费金额
		BigDecimal basePriceOweAmount = BigDecimalUtils.subtract(basePriceTotalAmount, payAmount);
		return basePriceOweAmount;
	}
	
	/**
	 * @Title: getTreatmentFeeOweAmount
	 * @Description: 获取账单污水处理费欠费金额
	 * @param assistant
	 * @param waterAmount
	 * @param treatmentFee
	 * @return 
	 */
	private BigDecimal getTreatmentFeeOweAmount(String assistant, BigDecimal waterAmount, BigDecimal treatmentFee) {
		
		//污水处理费科目
		String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_TREATMENT_FEE.getKey();
		
		//计算污水处理费总金额=水量*污水处理费单价
		BigDecimal basePriceTotalAmount = BigDecimalUtils.multiply(waterAmount, treatmentFee);
		
		BigDecimal payAmount = new BigDecimal(0.00);//污水处理费已支付金额变量
		
		//assistant
		if(StringUtils.isNotBlank(assistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);
			
			for(AssistantBean assistantBean : assistantBeanList) {
				String subject = assistantBean.getSubject();
				if(subject.indexOf(subjectBase)!=-1) {
					BigDecimal amount = assistantBean.getAmount();
					payAmount = BigDecimalUtils.add(payAmount, amount); 
				}
			}
		}
		//计算污水处理费欠费金额
		BigDecimal basePriceOweAmount = BigDecimalUtils.subtract(basePriceTotalAmount, payAmount);
		return basePriceOweAmount;
	}

	/**
	 * @Title: settleAccount
	 * @Description: 结算账单
	 * @param model
	 * @param accountItemId
	 * @param settleAmount
	 * @param subjectAction
	 * @param subjectPayment
	 * @param paymentType	缴费类型：1=充值缴费；2=清欠缴费
	 * @return 
	 */
	@RequestMapping(value = "/settle-account", produces = "application/json")
	@ResponseBody
	public Object settleAccount(Model model, Long accountItemId, BigDecimal settleAmount, String subjectAction, String subjectPayment, Integer paymentType) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}

		//获取缴费类型
		EnumAiDebitSubjectAction debitSubjectAction = EnumAiDebitSubjectAction.getSubjectAction(subjectAction);
		//获取支付方式
		EnumAiDebitSubjectPayment debitSubjectPayment = EnumAiDebitSubjectPayment.getSubjectPayment(subjectPayment);
		
		int rows = customerAccountItemService.settleAccount(accountItemId, userBean.getId(), settleAmount, debitSubjectAction, debitSubjectPayment, paymentType, EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
		if (rows > 0) {

			return RequestResultUtil.getResultSaveSuccess("账单结算成功！");
		}

		return RequestResultUtil.getResultSaveWarn("账单结算异常，请重新尝试！");

	}
	
	
	// ------------------------分账单按钮-------------------

	/**
	 * @Title: loadSubAccountDialog
	 * @Description: 加载分账单对话框对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-sub-account-dialog")
	public String loadSubAccountDialog(Model model, Long customerId, String period, Long accountItemId) {
		CustomerAccountItem currItem = customerAccountItemService.selectByPrimaryKey(accountItemId);//查询分账账单
		PartitionWater partWater = partitionWaterService.getPartitionWater(accountItemId);//查询分账的分水量记录
		//根据默认客户id，获取一表多户的其他客户
		List<Long> customerIdList = customerMeterService.getSubAccountCustomerId(customerId);
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for (Long currCustomerId : customerIdList) {

			Customers customers = customersService.selectByPrimaryKey(currCustomerId);
			
			//Map<String, Object> subAccountMap = EntityUtils.entityToMap(customerMeter);
			Map<String, Object> subAccountMap = new HashMap<>();
			subAccountMap.put("customerId", customers.getId());
			subAccountMap.put("customerName", customers.getCustomerName());
			subAccountMap.put("period", currItem.getPeriod());
			subAccountMap.put("creditAmount", currItem.getCreditAmount());
			
			subAccountMap.put("waterPrice", partWater.getWaterPrice());//水价（单价）
			
			customerMapList.add(subAccountMap);

		}

		model.addAttribute("customerList", customerMapList);
		model.addAttribute("period", currItem.getPeriod());//期间
		model.addAttribute("totalWaterFee", currItem.getCreditAmount());//水费
		model.addAttribute("totalWaterAmount", partWater.getRealWaterAmount());//水量
		return TEMPLATE_PATH + "sub_account_dialog";
	}
	
	/**
	 * @Title: saveSubAccount
	 * @Description: 保存分账单
	 * @param model
	 * @param subFineJSON
	 * @param subWaterAmountJSON
	 * 			{"100", "200", "300"...}
	 * @param accountItemId
	 * @param totalAmount
	 * @return 
	 */
	@RequestMapping(value = "/save-sub-account", produces = "application/json")
	@ResponseBody
	public Object saveSubAccount(Model model, String subFineJSON, String subWaterAmountJSON, Long accountItemId, BigDecimal totalAmount) {
		List<CustomerAccountItem> accountItemList = JSON.parseArray(subFineJSON, CustomerAccountItem.class);
		List<BigDecimal> waterAmountList = JSON.parseArray(subWaterAmountJSON, BigDecimal.class);//分账的水量集合
		//分配金额
		BigDecimal subAmount = new BigDecimal("0");
		for(CustomerAccountItem item : accountItemList) {
			subAmount = BigDecimalUtils.add(subAmount, item.getCreditAmount());
		}
		if(BigDecimalUtils.equals(totalAmount, subAmount)) {//判断账单是否全部分配完毕
			int rows = customerAccountItemService.saveSubAccount(accountItemList, accountItemId, waterAmountList);
			if (rows > 0) {
				return RequestResultUtil.getResultSaveSuccess("分账成功！");
			}
			return RequestResultUtil.getResultSaveWarn("分账数据异常，请重新尝试！");
		}
		
		return RequestResultUtil.getResultSaveWarn("分账金额异常，分账金额之和必须等于"+totalAmount+"元");
	}
	
	// ------------------------撤销分账单按钮-------------------

	/**
	 * @Title: loadCancelSubAccountDialog
	 * @Description: 撤销分账单对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-cancel-sub-account-dialog")
	public String loadCancelSubAccountDialog(Model model, Long customerId, String period, Long accountItemId) {
		
		final Long zero = 0l;//Long类型0
		
		CustomerAccountItem currItem = customerAccountItemService.selectByPrimaryKey(accountItemId);
		//根据默认客户id，获取一表多户的其他客户
		List<CustomerAccountItem> subAccountItemList = customerAccountItemService.selectNewCustomerAccountItem(customerId, currItem.getPeriod(), null);
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for (CustomerAccountItem caItem : subAccountItemList) {

			Map<String, Object> accountItemMap = EntityUtils.entityToMap(caItem);

			Customers customers = customersService.selectByPrimaryKey(caItem.getCustomerId());
			accountItemMap.put("customerName", customers.getCustomerName());
			accountItemMap.put("creditAmount", caItem.getCreditAmount());
			accountItemMap.put("creditDigest", caItem.getCreditDigest());
			accountItemMap.put("creditSubject", AiSubjectUtils.getAiSubjectStr(caItem.getCreditSubject()));
			accountItemMap.put("debitCredit", caItem.getDebitCredit());
			if(caItem.getPid().equals(zero)) {
				accountItemMap.put("remark", "原账单");
			}else {
				accountItemMap.put("remark", "分账账单");
			}
			
			customerMapList.add(accountItemMap);

		}

		model.addAttribute("customerList", customerMapList);
		model.addAttribute("currItem", currItem);
		return TEMPLATE_PATH + "cancel_sub_account_dialog";
	}
	
	/**
	 * @Title: saveCancelSubAccount
	 * @Description: 保存撤销分账单
	 * @param model
	 * @param subFineJSON
	 * @return 
	 */
	@RequestMapping(value = "/save-cancel-sub-account", produces = "application/json")
	@ResponseBody
	public Object saveCancelSubAccount(Model model, Long accountItemId, Long customerId, String period) {
		//根据默认客户id，获取一表多户的其他客户
		//List<CustomerAccountItem> subAccountItemList = customerAccountItemService.selectNewCustomerAccountItem(customerId, period, null);
		int rows = customerAccountItemService.saveCancelSubAccount(accountItemId);
		if (rows > 0) {
			return RequestResultUtil.getResultSaveSuccess("撤销分账成功！");
		}
		return RequestResultUtil.getResultSaveWarn("撤销分账数据异常，请重新尝试！");
		
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
	
	//---------------------------------发送短信通知-----------------------------------------------------------------------------------------------
	/**
	 * @Title: sendSmsNotify
	 * @Description: 发送短信通知
	 * @param request
	 * @param model
	 * @param accountItemId
	 * @return 
	 */
	@RequestMapping(value = "/send-sms-notify")
	@ResponseBody
	public Object sendSmsNotify(HttpServletRequest request, Model model, Long accountItemId) {
		
		try {
			
			BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型 0
			
			//查询账单
			CustomerAccountItem item = customerAccountItemService.selectByPrimaryKey(accountItemId);
			//计算账单欠费金额
			BigDecimal oweAmount = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());
			
			//如果欠费金额>0，则发送短信通知
			if(BigDecimalUtils.greaterThan(oweAmount, zero)) {
				//发送短信通知
				//查询客户信息
				Customers customer = customersService.selectByPrimaryKey(item.getCustomerId());
				
				//客户手机号
				String mobileNo = customer.getPropMobile();
				if(StringUtils.isBlank(mobileNo)) {
					mobileNo = customer.getCustomerMobile();
				}
				//验证发送短信的手机号码是否为空
				if(StringUtils.isBlank(mobileNo)) {
					return RequestResultUtil.getResultFail("客户手机号为空！");
				}
				
				//指定模板所对应的参数值
				List<String> tpl_parms = new ArrayList<>();
				//客户姓名
				String customerName = customer.getPropName();
				if(StringUtils.isBlank(customerName)) {
					customerName = customer.getCustomerName();
				}
				if(StringUtils.isNotBlank(customerName)) {
					tpl_parms.add(customerName);
				}
				
				String period = item.getPeriod();//期间
				if(StringUtils.isNotBlank(period)) {
					tpl_parms.add(period);
				}
				tpl_parms.add(oweAmount.toString());//账单金额
				
				//如果手机号不为空，且模版参数正确，则发送短信
				if(StringUtils.isNotBlank(mobileNo) && tpl_parms.size()==3) {
					String retJSON = smsService.sendSingleSMS(mobileNo, SMSConstants.SMS_TEMPLATE_ID_FEE, tpl_parms);
					if(StringUtils.isNotBlank(retJSON)) {
						SendSingleSMResponse ret = JSON.parseObject(retJSON, SendSingleSMResponse.class);
						if(ret.getResult()==0) {
							return RequestResultUtil.getResultSuccess("发送短信通知成功！");
						}else {
							return RequestResultUtil.getResultFail("发送短信通知失败，原因："+ret.getErrmsg());
						}
					}else {
						return RequestResultUtil.getResultFail("发送短信通知失败，返回信息为空！");
					}
				}else {
					return RequestResultUtil.getResultFail("缺少参数！");
				}
				
			}else {
				return RequestResultUtil.getResultFail("此账单已全部结算，不需要发送短信通知！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(RequestResultUtil.getResultFail("上传CMBC文本文件销账异常！请重试！"));
	}
	
	//***********************************************导出账单Excel**********************************
	
	/**
	 * @Title: exportCustomerBillExcel
	 * @Description: 导出客户账单
	 * @param request
	 * @param response
	 * @param searchCond
	 * @param settlementStatus
	 * @param traceIds
	 * @param period
	 * @param accountStatus
	 * @param startDate
	 * @param endDate 
	 */
	@RequestMapping("/export-customer-bill-excel")
	public void exportCustomerBillExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer settlementStatus, String traceIds, String period, Integer accountStatus,String startDate, String endDate) {
		
		Long operatorId = this.getOperatorId();
		//excel标题
		String[] titles = { "房间号", "客户姓名", "期间", "用水性质", "水量", "基础水价", "污水处理费", "合计水价", "基础水费", "污水水费", "合计水费", "已结算金额", "欠费金额", "备注"};
		//excel列名
		String[] columnNames = { "room", "customerName", "period", "priceCode", "waterAmount", "basePrice", "treatmentPrice", "waterPrice", "baseWaterFee", "treatmentFee", "waterFee", "debitAmount", "oweAmount", "remark"};
		//sheet名
		String sheetName = period+"账单";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, settlementStatus, traceIds, period, accountStatus, startDate, endDate);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//获取导出EXCEL的文件名
		String fileName = this.getBillFileName(period, traceIds);
		
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
	private List<Map<String, Object>> getExportExcelData(String searchCond, Integer settlementStatus, String traceIds, String period, Integer accountStatus,String startDate, String endDate){
		//List<Map<String, Object>> customerBillMapList = customerAccountItemService.getExportCustomerBillData(searchCond, settlementStatus, traceIds, period, accountStatus, startDate, endDate);
		List<Map<String, Object>> customerBillMapList = customerAccountItemService.searchCustomerAccountItem(period, traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		
		for(Map<String, Object> billMap : customerBillMapList) {
			//String currTraceIds = billMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			String customerId = billMap.get("CUSTOMER_ID").toString();//客户ID
			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
			
			//String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			String place = locationCustomerService.getPlace(Long.valueOf(customerId));
			billMap.put("room", place);
			billMap.put("customerName", customer.getCustomerName());
			if(billMap.get("PERIOD") != null) {
				billMap.put("period",billMap.get("PERIOD").toString());
			}
			//billMap.put("period", period);
			//获取分水量
			PartitionWater partWater = partitionWaterService.getPartitionWater(Long.valueOf(billMap.get("ID").toString()));
			if(partWater != null) {
				SysWaterPrice waterPrice = waterPriceService.getWaterPriceByPriceCode(partWater.getWaterUse());
				billMap.put("priceCode", waterPrice.getLadderName());
				billMap.put("waterAmount", partWater.getRealWaterAmount());
				billMap.put("basePrice", partWater.getBasePrice());
				billMap.put("treatmentPrice", partWater.getTreatmentFee());
				billMap.put("waterPrice", partWater.getWaterPrice());
				billMap.put("waterAmount", partWater.getRealWaterAmount());
				BigDecimal baseWaterFee = BigDecimalUtils.multiply(partWater.getBasePrice(),partWater.getRealWaterAmount());
				billMap.put("baseWaterFee", baseWaterFee);
				BigDecimal treatmentFee = BigDecimalUtils.multiply(partWater.getTreatmentFee(),partWater.getRealWaterAmount());
				billMap.put("treatmentFee", treatmentFee);
				billMap.put("waterFee", partWater.getWaterFee());
			}
			if(billMap.get("DEBIT_AMOUNT") != null) {
				billMap.put("debitAmount",billMap.get("DEBIT_AMOUNT").toString());
			}
			if(billMap.get("OWE_AMOUNT") != null) {
				billMap.put("oweAmount",billMap.get("OWE_AMOUNT").toString());
			}
			
			String creditSubject = "";
			if(billMap.get("CREDIT_SUBJECT") != null) {
				creditSubject = AiSubjectUtils.getAiSubjectStr(billMap.get("CREDIT_SUBJECT").toString());
				billMap.put("remark",creditSubject);
			}
			
		}
		
		return customerBillMapList;
	}
	
	/**
	 * 获取导出EXCEL文件名
	 * @return
	 */
	private String getBillFileName(String period, String traceIds){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = sdf.format(new Date());
		String place = locationService.getPlace(traceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
		// excel文件名
		String fileName = "";
		if(StringUtils.isBlank(period)) {
			return fileName=place+"-"+times+"-"+"账单"+".xls";
		}
		if(StringUtils.isNotBlank(place)) {
			fileName = period+"-"+place;
		}
		
		fileName = fileName+"-"+times+"-"+"账单"+".xls";
		
		return fileName;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//------------------------------	导出民生银行自动扣费Excel	------------------------------
	/**
	 * @Title: exportExcel
	 * @Description: 导出民生银行扣费Excel
	 * @param request
	 * @param response
	 * @param searchCond
	 * @param locationCode 
	 */
	@RequestMapping("/export-cmbc-excel")
	public void exportCmbcExcel(HttpServletRequest request, HttpServletResponse response, String period, String locationCode, Integer settlementStatus, String searchCond) {
		
		Long operatorId = this.getOperatorId();//操作员ID
		
		//excel标题
		String[] titles = { "序号", "银行卡号", "账户名", "金额（￥）"};
		//excel列名
		String[] columnNames = { "no", "cardNo", "customerName", "owedAmountValue"};
		//sheet名
		String sheetName = "中国民生银行扣费信息";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportCmbcExcelData(period, locationCode, settlementStatus, searchCond, operatorId);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//获取导出EXCEL的文件名
		String fileName = this.getFileName(period, locationCode);
		
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
	    
	    System.out.println("导出2007文件成功！文件导出路径：--"+file);
	    
	    try {
			DownLoadFileUtil.downLoad(fileName, "xls", realPath+fileName, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	    
	}
	
	/**
	 * @Title: getCustomerBank
	 * @Description: 获取客户-银行信息
	 * @param customerId
	 * @return 
	 */
	private CustomerBanks getCustomerBank(Long customerId) {
		Example example = new Example(CustomerBanks.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("enabled", EnumEnabledStatus.ENABLED_NO.getValue());
		example.setOrderByClause(" ID DESC");
		List<CustomerBanks> bankList = bankService.selectByExample(example);
		if(bankList!=null && bankList.size()>0) {
			CustomerBanks respBank = null;
			for(CustomerBanks tempBank : bankList) {
				String accountName = tempBank.getAccountName();//开户名
				String cardNo = tempBank.getCardNo();//卡号
				if(StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(cardNo)) {
					respBank = tempBank;
					break;
				}
			}
			return respBank;
		}
		return null;
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
	private String getFileName(String period, String locationCode){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String times = sdf.format(new Date());
		
		// excel文件名
		String fileName = "CMBC";
		if(StringUtils.isNotBlank(period)) {
			fileName = fileName+"-"+period;
		}
		if(StringUtils.isNotBlank(locationCode)) {
			fileName = fileName+"-"+locationCode;
		}
		fileName = fileName+"-"+times+".xls";
		
		return fileName;
	}
	
	/**
	 * @Title: getExportCmbcExcelData
	 * @Description: 获取导出EXCEL数据
	 * @param searchCond
	 * @param locationCode
	 * @param operatorId
	 * @return 
	 */
	private List<Map<String, Object>> getExportCmbcExcelData(String period, String locationCode, Integer settlementStatus, String searchCond, Long operatorId){
		try {
			//查询
			List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, locationCode, settlementStatus, null, searchCond, operatorId, null, null);

			List<Map<String, Object>> excelDataMapList = new ArrayList<>();//导出数据集合
			for (Map<String, Object> accountItemMap : accountItemMapList) {
				//BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
				
				String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
				Long customerId = Long.valueOf(customerIdStr);//客户ID
				//String periodTemp = accountItemMap.get("PERIOD").toString();//期间
				//String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
				
				//计算违约金总金额
				//BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(customerId,period);
				//计算欠费金额
				//BigDecimal owedAmountValue = customerAccountItemService.getCurrBillOwedAmount(customerId,periodTemp, EnumAiDebitCreditStatus.CREDIT.getKey());
				
				//CustomerAccount account = this.getCustomerAccount(customerId);//客户-账户
				Customers customer = customersService.selectByPrimaryKey(customerId);//客户信息
				
				Integer deductType = customer.getDeductType();//扣费方式 1=其他；2=建行自动扣费；3=民生银行自动扣费
				
				if(deductType!=null && deductType==3) {
					
					CustomerBanks bank = this.getCustomerBank(customerId);//获取客户-银行信息
					if(bank!=null) {
						Map<String, Object> excelData = new HashMap<>();
						excelData.put("cardNo", bank.getCardNo());
						excelData.put("customerName", bank.getAccountName());
						//excelData.put("owedAmountValue", owedAmountValue);
						excelDataMapList.add(excelData);
					}
					
				}
				
			}
			//设置序号
			for(int i=0; i<excelDataMapList.size(); i++) {
				Map<String, Object> excelDataMap = excelDataMapList.get(i);
				int no = i+1;
				excelDataMap.put("no", no);
			}
			return excelDataMapList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Title: importCMBCTxtFile
	 * @Description: 导入CMBC回执文件销账
	 * @param request
	 * @param model
	 * @param fileType
	 * @param inputName
	 * @return 
	 */
	@RequestMapping(value = "/import-txt-file")
	@ResponseBody
	public Object importCMBCTxtFile(HttpServletRequest request, Model model, String period, String fileType, String inputName) {
		
		try {
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			//String serverUrl = FileUploadUtil.getReqServerURL(request);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("--------------------文件上传路径："+filePath);
				
				String txtContent = this.readTxtFile(filePath);//读取文件
				System.out.println("--------------------文件内容：");
				System.out.print(txtContent);
				System.out.println("");
				String validContent = this.getValidContent(txtContent);//获取txt文件内容中所需要的内容
				System.out.println("--------------------有效内容：");
				System.out.print(validContent);
				
				//获取扣费回执单结果
				String[] settleResult = validContent.split("\r\n");
				System.out.println("--------------------扣费回执结果：");
				
				List<CMBCAutoDeductReceiptBean> receiptList = new ArrayList<>();
				for(String str : settleResult) {
					//System.out.println(str);
					
					//如果字符串不为空
					if(StringUtils.isNotBlank(str)) {
						//如果字符串不是以===开始，且字符串不是以===结束
						if(!str.startsWith("=") && !str.endsWith("=")) {
							CMBCAutoDeductReceiptBean receipt = this.getCMBCAutoDeductReceiptBean(str);
							receiptList.add(receipt);
						}
					}
					
				}
				int rows = customerAccountItemService.cmbcSettlementChargeOff(receiptList, period);
				if(rows>0) {
					return JSON.toJSONString(RequestResultUtil.getResultSuccess("上传CMBC文本文件销账成功！"));
				}
			}
			return JSON.toJSONString(RequestResultUtil.getResultFail("上传CMBC文本文件销账失败！请重试！"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(RequestResultUtil.getResultFail("上传CMBC文本文件销账异常！请重试！"));
	}
	
	/**
	 * @Title: getCMBCAutoDeductReceiptBean
	 * @Description: 获取中国民生银行自动扣费回执单Bean
	 * @param str
	 * @return 
	 */
	private CMBCAutoDeductReceiptBean getCMBCAutoDeductReceiptBean(String str){
		
		str = str.replaceAll(" +", " ");//把多个空格替换成一个空格
		String[] strArr = str.split(" ");//根据空格拆分字符串
		int length = strArr.length;
		
		String cardNo = strArr[0];//卡号
		String accountName = strArr[1];//用户名
		BigDecimal deductionAmount = new BigDecimal(strArr[2]);//扣费金额
		Integer status = null;//扣费成功/失败状态 0=成功；1=失败；
		String errMsg = null;//扣费失败原因
		
		if(length==CMBCAutoDeductReceiptBean.LENGTH_SUCCESS) {
			status = EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue();//扣费成功/失败状态 0=成功；1=失败；
			errMsg = "交易成功";
		}else {
			status = EnumSettlementStatus.SETTLEMENT_FAIL.getValue();//扣费成功/失败状态 0=成功；1=失败；
			errMsg = strArr[3];
		}
		
		CMBCAutoDeductReceiptBean receipt = new CMBCAutoDeductReceiptBean();
		receipt.setCardNo(cardNo);
		receipt.setAccountName(accountName);
		receipt.setDeductAmount(deductionAmount);
		receipt.setStatus(status);
		receipt.setErrMsg(errMsg);
		return receipt;
		
	}
	
	/**
	 * @Title: readTxtFile
	 * @Description: 读取txt文件
	 * @param filePath
	 * @return 
	 */
	private String readTxtFile(String filePath) {
		
		String encoding = "UTF-8";
		try {
			encoding = this.getCode(filePath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        File file = new File(filePath);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			System.out.println("要读取的文件不存在");
			return null;
		}
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        } 
	}
	
	/**
	 * @Title: getCode
	 * @Description: 获取编码格式 gb2312,UTF-16,UTF-8,Unicode,UTF-8
	 * @param path
	 * @return
	 * @throws Exception 
	 */
	private String getCode(String path) throws Exception {
		InputStream inputStream = new FileInputStream(path);
		byte[] head = new byte[3];
		inputStream.read(head);
		String code = "gb2312"; // 或GBK
		if (head[0] == -1 && head[1] == -2)
			code = "UTF-16";
		else if (head[0] == -2 && head[1] == -1)
			code = "Unicode";
		else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
			code = "UTF-8";
		inputStream.close();
		return code;
	}
	
	/**
	 * @Title: getValiddContent
	 * @Description: 获取txt文件内容中所需要的内容
	 * @param txtContent
	 * @return 
	 */
	private String getValidContent(String txtContent) {
		Pattern pattern = Pattern.compile("=+\r\n([^=]+)=+");
		Matcher matcher = pattern.matcher(txtContent);
		StringBuffer buffer = new StringBuffer();
		while(matcher.find()){
		    buffer.append(matcher.group());
		    buffer.append("\r\n");              
		}
		return buffer.toString();
	}
	
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
	
}