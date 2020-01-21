package com.learnbind.ai.controller.bill;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.AssistantBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.EnumSettleType;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiTraceOperate;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysRoles;
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
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.sms.SMSConstants;
import com.learnbind.ai.sms.SMSService;
import com.learnbind.ai.sms.SendSingleSMResponse;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: AutoSettleBillController.java
 * @Description: 自动结算前端控制器
 * 			用户有余额时可以此操作结算
 *
 * @author Administrator
 * @date 2019年11月12日 上午11:18:05
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/auto-settle-account-item")
public class AutoSettleBillController {
	private static Log log = LogFactory.getLog(AutoSettleBillController.class);
	private static final String TEMPLATE_PATH = "bill/auto_settle_bill/"; // 页面目录
	private static final String TEMPLATE_PATH_CUSTOMER_BILL = "bill/auto_settle_bill/customerbill/"; // 页面目录

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService; // 客户信息
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户
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
	private BankService bankService;//客户银行信息
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计绑定关系表
	@Autowired
	private SMSService smsService;//发送短信
	@Autowired
	private PartitionWaterService partitionWaterService;//分水量

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
	 * @Title: haveCustomerTable
	 * @Description: 查询有余额的客户
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/have-balance-customer-table")
	public String haveCustomerTable(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond, Integer balanceStatus) {
		

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
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		if(balanceStatus != null) {
			accountItemMapList = customerAccountItemService.getCustomerBalanceStatusList(searchCond, traceIds,balanceStatus);
		} else {
			accountItemMapList = customerAccountItemService.getCustomerBalanceList(searchCond, traceIds);
		}

		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

//		I.CUSTOMER_ID,	客户ID
//		C.CUSTOMER_NAME, 	客户名称
//		( SUM(I.DEBIT_AMOUNT) - SUM(I.CREDIT_AMOUNT) ) AS BALANCE,	客户余额 
//		LM.TRACE_IDS 	地理位置
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID

			String place = "";//地理位置
//			if(StringUtils.isNotBlank(traceIds)) {
//				place = locationService.getPlace(traceIds);
//			}else {
				place = locationCustomerService.getPlace(customerId);
//			}
			accountItemMap.put("place", place);//地理位置
			
			//计算违约金总金额
			//BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			//BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			//accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			//accountItemMap.put("overdueValue", overdueValue);//违约金总金额
			
			//查询水费欠费金额（包含本期和往期）
			BigDecimal waterFeeOweAmount = customerAccountItemService.getWaterFeeOweAmount(customerId, null);
			//查询违约金欠费金额（包含本期和往期）
			//BigDecimal pastOverdueOweAmount = customerAccountItemService.getOverdueOweAmount(customerId, currPeriod);
			//欠费总金额=水费欠费金额+违约金欠费金额
			BigDecimal totalOweAmount = waterFeeOweAmount;//BigDecimalUtils.add(pastWaterFeeOweAmount, pastOverdueOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);
			//查询欠费期间
			List<String> owePeriodList = customerAccountItemService.getWaterFeeOwePeriod(customerId, null);
			accountItemMap.put("owePeriodList", owePeriodList);
			
			//如果欠费总金额>0时可结算，否则不可用余额结算
			if(BigDecimalUtils.greaterThan(totalOweAmount, zero)) {
				accountItemMap.put("isSettled", false);//是否已结算
			}else {
				accountItemMap.put("isSettled", true);//是否已结算
			}
			
		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "account_table";
	}
	
