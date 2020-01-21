package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterRecordTemp;
import tk.mybatis.mapper.common.Mapper;

public interface MeterRecordTempMapper extends Mapper<MeterRecordTemp> {
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<MeterRecordTemp> searchAppMeterRecord(@Param("searchCond")String searchCond, @Param("operatorId")Long operatorId);
	
	
	
	/**
	 * @Title: getListGroupByCustomerAndPeriod
	 * @Description: 根据客户ID和条件查询集合并按钮客户和期间分组，客户ID为null时，只根据条件查询
	 * @param customerId
	 * @param period
	 * @param searchCond
	 * @param readType		抄表类型：0=正常抄表；1=估抄；2=预抄；
	 * @param operatorId	记录操作员ID
	 * @return
	 * 		[{"RECORD_IDS":"1,2","METER_IDS":"3,4","CUSTOMER_ID":"1","PERIOD":"2019-06","PROP":"张三"},...] 
	 */
	public List<Map<String, Object>> getListGroupByCustomer(@Param("customerId") Long customerId, @Param("searchCond") String searchCond, @Param("readResult") Integer readResult, @Param("readMode") String readMode, @Param("operatorId") Long operatorId, @Param("traceIds") String traceIds, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	
	/**
	 * @Title: getConfirmAppMeterRecord
	 * @Description: 条件查询确认的app抄表记录
	 * @param customerId
	 * @param searchCond
	 * @param readType
	 * @param operatorId
	 * @param traceIds
	 * @return 
	 */
	public List<MeterRecordTemp> getConfirmAppMeterRecord(@Param("customerId") Long customerId, @Param("searchCond") String searchCond, @Param("readResult") Integer readResult, @Param("readMode") String readMode, @Param("operatorId") Long operatorId, @Param("traceIds") String traceIds);
	
	
	/**
	 * @Title: getExportMeterRecordTempData
	 * @Description: 查询要导出的APP抄表数据
	 * @param searchCond
	 * @param readResult
	 * @param traceIds
	 * @param readMode
	 * @return 
	 */
	public List<Map<String, Object>> getExportMeterRecordTempData(@Param("searchCond") String searchCond, @Param("readResult") Integer readResult, @Param("traceIds") String traceIds, @Param("readMode") Integer readMode);
	
	/**
	 * @Title: getRepeatListGroupByCustomer
	 * @Description: 查询重复的APP抄表记录数据
	 * @param customerId
	 * @param searchCond
	 * @param readResult
	 * @param readMode
	 * @param operatorId
	 * @param traceIds
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> getRepeatListGroupByCustomer(@Param("customerId") Long customerId, @Param("searchCond") String searchCond, @Param("readResult") Integer readResult, @Param("readMode") String readMode, @Param("operatorId") Long operatorId, @Param("traceIds") String traceIds, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: getExportMoreMeterRecordTempData
	 * @Description: 导出一户多表客户数据
	 * @param searchCond
	 * @param readResult
	 * @param traceIds
	 * @param readMode
	 * @return 
	 */
	public List<Map<String, Object>> getExportMoreMeterRecordTempData(@Param("searchCond") String searchCond, @Param("readResult") Integer readResult, @Param("traceIds") String traceIds, @Param("readMode") Integer readMode);
}