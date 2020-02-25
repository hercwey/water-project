package com.learnbind.ai.service.meterrecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterrecord
 *
 * @Title: PartitionWaterService.java
 * @Description: 分水量记录服务接口类
 *
 * @author Administrator
 * @date 2019年6月4日 上午10:39:11
 * @version V1.0 
 *
 */
public interface PartitionWaterService extends IBaseService<PartitionWater, Long> {
	
	/**
	 * @Title: saveList
	 * @Description: 删除原分水量记录，并保存分水量集合
	 * @param recordIds
	 * @param meterIds
	 * @param customerId
	 * @param period
	 * @param pwList
	 * @return 
	 */
	public int saveList(String recordIds, String meterIds, Long customerId, String period, List<PartitionWater> pwList);
	//json array 格式：[{waterPriceId:waterPriceId, waterAmount:waterAmount, waterPrice:waterPrice, waterUse:waterUse}]
	public int saveList(String recordIds, String meterIds, Long customerId, String period, JSONArray jsonArr);
	
	/**
	 * @Title: getYearWaterAmount
	 * @Description: 获取年总用水量
	 * @param customerId
	 * @param year
	 * @return 
	 */
	/**
	 * @Title: getYearWaterAmount
	 * @Description: 获取年总用水量(不包含本期)
	 * @param customerId	客户ID
	 * @param year			年
	 * @param period		本期期间
	 * @return 
	 */
	public BigDecimal getYearWaterAmount(Long customerId, String year, String period);
	
	/**
	 * 获取客户最近6个月的用水量
	 * @param customerId 客户ID
	 * @return 用水量列表
	 */
	public List<Map<String,Object>> getRecentlySixMonthAmount(Long customerId);
	
	/**
	 * @Title: getPartitionWaterMapList
	 * @Description: 根据条件查询分水量map集合
	 * @param operatorId	操作员ID
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getPartitionWaterMapList(Long operatorId, String searchCond, String traceIds, Integer isMakeBill, Integer isPartWater,  String startDate, String endDate, String period);
	
	/**
	 * @Title: getPartitionWaterList
	 * @Description: 根据条件查询分水量集合,与开账查询列表SQL相同，此处返回实体类集合，方便操作（批量开账时，查询开账数据）
	 * @param operatorId
	 * @param searchCond
	 * @param traceIds
	 * @param isMakeBill
	 * @return 
	 */
	public List<PartitionWater> getPartitionWaterList(Long operatorId, String searchCond, String traceIds, Integer isMakeBill);
	
	/**
	 * @Title: getCurrWaterAmount
	 * @Description: 查询某客户某期分水量
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public BigDecimal getCurrWaterAmount(Long customerId, String period);
	
	/**
	 * @Title: insertPartitionWater
	 * @Description: 单独收费-增加本期分水量
	 * @param customerId
	 * @param period
	 * @param meterId
	 * @param waterAmount
	 * @param startTime
	 * @param endTime
	 * @param sysDate
	 * @return 
	 */
	//public int insertPartitionWater(Long customerId, String period, Long meterId, BigDecimal waterAmount, Date startTime, Date endTime, Date sysDate);
	public int insertPartitionWater(MeterRecord meterRecord, Date sysDate);
	
	/**
	 * @Title: insertMergePartitionWater
	 * @Description: 合并收费-增加本期分水量(增加前会删除其他本期分水量)
	 * @param customerId
	 * @param period
	 * @param waterAmount
	 * @param startTime
	 * @param endTime
	 * @param sysDate
	 * @return 
	 */
	public int insertMergePartitionWater(Long customerId, String period, BigDecimal waterAmount, Date startTime, Date endTime, Date sysDate);
	
	/**
	 * @Title: isInsertPartitionWater
	 * @Description: 验证是否需要增加分水量
	 * @param customerId
	 * @param meterId
	 * @return 
	 */
	public boolean isInsertPartitionWater(Long customerId, Long meterId);
	
	/**
	 * @Title: batchGeneratorBill
	 * @Description: 批量开账：根据分水量批量生成账单
	 * @param pwList
	 * @return 
	 */
	public int batchGeneratorBill(List<PartitionWater> pwList);
	
