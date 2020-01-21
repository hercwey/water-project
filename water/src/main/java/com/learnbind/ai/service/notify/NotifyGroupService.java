package com.learnbind.ai.service.notify;

import java.util.List;

import com.learnbind.ai.model.NotifyGroup;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.notify
 *
 * @Title: NotifyGroupService.java
 * @Description: 水费通知单分组
 *
 * @author Thinkpad
 * @date 2019年12月7日 下午12:33:34
 * @version V1.0 
 *
 */
public interface NotifyGroupService extends IBaseService<NotifyGroup, Long> {

	
	/**
	 * @Title: searchCond
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @param includeFlag
	 * @return 
	 */
	public List<NotifyGroup> searchCond(String searchCond, Integer includeFlag);
    
	/**
	 * @Title: delete
	 * @Description: 根据ID删除
	 * @param id
	 * @return 
	 */
	public int delete(Long id);
    
	/**
	 * @Title: getCustomerGroupList
	 * @Description: 获取客户的所有分组
	 * @param customerId
	 * @return 
	 */
	public List<NotifyGroup> getCustomerGroupList(Long customerId);
}
