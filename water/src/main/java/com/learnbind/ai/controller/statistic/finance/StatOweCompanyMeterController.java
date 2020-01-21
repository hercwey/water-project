package com.learnbind.ai.controller.statistic.finance;

import java.math.BigDecimal;
import java.text.ParseException;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.statclassify.StatClassifyService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.statistic.finance
 *
 * @Title: StatOweCompanyMeterController.java
 * @Description: 欠费单位总表统计
 *
 * @author Administrator
 * @date 2019年12月31日 下午3:22:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat-owe-company-meter")
public class StatOweCompanyMeterController {
	private static Log log = LogFactory.getLog(StatOweCompanyMeterController.class);
	private static final String TEMPLATE_PATH = "statistic/finance/owecompanymeter/"; // 页面目录
	
	@Autowired
	private StatClassifyService statClassifyService;
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private CustomersService customersService;//客户
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param searchPeriod			查询期间
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, String parmsJSON) {
		
		JSONObject parmsObj = JSON.parseObject(parmsJSON);
		Integer pageNum = parmsObj.getInteger("pageNum");
		Integer pageSize = parmsObj.getInteger("pageSize");
		String searchPeriod = parmsObj.getString("searchPeriod");
		String searchCond = parmsObj.getString("searchCond");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String periodStr = "";
		try {
			Date periodD = sdf.parse(searchPeriod);
			sdf = new SimpleDateFormat("yyyy年MM月");
			periodStr = sdf.format(periodD);
		} catch (ParseException e) {
			e.printStackTrace();
			periodStr = searchPeriod;
		}
		
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
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.getStatOweCompanyMeter(searchCond, searchPeriod, settleStatusList);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {

			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
			Long customerId = Long.valueOf(customerIdStr);// 客户ID
//			String billIds = accountItemMap.get("BILL_IDS").toString();//账单ID集合,多个账单ID用逗号","分隔
			String currPeriod = accountItemMap.get("PERIOD").toString();// 期间
			String sumBaseWaterFee = accountItemMap.get("SUM_BASE_WATER_FEE").toString();//基础水费
			String sumSewageWaterFee = accountItemMap.get("SUM_SEWAGE_WATER_FEE").toString();//污水处理费

			//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());

			Customers customer = customersService.selectByPrimaryKey(customerId);//查询客户
			accountItemMap.put("customerName", customer.getCustomerName());
			
			// 查询本期欠费账单
			List<CustomerAccountItem> oweBillList = customerAccountItemService.getWaterFeeOweBillList(customerId, currPeriod);
			BigDecimal baseWaterFeeOweAmount = this.getBaseWaterFeeOweAmount(oweBillList);//本期基础水费欠费金额
			BigDecimal sewageWaterFeeOweAmount = this.getSewageWaterFeeOweAmount(oweBillList);//本期污水水费欠费金额
			
			// 查询往期欠费账单
			List<CustomerAccountItem> pastOweBillList = customerAccountItemService.getPastWaterFeeOweBillList(customerId, currPeriod);
			BigDecimal pastBaseWaterFeeOweAmount = this.getBaseWaterFeeOweAmount(pastOweBillList);//往期基础水费欠费金额
			BigDecimal pastSewageWaterFeeOweAmount = this.getSewageWaterFeeOweAmount(pastOweBillList);//往期污水水费欠费金额
			
			accountItemMap.put("baseWaterFeeOweAmount", baseWaterFeeOweAmount);
			accountItemMap.put("pastBaseWaterFeeOweAmount", pastBaseWaterFeeOweAmount);
			// 查询违约金欠费金额 TODO
			
			BigDecimal sewageWaterFeeTotalOweAmount = new BigDecimal(0.00);//污水水费欠费金额（本期污水欠费+往期污水欠费）
			sewageWaterFeeTotalOweAmount = BigDecimalUtils.add(sewageWaterFeeOweAmount, pastSewageWaterFeeOweAmount);
			accountItemMap.put("sewageWaterFeeTotalOweAmount", sewageWaterFeeTotalOweAmount);
			
			// 计算总欠费金额，本期欠费金额+往期欠费金额+本期和往期污水处理费
			BigDecimal totalOweAmount = BigDecimalUtils.add(baseWaterFeeOweAmount, pastBaseWaterFeeOweAmount);
			totalOweAmount = BigDecimalUtils.add(totalOweAmount, sewageWaterFeeTotalOweAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);

		}

		//获取合计金额
		Map<String, BigDecimal> statTotalMap = this.getStatTotal(searchCond, searchPeriod, settleStatusList);
		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		model.addAttribute("counter", (pageNum-1)*pageSize);
		model.addAttribute("period", periodStr);
		model.addAttribute("statTotalMap", statTotalMap);
		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "table";
	}
	/**
	 * @Title: getBaseWaterFeeOweAmount
	 * @Description: 获取基础水费欠费金额
	 * @param oweBillList
	 * @return 
	 */
	private BigDecimal getBaseWaterFeeOweAmount(List<CustomerAccountItem> oweBillList) {
		
		BigDecimal oweTotalAmount = new BigDecimal(0.00);//基础水费欠费总金额
		for(CustomerAccountItem oweBill : oweBillList) {
			BigDecimal baseWaterFee = oweBill.getBaseWaterFee();
			String debitAssistant = oweBill.getDebitAssistant();
			log.debug("获取基础水费欠费金额，账目ID:"+oweBill.getId()+"，借方辅助核算："+debitAssistant);
			if(StringUtils.isNotBlank(debitAssistant)) {
				BigDecimal oweAmount = customerAccountItemService.getBaseFeeOweAmount(debitAssistant, baseWaterFee);
				oweTotalAmount = BigDecimalUtils.add(oweTotalAmount, oweAmount);
			}else {
				oweTotalAmount = BigDecimalUtils.add(oweTotalAmount, baseWaterFee);
			}
			
		}
		return oweTotalAmount;
	}
	/**
	 * @Title: getSewageWaterFeeOweAmount
	 * @Description: 获取污水处理费欠费金额
	 * @param oweBillList
	 * @return 
	 */
	private BigDecimal getSewageWaterFeeOweAmount(List<CustomerAccountItem> oweBillList) {
		
		BigDecimal oweTotalAmount = new BigDecimal(0.00);//基础水费欠费总金额
		for(CustomerAccountItem oweBill : oweBillList) {
			BigDecimal sewageWaterFee = oweBill.getSewageWaterFee();
			String debitAssistant = oweBill.getDebitAssistant();
			log.debug("获取污水水费欠费金额，账目ID:"+oweBill.getId()+"，借方辅助核算："+debitAssistant);
			if(StringUtils.isNotBlank(debitAssistant)) {
				BigDecimal oweAmount = customerAccountItemService.getSewageFeeOweAmount(debitAssistant, sewageWaterFee);
				oweTotalAmount = BigDecimalUtils.add(oweTotalAmount, oweAmount);
			}else {
				oweTotalAmount = BigDecimalUtils.add(oweTotalAmount, sewageWaterFee);
			}
			
		}
		return oweTotalAmount;
	}
	
	/**
	 * @Title: getStatTotal
	 * @Description: 获取合计金额
	 * @param searchCond
	 * @param searchPeriod
	 * @param settleStatusList
	 * @return 
	 */
	private Map<String, BigDecimal> getStatTotal(String searchCond, String searchPeriod, List<Integer> settleStatusList){
		
		BigDecimal oweTotalAmount = new BigDecimal(0.00);//总欠费金额
		BigDecimal pastBaseOweTotalAmount = new BigDecimal(0.00);//上期所欠水费
		BigDecimal baseOweTotalAmount = new BigDecimal(0.00);//本期所欠水费
		BigDecimal sewageOweTotalAmount = new BigDecimal(0.00);//污水欠费水费
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.getStatOweCompanyMeter(searchCond, searchPeriod, settleStatusList);

		for (Map<String, Object> accountItemMap : accountItemMapList) {

			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
			Long customerId = Long.valueOf(customerIdStr);// 客户ID
//			String billIds = accountItemMap.get("BILL_IDS").toString();//账单ID集合,多个账单ID用逗号","分隔
			String currPeriod = accountItemMap.get("PERIOD").toString();// 期间

			// 查询本期欠费账单
			List<CustomerAccountItem> oweBillList = customerAccountItemService.getWaterFeeOweBillList(customerId, currPeriod);
			BigDecimal baseWaterFeeOweAmount = this.getBaseWaterFeeOweAmount(oweBillList);//本期基础水费欠费金额
			BigDecimal sewageWaterFeeOweAmount = this.getSewageWaterFeeOweAmount(oweBillList);//本期污水水费欠费金额
			
			// 查询往期欠费账单
			List<CustomerAccountItem> pastOweBillList = customerAccountItemService.getPastWaterFeeOweBillList(customerId, currPeriod);
			BigDecimal pastBaseWaterFeeOweAmount = this.getBaseWaterFeeOweAmount(pastOweBillList);//往期基础水费欠费金额
			BigDecimal pastSewageWaterFeeOweAmount = this.getSewageWaterFeeOweAmount(pastOweBillList);//往期污水水费欠费金额
			
			baseOweTotalAmount = BigDecimalUtils.add(baseOweTotalAmount, baseWaterFeeOweAmount);//本期所欠水费
			pastBaseOweTotalAmount = BigDecimalUtils.add(pastBaseOweTotalAmount, pastBaseWaterFeeOweAmount);//上期所欠水费
			// 查询违约金欠费金额 TODO
			
			BigDecimal sewageWaterFeeTotalOweAmount = new BigDecimal(0.00);//污水水费欠费金额（本期污水欠费+往期污水欠费）
			sewageWaterFeeTotalOweAmount = BigDecimalUtils.add(sewageWaterFeeOweAmount, pastSewageWaterFeeOweAmount);
			sewageOweTotalAmount = BigDecimalUtils.add(sewageOweTotalAmount, sewageWaterFeeTotalOweAmount);//污水欠费水费
			
			// 计算总欠费金额，本期欠费金额+往期欠费金额+本期和往期污水处理费
			BigDecimal totalOweAmount = BigDecimalUtils.add(baseWaterFeeOweAmount, pastBaseWaterFeeOweAmount);
			totalOweAmount = BigDecimalUtils.add(totalOweAmount, sewageWaterFeeTotalOweAmount);
			oweTotalAmount = BigDecimalUtils.add(oweTotalAmount, totalOweAmount);//总欠费金额
		}
		
		Map<String, BigDecimal> statTotalMap = new HashMap<>();
		statTotalMap.put("oweTotalAmount", oweTotalAmount);//总欠费金额
		statTotalMap.put("pastBaseOweTotalAmount", pastBaseOweTotalAmount);//上期所欠水费
		statTotalMap.put("baseOweTotalAmount", baseOweTotalAmount);//本期所欠水费
		statTotalMap.put("sewageOweTotalAmount", sewageOweTotalAmount);//污水欠费水费
		
		return statTotalMap;
	}

	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 
	 * 		为null时是管理员角色，查询所有；不为null时是工程公司角色，只查询此工程公司创建的安装工程
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;//操作员ID
		if (userBean!=null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for(SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}
			
			//indexOf() 返回指定字符在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
			if(roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_ADMIN)==-1) {
				operatorId = userBean.getId();//操作员ID
			}
			
		}
		return operatorId;
	}

}