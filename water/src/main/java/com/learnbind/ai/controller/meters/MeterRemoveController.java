package com.learnbind.ai.controller.meters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
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
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.meters
 *
 * @Title: MeterRemoveController.java
 * @Description: 暂拆复装前端控制器
 *
 * @author Administrator
 * @date 2019年8月6日 下午7:03:23
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/meter-remove")
public class MeterRemoveController {
	private static Log log = LogFactory.getLog(MetersController.class);
	private static final String TEMPLATE_PATH = "meter_remove/"; // 页面
	private static final String TEMPLATE_PATH_CUSTOMER = "meter_remove/customertab/"; // 页面
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
	
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @param status
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model, String functionModule, Integer status) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = MeterFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(MeterFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		if(status==null) {
			status = EnumMeterSettingStatus.ZERO.getValue();
		}
		model.addAttribute("status", status);//暂拆/复装状态
		
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
	public String table(Model model, String functionModule, Integer pageNum, Integer pageSize, String searchCond, String traceIds, Integer status) {

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
			meterList = metersService.getRemoveMeterList(searchCond, traceIds, status);
		} else {
			meterList = metersService.searchRemoveMeters(searchCond, status);
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
	
		
		return TEMPLATE_PATH + "meters_dialog_add";
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
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();//获取用水性质
		
		model.addAttribute("waterCaliberList", waterCaliberList);
		model.addAttribute("meterTypeList", meterTypeList);
		model.addAttribute("meterUseList", meterUseList);
		model.addAttribute("readModeList", readModeList);
		model.addAttribute("factoryList", factoryList);
		model.addAttribute("meterModelList", meterModelList);
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		
		if(meterId!=null) {
			//读取需要编辑的条目
			Meters currItem=metersService.selectByPrimaryKey(meterId);
			model.addAttribute("currItem",currItem);
			Location location = this.getLocationByMeterId(meterId);
			model.addAttribute("location",location);
			model.addAttribute("location",location);
			String place = "";
			if(location != null) {
				place = locationService.getPlace(location.getTraceIds());//查询地理位置全称
			}
			model.addAttribute("place",place);
		}
		
		return TEMPLATE_PATH + "meters_dialog_edit";
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
	 * @Description: 删除	暂未用，删除功用用的是MetersController.java中的delete
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteMeters(Long id) throws Exception {
		try {
			metersService.deleteMeters(id);
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
			String customerType = customerMap.get("CUSTOMER_TYPE").toString();
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
	
	//------------------------------	暂拆/复装处理	------------------------------
	/**
	 * @Title: updateStatus
	 * @Description: 修改暂拆/复装状态
	 * @param model
	 * @param meterId
	 * @param status
	 * @return 
	 */
	@RequestMapping(value = "/update-remove-status", produces = "application/json")
	@ResponseBody
	public Object updateStatus(Model model, Long meterId, Integer status) {
		
		try {
			
			//修改表计表暂拆/复装状态
			Meters meter = new Meters();
			meter.setId(meterId);
			meter.setStatus(status);
			metersService.updateByPrimaryKeySelective(meter);
			this.updateMeterRelStatus(meterId, status);//修改与表计相关的表的暂拆/复装状态（所涉及到的表为地理位置-表计表、客户-表计表）
			
			String msg = "";
			//表计状态 0复装 1暂拆
			if(status==0) {
				msg = "复装成功！";
			}else {
				msg = "暂拆成功！";
			}
			
			return RequestResultUtil.getResultSuccess(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultFail("操作异常，请重新操作！");

	}
	
	/**
	 * @Title: updateMeterRelStatus
	 * @Description: 修改与表计相关的表的暂拆/复装状态（所涉及到的表为地理位置-表计表、客户-表计表）
	 * @param meterId
	 * @param status 
	 */
	private void updateMeterRelStatus(Long meterId, Integer status) {
		//修改地理位置-表计表暂拆/复装状态
		Example example = new Example(LocationMeter.class);
		example.createCriteria().andEqualTo("meterId", meterId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		LocationMeter lm = new LocationMeter();
		lm.setMeterStatus(status);
		locationMeterService.updateByExampleSelective(lm, example);
		//修改客户-表计表暂拆/复装状态
		example = new Example(CustomerMeter.class);
		example.createCriteria().andEqualTo("meterId", meterId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		CustomerMeter cm = new CustomerMeter();
		cm.setMeterStatus(status);
		customerMeterService.updateByExampleSelective(cm, example);
	}
	
}