	/**
	 * @Title: allCustomerTable
	 * @Description: 查询所有预存客户列表
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/all-customer-table")
	public String allCustomerTable(Model model, Integer pageNum, Integer pageSize, String traceIds, String searchCond) {
		
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
		List<Customers> customerList = customersService.getPrepaymentCustomerList(searchCond, traceIds);
		PageInfo<Customers> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		for (Customers customer : customerList) {
			
			Long customerId = customer.getId();//客户ID
			String customerName = customer.getCustomerName();//客户名称
			
			LocationCustomer lc = locationCustomerService.getLocationByCustomerId(customerId);
			String currTraceIds = lc.getTraceIds();
//			if(StringUtils.isNotBlank(traceIds)) {
//				currTraceIds = traceIds;
//			}
			
			//查询地理位置
			String place = locationService.getPlace(currTraceIds);
			//查询水费欠费金额（包含本期和往期）
			BigDecimal waterFeeOweAmount = customerAccountItemService.getWaterFeeOweAmount(customerId, null);
			//查询违约金欠费金额（包含本期和往期）
			//BigDecimal pastOverdueOweAmount = customerAccountItemService.getOverdueOweAmount(customerId, currPeriod);
			//欠费总金额=水费欠费金额+违约金欠费金额
			BigDecimal totalOweAmount = waterFeeOweAmount;//BigDecimalUtils.add(pastWaterFeeOweAmount, pastOverdueOweAmount);
			//查询欠费期间
			List<String> owePeriodList = customerAccountItemService.getWaterFeeOwePeriod(customerId, null);
			//查询客户余额
			BigDecimal balance = customerAccountItemService.getCustomerBalance(customerId);
			
			Map<String, Object> accountItemMap = new HashMap<>();
			accountItemMap.put("CUSTOMER_ID", customerId);//客户ID
			accountItemMap.put("CUSTOMER_NAME", customerName);//客户名称
			accountItemMap.put("BALANCE", balance);//客户余额
			accountItemMap.put("place", place);//地理位置
			accountItemMap.put("owePeriodList", owePeriodList);//欠费期间
			accountItemMap.put("totalOweAmount", totalOweAmount);//欠费总金额
			
			//如果欠费总金额>0时可结算，否则不可用余额结算
			if(BigDecimalUtils.greaterThan(totalOweAmount, zero)) {
				accountItemMap.put("isSettled", false);//是否已结算
			}else {
				accountItemMap.put("isSettled", true);//是否已结算
			}
			accountItemMapList.add(accountItemMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "account_table";
	}
	
	//-----------------------------加载客户档案明细--------------------------------------------------------------------------
	/**
	 * @Title: main
	 * @Description: 加载客户档案明细主页面
	 * @param model
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/customer-bill-main")
	public String customerBillMain(Model model, Long customerId) {
		
//		//查询客户
//		Customers customer = customersService.selectByPrimaryKey(customerId);
//		String customerName = "";
//		if(customer!=null) {
//			customerName = customer.getCustomerName();
//		}
//		model.addAttribute("customerName", customerName);
		
		return TEMPLATE_PATH_CUSTOMER_BILL + "account_main";
	}

	/**
	 * @Title: customerBillTable
	 * @Description: 加载客户账单明细列表页面
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
	@RequestMapping(value = "/customer-bill-table")
	public String customerBillTable(Model model, Integer pageNum, Integer pageSize, Long customerId, String period, Integer settlementStatus) {
		

		//BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
		
		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		//Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerAccountItem> itemList = customerAccountItemService.getCustomerAccountItemList(period, customerId, settlementStatus);
		PageInfo<CustomerAccountItem> pageInfo = new PageInfo<>(itemList);// (使用了拦截器或是AOP进行查询的再次处理)

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式化
		List<Map<String, Object>> billMapList = new ArrayList<>();//返回的账单集合
		for (CustomerAccountItem item : itemList) {
			
			Map<String, Object> billMap = EntityUtils.entityToMap(item);
			
			//获取贷方科目
			String creditSubject = AiSubjectUtils.getAiSubjectStr(item.getCreditSubject());
			billMap.put("creditSubject", creditSubject);
			
			//记账日期
			String accountDateStr = sdf.format(item.getAccountDate());
			billMap.put("accountDateStr", accountDateStr);
			
			billMapList.add(billMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("billMapList", billMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		//model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH_CUSTOMER_BILL + "account_table";
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
			
			customerAccountItemMap = EntityUtils.entityToMap(accountItem);
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
			// 获取账户名称
			//Long accountId = accountItem.getAccountId();
			//CustomerAccount account = customerAccountService.selectByPrimaryKey(accountId);
			//customerAccountItemMap.put("accountName", account.getAccountName());
		}

		model.addAttribute("accountItem", customerAccountItemMap);

		return TEMPLATE_PATH + "account_detail_table";
	}

	// ------------------------结算账单-------------------
	/**
	 * @Title: loadSettleAccountDialog
	 * @Description: 结算账单对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadsettleaccountdialog")
	public String loadSettleAccountDialog(Long customerId, String period, Long accountItemId, Model model) {
		
		Long currCustomerId = null;//当前结算的客户ID
		String currPeriod = null;//当前结算的期间
		BigDecimal oweTotalAmount = new BigDecimal(0.00);//总欠费金额
		BigDecimal basePriceOweTotalAmount = new BigDecimal(0.00);//基础水费欠费总金额
		BigDecimal treatmentFeeOweTotalAmount = new BigDecimal(0.00);//污水处理费欠费总金额
		
		boolean isSettleWaterFee = true;//只结算水费（综价）
		Integer settleType = EnumSettleType.SETTLE_TYPE_BILL.getKey();//结算类型，默认按账单结算
		if(accountItemId!=null) {//按账单结算
			settleType = EnumSettleType.SETTLE_TYPE_BILL.getKey();//结算类型-按账单结算
			// 读取需要编辑的条目
			CustomerAccountItem currItem = customerAccountItemService.selectByPrimaryKey(accountItemId);
			currCustomerId = currItem.getCustomerId();//当前结算的客户ID
			currPeriod = currItem.getPeriod();//当前结算的期间
			//获取账单欠费金额
			BigDecimal billOweAmount = BigDecimalUtils.subtract(currItem.getCreditAmount(), currItem.getDebitAmount());
			//获取违约金金额之和
			//BigDecimal overdueOweAmountSum = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			//计算总欠费金额=账单欠费金额+违约金金额之和
			BigDecimal totalOweAmount = billOweAmount;//BigDecimalUtils.add(billOweAmount, overdueOweAmountSum);
			oweTotalAmount = totalOweAmount;//欠费金额
			
			//查询分水量
			PartitionWater pw = partitionWaterService.getPartitionWater(currItem.getId());
			if(pw!=null) {
				isSettleWaterFee = false;//只结算水费（综价）
				
				//获取账单基础水费欠费金额
				BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(currItem.getDebitAssistant(), pw.getRealWaterAmount(), pw.getBasePrice());
				//获取账单污水处理费欠费金额
				BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(currItem.getDebitAssistant(), pw.getRealWaterAmount(), pw.getTreatmentFee());
				
				basePriceOweTotalAmount = basePriceOweAmount;//基础水费欠费金额
				treatmentFeeOweTotalAmount = treatmentFeeOweAmount;//污水处理费欠费金额
			}
			
		}else {//按客户结算
			
			settleType = EnumSettleType.SETTLE_TYPE_CUSTOMER.getKey();//结算类型-按账单结算
			currCustomerId = customerId;//当前结算的客户ID
			
			BigDecimal totalOweAmount = new BigDecimal(0.00);//客户水费欠费总金额
			BigDecimal baseOweTotalAmount = new BigDecimal(0.00);//客户基础水费欠费总金额
			BigDecimal treatOweTotalAmount = new BigDecimal(0.00);//客户污水处理费欠费总金额
			
			if(StringUtils.isBlank(period)) {
				//获取欠费账单（包含本期欠费账单和上期欠费账单）
				List<CustomerAccountItem> oweBillList = customerAccountItemService.getWaterFeeOweBillList(customerId, null);
				for(CustomerAccountItem oweBill : oweBillList) {
					
					//获取账单欠费金额
					BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(), oweBill.getDebitAmount());
					//获取总欠费金额
					totalOweAmount = BigDecimalUtils.add(totalOweAmount, billOweAmount);
					
					//查询分水量
					PartitionWater pw = partitionWaterService.getPartitionWater(oweBill.getId());
					if(pw!=null) {
						isSettleWaterFee = false;//只结算水费（综价）
						
						//获取账单基础水费欠费金额
						BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(oweBill.getDebitAssistant(), pw.getRealWaterAmount(), pw.getBasePrice());
						baseOweTotalAmount = BigDecimalUtils.add(baseOweTotalAmount, basePriceOweAmount);
						//获取账单污水处理费欠费金额
						BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(oweBill.getDebitAssistant(), pw.getRealWaterAmount(), pw.getTreatmentFee());
						treatOweTotalAmount = BigDecimalUtils.add(treatOweTotalAmount, treatmentFeeOweAmount);
					}
					
				}
				//查询欠费期间
				List<String> owePeriodList = customerAccountItemService.getWaterFeeOwePeriod(customerId, null);
				currPeriod = owePeriodList.toString();//当前结算的期间
			}else {
				currPeriod = period;//当前结算的期间
				//获取本期欠费账单
				List<CustomerAccountItem> oweBillList = customerAccountItemService.getWaterFeeOweBillList(customerId, period);
				for(CustomerAccountItem oweBill : oweBillList) {
					
					//获取账单欠费金额
					BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(), oweBill.getDebitAmount());
					//获取总欠费金额
					totalOweAmount = BigDecimalUtils.add(totalOweAmount, billOweAmount);
					
					//查询分水量
					PartitionWater pw = partitionWaterService.getPartitionWater(oweBill.getId());
					if(pw!=null) {
						isSettleWaterFee = false;//只结算水费（综价）
						
						//获取账单基础水费欠费金额
						BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(oweBill.getDebitAssistant(), pw.getRealWaterAmount(), pw.getBasePrice());
						baseOweTotalAmount = BigDecimalUtils.add(baseOweTotalAmount, basePriceOweAmount);
						//获取账单污水处理费欠费金额
						BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(oweBill.getDebitAssistant(), pw.getRealWaterAmount(), pw.getTreatmentFee());
						treatOweTotalAmount = BigDecimalUtils.add(treatOweTotalAmount, treatmentFeeOweAmount);
					}
					
				}
				//获取往期欠费账单
				List<CustomerAccountItem> pastOweBillList = customerAccountItemService.getPastWaterFeeOweBillList(customerId, period);
				for(CustomerAccountItem pastOweBill : pastOweBillList) {
					
					//获取账单欠费金额
					BigDecimal billOweAmount = BigDecimalUtils.subtract(pastOweBill.getCreditAmount(), pastOweBill.getDebitAmount());
					//获取总欠费金额
					totalOweAmount = BigDecimalUtils.add(totalOweAmount, billOweAmount);
					
					//查询分水量
					PartitionWater pw = partitionWaterService.getPartitionWater(pastOweBill.getId());
					if(pw!=null) {
						isSettleWaterFee = false;//只结算水费（综价）
						
						//获取账单基础水费欠费金额
						BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(pastOweBill.getDebitAssistant(), pw.getRealWaterAmount(), pw.getBasePrice());
						baseOweTotalAmount = BigDecimalUtils.add(baseOweTotalAmount, basePriceOweAmount);
						//获取账单污水处理费欠费金额
						BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(pastOweBill.getDebitAssistant(), pw.getRealWaterAmount(), pw.getTreatmentFee());
						treatOweTotalAmount = BigDecimalUtils.add(treatOweTotalAmount, treatmentFeeOweAmount);
					}
					
				}
				
			}
			
			oweTotalAmount = totalOweAmount;//欠费总金额
			basePriceOweTotalAmount = baseOweTotalAmount;//基础水费欠费总金额
			treatmentFeeOweTotalAmount = treatOweTotalAmount;//污水处理费欠费总金额
			
		}
		//查询客户
		Customers customer = customersService.selectByPrimaryKey(currCustomerId);
		
		//List<EnumAiDebitSubjectAction> subjectActionList = EnumAiDebitSubjectAction.getSettleType();;//缴费时缴费类型
		List<EnumAiDebitSubjectAction> subjectActionList = new ArrayList<>();
		if(customer.getCustomerType()==EnumCustomerType.CUSTOMER_PEOPLE.getValue() || isSettleWaterFee) {//如果客户类型为个人用户，或只结算水费综价时缴费类型只有水费（综价）
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_WATER_FEE);
		}else {//如果客户类型为非个人用户时缴费类型为基础水费和污水处理费
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_WATER_FEE);
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_BASE_FEE);
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_TREATMENT_FEE);
		}
		
		model.addAttribute("settleType", settleType);//结算类型
		model.addAttribute("subjectActionList", subjectActionList);//缴费时缴费类型
		List<EnumAiDebitSubjectPayment> subjectPaymentList = EnumAiDebitSubjectPayment.getSettlePayment();//缴费时的支付方式
		model.addAttribute("subjectPaymentList", subjectPaymentList);//缴费时的支付方式
		
		model.addAttribute("customer", customer);//当前账单对应的客户信息
		model.addAttribute("period", currPeriod);//当前账单对应的客户信息
		model.addAttribute("totalOweAmount", oweTotalAmount);//当前账单总欠费金额
		model.addAttribute("basePriceOweAmount", basePriceOweTotalAmount);//当前账单基础水费欠费金额
		model.addAttribute("treatmentFeeOweAmount", treatmentFeeOweTotalAmount);//账单污水处理费欠费金额
		
		return TEMPLATE_PATH + "settle_account_dialog";
	}
	
	/**
	 * @Title: getOweAmount
	 * @Description: 获取欠费金额
	 * @param assistant
	 * @param waterAmount
	 * @param waterPrice
	 * @return 
	 */
	private BigDecimal getOweAmount(String assistant, BigDecimal waterAmount, BigDecimal waterPrice) {
		
		//计算基础水价总金额=水量*基础水价单价
		BigDecimal basePriceTotalAmount = BigDecimalUtils.multiply(waterAmount, waterPrice);
		
		BigDecimal payAmount = new BigDecimal(0.00);//基础水价已支付金额变量
		
		//assistant
		if(StringUtils.isNotBlank(assistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);
			
			for(AssistantBean assistantBean : assistantBeanList) {
				//String subject = assistantBean.getSubject();
				//if(subject.indexOf(subjectBase)!=-1) {
					BigDecimal amount = assistantBean.getAmount();
					payAmount = BigDecimalUtils.add(payAmount, amount); 
				//}
			}
		}
		//计算基础水费欠费金额
		BigDecimal basePriceOweAmount = BigDecimalUtils.subtract(basePriceTotalAmount, payAmount);
		return basePriceOweAmount;
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
	 * @Description: 结算
	 * @param model
	 * @param settleType
	 * @param accountItemId
	 * @param customerId
	 * @param period
	 * @param settleAmount
	 * @param subjectAction
	 * @param subjectPayment
	 * @return 
	 */
	@RequestMapping(value = "/balance-auto-settle", produces = "application/json")
	@ResponseBody
	//public Object balanceAutoSettle(Model model, Long customerId, String subjectAction, String subjectPayment) {
	public Object settleAccount(Model model, Integer settleType, Long accountItemId, Long customerId, String subjectAction) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}

