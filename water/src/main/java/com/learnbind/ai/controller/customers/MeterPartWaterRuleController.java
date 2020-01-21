package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: MeterPartWaterRuleController.java
 * @Description: 表计规则（分水量规则、追加/减免水量规则）前端控制器
 *
 * @author Administrator
 * @date 2019年9月29日 下午4:43:32
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-part-water-rule")
public class MeterPartWaterRuleController {
	
	//private static Log log = LogFactory.getLog(MeterPartWaterRuleController.class);
	private static final String TEMPLATE_PATH = "part_water_rule/"; // 页面目录
	
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;//表计分水量
	@Autowired
	private WaterPriceService waterPriceService;//水价
	@Autowired
	private DataDictService dataDictService;//数据字典
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private MetersService metersService;//表计
	
	/**
	 * @Title: loadPartWaterRuleDialog
	 * @Description: 加载表计分水量规则页面
	 * @param model
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/load-part-water-rule-dialog")
	public String loadPartWaterRuleDialog(Model model, Long meterId) {
		
		//查询非阶梯水价的所有数据
		List<SysWaterPrice> waterPriceList = waterPriceService.getNotJmshysPriceList();
		model.addAttribute("waterPriceList", waterPriceList);
		
		MeterPartWaterRule record = new MeterPartWaterRule();
		record.setMeterId(meterId);
		List<MeterPartWaterRule> meterPartWaterList = meterPartWaterRuleService.select(record);
		model.addAttribute("ruleList", meterPartWaterList);
		
		model.addAttribute("meterId", meterId);
		
		return TEMPLATE_PATH + "part_water_rule_dialog";
		
	}
	
	/**
	 * @Title: loadPartWaterRule
	 * @Description: 加载一条规则
	 * @param model
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/load-part-water-rule")
	public String loadPartWaterRule(Model model, Long meterId, String ruleId) {
		
		//查询非阶梯水价的所有数据
		List<SysWaterPrice> waterPriceList = waterPriceService.getNotJmshysPriceList();
		model.addAttribute("waterPriceList", waterPriceList);
		
		//查询已配置的规则
		MeterPartWaterRule searchObj = new MeterPartWaterRule();
		searchObj.setMeterId(meterId);
		
		model.addAttribute("editorId", ruleId);
		
		return TEMPLATE_PATH + "part_water_rule_tr";
		
	}
	
	/**
	 * @Title: savePartWaterRule
	 * @Description: 保存表计分水量规则
	 * @param model
	 * @param meterId
	 * @param meterPartWaterJson
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/save", produces = "application/json")
	@ResponseBody
	public Object savePartWaterRule(Model model, Long meterId, String meterPartWaterJson) throws Exception {
		try {
			
			List<MeterPartWaterRule> partWaterRuleList = JSON.parseArray(meterPartWaterJson, MeterPartWaterRule.class);
			
			int rows = meterPartWaterRuleService.insert(meterId, partWaterRuleList);
			
			if (rows>0) {
				return RequestResultUtil.getResultSaveSuccess("保存成功！");
			}
			return RequestResultUtil.getResultSaveWarn("保存异常，请重新操作！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultSaveWarn("保存异常，请重新操作！");

	}
	
	/**
	 * @Title: delete
	 * @Description: 删除规则
	 * @param model
	 * @param ruleId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(Model model, Long ruleId) throws Exception {
		try {
			
			int rows = meterPartWaterRuleService.deleteByPrimaryKey(ruleId);
			
			if (rows>0) {
				return RequestResultUtil.getResultSaveSuccess("删除成功！");
			}
			return RequestResultUtil.getResultSaveWarn("删除异常，请重新操作！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultSaveWarn("删除异常，请重新操作！");

	}
	
	//-------------------------------------选择表计部分------------------------------------------------------------------------------------
	
	/**
	 * @Title: loadMeterMain
	 * @Description: 加载表计主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-meter-main")
	public String loadMeterMain(Model model) {
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		List<DataDict> meterUseList = dataDictService.getListByTypeCode(DataDictType.METER_USE);
		model.addAttribute("meteUseList", meterUseList);
		return TEMPLATE_PATH + "meter/bind_bigmeter_main";
	}
	
	/**
	 * @Title: loadMeterTable
	 * @Description: 加载表计列表
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @param meterUse
	 * @return 
	 */
	@RequestMapping(value = "/load-meter-table")
	public String loadMeterTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String meterUse) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = new ArrayList<>();
		//获取实表
		Integer virtualRealStatus = EnumMeterVirtualReal.REAL_METER.getValue();
		if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			meterList = metersService.getRealMeterList(searchCond, traceIds, virtualRealStatus);
		} else {
			meterList = metersService.searchRealMeters(searchCond, virtualRealStatus);
		}
		//if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			//meterList = metersService.getBindBigMeterList(searchCond, traceIds, meterUse, customerId);
		//} else {
		//	meterList = metersService.searchMeters(searchCond);
		//}
		PageInfo<Meters> pageInfo = new PageInfo<>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(Meters meter : meterList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(meter);
			customerMap.put("meterUseStr", this.getDataDictValue(EnumDataDictType.METER_USE.getCode(), meter.getMeterUse()));//行业性质
			customerMapList.add(customerMap);		
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("metersList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "meter/bind_bigmeter_table";
	}
	
	/**
	 * @Title: getDataDictValue
	 * @Description: 根据数据字典类型编码和字典编码查询
	 * @param typeCode
	 * @param key
	 * @return 
	 */
	private String getDataDictValue(String typeCode, String key) {
		
		if(StringUtils.isBlank(typeCode) && StringUtils.isBlank(key)) {
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
	
	public static void main(String[] args) {
		Random random = new Random();
		Long ranLong = random.nextLong();
		if(ranLong<0) {
			ranLong = 0-ranLong;
		}
		System.out.println(ranLong);
	}
	
}