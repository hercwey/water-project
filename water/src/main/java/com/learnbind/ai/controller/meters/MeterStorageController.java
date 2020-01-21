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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumMeterStockStatus;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MeterStockTraceService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterStorageController.java
 * @Description: 库存管理-入库
 *
 * @author Thinkpad
 * @date 2019年10月21日 下午3:29:32
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-storage")
public class MeterStorageController {
	private static Log log = LogFactory.getLog(MeterStorageController.class);
	private static final String TEMPLATE_PATH = "meter_stock/meter_storage/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private MetersService metersService;  //客户档案
	@Autowired
	private LocationMeterService locationMeterService;  //地理位置-表计关系表
	@Autowired
	private LocationService locationService;  //地理位置
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关系
	@Autowired
	private CustomersService customersService;  //客户档案
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计关联表
	@Autowired
	private DataDictService dataDictService;  //数据字典
	@Autowired
	private DiscountService discountService;  //政策减免
	@Autowired
	private WaterPriceService waterPriceService;  //水价
	@Autowired
	private CustomerAccountItemService customerAccountItemService;//客户账目
	@Autowired
	private MeterStockTraceService meterStockTraceService;  //库存日志
	
	
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
	 * @Title: tabs
	 * @Description: 加载选项卡页面
	 * @param model
	 * @param functionModule
	 * @return 
	 */
	@RequestMapping(value = "/tabs")
	public String tabs(Model model) {
		
		return TEMPLATE_PATH + "tabs";
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
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		model.addAttribute("meter", null);//初始化表计档案详情信息
		
		return TEMPLATE_PATH + "meters_main";
	}
	
	/**
	 * @Title: locationTable
	 * @Description: 加载地理位置
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/location-table")
	public String locationTable(Model model) {

		return TEMPLATE_PATH + "location_table";
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds) {

		
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = new ArrayList<>();
		//查询未使用的表计
		List<Integer> cycleStatusList = this.getCycleStatus();
		if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			meterList = metersService.getMeterListByCycleStatus(searchCond, traceIds, cycleStatusList);
		} else {
			meterList = metersService.searchMetersByCycleStatus(searchCond, cycleStatusList);
		}
		PageInfo<Meters> pageInfo = new PageInfo<Meters>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(Meters meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("cycleStatusStr", meter.getCycleStatusStr());//表计生命周期状态
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMap.put("meterTypeValue", this.getDataDictValue(EnumDataDictType.METER_TYPE.getCode(), meter.getMeterType()));//水表类型
			meterMap.put("caliber", this.getDataDictValue(EnumDataDictType.METER_WATER_CALIBER.getCode(), meter.getCaliber()));//水表口径
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
	
	/**
	 * @Title: getCycleStatus
	 * @Description: 获取查询表计生命周期状态
	 * @return 
	 */
	private List<Integer> getCycleStatus(){
		List<Integer> cycleStatusList = new ArrayList<>();
		//待用
		Integer inactiveStatus = EnumMeterCycleStatus.INACTIVE.getValue();
		cycleStatusList.add(inactiveStatus);
		
		return cycleStatusList;
	}
	
	/**
	 * @Title: loadAddDialog
	 * @Description:获取增加对话框
	 * @param model
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/load-add-dialog")
	public String loadAddDialog(Model model, Long meterId) {
		
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
		
		String title="增加：表计档案";
		model.addAttribute("title", title);
		
		return TEMPLATE_PATH + "meters_dialog_edit";
	}
	
	/**
	 * @Title: addMeters
	 * @Description: 增加
	 * @param meter
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addMeters(Meters meter, Long locationId) {
		
		Meters record = new Meters();
		record.setSteelSealNo(meter.getSteelSealNo());
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setStatus(EnumMeterSettingStatus.ZERO.getValue());
		record.setVirtualReal(EnumMeterVirtualReal.REAL_METER.getValue());
		List<Meters> meterList = metersService.select(record);
		if(meterList!=null && meterList.size()>0) {
			return RequestResultUtil.getResultAddWarn("表计钢印号重复！请重新输入！");
		}
		
		meter.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		meter.setStatus(EnumMeterSettingStatus.ZERO.getValue());
		meter.setPid(0l);
		int row = metersService.insertMeters(meter, locationId);
		if (row>0){
			//添加入库日志
			meterStockTraceService.insertTrace(meter.getId(), EnumMeterStockStatus.IN_STORAGE.getValue());
			Map<String, Object> respMap = RequestResultUtil.getResultAddSuccess();
			respMap.put("newNodes", record);
			return respMap;
		}
		
		return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, Long meterId) {
		
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
		String title="编辑：表计档案";
		model.addAttribute("title", title);
		if(meterId!=null) {
			//读取需要编辑的条目
			Meters currItem=metersService.selectByPrimaryKey(meterId);
			model.addAttribute("currItem",currItem);
		}
		
		return TEMPLATE_PATH + "meters_dialog_edit";
	}
	
	/**
	 * @Title: updateMeters
	 * @Description: 修改
	 * @param meter
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateMeters(Meters meter) throws Exception {
		
		int rows = metersService.updateByPrimaryKeySelective(meter);
		if(rows>0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	


	/**
	 * @Title: detail
	 * @Description: 加载表计档案详细信息
	 * @param model
	 * @param id
	 * @return 
	 */
	@RequestMapping(value = "/detail-table")
	public String detail(Model model, Long meterId) {
		
		Map<String, Object> meterMap = null;
		if(meterId!=null && meterId>0) {
			Meters meter = metersService.selectByPrimaryKey(meterId);
			
			meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("checkDateStr", meter.getCheckDateStr());//水表检定日期
			meterMap.put("effectiveDateStr", meter.getEffectiveDateStr());//水表有效日期
			meterMap.put("meterStatusStr", meter.getMeterStatusStr());//水表状态
			meterMap.put("virtualRealStr", meter.getVirtualRealStr());//虚实表
			meterMap.put("cycleStatusStr", meter.getCycleStatusStr());//水表生命周期状态
			
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMap.put("caliberValue", this.getDataDictValue(EnumDataDictType.METER_WATER_CALIBER.getCode(), meter.getCaliber()));//水表口径
			meterMap.put("meterModelValue", this.getDataDictValue(EnumDataDictType.METER_MODEL.getCode(), meter.getMeterModel()));//水表型号
			meterMap.put("meterTypeValue", this.getDataDictValue(EnumDataDictType.METER_TYPE.getCode(), meter.getMeterType()));//水表类型
			meterMap.put("meterUseValue", this.getDataDictValue(EnumDataDictType.METER_USE.getCode(), meter.getMeterUse()));//水表用途
			meterMap.put("readModeValue", this.getDataDictValue(EnumDataDictType.METER_READ_MODE.getCode(), meter.getReadMode()));//抄表方式
			meterMap.put("waterUseValue", this.getPriceTypeName(meter.getWaterUse()));//用水性质名称
		}
		
		model.addAttribute("meter", meterMap);
		
		return TEMPLATE_PATH + "meters_detail_table";
	}
	
	/**
	 * @Title: getDiscountName
	 * @Description: 获取用水性质名称
	 * @param discountId
	 * @return 
	 */
	private String getPriceTypeName(String priceTypeCode) {
		String priceTypeName = waterPriceService.getPriceTypeName(priceTypeCode);
		if(priceTypeName!=null) {
			return priceTypeName;
		}
		return null;
	}
	
	/**
	 * @Title: deleteMeters
	 * @Description: 删除
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteMeters(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			
			metersService.deleteMeters(ids);
			//metersService.deleteMeters(id);
		}catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

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
	

	
	
	
	
}