package com.learnbind.ai.controller.location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.LocationBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumLocalNodeType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.WorkOrderFunctionModuleConstant;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.InstallEngineering;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.engineering.EngineeringService;
import com.learnbind.ai.service.engineering.LocationEngineeringService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.util.pinyin4j.Pinyin4jUtils;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.controller.location
 *
 * @Title: LocationController.java
 * @Description: 地理位置前端控制器
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:18:20
 * @version V1.0
 *
 */
@Controller
@RequestMapping(value = "/location")
public class LocationController {

	private static Log log = LogFactory.getLog(LocationController.class);

	private static final String TEMPLATE_PATH = "location/"; // 页面
	private static final String TEMPLATE_PATH_CUSTOMER = "location/customertab/"; // 页面
	private static final String TEMPLATE_PATH_METER = "location/metertab/"; // 页面
	private static final String TEMPLATE_PATH_CUSTOMER_DETAIL = "customers/"; // 页面
	private static final int PAGE_SIZE = 8; // 页大小

	@Autowired
	private LocationService locationService;// 地理位置服务
	@Autowired
	private DataDictService dataDictService;// 数据字典服务
	@Autowired
	private WaterPriceService waterPriceService;//用水性质
	@Autowired
	private DiscountService discountService; // 政策减免
	@Autowired
	private MetersService metersService; // 表计档案
	@Autowired
	private CustomersService customersService; // 客户档案
	@Autowired
	private CustomerMeterService customerMeterService; // 客户档案
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关系
	@Autowired
	private LocationMeterService locationMeterService;//地理位置-表计关系
	@Autowired
	private EngineeringService engineeringService;//工程关系
	@Autowired
	private LocationEngineeringService locationEngineeringService;//地理位置-工程关系

