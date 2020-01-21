package com.learnbind.ai.controller.statistic.water.monthsummary;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.statistic.StatisticService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.statistic.water.monthsummary
 *
 * @Title: WaterMonthSummaryController.java
 * @Description: 售水量-月统计汇总
 *
 * @author lenovo
 * @date 2019年9月1日 下午10:50:07
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class WaterMonthSummaryController {
	private static Log log = LogFactory.getLog(WaterMonthSummaryController.class);
	private static final String TEMPLATE_PATH = "statistic/water/monthsummary/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	
	/**
	 * @Fields dataDictService：数据字典-服务
	 */
	@Autowired
	private WaterPriceService waterPriceService;
	
	
	//------------------售水量汇总-月------------------------
		/**
		 * @Title: statisticWaterMonthSummaryMain
		 * @Description: 售水量-月汇总主页
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/water/monthsummary/main")
		public String statisticWaterMonthSummaryMain(Model model) {
			return TEMPLATE_PATH + "statistic_water_month_summary_main";
		}
		
		/**
		 * @Title: statisticWaterMonthSummaryTable
		 * @Description: 		查询-指定月售水记录(分水量记录)
		 * @param pageNum  		页号
		 * @param pageSize 		页大小
		 * @param searchCond   查询条件  yyyy-mm
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/water/monthsummary/table")
		public String statisticWaterMonthSummaryTable(Integer pageNum, 
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
			partRecordList=statisticService.searchPartRecordPerMonth(traceIds, period);   //查询指定月份分水量记录
			PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(partRecordList); // (使用了拦截器或是AOP进行查询的再次处理)
			
			// 传递如下数据至前台页面
			model.addAttribute("dataList", partRecordList);  		//列表数据
			model.addAttribute("pageInfo", pageInfo);  				//分页数据
			model.addAttribute("searchCond", searchCond);  			//查询条件回传
			
			return TEMPLATE_PATH+"statistic_water_month_summary_table";
		}
		
		/**
		 * @Title: statisticWaterMonthChart
		 * @Description: 售水量-月汇总  pie饼图  按价格 PRICE_CODE
		 * @param period  期间  yyyy-mm
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/water/monthsummary/chart")
		public String statisticWaterMonthChart(String searchCond, Model model) {			
			JSONObject parmsObj = JSON.parseObject(searchCond);
			String traceIds = parmsObj.getString("traceIds");//地理位置traceIds
			String period = parmsObj.getString("period");//期间
			List<String> statPrpertyList = getStatPropertyList(); // 统计属性值列表
			List<Map<String, Object>> statDateList = statWaterAmountByProperty(period, traceIds); 		//分类汇总售水量
			Map<String, Object> statData = packStatDataPie(statPrpertyList, statDateList); 		// 封装统计数据
			/*
			 * 封装后的结构如下所示: 
			 * legendList:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎'],
			 *  statData:[
			 * 		{value:335, name:'直接访问'},
			 * 		{value:310, name:'邮件营销'},
			 * 		{value:234, name:'联盟广告'},
			 * 		{value:135, name:'视频广告'}, 
			 * 		{value:1548, name:'搜索引擎'} ]
			 */

			model.addAttribute("statData", statData); 	// 统计数据
			model.addAttribute("period", period); 		// 期间

			return TEMPLATE_PATH + "statistic_water_month_summary_pie";
		}
	
		/**
		 * @Title: getStatPropertyList
		 * @Description: 获取统计属性所对应的值列表
		 * @return 
		 * 		用水性质-水价(包括各个阶梯)
		 */
		private List<String> getStatPropertyList() {			
			List<SysWaterPrice>  waterPriceList=searchWaterPriceList();  //自数据字典中读取用水性质列表
			
			List<String> propertyValueList = new ArrayList<>();
			for(SysWaterPrice waterPrice:waterPriceList) {
				propertyValueList.add(waterPrice.getLadderName());
			}
			
//			List<String> propertyValueList = new ArrayList<>();
//			propertyValueList.add("绿化");
//			propertyValueList.add("生活");
//			propertyValueList.add("消防");
//			propertyValueList.add("商业");
			return propertyValueList;
		}
		
		/**
		 * @Title: searchWaterUseList
		 * @Description: 自数据字典中获取用水性质(包括各阶梯).
		 * @return 
		 */
		private List<SysWaterPrice> searchWaterPriceList(){
			//自数据字典中读取列表			
			List<SysWaterPrice>  waterPriceList=waterPriceService.selectAll();
			return waterPriceList;
		}
		
		/**
		 * @Title: statWaterMonthSummary
		 * @Description: 售水量月汇总统计  --按用水性质
		 * @param period  yyyy-mm
		 * @return 
		 */
		private List<Map<String, Object>> statWaterMonthSummary(String period, String traceIds){			
			return statisticService.statWaterMonthSummary(period, traceIds);			
		}
		
		/*
		 * 
		 * [ 
		 * 	{value:335, name:'直接访问'}, 
		 * 	{value:310, name:'邮件营销'}, 
		 * 	{value:234, name:'联盟广告'}, 
		 * 	{value:135, name:'视频广告'}, 
		 * 	{value:1548, name:'搜索引擎'} 
		 * ]
		 * 
		 */
		// TODO 需要根据业务进行调整
		private List<Map<String, Object>> statWaterAmountByProperty(String period, String traceIds) {
			List<Map<String, Object>> statData = new ArrayList<>();
			
			List<Map<String,Object>> statResultList= statWaterMonthSummary(period, traceIds);  //统计结果
			List<SysWaterPrice> waterPriceList= searchWaterPriceList();  //用水性质列表
			
			//按用水性质初始化抄表初始值为0
//			[
//			 {
//				value:0,
//				name:'xxx',
//				key:'jumin'
//			 },
//			 {}
//			 ]
			for (SysWaterPrice waterPrice : waterPriceList) {
				Map<String, Object> tempMap = new HashMap<>();
				tempMap.put("value", 0);				
				tempMap.put("name", waterPrice.getLadderName());
				tempMap.put("key", waterPrice.getPriceCode());				
				statData.add(tempMap);
			}
			
			//建立索引   key(用水性质)--->在汇总列表中的索引idx
//			{
//				jumin:0,
//				fiejumin:1,				
//			}
			Map<String,Integer>  indexMap=new HashMap<>();
			for(int i=0;i<statData.size();i++) {
				indexMap.put((String)statData.get(i).get("key"), i);
			}
			
			//将统计数据置于汇总列表中
			for(Map<String,Object> map:statResultList) {
				String key=(String)map.get("PRICE_CODE");  //水价代码, 统计结果中可能存在null值
				if(StringUtils.isNotBlank(key)) {
					int readNum=((BigDecimal)map.get("WATER_AMOUNT")).intValue();  //水量				
					int idx=indexMap.get(key);				
					statData.get(idx).put("value", readNum);
				}								
			}
			
			//将汇总列表项中的key移除
			for(int i=0;i<statData.size();i++) {
				statData.get(i).remove("key");
			}			
			
			return statData;
		}
		
		/**
		 * @Title: packStatDataPie
		 * @Description: 打包饼图所需要的数据
		 * @param legendList  图例列表
		 * @param statData	    统计汇总数据
		 * @return 
		 * 				{
		 * 					legendList:object1,
		 * 					statData:object2
		 * 				}
		 */
		private Map<String, Object> packStatDataPie(List<String> legendList, List<Map<String, Object>> statData) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("legendList", legendList);
			tempMap.put("statData", statData);
			return tempMap;
		}
	
	
}
