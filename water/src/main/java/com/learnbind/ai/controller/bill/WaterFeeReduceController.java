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
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerOverdueFine;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DiscountWaterFeeTrace;
import com.learnbind.ai.model.Location;
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

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: WaterFeeReduceController.java
 * @Description: 收费管理-水费减免
 *
 * @author Thinkpad
 * @date 2019年9月7日 下午10:48:53
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/water-fee-reduce-bill")
public class WaterFeeReduceController {
	private static Log log = LogFactory.getLog(WaterFeeReduceController.class);
	private static final String TEMPLATE_PATH = "bill/water_fee_reduce/"; // 页面目录
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
	private BankService bankService;//客户银行信息
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计绑定关系表
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置客户
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
	public String table(Model model, Integer pageNum, Integer pageSize, String period, String traceIds, Integer settlementStatus, Integer accountStatus,  String searchCond, String startDate, String endDate) {
		

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
		//如果他是欠费管理，查询除交易成功的账单
		//accountItemMapList = customerAccountItemService.searchCustomerArrearsAccountItem(period, traceIds, settlementStatus, searchCond, operatorId, startDate, endDate);
		accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, traceIds, settlementStatus, accountStatus, searchCond, null, startDate, endDate);
		
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
			
			//计算违约金总金额
			BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationCustomerService.getPlace(customerId);
			accountItemMap.put("place", place);//地理位置
			
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
		//获取账单欠费金额
		BigDecimal billOweAmount = BigDecimalUtils.subtract(currItem.getCreditAmount(), currItem.getDebitAmount());
		//获取违约金金额之和
		BigDecimal overdueOweAmountSum = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
		//计算总欠费金额=账单欠费金额+违约金金额之和
		BigDecimal totalOweAmount = BigDecimalUtils.add(billOweAmount, overdueOweAmountSum);
		
		//查询分水量
		PartitionWater pw = partitionWaterService.getPartitionWater(currItem.getId());
		//获取账单基础水费欠费金额
		BigDecimal basePriceOweAmount = this.getBasePriceOweAmount(currItem.getDebitAssistant(), pw.getRealWaterAmount(), pw.getBasePrice());
		//获取账单污水处理费欠费金额
		BigDecimal treatmentFeeOweAmount = this.getTreatmentFeeOweAmount(currItem.getDebitAssistant(), pw.getRealWaterAmount(), pw.getTreatmentFee());
		
		//查询客户
		Customers customer = customersService.selectByPrimaryKey(currItem.getCustomerId());
		
		//List<EnumAiDebitSubjectAction> subjectActionList = EnumAiDebitSubjectAction.getSettleType();;//缴费时缴费类型
		List<EnumAiDebitSubjectAction> subjectActionList = new ArrayList<>();
		if(customer.getCustomerType()==EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {//如果客户类型为个人用户时缴费类型只有水费（综价）
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
	 * @param customerId
	 * @param accountItemId
	 * @param overdueFineJSON
	 * @return
	 */
	/**
	 * @Title: settleAccount
	 * @Description: TODO(这里用一句话描述这个方法的作用)
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
				String periodTemp = accountItemMap.get("PERIOD").toString();//期间
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