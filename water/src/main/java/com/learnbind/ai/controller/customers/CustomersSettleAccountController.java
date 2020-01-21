package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumBindStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumOperationType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersSettleAccountController.java
 * @Description: 客户档案-立户
 *
 * @author Thinkpad
 * @date 2019年9月15日 下午3:53:02
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customers-settle-account")
public class CustomersSettleAccountController {
	
	//private static Log log = LogFactory.getLog(CustomersSettleAccountController.class);
	private static final String TEMPLATE_PATH = "customers/settle_account/"; // 页面目录角色

	//解决冲突
	@Autowired
	private CustomersService customersService;//客户档案
	@Autowired
	private DataDictService dataDictService;//数据字典
	@Autowired
	private DiscountService discountService;//政策减免
	@Autowired
	private WaterPriceService waterPriceService;//用水性质
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计服务
	@Autowired
	private CustomersTraceService traceService;//客户档案维护日志
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private MetersService metersService;//表计位置

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @param functionModule
	 * 		功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		
		return TEMPLATE_PATH + "customers_starter";
	}

	/**
	 * @Title: tabs
	 * @Description: 选项卡页
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/tabs")
	public String tabs(Model model) {
		
		
		return TEMPLATE_PATH + "tabs";
	}
	
	/**
	 * @Title: customersSearch
	 * @Description: 主页
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String customersSearch(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		//调用水价配置接口获取用水性质
		List<Map<String, Object>> priceTypeMapList = null;
		priceTypeMapList = waterPriceService.getPriceTypeList();
						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
				
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		
		return TEMPLATE_PATH + "customers_main";
	}

	
	/**
	 * @Title: customersTable
	 * @Description: 加载客户列表
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @param pageNum	页号
	 * @param pageSize	页大小
	 * @param searchCond	查询条件
	 * @param customerStatus	客户状态（可以有多个状态）
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String customersTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String customerStatus, Integer customerType) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		//获取客户状态集合
		List<Integer> statusList = new ArrayList<>();
		if(StringUtils.isNotBlank(customerStatus)) {
			String[] statusArr = customerStatus.split(",");
			for(String status : statusArr) {
				if(StringUtils.isNotBlank(status)) {
					statusList.add(Integer.valueOf(status));
				}
			}
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Customers> customerList = new ArrayList<>();
		if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			customerList = customersService.getCustomersList(searchCond, traceIds, statusList, customerType);
		} else {
			customerList = customersService.searchCustomers(searchCond, statusList, customerType);
		}
		PageInfo<Customers> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(Customers customer : customerList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("waterStatusStr", customer.getWaterStatusStr());//用水状态
			customerMap.put("statusStr", customer.getStatusStr());//客户状态
			
			Long customerId = customer.getId();//客户ID
			
			//获取客户-表计关系的绑定状态
			CustomerMeter cm = new CustomerMeter();
			cm.setCustomerId(customerId);
			cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
 			List<CustomerMeter> cmList = customerMeterService.select(cm);
			if(cmList==null || cmList.size()<=0) {
				customerMap.put("bindStatus", EnumBindStatus.BIND_NO.getValue());
			}else {
				customerMap.put("bindStatus", EnumBindStatus.BIND_YES.getValue());
			}
			
			customerMapList.add(customerMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("customersList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "customers_table";
	}
	
	
	/** 
	*	@Title: addDiscount 
	*	@Description: 新增
	*	@param @param label
	*	@param @return     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addCustomers(Customers customers, Long locationId) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录！");
		}
		
		customers.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		int row = customersService.insertCustomers(customers, locationId);
		//增加客户档案维护日志
		CustomersTrace trace = new CustomersTrace();
		trace.setCustomerId(customers.getId());
		trace.setCustomerName(customers.getCustomerName());
		trace.setOperationTime(new Date());
		trace.setOperatorId(userBean.getId());
		trace.setOperationType(EnumOperationType.INSERT.getValue());//新增用户档案
		trace.setOperatorName(userBean.getRealname());
		//HTML字符编码
		String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(customers));
		trace.setChangeAfter(changeBefore);
		traceService.insertSelective(trace);
		if (row > 0) {
			Map<String, Object> retMap = RequestResultUtil.getResultAddSuccess();
			retMap.put("customerId", customers.getId());
			return retMap;
		}
		return RequestResultUtil.getResultAddWarn();
	}

	/** 
	*	@Title: deleteCustomers
	*	@Description: 删除
	*	@param @param ids 被删除条目对象id所组成的数组
	*	@param @return
	*	@param @throws Exception     
	*	@return Object    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteCustomers(@RequestBody ArrayList<Long> ids) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录！");
		}
		
		try {
			for (Long id : ids) {
				//增加客户档案维护日志
				CustomersTrace trace = new CustomersTrace();
				trace.setCustomerId(id);
				String customerName = customersService.getCustomerNameById(id);
				Customers cus = customersService.selectByPrimaryKey(id);
				trace.setCustomerName(customerName);
				trace.setOperationTime(new Date());
				trace.setOperatorId(userBean.getId());
				trace.setOperationType(EnumOperationType.DELETE.getValue());//删除用户档案
				trace.setOperatorName(userBean.getRealname());
				
				//HTML字符编码
				String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(cus));
				
				trace.setChangeBefore(changeBefore);
				traceService.insertSelective(trace);
				//System.out.println(id);
				customersService.deleteCustomers(id);
				//植入的BUG,用于测试
				//long x=1/0;  
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}


	/** 
	*	@Title: updatCustomers
	*	@Description: 修改 
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateCustomers(Customers customers, Long prevLocationId, Long locationId) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date sysdate = new Date();
		Customers afterCustomer = customersService.selectByPrimaryKey(customers.getId());
		 
		int rows = customersService.updateCustomers(customers, prevLocationId, locationId);
		if(rows>0) {
			//增加客户档案维护日志
			CustomersTrace trace = new CustomersTrace();
			trace.setCustomerId(customers.getId());
			String customerName = customersService.getCustomerNameById(customers.getId());
			trace.setCustomerName(customerName);
			trace.setOperationTime(sysdate);
			trace.setOperatorId(userBean.getId());
			trace.setOperationType(EnumOperationType.UPDATE.getValue());//修改客户档案
			trace.setOperatorName(userBean.getRealname());
			//HTML字符编码
			String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(customers));
			String changeAfter = StringEscapeUtils.escapeHtml(JSON.toJSONString(afterCustomer));
			trace.setChangeBefore(changeBefore);
			trace.setChangeAfter(changeAfter);
			traceService.insertSelective(trace);
			
		}
		
		return RequestResultUtil.getResultUpdateSuccess();
	}

	
	
	
	//*******************************************绑定大表***************************
	
	/**
	 * @param itemId
	 * @param model
	 * @return
	 * 	打开绑定水表对话框
	 */
	@RequestMapping(value = "/load-bind-bigmeter-dialog")
	public String loadBindBigmeterDialog(Model model) {
		
		return TEMPLATE_PATH + "bind_bigmeter_dialog";
	}
	
	/**
	 * @Title: loadBindBigmeterPage
	 * @Description: 加载绑定/解绑表计页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/load-bind-bigmeter-page")
	public String loadBindBigmeterPage(Model model) {
		
		return TEMPLATE_PATH + "bind_bigmeter_page";
	}
	
	/**
	 * @param itemId
	 * @param model
	 * @return
	 * 	打开绑定水表对话框
	 */
	@RequestMapping(value = "/load-bind-meter-dialog")
	public String loadBindMeterDialog(Model model) {
		
		return TEMPLATE_PATH + "bind_meter_dialog";
	}
	
	/**
	 * @Title: customerItemMain
	 * @Description: 加载客户列表主页（对话框中显示）
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/customer-bindmeter-main")
	public String bindMeterMain(Model model, Long customerId) {
		Customers customer = customersService.selectByPrimaryKey(customerId);
		String customerName = customer.getCustomerName();
		model.addAttribute("customerName", customerName);
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		List<DataDict> meterUseList = dataDictService.getListByTypeCode(DataDictType.METER_USE);
		model.addAttribute("meteUseList", meterUseList);
		return TEMPLATE_PATH + "bind_bigmeter_main";
	}
	
	/**
	 * @Title: customerItemTable
	 * @Description: 加载客户列表（对话框中显示）
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @param customerStatus
	 * @return 
	 */
	@RequestMapping(value = "/bind-bigmeter-table")
	public String bindBigmeterTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String meterUse, Long customerId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = new ArrayList<>();
		//if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
			meterList = metersService.getBindBigMeterList(searchCond, traceIds, meterUse, customerId);
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
		
		return TEMPLATE_PATH + "bind_bigmeter_table";
	}
	
	
	/**
	 * @param model
	 * @param customerId
	 * @param meterIdJSON
	 * 			绑定大表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/bind-bigmeter-confirm", produces = "application/json")
	@ResponseBody
	public Object bindBigmeterConfirm(Model model, Long customerId, String meterIdJSON) throws Exception {
		try {
			Date sysdate = new Date();
			int rows = 1;
			List<Long> meterIdList = JSON.parseArray(meterIdJSON, Long.class);
			
			rows = customersService.bindBigMeter(meterIdList, customerId);
			if (rows>0) {
				return RequestResultUtil.getResultSaveSuccess("绑定成功！");
			}
			return RequestResultUtil.getResultSaveWarn("绑定异常，请重新操作！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultSaveWarn("绑定异常，请重新操作！");

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

	//*******************************************解绑大表***************************
	
	/**
	 * @Title: customerItemMain
	 * @Description: 加载客户列表主页（对话框中显示）
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/customer-unbindmeter-main")
	public String unbindMeterMain(Model model, Long customerId) {
		
		Customers customer = customersService.selectByPrimaryKey(customerId);
		String customerName = customer.getCustomerName();
		model.addAttribute("customerName", customerName);
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		List<DataDict> meterUseList = dataDictService.getListByTypeCode(DataDictType.METER_USE);
		model.addAttribute("meteUseList", meterUseList);
		return TEMPLATE_PATH + "unbind_bigmeter_main";
	}
	
	/**
	 * @Title: customerItemTable
	 * @Description: 加载客户列表（对话框中显示）
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @param traceIds
	 * @param customerStatus
	 * @return 
	 */
	@RequestMapping(value = "/unbind-bigmeter-table")
	public String unbindBigmeterTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String meterUse, Long customerId) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Meters> meterList = new ArrayList<>();
		meterList = metersService.getUnBindBigMeterList(searchCond, traceIds, meterUse, customerId);
		
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
		
		return TEMPLATE_PATH + "unbind_bigmeter_table";
	}
	
	
	/**
	 * @param model
	 * @param customerId
	 * @param meterIdJSON
	 * 			绑定大表
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/unbind-bigmeter-confirm", produces = "application/json")
	@ResponseBody
	public Object unbindBigmeterConfirm(Model model, Long customerId, String meterIdJSON) throws Exception {
		try {
			Date sysdate = new Date();
			int rows = 1;
			List<Long> meterIdList = JSON.parseArray(meterIdJSON, Long.class);
			
			rows = customersService.unbindBigMeter(meterIdList, customerId);
			if (rows>0) {
				return RequestResultUtil.getResultSaveSuccess("解绑成功！");
			}
			return RequestResultUtil.getResultSaveWarn("解绑异常，请重新操作！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultSaveWarn("解绑异常，请重新操作！");

	}
		
		
	//------------------------------------------加载增加/编辑客户表单------------------------------------------------------------------------------------------
	/**
	 * @Title: loadCustomerForm
	 * @Description: 加载客户表单
	 * @param model
	 * @param customerType
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-customer-form")
	public String loadCustomerForm(Model model, Integer customerType, Long customerId) {
		
		return TEMPLATE_PATH + "edit_customer/edit_customer_form";
		
	}
	
	/**
	 * @Title: loadCustomerTable
	 * @Description: 加载增加/编辑客户表单中的table页面
	 * @param model
	 * @param customerType
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-customer-table")
	public String loadCustomerTable(Model model, Integer customerType, Long customerId) {
		
		//加载数据字典-用水性质
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		//查询用水性质
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();
						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		
		if(customerId!=null) {
			//读取需要编辑的条目
			Customers currItem=customersService.selectByPrimaryKey(customerId);
			Example example = new Example(SysWaterPrice.class);
			example.createCriteria().andEqualTo("priceTypeCode", currItem.getWaterUse()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
			List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
			model.addAttribute("currItem",currItem);
			model.addAttribute("waterPriceList",waterPriceList);
		}
		
		//判断客户类型是个人客户还是单位客户
		if(customerType==EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {
			return TEMPLATE_PATH + "edit_customer/edit_person_customer_table";
		}
		
		return TEMPLATE_PATH + "edit_customer/edit_company_customer_table";
		
	}
	
	//-----------------------------------配置分水量-------------------------------------------------------------------------------
	
	/**
	 * @Title: loadSettingPartWaterDialog
	 * @Description: 加载配置分水量对话框
	 * @param model
	 * @param meterId
	 * @return 
	 */
