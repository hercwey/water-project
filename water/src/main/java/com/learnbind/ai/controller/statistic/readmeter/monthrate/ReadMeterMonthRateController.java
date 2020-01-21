package com.learnbind.ai.controller.statistic.readmeter.monthrate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learnbind.ai.service.statistic.StatisticService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.statistic.readmeter.monthrate
 *
 * @Title: ReadMeterMonthRateController.java
 * @Description: 抄表率-月统计
 *
 * @author lenovo
 * @date 2019年8月31日 上午10:55:47
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class ReadMeterMonthRateController {
	private static Log log = LogFactory.getLog(ReadMeterMonthRateController.class);
	private static final String TEMPLATE_PATH = "statistic/readmeter/monthrate/"; // 页面目录
	//private static final int PAGE_SIZE = 8; // 页大小

	/**
	 * @Fields statisticService：统计-服务
	 */
	@Autowired
	StatisticService statisticService;
	
	//------------------抄表汇总-月------------------------
	/**
	 * @Title: statisticReadMeterMonthRateMain
	 * @Description: 抄表率-月统计	 
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/readmeter/monthrate/main")
	public String statisticReadMeterMonthRateMain(Model model) {
		return TEMPLATE_PATH + "statistic_readmeter_month_rate_main";
	}
	
	/**
	 * @Title: statisticReadMeterMonthRateTable
	 * @Description: 		查询-指定月抄表率数据列表
	 * @param pageNum  		页号
	 * @param pageSize 		页大小
	 * @param searchCond   查询条件  yyyy-mm
	 * @param model
	 * @return 
	 * 		数据格式:
	 * 			[
	 * 				{
	 * 					readType: "全部",     // 可选值:  全部,己抄,未抄,抄表率
	 * 					amount:   xxx    // 数量
	 * 				},
	 * 				{}
	 * 			]
	 */
	@RequestMapping(value = "/readmeter/monthrate/table")
	public String statisticReadMeterMonthRateTable(Integer pageNum, 
											  Integer pageSize,
											  String searchCond, 
											  String traceIds, 
											  Model model) {
		
		log.debug("searchCond"+searchCond);
		
		List<String> itemNameList=genItemNameList();	//表格中条目名称	
		List<String> itemValueList=statReadMeterRate(searchCond, traceIds);  //统计抄表率.  统计需抄表数量,统计实际抄表数量
		List<Map<String,Object>> readRateList =packReadRateList(itemNameList,itemValueList);  
		
		model.addAttribute("readRateList", readRateList);		
		model.addAttribute("searchCond", searchCond);  			//查询条件回传			
		
		return TEMPLATE_PATH+"statistic_readmeter_month_rate_table";
	}
	
	/**
	 * @Title: genItemNameList
	 * @Description: 统计项目名称列表
	 * @return
	 * 	["应抄数", " 实抄数", "未抄数","抄表率" ] 
	 */   
	private List<String>  genItemNameList(){
		List<String> itemNameList=new ArrayList<>();
		itemNameList.add("应抄数");
		itemNameList.add("实抄数");
		itemNameList.add("未抄数");
		itemNameList.add("抄表率");
		return itemNameList;
	}
	/**
	 * @Title: searchReadMeterRate
	 * @Description: 统计抄表率数据
	 * @param period
	 * @return 
	 * 		[应抄数,  实抄数, 未抄数,抄表率 ]
	 */
	private List<String> statReadMeterRate(String period, String traceIds){
		int meterAmount=statisticService.statNeedReadMeterAmount(traceIds);  			//应抄表数
		int readMeterAmount=statisticService.statHasReadMeterAmount(period, traceIds);  	//实抄表数
		
		List<String>  itemValueList=new ArrayList<>();		
		itemValueList.add(Integer.toString(meterAmount));   				//全部
		itemValueList.add(Integer.toString(readMeterAmount));  				//己抄
		itemValueList.add(Integer.toString(meterAmount-readMeterAmount));  	//未抄数量
		float readRate=(float)readMeterAmount/meterAmount*100;  //抄表率		
		String readRateStr = fmtFloat(readRate)+"%";  //抄表率格式化 		
		itemValueList.add(readRateStr);			
		
		return itemValueList;
	}
	/**
	 * @Title: fmtFloat
	 * @Description: 格式化浮点数,保留两部小数
	 * @param f  需格式化的数据
	 * @return 格式化的字符串
	 */
	private String fmtFloat(float f) {
		DecimalFormat df = new DecimalFormat("0.00");//格式化小数   
		String fStr = df.format(f);//返回的是String类型
		return fStr;
	}
	/**
	 * @Title: packReadRateList
	 * @Description: 打包成抄表率列表
	 * @param itemNameList
	 * @param itemValueList
	 * @return 
	 * [
	 * 	{
	 * 		readType:"",
	 * 		amount:xxx
	 * 	},
	 * 	{}
	 * ]
	 */
	private List<Map<String,Object>> packReadRateList(List<String> itemNameList,List<String> itemValueList){
		List<Map<String,Object>> readRateList =new ArrayList<>();
		for(int i=0;i<itemNameList.size();i++) {
			Map<String, Object> tempMap=new HashMap<>();
			tempMap.put("readType", itemNameList.get(i));
			tempMap.put("amount", itemValueList.get(i));
			readRateList.add(tempMap);
		}
		return readRateList;
			
	}
	/**
	 * @Title: statisticReadMeterMonthChart
	 * @Description: 抄表数月汇总,.统计汇总当月抄表  pie饼图
	 * @param period  期间  yyyy-mm
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/readmeter/monthrate/chart")
	public String statisticReadMeterMonthChart(String period, Model model, String traceIds) {			

		List<String> statPrpertyList = getStatPropertyList(); // 统计属性值列表
		List<Map<String, Object>> statDateList = statReadMeterRateByProperty(period,statPrpertyList, traceIds); 	// 抄表数量
		Map<String, Object> statData = packStatDataPie(statPrpertyList, statDateList); 			// 封装统计数据
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

		return TEMPLATE_PATH + "statistic_readmeter_month_rate_pie";
	}

	/**
	 * @Title: getStatPropertyList
	 * @Description: 获取统计属性所对应的值列表
	 * @return
	 * 	["己抄","未抄"]
	 */
	private List<String> getStatPropertyList() {
		List<String> propertyValueList = new ArrayList<>();		
		propertyValueList.add("己抄");
		propertyValueList.add("未抄");
		return propertyValueList;
	}
	
	/**
	 * @Title: statReadMeterRateByProperty
	 * @Description: 生成统计图表所需要的数据结构
	 * @param period
	 * @param itemNameList
	 * @return
	 * 
	 *  [ 
	 * 	{value:335, name:'直接访问'}, 
	 * 	{value:310, name:'邮件营销'}, 
	 * 	{value:234, name:'联盟广告'}, 
	 * 	{value:135, name:'视频广告'}, 
	 * 	{value:1548, name:'搜索引擎'} 
	 * ]
	 */
	private List<Map<String, Object>> statReadMeterRateByProperty(String period,List<String> itemNameList, String traceIds) {
		List<Map<String, Object>> statData = new ArrayList<>();
		
		int meterAmount=statisticService.statNeedReadMeterAmount(traceIds);  			//应抄表数
		int readMeterAmount=statisticService.statHasReadMeterAmount(period, traceIds);  	//实抄表数
		
		List<String>  itemValueList=new ArrayList<>();
		//itemValueList.add(Integer.toString(meterAmount));   				//全部
		itemValueList.add(Integer.toString(readMeterAmount));  				//己抄
		itemValueList.add(Integer.toString(meterAmount-readMeterAmount));  	//未抄
		
		for(int i=0;i<itemNameList.size();i++) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("value", itemValueList.get(i));				
			tempMap.put("name", itemNameList.get(i));							
			statData.add(tempMap);
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
