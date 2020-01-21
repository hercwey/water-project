package com.learnbind.ai.service.tax;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.bean.TaxInvoiceBean;
import com.learnbind.ai.model.TaxInvoice;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.taxinvoice
 *
 * @Title: TaxInvoiceService.java
 * @Description: 开票信息service接口类
 *
 * @author Administrator
 * @date 2019年12月1日 上午12:06:14
 * @version V1.0 
 *
 */
public interface TaxInvoiceService extends IBaseService<TaxInvoice, Long> {
	
	/**
	 * @Title: getTaxInvocie
	 * @Description: 条件查询发票
	 * @param bean
	 * @return 
	 */
	public List<TaxInvoice> getTaxInvocie(TaxInvoiceBean bean);
	
	
	/**
	 * @Title: getTaxMessage
	 * @Description: 根据发票号码及发票代码获取唯一发票
	 * @param fpdm
	 * @param fphm
	 * @return 
	 */
	public TaxInvoice getTaxMessage(String fpdm,String fphm);
	
	
	/**
	 * @Title: getPreTaxInvocie
	 * @Description:获取往期最后一条发票
	 * @param customerId
	 * @return 
	 */
	public TaxInvoice getPreTaxInvocie(Long customerId);
	
	/**
	 * @Title: getStatTaxInvoiceAmount
	 * @Description: 统计开票金额
	 * @param kprq	开票日期
	 * @param fpzl	发票种类
	 * @param zfbz	作废标志
	 * @param chbz	冲红标志
	 * @return 
	 * 		SUM( HJJE ) AS SUM_HJJE	合计金额
			SUM( HJSE ) AS SUM_HJSE 合计税额
	 */
	public Map<String, Object> getStatTaxInvoiceAmount(String kprq, String fpzl, Integer zfbz, Integer chbz);
	
}
