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

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.StatClassify;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.statclassify.StatClassifyService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.statistic.finance
 *
 * @Title: StatWaterFeeNoTaxDetailController.java
 * @Description: 水费无票明细
 *
 * @author Administrator
 * @date 2019年12月31日 下午3:22:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat-water-fee-notax-detail")
public class StatWaterFeeNoTaxDetailController {
	private static Log log = LogFactory.getLog(StatWaterFeeNoTaxDetailController.class);
	private static final String TEMPLATE_PATH = "statistic/finance/waterfeenotaxdetail/"; // 页面目录
	
	@Autowired
	private StatClassifyService statClassifyService;
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private LocationService locationService;//地理位置
	
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
	 * @param searchSubjectPayment	支付方式
	 * @param periodType			期间类型：1=本期；2=往期
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, String searchPeriod) {
		StatClassify searchObj = new StatClassify();
		searchObj.setPid(0l);
		searchObj.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<StatClassify> classifyList = statClassifyService.select(searchObj);//查询统计小区节点
		//获取结算状态
		List<Integer> settleStatusList = this.getSettleMentStatus();
		List<Map<String, Object>> statMapList = new ArrayList<>();//统计结果集合
		int count = 0;//序号
		BigDecimal totalDebitAmount = new BigDecimal("0");//合计收入
		
		for(StatClassify classify : classifyList) {
			List<String> traceIdsList = this.getTraceIdsList(classify.getId());
			List<Map<String, Object>> statBillMapList = customerAccountItemService.getStatNoTaxBillList(searchPeriod, settleStatusList, traceIdsList);
			
			for(Map<String, Object> statBillMap : statBillMapList) {
				count = count+1;
				Map<String, Object> statMap = this.getStatMap(count, classify.getClassifyName(), statBillMap);
				BigDecimal debitAmount = new BigDecimal("0");//合计收入
				if(statMap.get("sumDebitAmount") != null) {
					debitAmount = new BigDecimal(statMap.get("sumDebitAmount").toString());
				}
				totalDebitAmount = BigDecimalUtils.add(totalDebitAmount, debitAmount);
				statMapList.add(statMap);
			}
		}
		BigDecimal noTaxDebitAmount = BigDecimalUtils.multiply(totalDebitAmount, new BigDecimal(0.97));//不含税合计
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date periodD = new Date();
		try {
			periodD = sdf.parse(searchPeriod);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		sdf = new SimpleDateFormat("yyyy年MM月");
		String dayStr = sdf.format(periodD);
		
		model.addAttribute("statMapList", statMapList);//返回页面参数
		model.addAttribute("period", dayStr);//返回页面参数
		model.addAttribute("totalDebitAmount", totalDebitAmount);
		model.addAttribute("noTaxDebitAmount", noTaxDebitAmount);
		return TEMPLATE_PATH + "table";
	}
	
	/**
	 * @Title: getSettleMentStatus
	 * @Description: 获取结算状态
	 * @return 
	 */
	private List<Integer> getSettleMentStatus() {
		List<Integer> settleStatusList = new ArrayList<>();
		// 结算成功
		Integer settleSuccess = EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue();
		// 部分结算
		Integer settlePart = EnumSettlementStatus.SETTLEMENT_PART.getValue();
		settleStatusList.add(settlePart);
		settleStatusList.add(settleSuccess);
		return settleStatusList;
	}
	
	/**
	 * @Title: getTraceIdsList
	 * @Description: 获取分组下的地理位置
	 * @param classifyId
	 * @return 
	 */
	private List<String> getTraceIdsList(Long classifyId){
		List<StatClassify> childClassifyList = statClassifyService.getChildList(classifyId);
		List<String> traceIdsList = new ArrayList<>();
		for(StatClassify childClassify : childClassifyList) {
			Long locationId = childClassify.getLocationId();
			Location location = locationService.selectByPrimaryKey(locationId);
			if(location!=null && StringUtils.isNotBlank(location.getTraceIds())) {
				traceIdsList.add(location.getTraceIds());
			}
		}
		return traceIdsList;
	}
	
	/**
	 * @Title: getStatMap
	 * @Description: 获取小区汇总表对象Map
	 * @param classifyName
	 * @param statBillMap
	 * @return 
	 */
	private Map<String, Object> getStatMap(int count, String classifyName, Map<String, Object> statBillMap){
		BigDecimal billCount = (BigDecimal)statBillMap.get("COUNT");//账单个数
		BigDecimal amount = (BigDecimal)statBillMap.get("SUM_DEBIT_AMOUNT");//账单水费
		
		BigDecimal sumDebitAmount = new BigDecimal(0.00);
		if(amount!=null) {
			sumDebitAmount = amount;
		}
		
		Map<String, Object> statMap = new HashMap<>();
		statMap.put("classifyName", classifyName);//小区名称
		statMap.put("count", count);//序号
		statMap.put("billCount", billCount);////账单个数
		statMap.put("sumDebitAmount", sumDebitAmount);//账单水费
		statMap.put("remark", "");//备注
		return statMap;
	}



}