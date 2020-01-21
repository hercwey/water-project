package com.learnbind.ai.service.company.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.dao.CompanyInvoiceMapper;
import com.learnbind.ai.model.CompanyInvoice;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.company.CompanyInvoiceService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.company.impl
 *
 * @Title: CompanyAccountItemServiceImpl.java
 * @Description: 公司账目service实现类
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:04:04
 * @version V1.0 
 *
 */
@Service
public class CompanyInvoiceServiceImpl extends AbstractBaseService<CompanyInvoice, Long> implements CompanyInvoiceService {

	public CompanyInvoiceMapper companyInvoiceMapper;

	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public CompanyInvoiceServiceImpl(CompanyInvoiceMapper mapper) {
		this.companyInvoiceMapper = mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<CompanyInvoice> searchCond(String searchCond) {
		return companyInvoiceMapper.searchCond(searchCond);
	}

	@Override
	public CompanyInvoice getDefaultCompanyInvoice() {
		CompanyInvoice searchObj = new CompanyInvoice();
		searchObj.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
		searchObj.setIsDefault(EnumDefaultStatus.DEFAULT_YES.getValue());
		List<CompanyInvoice> invoiceList = companyInvoiceMapper.select(searchObj);
		if(invoiceList!=null && invoiceList.size()>0) {
			return invoiceList.get(0);
		}
		return null;
	}

}
