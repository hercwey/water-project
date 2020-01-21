package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.PrinterConfig;

import tk.mybatis.mapper.common.Mapper;

public interface PrinterConfigMapper extends Mapper<PrinterConfig> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<PrinterConfig> searchPrinter(@Param("searchCond") String searchCond);
}