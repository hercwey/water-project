package com.learnbind.ai.service.meters.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.bean.MeterTreeNodeBean;
import com.learnbind.ai.dao.MeterTreeMapper;
import com.learnbind.ai.model.MeterTree;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meters.MeterTreeService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MeterTreeServiceImpl.java
 * @Description: 表计父子关系service实现类
 *
 * @author Administrator
 * @date 2019年9月2日 下午12:58:28
 * @version V1.0 
 *
 */
@Service
public class MeterTreeServiceImpl extends AbstractBaseService<MeterTree, Long> implements  MeterTreeService {
	
	@Autowired
	public MeterTreeMapper meterTreeMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterTreeServiceImpl(MeterTreeMapper mapper) {
		this.meterTreeMapper=mapper;
		this.setMapper(mapper);
	}


	@Override
	public List<MeterTreeNodeBean> getChildListById(Long id) {
		return meterTreeMapper.getChildListById(id);
	}


	@Override
	public Integer getMaxSortValueOfSibling(Long parentId) {
		return meterTreeMapper.getMaxSortValueOfSibling(parentId);
	}


	@Override
	public int deleteMeterTreeNodeByTraceIds(String traceIds) {
		return meterTreeMapper.deleteMeterTreeNodeByTraceIds(traceIds);
	}


	@Override
	public int addNodeSortValueOfSilbing(Integer startSortValue,Integer includeStart, 
										Integer endSortValue, Integer includeEnd,
										Integer addValue,Long pid) {
		return meterTreeMapper.addNodeSortValueOfSilbing(startSortValue,includeStart,endSortValue,includeEnd,addValue,pid);
	}


	@Override
	public int updateSubTreeLevel(Integer subTreeRootNewLevel, Integer subTreeRootOldLevel,
			String subTreeRootTraceIds) {
		return meterTreeMapper.updateSubTreeLevel(subTreeRootNewLevel,subTreeRootOldLevel,
				subTreeRootTraceIds);
	}


	@Override
	public int updateSubTreeTraceIds(String subTreeRootNewTraceIds, String subTreeRootOldTraceIds,
			String subTreeRootTraceIds) {
		return meterTreeMapper.updateSubTreeTraceIds( subTreeRootNewTraceIds,  subTreeRootOldTraceIds,
				subTreeRootTraceIds);
	}


	@Override
	public int adjustTreeNodeSilbingPos(Long pid, Integer startSortValue, Integer includeStart) {
		return meterTreeMapper.adjustTreeNodeSilbingPos(pid, startSortValue, includeStart);
	}


	@Override
	public MeterTree getFirstBySearchCond(String searchCond) {
		return meterTreeMapper.getFirstBySearchCond(searchCond);
	}


	@Override
	public MeterTree getOneBySearchCond(String searchCond, Long id, Integer action) {
		return meterTreeMapper.getOneBySearchCond(searchCond, id, action);
	}
	

}
