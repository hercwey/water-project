package com.learnbind.ai.controller.bill;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumSendWechatStatus;
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SendWechatLog;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.sendlog.SendWechatLogService;
import com.learnbind.ai.service.wechat.service.TemplateService;
import com.learnbind.ai.service.wechatuser.WechatCustomerService;
import com.learnbind.ai.service.wechatuser.WechatUserService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.bill
 *
 * @Title: WechatRemindController.java
 * @Description: 微信提醒
 *
 * @author Thinkpad
 * @date 2019年9月8日 上午12:19:47
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/wechat-account-item")
public class WechatRemindController {
	private static Log log = LogFactory.getLog(WechatRemindController.class);
	private static final String TEMPLATE_PATH = "bill/wechat_bill/"; // 页面目录
	private static final String TEMPLATE_PATH_CUSTOMER_BILL = "bill/wechat_bill/customerbill/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService; // 客户信息
	@Autowired
	private LocationService locationService;// 地理位置
	@Autowired
	private TemplateService templateService;//模版消息
	@Autowired
	private WechatCustomerService wechatCustomerService;//微信-客户关系
	@Autowired
	private WechatUserService wechatUserService;//微信用户
	@Autowired
	private SendWechatLogService sendWechatLogService;//发送微信日志

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
			Integer settlementStatus, String searchCond, String startDate, String endDate) {

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
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.getListGroupByCustomerId(searchCond,
				traceIds, period, null, startDate, endDate, settleStatusList);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {

			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
			Long customerId = Long.valueOf(customerIdStr);// 客户ID
//									String billIds = accountItemMap.get("BILL_IDS").toString();//账单ID集合,多个账单ID用逗号","分隔
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
			BigDecimal pastOweTotalAmount = pastWaterFeeOweAmount;// BigDecimalUtils.add(pastWaterFeeOweAmount,
																	// pastOverdueOweAmount);
			accountItemMap.put("pastOweTotalAmount", pastOweTotalAmount);

			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			// 计算总欠费金额，本期欠费金额+往期欠费金额
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweTotalAmount);
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

//				//查询客户
//				Customers customer = customersService.selectByPrimaryKey(customerId);
//				String customerName = "";
//				if(customer!=null) {
//					customerName = customer.getCustomerName();
//				}
//				model.addAttribute("customerName", customerName);

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

	// ---------------------------------发送微信通知-----------------------------------------------------------------------------------------------
	/**
	 * @Title: sendWechatNotify
	 * @Description: 发送微信通知
	 * @param request
	 * @param model
	 * @param customerId
	 * @param period
	 * @return 
	 */
	@RequestMapping(value = "/send-wechat-notify")
	@ResponseBody
	public Object sendWechatNotify(HttpServletRequest request, Model model, Long customerId, String period) {
		Long operatorId= this.getOperatorId();
		try {

			BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型 0

			BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customerId, period);//获取本期欠费金额
			BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customerId, period);//获取往期欠费金额
			
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);

			// 如果欠费金额>0，则发送短信通知
			if (BigDecimalUtils.greaterThan(totalOweAmount, zero)) {
				// 发送短信通知
				// 查询客户信息
				Customers customer = customersService.selectByPrimaryKey(customerId);
				WechatUser  wu = wechatCustomerService.getWechatCustomer(customerId);//查询客户绑定的微信
				if(wu!=null) {
					templateService.sendWaterFeeNofity(wu.getOpenid(), customer.getCustomerName(), totalOweAmount);
					//添加微信发送日志
					SendWechatLog log = new SendWechatLog();
					log.setCustomerId(customerId);
					log.setOpenId(wu.getOpenid());
					log.setOperationTime(new Date());
					log.setOperatorId(operatorId);
					log.setPeriod(period);
					log.setResult(EnumSendWechatStatus.SUCCESS.getValue());
					sendWechatLogService.insertSelective(log);
					return RequestResultUtil.getResultSuccess("微信通知已发送！");
				}
				return RequestResultUtil.getResultFail("客户【"+customer.getCustomerName()+"】未绑定微信！");
			} else {
				return RequestResultUtil.getResultFail("此客户账单已全部结算，不需要发送微信通知！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("发送微信通知异常！请重试！");
	}
	
	/**
	 * @Title: sendWechatNotifyBatch
	 * @Description: 发送微信通知
	 * @param period
	 * @param traceIds
	 * @param searchCond
	 * @param startDate
	 * @param endDate
	 * @param customerIdJson
	 * @return 
	 */
	@RequestMapping(value = "/send-wechat-notify-batch")
	@ResponseBody
	public Object sendWechatNotifyBatch(String period, String traceIds, String searchCond, String startDate, String endDate, String customerIdJson) {
		Long operatorId= this.getOperatorId();
		try {

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
			if(StringUtils.isNotBlank(customerIdJson)) {
				Map<String, Object> temp = new HashMap<>();
				String[] s1 = customerIdJson.split(",");
				for (String customerId : s1) {
					temp.put("CUSTOMER_ID", customerId);
					temp.put("PERIOD", period);
					accountItemMapList.add(temp);
				}
				
			} else {
				//accountItemMapList = customerAccountItemService.getListGroupByCustomerId(searchCond, traceIds, period, null, startDate, endDate, settleStatusList);
				//查询已绑定微信的客户
				accountItemMapList = customerAccountItemService.getWechatCustomerOweBillList(searchCond, traceIds, period, null, startDate, endDate, settleStatusList);
			}
			
			if(accountItemMapList==null || accountItemMapList.size()<=0) {
				return RequestResultUtil.getResultSuccess("未查询到已绑定微信的欠费客户，不需要发送微信通知！");
			}
			
			BigDecimal zero = new BigDecimal(0.00);// 初始化BigDecimal类型 0

			int sendCount = 0;
			int notSendCount = 0;
			for (Map<String, Object> accountItemMap : accountItemMapList) {
				String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
				Long customerId = Long.valueOf(customerIdStr);// 客户ID
				String currPeriod = accountItemMap.get("PERIOD").toString();// 期间
				
				BigDecimal oweAmount = customerAccountItemService.getWaterFeeOweAmount(customerId, currPeriod);//获取本期欠费金额
				BigDecimal pastOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customerId, currPeriod);//获取往期欠费金额
				
				BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweAmount);
				// 如果欠费金额>0，则发送短信通知
				if (BigDecimalUtils.greaterThan(oweAmount, zero)) {
					// 发送短信通知
					// 查询客户信息
					Customers customer = customersService.selectByPrimaryKey(customerId);
					WechatUser  wu = wechatCustomerService.getWechatCustomer(customerId);//查询客户绑定的微信
					if(wu!=null) {
						//添加微信发送日志
						SendWechatLog log = new SendWechatLog();
						log.setCustomerId(customerId);
						log.setOpenId(wu.getOpenid());
						log.setOperationTime(new Date());
						log.setOperatorId(operatorId);
						log.setPeriod(period);
						log.setResult(EnumSendWechatStatus.SUCCESS.getValue());
						sendWechatLogService.insertSelective(log);
						templateService.sendWaterFeeNofity(wu.getOpenid(), customer.getCustomerName(), totalOweAmount);
						sendCount+=1;
					}else {
						notSendCount+=1;
					}
					
				}
			}
			return RequestResultUtil.getResultSuccess("共发送"+sendCount+"条微信通知，"+notSendCount+"个客户未绑定微信！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("发送微信通知异常！请重试！");
	}

}