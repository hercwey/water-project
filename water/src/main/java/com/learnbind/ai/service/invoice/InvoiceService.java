package com.learnbind.ai.service.invoice;

import java.util.List;

import com.learnbind.ai.model.Invoice;
import com.learnbind.ai.model.KnowLibrary;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.invoice
 *
 * @Title: InvoiceService.java
 * @Description: 单据管理
 *
 * @author Thinkpad
 * @date 2019年9月1日 上午9:33:59
 * @version V1.0 
 *
 */
public interface InvoiceService extends IBaseService<Invoice, Long> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<Invoice> searchInvoice(String searchCond, Integer invoiceType, String period);
	
}
