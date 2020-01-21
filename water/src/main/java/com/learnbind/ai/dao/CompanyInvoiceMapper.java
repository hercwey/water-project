package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CompanyInvoice;
import tk.mybatis.mapper.common.Mapper;

public interface CompanyInvoiceMapper extends Mapper<CompanyInvoice> {
	
	/**
	 * @Title: searchCond
	 * @Description: 条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<CompanyInvoice> searchCond(@Param("searchCond")String searchCond);
}