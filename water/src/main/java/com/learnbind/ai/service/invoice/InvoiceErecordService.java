package com.learnbind.ai.service.invoice;

import java.util.List;

import com.learnbind.ai.model.Invoice;
import com.learnbind.ai.model.InvoiceErecord;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.invoice
 *
 * @Title: InvoiceErecordService.java
 * @Description: 发票管理-文件
 *
 * @author Thinkpad
 * @date 2019年9月1日 上午10:25:09
 * @version V1.0 
 *
 */
public interface InvoiceErecordService extends IBaseService<InvoiceErecord, Long> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<InvoiceErecord> searchInvoiceErecord(String searchCond, Integer invoiceType);
}
