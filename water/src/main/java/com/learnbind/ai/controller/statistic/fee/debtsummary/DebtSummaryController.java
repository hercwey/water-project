package com.learnbind.ai.controller.statistic.fee.debtsummary;

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
 * @Package com.learnbind.ai.controller.statistic.fee.debtsummary
 *
 * @Title: DebtSummaryController.java
 * @Description: 欠费分类统计
 * 		  两种方式
 * 			A:实际的价格
 * 			B:价格分类
 *
 * @author lenovo
 * @date 2019年9月7日 上午7:46:23
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class DebtSummaryController {
	private static Log log = LogFactory.getLog(DebtSummaryController.class);
	private static final String TEMPLATE_PATH = "statistic/fee/debtsummary/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	
	/**
	 * @Fields waterPriceService：水价
	 */
	@Autowired
	WaterPriceService waterPriceService;
	
	
	//------------------欠费统计,指定时间段内-------------------
		
		@RequestMapping(value = "/fee/debtsummary/main")
		public String statisticFeeDebtSummaryMain(Model model) {
			return TEMPLATE_PATH + "statistic_fee_debtsummary_main";
			
		}
		
		/**
		 * @Title: statisticFeeDebtSummaryTable
		 * @Description: 		查询-指定期间的欠费汇总统计(按价格)
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
		@RequestMapping(value = "/fee/debtsummary/table")
		public String statisticFeeDebtSummaryTable(Integer pageNum, 
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
			//欠费客户列表
			accountItemList=statisticService.searchDebtDetailList(periodStart, periodEnd, traceIds);  //查询指定期间的实收数据列表			
			PageInfo<Map<String,Object>> pageInfo = new PageInfo<>(accountItemList); 	   // (使用了拦截器或是AOP进行查询的再次处理)
			
			// 传递如下数据至前台页面
			model.addAttribute("dataList", accountItemList);  		//列表数据
			model.addAttribute("pageInfo", pageInfo);  				//分页数据
			model.addAttribute("searchCond", searchCond);  			//查询条件回传
			
			return TEMPLATE_PATH+"statistic_fee_debtsummary_table";
		}
		
		
		/**
		 * @Title: statisticFeeDebtSummaryChart
		 * @Description: 欠费分类-统计   饼状图
		 * @param periodStart  开始期间  yyyy-mm
		 * @param periodEnd		结束期间  yyyy-mm
		 * @param model
		 * @return 
		 */
		@RequestMapping(value = "/fee/debtsummary/chart")
		public String statisticFeeDebtSummaryChart(String periodStart,String periodEnd, Model model) {
			
			List<String> statPrpertyList = getStatPropertyList(); // 统计属性值列表
			
			//按价格类型统计
			List<Map<String, Object>> statDateList = statDebtByProperty(periodStart,periodEnd);  
			Map<String, Object> statData = packStatDataPie(statPrpertyList, statDateList); // 封装统计数据
			/*
			 * 封装后的结构如下所示: 
			 * legendList:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎'], 
			 * statData:[
			 * 		{value:335, name:'直接访问'}, 
			 * 		{value:310, name:'邮件营销'}, 
			 * 		{value:234, name:'联盟广告'},
			 * 		{value:135, name:'视频广告'}, 
			 * 		{value:1548, name:'搜索引擎'} ]
			 */

			model.addAttribute("statData", statData); // 统计数据
			//model.addAttribute("period", period); // 期间
			
			return TEMPLATE_PATH + "statistic_fee_debtsummary_pricetype_pie";
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
		private List<Map<String, Object>> statDebtByProperty(String periodStart,String periodEnd) {
			List<Map<String, Object>> statData = new ArrayList<>();
			
			List<Map<String,Object>> statResultList= statisticService.statDebtSummaryByPriceCode(periodStart,periodEnd);  //统计结果
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
					int readNum=((BigDecimal)map.get("STAT_AMOUNT")).intValue();  //水量				
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
		 * @Description: 打包成pie所需要的格式
		 * @param legendList
		 * @param statData
		 * @return 
		 */
		private Map<String, Object> packStatDataPie(List<String> legendList, List<Map<String, Object>> statData) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("legendList", legendList);
			tempMap.put("statData", statData);
			return tempMap;
		}
		
		/**
		 * @Title: getStatPropertyList
		 * @Description: 获取统计属性所对应的值列表
		 * @return 
		 * 		水价名称列表
		 * 		["绿化","消防","第一阶梯","第二阶梯"]
		 */
		private List<String> getStatPropertyList() {			
			List<SysWaterPrice>  waterPriceList=searchWaterPriceList();  //自数据字典中读取用水性质列表
			
			List<String> propertyValueList = new ArrayList<>();
			for(SysWaterPrice waterPrice:waterPriceList) {
				propertyValueList.add(waterPrice.getLadderName());
			}
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
		
		
	
}
