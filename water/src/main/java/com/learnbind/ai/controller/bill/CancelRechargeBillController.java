package com.learnbind.ai.controller.bill;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.AssistantBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
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
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.sms.SMSService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: CancelRechargeBillController.java
 * @Description: 销账撤销
 *
 * @author Thinkpad
 * @date 2019年11月25日 下午4:53:27
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/cancel-recharge-account")
public class CancelRechargeBillController {
	private static Log log = LogFactory.getLog(CancelRechargeBillController.class);
	private static final String TEMPLATE_PATH = "bill/cancel_recharge/"; // 页面目录
	private static final String TEMPLATE_PATH_BILL = "bill/cancel_recharge/insert_bill/"; // 增加账单
	private static final String TEMPLATE_PATH_CUSTOMER_BILL = "bill/cancel_recharge/customerbill/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

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
	private LocationCustomerService locationCustomerService;// 地理位置-客户
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
	private MetersService metersService;// 用水性质

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

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		// List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		// 如果他是欠费管理，查询除交易成功的账单
		// accountItemMapList =
		// customerAccountItemService.searchCustomerArrearsAccountItem(period, traceIds,
		// settlementStatus, searchCond, operatorId, startDate, endDate);
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period,
				traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {

			String accountItemIdStr = accountItemMap.get("ID").toString();// 账单ID
			Long accountItemId = Long.valueOf(accountItemIdStr);// 账单ID
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
			Long customerId = Long.valueOf(customerIdStr);// 客户ID
			// String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();// 记账日期
			// String currTraceIds =
			// accountItemMap.get("TRACE_IDS").toString();//地理位置traceIds
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());// 账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			PartitionWater part = partitionWaterService.getPartitionWater(accountItemId);
			String meterDesc = "";// 表计描述
			if (part != null) {
				log.debug("----------分水量记录ID：" + part.getId() + "，表计ID：" + part.getMeterId());
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(part.getMeterId()));
				meterDesc = meter.getDescription();
			}

			// 获取客户类型
			Integer customerType = Integer.valueOf(accountItemMap.get("CUSTOMER_TYPE").toString());
			String customerName = (String) accountItemMap.get("CUSTOMER_NAME");

			if (customerType == EnumCustomerType.CUSTOMER_UNIT.getValue() && StringUtils.isNotBlank(meterDesc)) {// 如果是单位用户，且表计描述不为空
				customerName = customerName + "（" + meterDesc + "）";
			}

			// 计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			// 计算违约金欠费总金额
			BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);

			String place = locationCustomerService.getPlace(customerId);
			accountItemMap.put("place", place);// 地理位置
			accountItemMap.put("meterPlace", customerName);// 地理位置

			accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);// 记账日期
			// accountItemMap.put("DEBIT_AMOUNT", zero);
			accountItemMap.put("overdueValue", overdueValue.setScale(2));// 违约金总金额

			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			// 计算总欠费金额，欠费金额+违约金欠款
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount.setScale(2));
			// 获取贷方摘要
			String creditSubject = "";
			if (accountItemMap.get("CREDIT_SUBJECT") != null) {
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

		// 统计本期账单
		String billTotalPrice = "";// 账单总金额
		String rechargeTotalPrice = "";// 已充值总金额
		// BigDecimal overdueTotalPrice = new BigDecimal(0.00);//违约金总金额
		String oweTotalPrice = "";// 欠费总金额
		// 获取统计数据
		Map<String, Object> statisticMap = customerAccountItemService.getCustomerAccountItemStatistic(period, traceIds,
				settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		if (statisticMap != null) {
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

		// 回传统计金额
		model.addAttribute("billTotalPrice", billTotalPrice);
		model.addAttribute("rechargeTotalPrice", rechargeTotalPrice);
		// model.addAttribute("overdueTotalPrice", overdueTotalPrice);
		model.addAttribute("oweTotalPrice", oweTotalPrice);

		return TEMPLATE_PATH + "account_table";
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
		model.addAttribute("waterPriceList", waterPriceList);// 初始化水价信息
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
	public String customerBillMain(Model model) {

		return TEMPLATE_PATH_CUSTOMER_BILL + "account_main";
	}

	/**
	 * @Title: customerRechargeBillTable
	 * @Description: 根据水费账单ID查询对应的充值记录
	 * @param model
	 * @param accountItemId
	 * @return 
	 */
	@RequestMapping(value = "/customer-recharge-bill-table")
	public String customerRechargeBillTable(Model model, Long waterFeeBillId) {

		// BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0

		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		// Long operatorId = this.getOperatorId();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式化
		
		List<Map<String, Object>> billMapList = new ArrayList<>();// 返回的账单集合
		if(waterFeeBillId!=null) {
			CustomerAccountItem waterFeeBill = customerAccountItemService.selectByPrimaryKey(waterFeeBillId);
			//解析借方辅助核算
			List<AssistantBean> assistantBeanList = new ArrayList<>();
			if(waterFeeBill.getDebitAssistant() != null) {
				assistantBeanList = JSON.parseArray(waterFeeBill.getDebitAssistant(), AssistantBean.class);
			}
			
			List<CustomerAccountItem> rechargeBillList = new ArrayList<>();
			for(AssistantBean bean : assistantBeanList) {
				Long rechargeBillId = bean.getId();
				CustomerAccountItem rechargeBill = customerAccountItemService.selectByPrimaryKey(rechargeBillId);
				rechargeBillList.add(rechargeBill);
			}
			
			for (CustomerAccountItem rechargeBill : rechargeBillList) {
				//Map<String, Object> rechargeBillMap = EntityUtils.entityToMap(rechargeBillList);
				Long rechargeBillId = rechargeBill.getId();//充值记录ID
				String debitSubject = rechargeBill.getDebitSubject();//借方科目
				BigDecimal debitAmount = rechargeBill.getDebitAmount();//借方金额
				String debitDigest = rechargeBill.getDebitDigest();//借方摘要
				Date accountDate = rechargeBill.getAccountDate();//记账日期
				
				String debitSubjectStr = AiSubjectUtils.getAiSubjectStr(debitSubject);
				String accountDateStr = sdf.format(accountDate);
				
				
				Map<String, Object> billMap = new HashMap<>();
				billMap.put("rechargeBillId", rechargeBillId);
				billMap.put("debitSubjectStr", debitSubjectStr);//借方科目
				billMap.put("debitAmount", debitAmount);//借方金额
				billMap.put("debitDigest", debitDigest);//借方摘要
				billMap.put("accountDateStr", accountDateStr);//记账日期
				billMapList.add(billMap);
			}
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("rechargeBillList", billMapList); // 列表数据

		return TEMPLATE_PATH_CUSTOMER_BILL + "recharge_bill_table";
	}
	
	/**
	 * @Title: customerWaterFeeBillTable
	 * @Description: 根据充值记录ID查询对应水费账单
	 * @param model
	 * @param rechargeBillId
	 * @return 
	 */
	@RequestMapping(value = "/customer-water-fee-bill-table")
	public String customerWaterFeeBillTable(Model model, Long rechargeBillId) {

		// BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0

		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		// Long operatorId = this.getOperatorId();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式化
		
		List<Map<String, Object>> billMapList = new ArrayList<>();// 返回的账单集合
		BigDecimal totalCreditAmount = new BigDecimal(0.00);//合计金额
		if(rechargeBillId!=null) {
			//查询充值记录
			CustomerAccountItem account = customerAccountItemService.selectByPrimaryKey(rechargeBillId);
			//解析贷方辅助核算
			List<AssistantBean> assistantBeanList = new ArrayList<>();
			if(account.getCreditAssistant() != null) {
				assistantBeanList = JSON.parseArray(account.getCreditAssistant(), AssistantBean.class);
			}
			
			List<CustomerAccountItem> waterFeeBillList = new ArrayList<>();
			for(AssistantBean bean : assistantBeanList) {
				Long waterFeeBillId = bean.getId();
				CustomerAccountItem waterFeeBill = customerAccountItemService.selectByPrimaryKey(waterFeeBillId);
				waterFeeBillList.add(waterFeeBill);
			}

			for (CustomerAccountItem waterFeeBill : waterFeeBillList) {
				//Map<String, Object> rechargeBillMap = EntityUtils.entityToMap(rechargeBillList);
				Long waterFeeBillId = waterFeeBill.getId();//充值记录ID
				String creditSubject = waterFeeBill.getCreditSubject();//贷方科目
				BigDecimal creditAmount = waterFeeBill.getCreditAmount();//贷方金额
				String creditDigest = waterFeeBill.getCreditDigest();//贷方摘要
				Date accountDate = waterFeeBill.getAccountDate();//记账日期
				
				String creditSubjectStr = AiSubjectUtils.getAiSubjectStr(creditSubject);
				String accountDateStr = sdf.format(accountDate);
				
				totalCreditAmount = BigDecimalUtils.add(totalCreditAmount, creditAmount);//合计金额
				
				Map<String, Object> billMap = new HashMap<>();
				billMap.put("waterFeeBillId", waterFeeBillId);
				billMap.put("creditSubjectStr", creditSubjectStr);//借方科目
				billMap.put("creditAmount", creditAmount);//借方金额
				billMap.put("creditDigest", creditDigest);//借方摘要
				billMap.put("accountDateStr", accountDateStr);//记账日期
				billMapList.add(billMap);
			}
		}

		// 传递如下数据至前台页面
		model.addAttribute("waterFeeBillList", billMapList);//列表数据
		model.addAttribute("totalCreditAmount", totalCreditAmount);//合计金额

		return TEMPLATE_PATH_CUSTOMER_BILL + "water_fee_bill_table";
	}
	
	/**
	 * @Title: cancelRecharge
	 * @Description: 撤销充值
	 * @param accountItemId
	 * @param arrearsAccountItemId
	 * @return 
	 */
	@RequestMapping(value = "/cancel-recharge-bill", produces = "application/json")
	@ResponseBody
	public Object cancelRecharge(Long rechargeBillId) {
		Long operatorId = this.getOperatorId();
		//撤销充值账单
		int rows = customerAccountItemService.cancelRechargeBill(rechargeBillId, operatorId);
		if (rows > 0)
			return RequestResultUtil.getResultSaveSuccess("账单撤销成功！");
		else
			return RequestResultUtil.getResultSaveWarn("账单撤销失败！");
	}
	
	/**
	 * @Title: deleteRecharge
	 * @Description: 删除充值
	 * @param accountItemId
	 * @param arrearsAccountItemId
	 * @return 
	 */
//	@RequestMapping(value = "/delete-recharge", produces = "application/json")
//	@ResponseBody
//	public Object deleteRecharge(Long accountItemId, Long arrearsAccountItemId) {
//		Long operatorId = this.getOperatorId();
//		//撤销充值账单
//		int rows = customerAccountItemService.deleteRecharge(accountItemId, arrearsAccountItemId, operatorId);
//		
//		if (rows > 0)
//			return RequestResultUtil.getResultSaveSuccess("账单删除成功！");
//		else
//			return RequestResultUtil.getResultSaveWarn("账单删除失败！");
//	}
	
	@RequestMapping(value = "/clean-error-data", produces = "application/json")
	@ResponseBody
	public Object uodateAccount(String period, String traceIds,
			Integer settlementStatus, Integer accountStatus, String searchCond, String startDate, String endDate) {
		Long operatorId = this.getOperatorId();
		int rows = 0 ;
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period,
				traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		for (Map<String, Object> accountItemMap : accountItemMapList) {

			String accountItemIdStr = accountItemMap.get("ID").toString();// 账单ID
			Long accountItemId = Long.valueOf(accountItemIdStr);// 账单ID
			rows = customerAccountItemService.cleanRecharge(accountItemId, operatorId);
			
		}

		if (rows > 0)
			return RequestResultUtil.getResultSaveSuccess("账单撤销成功！");
		else
			return RequestResultUtil.getResultSaveWarn("账单撤销失败！");
	}

	/*
	 * @Title: getOperatorId
	 * 
	 * @Description: 根据角色获取当前用户ID
	 * 
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