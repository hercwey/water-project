package com.learnbind.ai.controller.statistic.fee.recestat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.common.StatisticUtil;
import com.learnbind.ai.service.statistic.StatisticService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.statistic.fee.recestat
 *
 * @Title: ReceStatController.java
 * @Description: 应收统计(用户查询指定时间段内的应收)
 *
 * @author lenovo
 * @date 2019年9月5日 上午9:43:14
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class ReceStatController {
	private static Log log = LogFactory.getLog(ReceStatController.class);
	private static final String TEMPLATE_PATH = "statistic/fee/recestat/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	
	
	//------------------应收汇总,  指定时间段内------------------------
		
		@RequestMapping(value = "/fee/recestat/main")
		public String statisticFeeReceStatMain(Model model) {
			return TEMPLATE_PATH + "statistic_fee_recestat_main";
		}
		
		/**
		 * @Title: statisticWaterMonthAmountTable
		 * @Description: 		查询-指定期间的应收数据列表
		 * @param pageNum  		页号
		 * @param pageSize 		页大小
		 * @param searchCond   查询条件   JSON格式
		 * 		{
		 * 			periodStart:  yyyy-mm
		 * 			periodEnd:  yyyy-mm
		 * 		}
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/fee/recestat/table")
		public String statisticFeeReceStatTable(Integer pageNum, 
												  Integer pageSize,
												  String searchCond, 
												  Model model) {
			// 判定页码有效性
			if (pageNum == null || pageNum == 0) {
				pageNum = 1;
				pageSize = PAGE_SIZE;
			}
			
			log.debug("searchCond:"+searchCond);

			//取得查询参数
			JSONObject jsonObj=JSON.parseObject(searchCond);
			String periodStart=jsonObj.getString("periodStart");
			String periodEnd=jsonObj.getString("periodEnd");
			String traceIds=jsonObj.getString("traceIds");
			
			// 查询并分页
			PageHelper.startPage(pageNum, pageSize); // PageHelper
			List<Map<String,Object>> accountItemList = new ArrayList<>();		
			accountItemList=statisticService.searchReceivableList(periodStart,periodEnd, traceIds); //查询指定期间的应收数据列表			
			PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(accountItemList); 	// (使用了拦截器或是AOP进行查询的再次处理)
			
			
			List<Map<String,Object>> accountItemSumList=statisticService.statReceivableSum(periodStart,periodEnd, traceIds); //查询合计
			if((Map<String,Object>)accountItemSumList.get(0)==null) {
				accountItemSumList=new ArrayList<>();
			}
			model.addAttribute("accountItemSumList",accountItemSumList);  //向前台传递合计值
			
			// 传递如下数据至前台页面
			model.addAttribute("dataList", accountItemList);  		//列表数据
			model.addAttribute("pageInfo", pageInfo);  				//分页数据
			model.addAttribute("searchCond", searchCond);  			//查询条件回传
			
			return TEMPLATE_PATH+"statistic_fee_recestat_table";
		}
		
		/**
		 * @Title: statisticWaterMonthAmountChart
		 * @Description: 月售水量-统计   (按每天的售水量进行统计)   柱状图
		 * @param period  期间  yyyy-mm
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/fee/monthamount/chart")
		public String statisticWaterMonthAmountChart(String period, Model model) {			
			List<String> dayList = genXAxisDataMonth(period); // 天列表 x轴
			List<Integer> numPerDayList = null;//statWaterAmountPerDay(period); // 每天所售水量列表 y轴
			Map<String, Object> statData = packStatData(dayList, numPerDayList); // 封装统计数据

			model.addAttribute("statData", statData); // 统计数据
			model.addAttribute("period", period); // 期间

			//List<Map<String, Object>> testData = statisticService.test();

			return TEMPLATE_PATH + "statistic_water_month_amount_bar";
			
		}
		
		/**
		 * @Title: statWaterAmountPerDay
		 * @Description: 统计指定月份每天售水量.
		 * @param period 期间  yyyy-mm
		 * @return 天--->水量
		 */
		private List<Integer> statWaterAmountPerDay(String traceIds, String period) {
			String datex = period + "-01";
			int daysPerMonth = StatisticUtil.getDaysOfMonth(datex);
			
			//初始化每天数值列表
			List<Integer> numPerDayList = new ArrayList<>();
			for (int i = 0; i < daysPerMonth; i++) {
				numPerDayList.add(0);
			}
			
			//TODO 统计每天的售水量
			//自数据库中统计指定期间新增客户: 天--->售水量
			List<Map<String,Object>> statList=statisticService.statWaterAmountPerDay(traceIds, period);
			for(Map<String,Object> map:statList) {
				Date settleTime=(Date)map.get("DAY_DATE");   //格式yyyy-mm-dd
				int amount=((BigDecimal)map.get("WATER_AMOUNT")).intValue();
				
				//取出"dd-日"部分,以其为索引置新增人数
				int dayx=DateUtil.get(settleTime, "day");
				numPerDayList.set(dayx-1, amount);
			}
			
			return numPerDayList;
		}
		
		/**
		 * @Title: packStatData
		 * @Description: 封装统计数据
		 * @param xAxisData
		 * @param yAxisData
		 * @return statisticData 统计数据 格式 { periodList:[], //x轴: numPerPeriodList:[]
		 *         //y轴:数据列表 与时间一一对应 }
		 */
		private Map<String, Object> packStatData(List<String> xAxisData, List<Integer> yAxisData) {
			final String X_AXIS = "periodList";
			final String Y_AXIS = "numPerPeriodList";
			Map<String, Object> statData = new HashMap<>();
			statData.put("periodList", xAxisData); // 时间列表 x轴
			statData.put("numPerPeriodList", yAxisData); // 数据列表 y轴
			return statData;
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
