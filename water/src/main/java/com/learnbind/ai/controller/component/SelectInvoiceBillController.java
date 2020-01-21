package com.learnbind.ai.controller.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.component
 *
 * @Title: SelectInvoiceBillController.java
 * @Description: 选择开票账单控制器
 *
 * @author Administrator
 * @date 2019年12月11日 下午11:29:55
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/select-invoice-bill")
public class SelectInvoiceBillController {
	
	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(SelectInvoiceBillController.class);
	
	/**
	 * @Fields TEMPLATE_PATH：页面目录
	 */
	private static final String TEMPLATE_PATH = "customers/invoice/"; // 页面目录
	
	//选择账单类型，1：按通知单选择；2：按客户账单选择
	private static final int INVOICE_BILL_TYPE_NOTIFY = 1;//选择账单类型，1：按通知单选择
	private static final int INVOICE_BILL_TYPE_BILL = 2;//选择账单类型，2：按客户账单选择
	
	/**
	 * @Title: table
	 * @Description: 加载选择开票账单页面
	 * @param model
	 * @param type
	 * @return 
	 */
	@RequestMapping(value = "/load-page")
	public String table(Model model, Integer type, String containerId) {
		
		model.addAttribute("containerId", containerId);
		
		if(type==INVOICE_BILL_TYPE_BILL) {//按客户账单选择
			return TEMPLATE_PATH + "bill/tabs";
		}
		//按通知单选择
		return TEMPLATE_PATH + "notify/customer_notify_main";
	}
	
}