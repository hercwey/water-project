package com.learnbind.ai.service.statistic;

import java.util.List;

import com.learnbind.ai.model.DrainWaterRecord;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.statistic
 *
 * @Title: DrainWaterRecordService.java
 * @Description: 官网末梢排水记录
 *
 * @author Thinkpad
 * @date 2020年1月18日 下午6:44:46
 * @version V1.0 
 *
 */
public interface DrainWaterRecordService extends IBaseService<DrainWaterRecord, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<DrainWaterRecord> searchCond(String searchCond, String period);
    
    
}
