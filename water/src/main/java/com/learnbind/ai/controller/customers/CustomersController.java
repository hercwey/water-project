package com.learnbind.ai.controller.customers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumBindStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDataDictType;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.EnumOperationType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.constant.WaterPriceConstant;
import com.learnbind.ai.jsengine.PartitionRuleUtil;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.service.wechatuser.WechatCustomerService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersController.java
 * @Description: 客户档案
 *
 * @author Thinkpad
 * @date 2019年5月16日 上午11:42:14
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customers")
public class CustomersController {
	private static Log log = LogFactory.getLog(CustomersController.class);
	//private static Log log = LogFactory.getLog(CustomersController.class);
	private static final String TEMPLATE_PATH = "customers/"; // 页面目录角色

	//解决冲突
	@Autowired
	private CustomersService customersService;//客户档案
	@Autowired
	private DataDictService dataDictService;//数据字典
	@Autowired
	private DiscountService discountService;//政策减免
	@Autowired
	private WaterPriceService waterPriceService;//水价
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计服务
	@Autowired
	private CustomersTraceService traceService;//客户档案维护日志
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private MetersService metersService;//表计位置

	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关联表
	@Autowired
	private CustomerAccountItemService customerAccountItemService;//客户账目
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private MeterPartWaterRuleTraceService meterPartWaterRuleTraceService;//分水量规则配置日志表
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;//分水量规则配置表
	@Autowired
	private PartitionRuleUtil partitionRuleUtil;//分水量规则工具类
	@Autowired
	private BankService bankService;//客户银行信息
	@Autowired
	private WechatCustomerService wechatCustomerService;//微信-客户关系
	
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
			
			boolean isWxCustomer = false;
			WechatUser wu = wechatCustomerService.getWechatCustomer(customerId);
			if(wu!=null) {
				isWxCustomer = true;
			}
			customerMap.put("isWxCustomer", isWxCustomer);
			
