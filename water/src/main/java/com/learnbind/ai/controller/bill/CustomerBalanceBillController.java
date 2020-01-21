package com.learnbind.ai.controller.bill;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumBindStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerAccountItemLogType;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumSettleType;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiTraceOperate;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerAccountItemLog;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysUsers;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemLogService;
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
import com.learnbind.ai.service.user.UsersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.sms.SMSConstants;
import com.learnbind.ai.sms.SMSService;
import com.learnbind.ai.sms.SendSingleSMResponse;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: CustomerBalanceBillController.java
 * @Description: 余额查询
 *
 * @author Thinkpad
 * @date 2019年11月25日 下午11:44:49
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/customer-balance-bill")
public class CustomerBalanceBillController {
	private static Log log = LogFactory.getLog(CustomerBalanceBillController.class);
	private static final String TEMPLATE_PATH = "bill/balance_bill/"; // 页面目录
	private static final String TEMPLATE_PATH_BILL = "bill/balance_bill/insert_bill/"; // 增加账单
	private static final String TEMPLATE_PATH_CUSTOMER_BILL = "bill/balance_bill/customerbill/"; // 页面目录

	@Autowired
	private UploadFileConfig uploadFileConfig;// 文件配置
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
	private LocationService locationService;// 地理位置
	@Autowired
	private BankService bankService;// 客户银行信息
	@Autowired
	private CustomerMeterService customerMeterService;// 客户-表计绑定关系表
	@Autowired
	private SMSService smsService;// 发送短信
	@Autowired
	private PartitionWaterService partitionWaterService;// 分水量
	@Autowired
	private WaterPriceService waterPriceService;// 用水性质
	@Autowired
	private LocationCustomerService locationCustomerService;// 地理位置-客户
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录信息
	@Autowired
	private UsersService usersService;//用户信息
	@Autowired
	public CustomerAccountItemLogService customerAccountItemLogService;//账单日志

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

