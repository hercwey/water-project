package com.learnbind.ai.controller.statistic.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumPeriodType;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
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
 * @Package com.learnbind.ai.controller.invoice
 *
 * @Title: StatBlockWaterFeeController.java
 * @Description: 统计小区水费前端控制器
 *
 * @author Administrator
 * @date 2019年8月31日 下午3:22:31
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat-block-water-fee")
public class StatBlockWaterFeeController {
	private static Log log = LogFactory.getLog(StatBlockWaterFeeController.class);
	private static final String TEMPLATE_PATH = "statistic/finance/blockwaterfee/"; // 页面目录
	
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
		return TEMPLATE_PATH + "water_fee_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		List<EnumAiDebitSubjectPayment> subjectPaymentList = EnumAiDebitSubjectPayment.getSettlePayment();//缴费时的支付方式
		model.addAttribute("subjectPaymentList", subjectPaymentList);//缴费时的支付方式
		
		
		return TEMPLATE_PATH + "water_fee_main";
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
	public String table(Model model, String searchPeriod, String searchSubjectPayment, Integer periodType) {

		Long operatorId = this.getOperatorId();//操作员ID
		//支付类型：借贷状态+借方科目动作（110表示交水费（综合水费））
		String payType = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_WATER_FEE.getKey();
		String debitSubject = payType+searchSubjectPayment;//借方科目=支付类型+支付方式
		
		StatClassify searchObj = new StatClassify();
		searchObj.setPid(0l);
		searchObj.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<StatClassify> classifyList = statClassifyService.select(searchObj);//查询统计小区节点
		
		List<Map<String, Object>> statMapList = new ArrayList<>();//统计结果集合
		int count = 0;//序号
		for(StatClassify classify : classifyList) {
//			Map<String, Object> statMap = new HashMap<>();
//			statMap.put("name", classify.getClassifyName());//小区名称
			
			List<String> traceIdsList = this.getTraceIdsList(classify.getId());
//			COUNT(1) AS COUNT,
//			SUM( PW.REAL_WATER_AMOUNT ) AS SUM_REAL_WATER_AMOUNT,
//			MAX( PW.BASE_PRICE ) AS BASE_PRICE,
//			MAX( PW.TREATMENT_FEE ) AS SEWAGE_FEE,
//			SUM( PW.WATER_FEE ) AS SUM_WATER_FEE,
//			SUM( I.DEBIT_AMOUNT ) AS SUM_DEBIT_AMOUNT
			List<Map<String, Object>> statBillMapList = new ArrayList<>();
			if(periodType==EnumPeriodType.PERIOD_CURR.getKey()) {//本期
				statBillMapList = customerAccountItemService.getStatSettlementBillList(searchPeriod, debitSubject, traceIdsList);
			}else {//往期
				statBillMapList = customerAccountItemService.getStatPastSettlementBillList(searchPeriod, debitSubject, traceIdsList);
			}
			for(Map<String, Object> statBillMap : statBillMapList) {
				count = count+1;
				Map<String, Object> statMap = this.getStatMap(count, classify.getClassifyName(), statBillMap);
				statMapList.add(statMap);
			}
		}
		
		//按单价统计所有小区账单
		List<Map<String, Object>> statAllBlockBillList = this.getStatAllBlock(classifyList, searchPeriod, debitSubject);
		//统计合计
		Map<String, BigDecimal> totalAmountMap = this.getStatTotal(statAllBlockBillList);
		
		//返回页面参数
		model.addAttribute("statMapList", statMapList);
		model.addAttribute("totalAmountMap", totalAmountMap);
		model.addAttribute("statAllBlockBillList", statAllBlockBillList);
		
		return TEMPLATE_PATH + "water_fee_table";
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
	 * @Title: getAllTraceIdsList
	 * @Description: 获取当前小区的所有traceIds
	 * @param classifyList
	 * @return 
	 */
	private List<String> getAllTraceIdsList(List<StatClassify> classifyList){
		List<String> traceIdsList = new ArrayList<>();
		for(StatClassify classify : classifyList) {
			List<String> tempTraceIdsList = this.getTraceIdsList(classify.getId());
			for(String tempTraceIds : tempTraceIdsList) {
				traceIdsList.add(tempTraceIds);
			}
		}
		return traceIdsList;
	}
	
	/**
	 * @Title: getStatTotal
	 * @Description: 统计合计
	 * @param classifyList
	 * @return 
	 */
	private List<Map<String, Object>> getStatAllBlock(List<StatClassify> classifyList, String searchPeriod, String debitSubject){
		//统计合计
		List<String> traceIdsList = this.getAllTraceIdsList(classifyList);//获取traceIds
//		COUNT(1) AS COUNT,
//		SUM( PW.REAL_WATER_AMOUNT ) AS SUM_REAL_WATER_AMOUNT,
//		MAX( PW.BASE_PRICE ) AS BASE_PRICE,
//		MAX( PW.TREATMENT_FEE ) AS SEWAGE_FEE,
//		SUM( PW.WATER_FEE ) AS SUM_WATER_FEE,
//		SUM( I.DEBIT_AMOUNT ) AS SUM_DEBIT_AMOUNT
		List<Map<String, Object>> statBillMapList = customerAccountItemService.getStatSettlementBillList(searchPeriod, debitSubject, traceIdsList);
		List<Map<String, Object>> statMapList = new ArrayList<>();
		for(Map<String, Object> statBillMap : statBillMapList) {
			Map<String, Object> statMap = this.getStatMap(0, null, statBillMap);
			statMapList.add(statMap);
		}
		return statMapList;
	}
	
	/**
	 * @Title: getStatTotal
	 * @Description: 统计合计
	 * @param statMapList
	 * 				//		COUNT(1) AS COUNT,
					//		SUM( PW.REAL_WATER_AMOUNT ) AS SUM_REAL_WATER_AMOUNT,
					//		MAX( PW.BASE_PRICE ) AS BASE_PRICE,
					//		MAX( PW.TREATMENT_FEE ) AS SEWAGE_FEE,
					//		SUM( PW.WATER_FEE ) AS SUM_WATER_FEE,
					//		SUM( I.DEBIT_AMOUNT ) AS SUM_DEBIT_AMOUNT
	 * @return 
	 */
	private Map<String, BigDecimal> getStatTotal(List<Map<String, Object>> statMapList){
		Map<String, BigDecimal> totalAmountMap = new HashMap<>();
		BigDecimal totalRealWaterAmount = new BigDecimal(0.00);//实际水量
		BigDecimal totalBaseWaterFee = new BigDecimal(0.00);//基础水费
		BigDecimal totalSewageWaterFee = new BigDecimal(0.00);//污水水费
		BigDecimal totalDebitAmount = new BigDecimal(0.00);//水污合计
		for(Map<String, Object> statMap : statMapList) {
			
//			statMap.put("classifyName", classifyName);//小区名称
//			statMap.put("count", count);//序号
//			statMap.put("billCount", billCount);////账单个数
//			statMap.put("sumRealWaterAmount", sumRealWaterAmount);//实际水量
//			statMap.put("basePrice", basePrice);//水费单价
//			statMap.put("sewagePrice", sewagePrice);//污水费单价
//			statMap.put("baseWaterFee", baseWaterFee);//水费金额
//			statMap.put("sewageWaterFee", sewageWaterFee);//污水费金额
//			statMap.put("sumWaterFee", sumWaterFee);//分水量水费
//			statMap.put("sumDebitAmount", sumDebitAmount);//水污合计
//			statMap.put("remark", "");//备注
			
			
			BigDecimal sumRealWaterAmount = (BigDecimal)statMap.get("sumRealWaterAmount");//实际水量
			BigDecimal baseWaterFee = (BigDecimal)statMap.get("baseWaterFee");//基础水费
			BigDecimal sewageWaterFee = (BigDecimal)statMap.get("sewageWaterFee");//污水水费
			//BigDecimal sumWaterFee = (BigDecimal)statMap.get("sumWaterFee");//分水量水费
			BigDecimal sumDebitAmount = (BigDecimal)statMap.get("sumDebitAmount");//水污合计
			
			totalRealWaterAmount = BigDecimalUtils.add(totalRealWaterAmount, sumRealWaterAmount);
			totalBaseWaterFee = BigDecimalUtils.add(totalBaseWaterFee, baseWaterFee);
			totalSewageWaterFee = BigDecimalUtils.add(totalSewageWaterFee, sewageWaterFee);
			totalDebitAmount = BigDecimalUtils.add(totalDebitAmount, sumDebitAmount);
		}
		
		totalAmountMap.put("totalRealWaterAmount", totalRealWaterAmount);
		totalAmountMap.put("totalBaseWaterFee", totalBaseWaterFee);
		totalAmountMap.put("totalSewageWaterFee", totalSewageWaterFee);
		totalAmountMap.put("totalDebitAmount", totalDebitAmount);
		return totalAmountMap;
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
		BigDecimal sumRealWaterAmount = (BigDecimal)statBillMap.get("SUM_REAL_WATER_AMOUNT");//实际水量
		BigDecimal basePrice = (BigDecimal)statBillMap.get("BASE_PRICE");//基础水价
		BigDecimal sewagePrice = (BigDecimal)statBillMap.get("SEWAGE_FEE");//污水水价
		BigDecimal sumWaterFee = (BigDecimal)statBillMap.get("SUM_WATER_FEE");//分水量水费
		BigDecimal sumDebitAmount = (BigDecimal)statBillMap.get("SUM_DEBIT_AMOUNT");//账单水费
		
		BigDecimal baseWaterFee = BigDecimalUtils.multiply(sumRealWaterAmount, basePrice);//基础水费
		BigDecimal sewageWaterFee = BigDecimalUtils.multiply(sumRealWaterAmount, sewagePrice);//污水水费
		
		Map<String, Object> statMap = new HashMap<>();
		statMap.put("classifyName", classifyName);//小区名称
		statMap.put("count", count);//序号
		statMap.put("billCount", billCount);////账单个数
		statMap.put("sumRealWaterAmount", sumRealWaterAmount);//实际水量
//		statMap.put("basePrice", basePrice);//基础水价
//		statMap.put("sewagePrice", sewagePrice);//污水水价
//		statMap.put("baseWaterFee", baseWaterFee);//基础水费
//		statMap.put("sewageWaterFee", sewageWaterFee);//污水水费
//		statMap.put("sumWaterFee", sumWaterFee);//分水量水费
//		statMap.put("sumDebitAmount", sumDebitAmount);//账单水费
		statMap.put("basePrice", basePrice);//水费单价
		statMap.put("sewagePrice", sewagePrice);//污水费单价
		statMap.put("baseWaterFee", baseWaterFee);//水费金额
		statMap.put("sewageWaterFee", sewageWaterFee);//污水费金额
		statMap.put("sumWaterFee", sumWaterFee);//分水量水费
		statMap.put("sumDebitAmount", sumDebitAmount);//水污合计
		statMap.put("remark", "");//备注
		return statMap;
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