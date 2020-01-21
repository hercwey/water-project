package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.bean.MeterTreeNodeBean;
import com.learnbind.ai.model.MeterTree;

import tk.mybatis.mapper.common.Mapper;

public interface MeterTreeMapper extends Mapper<MeterTree> {
	public List<MeterTreeNodeBean> getChildListById(@Param("id") Long id);
	public Integer getMaxSortValueOfSibling(@Param("parentId") Long parentId);
	public int deleteMeterTreeNodeByTraceIds(@Param("traceIds") String traceIds);
	public int addNodeSortValueOfSilbing(@Param("startSortValue") Integer startSortValue,
			@Param("includeStart") Integer includeStart,
			@Param("endSortValue") Integer endSortValue,
			@Param("includeEnd") Integer includeEnd,
			@Param("addValue") Integer addValue, @Param("pid") Long pid);
	
	public int updateSubTreeLevel(@Param("subTreeRootNewLevel")  Integer subTreeRootNewLevel, @Param("subTreeRootOldLevel") Integer subTreeRootOldLevel,
			@Param("subTreeRootTraceIds") String subTreeRootTraceIds);
	
	public int updateSubTreeTraceIds(@Param("subTreeRootNewTraceIds") String subTreeRootNewTraceIds,@Param("subTreeRootOldTraceIds") String subTreeRootOldTraceIds,
			@Param("subTreeRootTraceIds") String subTreeRootTraceIds);
	
	public int adjustTreeNodeSilbingPos(@Param("pid") Long pid, 
										@Param("startSortValue") Integer startSortValue, 
										@Param("includeStart") Integer includeStart);
	
	public MeterTree getFirstBySearchCond(@Param("searchCond") String searchCond);
	public MeterTree getOneBySearchCond(@Param("searchCond") String searchCond, @Param("id") Long id, @Param("action") Integer action) ;
	
}