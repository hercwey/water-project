package com.learnbind.ai.controller.meters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.constant.MeterFactoryConstant;
import com.learnbind.ai.constant.MeterFunctionModuleConstant;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.DeviceRegisterRspBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MetersController.java
 * @Description: 表计档案前端控制器
 *
 * @author Administrator
 * @date 2019年5月22日 上午10:56:21
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meters")
public class MetersController {
	private static Log log = LogFactory.getLog(MetersController.class);
	private static final String TEMPLATE_PATH = "meters/"; // 页面
	private static final String TEMPLATE_PATH_CUSTOMER = "meters/customertab/"; // 页面
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
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录
	
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model, String functionModule) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = MeterFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(MeterFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
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
	public String tabs(Model model, String functionModule) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = MeterFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(MeterFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
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
	public String main(Model model, String functionModule) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = MeterFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(MeterFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
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
	public String table(Model model, String functionModule, Integer pageNum, Integer pageSize, String searchCond, String traceIds) {

		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = new ArrayList<>();
		if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			meterList = metersService.getMeterList(searchCond, traceIds);
		} else {
			meterList = metersService.searchMeters(searchCond);
		}
		PageInfo<Meters> pageInfo = new PageInfo<Meters>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(Meters meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			//meterMap.put("meterUseStr", meter.getWaterStatusStr());//水表用途
			//meterMap.put("readModeStr", meter.getWaterStatusStr());//抄表方式
			meterMap.put("meterStatusStr", meter.getMeterStatusStr());//停/复水状态
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMap.put("readModeValue", this.getDataDictValue(EnumDataDictType.METER_READ_MODE.getCode(), meter.getReadMode()));//抄表方式
			
			String meterAddress = meter.getMeterAddress();
			String collectorAddr = meter.getCollectorAddr();//采集器地址
			if(StringUtils.isNoneBlank(meter.getFactory())) {
				if(meter.getFactory().equalsIgnoreCase(MeterFactoryConstant.METER_FACTORY_EG)) {
					meterAddress = collectorAddr+meterAddress;
				}
			}
			
			meterMap.put("meterAddress", meterAddress);
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
		if(meter.getChannelNo() == null) {
			meter.setChannelNo("0");
		}
		List<Meters> meterList = metersService.select(record);
		if(meterList!=null && meterList.size()>0) {
			return RequestResultUtil.getResultAddWarn("表计钢印号重复！请重新输入！");
		}
		
		meter.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		meter.setStatus(EnumMeterSettingStatus.ZERO.getValue());
		meter.setPid(0l);
		int row = metersService.insertMeters(meter, locationId);
		if (row>0){
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
		List<DataDict> protocolTypeList = dataDictService.getListByTypeCode(DataDictType.PROTOCOL_TYPE);
		List<DataDict> meterUseTypeList = dataDictService.getListByTypeCode(DataDictType.METER_USE_TYPE);
		List<DataDict> meterFactoryCodeList = dataDictService.getListByTypeCode(DataDictType.METER_FACTORY_CODE);
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();//获取用水性质
		
		model.addAttribute("waterCaliberList", waterCaliberList);
		model.addAttribute("meterTypeList", meterTypeList);
		model.addAttribute("meterUseList", meterUseList);
		model.addAttribute("readModeList", readModeList);
		model.addAttribute("factoryList", factoryList);
		model.addAttribute("meterModelList", meterModelList);
		model.addAttribute("protocolTypeList", protocolTypeList);
		model.addAttribute("meterUseTypeList", meterUseTypeList);
		model.addAttribute("meterFactoryCodeList", meterFactoryCodeList);
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		
		if(meterId!=null) {
			//读取需要编辑的条目
			Meters currItem=metersService.selectByPrimaryKey(meterId);
			String waterUse = currItem.getWaterUse();
			Example example = new Example(SysWaterPrice.class);
			example.createCriteria().andEqualTo("priceTypeCode", waterUse).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
			List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
			model.addAttribute("currItem",currItem);
			Location location = this.getLocationByMeterId(meterId);
			model.addAttribute("location",location);
			String place = "";
			if(location != null) {
				place = locationService.getPlace(location.getTraceIds());//查询地理位置全称
			}
			model.addAttribute("place",place);
			model.addAttribute("waterPriceList",waterPriceList);
		}
		
		return TEMPLATE_PATH + "meters_dialog_edit";
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
		List<DataDict> protocolTypeList = dataDictService.getListByTypeCode(DataDictType.PROTOCOL_TYPE);
		List<DataDict> meterUseTypeList = dataDictService.getListByTypeCode(DataDictType.METER_USE_TYPE);
		List<DataDict> meterFactoryCodeList = dataDictService.getListByTypeCode(DataDictType.METER_FACTORY_CODE);
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();//获取用水性质
		
		model.addAttribute("waterCaliberList", waterCaliberList);
		model.addAttribute("meterTypeList", meterTypeList);
		model.addAttribute("meterUseList", meterUseList);
		model.addAttribute("readModeList", readModeList);
		model.addAttribute("factoryList", factoryList);
		model.addAttribute("meterModelList", meterModelList);
		model.addAttribute("protocolTypeList", protocolTypeList);
		model.addAttribute("meterUseTypeList", meterUseTypeList);
		model.addAttribute("meterFactoryCodeList", meterFactoryCodeList);
		model.addAttribute("priceTypeMapList", priceTypeMapList);
	
		
		return TEMPLATE_PATH + "meters_dialog_add";
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
			meterMap.put("protocolTypeValue", this.getDataDictValue(EnumDataDictType.PROTOCOL_TYPE.getCode(), meter.getProtocolType()));//水表协议类型
			meterMap.put("meterUseTypeValue", this.getDataDictValue(EnumDataDictType.METER_USE_TYPE.getCode(), meter.getMeterUseType()));//水表用水类型
			meterMap.put("meterFactoryCodeValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY_CODE.getCode(), meter.getMeterFactoryCode()));//水表厂家代码
			meterMap.put("waterUseValue", this.getPriceTypeName(meter.getWaterUse()));//用水性质名称
			String waterPriceName = "";
			if(meter.getPriceCode() != null) {
				SysWaterPrice waterPrice = waterPriceService.getWaterPriceByPriceCode(meter.getPriceCode());
				waterPriceName = waterPrice.getLadderName();
			}
			
			meterMap.put("waterPriceName", waterPriceName);//水价
			
			Example exampleLocation = new Example(LocationMeter.class);
			exampleLocation.createCriteria().andEqualTo("meterId", meterId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
			List<LocationMeter> locationList = locationMeterService.selectByExample(exampleLocation);
			String locationName = "";
			if(locationList.size() > 0) {//判断客户是否绑定地理位置
				for(LocationMeter temp : locationList) {
					String place = locationService.getPlace(temp.getTraceIds());//查询地理位置全称
					locationName = place+"；"+locationName;
				}
			}
			
			model.addAttribute("locationName",locationName);
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
	 * @Title: updateMeters
	 * @Description: 修改
	 * @param meter
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateMeters(Meters meter, Long prevLocationId, Long locationId) throws Exception {
		
		Example example = new Example(Meters.class);
		example.createCriteria().andEqualTo("steelSealNo", meter.getSteelSealNo()).andNotEqualTo("id", meter.getId());
		List<Meters> meterList = metersService.selectByExample(example);
		if(meterList!=null && meterList.size()>0) {
			return RequestResultUtil.getResultUpdateWarn("钢印号重复！请重新输入！");
		}
		if(meter.getChannelNo() == null) {
			meter.setChannelNo("0");
		}
		
		int row = metersService.updateMeters(meter, prevLocationId, locationId);
		if (row>0){
			Map<String, Object> respMap = RequestResultUtil.getResultUpdateSuccess();
			respMap.put("newNodes", meter);
			return respMap;
		}else{
			return RequestResultUtil.getResultUpdateWarn();
		}
		
	}
	
	/**
	 * @Title: updateStatus
	 * @Description: 修改表计状态（用水状态或停/复水状态）
	 * @param meterId
	 * @param meterStatus
	 * @return 
	 */
	@RequestMapping(value = "/update-status", produces = "application/json")
	@ResponseBody
	public Object updateStatus(Long meterId ,Integer meterStatus) {
		
		try {
			int rows = metersService.updateMeterStatus(meterId, meterStatus);
			if(rows>0) {
				return RequestResultUtil.getResultUpdateSuccess();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultUpdateWarn();

	}

	/**
	 * @Title: updatePidById
	 * @Description: 拖拽后更新数据库
	 * @param request
	 * @param updateDataListJSON
	 * 		[{"id":1,"pid":0},{"id":2,"pid":1,},...]
	 * @return 
	 */
	@RequestMapping(value = "/updatePidById", produces = "application/json")
	@ResponseBody
	public Object updatePidById(HttpServletRequest request, String updateDataListJSON){
		
		List<Meters> metgerList = new ArrayList<>();//需要更新的数据
		
		JSONArray updateDataList = JSON.parseArray(updateDataListJSON);
		for(int i=0; i<updateDataList.size(); i++) {
			String dataStr = updateDataList.get(i).toString();
			JSONObject data = JSON.parseObject(dataStr);
			String id = data.get("id").toString();
			String pid = data.get("pid").toString();
			if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(pid)) {
				Meters meter = new Meters();
				meter.setId(Long.valueOf(id));
				meter.setPid(Long.valueOf(pid));
				metgerList.add(meter);
			}
		}
		
		int rows = metersService.updateListById(metgerList);
		if (rows>0){
			return RequestResultUtil.getResultSaveSuccess();
		}else{
			return RequestResultUtil.getResultSaveWarn();
		}
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
	
	@RequestMapping(value = "/register", produces = "application/json")
	@ResponseBody
	public Object register(Model model) throws Exception {

		return RequestResultUtil.getResultSaveWarn("注册成功！");

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
	
	/**
	 * @Title: sortList
	 * @Description: List排序
	 * @param resultList
	 * @param metersList
	 * @param meterId 
	 */
	private void sortList(List<Meters> resultList, List<Meters> metersList,Long meterId) {
		
		for(int i=0; i<metersList.size(); i++){
			Meters meter = metersList.get(i);
        	if(meter.getPid().equals(meterId)){
        		//System.out.println(privilege.getType()+","+privilege.getName());
                resultList.add(meter);
                sortList(resultList, metersList,meter.getId());
            }
        }
    }
	
	//-------------------------查询客户信息-----------------------------------
	/**
	 * @Title: customersSearch
	 * @Description: 主页
	 * @param model
	 * @param functionModule
	 * 			显示客户档案信息
	 * @return 
	 */
	@RequestMapping(value = "/cmain")
	public String customersSearch(Model model) {
		
		
		return TEMPLATE_PATH_CUSTOMER + "meter_customer_main";
	}
	
	
	/**
	 * @Title: customersTable
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @param pageNum	页号
	 * @param pageSize	页大小
	 * @param searchCond	查询条件
	 * @return 
	 */
	@RequestMapping(value = "/ctable")
	public String customersTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long meterId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> customerMapList = customersService.getCustomerListByMeterId(meterId, searchCond);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		for(Map<String, Object> customerMap : customerMapList) {
			
			String waterStatus = customerMap.get("WATER_STATUS").toString();
			String customerType = "";
			if(customerMap.get("CUSTOMER_TYPE") != null) {//判断客户类型字段是否为空，一般情况下都不为空
				customerType = customerMap.get("CUSTOMER_TYPE").toString();
			}
			
			String defaultCustomer = customerMap.get("DEFAULT_CUSTOMER").toString();
			
			String waterStatusStr = null;//用水状态
			String customerTypeStr = null;//客户类型
			String defaultCustomerStr = null;//默认客户
			
			if(StringUtils.isNotBlank(waterStatus)) {
				waterStatusStr = EnumWaterStatus.getName(Integer.parseInt(waterStatus));
			}
			if(StringUtils.isNotBlank(customerType)) {
				customerTypeStr = EnumCustomerType.getName(Integer.parseInt(customerType));
			}
			if(StringUtils.isNotBlank(defaultCustomer)) {
				defaultCustomerStr = EnumDefaultStatus.getName(Integer.parseInt(defaultCustomer));
			}
			
			customerMap.put("waterStatusStr", waterStatusStr);//用水状态
			customerMap.put("customerTypeStr", customerTypeStr);//客户类型
			customerMap.put("defaultCustomerStr", defaultCustomerStr);//默认客户
			
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("customerMapList", customerMapList);  //列表数据
		
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH_CUSTOMER + "customer_table";
	}
	
	// -------------------------设置默认客户-----------------------------------
	/**
	 * @Title: setDefaultCustomer
	 * @Description: 设置默认客户
	 * @param request
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/set-default-customer", produces = "application/json")
	@ResponseBody
	public Object setDefaultCustomer(HttpServletRequest request, Long meterId, Long customerId) {

		try {
			
			int rows = customerMeterService.setDefaultCustomer(customerId, meterId);
			if(rows>0) {
				return RequestResultUtil.getResultSuccess("设置默认客户成功！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("操作异常！");
	}
	
	/**
	 * @Title: loadmodicustomerialog
	 * @Description: 加载增加客户信息对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodicustomerialog")
	public String loadCustomerDialog(Long itemId, Model model) {
		
		//加载数据字典-用水性质
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		//用水性质，查询价格类型编码和名称
		List<Map<String, Object>> priceTypeMapList  = waterPriceService.getPriceTypeList();
						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		
		//读取需要编辑的条目
		Customers currItem=customersService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH_CUSTOMER + "customer_dialog_edit";
	}
	
	//------------------------------	查询欠费金额	------------------------------
	/**
	 * @Title: verifyOwedAmount
	 * @Description: 验证欠费金额
	 * @param model
	 * @param meterId
	 * @return
	 * 		success表示无欠费金额，否则返回欠费金额提示
	 */
	@RequestMapping(value = "/verify-owed-amount", produces = "application/json")
	@ResponseBody
	public Object verifyOwedAmount(Model model, Long meterId) {
		
		try {
			
			CustomerMeter cm = new CustomerMeter();
			cm.setMeterId(meterId);
			cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			List<CustomerMeter> cmList = customerMeterService.select(cm);
			if(cmList!=null && cmList.size()>0) {
				cm = cmList.get(0);
				Long customerId = cm.getCustomerId();//绑定表计的客户ID
				//判断客户欠费金额是否大于0，大于0表示有欠费，否则表示无欠费
				BigDecimal zero = new BigDecimal("0");
				BigDecimal owedAmount = customerAccountItemService.getCurrBillOwedAmount(customerId);
				if(BigDecimalUtils.greaterThan(owedAmount, zero)) {
					return RequestResultUtil.getResultFail("该客户欠费金额为【"+owedAmount+"】，请结清所有欠款后再继续！");
				}
			}
			
			return RequestResultUtil.getResultSuccess("该客户无欠费金额，可继续操作！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultFail("操作异常，请重新操作！");

	}
	
	/**
	 * @Title: changeMeterVerify
	 * @Description: 换表时验证
	 * @param model
	 * @param meterId
	 * @return 
	 */
	@RequestMapping(value = "/change-meter-verify", produces = "application/json")
	@ResponseBody
	public Object changeMeterVerify(Model model, Long meterId) {
		
		try {
			
			boolean isChangeMeter = meterRecordService.changeMeterVerify(meterId);
			if(isChangeMeter) {
				return RequestResultUtil.getResultSuccess("允许换表，可继续操作！");
			}
			return RequestResultUtil.getResultFail("不允许换表，请确认所有抄表记录均已开账！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultFail("操作异常，请重新操作！");

	}
	
	
	//------------------------------	导出水表数据Excel	------------------------------
	/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-meters-excel")
	public void exportMeterRecordExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, String traceIds) {
		
		String period = DateUtils.getYearMonth();//期间默认本月
		//excel标题
		String[] titles = { "地理位置","水表位置", "水表编号", "水表类型", "水表型号", "水表口径", "水表钢印号", "水表厂家", "水表检定日期", "水表用途", "表底数", "抄表方式", "虚实表", "采集器地址", "表地址", "生命周期状态", "水价分类", "水价"};
		//excel列名
		String[] columnNames = { "place","meterPlace", "meterNo", "meterType", "meterModel", "caliber", "steelSealNo", "factory", "checkDateStr", "meterUse", "newMeterBottom", "readMode", "virtualReal",  "collectorAddr","meterAddress", "cycleStatus", "waterUse", "priceCode"};
		//sheet名
		String sheetName = period+"水表数据";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, traceIds);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//获取导出EXCEL的文件名
		String fileName = this.getFileName(period, traceIds);
		
		File file = new File(realPath+fileName);
		
		//文件输出流
	    FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    try {
			wb.write(outStream);
			outStream.flush();
			outStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    System.out.println("导出文件成功！文件导出路径：--"+file);
	    
	    try {
			DownLoadFileUtil.downLoad(fileName, "xls", realPath+fileName, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * @Title: getExportExcelData
	 * @Description: 获取导出excel
	 * @return 
	 */
	private List<Map<String, Object>> getExportExcelData(String searchCond, String traceIds){
		List<Map<String, Object>> meterMapList = metersService.getExportMeterData(searchCond, traceIds);
		
		for(Map<String, Object> meterMap : meterMapList) {
			String currTraceIds = meterMap.get("TRACE_IDS").toString();//地理位置痕迹ID（用英文减号分隔）
			
			String place = locationService.getPlace(currTraceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
			meterMap.put("place", place);
			
			if(meterMap.get("PLACE") != null) {//水表编号
				meterMap.put("meterPlace", meterMap.get("PLACE").toString());
			}
			
			if(meterMap.get("METER_NO") != null) {//水表编号
				meterMap.put("meterNo", meterMap.get("METER_NO").toString());
			}
			if(meterMap.get("METER_TYPE") != null) {//水表类型
				String meterType = this.getDataDictValue(EnumDataDictType.METER_TYPE.getCode(), meterMap.get("METER_TYPE").toString());
				meterMap.put("meterType", meterType);
			}
			if(meterMap.get("METER_MODEL") != null) {//水表型号
				String meterModel = this.getDataDictValue(EnumDataDictType.METER_MODEL.getCode(), meterMap.get("METER_MODEL").toString());
				meterMap.put("meterModel",meterModel);
			}
			if(meterMap.get("CALIBER") != null) {//水表口径
				meterMap.put("caliber",meterMap.get("CALIBER").toString());
			}
			if(meterMap.get("STEEL_SEAL_NO") != null) {//水表钢印号
				meterMap.put("steelSealNo",meterMap.get("STEEL_SEAL_NO").toString());
			}
			if(meterMap.get("FACTORY") != null) {//水表厂家
				String factory = this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meterMap.get("FACTORY").toString());
				meterMap.put("factory",factory);
			}
			if(meterMap.get("CHECK_DATE") != null) {//检定日期
				meterMap.put("checkDateStr",meterMap.get("CHECK_DATE").toString());
			}
			if(meterMap.get("METER_USE") != null) {//水表用途
				String meterUse = this.getDataDictValue(EnumDataDictType.METER_USE.getCode(), meterMap.get("METER_USE").toString());
				meterMap.put("meterUse", meterUse);
			}
			if(meterMap.get("NEW_METER_BOTTOM") != null) {//表底数
				meterMap.put("newMeterBottom",meterMap.get("NEW_METER_BOTTOM").toString());
			}
			if(meterMap.get("READ_MODE") != null) {//抄表方式
				String readMode = this.getDataDictValue(EnumDataDictType.METER_READ_MODE.getCode(), meterMap.get("READ_MODE").toString());
				meterMap.put("readMode",readMode);
			}
			if(meterMap.get("VIRTUAL_REAL") != null) {//虚实表
				meterMap.put("virtualReal",EnumMeterVirtualReal.getName(Integer.valueOf(meterMap.get("VIRTUAL_REAL").toString())));
			}
			if(meterMap.get("METER_ADDRESS") != null) {//表地址
				meterMap.put("meterAddress",meterMap.get("METER_ADDRESS").toString());
			}
			if(meterMap.get("COLLECTOR_ADDR") != null) {//采集器地址
				meterMap.put("collectorAddr",meterMap.get("COLLECTOR_ADDR").toString());
			}
			
			if(meterMap.get("CYCLE_STATUS") != null) {//水表生命周期状态
				meterMap.put("cycleStatus",EnumMeterCycleStatus.getName(Integer.valueOf(meterMap.get("CYCLE_STATUS").toString())));
			}
			if(meterMap.get("WATER_USE") != null) {//表地址
				String waterUse = this.getDataDictValue(EnumDataDictType.WATER_USE.getCode(), meterMap.get("WATER_USE").toString());
				meterMap.put("waterUse",waterUse);
			}
			if(meterMap.get("PRICE_CODE") != null) {//采集器地址
				SysWaterPrice waterPrice = waterPriceService.getWaterPriceByPriceCode(meterMap.get("PRICE_CODE").toString());
				meterMap.put("priceCode",waterPrice.getLadderName());
			}
			
		}
		
		return meterMapList;
	}
	
	/**
	 * 获取文件路径
	 * @param request
	 * @return
	 */
	private String getRealPath(HttpServletRequest request){
		String realPath = this.getPath();
		realPath = realPath+File.separator+"export excel"+File.separator;
		
		File temp = new File(realPath);
		//如果文件路径不存在，则创建
		if(!temp.exists()){
			temp.mkdirs();
		}
		return realPath;
	}
	/**
	 * 获取导出EXCEL文件名
	 * @return
	 */
	private String getFileName(String period, String traceIds){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = sdf.format(new Date());
		String place = locationService.getPlace(traceIds);//获取地理位置信息（小区 楼号-单元-门牌号）
		// excel文件名
		String fileName = period;
		if(StringUtils.isNotBlank(place)) {
			fileName = period+"-"+place;
		}
		
		fileName = fileName+"-"+times+"-"+"水表数据"+".xls";
		
		return fileName;
	}
	
	//------------------------------	上传文件通用方法	------------------------------
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getLinuxUploadFolder();
		if(this.isWindowsOS()) {
			path = uploadFileConfig.getWindowsUploadFolder();
		}
		System.out.println("----------"+path);
		return path;
	}
	
	/**
	 * @Title: isWindowsOS
	 * @Description: 判断操作系统是否是Windows
	 * @return 
	 * 		true=windows;false=其他
	 */
	private boolean isWindowsOS() {
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		System.out.println("==============================操作系统："+os);
    		return true;
    	}else {
    		System.out.println("==============================操作系统："+os);
    		return false;
    	}
	}

}