			customerMapList.add(customerMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("customersList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		//model.addAttribute("pageNum", pageNum);
		//model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "customers_table";
	}
	
	/** 
	*	@Title: loadDetailDialog 
	*	@Description: 加载详情对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loaddetaildialog")
	public String loadDetailDialog(Model model) {
		return TEMPLATE_PATH + "customers_dialog_edit";
	}

	/** 
	*	@Title: loadAddDialog 
	*	@Description: 加载增加对话框 
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadadddialog")
	public String loadAddDialog(Model model) {
		//加载数据字典-用水性质
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		List<Map<String, Object>> priceTypeMapList = null;
		priceTypeMapList = waterPriceService.getPriceTypeList();
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		return TEMPLATE_PATH + "customers_dialog_edit";
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
	public Object addCustomers(Customers customer, String locationCode, Long locationId, String bankName, String cardNo) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录！");
		}
		
		String customerCode = this.getCustomerCode(customer.getCustomerType(), locationCode);//获取不重复的客户编码
//		//验证用户编号是否为空
//		if(StringUtils.isBlank(customer.getCustomerCode())) {
//			return RequestResultUtil.getResultFail("用户编号不允许为空！");
//		}
//		
//		//验证用户编号是否重复
//		Customers searchObj = new Customers();
//		searchObj.setCustomerCode(customer.getCustomerCode());
//		List<Customers> customerList = customersService.select(searchObj);
//		if(customerList!=null && customerList.size()>0) {
//			return RequestResultUtil.getResultFail("用户编号重复，请重新生成用户编号！");
//		}
		customer.setCustomerCode(customerCode);//设置客户编码
		customer.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		int row = customersService.insertCustomers(customer, locationId);
		
		if (row > 0) {
			if(customer.getCustomerType() == EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {//如果添加的是个人用户，则添加银行卡信息
				CustomerBanks bank = new CustomerBanks();
				bank.setCustomerId(customer.getId());
				bank.setBankName(bankName);
				bank.setCardNo(cardNo);
				bank.setAccountName(customer.getCustomerName());
				bank.setAccountBank(bankName);
				bank.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
				bankService.insertCustomerBanks(bank);
			}
			//增加客户档案维护日志
			CustomersTrace trace = new CustomersTrace();
			trace.setCustomerId(customer.getId());
			trace.setCustomerName(customer.getCustomerName());
			trace.setOperationTime(new Date());
			trace.setOperatorId(userBean.getId());
			trace.setOperationType(EnumOperationType.INSERT.getValue());//新增用户档案
			trace.setOperatorName(userBean.getRealname());
			//HTML字符编码
			String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(customer));
			trace.setChangeAfter(changeBefore);
			traceService.insertSelective(trace);
			
			Map<String, Object> retMap = RequestResultUtil.getResultAddSuccess();
			retMap.put("customerId", customer.getId());
			return retMap;
		}
		return RequestResultUtil.getResultAddWarn();
	}
	/**
	 * @Title: getCustomerCode
	 * @Description: 获取不重复的客户编码
	 * @param customerType
	 * @param locationCode
	 * @return 
	 */
	private String getCustomerCode(Integer customerType, String locationCode) {
		String customerCode = "";
		boolean isInsert = true;
		while (isInsert) {
			customerCode = this.getCustomerCode(customerType+locationCode);
			boolean flag = this.isInsert(customerCode);
			if(flag) {
				break;
			}
		}
		log.debug("生成的客户编码是："+customerCode);
		return customerCode;
	}
	/**
	 * @Title: getCustomerCode
	 * @Description: 获取客户编码
	 * @param code
	 * @return 
	 */
	private String getCustomerCode(String code) {
		//生成5位随机数
		String random = getRandom();
		//拼接客户编码（小区编码+5位随机数）
		String customerCode = code+random;
		return customerCode;
	}
	/**
	 * @Title: isInsert
	 * @Description: 是否可以增加客户编号
	 * @param customerCode
	 * @return 
	 */
	private boolean isInsert(String customerCode) {
		
		
		//验证客户编码是否相同，不同时更新，相同时重新生成随机数
		
		Customers searchObj = new Customers();
		searchObj.setCustomerCode(customerCode);
		int count = customersService.selectCount(searchObj);
		if(count>0) {//重新生成随机数
			return false;
		}else {//更新客户编码
			return true;
		}
	}
	/**
	 * @Title: getRandom
	 * @Description: 获取5位随机数
	 * @return 
	 */
	private String getRandom() {
		int random = (int)((Math.random()*9+1)*10000);
		return String.valueOf(random);
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
	*	@Title: loadModiDialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodidialog")
	public String loadModiDialog(Long itemId, Model model) {
		
		//加载数据字典-用水性质
		List<DataDict> tradeTypeList = dataDictService.getListByTypeCode(DataDictType.TRADE_TYPE);
		//调用政策减免配置接口获取减免政策
		List<SysDiscount> discountList = discountService.selectAll();
		//查询用水性质
		List<Map<String, Object>> priceTypeMapList = waterPriceService.getPriceTypeList();
		//查询地理位置-小区
		List<Location> locationBlockList = locationService.getBlockListByPid(null);
		
		model.addAttribute("locationBlockList", locationBlockList);
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		
		if(itemId!=null) {
			//读取需要编辑的条目
			Customers currItem=customersService.selectByPrimaryKey(itemId);
			model.addAttribute("currItem",currItem);
			Example example = new Example(SysWaterPrice.class);
			example.createCriteria().andEqualTo("priceTypeCode", currItem.getWaterUse()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
			List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
			
			String customerCode = currItem.getCustomerCode();
			String locationBlockCode = customerCode.substring(1, 3);
			model.addAttribute("locationBlockCode",locationBlockCode);
			
			Location location = this.getLocationByCustomerId(itemId);//查询地理位置
			String place = "";
			if(location != null) {
				place = locationService.getPlace(location.getTraceIds());//查询地理位置全称
			}
			
			model.addAttribute("location",location);
			model.addAttribute("waterPriceList",waterPriceList);
			
			model.addAttribute("place",place);
		}
		
		
		return TEMPLATE_PATH + "customers_dialog_edit";
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
	public Object updateCustomers(Customers customer, String locationCode, Long prevLocationId, Long locationId, String bankName, String cardNo, Long bankId) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
//		//验证用户编号是否为空
//		if(StringUtils.isBlank(customer.getCustomerCode())) {
//			return RequestResultUtil.getResultFail("用户编号不允许为空！");
//		}
		
		String customerCode = customer.getCustomerType()+locationCode;//客户类型+小区编码

		Customers beforeCustomer = customersService.selectByPrimaryKey(customer.getId());//查询原客户信息
		String beforeCustomerCode = beforeCustomer.getCustomerCode();
		if(beforeCustomerCode.indexOf(customerCode)!=0) {
			log.debug("修改客户档案，重新生成客户编码。。。");
			String tempCustomerCode = this.getCustomerCode(customer.getCustomerType(), locationCode);//获取不重复的客户编码
			customer.setCustomerCode(tempCustomerCode);
		}else {
			log.debug("修改客户档案，不需要重新生成客户编码。。。");
		}
		
//		//验证用户编号是否重复
//		Customers searchObj = new Customers();
//		searchObj.setCustomerCode(customer.getCustomerCode());
//		Example example = new Example(Customers.class);
//		example.createCriteria().andEqualTo("customerCode", customer.getCustomerCode()).andNotEqualTo("id", beforeCustomer.getId());
//		List<Customers> customerList = customersService.selectByExample(example);
//		if(customerList!=null && customerList.size()>0) {
//			return RequestResultUtil.getResultFail("用户编号重复，请重新生成用户编号！");
//		}
		
		Date sysdate = new Date();
		
		int rows = customersService.updateCustomers(customer, prevLocationId, locationId);
		if(rows>0) {
			if(customer.getCustomerType() == EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {//如果添加的是个人用户，则添加或修改银行卡信息
				if(bankId != null) {
					CustomerBanks bankTemp = bankService.selectByPrimaryKey(bankId);
					bankTemp.setBankName(bankName);
					bankTemp.setCardNo(cardNo);
					bankTemp.setAccountName(customer.getCustomerName());
					bankTemp.setAccountBank(bankName);
					bankService.updateByPrimaryKeySelective(bankTemp);
				} else {
					CustomerBanks bank = new CustomerBanks();
					bank.setCustomerId(customer.getId());
					bank.setBankName(bankName);
					bank.setCardNo(cardNo);
					bank.setAccountName(customer.getCustomerName());
					bank.setAccountBank(bankName);
					bank.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
					bankService.insertCustomerBanks(bank);
				}
				
			}
			
			String custCode = customer.getCustomerCode();
			if(StringUtils.isNotBlank(custCode)) {
				wechatCustomerService.updateCustomerCode(customer.getId(), custCode);
			}
			
			//增加客户档案维护日志
			CustomersTrace trace = new CustomersTrace();
			trace.setCustomerId(customer.getId());
			String customerName = customersService.getCustomerNameById(customer.getId());
			trace.setCustomerName(customerName);
			trace.setOperationTime(sysdate);
			trace.setOperatorId(userBean.getId());
			trace.setOperationType(EnumOperationType.UPDATE.getValue());//修改客户档案
			trace.setOperatorName(userBean.getRealname());
			//HTML字符编码
			String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(beforeCustomer));
			String changeAfter = StringEscapeUtils.escapeHtml(JSON.toJSONString(customer));
			trace.setChangeBefore(changeBefore);
			trace.setChangeAfter(changeAfter);
			traceService.insertSelective(trace);
			
			Map<String, Object> retMap = RequestResultUtil.getResultUpdateSuccess();
			retMap.put("customerId", customer.getId());
			return retMap;
			
		}
		
		return RequestResultUtil.getResultUpdateSuccess();
	}

	
	
	//------------------------------	查询欠费金额	------------------------------
	/**
	 * @Title: verifyOwedAmount
	 * @Description: 验证欠费金额
	 * @param model
	 * @param customerId
	 * @return
	 * 		success表示无欠费金额，否则返回欠费金额提示
	 */
	@RequestMapping(value = "/verify-owed-amount", produces = "application/json")
	@ResponseBody
	public Object verifyOwedAmount(Model model, Long customerId) {
		
		try {
			
			//判断客户欠费金额是否大于0，大于0表示有欠费，否则表示无欠费
			BigDecimal zero = new BigDecimal("0");
			BigDecimal owedAmount = customerAccountItemService.getCurrBillOwedAmount(customerId);
			if(BigDecimalUtils.greaterThan(owedAmount, zero)) {
				return RequestResultUtil.getResultFail("该客户欠费金额为【"+owedAmount+"】，请结清所有欠款后再继续！");
			}
			
			return RequestResultUtil.getResultSuccess("该客户无欠费金额，可继续操作！");
			
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultFail("操作异常，请重新操作！");

	}
	
	//------------------------------	过户	------------------------------
	/**
	 * @Title: customerItemMain
	 * @Description: 加载客户列表主页（对话框中显示）
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/customer-item-main")
	public String customerItemMain(Model model) {
		
		//查询小区
		List<Location> locationList = locationService.getBlockListByPid(null);
		model.addAttribute("locationList", locationList);
		
		return TEMPLATE_PATH + "customer_item_main";
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
	@RequestMapping(value = "/customer-item-table")
	public String customerItemTable(Model model, Integer pageNum, Integer pageSize, String searchCond, String traceIds, String customerStatus, Integer customerType) {

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
//					//获取客户-账户的账户状态
//					CustomerAccount account = new CustomerAccount();
//					account.setCustomerId(customerId);
//					account.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
//					//account.setStatus(EnumCustomerAccountStatus.ACCOUNT_OPEN.getValue());
//					List<CustomerAccount> accountList = customerAccountService.select(account);
//					if(accountList==null || accountList.size()<=0) {
//						customerMap.put("accountStatus", EnumCustomerStatus.ACCOUNT_NO.getValue());//未立户
//					}else {
//						customerMap.put("accountStatus", accountList.get(0).getStatus());
//						customerMap.put("accountId", accountList.get(0).getId());
//					}
			
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
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "customer_item_table";
	}
	
	/**
	 * @Title: transferAccount
	 * @Description: 过户
	 * @param model
	 * @param oldCustomerId
	 * @param newCustomerId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/transfer-account", produces = "application/json")
	@ResponseBody
	public Object transferAccount(Model model, Long oldCustomerId, Long newCustomerId) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			
			//判断客户欠费金额是否大于0，大于0表示有欠费，否则表示无欠费
			BigDecimal zero = new BigDecimal("0");
			BigDecimal owedAmount = customerAccountItemService.getCurrBillOwedAmount(oldCustomerId);
			if(BigDecimalUtils.greaterThan(owedAmount, zero)) {
				return RequestResultUtil.getResultSaveSuccess("该客户欠费金额为【"+owedAmount+"】，请结清欠款后再执行过户操作！");
			}
			
			int rows = customersService.transferAccount(oldCustomerId, newCustomerId);
			if(rows>0) {
				
				String customerName = customersService.getCustomerNameById(oldCustomerId);
				//增加客户档案维护日志
				CustomersTrace trace = new CustomersTrace();
				trace.setCustomerId(oldCustomerId);
				trace.setCustomerName(customerName);
				trace.setOperationTime(new Date());
				trace.setOperatorId(userBean.getId());
				trace.setOperatorName(userBean.getRealname());
				trace.setOperationType(EnumOperationType.TRANSFER_ACCOUNT.getValue());//过户
				
				Customers oldCus = customersService.selectByPrimaryKey(oldCustomerId);
				Customers newCus = customersService.selectByPrimaryKey(newCustomerId);
				trace.setChangeBefore(JSON.toJSONString(oldCus));
				trace.setChangeAfter(JSON.toJSONString(newCus));
				
				traceService.insertSelective(trace);
				return RequestResultUtil.getResultSaveSuccess("过户成功！");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return RequestResultUtil.getResultSaveWarn("过户异常，请重新操作！");

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
			
			int rows = 1;
			List<Long> meterIdList = JSON.parseArray(meterIdJSON, Long.class);
			
			//rows = customersService.bindBigMeter(meterIdList, customerId);
			rows = customersService.bindMeter(meterIdList, customerId);
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
			//获取分水量规则
			String partWaterRule = this.getPartWaterRule(meter.getId());
			
			customerMap.put("partWaterRule", partWaterRule);
			customerMap.put("virtualRealStr", meter.getVirtualRealStr());
			customerMap.put("partWaterRule", partWaterRule);
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
	 * @Title: getPartWaterRule
	 * @Description: 通过水表ID获取分水量规则
	 * @param meterId
	 * @return 
	 */
	private String getPartWaterRule(Long meterId) {
		//查询抄表记录使用的分水量规则
		List<MeterPartWaterRule> ruleList = meterPartWaterRuleService.getPWaterRuleTraceByMeterId(meterId);
		String partWaterRule = "";
		if(ruleList.size() <= 0) {
			return partWaterRule; 
		}
		for(MeterPartWaterRule trace : ruleList) {
			String currRule = trace.getRuleReal();
			if(StringUtils.isBlank(currRule)) {
				continue;
			}
			log.debug("----------表计ID:"+meterId+"，表计规则:"+currRule);
			currRule = partitionRuleUtil.getRealExpression(currRule);//去规则中的分隔符号，返回表达式
			List<String> formalParamList = partitionRuleUtil.getRuleFormalParams(currRule);//获取表达式中的形参
			Map<String, String> actualParamMap = this.getActualParamMap(formalParamList);//获取实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
			String showExpression = partitionRuleUtil.setRuleActualParams(actualParamMap, currRule);//实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
			if(StringUtils.isNotBlank(showExpression)) {
				//String html = "<p>"+currRule+"</p>";
				SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(trace.getWaterPriceId());//水价
				
				String html = "("+showExpression+")"+"*"+waterPrice.getLadderName()+"("+waterPrice.getTotalPrice()+")"+"<br>";
				partWaterRule = partWaterRule+html;
			}
			
		}
		return partWaterRule;
	}
	
	/**
	 * @Title: getActualParamMap
	 * @Description: 获取实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
	 * @param formalParamList
	 * @return 
	 */
	private Map<String, String> getActualParamMap(List<String> formalParamList){
		Map<String, String> actualParamMap = new HashMap<>();
		for(String meterParam : formalParamList) {
			String meterId = partitionRuleUtil.getMeterIndentify(meterParam);
			if(StringUtils.isNotBlank(meterId)) {
				Meters meter = metersService.selectByPrimaryKey(Long.valueOf(meterId));
				//String meterNo = meter.getMeterNo();//表计编号
				//默认显示表计描述，如果表计描述为空时显示表计安装位置，如果表计安装位置也为空时显示表计编号
				String meterDesc = meter.getDescription();//表计描述
				if(StringUtils.isBlank(meterDesc)) {
					String place = meter.getPlace();//表计安装位置
					if(StringUtils.isNotBlank(place)) {
						meterDesc = place;
					}else {
						meterDesc = meter.getMeterNo();//表计编号
					}
				}
				actualParamMap.put(meterParam, meterDesc);
			}
		}
		return actualParamMap;
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
			
			int rows = 1;
			List<Long> meterIdList = JSON.parseArray(meterIdJSON, Long.class);
			
			rows = customersService.unbindMeter(meterIdList, customerId);
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
		List<DataDict> bankList = dataDictService.getListByTypeCode(DataDictType.BANK);	
		model.addAttribute("bankList", bankList);				
		//查询地理位置-小区
		List<Location> locationBlockList = locationService.getBlockListByPid(null);
		
		model.addAttribute("locationBlockList", locationBlockList);						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		model.addAttribute("tradeTypeList", tradeTypeList);
		model.addAttribute("discountList", discountList);
		
		if(customerId!=null) {
			//读取需要编辑的条目
			Customers currItem=customersService.selectByPrimaryKey(customerId);
			
			String customerCode = currItem.getCustomerCode();//客户编号：客户类型（1位）+小区编码（3位）+5位随机数
			String locationBlockCode = customerCode.substring(1, 4);
			model.addAttribute("locationBlockCode",locationBlockCode);
			
			Example example = new Example(SysWaterPrice.class);
			example.createCriteria().andEqualTo("priceTypeCode", currItem.getWaterUse()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
			List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
			model.addAttribute("currItem",currItem);
			model.addAttribute("waterPriceList",waterPriceList);
			
			CustomerBanks bank = bankService.getCustomerBank(customerId);
			model.addAttribute("bank",bank);
		}
		
		//判断客户类型是个人客户还是单位客户
		if(customerType==EnumCustomerType.CUSTOMER_PEOPLE.getValue()) {
			return TEMPLATE_PATH + "edit_customer/edit_person_customer_table";
		}
		
		return TEMPLATE_PATH + "edit_customer/edit_company_customer_table";
		
	}
	
	//------------------------------	导出客户数据Excel	------------------------------
	/**
	 * @Title: exportMeterRecordExcel
	 * @Description: 导出抄表记录Excel
	 * @param request
	 * @param response 
	 */
	@RequestMapping("/export-customers-excel")
	public void exportCustomersExcel(HttpServletRequest request, HttpServletResponse response, String searchCond, String traceIds, String customerStatus, Integer customerType) {
		
		String period = DateUtils.getYearMonth();//期间默认本月
		//excel标题
		String[] titles = {"客户ID", "门牌号", "客户编号", "客户名称", "客户电话", "客户手机", "地址", "水价分类", "水价", "代扣方式", "行业性质", "减免政策", "银行名称", "账户名称", "银行卡号"};
		//excel列名
		String[] columnNames = {"id", "room", "customerNo", "customerName", "customerTel", "customerMobile", "address", "waterUseValue", "priceCodeValue", "deductTypeValue", "industryValue", "discountTypeValue", "bankName", "accountName", "bankNo"};
		//sheet名
		String sheetName = period+"客户数据";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(searchCond, traceIds, customerStatus, customerType);
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
	private List<Map<String, Object>> getExportExcelData(String searchCond, String traceIds, String customerStatus, Integer customerType){
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
		List<Customers>	customerList = customersService.getCustomersList(searchCond, traceIds, statusList, customerType);
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(Customers customer : customerList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			String waterUse = "";
			if(customer.getWaterUse() != null) {
				waterUse = this.getDataDictValue(EnumDataDictType.WATER_USE.getCode(), customer.getWaterUse());
			}
			customerMap.put("waterUseValue", waterUse);
			
			String JMStr = WaterPriceConstant.JMSHYS;
			String waterPriceStr = "";
			if(StringUtils.equals(customer.getWaterUse(), JMStr)) {
				waterPriceStr="阶梯水价";
				customerMap.put("priceCodeValue", waterPriceStr);
			} else {
				if(customer.getPriceCode() != null) {
					SysWaterPrice waterPrice = waterPriceService.getWaterPriceByPriceCode(customer.getPriceCode());
					waterPriceStr = waterPrice.getLadderName();
				}
				
				customerMap.put("priceCodeValue", waterPriceStr);
			}
			
			
			String deductType = "";
			if(customer.getDeductType() != null) {
				deductType = EnumDeductType.getName(customer.getDeductType());
			}
			customerMap.put("deductTypeValue", deductType);
			
			String industry = "";
			if(customer.getIndustry() != null) {
				industry = this.getDataDictValue(EnumDataDictType.CUSTOMER_TRADE_TYPE.getCode(), customer.getIndustry());
			}
			customerMap.put("industryValue", industry);
			
			String discountName = "";
			if(customer.getDiscountType() != null && customer.getDiscountType() != 0) {
				SysDiscount discount = discountService.selectByPrimaryKey(customer.getDiscountType());
				discountName = discount.getName();
			}
			customerMap.put("discountTypeValue", discountName);
			CustomerBanks bank = bankService.getCustomerBank(customer.getId());
			String bankName = "";
			String accountName = "";
			String bankNo = "";
			if(bank != null) {
				bankName = bank.getBankName();
				accountName = bank.getAccountName();
				bankNo = bank.getCardNo();
			}
			customerMap.put("bankName", bankName);
			customerMap.put("accountName", accountName);
			customerMap.put("bankNo", bankNo);
			customerMapList.add(customerMap);
		}
		
		return customerMapList;
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
		
		fileName = fileName+"-"+times+"-"+"客户数据"+".xls";
		
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
	
	/**
	 * @Title: editDeductType
	 * @Description: 修改客户代扣方式
	 * @param model
	 * @param searchCond
	 * @param traceIds
	 * @param customerStatus
	 * @param customerType
	 * @return
	 * @throws Exception 
	 */
//	@RequestMapping(value = "/edit-deduct-type", produces = "application/json")
//	@ResponseBody
//	public Object editDeductType(Model model, String searchCond, String traceIds, String customerStatus, Integer customerType) throws Exception {
//		int rows= -1;
//		//获取客户状态集合
//		List<Integer> statusList = new ArrayList<>();
//		if(StringUtils.isNotBlank(customerStatus)) {
//			String[] statusArr = customerStatus.split(",");
//			for(String status : statusArr) {
//				if(StringUtils.isNotBlank(status)) {
//					statusList.add(Integer.valueOf(status));
//				}
//			}
//		}
//		
//		List<Customers> customerList = new ArrayList<>();
//		if(StringUtils.isNotBlank(traceIds)) {//如果地理位置痕迹ID（ID-ID-ID-ID）不为空时查询
//			customerList = customersService.getCustomersList(searchCond, traceIds, statusList, customerType);
//		} else {
//			customerList = customersService.searchCustomers(searchCond, statusList, customerType);
//		}
//		for(Customers customer : customerList) {
//			customer.setDeductType(EnumDeductType.CCB.getValue());
//			rows = customersService.updateByPrimaryKeySelective(customer);
//		}
//		if(rows >0) {
//			return RequestResultUtil.getResultSaveWarn("修改代扣方式成功！");
//		}
//		return RequestResultUtil.getResultSaveWarn("修改代扣方式失败！");
//
//	}
	
}