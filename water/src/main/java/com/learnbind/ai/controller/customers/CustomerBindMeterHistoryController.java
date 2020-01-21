package com.learnbind.ai.controller.customers;

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
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerBindMeterHistoryController.java
 * @Description: 客户-表计历史
 *
 * @author Thinkpad
 * @date 2019年10月17日 上午9:58:13
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-meter-history")
public class CustomerBindMeterHistoryController {
	private static Log log = LogFactory.getLog(CustomerBindMeterHistoryController.class);
	private static final String TEMPLATE_PATH = "customers/meter_history/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MetersService metersService;  //表计信息
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计信息
	@Autowired
	private DataDictService dataDictService;//数据字典
	@Autowired
	private WaterPriceService waterPriceService;  //水价
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private LocationMeterService locationMeterService;  //地理位置-表计关系表
	/**
	 * @Title: main
	 * @Description: 客户绑定表计历史
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/customer-meter-main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "meter_history_main";
	}

	/**
	 * @Title: table
	 * @Description: 客户绑定的表计历史
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param customerId
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model,Integer pageNum, Integer pageSize, Long customerId, String searchCond) {
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerMeter> meterList = customerMeterService.getMeterHistoryMessage(customerId, searchCond);
		PageInfo<List<CustomerMeter>> pageInfo = new PageInfo(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(CustomerMeter temp : meterList) {
			Meters meter = metersService.selectByPrimaryKey(temp.getMeterId());
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("cycleStatusStr", meter.getCycleStatusStr());//水表生命周期
			meterMap.put("virtualRealStr", meter.getVirtualRealStr());//虚实表状态
			meterMap.put("bindTimeStr", temp.getBindTimeStr());//绑定时间
			meterMap.put("unBindTimeStr", temp.getUnBindTimeStr());//解绑时间
			meterMap.put("meterId", temp.getMeterId());
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMap.put("meterUseValue", this.getDataDictValue(EnumDataDictType.METER_USE.getCode(), meter.getMeterUse()));//水表用途
			
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
		
		return TEMPLATE_PATH + "meter_history_table";
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
	
	
	/**
	 * @Title: loadModiBankDialog
	 * @Description: 修改客户银行信息
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-meter-history-dialog")
	public String loadModiBankDialog(Long itemId, Model model) {
		//加载数据字典-水表口径、水表类型、水用途、抄表方式、水表型号、水表生产厂家
		List<DataDict> waterCaliberList = dataDictService.getListByTypeCode(DataDictType.WARTER_CALIBER);
		List<DataDict> meterTypeList = dataDictService.getListByTypeCode(DataDictType.METER_TYPE);
		List<DataDict> meterUseList = dataDictService.getListByTypeCode(DataDictType.METER_USE);
		List<DataDict> readModeList = dataDictService.getListByTypeCode(DataDictType.READ_MODE);
		List<DataDict> factoryList = dataDictService.getListByTypeCode(DataDictType.METER_FACTORY);
		List<DataDict> meterModelList = dataDictService.getListByTypeCode(DataDictType.METER_MODEL);
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();//获取用水性质
		
		model.addAttribute("waterCaliberList", waterCaliberList);
		model.addAttribute("meterTypeList", meterTypeList);
		model.addAttribute("meterUseList", meterUseList);
		model.addAttribute("readModeList", readModeList);
		model.addAttribute("factoryList", factoryList);
		model.addAttribute("meterModelList", meterModelList);
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		
		//读取需要编辑的条目
		Meters currItem=metersService.selectByPrimaryKey(itemId);
		String waterUse = currItem.getWaterUse();
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("priceTypeCode", waterUse).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
		model.addAttribute("currItem",currItem);
		Location location = this.getLocationByMeterId(itemId);
		model.addAttribute("location",location);
		String place = "";
		if(location != null) {
			place = locationService.getPlace(location.getTraceIds());//查询地理位置全称
		}
		model.addAttribute("place",place);
		model.addAttribute("waterPriceList",waterPriceList);
		
		return TEMPLATE_PATH + "meter_history_dialog";
	}
	
	/**
	 * @Title: getLocationByMeterId
	 * @Description: 根据表计ID查询绑定的地理位置
	 * @param meterId
	 * @return 
	 */
	private Location getLocationByMeterId(Long meterId) {
		List<Long> meterIdList = new ArrayList<>();
		meterIdList.add(meterId);
		List<LocationMeter> lmList = locationMeterService.getListByMeterIdList(meterIdList);
		if(lmList!=null && lmList.size()>0) {
			Long locationId = lmList.get(0).getLocationId();
			Location location = locationService.selectByPrimaryKey(locationId);
			return location;
		}
		return null;
	}
	

}