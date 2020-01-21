package com.learnbind.ai.controller.customers;

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
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.service.customers.BillService;
import com.learnbind.ai.service.customers.CustomersService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: SearchCustomerInvoiceController.java
 * @Description: 查询客户开票信息控制器
 *
 * @author Administrator
 * @date 2019年12月24日 下午05:48:51
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/search-customer-invoice")
public class SearchCustomerInvoiceController {
	
	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(SearchCustomerInvoiceController.class);
	
	/**
	 * @Fields TEMPLATE_PATH：页面目录
	 */
	private static final String TEMPLATE_PATH = "customers/searchinvoice/"; // 页面目录
	/**
	 * @Fields PAGE_SIZE：页大小
	 */
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;  //客户信息
	@Autowired
	private BillService billService;//客户-开票信息
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "search_invoice_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "search_invoice_main";
	}
	
	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param parmsJSON
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, String parmsJSON) {
		
		JSONObject parmsObj = JSON.parseObject(parmsJSON);
		String pageNumStr = parmsObj.getString("pageNum");
		String pageSizeStr = parmsObj.getString("pageSize");
		String searchCond = parmsObj.getString("searchCond");
		Integer billType = parmsObj.getInteger("billType");
		String containerId = parmsObj.getString("containerId");

		int pageNum = 0;
		int pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		if(StringUtils.isNotBlank(pageNumStr)) {
			pageNum = Integer.valueOf(pageNumStr);
		}
		if(StringUtils.isNotBlank(pageSizeStr)) {
			pageSize = Integer.valueOf(pageSizeStr);
		}
		// 判定页码有效性
		if (pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> invoiceMapList = billService.searchCustomerInvoiceList(searchCond, billType);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(invoiceMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for(Map<String, Object> invoiceMap : invoiceMapList) {
			String currCustomerId = invoiceMap.get("CUSTOMER_ID").toString();
			if(StringUtils.isNotBlank(currCustomerId)) {
				int count = billService.getCustomerInvoiceCount(Long.valueOf(currCustomerId));
				invoiceMap.put("invoiceSize", count);
			}
		}
		
		// 传递如下数据至前台页面
		model.addAttribute("invoiceMapList", invoiceMapList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		model.addAttribute("containerId", containerId);
		
		return TEMPLATE_PATH + "search_invoice_table";
	}
	
}