package com.learnbind.ai.service.meterbook;

import java.util.List;

import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.UserMeterBook;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterbook
 *
 * @Title: MeterBookMeterService.java
 * @Description: 表册-表计关系服务接口类
 *
 * @author Administrator
 * @date 2019年6月11日 下午12:27:29
 * @version V1.0 
 *
 */
public interface MeterBookMeterService extends IBaseService<MeterBookMeter, Long> {
	
	/**
	 * @Title: searchMeterBookMeterList
	 * @Description: 获取表册的表计列表
	 * @param searchCond
	 * @param meterBookId
	 * @return 
	 */
	public List<MeterBookMeter> searchMeterBookMeterList(String searchCond, Long meterBookId);
	
	/**
	 * @Title: insertBatch
	 * @Description: 批量保存
	 * @param userMeterBookList
	 * @return 
	 */
	public int insertBatch(List<MeterBookMeter> meterBookMeterList);
	
	/**
	 * @Title: deleteBatch
	 * @Description: 批量删除
	 * @param userMeterBookList
	 * @return 
	 */
	public int deleteBatch(List<MeterBookMeter> meterBookMeterList);
	
	/**
	 * @Title: moveBatch
	 * @Description: 批量移动
	 * @param userMeterBookList
	 * @return 
	 */
	public int moveBatch(List<MeterBookMeter> meterBookMeterList , String meterBookId);
	
	
}
