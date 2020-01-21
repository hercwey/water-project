package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.InvoiceErecord;

import tk.mybatis.mapper.common.Mapper;

public interface InvoiceErecordMapper extends Mapper<InvoiceErecord> {
	
	public List<InvoiceErecord> searchInvoiceErecord(@Param("searchCond")String searchCond, @Param("invoiceType")Integer invoiceType);
}