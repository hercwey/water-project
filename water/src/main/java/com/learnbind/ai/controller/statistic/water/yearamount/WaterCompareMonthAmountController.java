package com.learnbind.ai.controller.statistic.water.yearamount;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.learnbind.ai.controller.statistic.StatisticController;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.statistic.StatisticService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.statistic.water.yearamount
 *
 * @Title: WaterCompareMonthAmountController.java
 * @Description: 月售水量对比
 *
 * @author Thinkpad
 * @date 2020年1月3日 上午10:51:36
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class WaterCompareMonthAmountController {
	private static Log log = LogFactory.getLog(WaterCompareMonthAmountController.class);
	private static final String TEMPLATE_PATH = "statistic/water/compare_month_amount/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	
	
	//------------------售水量汇总-年------------------------
		/**
		 * @Title: statisticWaterYearAmountMain
		 * @Description: 水量-年汇总主页
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/water/comparemonth/main")
		public String statisticWaterCompareMonthMain(Model model) {
			return TEMPLATE_PATH + "statistic_water_compare_month_amount_main";
		}
		
		/**
		 * @Title: statisticWaterYearAmountTable
		 * @Description: 		查询-指定月-售水量记录(分水量记录)
		 * @param pageNum  		页号
		 * @param pageSize 		页大小
		 * @param searchCond   查询条件  yyyy-mm
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/water/comparemonth/table")
		public String statisticWaterCompareMonthTable(Integer pageNum, 
												  Integer pageSize,
												  String searchCond, 
												  Model model) {
			// 判定页码有效性
			if (pageNum == null || pageNum == 0) {
				pageNum = 1;
				pageSize = PAGE_SIZE;
			}
			
			log.debug("searchCond"+searchCond);
			JSONObject parmsObj = JSON.parseObject(searchCond);
			String traceIds = parmsObj.getString("traceIds");//地理位置traceIds
			String period = parmsObj.getString("period");//期间
			// 查询并分页
			PageHelper.startPage(pageNum, pageSize); // PageHelper
			List<Map<String,Object>> partRecordList = new ArrayList<>();		
			partRecordList=statisticService.searchPartRecordPerYear(period, traceIds);   	//查询指定年份分水量记录
			PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(partRecordList); // (使用了拦截器或是AOP进行查询的再次处理)
			
			
			//model.addAttribute("totalWaterAmount", attributeValue);//统计总水量
			
			// 传递如下数据至前台页面
			model.addAttribute("dataList", partRecordList);  		//列表数据
			model.addAttribute("pageInfo", pageInfo);  				//分页数据
			model.addAttribute("searchCond", searchCond);  			//查询条件回传
			
			return TEMPLATE_PATH+"statistic_water_compare_month_amount_table";
		}
		
		/**
		 * @Title: statisticWaterYearAmountChart
		 * @Description: 年售水量-统计   (按每天的售水量进行统计)   柱状图
		 * @param period  期间  yyyy-mm
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/water/comparemonth/chart")
		public String statisticWaterCompareMonthChart(String searchCond, Model model) {
			JSONObject parmsObj = JSON.parseObject(searchCond);
			String traceIds = parmsObj.getString("traceIds");//地理位置traceIds
			String period = parmsObj.getString("period");//期间
			List<String> dayList = genXAxisDataYear(); // 月份列表 x轴(数据)
			List<Integer> numPerDayList = statWaterCompareMonthPerMonth(period, traceIds); // 每月所售水量列表 y轴(数据)
			Map<String, Object> statData = packStatData(dayList, numPerDayList); // 封装统计数据

			model.addAttribute("statData", statData); // 统计数据
			model.addAttribute("period", period); // 期间

			//List<Map<String, Object>> testData = statisticService.test();

			return TEMPLATE_PATH + "statistic_water_compare_month_amount_bar";
			
		}
		
		/**
		 * @Title: statWaterAmountPerMonth
		 * @Description: 统计指定月份每月售水量.
		 * @param period 期间  yyyy
		 * @return 月--->水量
		 */
		private List<Integer> statWaterCompareMonthPerMonth(String period, String traceIds) {
			final int MONTHS_PER_YEAR=12;
			List<Integer> numPerMonthList = new ArrayList<>();		
			for (int i = 0; i < MONTHS_PER_YEAR; i++) {
				numPerMonthList.add(0);
			}		
			//自数据库中统计指定期间新增客户.月份--->人数
			List<Map<String,Object>> statList=statisticService.statWaterAmountPerMonth(period, traceIds);
			for(Map<String,Object> map:statList) {
				Date date=(Date)map.get("MONTH_DATE");  //yyyy-mm
				int amount=((BigDecimal)map.get("WATER_AMOUNT")).intValue();  //数量
				
				int monthx=DateUtil.get(date, "month");
				numPerMonthList.set(monthx-1, amount);
			}
			return numPerMonthList;
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
