package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.BusinessOffice;
import com.learnbind.ai.model.UsersBusOffice;
import tk.mybatis.mapper.common.Mapper;

public interface UsersBusOfficeMapper extends Mapper<UsersBusOffice> {
	
	public List<UsersBusOffice> searchCond(@Param("searchCond")String searchCond);
}