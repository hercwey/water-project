package com.learnbind.ai.controller.customers;

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
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.CMBCAutoDeductReceiptBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.AccountItemConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DiscountWaterFeeTrace;
import com.learnbind.ai.model.Location;
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

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerAccountItemController.java
 * @Description: 客户账单
 *
 * @author Thinkpad
 * @date 2019年6月3日 下午4:44:12
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/account-item/old")
public class CustomerAccountItemController {
	private static Log log = LogFactory.getLog(CustomerAccountItemController.class);
	private static final String TEMPLATE_PATH = "customers/account_item/"; // 页面目录
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
	private LocationCustomerService locationCustomerService;//地理位置客户
	@Autowired
	private BankService bankService;//客户银行信息
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计绑定关系表

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
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, locationCode, settlementStatus, accountStatus, searchCond, operatorId, null, null);
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
		int row = 1;
		//int row = customerAccountItemService.insertCustomerAccountItem(accountItem);
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
				//软删除
				item.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				customerAccountItemService.updateByPrimaryKeySelective(item);
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
		
		Example example = new Example(CustomerAccountItem.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("period", period)
			.andEqualTo("creditSubject", AccountItemConstant.CREDIT_SUBJECT_OVERDUE_FINE).andEqualTo("debitCredit", EnumAiDebitCreditStatus.CREDIT.getKey());
		List<CustomerAccountItem> accountItemList = customerAccountItemService.selectByExample(example);
		
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
			CustomerAccountItem item = customerAccountItemService.selectByPrimaryKey(overdueFineItem.getOverdueAccountId());

			Map<String, Object> accountItemMap = EntityUtils.entityToMap(overdueFineItem);

			Customers customers = customersService.selectByPrimaryKey(customerId);
			accountItemMap.put("customerName", customers.getCustomerName());
			accountItemMap.put("discountAmountSum", item.getDebitAmount());
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

			List<Map<String, Object>> overdueFineMapList = new ArrayList<>();

			Map<String, Object> accountItemMap = EntityUtils.entityToMap(waterFee);

			Customers customers = customersService.selectByPrimaryKey(customerId);
			accountItemMap.put("customerName", customers.getCustomerName());
			accountItemMap.put("discountAmountSum", waterFee.getDebitAmount());
			overdueFineMapList.add(accountItemMap);


			model.addAttribute("waterFeeList", overdueFineMapList);
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
	
	
	// ------------------------分账单按钮-------------------

	/**
	 * @Title: loadSubAccountDialog
	 * @Description: 加载分账单对话框对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-sub-account-dialog")
	public String loadSubAccountDialog(Model model, Long customerId, String period, Long accountItemId) {
		CustomerAccountItem currItem = customerAccountItemService.selectByPrimaryKey(accountItemId);
		//根据默认客户id，获取一表多户的其他客户
		List<CustomerMeter> cmList = customerMeterService.selectCustomerCountByMeter(customerId);
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for (CustomerMeter customerMeter : cmList) {

			Map<String, Object> accountItemMap = EntityUtils.entityToMap(customerMeter);

			Customers customers = customersService.selectByPrimaryKey(customerMeter.getCustomerId());
			accountItemMap.put("customerName", customers.getCustomerName());
			accountItemMap.put("period", currItem.getPeriod());
			accountItemMap.put("creditAmount", currItem.getCreditAmount());
			customerMapList.add(accountItemMap);

		}

		model.addAttribute("customerList", customerMapList);
		model.addAttribute("currItem", currItem);
		return TEMPLATE_PATH + "sub_account_dialog";
	}
	
	/**
	 * @Title: saveSubAccountFine
	 * @Description: 保存分账单
	 * @param model
	 * @param subFineJSON
	 * @return 
	 */
	@RequestMapping(value = "/save-sub-account", produces = "application/json")
	@ResponseBody
	public Object saveSubAccount(Model model, String subFineJSON, String subWaterAmountJSON, Long accountItemId, BigDecimal totalAmount) {
		List<CustomerAccountItem> accountItemList = JSON.parseArray(subFineJSON, CustomerAccountItem.class);
		//分配金额
		BigDecimal subAmount = new BigDecimal("0");
		for(CustomerAccountItem item : accountItemList) {
			subAmount = BigDecimalUtils.add(subAmount, item.getCreditAmount());
		}
		if(BigDecimalUtils.equals(totalAmount, subAmount)) {//判断账单是否全部分配完毕
			List<BigDecimal> waterAmountList = JSON.parseArray(subWaterAmountJSON, BigDecimal.class);
			int rows = customerAccountItemService.saveSubAccount(accountItemList, accountItemId, waterAmountList);
			if (rows > 0) {
				return RequestResultUtil.getResultSaveSuccess("分账单成功！");
			}
			return RequestResultUtil.getResultSaveWarn("分账单数据异常，请重新尝试！");
		}
		
		return RequestResultUtil.getResultSaveWarn("分账单金额异常，分配金额之和应为"+totalAmount+"元");
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
			accountItemMap.put("creditSubject", caItem.getCreditSubject());
			accountItemMap.put("debitCredit", caItem.getDebitCredit());
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
			return RequestResultUtil.getResultSaveSuccess("撤销分账单成功！");
		}
		return RequestResultUtil.getResultSaveWarn("撤销分账单数据异常，请重新尝试！");
		
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
			List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, locationCode, settlementStatus, null, searchCond, operatorId, null,null);

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