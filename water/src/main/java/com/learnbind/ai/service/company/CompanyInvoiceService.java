package com.learnbind.ai.service.company;

import java.util.List;

import com.learnbind.ai.model.CompanyInvoice;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.company
 *
 * @Title: CompanyInvoiceService.java
 * @Description: 供排水开票信息
 *
 * @author Thinkpad
 * @date 2019年11月27日 下午2:43:25
 * @version V1.0 
 *
 */
public interface CompanyInvoiceService extends IBaseService<CompanyInvoice, Long> {
	
	/**
	 * @Title: searchCond
	 * @Description: 条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<CompanyInvoice> searchCond(String searchCond);
	
	/**
	 * @Title: getDefaultCompanyInvoice
	 * @Description: 获取默认开票信息
	 * @return 
	 */
	public CompanyInvoice getDefaultCompanyInvoice();
}
