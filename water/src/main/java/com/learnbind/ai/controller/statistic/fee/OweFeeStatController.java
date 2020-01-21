package com.learnbind.ai.controller.statistic.fee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.StatisticUtil;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.statistic.StatisticService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.statistic.fee
 *
 * @Title: OweFeeStatController.java
 * @Description: 欠费表
 *
 * @author Administrator
 * @date 2019年11月9日 上午9:17:03
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/owe-fee-stat")
public class OweFeeStatController {
	private static Log log = LogFactory.getLog(OweFeeStatController.class);
	private static final String TEMPLATE_PATH = "statistic/fee/owefee/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private PartitionWaterService partitionWaterService;
	@Autowired
	private WaterPriceService waterPriceService;

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
	public String table(Model model, Integer pageNum, Integer pageSize, String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, String startDate, String endDate) {
		

		BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
		
		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		//Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper

		List<Map<String, Object>> accountItemMapList = statisticService.getListGroupByCustomerId(searchCond, traceIds, period, null, startDate, endDate);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)

//				I.CUSTOMER_ID,	客户ID
//				LISTAGG ( I.ID, ',' ) WITHIN GROUP ( ORDER BY I.ID ) AS BILL_IDS,	账单ID,多条记录用逗号","分隔
//				I.PERIOD,	期间
//				-- I.*,
//				SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,	账单金额
//				SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,	已充值金额
//				( SUM(I.CREDIT_AMOUNT) - SUM(I.DEBIT_AMOUNT) ) AS OWE_AMOUNT	欠费金额
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
//					String billIds = accountItemMap.get("BILL_IDS").toString();//账单ID集合,多个账单ID用逗号","分隔
//					String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			
			//String currTraceIds = (String)accountItemMap.get("TRACE_IDS");//地理位置
			
			String waterPrices = (String)accountItemMap.get("WATER_PRICE");//水价
			String waterUse = this.getWaterUse(waterPrices);
			accountItemMap.put("waterUse", waterUse);//用水性质
			
			//计算违约金总金额
			//BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			//BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			//String place = locationService.getPlace(currTraceIds);
			//accountItemMap.put("place", place);//地理位置
			
			//accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			//accountItemMap.put("overdueValue", overdueValue);//违约金总金额
			
			//查询往期水费欠费金额
			BigDecimal pastWaterFeeOweAmount = statisticService.getPastWaterFeeOweAmount(customerId, period);
			//查询往期违约金欠费金额
			//BigDecimal pastOverdueOweAmount = customerAccountItemService.getPastOverdueOweAmount(customerId, period);
			//往期欠费总金额=往期水费欠费金额+往期违约金欠费金额
			BigDecimal pastOweTotalAmount = pastWaterFeeOweAmount;//BigDecimalUtils.add(pastWaterFeeOweAmount, pastOverdueOweAmount);
			accountItemMap.put("pastOweTotalAmount", pastOweTotalAmount);
			
			//查询欠费月份
			List<String> oweFeePeriodList = statisticService.getWaterFeeOwePeriod(customerId, period);
			accountItemMap.put("oweFeePeriodList", oweFeePeriodList);
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，本期欠费金额+往期欠费金额
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweTotalAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);
			
			//获取贷方摘要
