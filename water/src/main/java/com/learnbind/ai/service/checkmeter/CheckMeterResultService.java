package com.learnbind.ai.service.checkmeter;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.model.SysCheckMeterResult;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.checkmeter
 *
 * @Title: CheckMeterResultService.java
 * @Description: 水表检测结果
 *
 * @author Thinkpad
 * @date 2019年8月3日 下午8:07:22
 * @version V1.0 
 *
 */
public interface CheckMeterResultService extends IBaseService<SysCheckMeterResult, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysCheckMeterResult> searchCond(String searchCond);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<SysCheckMeterResult> getList(String searchCond, Long meterId, Integer checkType);
	
}
