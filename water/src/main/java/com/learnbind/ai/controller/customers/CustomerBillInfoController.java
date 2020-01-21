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
import com.learnbind.ai.constant.CustomerFunctionModuleConstant;
import com.learnbind.ai.model.CustomerBillInfo;
import com.learnbind.ai.service.customers.BillService;




/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerBillInfoController.java
 * @Description: 客户开票信息
 *
 * @author Thinkpad
 * @date 2019年5月21日 下午5:25:45
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/customer-bill")
public class CustomerBillInfoController {
	private static Log log = LogFactory.getLog(CustomerBillInfoController.class);
	private static final String TEMPLATE_PATH = "customers/bill/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private BillService billService;  //客户-开票信息
	
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
		
		return TEMPLATE_PATH + "bill_main";
	}

	/**
	 * @Title: table
	 * @Description: 开票信息table
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
		List<CustomerBillInfo> billList = billService.searchCustomerBillInfo(customerId, searchCond);
		PageInfo<List<CustomerBillInfo>> pageInfo = new PageInfo(billList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("billList", billList);  //列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		//查询条件回传
		model.addAttribute("searchCond", searchCond);
		
		return TEMPLATE_PATH + "bill_table";
	}
	
	/**
	 * @Title: loadAddBillDialog
	 * @Description: 加载增加客户银行信息对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadaddbilldialog")
	public String loadAddBillDialog(Model model) {
		
		return TEMPLATE_PATH + "bill_dialog_edit";
	}
	
	/**
	 * @Title: addBill
	 * @Description: 增加客户开票信息
	 * @param bank
	 * @return 
	 */
	@RequestMapping(value = "/insert", produces = "application/json")
	@ResponseBody
	public Object addBill(CustomerBillInfo bill) {
		//waterPrice.setCreateTime(new Date());
		bill.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
		int row = billService.insertCustomerBillInfo(bill);
		if (row > 0)
			return RequestResultUtil.getResultAddSuccess();
		else
			return RequestResultUtil.getResultAddWarn();
	}
	
	/**
	 * @Title: deleteBill
	 * @Description: 删除客户开票信息
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object deleteBill(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				billService.deleteCustomerBillInfo(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	
	/**
	 * @Title: loadModiBillDialog
	 * @Description: 修改客户银行信息
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodibilldialog")
	public String loadModiBillDialog(Long itemId, Model model) {
		
		//读取需要编辑的条目
		CustomerBillInfo currItem=billService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem",currItem);
		
		return TEMPLATE_PATH + "bill_dialog_edit";
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
			CustomerBillInfo bill = new CustomerBillInfo();
			bill.setId(id);
			bill.setEnabled(enabled);
			billService.updateByPrimaryKeySelective(bill);
		}
		catch(Exception e) {
			return RequestResultUtil.getResultUpdateWarn();
		}

		return RequestResultUtil.getResultUpdateSuccess();
	}
	

	/**
	 * @Title: updateBill
	 * @Description: 编辑客户银行信息
	 * @param bank
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object updateBank(CustomerBillInfo bill) throws Exception {
		int rows = billService.updateByPrimaryKeySelective(bill);
		if (rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	
	


}