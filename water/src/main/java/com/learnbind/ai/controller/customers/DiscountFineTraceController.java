package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.meterrecord.DiscountFineTraceService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersTraceController.java
 * @Description: 客户档案维护日志
 *
 * @author Thinkpad
 * @date 2019年7月7日 上午9:57:29
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/discount-fine-trace")
public class DiscountFineTraceController {
	
	private static Log log = LogFactory.getLog(DiscountFineTraceController.class);
	
	private static final String TEMPLATE_PATH = "customers/discount_fine_trace/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private DiscountFineTraceService traceService;//违约金减免记录
	@Autowired
	private CustomersService customersService;

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "discount_fine_trace_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "discount_fine_trace_main";
	}
	
	/**
	 * @Title: loginoutLogTable
	 * @Description: 登陆登出日志
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table")
	/* @ResponseBody */
	public String loginoutLogTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<DiscountFineTrace> traceList = traceService.search(searchCond);
		PageInfo<List<DiscountFineTrace>> pageInfo = new PageInfo(traceList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(DiscountFineTrace ft : traceList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(ft);
			String customerName = customersService.getCustomerNameById(ft.getCustomerId());
			customerMap.put("customerName", customerName);//业主姓名
			customerMap.put("operationTimeStr", ft.getOperationTimeStr());
			customerMapList.add(customerMap);
		}
		// 传递如下数据至前台页面
		model.addAttribute("traceList", customerMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "discount_fine_trace_table";
	}

}