package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterBookMeter;
import tk.mybatis.mapper.common.Mapper;

public interface MeterBookMeterMapper extends Mapper<MeterBookMeter> {
	
	/**
	 * @Title: searchMeterBookMeterList
	 * @Description: 获取表册的表计列表
	 * @param searchCond
	 * @param meterBookId
	 * @return 
	 */
	public List<MeterBookMeter> searchMeterBookMeterList(@Param("searchCond")String searchCond, @Param("meterBookId")Long meterBookId);
}