		//获取缴费类型
		EnumAiDebitSubjectAction debitSubjectAction = EnumAiDebitSubjectAction.getSubjectAction(subjectAction);
		//获取支付方式-余额扣费
		EnumAiDebitSubjectPayment debitSubjectPayment = EnumAiDebitSubjectPayment.PAYMENT_BALANCE;
		
		int rows = 0;
		if(settleType==EnumSettleType.SETTLE_TYPE_BILL.getKey()) {//按账单结算
			rows = customerAccountItemService.balanceSettleAccount(accountItemId, userBean.getId(), debitSubjectAction, debitSubjectPayment, EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
		}else {//按客户结算
			rows = customerAccountItemService.balanceSettleCustomerBill(customerId, userBean.getId(), debitSubjectAction, debitSubjectPayment, EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
		}
		
		if (rows>0) {
			return RequestResultUtil.getResultSaveSuccess("余额扣费成功！");
		}else if(rows==-1 || rows==-2) {
			return RequestResultUtil.getResultSaveSuccess("客户余额不足！");
		}

		return RequestResultUtil.getResultSaveWarn("余额扣费异常，请重新尝试！");

	}
	
	
	/**
	 * @Title: batchSettleAccount
	 * @Description: 批量余额扣费
	 * @param settleType
	 * @param accountItemId
	 * @param customerId
	 * @param subjectAction
	 * @return 
	 */
	@RequestMapping(value = "/batch-balance-auto-settle", produces = "application/json")
	@ResponseBody
	public Object batchSettleAccount(String searchCond, String traceIds) {
		Long operatorId = this.getOperatorId();
		int successNum = 0;
		int failNum = 0;
		//获取缴费类型
		EnumAiDebitSubjectAction debitSubjectAction = EnumAiDebitSubjectAction.PAY_WATER_FEE;
		//获取支付方式-余额扣费
		EnumAiDebitSubjectPayment debitSubjectPayment = EnumAiDebitSubjectPayment.PAYMENT_BALANCE;
		//获取有余额的客户
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.getCustomerBalanceList(searchCond, traceIds);
		if(accountItemMapList==null || accountItemMapList.size()<=0) {
			return RequestResultUtil.getResultFail("未查询到有余额的客户！");
		}
		for(Map<String, Object> map : accountItemMapList) {
			Long customerId = Long.valueOf(map.get("CUSTOMER_ID").toString());
			
			Customers customer = customersService.selectByPrimaryKey(customerId);
			//TODO 余额扣费批量处理时现在只支持个人客户的余额扣费，后续需要增加大表的余额扣费处理
			if(customer!=null && customer.getCustomerType()==EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {//如果是个人客户
				//按客户结算
				int rows = customerAccountItemService.balanceSettleCustomerBill(customerId, operatorId, debitSubjectAction, debitSubjectPayment, EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
				if(rows > 0 ) {
					successNum += 1;
				} else {
					failNum += 1;
				}
			}
		}
//		return RequestResultUtil.getResultSuccess("余额扣费成功 "+successNum+"条；"+"余额扣费失败 "+failNum+"条;");
		return RequestResultUtil.getResultSuccess("余额扣费批量处理完成！");

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
		return JSON.toJSONString(RequestResultUtil.getResultFail("发送短信通知异常！请重试！"));
	}
	
	//------------------------------	导出Excel	------------------------------
	/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-balance-excel")
	public void exportBalanceExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, Integer balanceStatus, String traceIds) {
		
		//excel标题
		String[] titles = { "地理位置", "客户姓名", "余额", "欠费期间", "欠费总金额"};
		//excel列名
		String[] columnNames = { "place", "customerName", "balance", "owePeriodList", "totalOweAmount"};
		//sheet名
		String place = locationService.getPlace(traceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
		String sheetName = place+"余额记录";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, balanceStatus, traceIds);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//获取导出EXCEL的文件名
		String fileName = this.getFileName(null, traceIds);
		
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
	private List<Map<String, Object>> getExportExcelData(String searchCond, Integer balanceStatus, String traceIds){
		List<Map<String, Object>> recordMapList = new ArrayList<>();
		recordMapList = customerAccountItemService.getCustomerBalanceStatusList(searchCond, traceIds,balanceStatus);
		
		
		for(Map<String, Object> recordMap : recordMapList) {
			
			String place = locationService.getPlace(traceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			recordMap.put("place", place);
			if(recordMap.get("CUSTOMER_NAME") != null) {
				recordMap.put("customerName", recordMap.get("CUSTOMER_NAME").toString());
			}
			if(recordMap.get("BALANCE") != null) {
				recordMap.put("balance", recordMap.get("BALANCE").toString());
			}
			if(recordMap.get("OWE_AMOUNT") != null) {
				recordMap.put("totalOweAmount",recordMap.get("OWE_AMOUNT").toString());
			}
			Long customerId = Long.valueOf(recordMap.get("CUSTOMER_ID").toString());
			//查询欠费期间
			List<String> owePeriodList = customerAccountItemService.getWaterFeeOwePeriod(customerId, null);
			recordMap.put("owePeriodList", owePeriodList);
			
		}
		
		return recordMapList;
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
		String fileName = times;
		
		if(StringUtils.isNotBlank(place)) {
			fileName = fileName+"-"+place;
		}
		
		fileName = fileName+"-"+times+"-"+"抄表记录"+".xls";
		
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
	
}