package com.learnbind.ai.controller.invoice;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.tax.EnumTaxChbz;
import com.learnbind.ai.common.enumclass.tax.EnumTaxFpzl;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.model.CompanyInvoice;
import com.learnbind.ai.model.TaxInvoice;
import com.learnbind.ai.model.TaxRedInfo;
import com.learnbind.ai.service.company.CompanyInvoiceService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.tax.TaxInvoiceService;
import com.learnbind.ai.service.tax.TaxRedInfoService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.invoice
 *
 * @Title: TaxInvoiceRedController.java
 * @Description: 发票冲红
 *
 * @author Thinkpad
 * @date 2019年12月21日 下午2:53:24
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/tax-invoice-red")
public class TaxInvoiceRedController {
	private static Log log = LogFactory.getLog(TaxInvoiceRedController.class);
	private static final String TEMPLATE_PATH = "tax_invoice/"; // 页面目录
	
	@Autowired
	private TaxInvoiceService taxInvoiceService;//发票管理
	@Autowired
	private CustomersService customersService;//客户信息
	@Autowired
	private CompanyInvoiceService companyInvoiceService;//水司开票信息
	@Autowired
	private TaxRedInfoService taxRedInfoService;//红字信息
	

	
	/**
	 * @Title: loadTaxInvocieRedMain
	 * @Description: 专用发票冲红main
	 * @param model
	 * @param dialogId
	 * @return 
	 */
	@RequestMapping(value = "/load-tax-red-main")
	public String loadTaxInvocieRedMain(Model model, String dialogId) {
		
		model.addAttribute("dialogId", dialogId);
		
		return TEMPLATE_PATH + "tax_invoice_red_main";
	}

	/**
	 * @Title: loadTaxInvocieRedTable
	 * @Description: 专用发票冲红
	 * @param model
	 * @param taxInvoiceId
	 * @return 
	 */
	@RequestMapping(value = "/load-tax-red-table")
	public String loadTaxInvocieRedTable(Model model, Long taxInvoiceId) {
		Date sysDate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String tkrq = sf.format(sysDate);
		BigDecimal slv = new BigDecimal("0.03");
		TaxInvoice currItem = taxInvoiceService.selectByPrimaryKey(taxInvoiceId);
		model.addAttribute("currItem", currItem);
		
		List<Map<String, String>> sqfxxList = this.getSqfxxList(currItem);
		model.addAttribute("sqfxxList", sqfxxList);
		
		model.addAttribute("tkrq", tkrq);
		model.addAttribute("slv", slv);
		//返回表单页面
		return TEMPLATE_PATH + "tax_invoice_red_table";
	}
	
	/**
	 * @Title: getSqfxxList
	 * @Description: 获取申请方信息列表，水司信息和客户信息
	 * @param invoiceId
	 * @return 
	 */
	private List<Map<String, String>> getSqfxxList(TaxInvoice invoice){
		CompanyInvoice company = companyInvoiceService.getDefaultCompanyInvoice();
		//TaxInvoice invoice = taxInvoiceService.selectByPrimaryKey(invoiceId);
		
		Map<String, String> companyInfo = new HashMap<>();
		companyInfo.put("invoiceName", company.getCompanyName());
		companyInfo.put("taxNo", company.getTaxNo());
		
		Map<String, String> customerInfo = new HashMap<>();
		customerInfo.put("invoiceName", invoice.getGfmc());
		customerInfo.put("taxNo", invoice.getGfsh());
		
		List<Map<String, String>> sqfxxList = new ArrayList<>();
		sqfxxList.add(customerInfo);
		sqfxxList.add(companyInfo);
		
		return sqfxxList;
		
	}
	
	/**
	 * @Title: getCompany
	 * @Description: 获取水司开票信息
	 * @return 
	 */
	@RequestMapping(value = "/get-company-invoice")
	@ResponseBody
	public Object getCompany() {
		CompanyInvoice company = companyInvoiceService.getDefaultCompanyInvoice();
		Map<String, Object> companyMap = RequestResultUtil.getResultSuccess("获取水司开票信息成功");
		companyMap.put("company", company);
		return companyMap;
	}
	
