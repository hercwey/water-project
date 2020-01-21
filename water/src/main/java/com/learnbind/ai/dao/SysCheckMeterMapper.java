package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysCheckMeter;

import tk.mybatis.mapper.common.Mapper;

public interface SysCheckMeterMapper extends Mapper<SysCheckMeter> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysCheckMeter> searchCheckMeter(@Param("searchCond") String searchCond);
}