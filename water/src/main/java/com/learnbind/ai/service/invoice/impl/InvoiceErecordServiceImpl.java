package com.learnbind.ai.service.invoice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.EngineeringDocMapper;
import com.learnbind.ai.dao.InvoiceErecordMapper;
import com.learnbind.ai.model.Invoice;
import com.learnbind.ai.model.InvoiceErecord;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.invoice.InvoiceErecordService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.invoice.impl
 *
 * @Title: InvoiceErecordServiceImpl.java
 * @Description: 发票管理-文件
 *
 * @author Thinkpad
 * @date 2019年9月1日 上午10:26:52
 * @version V1.0 
 *
 */
@Service
public class InvoiceErecordServiceImpl extends AbstractBaseService<InvoiceErecord, Long> implements InvoiceErecordService {
	
	@Autowired
	public InvoiceErecordMapper invoiceErecordMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public InvoiceErecordServiceImpl(InvoiceErecordMapper mapper) {
		this.invoiceErecordMapper=mapper;
		this.setMapper(mapper);
	}
	
	@Override
	public List<InvoiceErecord> searchInvoiceErecord(String searchCond, Integer invoiceType) {
		return invoiceErecordMapper.searchInvoiceErecord(searchCond, invoiceType);
	}

}
