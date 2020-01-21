package com.learnbind.ai.service.addsubwater;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.addsubwater
 *
 * @Title: AddSubWaterService.java
 * @Description: 增加减免水量
 *
 * @author Thinkpad
 * @date 2019年6月5日 下午12:09:52
 * @version V1.0 
 *
 */
public interface AddSubWaterService extends IBaseService<AddSubWater, Long> {
	
	/**
	 * @Title: saveList
	 * @Description: 保存追加减免水量（未用）
	 * @param customerId
	 * @param period
	 * @param meterIds
	 * @param awList
	 * @return 
	 */
	public int saveList(Long customerId, String period, String meterIds, List<AddSubWater> awList, String recordIds);
	
	/**
	 * @Title: saveList
	 * @Description: 保存追加减免水量（未用）
	 * @param customerId
	 * @param period
	 * @param meterIds
	 * @param recordIds
	 * @param pwJsonOjb
	 * @return 
	 */
	public int saveList(Long customerId, String period, String meterIds, String recordIds, JSONObject pwJsonOjb);
	
	/**
	 * @Title: saveAddSubWater
	 * @Description: 保存追加减免水量
	 * @param partWaterId
	 * @param pwJsonOjb
	 * @return 
	 */
	public int saveAddSubWater(Long partWaterId, JSONObject pwJsonOjb);
	
	/**
	 * @Title: getAddSubLog
	 * @Description: 获取追加减免水量日志
	 * @param customerId
	 * @param period
	 * @param partwaterId
	 * @return 
	 */
	public AddSubWater getAddSubLog(Long customerId, String period, Long partwaterId);
	
}
