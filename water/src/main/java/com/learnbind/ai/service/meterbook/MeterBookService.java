package com.learnbind.ai.service.meterbook;

import java.util.List;

import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterbook
 *
 * @Title: MeterBookService.java
 * @Description: 表册服务接口类
 *
 * @author Administrator
 * @date 2019年6月10日 下午7:29:45
 * @version V1.0 
 *
 */
public interface MeterBookService extends IBaseService<MeterBook, Long> {
	
	/**
	 * @Title: getListByCondition
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getListByCondition(String searchCond);
	
	/**
	 * @Title: getUnAllotBookListByCondition
	 * @Description: 根据条件查询未分配的表册
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getUnAllotBookListByCondition(String searchCond, String traceIds, Long readerId);
	
	/**
	 * @Title: getReaderBookListByCondition
	 * @Description: 根据条件查询抄表员的表册
	 * @param userId	抄表员ID
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getReaderBookListByCondition(Long userId, String searchCond);
	
	/**
	 * @Title: getMyMeterBookList
	 * @Description: 根据条件查询抄表员的表册-带地理位置查询
	 * @param userId
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<MeterBook> getMyMeterBookList(Long userId, String searchCond, String traceIds);
	
	/**
	 * @Title: createMeterBookByUnitId
	 * @Description: 根据小区创建表册
	 * @param operatorId
	 * @param operatorName
	 * @param locationBlockId
	 * @return 
	 */
	public int createMeterBookByBlockId(Long operatorId, String operatorName, String typeCode, String typeName, Long locationBlockId);
	
	/**
	 * @Title: createMeterBookByUnitId
	 * @Description: 根据楼号创建表册
	 * @param operatorId
	 * @param operatorName
	 * @param locationBuildingId
	 * @return 
	 */
	public int createMeterBookByBuildingId(Long operatorId, String operatorName, String typeCode, String typeName, Long locationBuildingId, String name, String code);
	
	/**
	 * @Title: createMeterBookByUnitId
	 * @Description: 根据单元创建表册
	 * @param operatorId
	 * @param operatorName
	 * @param locationUnitId
	 * @return 
	 */
	public int createMeterBookByUnitId(Long operatorId, String operatorName, List<Location> locationList,String blockName);
	
	/**
	 * @Title: insertMeterBook
	 * @Description: 增加表册
	 * @param meterBook
	 * @param locationBlockId
	 * @param locationBuildingId
	 * @param locationUnitId
	 * @return 
	 */
	public int insertMeterBook(MeterBook meterBook, List<CustomerMeter> customerMeterList);
	
	/**
	 * @Title: deleteMeterBook
	 * @Description: 删除表册，同时删除表册-表计关系
	 * @param meterBookId
	 * @return 
	 */
	public int deleteMeterBook(Long meterBookId);
	
	
	/**
	 * @Title: getMeterBookTypeNameList
	 * @Description: 获取表册小区列表
	 * @return 
	 */
	public List<MeterBook> getMeterBookTypeNameList();
	
	
	/**
	 * @Title: getMeterBookIdByName
	 * @Description: 根据表名称查询表册id
	 * @param name
	 * @return 
	 */
	public List<MeterBook> getMeterBookIdByCode(String code);
	
	/**
	 * @Title: getMeterBookByTraceIdsAndSearchCond
	 * @Description: 条件查询表册
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getMeterBookByTraceIdsAndSearchCond(String traceIds, String searchCond, Integer generateStatus);
	
	public List<MeterBook> getAllotBookListByCondition(Long userId, String traceIds, String searchCond);
	
}
