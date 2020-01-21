package com.learnbind.ai.service.notify.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.NotifyGroupMapper;
import com.learnbind.ai.model.NotifyGroup;
import com.learnbind.ai.model.NotifyGroupMeter;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.notify.NotifyGroupMeterService;
import com.learnbind.ai.service.notify.NotifyGroupService;

import tk.mybatis.mapper.entity.Example;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     WaterPriceServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class NotifyGroupServiceImpl extends AbstractBaseService<NotifyGroup, Long> implements  NotifyGroupService {
	
	@Autowired
	public NotifyGroupMapper notifyGroupMapper;

	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public NotifyGroupServiceImpl(NotifyGroupMapper mapper) {
		this.notifyGroupMapper=mapper;
		this.setMapper(mapper);
	}

	@Autowired
	public NotifyGroupMeterService notifyGroupMeterService;//分组-表计关系
	
	@Override
	public List<NotifyGroup> searchCond(String searchCond, Integer includeFlag) {
		return notifyGroupMapper.searchCond(searchCond, includeFlag);
	}

	@Override
	public int delete(Long id) {
		int rows = notifyGroupMapper.deleteByPrimaryKey(id);
		if(rows>0) {
			NotifyGroupMeter groupMeter = new NotifyGroupMeter();
			groupMeter.setGroupId(id);
			notifyGroupMeterService.delete(groupMeter);
		}
		return rows;
	}

	@Override
	public List<NotifyGroup> getCustomerGroupList(Long customerId) {
		Example example = new Example(NotifyGroup.class);
		example.createCriteria().andEqualTo("customerId", customerId);
		List<NotifyGroup> groupList = this.selectByExample(example);
		return groupList;
	}

}
