package com.learnbind.ai.service.tax.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.bean.TaxInvoiceBean;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.dao.TaxInvoiceMapper;
import com.learnbind.ai.model.TaxInvoice;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.tax.TaxInvoiceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.taxinvoice.impl
 *
 * @Title: TaxInvoiceServiceImpl.java
 * @Description: 发票信息表service实现类
 *
 * @author Administrator
 * @date 2019年12月1日 上午12:15:07
 * @version V1.0 
 *
 */
@Service
public class TaxInvoiceServiceImpl extends AbstractBaseService<TaxInvoice, Long> implements  TaxInvoiceService {
	
	@Autowired
	public TaxInvoiceMapper taxInvoiceMapper;
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public TaxInvoiceServiceImpl(TaxInvoiceMapper mapper) {
		this.taxInvoiceMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<TaxInvoice> getTaxInvocie(TaxInvoiceBean bean) {
		return taxInvoiceMapper.getTaxInvocie(bean);
	}

	@Override
	public TaxInvoice getTaxMessage(String fpdm, String fphm) {
		Example example = new Example(TaxInvoice.class);
		example.createCriteria().andEqualTo("fphm", fphm).andEqualTo("fpdm", fpdm).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<TaxInvoice> invocieList = this.selectByExample(example);
		return invocieList.get(0);
	}

	@Override
	public TaxInvoice getPreTaxInvocie(Long customerId) {
		List<TaxInvoice> taxList = taxInvoiceMapper.getPreTaxInvocie(customerId);
		return taxList.get(0);
	}

	@Override
	public Map<String, Object> getStatTaxInvoiceAmount(String kprq, String fpzl, Integer zfbz, Integer chbz) {
		return taxInvoiceMapper.getStatTaxInvoiceAmount(kprq, fpzl, zfbz, chbz);
	}
	
}
