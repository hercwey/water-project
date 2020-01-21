package com.learnbind.ai.service.meterbook;

import java.util.List;

import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.UserMeterBook;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterbook
 *
 * @Title: UserMeterBookService.java
 * @Description: 用户-表册关系服务接口类
 *
 * @author Administrator
 * @date 2019年6月11日 下午12:24:40
 * @version V1.0 
 *
 */
public interface UserMeterBookService extends IBaseService<UserMeterBook, Long> {
	
	/**
	 * @Title: insertBatch
	 * @Description: 批量保存
	 * @param userMeterBookList
	 * @return 
	 */
	public int insertBatch(List<UserMeterBook> userMeterBookList);
	
	/**
	 * @Title: deleteBatch
	 * @Description: 批量删除
	 * @param userMeterBookList
	 * @return 
	 */
	public int deleteBatch(List<UserMeterBook> userMeterBookList);
	
	/**
	 * @Title: deleteByMeterBookIdList
	 * @Description: 根据表册ID集合删除
	 * @param meterBookIdList
	 * @return 
	 */
	public Integer deleteByMeterBookIdList(List<Long> meterBookIdList, Long readerId);
	 
	/**
	 * @Title: insertUserMeterBookByBlockCode
	 * @Description: 选择小区添加表册
	 * @param readerId
	 * @param locationBlockCode
	 * @return 
	 */
	public int insertUserMeterBookByBlockCode(Long readerId, String locationBlockCode);
	
	/**
	 * @Title: insertUserMeterBookByBuildingCode
	 * @Description: 选择楼号添加表册
	 * @param readerId
	 * @param locationBuiledingCode
	 * @return 
	 */
	public int insertUserMeterBookByBuildingCode(Long readerId, String locationBuiledingCode);
	
	/**
	 * @Title: insertUserMeterBookByUnitCode
	 * @Description: 选择单元添加表册
	 * @param readerId
	 * @param locationUnitCode
	 * @return 
	 */
	public int insertUserMeterBookByUnitCode(Long readerId, String locationUnitCode);
	
}
