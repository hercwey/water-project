package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterOutStockDoc;
import tk.mybatis.mapper.common.Mapper;

public interface MeterOutStockDocMapper extends Mapper<MeterOutStockDoc> {
	
	public List<MeterOutStockDoc>  searchCond(@Param("searchCond") String searchCond, @Param("meterId") Long meterId);
}