	/**
	 * @Title: generatorBill
	 * @Description: 开账：根据分水量生成账单
	 * @param partitionWaterId
	 * @return 
	 */
	public int generatorBill(Long partitionWaterId);
	
	/**
	 * @Title: updateAccountItemId
	 * @Description: 更新分水量记录的账目ID
	 * @param partitionWaterId
	 * @param accountItemId
	 * @return 
	 */
	public int updateAccountItemId(Long partitionWaterId, Long accountItemId);
	
	/**
	 * @Title: getPartitionWater
	 * @Description: 根据账目ID获取分水量（账目与分水量关系是一对一）
	 * @param accountItemId
	 * @return 
	 */
	public PartitionWater getPartitionWater(Long accountItemId);
	
	/**
	 * @Title: confirmVirtualMeter
	 * @Description: 确认虚表
	 * @param period
	 * @param meterList
	 * @return 
	 */
	public int confirmVirtualMeter(String period, List<Meters> meterList);
	
	/**
	 * @Title: getMeterPartWaterRuleTraceList
	 * @Description: 根据条件获取当前分水量规则
	 * @param customerId
	 * @param period
	 * @param recordIds
	 * @return 
	 */
	public List<MeterPartWaterRuleTrace> getMeterPartWaterRuleTraceList(Long customerId, String period, String recordIds);
	
	/**
	 * @Title: savePartitionWater
	 * @Description: 保存分水量
	 * @param partWaterId
	 * @param ruleTraceList
	 * @return 
	 */
	public int savePartitionWater(Long partWaterId, List<MeterPartWaterRuleTrace> ruleTraceList);
	
	
	/**
	 * @Title: getPartitionWaterStatisticMap
	 * @Description: 获取统计的水量及水费
	 * @param operatorId
	 * @param searchCond
	 * @param traceIds
	 * @param isMakeBill
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public Map<String, Object> getPartitionWaterStatisticMap(Long operatorId, String searchCond, String traceIds, Integer isMakeBill, Integer isPartWater,  String startDate, String endDate, String period);
	
	
	/**
	 * @Title: getPartitionWaterByMeterList
	 * @Description: 根据表计id查询分水量记录
	 * @param meterId
	 * @return 
	 */
	public List<PartitionWater> getPartitionWaterByMeterList(Long meterId, String period);
	
	/**
	 * @Title: getPartitionWaterByCustomerList
	 * @Description:根据客户ID查询分水量记录
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<PartitionWater> getPartitionWaterByCustomerList(Long customerId, String period);
	
	public List<PartitionWater> getPartWaterMessage(String period, Long customerId, String traceIds);
	
	/**
	 * @Title: delete
	 * @Description: 删除分水量
	 * @param partWaterIdList
	 * @return 
	 */
	public int delete(List<Long> partWaterIdList);
	
	/**
	 * @Title: getCustomerPartWater
	 * @Description: 根据表计ID集合，客户。期间查询
	 * @param meterIdList
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<PartitionWater> getCustomerPartWater(List<Long> meterIdList , Long customerId, String period);
	
	
	/**
	 * @Title: getCustomerPastPartWater
	 * @Description: 查询客户往期分水量记录
	 * @param meterIdList
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<PartitionWater> getCustomerPastPartWater(List<Long> meterIdList , Long customerId, String period);
	
	/**
	 * @Title: searchCustomerAccountItemErrorFee
	 * @Description: 获取异常水费
	 * @param operatorId
	 * @param searchCond
	 * @param traceIds
	 * @param isMakeBill
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @param period
	 * @return 
	 */
	public List<Map<String, Object>> searchCustomerAccountItemErrorFee(Long operatorId, String searchCond, String traceIds, Integer isMakeBill, Integer isPartWater,  String startDate, String endDate, String period);
	
	/**
	 * @Title: getCardMeterAmount
	 * @Description: 获取卡表金额
	 * @param period
	 * @return 
	 */
	//public BigDecimal getCardMeterAmount(String period);
	
	//-------------------------生成分水量部分-------------------------------------------------------------------------------------------------------------------------------
	/**
	 * @Title: generatorPartitionWater
	 * @Description: 生成分水量
	 * @param meterRecord
	 * @return 
	 */
	public List<Long> generatorPartitionWater(MeterRecord meterRecord);
}