	/**
	 * @Title: saveInvoiceRedMessage
	 * @Description: 专用发票保存
	 * @param info
	 * @return 
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public Object saveInvoiceRedMessage(TaxRedInfo info) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		info.setCzrq(new Date());
		info.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		info.setSlv(new BigDecimal("0.03"));
		info.setCzr(userBean.getRealname());
		info.setXxblx("0");
		info.setSzlb(EnumTaxFpzl.ZYFP.getKey());
		int rows = taxRedInfoService.insertSelective(info);
		if(rows > 0) {
			//修改发票状态为冲红
			TaxInvoice invoice = taxInvoiceService.getTaxMessage(info.getDylpdm(), info.getDylphm());
			invoice.setChbz(EnumTaxChbz.YCH.getValue());
			invoice.setChr(userBean.getRealname());
			invoice.setChrq(new Date());
			taxInvoiceService.updateByPrimaryKeySelective(invoice);
			return RequestResultUtil.getResultSuccess("发票冲红成功");
		}
		return RequestResultUtil.getResultSuccess("发票冲红失败");
	}
	

	
	//************************************普通发票冲红************************
	@RequestMapping(value = "/load-general-tax-red-main")
	public String loadTaxGeneralInvocieRedMain(Model model, String dialogId) {
		
		model.addAttribute("dialogId", dialogId);
		
		return TEMPLATE_PATH + "tax_general_invoice_red_main";
	}
	
	/**
	 * @Title: loadTaxGeneralInvocieRedTable
	 * @Description: 普通发票冲红
	 * @param model
	 * @param taxInvoiceId
	 * @return 
	 */
	@RequestMapping(value = "/load-tax-general-red-table")
	public String loadTaxGeneralInvocieRedTable(Model model, Long taxInvoiceId) {
		Date sysDate = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String tkrq = sf.format(sysDate);
		BigDecimal slv = new BigDecimal("0.03");
		TaxInvoice currItem = taxInvoiceService.selectByPrimaryKey(taxInvoiceId);
		model.addAttribute("currItem", currItem);
		
		model.addAttribute("tkrq", tkrq);
		//返回表单页面
		return TEMPLATE_PATH + "tax_general_invoice_red_table";
	}
	
	/**
	 * @Title: saveGeneralInvoiceRedMessage
	 * @Description: 普通发票冲红保存
	 * @param info
	 * @return 
	 */
	@RequestMapping(value = "/save-general")
	@ResponseBody
	public Object saveGeneralInvoiceRedMessage(TaxRedInfo info) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		info.setCzrq(new Date());
		info.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		info.setCzr(userBean.getRealname());
		info.setSzlb(EnumTaxFpzl.PTFP.getKey());
		int rows = taxRedInfoService.insertSelective(info);
		if(rows > 0) {
			//修改发票状态为冲红
			TaxInvoice invoice = taxInvoiceService.getTaxMessage(info.getDylpdm(), info.getDylphm());
			invoice.setChbz(EnumTaxChbz.YCH.getValue());
			invoice.setChr(userBean.getRealname());
			invoice.setChrq(new Date());
			taxInvoiceService.updateByPrimaryKeySelective(invoice);
			return RequestResultUtil.getResultSuccess("发票冲红成功");
		}
		return RequestResultUtil.getResultSuccess("发票冲红失败");
	}

	/**
	 * @Title: invoiceRedIfoDetail
	 * @Description: 根据发票号码和发票代码获取红字信息
	 * @param model
	 * @param fphm
	 * @param fpdm
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/tax-red-info")
	public String invoiceRedIfoDetail(Model model, String fphm, String fpdm) throws ParseException {
		
		TaxRedInfo redInfo = taxRedInfoService.getTaxRedMessage(fpdm, fphm);
		Map<String, Object> redMap = EntityUtils.entityToMap(redInfo);
		redMap.put("tkrqStr", redInfo.getTkrqStr());
		redMap.put("czrqStr", redInfo.getCzrqStr());
		//查询条件回传
		model.addAttribute("textData", redMap);
		
		return TEMPLATE_PATH+"preview_invoice_red_info";
	}
	
	

}