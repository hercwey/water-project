package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterBook;

import tk.mybatis.mapper.common.Mapper;

public interface MeterBookMapper extends Mapper<MeterBook> {
	
	/**
	 * @Title: getListByCondition
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getListByCondition(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: getUnAllotBookListByCondition
	 * @Description: 根据条件查询未分配的表册
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getUnAllotBookListByCondition(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("readerId") Long readerId);
	
	/**
	 * @Title: getReaderBookListByCondition
	 * @Description: 根据条件查询抄表员的表册
	 * @param userId	抄表员ID
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getReaderBookListByCondition(@Param("userId") Long userId, @Param("searchCond") String searchCond);
	
	public List<MeterBook> getMyMeterBookList(@Param("userId") Long userId, @Param("searchCond") String searchCond, @Param("traceIds") String traceIds);
	
	/**
	 * @Title: getMeterBookTypeNameList
	 * @Description: 获取表册小区列表
	 * @return 
	 */
	public List<MeterBook> getMeterBookTypeNameList();
	
	/**
	 * @Title: getMeterBookIdByCode
	 * @Description: 根据表名称查询表册id
	 * @param name
	 * @return 
	 */
	public List<MeterBook> getMeterBookIdByCode(@Param("code")String code);
	
	/**
	 * @Title: getMeterBookByTraceIdsAndSearchCond
	 * @Description: 条件查询表册
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	public List<MeterBook> getMeterBookByTraceIdsAndSearchCond(@Param("traceIds")String traceIds, @Param("searchCond")String searchCond ,@Param("generateStatus")Integer generateStatus);
	
	
	public List<MeterBook> getAllotBookListByCondition(@Param("userId") Long userId, @Param("traceIds") String traceIds, @Param("searchCond") String searchCond);
	
}