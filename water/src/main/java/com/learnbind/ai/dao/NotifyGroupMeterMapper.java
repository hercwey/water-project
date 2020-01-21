package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.NotifyGroupMeter;

import tk.mybatis.mapper.common.Mapper;

public interface NotifyGroupMeterMapper extends Mapper<NotifyGroupMeter> {
	
	/**
	 * @Title: getSelectedMeterList
	 * @Description: 根据分组ID获取该分组下的表计集合
	 * @param groupId
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getSelectedMeterList(@Param("groupId") Long groupId, @Param("searchCond") String searchCond);
	
	/**
	 * @Title: getUnelectedMeterList
	 * @Description: 获取未选择的表计集合
	 * @param customerId
	 * @param includeFlag
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getUnelectedMeterList(@Param("customerId") Long customerId, @Param("includeFlag") Integer includeFlag, @Param("searchCond") String searchCond);
	
}