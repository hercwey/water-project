package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.BusinessOffice;
import tk.mybatis.mapper.common.Mapper;

public interface BusinessOfficeMapper extends Mapper<BusinessOffice> {
	
	public List<BusinessOffice> searchCond(@Param("searchCond")String searchCond);
	
}