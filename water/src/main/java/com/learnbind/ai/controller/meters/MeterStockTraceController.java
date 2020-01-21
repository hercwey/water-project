package com.learnbind.ai.controller.meters;

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
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.MeterStockTrace;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MeterStockTraceService;
import com.learnbind.ai.service.meters.MetersService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterStockTraceController.java
 * @Description: 库存管理日志
 *
 * @author Thinkpad
 * @date 2019年10月27日 上午11:27:12
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-stock-trace")
public class MeterStockTraceController {
	private static Log log = LogFactory.getLog(MeterStockTraceController.class);
	private static final String TEMPLATE_PATH = "meter_stock/meter_stock_trace/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MeterStockTraceService meterStockTraceService;  //库存管理日志
	@Autowired
	private DataDictService dataDictService;  //数据字典
	
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		
		return TEMPLATE_PATH + "meters_starter";
	}

	
	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "meters_main";
	}
	
	
	/**
	 * @Title: table
	 * @Description: 加载地理位置列表
	 * @param model
	 * @param functionModule
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, Integer operationType) {

		
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<MeterStockTrace> meterList = meterStockTraceService.searchCond(searchCond, operationType);
		
		PageInfo<MeterStockTrace> pageInfo = new PageInfo<MeterStockTrace>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(MeterStockTrace meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("operatorTimeStr", meter.getOperatorTimeStr());//操作时间字符串
			meterMap.put("operationTypeValue", meter.getOperationTypeStr());//操作类型
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMapList.add(meterMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "meters_table";
	}
	
	//------------------------------	内部方法	------------------------------
		/**
		 * @Title: getDataDictValue
		 * @Description: 根据数据字典类型编码和字典编码查询
		 * @param typeCode
		 * @param key
		 * @return 
		 */
		private String getDataDictValue(String typeCode, String key) {
			
			if(StringUtils.isBlank(typeCode) || StringUtils.isBlank(key)) {
				return null;
			}
			
			DataDict dict = new DataDict();
			if(StringUtils.isNotBlank(typeCode)) {
				dict.setTypeCode(typeCode);
			}
			dict.setKey(key);
			List<DataDict> dictList = dataDictService.select(dict);
			if(dictList!=null && dictList.size()>0) {
				dict = dictList.get(0);
			}
			
			if(dict!=null) {
				return dict.getValue();
			}
			return null;
		}
	
	
}