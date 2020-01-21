package com.learnbind.ai.service.invoice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.InvoiceMapper;
import com.learnbind.ai.dao.KnowLibraryMapper;
import com.learnbind.ai.model.Invoice;
import com.learnbind.ai.model.KnowLibrary;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.invoice.InvoiceService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.invoice.impl
 *
 * @Title: InvoiceServiceImpl.java
 * @Description: 单据管理
 *
 * @author Thinkpad
 * @date 2019年9月1日 上午9:38:25
 * @version V1.0 
 *
 */
@Service
public class InvoiceServiceImpl extends AbstractBaseService<Invoice, Long> implements  InvoiceService {
	
	@Autowired
	public InvoiceMapper invoiceMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public InvoiceServiceImpl(InvoiceMapper mapper) {
		this.invoiceMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<Invoice> searchInvoice(String searchCond, Integer invoiceType, String period) {
		return invoiceMapper.searchInvoice(searchCond, invoiceType, period);
	}




}
