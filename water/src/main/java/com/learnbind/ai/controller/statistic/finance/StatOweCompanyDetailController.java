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
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.PartitionWater;
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
 * @Title: StatOweCompanyDetailController.java
 * @Description: 欠费单位明细统计表
 *
 * @author Administrator
 * @date 2019年12月31日 下午3:22:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat-owe-company-detail")
public class StatOweCompanyDetailController {
	private static Log log = LogFactory.getLog(StatOweCompanyDetailController.class);
	private static final String TEMPLATE_PATH = "statistic/finance/owecompanydetail/"; // 页面目录
	
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
	 * @Description: 欠费单位明细表
	 * @param model
	 * @param parmsJSON
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
			pageSize = 3;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//获取结算状态
		List<Integer> settleStatusList = this.getSettleMentStatus();
		
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.getStatOweCompanyMeter(searchCond, searchPeriod, settleStatusList);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();// 客户ID
			Long customerId = Long.valueOf(customerIdStr);// 客户ID
			String currPeriod = accountItemMap.get("PERIOD").toString();// 期间
			Customers customer = customersService.selectByPrimaryKey(customerId);
			
			//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());

			accountItemMap.put("customerName", customer.getCustomerName());
			// 查询往期水费欠费金额
			BigDecimal pastWaterFeeOweAmount = customerAccountItemService.getPastWaterFeeOweAmount(customerId,
					currPeriod);
			// 查询往期违约金欠费金额
			// 往期欠费总金额=往期水费欠费金额+往期违约金欠费金额
			BigDecimal pastOweTotalAmount = pastWaterFeeOweAmount;// BigDecimalUtils.add(pastWaterFeeOweAmount,
			accountItemMap.put("pastOweTotalAmount", pastOweTotalAmount);

			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			// 计算总欠费金额，本期欠费金额+往期欠费金额
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweTotalAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);
			
			//获取欠费明细
			Map<String, Object> detailMap = this.getDetailMapList(currPeriod, customerId, settleStatusList);
			accountItemMap.put("detailMapList", detailMap.get("detailMapList"));
			accountItemMap.put("totalOweBaseAmount", detailMap.get("totalOweBaseAmount"));
			accountItemMap.put("totalOweSewageAmount", detailMap.get("totalOweSewageAmount"));

		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		model.addAttribute("counter", (pageNum-1)*pageSize);
		model.addAttribute("period", periodStr);
		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		// 查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "table";
	}
	
	/**
	 * @Title: getDetailMapList
	 * @Description: 获取欠费明细
	 * @param period
	 * @param customerId
	 * @param settleStatusList
	 * @return 
	 */
	public Map<String, Object> getDetailMapList(String period,Long customerId, List<Integer> settleStatusList){
		//获取欠费明细
		Map<String, Object> detailMap = new HashMap();
		//获取客户欠费明细
		List<Map<String, Object>> detailMapList = customerAccountItemService.getCustomerAccountItemDetail(period, customerId, settleStatusList);
		BigDecimal totalOweBaseAmount = new BigDecimal("0");
		BigDecimal totalOweSewageAmount = new BigDecimal("0");
		for(Map<String, Object> map : detailMapList) {
			//计算这个账单基础水费和污水总欠费
			String debitAssistant = null;
			if(map.get("DEBIT_ASSISTANT") != null) {
				debitAssistant = map.get("DEBIT_ASSISTANT").toString();
			}
			BigDecimal oweBaseAmount = customerAccountItemService.getBaseFeeOweAmount(debitAssistant, new BigDecimal(map.get("BASE_WATER_FEE").toString()));
			BigDecimal oweSewageAmount = customerAccountItemService.getSewageFeeOweAmount(debitAssistant, new BigDecimal(map.get("SEWAGE_WATER_FEE").toString()));
			BigDecimal oweTotalAmount = BigDecimalUtils.add(oweBaseAmount, oweSewageAmount);
			totalOweBaseAmount = BigDecimalUtils.add(totalOweBaseAmount, oweBaseAmount);
			totalOweSewageAmount = BigDecimalUtils.add(totalOweSewageAmount, oweSewageAmount);
			map.put("oweBaseAmount", oweBaseAmount);
			map.put("oweSewageAmount", oweSewageAmount);
			map.put("oweTotalAmount", oweTotalAmount);
		}
		
		List<Map<String, Object>> newMapList = this.combineList(detailMapList);
		detailMap.put("totalOweBaseAmount", totalOweBaseAmount);
		detailMap.put("totalOweSewageAmount", totalOweSewageAmount);
		detailMap.put("detailMapList", newMapList);
		return detailMap;
	}
	
	/**
	 * @Title: combineList
	 * @Description:合并期间相同账单
	 * @param detailMapList
	 * @return 
	 */
	private List<Map<String , Object>> combineList(List<Map<String, Object>> detailMapList){
		BigDecimal zero = new BigDecimal("0");
		
		List<Map<String, Object>> newMapList = new ArrayList<>();
		for (int i = 0; i < detailMapList.size(); i++) {
			Map<String, Object> firstmap = detailMapList.get(i);
			BigDecimal oweBaseAmount = new BigDecimal(firstmap.get("oweBaseAmount").toString());
			BigDecimal oweSewageAmount = new BigDecimal(firstmap.get("oweSewageAmount").toString());
			BigDecimal oweTotalAmount = new BigDecimal(firstmap.get("oweTotalAmount").toString());
			Map<String, Object> map = new HashMap<>();
			
			
			List<Map<String, Object>> pwList = new ArrayList<>();
			pwList.addAll(detailMapList);
			int removeCount = 0;
			for (int j = 0; j < pwList.size(); j++) {
				Map<String, Object> secondmap = pwList.get(j);
				if (!StringUtils.equals(firstmap.get("ID").toString(), secondmap.get("ID").toString()) 
						&& StringUtils.equals(firstmap.get("PERIOD").toString(), secondmap.get("PERIOD").toString()) ) {
					
					oweBaseAmount = BigDecimalUtils.add(oweBaseAmount, new BigDecimal(secondmap.get("oweBaseAmount").toString()));
					oweSewageAmount = BigDecimalUtils.add(oweSewageAmount, new BigDecimal(secondmap.get("oweSewageAmount").toString()));
					oweTotalAmount = BigDecimalUtils.add(oweTotalAmount, new BigDecimal(secondmap.get("oweTotalAmount").toString()));
					detailMapList.remove((j - removeCount));
					removeCount = removeCount + 1;
				}

			}
			map.put("period", firstmap.get("PERIOD").toString());
			map.put("oweBaseAmount", oweBaseAmount);
			map.put("oweSewageAmount", oweSewageAmount);
			map.put("oweTotalAmount", oweTotalAmount);
			newMapList.add(map);
			firstmap.put("oweBaseAmount", oweBaseAmount);
			firstmap.put("oweSewageAmount", oweSewageAmount);
			firstmap.put("oweTotalAmount", oweTotalAmount);
		}
		return newMapList;
	}
	
	/**
	 * @Title: getSettleMentStatus
	 * @Description: 获取结算状态
	 * @return 
	 */
	private List<Integer> getSettleMentStatus() {
		List<Integer> settleStatusList = new ArrayList<>();
		// 结算失败
		Integer settleFail = EnumSettlementStatus.SETTLEMENT_FAIL.getValue();
		// 部分结算
		Integer settlePart = EnumSettlementStatus.SETTLEMENT_PART.getValue();
		// 未结算
		Integer settleNormal = EnumSettlementStatus.SETTLEMENT_NORMAL.getValue();
		settleStatusList.add(settlePart);
		settleStatusList.add(settleFail);
		settleStatusList.add(settleNormal);
		return settleStatusList;
	}



}