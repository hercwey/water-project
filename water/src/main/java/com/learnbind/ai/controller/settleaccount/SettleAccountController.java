package com.learnbind.ai.controller.settleaccount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.AccountItemConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.CustomerOverdueFineService;
import com.learnbind.ai.service.meterrecord.DiscountFineTraceService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.settleaccount
 *
 * @Title: SettleAccountController.java
 * @Description: 账单结算管理
 *
 * @author Administrator
 * @date 2019年7月6日 下午7:47:04
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/settle-account")
public class SettleAccountController {
	private static Log log = LogFactory.getLog(SettleAccountController.class);
	private static final String TEMPLATE_PATH = "settle_account/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

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
	private LocationService locationService;//地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;
	
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
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String period, String locationCode, Integer settlementStatus, Integer accountStatus, String searchCond) {

		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, locationCode, settlementStatus, accountStatus, searchCond, null, null , null);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String accountItemIdStr = accountItemMap.get("ID").toString();//账单ID
			Long accountItemId = Long.valueOf(accountItemIdStr);//账单ID
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
			//String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
			//String currTraceIds = accountItemMap.get("TRACE_IDS").toString();//地理位置traceIds
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额-贷方金额（账单金额）-借方金额（充值金额）
			
			//计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationCustomerService.getPlace(customerId);
			accountItemMap.put("place", place);//地理位置
			
			accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			accountItemMap.put("overdueValue", overdueValue);//违约金总金额
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，欠费金额+违约金欠款
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, overdueOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);

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
			customerAccountItemMap.put("overdueValue", overdueValue);// 违约金账单金额
			customerAccountItemMap.put("rechargeOverdueValue", rechargeOverdueValue);// 已充值违约金账单金额
			// 计算欠费金额，欠费金额=贷方金额（账单金额）+ 违约金金额 - 充值金额
			customerAccountItemMap.put("arrearsValue", owedAmountValue);
			// 获取客户名称
			Long customerId = accountItem.getCustomerId();
			Customers customer = customersService.selectByPrimaryKey(customerId);
			customerAccountItemMap.put("customerName", customer.getCustomerName());
			// 获取账户名称
			Long accountId = accountItem.getAccountId();
			CustomerAccount account = customerAccountService.selectByPrimaryKey(accountId);
			customerAccountItemMap.put("accountName", account.getAccountName());
		}

		model.addAttribute("accountItem", customerAccountItemMap);

		return TEMPLATE_PATH + "account_detail_table";
	}

	/**
	 * @Title: loadAddAccountItemDialog
	 * @Description: 增加账单对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadaddaccountitemdialog")
	public String loadAddAccountItemDialog(Model model) {

		return TEMPLATE_PATH + "account_dialog_edit";
	}

	/**
	 * @Title: addAccountItem
	 * @Description: 增加账单
	 * @param accountItem
	 * @return
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addAccountItem(CustomerAccountItem accountItem) {

		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}

		accountItem.setAccounter(userBean.getRealname());
		accountItem.setAccountDate(new Date());
		//int row = customerAccountItemService.insertCustomerAccountItem(accountItem);
		int row = 1;
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
			BigDecimal zero = new BigDecimal("0");//默认值：0
			
			boolean isDelete = true;
			for (Long id : ids) {
				CustomerAccountItem item = customerAccountItemService.selectByPrimaryKey(id);
				BigDecimal creditAmount = item.getCreditAmount();//贷方金额
				BigDecimal debitAmount = item.getDebitAmount();//借方金额
				if(BigDecimalUtils.greaterThan(debitAmount, zero)) {
					isDelete = true;
					break;
				}
				customerAccountItemService.deleteCustomerAccountItem(id);
			}
			if(!isDelete) {
				return RequestResultUtil.getResultFail("该账单客户已充值，不能删除！");
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
			/*
			 * Map<String, Object> respMap =
			 * RequestResultUtil.getResultSaveSuccess("创建违约金账单成功！"); return respMap;
			 */
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
		// 查询客户违约金数据
		CustomerOverdueFine customerOverdueFine = new CustomerOverdueFine();
		customerOverdueFine.setCustomerId(customerId);
		customerOverdueFine.setPeriod(period);
		List<CustomerOverdueFine> overdueFineList = customerOverdueFineService.select(customerOverdueFine);

		List<Map<String, Object>> overdueFineMapList = new ArrayList<>();
		for (CustomerOverdueFine overdueFineItem : overdueFineList) {
			// 查询违约金减免日志
			BigDecimal discountAmountSum = discountFineTraceService.getDiscountAmountSum(overdueFineItem.getId());

			Map<String, Object> accountItemMap = EntityUtils.entityToMap(overdueFineItem);

			Customers customers = customersService.selectByPrimaryKey(customerId);
			accountItemMap.put("customerName", customers.getCustomerName());
			accountItemMap.put("discountAmountSum", discountAmountSum);
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
	public Object saveOverdueFine(Model model, Long customerId, Long accountItemId, String overdueFineJSON) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}
		List<DiscountFineTrace> dfList = JSON.parseArray(overdueFineJSON, DiscountFineTrace.class);
		int rows = discountFineTraceService.saveList(customerId, accountItemId, dfList);
		if (rows > 0) {
			return RequestResultUtil.getResultSaveSuccess("减免违约金成功！");
		}
		return RequestResultUtil.getResultSaveWarn("减免违约金数据异常，请重新尝试！");

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
			model.addAttribute("currItem", currItem);
			return TEMPLATE_PATH + "settle_account_dialog";
		}

	/**
	 * @Title: settleAccount
	 * @Description: 结算账单
	 * @param model
	 * @param customerId
	 * @param accountItemId
	 * @param overdueFineJSON
	 * @return
	 */
	@RequestMapping(value = "/settle-account", produces = "application/json")
	@ResponseBody
	public Object settleAccount(Model model, Long accountItemId, BigDecimal settleAmount) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean == null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}

		int rows = customerAccountItemService.settleAccount(accountItemId, userBean.getId(), settleAmount, AccountItemConstant.DEBIT_SUBJECT_RECHARGE, AccountItemConstant.WATER_FEE_RECHARGE);
		if (rows > 0) {

			return RequestResultUtil.getResultSaveSuccess("账单结算成功！");
		}

		return RequestResultUtil.getResultSaveWarn("账单结算异常，请重新尝试！");

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