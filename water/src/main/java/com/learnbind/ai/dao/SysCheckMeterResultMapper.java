package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.model.SysCheckMeterResult;
import tk.mybatis.mapper.common.Mapper;

public interface SysCheckMeterResultMapper extends Mapper<SysCheckMeterResult> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysCheckMeterResult> searchCond(@Param("searchCond") String searchCond);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysCheckMeterResult> getList(@Param("searchCond") String searchCond, @Param("meterId") Long meterId, @Param("checkType") Integer checkType);
}