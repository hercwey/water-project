package com.learnbind.ai.service.tax.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.dao.TaxInvoiceDetailMapper;
import com.learnbind.ai.model.TaxInvoiceDetail;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.tax.TaxInvoiceDetailService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.taxinvoice.impl
 *
 * @Title: TaxInvoiceDetailServiceImpl.java
 * @Description: 发票-明细表service实现类
 *
 * @author Administrator
 * @date 2019年12月1日 上午12:15:07
 * @version V1.0 
 *
 */
@Service
public class TaxInvoiceDetailServiceImpl extends AbstractBaseService<TaxInvoiceDetail, Long> implements  TaxInvoiceDetailService {
	
	@Autowired
	public TaxInvoiceDetailMapper taxInvoiceDetailMapper;
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public TaxInvoiceDetailServiceImpl(TaxInvoiceDetailMapper mapper) {
		this.taxInvoiceDetailMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<TaxInvoiceDetail> getInvoiceDetail(Long invocieId) {
		Example example = new Example(TaxInvoiceDetail.class);
		example.createCriteria().andEqualTo("invoiceId", invocieId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<TaxInvoiceDetail> detailList = this.selectByExample(example);
		return detailList;
	}

}