//					String creditSubject = "";
//					if(accountItemMap.get("CREDIT_SUBJECT") != null) {
//						creditSubject = AiSubjectUtils.getAiSubjectStr(accountItemMap.get("CREDIT_SUBJECT").toString());
//
//					}
//					accountItemMap.put("creditSubject", creditSubject);

		}

		// 传递如下数据至前台页面
		model.addAttribute("accountItemList", accountItemMapList); // 列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "account_table";
	}
	
	@RequestMapping(value = "/bar")
	public String bar(String period, String traceIds, Integer settlementStatus, Integer accountStatus, String searchCond, String startDate, String endDate) {
		

		BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
		
		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		//Long operatorId = this.getOperatorId();



		List<Map<String, Object>> accountItemMapList = statisticService.getListGroupByCustomerId(searchCond, traceIds, period, null, startDate, endDate);

//				I.CUSTOMER_ID,	客户ID
//				LISTAGG ( I.ID, ',' ) WITHIN GROUP ( ORDER BY I.ID ) AS BILL_IDS,	账单ID,多条记录用逗号","分隔
//				I.PERIOD,	期间
//				-- I.*,
//				SUM(I.CREDIT_AMOUNT) AS BILL_AMOUNT,	账单金额
//				SUM(I.DEBIT_AMOUNT) AS RECHARGE_AMOUNT,	已充值金额
//				( SUM(I.CREDIT_AMOUNT) - SUM(I.DEBIT_AMOUNT) ) AS OWE_AMOUNT	欠费金额
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
//					String billIds = accountItemMap.get("BILL_IDS").toString();//账单ID集合,多个账单ID用逗号","分隔
//					String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			
			BigDecimal oweAmount = new BigDecimal(accountItemMap.get("OWE_AMOUNT").toString());//账单欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			
			String currTraceIds = accountItemMap.get("TRACE_IDS").toString();//地理位置
			
			String waterPrices = (String)accountItemMap.get("WATER_PRICE");//水价
			String waterUse = this.getWaterUse(waterPrices);
			accountItemMap.put("waterUse", waterUse);//用水性质
			
			//计算违约金总金额
			//BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(accountItemId);
			//计算违约金欠费总金额
			//BigDecimal overdueOweAmount = customerAccountItemService.getOverdueBillOweAmountSum(accountItemId);
			
			String place = locationService.getPlace(currTraceIds);
			accountItemMap.put("place", place);//地理位置
			
			//accountItemMap.put("ACCOUNT_DATE_STR", accountDateStr);//记账日期
			//accountItemMap.put("DEBIT_AMOUNT", zero);
			//accountItemMap.put("overdueValue", overdueValue);//违约金总金额
			
			//查询往期水费欠费金额
			BigDecimal pastWaterFeeOweAmount = statisticService.getPastWaterFeeOweAmount(customerId, period);
			//查询往期违约金欠费金额
			//BigDecimal pastOverdueOweAmount = customerAccountItemService.getPastOverdueOweAmount(customerId, period);
			//往期欠费总金额=往期水费欠费金额+往期违约金欠费金额
			BigDecimal pastOweTotalAmount = pastWaterFeeOweAmount;//BigDecimalUtils.add(pastWaterFeeOweAmount, pastOverdueOweAmount);
			accountItemMap.put("pastOweTotalAmount", pastOweTotalAmount);
			
			//查询欠费月份
			List<String> oweFeePeriodList = statisticService.getWaterFeeOwePeriod(customerId, period);
			accountItemMap.put("oweFeePeriodList", oweFeePeriodList);
			
			// 计算欠费金额，欠费金额=贷方金额（账单金额）-借方金额（充值金额）
			//计算总欠费金额，本期欠费金额+往期欠费金额
			BigDecimal totalOweAmount = BigDecimalUtils.add(oweAmount, pastOweTotalAmount);
			accountItemMap.put("totalOweAmount", totalOweAmount);
			
			//获取贷方摘要
//					String creditSubject = "";
//					if(accountItemMap.get("CREDIT_SUBJECT") != null) {
//						creditSubject = AiSubjectUtils.getAiSubjectStr(accountItemMap.get("CREDIT_SUBJECT").toString());
//
//					}
//					accountItemMap.put("creditSubject", creditSubject);

		}

		return TEMPLATE_PATH + "account_table";
	}
	
	/**
	 * @Title: getWaterUse
	 * @Description: 获取用水性质
	 * @param waterPrices
	 * @return 
	 */
	private String getWaterUse(String waterPrices) {
		if(StringUtils.isNotBlank(waterPrices)) {
			String[] waterPriceArr = waterPrices.split(",");
			if(waterPriceArr!=null && waterPriceArr.length>0) {
				String waterPrice = waterPriceArr[0];
				SysWaterPrice wp = waterPriceService.getWaterPriceByPriceCode(waterPrice);
				return wp.getLadderName();
			}
		}
		return null;
	}

	/**
	 * @Title: genXAxisDataMonth
	 * @Description: 生成x轴数据(天/月)
	 * @param daysPerMonth 天数
	 * @return 天列表 1,2,3,4,...31
	 */
	private List<String> genXAxisDataMonth(String period) {
		String datex = period + "-01";
		int daysPerMonth = StatisticUtil.getDaysOfMonth(datex);
		List<String> periodList = new ArrayList<>();
		for (int i = 0; i < daysPerMonth; i++) {
			periodList.add(Integer.toString(i + 1));
		}
		return periodList;
	}

	/**
	 * @Title: genXAxisDataYear
	 * @Description:年月份列表
	 * @return 1,2,3,...12
	 */
	private List<String> genXAxisDataYear() {
		List<String> periodList = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			periodList.add(Integer.toString(i + 1) + "月");
		}
		return periodList;
	}

}