//	@RequestMapping(value = "/load-setting-part-water-dialog")
//	public String loadSettingPartWaterDialog(Model model, Long meterId) {
//		
//		//查询非阶梯水价的所有数据
//		List<SysWaterPrice> waterPriceList = waterPriceService.getNotJmshysPriceList();
//		model.addAttribute("waterPriceList", waterPriceList);
//		
//		MeterPartWater record = new MeterPartWater();
//		record.setMeterId(meterId);
//		List<MeterPartWater> meterPartWaterList = meterPartWaterService.select(record);
//		model.addAttribute("meterPartWaterList", meterPartWaterList);
//		
//		model.addAttribute("meterId", meterId);
//		
//		return TEMPLATE_PATH + "setting_meter_part_water_dialog";
//		
//	}
	
	/**
	 * @Title: saveMeterPartWater
	 * @Description: 保存表计分水量配置
	 * @param model
	 * @param meterId
	 * @param meterPartWaterJson
	 * @return
	 * @throws Exception 
	 */
//	@RequestMapping(value = "/save-meter-part-water", produces = "application/json")
//	@ResponseBody
//	public Object saveMeterPartWater(Model model, Long meterId, String meterPartWaterJson) throws Exception {
//		try {
//			
//			List<MeterPartWater> meterPartWaterList = JSON.parseArray(meterPartWaterJson, MeterPartWater.class);
//			
//			int rows = meterPartWaterService.insert(meterId, meterPartWaterList);
//			
//			if (rows>0) {
//				return RequestResultUtil.getResultSaveSuccess("保存成功！");
//			}
//			return RequestResultUtil.getResultSaveWarn("保存异常，请重新操作！");
//			
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//
//		return RequestResultUtil.getResultSaveWarn("保存异常，请重新操作！");
//
//	}
	
}