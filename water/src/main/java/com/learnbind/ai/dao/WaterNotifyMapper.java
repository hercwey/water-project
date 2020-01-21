package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.WaterNotify;
import tk.mybatis.mapper.common.Mapper;

public interface WaterNotifyMapper extends Mapper<WaterNotify> {
	
	/**
	 * @Title: searchCond
	 * @Description: 查询通知单
	 * @param searchCond
	 * @param period
	 * @return 
	 */
	public List<WaterNotify> searchCond(@Param("searchCond") String searchCond, @Param("period") String period, @Param("sortMethod") Integer sortMethod, @Param("arrearsAmount") Integer arrearsAmount);
	
	public List<WaterNotify> getNotifyMeterIdList(@Param("period") String period, @Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("sortMethod") Integer sortMethod, @Param("arrearsAmount") Integer arrearsAmount);
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<WaterNotify> getWaterNotifyList(@Param("searchCond") String searchCond, @Param("period") String period, @Param("traceIds") String traceIds);
	
	/**
	 * @Title: getList
	 * @Description: 查询某客户的通知单
	 * @param customerId
	 * @param period
	 * @param searchCond
	 * @return 
	 */
	public List<WaterNotify> getList(@Param("customerId") Long customerId, @Param("period") String period, @Param("searchCond") String searchCond);
	
	public int isExistsLocation(@Param("meterIdStr") String meterIdStr, @Param("traceIds") String traceIds);
}