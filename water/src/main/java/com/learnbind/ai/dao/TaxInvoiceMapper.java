package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.bean.TaxInvoiceBean;
import com.learnbind.ai.model.TaxInvoice;
import tk.mybatis.mapper.common.Mapper;

public interface TaxInvoiceMapper extends Mapper<TaxInvoice> {
	
	/**
	 * @Title: getTaxInvocie
	 * @Description: 条件查询发票
	 * @param bean
	 * @return 
	 */
	public List<TaxInvoice> getTaxInvocie(@Param("bean") TaxInvoiceBean bean);
	
	/**
	 * @Title: getPreTaxInvocie
	 * @Description:获取往期最后一条发票
	 * @param customerId
	 * @return 
	 */
	public List<TaxInvoice> getPreTaxInvocie(@Param("bean") Long customerId);
	
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
	public Map<String, Object> getStatTaxInvoiceAmount(@Param("kprq") String kprq, @Param("fpzl") String fpzl, @Param("zfbz") Integer zfbz, @Param("chbz") Integer chbz);
	
}