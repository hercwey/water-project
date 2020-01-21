package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterChangeRecepit;
import com.learnbind.ai.model.MeterUnusedReceipt;
import tk.mybatis.mapper.common.Mapper;

public interface MeterUnusedReceiptMapper extends Mapper<MeterUnusedReceipt> {
	
	public List<MeterUnusedReceipt> searchCond(@Param("searchCond") String searchCond, @Param("meterId") Long meterId);
}