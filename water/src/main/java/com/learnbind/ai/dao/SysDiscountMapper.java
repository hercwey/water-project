package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysDiscount;

import tk.mybatis.mapper.common.Mapper;

public interface SysDiscountMapper extends Mapper<SysDiscount> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysDiscount> searchDiscount(@Param("searchCond") String searchCond);
}