package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.DrainWaterRecord;
import tk.mybatis.mapper.common.Mapper;

public interface DrainWaterRecordMapper extends Mapper<DrainWaterRecord> {
	

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<DrainWaterRecord> searchCond(@Param("searchCond")String searchCond, @Param("period")String period);
}