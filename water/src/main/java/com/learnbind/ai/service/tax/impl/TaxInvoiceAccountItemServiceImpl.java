package com.learnbind.ai.service.tax.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.TaxInvoiceAccountItemMapper;
import com.learnbind.ai.model.TaxInvoiceAccountItem;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.tax.TaxInvoiceAccountItemService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.taxinvoice.impl
 *
 * @Title: TaxInvoiceAccountItemServiceImpl.java
 * @Description: 发票与账单关系表service实现类
 *
 * @author Administrator
 * @date 2019年12月1日 上午12:15:07
 * @version V1.0 
 *
 */
@Service
public class TaxInvoiceAccountItemServiceImpl extends AbstractBaseService<TaxInvoiceAccountItem, Long> implements  TaxInvoiceAccountItemService {
	
	@Autowired
	public TaxInvoiceAccountItemMapper taxInvoiceAccountItemMapper;
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public TaxInvoiceAccountItemServiceImpl(TaxInvoiceAccountItemMapper mapper) {
		this.taxInvoiceAccountItemMapper=mapper;
		this.setMapper(mapper);
	}

}
