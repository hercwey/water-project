package com.learnbind.ai.service.notify;

import java.util.List;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.NotifyGroupMeter;
import com.learnbind.ai.service.common.IBaseService;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.notify
 *
 * @Title: NotifyMeterGroupService.java
 * @Description: 水费通知单表计分组关系
 *
 * @author Thinkpad
 * @date 2019年12月7日 下午12:42:22
 * @version V1.0 
 *
 */
public interface NotifyGroupMeterService extends IBaseService<NotifyGroupMeter, Long> {

	/**
	 * @Title: getSelectedMeterList
	 * @Description: 根据分组ID获取该分组下的表计集合
	 * @param groupId
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getSelectedMeterList(Long groupId, String searchCond);
	
	/**
	 * @Title: getUnselectedMeterList
	 * @Description: 获取未选择的表计集合
	 * @param customerId
	 * @param includeFlag
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getUnselectedMeterList(Long customerId, Integer includeFlag, String searchCond);
	
	/**
	 * @Title: selectedMeter
	 * @Description: 选择表计（把表计增加到分组-表计关系表）
	 * @param groupId
	 * @param meterIdList
	 * @return 
	 */
	public int selectedMeter(Long groupId, List<Long> meterIdList);
	
	/**
	 * @Title: unselectedMeter
	 * @Description: 取消选择表计（把表计从分组-表计关系表中移除）
	 * @param groupId
	 * @param meterIdList
	 * @return 
	 */
	public int unselectedMeter(Long groupId, List<Long> meterIdList);
	
	/**
	 * @Title: getGroupMeterList
	 * @Description: 根据分组ID查询分组-表计
	 * @param groupId
	 * @return 
	 */
	public List<NotifyGroupMeter> getGroupMeterList(Long groupId);
 
}
