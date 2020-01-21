package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterChangeRecepit;
import tk.mybatis.mapper.common.Mapper;

public interface MeterChangeRecepitMapper extends Mapper<MeterChangeRecepit> {
	
	public List<MeterChangeRecepit> searchCond(@Param("searchCond") String searchCond, @Param("meterId") Long meterId);
}