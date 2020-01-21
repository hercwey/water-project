package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerOverdueFine;
import tk.mybatis.mapper.common.Mapper;

public interface CustomerOverdueFineMapper extends Mapper<CustomerOverdueFine> {
	
	/**
	 * @Title: selectObjectByAccountId
	 * @Description: 根据违约金账单ID超找违约金记录
	 * @param overdueAccountId
	 * @return 
	 */
	public List<CustomerOverdueFine> selectIdByAccountIdList(@Param("accountIdIdList")List<Long> accountIdIdList);
}