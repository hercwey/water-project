package com.learnbind.ai.controller.statistic;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.common.StatisticUtil;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.service.statistic.StatisticService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.statistic
 *
 * @Title: StatisticController.java
 * @Description: 统计及报表控制器
 *
 * @author lenovo
 * @date 2019年8月26日 下午2:35:21
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/stat")
public class StatisticController {
	private static Log log = LogFactory.getLog(StatisticController.class);
	private static final String TEMPLATE_PATH = "statistic"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	StatisticService statisticService;

	// ---------------------echart测试-------------------
	/**
	 * @Title: testEChart
	 * @Description: echart测试
	 * @return
	 */
	@RequestMapping(value = "/test")
	public String testEChart() {
		return TEMPLATE_PATH + "test_echart";
	}

	// ----------------------新增客户月/年统计------------------------

	/**
	 * @Title: statisticCustomerMonthMain
	 * @Description: 新增客户-月统计主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/month/main")
	public String statisticCustomerMonthMain(Model model) {
		return TEMPLATE_PATH + "/customer/month/statistic_customer_month_main";
	}

	/**
	 * @Title: statisticCustomerMonth
	 * @Description: 新增客户月统计-柱状图
	 * @param period 期间,格式 yyyy-mm
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/month/chart")
	public String statisticCustomerMonthChart(String period, String traceIds, Model model) {
		List<String> dayList = genXAxisDataMonth(period); // 天列表 x轴
		List<Integer> numPerDayList = statCustomerPerDay(period, traceIds); // 每天所新增的客户列表 y轴
		Map<String, Object> statData = packStatData(dayList, numPerDayList); // 封装统计数据

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		// List<Map<String, Object>> testData = statisticService.test();

		return TEMPLATE_PATH + "/customer/month/statistic_customer_month_bar";
	}

	/**
	 * @Title: statisticCustomerMonthTable
	 * @Description: 查询-月新增客户列表
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件 yyyy-mm
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/month/table")
	public String statisticCustomerMonthTable(Integer pageNum, Integer pageSize, String searchCond, String traceIds,
			Model model) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		log.debug("searchCond" + searchCond);

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> customerList = new ArrayList<>();
		customerList = statisticService.searchCustomerPerMonth(searchCond, traceIds);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerList); // 列表数据
		model.addAttribute("pageInfo", pageInfo); // 分页数据
		model.addAttribute("searchCond", searchCond); // 查询条件回传

		return TEMPLATE_PATH + "/customer/month/statistic_customer_month_table";
	}

	/**
	 * @Title: statisticCustomerYearMain
	 * @Description: 新增客户-年统计主页
	 * @param model
	 * @return 年统计主页
	 */
	@RequestMapping(value = "/customer/year/main")
	public String statisticCustomerYearMain(Model model) {
		return TEMPLATE_PATH + "/customer/year/statistic_customer_year_main";
	}

	@RequestMapping(value = "/customer/year/table")
	public String statisticCustomerYearTable(Integer pageNum, Integer pageSize, String searchCond, String traceIds,
			Model model) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		log.debug("searchCond" + searchCond);

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> customerList = new ArrayList<>();
		customerList = statisticService.searchCustomerPerYear(searchCond, traceIds);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("customerList", customerList); // 列表数据
		model.addAttribute("pageInfo", pageInfo); // 分页数据
		model.addAttribute("searchCond", searchCond); // 查询条件回传

