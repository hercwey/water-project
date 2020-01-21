package com.learnbind.ai.controller.bill;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.SendSmsLog;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.sendlog.SendSmsLogService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.service.wechatuser.WechatCustomerService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: SendNotifyController.java
 * @Description: 发送通知
 *
 * @author Administrator
 * @date 2020年1月15日 下午5:45:22
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/send-notify")
public class SendNotifyController {
	private static Log log = LogFactory.getLog(SendNotifyController.class);
	private static final String TEMPLATE_PATH = "bill/sendnotify/"; // 页面目录
	private static final String TEMPLATE_PATH_CUSTOMER_BILL = "bill/sendnotify/customerbill/"; // 页面目录
	private static final String TEMPLATE_PATH_BILL = "bill/sendnotify/insert_bill/"; // 增加账单

	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService; // 客户信息
	@Autowired
	private LocationService locationService;// 地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;// 地理位置-客户
	@Autowired
	private WaterPriceService waterPriceService;// 用水性质
	@Autowired
	private WechatCustomerService wechatCustomerService;//微信-客户关系
	@Autowired
	private SendSmsLogService sendSmsLogService;//发送短信日志

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
			Integer settlementStatus, String searchCond, boolean searchBindWechatCustomer, boolean searchUnbindWechatCustomer, String startDate, String endDate) {

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
		// 结算失败
		Integer settleFail = EnumSettlementStatus.SETTLEMENT_FAIL.getValue();
		// 部分结算
		Integer settlePart = EnumSettlementStatus.SETTLEMENT_PART.getValue();
		// 未结算
		Integer settleNormal = EnumSettlementStatus.SETTLEMENT_NORMAL.getValue();
		List<Integer> settleStatusList = new ArrayList<>();
		settleStatusList.add(settlePart);
		settleStatusList.add(settleFail);
		settleStatusList.add(settleNormal);
		List<Map<String, Object>> accountItemMapList = new ArrayList<>();
		if(searchBindWechatCustomer && searchUnbindWechatCustomer) {//查询已绑定微信和未绑定微信的所有客户
			accountItemMapList = customerAccountItemService.getListGroupByCustomerId(searchCond, traceIds, period, null, startDate, endDate, settleStatusList);
		}else if(searchBindWechatCustomer && !searchUnbindWechatCustomer){//查询已绑定微信的客户
			accountItemMapList = customerAccountItemService.getWechatCustomerOweBillList(searchCond, traceIds, period, null, startDate, endDate, settleStatusList);
		}else if(!searchBindWechatCustomer && searchUnbindWechatCustomer){//查询未绑定微信的客户
			accountItemMapList = customerAccountItemService.getNotWechatCustomerOweBillList(searchCond, traceIds, period, null, startDate, endDate, settleStatusList);
		}
		
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {

			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
			Long customerId = Long.valueOf(customerIdStr);// 客户ID
//					String billIds = accountItemMap.get("BILL_IDS").toString();//账单ID集合,多个账单ID用逗号","分隔
			String currPeriod = accountItemMap.get("PERIOD").toString();// 期间

			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());// 账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）

			String currTraceIds = (String) accountItemMap.get("TRACE_IDS");// 地理位置

			String place = locationService.getPlace(currTraceIds);
			accountItemMap.put("place", place);// 地理位置

			// 查询往期水费欠费金额
			BigDecimal pastWaterFeeOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customerId,
					currPeriod);
			// 查询往期违约金欠费金额
			// BigDecimal pastOverdueOweAmount =
			// customerAccountItemService.getPastOverdueOweAmount(customerId, currPeriod);
			// 往期欠费总金额=往期水费欠费金额+往期违约金欠费金额
			BigDecimal pastOweTotalAmount = pastWaterFeeOweAmount;// BigDecimalUtils.add(pastWaterFeeOweAmount, pastOverdueOweAmount);
			accountItemMap.put("pastOweTotalAmount", pastOweTotalAmount);

			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			// 计算总欠费金额，本期欠费金额+往期欠费金额
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweTotalAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);
			//是否绑定微信
			boolean isWxCustomer = false;
			WechatUser wu = wechatCustomerService.getWechatCustomer(customerId);
			if(wu!=null) {
				isWxCustomer = true;
			}
			accountItemMap.put("isWxCustomer", isWxCustomer);
			//是否发送过短息
			boolean isSendSms = false;
			SendSmsLog log = sendSmsLogService.getSmsLog(customerId, period);
			if(log!=null) {
				isSendSms = true;
			}
			accountItemMap.put("isSendSms", isSendSms);
		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH + "account_table";
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

//			//查询客户
//			Customers customer = customersService.selectByPrimaryKey(customerId);
//			String customerName = "";
//			if(customer!=null) {
//				customerName = customer.getCustomerName();
//			}
//			model.addAttribute("customerName", customerName);

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
	public String customerBillTable(Model model, Integer pageNum, Integer pageSize, Long customerId, String period,
			Integer settlementStatus) {

		// BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0

		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		// Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerAccountItem> itemList = customerAccountItemService.getCustomerAccountItemList(period, customerId,
				settlementStatus);
		PageInfo<CustomerAccountItem> pageInfo = new PageInfo<>(itemList);// (使用了拦截器或是AOP进行查询的再次处理)

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 时间格式化
		List<Map<String, Object>> billMapList = new ArrayList<>();// 返回的账单集合
		for (CustomerAccountItem item : itemList) {

			Map<String, Object> billMap = EntityUtils.entityToMap(item);

			// 获取贷方科目
			String creditSubject = AiSubjectUtils.getAiSubjectStr(item.getCreditSubject());
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
			// Long accountId = accountItem.getAccountId();
			// CustomerAccount account =
			// customerAccountService.selectByPrimaryKey(accountId);
			// customerAccountItemMap.put("accountName", account.getAccountName());
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