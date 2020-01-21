package com.learnbind.ai.controller.customers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;



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
@RequestMapping(value = "/customer-bank")
public class CustomerBanksController {
	private static Log log = LogFactory.getLog(CustomerBanksController.class);
	private static final String TEMPLATE_PATH = "customers/bank/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private BankService bankService;  //客户-银行信息
	@Autowired
	private CustomersService customersService;  //客户信息
	@Autowired
	private DataDictService dataDictService;//数据字典
	

	/**
	 * @Title: main
	 * @Description: 加载客户银行信息
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
		
		return TEMPLATE_PATH + "bank_main";
	}

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
	public String table(Model model, String functionModule, Integer pageNum, Integer pageSize, Long customerId, String searchCond) {

		if(StringUtils.isBlank(functionModule)) {
			functionModule = CustomerFunctionModuleConstant.FUNCTION_MODULE_MANAGE;
		}
		model.addAttribute(CustomerFunctionModuleConstant.FUNCTION_MODULE, functionModule);
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CustomerBanks> bankList = bankService.searchCustomerBanks(customerId, searchCond);
		PageInfo<List<CustomerBanks>> pageInfo = new PageInfo(bankList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("bankList", bankList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "bank_table";
	}
	
	/**
	 * @Title: loadAddBankDialog
	 * @Description: 加载增加客户银行信息对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadaddbankdialog")
	public String loadAddBankDialog(Model model, Long customerId) {
		Customers customer = customersService.selectByPrimaryKey(customerId);
		List<DataDict> bankList = dataDictService.getListByTypeCode(DataDictType.BANK);
		model.addAttribute("accountName", customer.getCustomerName());
		model.addAttribute("bankList", bankList);
		return TEMPLATE_PATH + "bank_dialog_edit";
	}
	
	/**
	 * @Title: addBank
	 * @Description: 增加客户银行信息
	 * @param bank
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addBank(CustomerBanks bank) {
		//waterPrice.setCreateTime(new Date());
		bank.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
		int row = bankService.insertCustomerBanks(bank);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: deleteBanks
	 * @Description: 删除客户银行信息
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteBanks(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				bankService.deleteCustomerBanks(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	
	/**
	 * @Title: loadModiBankDialog
	 * @Description: 修改客户银行信息
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodibankdialog")
	public String loadModiBankDialog(Long itemId, Model model) {
		List<DataDict> bankList = dataDictService.getListByTypeCode(DataDictType.BANK);
		model.addAttribute("bankList", bankList);
		//读取需要编辑的条目
		CustomerBanks currBankItem=bankService.selectByPrimaryKey(itemId);
		model.addAttribute("currBankItem",currBankItem);
		model.addAttribute("accountName",currBankItem.getAccountName());
		return TEMPLATE_PATH + "bank_dialog_edit";
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
			CustomerBanks bank = new CustomerBanks();
			bank.setId(id);
			bank.setEnabled(enabled);
			bankService.updateByPrimaryKeySelective(bank);
		}
		catch(Exception e) {
			return RequestResultUtil.getResultUpdateWarn();
		}

		return RequestResultUtil.getResultUpdateSuccess();

	}
	

	/**
	 * @Title: updateBank
	 * @Description: 编辑客户银行信息
	 * @param bank
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateBank(CustomerBanks bank) throws Exception {
		int rows = bankService.updateByPrimaryKeySelective(bank);
		if (rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	
	


}