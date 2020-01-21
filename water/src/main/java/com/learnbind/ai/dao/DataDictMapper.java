package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.DataDict;

import tk.mybatis.mapper.common.Mapper;

public interface DataDictMapper extends Mapper<DataDict> {
	/**
	 * 	根据条件查询数据字典类型
	 * @param searchDictTypeCond
	 * @return
	 */
	public List<Map<String, String>> searchDictTypeCond(@Param("searchCond")String searchCond);
	
	/**
	 * 	根据条件查询数据字典类型
	 * @param searchDictTypeCond
	 * @return
	 */
	public List<Map<String, Object>> searchLabelList(@Param("searchCond")String searchCond);
}