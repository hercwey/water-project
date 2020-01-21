package com.learnbind.ai.controller.statistic.readmeter.monthsummary;

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

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.controller.statistic.StatisticController;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.statistic.StatisticService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.statistic.readmeter.monthsummary
 *
 * @Title: ReadMeterMonthSummaryController.java
 * @Description: 抄表月统计汇总
 *
 * @author lenovo
 * @date 2019年8月30日 下午11:09:29
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class ReadMeterMonthSummaryController {
	private static Log log = LogFactory.getLog(StatisticController.class);
	private static final String TEMPLATE_PATH = "statistic"; // 页面目录
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
	private DataDictService dataDictService;

	// ------------------抄表汇总-月------------------------
	/**
	 * @Title: statisticReadMeterMonthSummaryMain
	 * @Description: 抄表-月汇总主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readmeter/monthsummary/main")
	public String statisticReadMeterMonthSummaryMain(Model model) {
		return TEMPLATE_PATH + "/readmeter/monthsummary/statistic_readmeter_month_summary_main";
	}

	/**
	 * @Title: statisticReadMeterMonthTable
	 * @Description: 查询-指定月抄表记录
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件 yyyy-mm
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readmeter/monthsummary/table")
	public String statisticReadMeterMonthSummaryTable(Integer pageNum, Integer pageSize, String searchCond,
			String traceIds, Model model) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		log.debug("searchCond" + searchCond);

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> readRecordList = new ArrayList<>();
		readRecordList = statisticService.searchReadMeterRecordPerMonth(searchCond, traceIds); // 查询抄表记录
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(readRecordList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("readRecordList", readRecordList); // 列表数据
		model.addAttribute("pageInfo", pageInfo); // 分页数据
		model.addAttribute("searchCond", searchCond); // 查询条件回传

		return TEMPLATE_PATH + "/readmeter/monthsummary/statistic_readmeter_month_summary_table";
	}

	/**
	 * @Title: statisticReadMeterMonthChart
	 * @Description: 抄表数月汇总,.统计汇总当月抄表 pie饼图
	 * @param period 期间 yyyy-mm
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readmeter/monthsummary/chart")
	public String statisticReadMeterMonthChart(String period, String traceIds, Model model) {

		List<String> statPrpertyList = getStatPropertyList(); // 统计属性值列表
		List<Map<String, Object>> statDateList = statReadMeterNumByProperty(period, traceIds); // 抄表数量
		Map<String, Object> statData = packStatDataPie(statPrpertyList, statDateList); // 封装统计数据
		/*
		 * 封装后的结构如下所示: legendList:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎'], statData:[
		 * {value:335, name:'直接访问'}, {value:310, name:'邮件营销'}, {value:234, name:'联盟广告'},
		 * {value:135, name:'视频广告'}, {value:1548, name:'搜索引擎'} ]
		 */

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		return TEMPLATE_PATH + "/readmeter/monthsummary/statistic_readmeter_month_summary_pie";
	}

	/**
	 * @Title: getStatPropertyList
	 * @Description: 获取统计属性所对应的值列表
	 * @return TODO 需要根据业务进行调整
	 */
	private List<String> getStatPropertyList() {
		List<DataDict> waterUseList = searchWaterUseList(); // 自数据字典中读取用水性质列表

		List<String> propertyValueList = new ArrayList<>();
		for (DataDict waterUse : waterUseList) {
			propertyValueList.add(waterUse.getValue());
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
	 * @Description: 自数据字典中获取用水性质.
	 * @return
	 */
	private List<DataDict> searchWaterUseList() {
		final String TYPE_CODE_WATER_USER = "WATER_USE";
		// 自数据字典中读取列表
		DataDict searchObj = new DataDict();
		searchObj.setTypeCode(TYPE_CODE_WATER_USER);
		List<DataDict> waterUseList = dataDictService.select(searchObj);
		return waterUseList;
	}

	/**
	 * @Title: statReadRecordMonthSummary
	 * @Description: 月汇总统计 --按用水性质
	 * @param period yyyy-mm
	 * @return
	 */
	private List<Map<String, Object>> statReadRecordMonthSummary(String period, String traceIds) {
		return statisticService.statReadRecordMonthSummary(period, traceIds);
	}

	/**
	 * @Title: statReadMeterNumByProperty
	 * @Description: 按用水性质进行统计抄表记录
	 * @param period
	 * @return
	 * 
	 *         [ {value:335, name:'直接访问'}, {value:310, name:'邮件营销'}, {value:234,
	 *         name:'联盟广告'}, {value:135, name:'视频广告'}, {value:1548, name:'搜索引擎'} ]
	 */
	private List<Map<String, Object>> statReadMeterNumByProperty(String period, String traceIds) {
		List<Map<String, Object>> statData = new ArrayList<>();

		List<Map<String, Object>> statResultList = statReadRecordMonthSummary(period, traceIds); // 统计结果
		List<DataDict> waterUseList = searchWaterUseList(); // 用水性质列表

		// 按用水性质初始化抄表初始值为0
//			[
//			 {
//				value:0,
//				name:'xxx',
//				key:'jumin'
//			 },
//			 {}
//			 ]
		for (DataDict waterUse : waterUseList) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("value", 0);
			tempMap.put("name", waterUse.getValue());
			tempMap.put("key", waterUse.getKey());
			statData.add(tempMap);
		}

		// 建立索引 key(用水性质)--->在汇总列表中的索引idx
//			{
//				jumin:0,
//				fiejumin:1,				
//			}
		Map<String, Integer> indexMap = new HashMap<>();
		for (int i = 0; i < statData.size(); i++) {
			indexMap.put((String) statData.get(i).get("key"), i);
		}

		// 将统计数据置于汇总列表中
		for (Map<String, Object> map : statResultList) {
			String key = (String) map.get("WATER_USE"); // 用水性质, 统计结果中可能存在null值
			if (StringUtils.isNotBlank(key)) {
				int readNum = 0;
				String readNumStr = ((String) map.get("READ_NUM"));
				if(StringUtils.isNotBlank(readNumStr)) {
					readNum = Integer.valueOf(readNumStr);
				}
				int idx = indexMap.get(key);
				statData.get(idx).put("value", readNum);
			}
		}

		// 将汇总列表项中的key移除
		for (int i = 0; i < statData.size(); i++) {
			statData.get(i).remove("key");
		}

		return statData;
	}

	/**
	 * @Title: packStatDataPie
	 * @Description: 打包饼图所需要的数据
	 * @param legendList 图例列表
	 * @param statData   统计汇总数据
	 * @return { legendList:object1, statData:object2 }
	 */
	private Map<String, Object> packStatDataPie(List<String> legendList, List<Map<String, Object>> statData) {
		Map<String, Object> tempMap = new HashMap<>();
		tempMap.put("legendList", legendList);
		tempMap.put("statData", statData);
		return tempMap;
	}

}
