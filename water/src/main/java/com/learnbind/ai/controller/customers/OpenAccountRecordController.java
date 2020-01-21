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
import com.learnbind.ai.common.enumclass.EnumCustomerAgreementType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.CustomerAgreement;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerAgreementService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomersController.java
 * @Description: 立户单管理
 *
 * @author Thinkpad
 * @date 2019年5月16日 上午11:42:14
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/open-account-record")
public class OpenAccountRecordController {
	private static Log log = LogFactory.getLog(OpenAccountRecordController.class);
	private static final String TEMPLATE_PATH = "open_account_record/"; // 页面目录角色
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CustomersService customersService;//客户档案
	@Autowired
	private DataDictService dataDictService;//数据字典
	@Autowired
	private CustomerAccountService customerAccountService;//客户-账户服务
	@Autowired
	private CustomerAgreementService customerAgreementService;//客户-协议服务
	
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
		
		return TEMPLATE_PATH + "open_account_starter";
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
		
		return TEMPLATE_PATH + "open_account_main";
	}

	/**
	 * @Title: customersTable
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @param pageNum	页号
	 * @param pageSize	页大小
	 * @param searchCond	查询条件
	 * @return 
	 */
	@RequestMapping(value = "/table")
	public String customersTable(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Customers> customerList = customersService.searchCustomerAgreement(searchCond, EnumCustomerAgreementType.OPEN_ACCOUNT.getValue());
		PageInfo<List<Customers>> pageInfo = new PageInfo(customerList);// (使用了拦截器或是AOP进行查询的再次处理)
		
		List<Map<String, Object>> customerMapList = new ArrayList<>();
		for(Customers customer : customerList) {
			Map<String, Object> customerMap = EntityUtils.entityToMap(customer);
			customerMap.put("waterStatusStr", customer.getWaterStatusStr());//用水状态
			
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
		
		return TEMPLATE_PATH + "open_account_table";
	}

	
	/**
	 * @Title: loadAgreementItem
	 * @Description: 加载客户-协议
	 * @param model
	 * @param functionModule
	 * @param customerId
	 * @return 
	 */
	@RequestMapping(value = "/load-agreement-item")
	public String loadAgreementItem(Model model, Long customerId) {
		
		CustomerAgreement record = new CustomerAgreement();
		record.setCustomerId(customerId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setAgreementType(0);
		List<CustomerAgreement> agreementList = customerAgreementService.select(record);
		
		model.addAttribute("agreementList", agreementList);
		
		return TEMPLATE_PATH + "agreement_item_table";
	}
	

	

	
	

}