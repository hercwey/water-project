package com.learnbind.ai.controller.component;

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
import com.learnbind.ai.service.customers.BillService;
import com.learnbind.ai.service.customers.CustomersService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: SelectCustomerInvoiceController.java
 * @Description: 开票选择客户发票
 *
 * @author Administrator
 * @date 2019年11月27日 下午4:10:10
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/select-customer-invoice")
public class SelectCustomerInvoiceController {
	
	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(SelectCustomerInvoiceController.class);
	
	/**
	 * @Fields TEMPLATE_PATH：页面目录
	 */
	private static final String TEMPLATE_PATH = "customers/invoice/"; // 页面目录
	/**
	 * @Fields PAGE_SIZE：页大小
	 */
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;  //客户信息
	@Autowired
	private BillService billService;//客户-开票信息
	
	/**
	 * @Title: table
	 * @Description: 银行客户信息table
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
	public String table(Model model, String parmsJSON) {
		
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
		List<Map<String, Object>> invoiceMapList = billService.getCustomerInvoiceList(searchCond);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo(invoiceMapList);// (使用了拦截器或是AOP进行查询的再次处理)

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
		model.addAttribute("dialogId", dialogId);
		
		return TEMPLATE_PATH + "customer_invoice_table";
	}
	
}