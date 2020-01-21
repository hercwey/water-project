package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerInfoCollect;
import tk.mybatis.mapper.common.Mapper;

public interface CustomerInfoCollectMapper extends Mapper<CustomerInfoCollect> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomerInfoCollect> searchCond(@Param("searchCond")String searchCond);
}