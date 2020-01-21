package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.UserMeterBook;

import tk.mybatis.mapper.common.Mapper;

public interface UserMeterBookMapper extends Mapper<UserMeterBook> {
	
	/**
	 * @Title: deleteByMeterBookIdList
	 * @Description: 根据表册ID集合删除
	 * @param meterBookIdList
	 * @return 
	 */
	public Integer deleteByMeterBookIdList(@Param("meterBookIdList") List<Long> meterBookIdList, @Param("readerId") Long readerId);
}