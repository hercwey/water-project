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
import com.learnbind.ai.common.enumclass.accountitem.AiSubjectUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.DiscountFineTrace;
import com.learnbind.ai.model.DiscountWaterFeeTrace;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.DiscountWaterFeeTraceService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersTraceController.java
 * @Description: 客户缴费日志
 *
 * @author Thinkpad
 * @date 2019年7月7日 上午9:57:29
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-charge")
public class CustomerRechargeController {
	
	private static Log log = LogFactory.getLog(CustomerRechargeController.class);
	
	private static final String TEMPLATE_PATH = "customers/customer_recharge/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private DiscountWaterFeeTraceService discountWaterFeeTraceService;//水费减免记录
	@Autowired
	private CustomersService customersService;
	@Autowired
	private CustomerAccountItemService customerAccountItemService;

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "customer_recharge_starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
		return TEMPLATE_PATH + "customer_recharge_main";
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerAccountItem> traceList = customerAccountItemService.searchCustomerRecharge(searchCond);
		PageInfo<List<CustomerAccountItem>> pageInfo = new PageInfo(traceList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(CustomerAccountItem ft : traceList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(ft);
			String customerName = customersService.getCustomerNameById(ft.getCustomerId());
			customerMap.put("customerName", customerName);//业主姓名
			customerMap.put("accountDateStr", ft.getAccountDateStr());//时间
			String creditSubject = AiSubjectUtils.getAiSubjectStr(ft.getDebitSubject());
			customerMap.put("creditSubject", creditSubject);//时间
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
		
		return TEMPLATE_PATH + "customer_recharge_table";
	}

}