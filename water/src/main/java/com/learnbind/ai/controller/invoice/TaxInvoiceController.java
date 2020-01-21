package com.learnbind.ai.controller.invoice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.TaxInvoiceBean;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.tax.EnumTaxFpzl;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.PagerConstant;
import com.learnbind.ai.model.CompanyInvoice;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.TaxInvoice;
import com.learnbind.ai.model.TaxInvoiceDetail;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.tax.TaxInvoiceDetailService;
import com.learnbind.ai.service.tax.TaxInvoiceService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.controller.invoice
 *
 * @Title: TaxInvoiceController.java
 * @Description: 发票查询
 *
 * @author Thinkpad
 * @date 2019年12月19日 下午2:34:05
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/tax-invoice")
public class TaxInvoiceController {
	private static Log log = LogFactory.getLog(TaxInvoiceController.class);
	private static final String TEMPLATE_PATH = "tax_invoice/"; // 页面目录
	
	@Autowired
	private TaxInvoiceService taxInvoiceService;//发票管理
	@Autowired
	private TaxInvoiceDetailService taxInvoiceDetailService;//发票详情信息
	@Autowired
	private CustomersService customersService;//客户信息
	

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "invoice_starter";
	}

	
	/**
	 * @Title: getPreRemark
	 * @Description: 获取往期发票的备注信息
	 * @param customerid
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/getPreRemark")
	@ResponseBody
	public Object getPreRemark(String customerId, Model model) {
		//获取往期最后一条发票信息
		TaxInvoice tax = taxInvoiceService.getPreTaxInvocie(Long.valueOf(customerId));
		String remark="";
		if(StringUtils.isNotBlank(tax.getBz())) {
			remark = tax.getBz();
		}
		Map<String,Object> respMap = RequestResultUtil.getResultAddSuccess(remark);
		
		return respMap;
		
		
		
	}
	
	
	
	
	
	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "invoice_main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return 
	 */
	@RequestMapping(value = "/table", produces = "application/json")
	public String table(Model model, Integer pageNum, Integer pageSize, TaxInvoiceBean bean) {
		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		if(StringUtils.isNotBlank(bean.getMonth())) {
			bean.setMonth(bean.getYear()+"-"+bean.getMonth());
		}
		List<TaxInvoice> invoiceList = taxInvoiceService.getTaxInvocie(bean);
		PageInfo<TaxInvoice> pageInfo = new PageInfo<>(invoiceList);// (使用了拦截器或是AOP进行查询的再次处理)
		List<Map<String, Object>> invoiceMapList = new ArrayList<>();
		for(TaxInvoice invoice : invoiceList) {
			Map<String, Object> map = EntityUtils.entityToMap(invoice);
			String customerName = "";
			if(invoice.getCustomerId() != null) {
				Customers customer = customersService.selectByPrimaryKey(invoice.getCustomerId());
				customerName = customer.getCustomerName();
			} else {
				customerName = invoice.getGfmc();
			}
			
			map.put("customerName", customerName);
			map.put("fpzlValue", EnumTaxFpzl.getValue(invoice.getFpzl()));//发票种类
			map.put("kprqStr", invoice.getKprqStr());//开票日期
			map.put("zfrqStr", invoice.getZfrqStr());//作废日期
			map.put("dyrqStr", invoice.getDyrqStr());//打印日期
			map.put("chrqStr", invoice.getChrqStr());//冲红日期
			
			invoiceMapList.add(map);
		}
		// 传递如下数据至前台页面
		model.addAttribute("invoiceList", invoiceMapList);//列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return TEMPLATE_PATH + "invoice_table";
	}
	
	
	
	
	/**
	 * @Title: taxInvoiceDetail
	 * @Description: 根据发票信息ID获取发票信息及明细
	 * @param invoiceId
	 * @return 
	 */
	@RequestMapping(value = "/invoice/query")
	@ResponseBody
	public String taxInvoiceDetail(Long invoiceId) {
		List<TaxInvoiceDetail> detailList = taxInvoiceDetailService.getInvoiceDetail(invoiceId);
		TaxInvoice invoice = taxInvoiceService.selectByPrimaryKey(invoiceId);
		Map<String, Object> map = new HashMap<>();
		map.put("taxInvoice", invoice);
		map.put("invoiceDetailList", detailList);
		return JSON.toJSONString(map);
	}

}