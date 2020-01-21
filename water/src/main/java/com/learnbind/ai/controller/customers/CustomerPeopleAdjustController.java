package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumBindStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.CustomerPeopleAdjust;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomerPeopleAdjustService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.waterprice.WaterPriceService;




/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerPeopleController.java
 * @Description: 多人口调整记录
 *
 * @author Thinkpad
 * @date 2019年5月21日 下午7:40:46
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-people")
public class CustomerPeopleAdjustController {
	
	//private static Log log = LogFactory.getLog(CustomerPeopleAdjustController.class);
	private static final String TEMPLATE_PATH = "customers/people/"; // 页面目录

	
	@Autowired
	private CustomerPeopleAdjustService customerPeopleAdjsutService;  //多人口调整记录
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
	private LocationService locationService;//地理位置
	
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
	@RequestMapping(value = "/customer-main")
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
	 * @Description: 客户列表
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @param pageNum	页号
	 * @param pageSize	页大小
	 * @param searchCond	查询条件
	 * @param customerStatus	客户状态（可以有多个状态）
	 * @return 
	 */
	@RequestMapping(value = "/customer-table")
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
	 * @Title: main
	 * @Description: 加载多人口调整信息
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model, String functionModule) {
		
		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		return TEMPLATE_PATH + "people_main";
	}

	/**
	 * @Title: table
	 * @Description: 多人口调整信息table
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @param pageNum
	 * @param pageSize
	 * @param customerId
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, String functionModule, Integer pageNum, Integer pageSize, Long customerId, String searchCond) {

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
		List<CustomerPeopleAdjust> peopleList = customerPeopleAdjsutService.searchCustomerPeopleAdjust(customerId, searchCond);
		PageInfo<CustomerPeopleAdjust> pageInfo = new PageInfo<>(peopleList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("peopleList", peopleList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "people_table";
	}
	
	/**
	 * @Title: loadAddpeopleDialog
	 * @Description: 加载增加多人口调整信息对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadaddpeopledialog")
	public String loadAddPeopleDialog(Model model) {
		
		return TEMPLATE_PATH + "people_dialog_edit";
	}
	
	/**
	 * @Title: addpeople
	 * @Description: 增加多人口调整信息
	 * @param bank
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addPeople(CustomerPeopleAdjust people) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}
		
		people.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
		people.setOperatorName(userBean.getRealname());
		people.setOperatorId(userBean.getId());
		people.setCreateTime(new Date());
		int row = customerPeopleAdjsutService.insertCustomerPeopleAdjust(people);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: deletepeople
	 * @Description: 删除多人口调整信息
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deletePeople(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				customerPeopleAdjsutService.deleteCustomerPeopleAdjust(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	
	/**
	 * @Title: loadModiPeopleDialog
	 * @Description: 修改多人口调整
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodipeopledialog")
	public String loadModiPeopleDialog(Long itemId, Model model) {
		
		//读取需要编辑的条目
		CustomerPeopleAdjust currItem=customerPeopleAdjsutService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "people_dialog_edit";
	}
	
	/**
	 * @Title: updateStatus
	 * @Description: 修改启用禁用状态
	 * @param id
	 * @param enabled
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update-status", produces = "application/json")
	@ResponseBody
	public Object updateStatus(Long id ,Integer enabled) throws Exception {
		try {
			CustomerPeopleAdjust people = new CustomerPeopleAdjust();
			people.setId(id);
			people.setEnabled(enabled);
			customerPeopleAdjsutService.updateByPrimaryKeySelective(people);
		}
		catch(Exception e) {
			return RequestResultUtil.getResultUpdateWarn();
		}

		return RequestResultUtil.getResultUpdateSuccess();

	}
	

	/**
	 * @Title: updatePeople
	 * @Description: 编辑多人口调整
	 * @param bank
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateBank(CustomerPeopleAdjust people) throws Exception {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录");
		}
		
		people.setOperatorName(userBean.getRealname());
		people.setOperatorId(userBean.getId());
		people.setCreateTime(new Date());
		
		customerPeopleAdjsutService.updateByPrimaryKeySelective(people);
		return RequestResultUtil.getResultUpdateSuccess();
	}
	
	


}