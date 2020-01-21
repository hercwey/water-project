package com.learnbind.ai.controller.company;

import java.util.ArrayList;
import java.util.List;

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
import com.learnbind.ai.model.CompanyInvoice;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.service.company.CompanyInvoiceService;
import com.learnbind.ai.service.dict.DataDictService;




/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.customers
 *
 * @Title: CustomerBillInfoController.java
 * @Description: 供排水开票信息
 *
 * @author Thinkpad
 * @date 2019年11月27日 下午2:41:30
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/company-bill-info")
public class CompanyInvoiceController {
	private static Log log = LogFactory.getLog(CompanyInvoiceController.class);
	private static final String TEMPLATE_PATH = "company/"; // 页面目录
	private static final int PAGE_SIZE = 8; // 页大小

	
	@Autowired
	private CompanyInvoiceService companyInvoiceService;  //供排水相关-开票信息
	@Autowired
	private DataDictService dataDictService;//数据字典
	
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		
		return TEMPLATE_PATH + "bill_starter";
	}
	
	/**
	 * @Title: main
	 * @Description: 加载客户银行信息
	 * @param model
	 * @param functionModule
	 * 			功能模块标识：用于页面显示不同功能
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		
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
	public String table(Model model, Integer pageNum, Integer pageSize, String searchCond) {

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<CompanyInvoice> billList = companyInvoiceService.searchCond(searchCond);
		PageInfo<List<CompanyInvoice>> pageInfo = new PageInfo(billList);// (使用了拦截器或是AOP进行查询的再次处理)

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
	 * @Description: 增加开票信息对话框
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadaddbilldialog")
	public String loadAddBillDialog(Model model) {
		
		//查询专用发票额度版本
		List<DataDict> specialAmountVersionList = dataDictService.getListByTypeCode(DataDictType.INVOICE_AMOUNT_VERSION_SPECIAL);
		//查询普通发票额度版本
		List<DataDict> normalAmountVersionList = dataDictService.getListByTypeCode(DataDictType.INVOICE_AMOUNT_VERSION_NORMAL);
		
		model.addAttribute("specialAmountVersionList", specialAmountVersionList);
		model.addAttribute("normalAmountVersionList", normalAmountVersionList);
		
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
	public Object addBill(CompanyInvoice bill) {
		bill.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
		int row = companyInvoiceService.insertSelective(bill);
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
				companyInvoiceService.deleteByPrimaryKey(id);
			}
		}
		catch(Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}
	
	
	/**
	 * @Title: loadModiBillDialog
	 * @Description: 修改开票信息
	 * @param itemId
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/loadmodibilldialog")
	public String loadModiBillDialog(Long itemId, Model model) {
		
		//查询专用发票额度版本
		List<DataDict> specialAmountVersionList = dataDictService.getListByTypeCode(DataDictType.INVOICE_AMOUNT_VERSION_SPECIAL);
		//查询普通发票额度版本
		List<DataDict> normalAmountVersionList = dataDictService.getListByTypeCode(DataDictType.INVOICE_AMOUNT_VERSION_NORMAL);
		
		model.addAttribute("specialAmountVersionList", specialAmountVersionList);
		model.addAttribute("normalAmountVersionList", normalAmountVersionList);
		
		//读取需要编辑的条目
		CompanyInvoice currItem=companyInvoiceService.selectByPrimaryKey(itemId);
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
			CompanyInvoice bill = new CompanyInvoice();
			bill.setId(id);
			bill.setEnabled(enabled);
			companyInvoiceService.updateByPrimaryKeySelective(bill);
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
	public Object updateBank(CompanyInvoice bill) throws Exception {
		int rows = companyInvoiceService.updateByPrimaryKeySelective(bill);
		if (rows > 0) {
			return RequestResultUtil.getResultUpdateSuccess();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	
	


}