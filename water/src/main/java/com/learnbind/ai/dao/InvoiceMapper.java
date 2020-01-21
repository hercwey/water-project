package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.Invoice;

import tk.mybatis.mapper.common.Mapper;

public interface InvoiceMapper extends Mapper<Invoice> {
	
	public List<Invoice> searchInvoice(@Param("searchCond")String searchCond, @Param("invoiceType")Integer invoiceType, @Param("period") String period);
}