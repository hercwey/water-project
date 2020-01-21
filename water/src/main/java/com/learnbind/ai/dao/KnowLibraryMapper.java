package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.KnowLibrary;
import tk.mybatis.mapper.common.Mapper;

public interface KnowLibraryMapper extends Mapper<KnowLibrary> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<KnowLibrary> searchKnowLibrary(@Param("searchCond")String searchCond, @Param("knowType")String knowType);
	
	public List<KnowLibrary> searchArticlebyType(@Param("knowType") String knowType);
	
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<KnowLibrary> searchAllKnowLibrary(@Param("searchCond")String searchCond, @Param("label")String label);
}