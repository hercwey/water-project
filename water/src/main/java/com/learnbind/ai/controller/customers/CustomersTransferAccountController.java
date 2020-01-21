package com.learnbind.ai.controller.customers;

import java.math.BigDecimal;
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
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumOperationType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersTransferAccountController.java
 * @Description: 客户档案-过户
 *
 * @author Thinkpad
 * @date 2019年9月15日 下午4:51:35
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customers-transfer-account")
public class CustomersTransferAccountController {
	
	//private static Log log = LogFactory.getLog(CustomersTransferAccountController.class);
	private static final String TEMPLATE_PATH = "customers/transfer_account/"; // 页面目录角色

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
	private CustomerAccountItemService customerAccountItemService;//客户账目
	
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
	
}