		return TEMPLATE_PATH + "/customer/year/statistic_customer_year_table";
	}

	/**
	 * @Title: statisticCustomerYear
	 * @Description: 新增客户年统计
	 * @param period 期间,格式 yyyy
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer/year/chart")
	public String statisticCustomerYearChart(String period, String traceIds, Model model) {

		List<String> periodList = genXAxisDataYear(); // 月份列表
		List<Integer> numPerPeriodList = statCustomerPerMonth(period, traceIds); // 每月所新增的客户列表
		Map<String, Object> statData = packStatData(periodList, numPerPeriodList); // 打包统计数据

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		return TEMPLATE_PATH + "/customer/year/statistic_customer_year_bar";
	}

	// ----------------------抄表统计------------------------

	// ------------------抄表统计-月--------------------------
	/**
	 * @Title: statisticReadMeterMonthMain
	 * @Description: 抄表-月统计主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readmeter/month/main")
	public String statisticReadMeterMonthMain(Model model) {
		return TEMPLATE_PATH + "/readmeter/month/statistic_readmeter_month_main";
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
	@RequestMapping(value = "/readmeter/month/table")
	public String statisticReadMeterMonthTable(Integer pageNum, Integer pageSize, String searchCond, String traceIds,
			Model model) {
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

		return TEMPLATE_PATH + "/readmeter/month/statistic_readmeter_month_table";
	}

	/**
	 * @Title: statisticReadMeterMonth
	 * @Description: 抄表数月统计.统计当月每天的抄表数量
	 * @param period 期间, 格式 yyyy-mm
	 * @param model
	 * @return 柱状图页面
	 */
	@RequestMapping(value = "/readmeter/month/chart")
	public String statisticReadMeterMonthChart(String period, String traceIds, Model model) {

		List<String> periodList = genXAxisDataMonth(period); // 天列表 x轴
		List<Integer> numPerPeriodList = statReadMeterNumPerDay(period, traceIds); // 每天抄表数量 y轴
		Map<String, Object> statData = packStatData(periodList, numPerPeriodList); // 封装统计数据

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		return TEMPLATE_PATH + "/readmeter/month/statistic_readmeter_month_bar";
	}

	// ------------------抄表统计-年---------------------

	/**
	 * @Title: statisticReadMeterYearMain
	 * @Description: 抄表-年统计主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readmeter/year/main")
	public String statisticReadMeterYearMain(Model model) {
		return TEMPLATE_PATH + "/readmeter/year/statistic_readmeter_year_main";
	}

	/**
	 * @Title: statisticReadMeterYearTable
	 * @Description: 查询-年抄表记录列表
	 * @param pageNum    页号
	 * @param pageSize   页大小
	 * @param searchCond 查询条件 yyyy
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/readmeter/year/table")
	public String statisticReadMeterYearTable(Integer pageNum, Integer pageSize, String searchCond, String traceIds,
			Model model) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		log.debug("searchCond" + searchCond);

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> readRecordList = new ArrayList<>();
		readRecordList = statisticService.searchReadMeterRecordPerYear(searchCond, traceIds);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(readRecordList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("readRecordList", readRecordList); // 列表数据
		model.addAttribute("pageInfo", pageInfo); // 分页数据
		model.addAttribute("searchCond", searchCond); // 查询条件回传

		return TEMPLATE_PATH + "/readmeter/year/statistic_readmeter_year_table";
	}

	@RequestMapping(value = "/readmeter/year/chart")
	public String statisticReadMeterYearBar(String period, String traceIds, Model model) {

		List<String> periodList = genXAxisDataYear(); // 月份列表
		List<Integer> numPerPeriodList = statReadMeterNumPerYear(period, traceIds); // 统计年抄表数量
		Map<String, Object> statData = packStatData(periodList, numPerPeriodList); // 打包统计数据

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		return TEMPLATE_PATH + "/readmeter/year/statistic_readmeter_year_bar";
	}

	// -----------------------水量统计-----------------------
	@RequestMapping(value = "/water/month")
	public String statisticWaterMonth(String period, Model model) {
		// TODO 参数默认初始化
		// TODO TEST DATA 实际测试时将以下语句remove即可.
		period = "2019-02";

		List<String> periodList = genXAxisDataMonth(period); // 天列表 x轴
		List<Integer> numPerPeriodList = statWaterMaountPerMonth(period); // 每天用水量 y轴
		Map<String, Object> statData = packStatData(periodList, numPerPeriodList); // 封装统计数据

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		return TEMPLATE_PATH + "/water/statistic_water_amount_month_bar";
	}

	@RequestMapping(value = "/water/year")
	public String statisticWaterYear(String period, Model model) {

		// TODO 参数默认初始化
		// TODO test data 实际将以下内容remove即可.
		period = "2019";

		List<String> periodList = genXAxisDataYear(); // 月份列表
		List<Integer> numPerPeriodList = statWaterAmountPerYear(period); // 统计年用水量
		Map<String, Object> statData = packStatData(periodList, numPerPeriodList); // 打包统计数据

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		return TEMPLATE_PATH + "/water/statistic_water_amount_year_bar";
	}

	@RequestMapping(value = "/water/monthpie")
	public String statisticWaterPieMonth(String period, Model model) {
		// TODO 参数默认初始化
		// TODO TEST DATA 实际测试时将以下语句remove即可.
		period = "2019-02";

		List<String> statPrpertyList = getStatPropertyList(); // 统计属性值列表
		List<Map<String, Object>> statDateList = statWaterAmountByProperty(statPrpertyList); // 每天用水量 y轴

		Map<String, Object> statData = packStatDataPie(statPrpertyList, statDateList); // 封装统计数据
		/*
		 * 封装后的结构如下所示: legendList:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎'], statData:[
		 * {value:335, name:'直接访问'}, {value:310, name:'邮件营销'}, {value:234, name:'联盟广告'},
		 * {value:135, name:'视频广告'}, {value:1548, name:'搜索引擎'} ]
		 */

		model.addAttribute("statData", statData); // 统计数据
		model.addAttribute("period", period); // 期间

		return TEMPLATE_PATH + "/water/statistic_water_amount_month_pie";
	}

	/**
	 * @Title: statisticWaterCompareMonth
	 * @Description: 年用水量月对比
	 * @param periodList 期间 [2018,2019]
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/water/comparemonth")
	public String statisticWaterCompareMonth(
			@RequestParam(value = "periodList[]", required = false, defaultValue = "") ArrayList<String> periodList,
			Model model) {
		// TODO TEST DATA 实际测试时将以下语句remove即可.
		// 对比期间,一般是两个年份 legendList
		String period1 = "2018年";
		String period2 = "2019年";
		List<String> legendList = new ArrayList<>();
		legendList.add(period1);
		legendList.add(period2);

		List<String> monthList = genXAxisDataYear(); // 月份列表 x轴
		List<Object> statDatax = statCompareDate(); // 模拟对比数据

		Map<String, Object> statData = packStatCompareData(monthList, legendList, statDatax); // 封装统计对比数据\

		model.addAttribute("statData", statData); // 统计数据

		return TEMPLATE_PATH + "/water/statistic_water_amount_month_compare_bar";
	}

	/**
	 * @Title: statCompareDate
	 * @Description: 生成统计对比数据
	 * @return TODO 生成统计对比数据
	 */
	private List<Object> statCompareDate() {
		List<Object> statDatax = new ArrayList<>();
		List<Object> statData1 = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			statData1.add(i);
		}
		List<Object> statData2 = new ArrayList<>();
		for (int i = 0; i < 12; i++) {
			statData2.add(i + 5);
		}
		statDatax.add(statData1);
		statDatax.add(statData2);
		return statDatax;
	}

	private Map<String, Object> packStatCompareData(List<String> monthList, List<String> legendList,
			List<Object> statData) {
		Map<String, Object> tempMap = new HashMap<>();
		tempMap.put("monthList", monthList);
		tempMap.put("legendList", legendList);
		tempMap.put("statDataList", statData);
		return tempMap;
	}

	private Map<String, Object> packStatDataPie(List<String> legendList, List<Map<String, Object>> statData) {
		Map<String, Object> tempMap = new HashMap<>();
		tempMap.put("legendList", legendList);
		tempMap.put("statData", statData);
		return tempMap;
	}

	/**
	 * @Title: getStatPropertyList
	 * @Description: 获取统计属性所对应的值列表
	 * @return TODO 需要根据业务进行调整
	 */
	private List<String> getStatPropertyList() {
		List<String> propertyValueList = new ArrayList<>();
		propertyValueList.add("绿化");
		propertyValueList.add("生活");
		propertyValueList.add("消防");
		propertyValueList.add("商业");
		return propertyValueList;
	}

	/*
	 * 
	 * [ {value:335, name:'直接访问'}, {value:310, name:'邮件营销'}, {value:234,
	 * name:'联盟广告'}, {value:135, name:'视频广告'}, {value:1548, name:'搜索引擎'} ]
	 * 
	 */
	// TODO 需要根据业务进行调整
	private List<Map<String, Object>> statWaterAmountByProperty(List<String> propertyValueList) {
		List<Map<String, Object>> statData = new ArrayList<>();

		int i = 100;
		for (String property : propertyValueList) {

			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("value", i);
			i += 200;
			tempMap.put("name", property);
			statData.add(tempMap);
		}
		return statData;
	}

	/**
	 * @Title: statReadMeterNumPerDay
	 * @Description: 统计指定月份每天的抄表数
	 * @param period 期间
	 * @return
	 */
	private List<Integer> statReadMeterNumPerDay(String period, String traceIds) {
		String datex = period + "-01";
		int daysPerMonth = StatisticUtil.getDaysOfMonth(datex); // 天数/指定月份

		List<Integer> numPerDayList = new ArrayList<>();
		for (int i = 0; i < daysPerMonth; i++) {
			numPerDayList.add(0);
		}

		// 自数据库中统计指定期间抄表数量(每天抄表数量): 天--->抄表数量
		List<Map<String, Object>> readList = statisticService.statReadRecordNumPerDay(period, traceIds);
		for (Map<String, Object> map : readList) {
			Date readDate = (Date) map.get("READ_DATE"); // 格式yyyy-mm-dd 抄表日期
			int amount = ((BigDecimal) map.get("READ_NUM")).intValue(); // 抄表数

			// 取出"dd-日"部分,以其为索引置值
			int dayx = DateUtil.get(readDate, "day");
			numPerDayList.set(dayx - 1, amount);
		}

		return numPerDayList;
	}

	/**
	 * @Title: statWaterMaountPerMonth
	 * @Description: 月用水量统计
	 * @param period
	 * @return TODO 月用水量统计
	 */
	private List<Integer> statWaterMaountPerMonth(String period) {
		String datex = period + "-01";
		int daysPerMonth = StatisticUtil.getDaysOfMonth(datex); // 天数/指定月份

		List<Integer> numPerPeriodList = new ArrayList<>();
		// TODO 自数据库中统计指定期间抄表数量(每天抄表数量),此处暂时采用模拟数据,需要与天一一对应
		for (int i = 0; i < daysPerMonth; i++) {
			numPerPeriodList.add(i * 2);
		}

		return numPerPeriodList;
	}

	/**
	 * @Title: statReadMeterNumPerYear
	 * @Description: 统计指定年份每月的抄表数量
	 * @param period 期间 yyyy
	 * @return 月--->抄表数量 索引号: 1,2,3,4,5,6,7,8,9,10,11,12 抄表数:[x1,x2,... x12] 共12个元素
	 */
	private List<Integer> statReadMeterNumPerYear(String period, String traceIds) {
		final int MONTHS_PER_YEAR = 12;
		List<Integer> numPerPeriodList = new ArrayList<>();
		for (int i = 0; i < MONTHS_PER_YEAR; i++) {
			numPerPeriodList.add(0);
		}
		// 自数据库中统计指定期间抄表数量.月份--->抄表数量
		List<Map<String, Object>> readList = statisticService.statReadRecordNumPerMonth(period, traceIds);

		for (Map<String, Object> map : readList) {
			Date readDate = (Date) map.get("READ_DATE"); // 格式yyyy-mm-dd 抄表日期
			int amount = ((BigDecimal) map.get("READ_NUM")).intValue(); // 抄表数

			// 取出"dd-日"部分,以其为索引置值
			int monthx = DateUtil.get(readDate, "month");
			numPerPeriodList.set(monthx - 1, amount);
		}
		return numPerPeriodList;
	}

	/**
	 * @Title: statWaterAmountPerYear
	 * @Description: 用水量年统计
	 * @param period
	 * @return TODO :用水量年统计
	 */
	private List<Integer> statWaterAmountPerYear(String period) {
		List<Integer> numPerPeriodList = new ArrayList<>();
		// TODO 自数据库中统计指定期间新增客户,此处暂时采用模拟数据,需与月份一一对应
		for (int i = 0; i < 12; i++) {
			numPerPeriodList.add(i * 100);
		}
		return numPerPeriodList;
	}

	/**
	 * @Title: statCustomerPerMonth
	 * @Description: 统计指定月份每天新增客户数 注:此与具体的业务相关
	 * @param period 期间 yyyy-mm
	 * @return 天--->人数
	 */
	private List<Integer> statCustomerPerDay(String period, String traceIds) {
		String datex = period + "-01";
		int daysPerMonth = StatisticUtil.getDaysOfMonth(datex);

		// 初始化每天新增人数列表
		List<Integer> numPerDayList = new ArrayList<>();
		for (int i = 0; i < daysPerMonth; i++) {
			numPerDayList.add(0);
		}

		// 自数据库中统计指定期间新增客户: 天--->人数
		List<Map<String, Object>> statList = statisticService.statCustomerPerDay(period, traceIds);
		for (Map<String, Object> map : statList) {
			Date settleTime = (Date) map.get("SETTLE_TIME"); // 格式yyyy-mm-dd
			int amount = ((BigDecimal) map.get("CUSTOMERAMOUNT")).intValue();

			// 取出"dd-日"部分,以其为索引置新增人数
			int dayx = DateUtil.get(settleTime, "day");
			numPerDayList.set(dayx - 1, amount);
		}

		return numPerDayList;
	}

	/**
	 * @Title: statCustomerPerYear
	 * @Description: 一年中每月新增客户数量
	 * @param period 期间 yyyy 年份
	 * @return 月份--->人数
	 */
	private List<Integer> statCustomerPerMonth(String period, String traceIds) {
		final int MONTHS_PER_YEAR = 12;
		List<Integer> numPerMonthList = new ArrayList<>();
		for (int i = 0; i < MONTHS_PER_YEAR; i++) {
			numPerMonthList.add(0);
		}
		// 自数据库中统计指定期间新增客户.月份--->人数
		List<Map<String, Object>> statList = statisticService.statCustomerPerMonth(period, traceIds);
		for (Map<String, Object> map : statList) {
			Date date = (Date) map.get("STATMONTH"); // yyyy-mm
			int amount = ((BigDecimal) map.get("CUSTOMERAMOUNT")).intValue(); // 数量

			int monthx = DateUtil.get(date, "month");
			numPerMonthList.set(monthx - 1, amount);
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

	// ----------------------统计报表模版-营业处-----------------------------------------------------------------
	private static final String TEMPLATE_PATH_STAT = "statistic/stat/"; // 页面目录

	@RequestMapping(value = "/a")
	public String a(Model model) {
		return TEMPLATE_PATH_STAT + "a";
	}

	@RequestMapping(value = "/b")
	public String b(Model model) {
		return TEMPLATE_PATH_STAT + "b";
	}

	@RequestMapping(value = "/c")
	public String c(Model model) {
		return TEMPLATE_PATH_STAT + "c";
	}

	@RequestMapping(value = "/d")
	public String d(Model model) {
		return TEMPLATE_PATH_STAT + "d";
	}

	@RequestMapping(value = "/e")
	public String e(Model model) {
		return TEMPLATE_PATH_STAT + "e";
	}

	// ----------------------统计报表模版-营业处-----------------------------------------------------------------
	private static final String TEMPLATE_PATH_STAT_CW = "statistic/stat/cw/"; // 页面目录

	@RequestMapping(value = "/cw/631")
	public String cw631(Model model) {
		return TEMPLATE_PATH_STAT_CW + "631";
	}

	@RequestMapping(value = "/cw/632")
	public String cw632(Model model) {
		return TEMPLATE_PATH_STAT_CW + "632";
	}

	@RequestMapping(value = "/cw/633")
	public String cw633(Model model) {
		return TEMPLATE_PATH_STAT_CW + "633";
	}

	@RequestMapping(value = "/cw/634")
	public String cw634(Model model) {
		return TEMPLATE_PATH_STAT_CW + "634";
	}

	@RequestMapping(value = "/cw/635")
	public String cw635(Model model) {
		return TEMPLATE_PATH_STAT_CW + "635";
	}

	@RequestMapping(value = "/cw/636")
	public String cw636(Model model) {
		return TEMPLATE_PATH_STAT_CW + "636";
	}

	@RequestMapping(value = "/cw/637")
	public String cw637(Model model) {
		return TEMPLATE_PATH_STAT_CW + "637";
	}

	@RequestMapping(value = "/cw/638")
	public String cw638(Model model) {
		return TEMPLATE_PATH_STAT_CW + "638";
	}

	@RequestMapping(value = "/cw/639")
	public String cw639(Model model) {
		return TEMPLATE_PATH_STAT_CW + "639";
	}

}