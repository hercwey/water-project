package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.LoginLog;
import tk.mybatis.mapper.common.Mapper;

public interface LoginLogMapper extends Mapper<LoginLog> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<LoginLog> search(@Param("searchCond")String searchCond);
}