	/**
	 * @Title: starter
	 * @Description: 起始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model, String functionModule) {
		if(StringUtils.isBlank(functionModule)) {
			functionModule = WorkOrderFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(WorkOrderFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		return TEMPLATE_PATH + "location_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model, String functionModule) {
		if(StringUtils.isBlank(functionModule)) {
			functionModule = WorkOrderFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(WorkOrderFunctionModuleConstant.FUNCTION_MODULE, functionModule);

		// 查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("dictList", dictList);

		return TEMPLATE_PATH + "location_main";
	}
	
	/**
	 * @Title: getNodes
	 * @Description: ztree异步请求获取节点
	 * @param response
	 * @param model
	 * @param id 
	 */
	@RequestMapping(value = "/get-nodes", produces = "application/json")
	public void getNodes(HttpServletResponse response, Model model, Long id) {
		
		try {
			
			if(id==null){
				id = 0l;
			}
			
			List<LocationBean> locationList = locationService.getChildListById(id);
			String locationMapListJson = JSON.toJSONString(locationList);
			
			response.setContentType("text/text;charset=utf-8"); 
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(locationMapListJson);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Title: search
	 * @Description: 查询地理位置
	 * @param model
	 * @param searchCond
	 * @param id
	 * @param action	-1=上一个；1=下一个
	 * @return 
	 */
	@RequestMapping(value = "/search", produces = "application/json")
	@ResponseBody
	public Object search(Model model, String searchCond, Long id, int action) {
		
		try {
			
			searchCond = Pinyin4jUtils.toPyUppercase(searchCond);//查询条件转拼音
			log.info("location search : "+searchCond);
			Location location=null;
			
			if(id==null || action==0){
				//返回第一个符合条件的记录
				location = locationService.getFirstBySearchCond(searchCond);
			} else {
				location = locationService.getOneBySearchCond(searchCond, id, action);
			}
			
			Map<String, Object> respMap = RequestResultUtil.getResultSuccess("查询成功！");
			respMap.put("location", location);
			return respMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("查询异常，请重新操作！");
	}
	
	/**
	 * @Title: searchLocationFullname
	 * @Description: 根据traceIds查询地理位置全名称
	 * @param model
	 * @param traceIds
	 * @return 
	 */
	@RequestMapping(value = "/search-fullname", produces = "application/json")
	@ResponseBody
	public Object searchLocationFullname(Model model, String traceIds) {
		
		try {
			
			String fullname = "";//地理位置全名（小区与其他用空格分隔，楼号、单元和门牌号用减号分隔）
			if(StringUtils.isNotBlank(traceIds)) {
				fullname = this.getFullname(traceIds);
			}
			
			Map<String, Object> respMap = RequestResultUtil.getResultSuccess("查询成功！");
			respMap.put("fullname", fullname);
			return respMap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("查询异常，请重新操作！");
	}
	
	/**
	 * @Title: getFullname
	 * @Description: 根据地理位置traceIds获取全名称
	 * @param traceIds
	 * @return 
	 */
	private String getFullname(String traceIds) {
		String fullname = "";//地理位置全名（小区与其他用空格分隔，楼号、单元和门牌号用减号分隔）
		
		String[] traceIdArr = traceIds.split("-");
		for(int i=0; i<traceIdArr.length; i++) {
			String traceIdStr = traceIdArr[i];
			if(StringUtils.isNotBlank(traceIdStr)) {
				Location location = locationService.selectByPrimaryKey(Long.valueOf(traceIdStr));
				if(i==0) {
					fullname = location.getName();//地理位置全名（小区与其他用空格分隔，楼号、单元和门牌号用减号分隔）
				}else {
					fullname += "-"+location.getName();//地理位置全名（小区与其他用空格分隔，楼号、单元和门牌号用减号分隔）
				}
			}
		}
		return fullname;
	}
	
	/**
	 * @Title: loadEditDialog
	 * @Description: 加载编辑对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadEditDialog(Model model, String action, Long locationId, Long locationPid) {

		final String ADD = "add";
		final String EDIT = "edit";
		
		// 查询地理位置节点类型数据字典
		List<DataDict> dictList = dataDictService.getListByTypeCode(DataDictType.LOCAL_NODE_TYPE);
		model.addAttribute("dictList", dictList);

		//查询安装工程
		List<InstallEngineering> engineeringList = engineeringService.selectAll();
		model.addAttribute("engineeringList", engineeringList);
		
		//当前需要编辑的地理位置-工程的工程ID
		//Long engineeringId = null;
		String fullname = null;//地理位置全名称
		Location parentLocation = null;//当前编辑的地理位置的父节点地理位置
		String childNodeType = null;//子节点类型
		if(locationId!=null && locationId>0) {
			
			//当前编辑的地理位置
			//location = locationService.selectByPrimaryKey(locationId);
			
			if(action.equalsIgnoreCase(ADD)) {
				//当前编辑的地理位置的父节点地理位置
				parentLocation = locationService.selectByPrimaryKey(locationId);
				if(parentLocation!=null) {
					//获取地理位置全名称
					if(StringUtils.isNotBlank(parentLocation.getTraceIds())) {
						fullname = this.getFullname(parentLocation.getTraceIds());
					}
					
					//根据节点类型判断子节点类型
					switch (parentLocation.getLocalNodeType()) {
					case "BLOCK":
						childNodeType = "BUILDING";//子节点类型
						break;
					case "BUILDING":
						childNodeType = "UNIT";//子节点类型
						break;
					case "UNIT":
						childNodeType = "ROOM";//子节点类型
						break;
					default:
						break;
					}
				}
				
			}else {
				//当前编辑的地理位置的父节点地理位置
				parentLocation = locationService.selectByPrimaryKey(locationPid);
				if(parentLocation!=null) {
					//获取地理位置全名称
					if(StringUtils.isNotBlank(parentLocation.getTraceIds())) {
						fullname = this.getFullname(parentLocation.getTraceIds());
					}
				}
				
//				//查询当前选择的工程ID
//				LocationEngineering leTemp = new LocationEngineering();
//				leTemp.setLocationId(locationId);
//				List<LocationEngineering> leList = locationEngineeringService.select(leTemp);
//				if(leList!=null && leList.size()>0) {
//					LocationEngineering locationEngineering = leList.get(0);
//					engineeringId = locationEngineering.getEngineeringId();
//				}
			}
			
		}
		
		//model.addAttribute("engineeringId", engineeringId);
		model.addAttribute("locationFullname", fullname);
		model.addAttribute("parentLocation", parentLocation);
		model.addAttribute("childNodeType", childNodeType);

		return TEMPLATE_PATH + "location_dialog_edit";
	}

	/**
	 * @Title: insert
	 * @Description: 增加
	 * @param request
	 * @param location
	 * @return
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object insert(HttpServletRequest request, Location location) {

//		Location record = new Location();
//		record.setCode(location.getCode());
//		List<Location> locationList = locationService.select(record);
//		if (locationList != null && locationList.size() > 0) {
//			return RequestResultUtil.getResultAddWarn("编码重复！请重新输入！");
//		}
		
		String currPyCode = "";
		String currPyLongCode = "";
		try {
			//获取拼音简码
			String pyFirstCodeU = Pinyin4jUtils.toPyFirstUppercase(location.getName());
			currPyCode = pyFirstCodeU;
			//获取拼音全码
			String pyCodeU = Pinyin4jUtils.toPyUppercase(location.getName());
			//拼接拼音简码和拼音全码
			currPyLongCode = pyFirstCodeU+"@&&@"+pyCodeU;
			
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		} 
		
		//获取拼音longCode编码
		String pyCode = locationService.getPyCode(location.getPid(), currPyCode);
		location.setPycode(pyCode);
		//获取拼音longCode编码
		String pyLongCode = locationService.getPyLongCode(location.getPid(), currPyLongCode);
		location.setPylongcode(pyLongCode);
		
		//获取当前节点下行数+1做为排序的序号
		Long sortValue = locationService.getSortValue(location.getPid());
		location.setSortValue(sortValue);
		
		//获取地理位置longCode编码
		//String longCode = locationService.getLongCode(location.getPid(), location.getCode());
		//location.setLongCode(longCode);
		
		location.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		Long locationId = locationService.insertLocation(location);
		if (locationId > 0) {

			location = locationService.selectByPrimaryKey(locationId);
			Map<String, Object> respMap = RequestResultUtil.getResultAddSuccess();
			respMap.put("newNodes", location);
			return respMap;
		}
		return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: update
	 * @Description: 修改
	 * @param request
	 * @param location
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(HttpServletRequest request, Location location, Long oldEngineeringId, Long engineeringId) {

//		Example example = new Example(Location.class);
//		example.createCriteria().andEqualTo("code", location.getCode()).andNotEqualTo("id", location.getId());
//		List<Location> locationList = locationService.selectByExample(example);
//		if (locationList != null && locationList.size() > 0) {
//			return RequestResultUtil.getResultUpdateWarn("编码重复！请重新输入！");
//		}

		int row = locationService.updateById(location, false);
		if (row > 0) {
			
			Map<String, Object> respMap = RequestResultUtil.getResultUpdateSuccess();
			respMap.put("newNodes", location);
			return respMap;
		} else {
			return RequestResultUtil.getResultUpdateWarn();
		}
	}

	/**
	 * @Title: updateDropLocation
	 * @Description: 拖拽后更新数据库
	 * @param request
	 * @param locationId
	 * @param parentLocationId
	 * @return 
	 */
	@RequestMapping(value = "/update-drop-location", produces = "application/json")
	@ResponseBody
	public Object updateDropLocation(HttpServletRequest request, Long locationId, Long parentLocationId) {

//		List<Location> locationList = new ArrayList<>();// 需要更新的数据
//
//		JSONArray updateDataList = JSON.parseArray(updateDataListJSON);
//		for (int i = 0; i < updateDataList.size(); i++) {
//			String dataStr = updateDataList.get(i).toString();
//			JSONObject data = JSON.parseObject(dataStr);
//			String id = data.get("id").toString();
//			String pid = data.get("pid").toString();
//			String level = data.get("level").toString();
//			if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(pid) && StringUtils.isNotBlank(level)) {
//				Location location = new Location();
//				location.setId(Long.valueOf(id));
//				location.setPid(Long.valueOf(pid));
//				location.setLocationLevel(Integer.valueOf(level));
//				locationList.add(location);
//			}
//		}
		try {
			//parentLocationId==null时表示拖拽到了根节点
			if(parentLocationId==null) {
				parentLocationId = 0l;
			}
			int rows = locationService.updateDropLocation(locationId, parentLocationId);

			//int rows = locationService.updateListById(locationList);
			if (rows > 0) {
				return RequestResultUtil.getResultSaveSuccess();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultSaveWarn();
		
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param request
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(HttpServletRequest request, Long id) {
		int row = locationService.deleteById(id);
		if (row > 0) {
			return RequestResultUtil.getResultDeleteSuccess();
		} else {
			return RequestResultUtil.getResultDeleteWarn();
		}
	}	
	
	//查询小区
	@RequestMapping(value = "/get-block", produces = "application/json")
	@ResponseBody
	public Object getBlock(HttpServletRequest request) {
		List<Location> locationList = locationService.getBlockListByPid(null);
		Map<String, Object> respMap = RequestResultUtil.getResultSuccess("查询成功！");
		respMap.put("locationList", locationList);
		return respMap;
	}
	

	/**
	 * @Title: getBlockBuildingUnit
	 * @Description: 查询小区/楼号/单元
	 * @param request
	 * @param locationId
	 * @param nodeType
	 * @return 
	 */
	@RequestMapping(value = "/get-block-building-unit", produces = "application/json")
	@ResponseBody
	public Object getBlockBuildingUnit(HttpServletRequest request, Long locationId, String nodeType) {

		try {
			List<Location> locationList = new ArrayList<>();
			if(nodeType.equals(EnumLocalNodeType.LOCAL_NODE_TYPE_BLOCK.getCode())) {//小区
				locationList = locationService.getBlockListByPid(locationId);
			}else if(nodeType.equals(EnumLocalNodeType.LOCAL_NODE_TYPE_BUILDING.getCode())) {//楼号
				locationList = locationService.getBuildingListByPid(locationId);
			}else if(nodeType.equals(EnumLocalNodeType.LOCAL_NODE_TYPE_UNIT.getCode())) {//单元
				locationList = locationService.getUnitListByPid(locationId);
			}
			
			Map<String, Object> respMap = RequestResultUtil.getResultSuccess("查询成功！");
			respMap.put("locationList", locationList);
			return respMap;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("查询异常！");
	}

	/**
	 * @Title: loadGlobalLocationPage
	 * @Description: 加载通用地理位置页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-global-location-page")
	public String loadGlobalLocationPage(Model model) {
		return TEMPLATE_PATH + "global_location_page";
	}
	
	/**
	 * @Title: loadLocationTree
	 * @Description: 加载location-tree  (TEST)  加载树页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-location-tree")
	public String loadLocationTree(Model model) {		
		return "component/locationtree/location_tree";
	}
	
	// -------------------------查询客户信息-----------------------------------
	/**
	 * @Title: customersSearch
	 * @Description: 主页
	 * @param model
	 * @param functionModule 显示客户档案信息
	 * @return
	 */
	@RequestMapping(value = "/cmain")
	public String customersSearch(Model model) {

		return TEMPLATE_PATH_CUSTOMER + "location_customer_main";
	}

	/**
	 * @Title: customersTable
	 * @Description: 查询客户列表
	 * @param model
	 * @param functionModule 功能模块标识：用于页面显示不同功能
	 * @param pageNum        页号
	 * @param pageSize       页大小
	 * @param searchCond     查询条件
	 * @return
	 */
	@RequestMapping(value = "/ctable")
	public String customersTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long locationId, String traceIds) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//List<Map<String, Object>> customerMapList = customersService.getCustomerListByLocationId(locationId, searchCond);
		List<Map<String, Object>> customerMapList = customersService.getCustomerListByTraceIds(traceIds, searchCond);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(customerMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		for(Map<String, Object> customerMap : customerMapList) {
			String place = locationService.getPlace(customerMap.get("TRACE_IDS").toString());
			customerMap.put("place", place);
//			Long customerId = Long.valueOf(customerMap.get("ID").toString());
//			Example example = new Example(CustomerMeter.class);
//			example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//			List<CustomerMeter> customerMeterList = customerMeterService.selectByExample(example);
//			for(CustomerMeter cm : customerMeterList) {
//				LocationMeter lm = locationMeterService.getLocationByMeterId(cm.getMeterId());
//				if(lm.getLocationId().equals(locationId)) {
//					customerMap.put("DEFAULT_CUSTOMER", cm.getDefaultCustomer());
//				}
//			}
			
			
		}
		// 传递如下数据至前台页面
		model.addAttribute("customerMapList", customerMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH_CUSTOMER + "customer_table";
	}
	
	/**
	 * @Title: loadCustomerTable
	 * @Description: 加载地理位置绑定的客户信息
	 * @param model
	 * @param customerType
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-customer-detail-table")
	public String loadCustomerTable(Model model, Long customerId) {
		
		//加载数据字典-用水性质
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		//查询用水性质
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();
						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		//读取需要编辑的条目
		Customers currItem=customersService.selectByPrimaryKey(customerId);
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("priceTypeCode", currItem.getWaterUse()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
		model.addAttribute("currItem",currItem);
		model.addAttribute("waterPriceList",waterPriceList);
		
		//判断客户类型是个人客户还是单位客户
		if(currItem.getCustomerType()==EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {
			return TEMPLATE_PATH_CUSTOMER_DETAIL + "edit_customer/edit_person_customer_table";
		}
		
		return TEMPLATE_PATH_CUSTOMER_DETAIL + "edit_customer/edit_company_customer_table";
		
	}

	/**
	 * @Title: loadmodicustomerialog
	 * @Description: 加载增加客户信息对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadmodicustomerialog")
	public String loadCustomerDialog(Long itemId, Model model) {

		// 读取需要编辑的条目
		Customers currItem = customersService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem", currItem);
		
		Location location = this.getLocationByCustomerId(itemId);//查询地理位置
		String place = "";
		if(location != null) {
			place = locationService.getPlace(location.getTraceIds());//查询地理位置全称
		}
		model.addAttribute("location",location);
		
		model.addAttribute("place",place);
		//加载数据字典-用水性质
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		//查询用水性质
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();
						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);

		return TEMPLATE_PATH_CUSTOMER + "customer_dialog_edit";
	}

	// -------------------------查询表计信息-----------------------------------
	/**
	 * @Title: metersSearch
	 * @Description: 主页
	 * @param model
	 * @param functionModule 显示客户档案信息
	 * @return
	 */
	@RequestMapping(value = "/mmain")
	public String metersSearch(Model model) {

		return TEMPLATE_PATH_METER + "location_meter_main";
	}

	/**
	 * @Title: metersTable
	 * @Description: 表计列表
	 * @param model
	 * @param functionModule 功能模块标识：用于页面显示不同功能
	 * @param pageNum        页号
	 * @param pageSize       页大小
	 * @param searchCond     查询条件
	 * @return
	 */
	@RequestMapping(value = "/mtable")
	public String metersTable(Model model, Integer pageNum, Integer pageSize, String searchCond, Long locationId, String traceIds) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		//List<Meters> meterList = metersService.getMeterListByLocationId(locationId, searchCond);
		List<Meters> meterList = metersService.getMeterListByTraceIds(traceIds, searchCond);
		PageInfo<Meters> pageInfo = new PageInfo<>(meterList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> meterMapList = new ArrayList<>();
		for(Meters meter : meterList) {
			Map<String, Object> meterMap = EntityUtils.entityToMap(meter);
			meterMap.put("meterTypeValue", this.getDataDictValue(EnumDataDictType.METER_TYPE.getCode(), meter.getMeterType()));//水表类型
			meterMap.put("factoryValue", this.getDataDictValue(EnumDataDictType.METER_FACTORY.getCode(), meter.getFactory()));//水表生产厂家
			meterMapList.add(meterMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("meterList", meterMapList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", searchCond);

		return TEMPLATE_PATH_METER + "meter_table";
	}

	/**
	 * @Title: loadmodimeterialog
	 * @Description: 加载表计信息对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadmodimeterialog")
	public String loadMeterDialog(Long itemId, Model model) {

		// 读取需要编辑的条目
		if(itemId!=null) {
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
		}
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

		return TEMPLATE_PATH_METER + "meter_dialog_edit";
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
	 * @Title: getLocationByMeterId
	 * @Description: 根据表计ID查询绑定的地理位置
	 * @param customerId
	 * @return 
	 */
	private Location getLocationByCustomerId(Long customerId) {
		List<Long> locationIdList = locationCustomerService.getLocationIdListByCustId(customerId);
		if(locationIdList!=null && locationIdList.size()>0) {
			Long locationId = locationIdList.get(0);
			Location location = locationService.selectByPrimaryKey(locationId);
			return location;
		}
		return null;
	}
	
	/**
	 * @Title: generatePycode
	 * @Description: 更新pycode和pylongcode
	 * @return 
	 */
//	@RequestMapping(value = "/generate-pycode")
//	@ResponseBody
//	public Object generatePycode() {
//		int rows = 1;
//		try {
//			Location location = new Location();
//			location.setPid(0l);
//			List<Location> locationList = locationService.select(location);
//			for(Location record : locationList) {
//				rows = locationService.updateById(record);
//			}
//			if(rows>0) {
//				return RequestResultUtil.getResultSuccess("操作成功！");
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return RequestResultUtil.getResultFail("操作异常！");
//	}
	

}