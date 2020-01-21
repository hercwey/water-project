
package com.learnbind.ai.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.PartitionWater;

import tk.mybatis.mapper.common.Mapper;

public interface PartitionWaterMapper extends Mapper<PartitionWater> {
	
	/**
	 * @Title: getYearWaterAmount
	 * @Description: 获取年总用水量(不包含本期)
	 * @param customerId	客户ID
	 * @param year			年
	 * @param period		本期期间
	 * @return 
	 */
	public BigDecimal getYearWaterAmount(@Param("customerId") Long customerId, @Param("year") String year, @Param("period") String period);
	
	/**
	 * @Title: getRecentlySixMonthAmount
	 * @Description: 获取最近6个月的水量
	 * @param customerId	查询条件 :客户ID-customerId 
	 * @return 
	 */
	public List<Map<String,Object>> getRecentlySixMonthAmount(@Param("customerId") Long customerId);
	
	/**
	 * @Title: getPartitionWaterMapList
	 * @Description: 根据条件查询分水量map集合
	 * @param operatorId	操作员ID
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getPartitionWaterMapList(@Param("operatorId") Long operatorId, @Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("isMakeBill") Integer isMakeBill, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("period") String period);
	
	/**
	 * @Title: getPartitionWaterList
	 * @Description: 根据条件查询分水量集合,与开账查询列表SQL相同，此处返回实体类集合，方便操作（批量开账时，查询开账数据）
	 * @param operatorId
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<PartitionWater> getPartitionWaterList(@Param("operatorId") Long operatorId, @Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("isMakeBill") Integer isMakeBill);
	
	/**
	 * @Title: getCurrWaterAmount
	 * @Description: 查询某客户某期未开账的分水量
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getCurrWaterAmount(@Param("customerId") Long customerId, @Param("period") String period);
	
	
	public Map<String, Object> getPartitionWaterStatisticMap(@Param("operatorId") Long operatorId, @Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("isMakeBill") Integer isMakeBill, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("period") String period);
	
	public List<PartitionWater> getPartWaterMessage(@Param("period") String period, @Param("customerId") Long customerId, @Param("traceIds") String traceIds);
	
	/**
	 * @Title: getCustomerPartWater
	 * @Description: 根据表计ID集合，客户。期间查询
	 * @param meterIdList
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<PartitionWater> getCustomerPartWater(@Param("meterIdList") List<Long> meterIdList , @Param("customerId") Long customerId, @Param("period") String period);
	
	/**
	 * @Title: getCustomerPastPartWater
	 * @Description: 查询客户往期分水量记录
	 * @param meterIdList
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<PartitionWater> getCustomerPastPartWater(@Param("meterIdList") List<Long> meterIdList , @Param("customerId") Long customerId, @Param("period") String period);
	
	public List<Map<String, Object>> searchCustomerAccountItemErrorFee(@Param("operatorId") Long operatorId, @Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("isMakeBill") Integer isMakeBill, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("period") String period);
	/**
	 * @Title: getCardMeterAmount
	 * @Description: 获取卡表金额
	 * @param period
	 * @return 
	 */
	//public BigDecimal getCardMeterAmount(@Param("period") String period);
	
}