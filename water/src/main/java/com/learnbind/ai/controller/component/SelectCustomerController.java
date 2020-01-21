package com.learnbind.ai.controller.component;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: SelectCustomerController.java
 * @Description: 对话框选择客户控制器
 *
 * @author Administrator
 * @date 2019年12月8日 下午5:21:40
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/select-customer")
public class SelectCustomerController {
	private static Log log = LogFactory.getLog(SelectCustomerController.class);
	//private static Log log = LogFactory.getLog(CustomersController.class);
	private static final String TEMPLATE_PATH = "customers/selectcustomer/"; // 页面目录角色

	private static final int PAGE_SIZE = 10;//默认页大小
	
	@Autowired
	private CustomersService customersService;//客户档案
	@Autowired
	private LocationService locationService;//地理位置
	
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
		
		return TEMPLATE_PATH + "customer_main";
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
	public String customersTable(Model model, String parmsJSON) {
		
		JSONObject parmsObj = JSON.parseObject(parmsJSON);
		String pageNumStr = parmsObj.getString("pageNum");
		String pageSizeStr = parmsObj.getString("pageSize");
		String searchCond = parmsObj.getString("searchCond");
		String dialogId = parmsObj.getString("dialogId");

		int pageNum = 0;
		int pageSize = PAGE_SIZE;
		if(StringUtils.isNotBlank(pageNumStr)) {
			pageNum = Integer.valueOf(pageNumStr);
		}
		if(StringUtils.isNotBlank(pageSizeStr)) {
			pageSize = Integer.valueOf(pageSizeStr);
		}
		// 判定页码有效性
		if (pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Customers> customerList = customersService.getCustomersList(searchCond, null, null, null);
		PageInfo<Customers> pageInfo = new PageInfo<>(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(Customers customer : customerList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("waterStatusStr", customer.getWaterStatusStr());//用水状态
			customerMap.put("statusStr", customer.getStatusStr());//客户状态
			
			customerMapList.add(customerMap);
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("customerMapList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		//model.addAttribute("pageNum", pageNum);
		//model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		model.addAttribute("dialogId", dialogId);
		
		return TEMPLATE_PATH + "customer_table";
	}
	
}