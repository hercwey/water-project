package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.NotifyGroup;
import tk.mybatis.mapper.common.Mapper;

public interface NotifyGroupMapper extends Mapper<NotifyGroup> {
	
	public List<NotifyGroup> searchCond(@Param("searchCond")String searchCond, @Param("includeFlag")Integer includeFlag);
}