		// 查询小区
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
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String period, String traceIds,
			Integer settlementStatus, Integer accountStatus, String searchCond, String startDate, String endDate) {

		BigDecimal zero = new BigDecimal("0");// 初始化BigDecimal类型的0

		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		PageHelper.startPage(pageNum, pageSize); // PageHelper}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Customers> customerList = new ArrayList<>();
		if (StringUtils.isNotBlank(traceIds)) {// 如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			customerList = customersService.getCustomersList(searchCond, traceIds, null, null);
		} else {
			customerList = customersService.searchCustomers(searchCond, null, null);
		}
		PageInfo<Customers> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for (Customers customer : customerList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			BigDecimal balanceAmounnt = customerAccountItemService.getCustomerBalance(customer.getId());
			customerMap.put("balanceAmounnt", balanceAmounnt);
			customerMapList.add(customerMap);
		}

		// 传递如下数据至前台页面
		model.addAttribute("customersList", customerMapList); // 列表数据
		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		return TEMPLATE_PATH + "account_table";
	}

	/*
	 * @Title: loadInsertAccountItem
	 * 
	 * @Description: 加载增加账单选项卡
	 * 
	 * @param model
	 * 
	 * @return
	 */
	@RequestMapping(value = "/load-account-item-insert")
	public String loadInsertAccountItem(Model model, Long customerId, String customerName) {
		LocationCustomer lm = locationCustomerService.getLocationByCustomerId(customerId);
		String place = null;
		if(lm!=null) {
			place = locationService.getPlace(lm.getTraceIds());
		}
		
		model.addAttribute("place", place);// 客户位置
		model.addAttribute("customerId", customerId);// 客户ID
		model.addAttribute("customerName", customerName);// 客户名称
		
		List<SysWaterPrice> waterPriceList = waterPriceService.selectAll();
		model.addAttribute("waterPriceList", waterPriceList);// 初始化水价信息
		//boolean isSettleWaterFee = true;// 只结算水费（综价）
		List<EnumAiDebitSubjectAction> subjectActionList = new ArrayList<>();
//		if (customer.getCustomerType() == EnumCustomerType.CUSTOMER_PEOPLE.getValue() || isSettleWaterFee) {// 如果客户类型为个人用户，或只结算水费综价时缴费类型只有水费（综价）
//			subjectActionList.add(EnumAiDebitSubjectAction.PAY_WATER_FEE);
//		} else {// 如果客户类型为非个人用户时缴费类型为基础水费和污水处理费
			//预存水费
			subjectActionList.add(EnumAiDebitSubjectAction.ADVANCE_WATER_FEE);
			//subjectActionList.add(EnumAiDebitSubjectAction.PAY_BASE_FEE);
			//subjectActionList.add(EnumAiDebitSubjectAction.PAY_TREATMENT_FEE);
//		}

		model.addAttribute("subjectActionList", subjectActionList);// 缴费时缴费类型
		List<EnumAiDebitSubjectPayment> subjectPaymentList = EnumAiDebitSubjectPayment.getSettlePayment();// 缴费时的支付方式
		model.addAttribute("subjectPaymentList", subjectPaymentList);// 缴费时的支付方式
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

		// 查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		model.addAttribute("customer", null);// 初始化客户信息

		return TEMPLATE_PATH_BILL + "customer_item_main";
	}

	@RequestMapping(value = "/customer-item-table")
	public String customerItemTable(Model model, Integer pageNum, Integer pageSize, String searchCond,
			String traceIds) {

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
		for (Map<String, Object> map : customerList) {
			String customerId = map.get("CUSTOMER_ID").toString();
			String currTraceIds = map.get("TRACE_IDS").toString();
			Customers customer = customersService.selectByPrimaryKey(Long.valueOf(customerId));
			String place = locationService.getPlace(currTraceIds);

			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("place", place);
			customerMapList.add(customerMap);
			// customer.put("waterUseValue",
			// this.getPriceTypeName(customer.get("WATER_USE").toString()));//

		}

		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
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
		if (priceTypeName != null) {
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
		if (lm != null) {
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
	public Object addAccountItem(Long customerId, String period, Integer debitCredit, String digest, Long waterPriceId,
			BigDecimal amount) {

		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		//根据客户id获取账户id
		Long accountId = customerAccountService.getAccountIdByCustomerName(customerId);
		if(accountId==null || (accountId!=null && accountId<=0)) {
			return RequestResultUtil.getResultFail("该客户未立户不允许预存水费!");
		}
		
		int row = customerAccountItemService.insertCustomerAccountItem(customerId, period, debitCredit, digest,
				waterPriceId, amount, userBean.getId());
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}

	// -----------------------------加载客户档案明细--------------------------------------------------------------------------
	/**
	 * @Title: main
	 * @Description: 加载客户档案明细主页面
	 * @param model
	 * @param customerId
	 * @return
	 */
	@RequestMapping(value = "/customer-bill-main")
	public String customerBillMain(Model model, Long customerId) {

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
	public String customerBillTable(Model model, Integer pageNum, Integer pageSize, Long customerId, Integer isBalance,String startDate, String endDate) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerAccountItem> itemList = new ArrayList<>();
		if(isBalance == null) {//查询所有充值账单
			itemList = customerAccountItemService.getAllRechargeBill(customerId, startDate, endDate);
		} else if(isBalance == 1){//查询所有有余额的账单
			itemList = customerAccountItemService.getBalanceRechargeBill(customerId, startDate, endDate);
		}
		
		
		PageInfo<CustomerAccountItem> pageInfo = new PageInfo<>(itemList);// (使用了拦截器或是AOP进行查询的再次处理)

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 时间格式化
		List<Map<String, Object>> billMapList = new ArrayList<>();// 返回的账单集合
		for (CustomerAccountItem item : itemList) {

			Map<String, Object> billMap = EntityUtils.entityToMap(item);

			// 获取贷方科目
			String creditSubject = AiSubjectUtils.getAiSubjectStr(item.getDebitSubject());
			billMap.put("creditSubject", creditSubject);

			// 记账日期
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
		// model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH_CUSTOMER_BILL + "account_table";
	}
	
	/**
	 * @Title: deleteRechargeBill
	 * @Description: 删除贷方金额为0的充值记录
	 * @param accountItemId
	 * @return 
	 */
	@RequestMapping(value = "/delete-recharge-bill", produces = "application/json")
	@ResponseBody
	public Object deleteRechargeBill(Long accountItemId) {
		Long operatorId = this.getOperatorId();
		//查找到这条充值记录
		CustomerAccountItem account = customerAccountItemService.selectByPrimaryKey(accountItemId);
		account.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		int rows = customerAccountItemService.updateByPrimaryKeySelective(account);
		if(rows > 0) {
			//删除清欠充值记录
			customerAccountItemService.deleteDebitBill(accountItemId);
			//增加日志
			CustomerAccountItemLog log = new CustomerAccountItemLog();
			log.setCustomerId(account.getCustomerId());
			log.setAccountItemId(accountItemId);
			log.setAmount(account.getDebitAmount());
			log.setType(EnumCustomerAccountItemLogType.DELETE_RECHARGE.getValue());
			log.setOperatorId(operatorId);
			log.setOperationTime(new Date());
			log.setRemark("删除充值记录金额为："+account.getDebitAmount());
			customerAccountItemLogService.insertSelective(log);
			return RequestResultUtil.getResultSaveSuccess("删除充值记录成功！");
		}

		return RequestResultUtil.getResultSaveWarn("删除充值记录失败！");

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
			// 计算账单的欠费金额
			BigDecimal billOweAmount = BigDecimalUtils.subtract(accountItem.getCreditAmount(),
					accountItem.getDebitAmount());

			// 计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(id);
			// 计算已充值违约金总金额
			BigDecimal rechargeOverdueValue = customerAccountItemService.getRechargeOverdueValueSum(id);
			// 计算违约金欠费金额 = 违约金总金额-已充值违约金总金额
			BigDecimal overdueOweAmount = BigDecimalUtils.subtract(overdueValue, rechargeOverdueValue);
			// 计算欠费金额=账单欠费金额+违约金欠费金额
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
			// 获取账户名称
			// Long accountId = accountItem.getAccountId();
			// CustomerAccount account =
			// customerAccountService.selectByPrimaryKey(accountId);
			// customerAccountItemMap.put("accountName", account.getAccountName());
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

	// ------------------------结算账单-------------------
	/**
	 * @Title: loadSettleAccountDialog
	 * @Description: 结算账单对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadsettleaccountdialog")
	public String loadSettleAccountDialog(Long customerId, String period, Long accountItemId, Model model) {

		Long currCustomerId = null;// 当前结算的客户ID
		String currPeriod = null;// 当前结算的期间
		BigDecimal oweTotalAmount = new BigDecimal(0.00);// 总欠费金额
		BigDecimal basePriceOweTotalAmount = new BigDecimal(0.00);// 基础水费欠费总金额
		BigDecimal treatmentFeeOweTotalAmount = new BigDecimal(0.00);// 污水处理费欠费总金额

		boolean isSettleWaterFee = true;// 只结算水费（综价）
		Integer settleType = EnumSettleType.SETTLE_TYPE_BILL.getKey();// 结算类型，默认按账单结算
		if (accountItemId != null) {// 按账单结算
			settleType = EnumSettleType.SETTLE_TYPE_BILL.getKey();// 结算类型-按账单结算
			// 读取需要编辑的条目
			CustomerAccountItem currItem = customerAccountItemService.selectByPrimaryKey(accountItemId);
			currCustomerId = currItem.getCustomerId();// 当前结算的客户ID
			currPeriod = currItem.getPeriod();// 当前结算的期间
			// 获取账单欠费金额
			BigDecimal billOweAmount = BigDecimalUtils.subtract(currItem.getCreditAmount(), currItem.getDebitAmount());
			// 获取违约金金额之和
			// BigDecimal overdueOweAmountSum =
			// customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			// 计算总欠费金额=账单欠费金额+违约金金额之和
			BigDecimal totalOweAmount = billOweAmount;// BigDecimalUtils.add(billOweAmount, overdueOweAmountSum);
			oweTotalAmount = totalOweAmount;// 欠费金额

			// 查询分水量
			PartitionWater pw = partitionWaterService.getPartitionWater(currItem.getId());
			if (pw != null) {
				isSettleWaterFee = false;// 只结算水费（综价）

				// 获取账单基础水费欠费金额
				BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(currItem.getDebitAssistant(),
						pw.getRealWaterAmount(), pw.getBasePrice());
				// 获取账单污水处理费欠费金额
				BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(currItem.getDebitAssistant(),
						pw.getRealWaterAmount(), pw.getTreatmentFee());

				basePriceOweTotalAmount = basePriceOweAmount;// 基础水费欠费金额
				treatmentFeeOweTotalAmount = treatmentFeeOweAmount;// 污水处理费欠费金额
			}

		} else {// 按客户结算

			settleType = EnumSettleType.SETTLE_TYPE_CUSTOMER.getKey();// 结算类型-按账单结算
			currCustomerId = customerId;// 当前结算的客户ID

			BigDecimal totalOweAmount = new BigDecimal(0.00);// 客户水费欠费总金额
			BigDecimal baseOweTotalAmount = new BigDecimal(0.00);// 客户基础水费欠费总金额
			BigDecimal treatOweTotalAmount = new BigDecimal(0.00);// 客户污水处理费欠费总金额

			if (StringUtils.isBlank(period)) {
				// 获取欠费账单（包含本期欠费账单和上期欠费账单）
				List<CustomerAccountItem> oweBillList = customerAccountItemService.getWaterFeeOweBillList(customerId,
						null);
				for (CustomerAccountItem oweBill : oweBillList) {

					// 获取账单欠费金额
					BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(),
							oweBill.getDebitAmount());
					// 获取总欠费金额
					totalOweAmount = BigDecimalUtils.add(totalOweAmount, billOweAmount);

					// 查询分水量
					PartitionWater pw = partitionWaterService.getPartitionWater(oweBill.getId());
					if (pw != null) {
						isSettleWaterFee = false;// 只结算水费（综价）

						// 获取账单基础水费欠费金额
						BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(oweBill.getDebitAssistant(),
								pw.getRealWaterAmount(), pw.getBasePrice());
						baseOweTotalAmount = BigDecimalUtils.add(baseOweTotalAmount, basePriceOweAmount);
						// 获取账单污水处理费欠费金额
						BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(oweBill.getDebitAssistant(),
								pw.getRealWaterAmount(), pw.getTreatmentFee());
						treatOweTotalAmount = BigDecimalUtils.add(treatOweTotalAmount, treatmentFeeOweAmount);
					}

				}
				// 查询欠费期间
				List<String> owePeriodList = customerAccountItemService.getWaterFeeOwePeriod(customerId, null);
				currPeriod = owePeriodList.toString();// 当前结算的期间
			} else {
				currPeriod = period;// 当前结算的期间
				// 获取本期欠费账单
				List<CustomerAccountItem> oweBillList = customerAccountItemService.getWaterFeeOweBillList(customerId,
						period);
				for (CustomerAccountItem oweBill : oweBillList) {

					// 获取账单欠费金额
					BigDecimal billOweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(),
							oweBill.getDebitAmount());
					// 获取总欠费金额
					totalOweAmount = BigDecimalUtils.add(totalOweAmount, billOweAmount);

					// 查询分水量
					PartitionWater pw = partitionWaterService.getPartitionWater(oweBill.getId());
					if (pw != null) {
						isSettleWaterFee = false;// 只结算水费（综价）

						// 获取账单基础水费欠费金额
						BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(oweBill.getDebitAssistant(),
								pw.getRealWaterAmount(), pw.getBasePrice());
						baseOweTotalAmount = BigDecimalUtils.add(baseOweTotalAmount, basePriceOweAmount);
						// 获取账单污水处理费欠费金额
						BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(oweBill.getDebitAssistant(),
								pw.getRealWaterAmount(), pw.getTreatmentFee());
						treatOweTotalAmount = BigDecimalUtils.add(treatOweTotalAmount, treatmentFeeOweAmount);
					}

				}
				// 获取往期欠费账单
				List<CustomerAccountItem> pastOweBillList = customerAccountItemService
						.getPastWaterFeeOweBillList(customerId, period);
				for (CustomerAccountItem pastOweBill : pastOweBillList) {

					// 获取账单欠费金额
					BigDecimal billOweAmount = BigDecimalUtils.subtract(pastOweBill.getCreditAmount(),
							pastOweBill.getDebitAmount());
					// 获取总欠费金额
					totalOweAmount = BigDecimalUtils.add(totalOweAmount, billOweAmount);

					// 查询分水量
					PartitionWater pw = partitionWaterService.getPartitionWater(pastOweBill.getId());
					if (pw != null) {
						isSettleWaterFee = false;// 只结算水费（综价）

						// 获取账单基础水费欠费金额
						BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(pastOweBill.getDebitAssistant(),
								pw.getRealWaterAmount(), pw.getBasePrice());
						baseOweTotalAmount = BigDecimalUtils.add(baseOweTotalAmount, basePriceOweAmount);
						// 获取账单污水处理费欠费金额
						BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(
								pastOweBill.getDebitAssistant(), pw.getRealWaterAmount(), pw.getTreatmentFee());
						treatOweTotalAmount = BigDecimalUtils.add(treatOweTotalAmount, treatmentFeeOweAmount);
					}

				}

			}

			oweTotalAmount = totalOweAmount;// 欠费总金额
			basePriceOweTotalAmount = baseOweTotalAmount;// 基础水费欠费总金额
			treatmentFeeOweTotalAmount = treatOweTotalAmount;// 污水处理费欠费总金额

		}
		// 查询客户
		Customers customer = customersService.selectByPrimaryKey(currCustomerId);

		// List<EnumAiDebitSubjectAction> subjectActionList =
		// EnumAiDebitSubjectAction.getSettleType();;//缴费时缴费类型
		List<EnumAiDebitSubjectAction> subjectActionList = new ArrayList<>();
		if (customer.getCustomerType() == EnumCustomerType.CUSTOMER_PEOPLE.getValue() || isSettleWaterFee) {// 如果客户类型为个人用户，或只结算水费综价时缴费类型只有水费（综价）
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_WATER_FEE);
		} else {// 如果客户类型为非个人用户时缴费类型为基础水费和污水处理费
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_WATER_FEE);
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_BASE_FEE);
			subjectActionList.add(EnumAiDebitSubjectAction.PAY_TREATMENT_FEE);
		}

		model.addAttribute("settleType", settleType);// 结算类型
		model.addAttribute("subjectActionList", subjectActionList);// 缴费时缴费类型
		List<EnumAiDebitSubjectPayment> subjectPaymentList = EnumAiDebitSubjectPayment.getSettlePayment();// 缴费时的支付方式
		model.addAttribute("subjectPaymentList", subjectPaymentList);// 缴费时的支付方式

		model.addAttribute("customer", customer);// 当前账单对应的客户信息
		model.addAttribute("period", currPeriod);// 当前账单对应的客户信息
		model.addAttribute("totalOweAmount", oweTotalAmount);// 当前账单总欠费金额
		model.addAttribute("basePriceOweAmount", basePriceOweTotalAmount);// 当前账单基础水费欠费金额
		model.addAttribute("treatmentFeeOweAmount", treatmentFeeOweTotalAmount);// 账单污水处理费欠费金额

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

		// 计算基础水价总金额=水量*基础水价单价
		BigDecimal basePriceTotalAmount = BigDecimalUtils.multiply(waterAmount, waterPrice);

		BigDecimal payAmount = new BigDecimal(0.00);// 基础水价已支付金额变量

		// assistant
		if (StringUtils.isNotBlank(assistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);

			for (AssistantBean assistantBean : assistantBeanList) {
				// String subject = assistantBean.getSubject();
				// if(subject.indexOf(subjectBase)!=-1) {
				BigDecimal amount = assistantBean.getAmount();
				payAmount = BigDecimalUtils.add(payAmount, amount);
				// }
			}
		}
		// 计算基础水费欠费金额
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

		// 基础水价科目
		String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey() + EnumAiDebitSubjectAction.PAY_BASE_FEE.getKey();

		// 计算基础水价总金额=水量*基础水价单价
		BigDecimal basePriceTotalAmount = BigDecimalUtils.multiply(waterAmount, basePrice);

		BigDecimal payAmount = new BigDecimal(0.00);// 基础水价已支付金额变量

		// assistant
		if (StringUtils.isNotBlank(assistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);

			for (AssistantBean assistantBean : assistantBeanList) {
				String subject = assistantBean.getSubject();
				if (subject.indexOf(subjectBase) != -1) {
					BigDecimal amount = assistantBean.getAmount();
					payAmount = BigDecimalUtils.add(payAmount, amount);
				}
			}
		}
		// 计算基础水费欠费金额
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

		// 污水处理费科目
		String subjectBase = EnumAiDebitCreditStatus.DEBIT.getKey()
				+ EnumAiDebitSubjectAction.PAY_TREATMENT_FEE.getKey();

		// 计算污水处理费总金额=水量*污水处理费单价
		BigDecimal basePriceTotalAmount = BigDecimalUtils.multiply(waterAmount, treatmentFee);

		BigDecimal payAmount = new BigDecimal(0.00);// 污水处理费已支付金额变量

		// assistant
		if (StringUtils.isNotBlank(assistant)) {
			List<AssistantBean> assistantBeanList = JSON.parseArray(assistant, AssistantBean.class);

			for (AssistantBean assistantBean : assistantBeanList) {
				String subject = assistantBean.getSubject();
				if (subject.indexOf(subjectBase) != -1) {
					BigDecimal amount = assistantBean.getAmount();
					payAmount = BigDecimalUtils.add(payAmount, amount);
				}
			}
		}
		// 计算污水处理费欠费金额
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
	// public Object balanceAutoSettle(Model model, Long customerId, String
	// subjectAction, String subjectPayment) {
	public Object settleAccount(Model model, Integer settleType, Long accountItemId, Long customerId,
			String subjectAction) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}

		// 获取缴费类型
		EnumAiDebitSubjectAction debitSubjectAction = EnumAiDebitSubjectAction.getSubjectAction(subjectAction);
		// 获取支付方式-余额扣费
		EnumAiDebitSubjectPayment debitSubjectPayment = EnumAiDebitSubjectPayment.PAYMENT_BALANCE;

		int rows = 0;
		if (settleType == EnumSettleType.SETTLE_TYPE_BILL.getKey()) {// 按账单结算
			rows = customerAccountItemService.balanceSettleAccount(accountItemId, userBean.getId(), debitSubjectAction,
					debitSubjectPayment, EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
		} else {// 按客户结算
			rows = customerAccountItemService.balanceSettleCustomerBill(customerId, userBean.getId(),
					debitSubjectAction, debitSubjectPayment, EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
		}

		if (rows > 0) {
			return RequestResultUtil.getResultSaveSuccess("余额结算成功！");
		} else if (rows == -1) {
			return RequestResultUtil.getResultSaveSuccess("客户余额不足！");
		}

		return RequestResultUtil.getResultSaveWarn("余额结算异常，请重新尝试！");

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

	// ---------------------------------发送短信通知-----------------------------------------------------------------------------------------------
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

			BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型 0

			// 查询账单
			CustomerAccountItem item = customerAccountItemService.selectByPrimaryKey(accountItemId);
			// 计算账单欠费金额
			BigDecimal oweAmount = BigDecimalUtils.subtract(item.getCreditAmount(), item.getDebitAmount());

			// 如果欠费金额>0，则发送短信通知
			if (BigDecimalUtils.greaterThan(oweAmount, zero)) {
				// 发送短信通知
				// 查询客户信息
				Customers customer = customersService.selectByPrimaryKey(item.getCustomerId());

				// 客户手机号
				String mobileNo = customer.getPropMobile();
				if (StringUtils.isBlank(mobileNo)) {
					mobileNo = customer.getCustomerMobile();
				}
				// 验证发送短信的手机号码是否为空
				if (StringUtils.isBlank(mobileNo)) {
					return RequestResultUtil.getResultFail("客户手机号为空！");
				}

				// 指定模板所对应的参数值
				List<String> tpl_parms = new ArrayList<>();
				// 客户姓名
				String customerName = customer.getPropName();
				if (StringUtils.isBlank(customerName)) {
					customerName = customer.getCustomerName();
				}
				if (StringUtils.isNotBlank(customerName)) {
					tpl_parms.add(customerName);
				}

				String period = item.getPeriod();// 期间
				if (StringUtils.isNotBlank(period)) {
					tpl_parms.add(period);
				}
				tpl_parms.add(oweAmount.toString());// 账单金额

				// 如果手机号不为空，且模版参数正确，则发送短信
				if (StringUtils.isNotBlank(mobileNo) && tpl_parms.size() == 3) {
					String retJSON = smsService.sendSingleSMS(mobileNo, SMSConstants.SMS_TEMPLATE_ID_FEE, tpl_parms);
					if (StringUtils.isNotBlank(retJSON)) {
						SendSingleSMResponse ret = JSON.parseObject(retJSON, SendSingleSMResponse.class);
						if (ret.getResult() == 0) {
							return RequestResultUtil.getResultSuccess("发送短信通知成功！");
						} else {
							return RequestResultUtil.getResultFail("发送短信通知失败，原因：" + ret.getErrmsg());
						}
					} else {
						return RequestResultUtil.getResultFail("发送短信通知失败，返回信息为空！");
					}
				} else {
					return RequestResultUtil.getResultFail("缺少参数！");
				}

			} else {
				return RequestResultUtil.getResultFail("此账单已全部结算，不需要发送短信通知！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return JSON.toJSONString(RequestResultUtil.getResultFail("发送短信通知异常！请重试！"));
	}

}