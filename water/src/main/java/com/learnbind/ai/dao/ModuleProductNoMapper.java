package com.learnbind.ai.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.ModuleProductNo;

import tk.mybatis.mapper.common.Mapper;

public interface ModuleProductNoMapper extends Mapper<ModuleProductNo> {
	
	/**
	 * @Title: searchList
	 * @Description: 查询
	 * @param operatorName
	 * @param operatorDate
	 * @param moduleNo
	 * @param productNo
	 * @return 
	 */
	public List<ModuleProductNo> searchList(@Param("operatorName") String operatorName, @Param("operatorDate") Date operatorDate, @Param("moduleNo") String moduleNo, @Param("productNo") String productNo);
	
}