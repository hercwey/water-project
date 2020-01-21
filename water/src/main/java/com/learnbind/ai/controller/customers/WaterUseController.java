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
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.CustomersTrace;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: WaterStatusController.java
 * @Description: 修改用水性质
 *
 * @author Thinkpad
 * @date 2019年5月25日 上午10:51:57
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/water-use")
public class WaterUseController {
	
	//private static Log log = LogFactory.getLog(WaterUseController.class);
	private static final String TEMPLATE_PATH = "customers/water_use/"; // 页面目录角色

	
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
	*	@Title: loadmodiusedialog 
	*	@Description: 加载编辑对话框 
	*	@param @param itemId
	*	@param @param model
	*	@param @return     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/loadmodiusedialog")
	public String loadModiCustomerWaterUseDialog(Long itemId, Model model) {
		List<Map<String, Object>> priceTypeMapList = null;
		priceTypeMapList = waterPriceService.getPriceTypeList();
						
		model.addAttribute("priceTypeMapList", priceTypeMapList);
		//读取需要编辑的条目
		Customers currItem=customersService.selectByPrimaryKey(itemId);
		
		Example example = new Example(SysWaterPrice.class);
		example.createCriteria().andEqualTo("priceTypeCode", currItem.getWaterUse()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<SysWaterPrice> waterPriceList = waterPriceService.selectByExample(example);
		
		model.addAttribute("waterPriceList", waterPriceList);
		model.addAttribute("currItem",currItem);
		return TEMPLATE_PATH + "use_dialog_edit";
	}
	

	/** 
	*	@Title: updatCustomers
	*	@Description: 编辑用水性质
	*	@param @param label
	*	@param @return
	*	@param @throws Exception     
	*	@return String    返回类型 
	*	@throws 
	*/
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateCustomers(Customers customers) throws Exception {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userBean==null) {
			return RequestResultUtil.getResultAddWarn("请重新登录！");
		}
		Customers beforeCustomer = customersService.selectByPrimaryKey(customers.getId());
		
		customersService.updateByPrimaryKeySelective(customers);
		
		//增加客户档案维护日志
		CustomersTrace trace = new CustomersTrace();
		trace.setCustomerId(customers.getId());
		String customerName = customersService.getCustomerNameById(customers.getId());
		trace.setCustomerName(customerName);
		trace.setOperationTime(new Date());
		trace.setOperatorId(userBean.getId());
		trace.setOperationType(EnumOperationType.USER_TYPE_CHANGE.getValue());//修改用户性质
		
		trace.setOperatorName(userBean.getRealname());
		//HTML字符编码
		String changeBefore = StringEscapeUtils.escapeHtml(JSON.toJSONString(beforeCustomer));
		String changeAfter = StringEscapeUtils.escapeHtml(JSON.toJSONString(customers));
		
		trace.setChangeBefore(changeBefore);
		trace.setChangeAfter(changeAfter);
		traceService.insertSelective(trace);
		return RequestResultUtil.getResultUpdateSuccess();
	}

}