package com.learnbind.ai.service.notify.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.NotifyGroupMeterMapper;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.NotifyGroupMeter;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.notify.NotifyGroupMeterService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.notify.impl
 *
 * @Title: NotifyMeterGroupServiceImpl.java
 * @Description: 表计分组关系
 *
 * @author Thinkpad
 * @date 2019年12月7日 下午12:44:16
 * @version V1.0 
 *
 */
@Service
public class NotifyGroupMeterServiceImpl extends AbstractBaseService<NotifyGroupMeter, Long> implements  NotifyGroupMeterService {
	
	@Autowired
	public NotifyGroupMeterMapper notifyGroupMeterMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public NotifyGroupMeterServiceImpl(NotifyGroupMeterMapper mapper) {
		this.notifyGroupMeterMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<Meters> getSelectedMeterList(Long groupId, String searchCond) {
		return notifyGroupMeterMapper.getSelectedMeterList(groupId, searchCond);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getCustomerGroupList
	 * @Description:获取分组表记关系
	 * @param groupId
	 * @return 
	 * @see com.learnbind.ai.service.notify.NotifyGroupMeterService#getCustomerGroupList(java.lang.Long)
	 */
	@Override
	public List<NotifyGroupMeter> getGroupMeterList(Long groupId) {
		Example example = new Example(NotifyGroupMeter.class);
		example.createCriteria().andEqualTo("groupId", groupId);
		List<NotifyGroupMeter> groupMeterList = this.selectByExample(example);
		return groupMeterList;
	}

	@Override
	public List<Meters> getUnselectedMeterList(Long customerId, Integer includeFlag, String searchCond) {
		return notifyGroupMeterMapper.getUnelectedMeterList(customerId, includeFlag, searchCond);
	}

	@Override
	public int selectedMeter(Long groupId, List<Long> meterIdList) {
		int rows = 0;
		for(Long meterId : meterIdList) {
			NotifyGroupMeter groupMeter = new NotifyGroupMeter();
			groupMeter.setGroupId(groupId);
			groupMeter.setMeterId(meterId);
			rows = notifyGroupMeterMapper.insertSelective(groupMeter);
		}
		return rows;
	}

	@Override
	public int unselectedMeter(Long groupId, List<Long> meterIdList) {
		Example example = new Example(NotifyGroupMeter.class);
		example.createCriteria().andEqualTo("groupId", groupId).andIn("meterId", meterIdList);
		return notifyGroupMeterMapper.deleteByExample(example);
	}

}
