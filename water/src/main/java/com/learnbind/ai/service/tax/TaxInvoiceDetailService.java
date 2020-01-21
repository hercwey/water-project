package com.learnbind.ai.service.tax;

import java.util.List;

import com.learnbind.ai.model.TaxInvoiceDetail;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.taxinvoice
 *
 * @Title: TaxInvoiceDetailService.java
 * @Description: 发票-明细表service接口类
 *
 * @author Administrator
 * @date 2019年12月1日 上午12:06:14
 * @version V1.0 
 *
 */
public interface TaxInvoiceDetailService extends IBaseService<TaxInvoiceDetail, Long> {
	
	/**
	 * @Title: getInvoiceDetail
	 * @Description: 根据发票Id获取发票明细
	 * @param invocieId
	 * @return 
	 */
	public List<TaxInvoiceDetail> getInvoiceDetail(Long invocieId);
}
