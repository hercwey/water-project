package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.MeterRecord;

import tk.mybatis.mapper.common.Mapper;

public interface MeterRecordMapper extends Mapper<MeterRecord> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
//	public List<MeterRecord> searchMeterRecord(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: getListGroupByCustomer
	 * @Description: 根据条件查询
	 * @param customerId
	 * @param searchCond
	 * @param readType
	 * @param operatorId
	 * @param traceIds
	 * @param startDate
	 * @param endDate
	 * @return 
	 */
	public List<Map<String, Object>> getMeterRecordList(@Param("customerId") Long customerId, @Param("searchCond") String searchCond, @Param("readType") Integer readType, @Param("operatorId") Long operatorId, @Param("traceIds") String traceIds, @Param("period") String period, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("meterRecordStatus") Integer meterRecordStatus, @Param("currAmount") Integer currAmount);
	
	public Map<String, Object> getMeterRecorStatisticMap(@Param("customerId") Long customerId, @Param("searchCond") String searchCond, @Param("readType") Integer readType, @Param("operatorId") Long operatorId, @Param("traceIds") String traceIds, @Param("period") String period, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("meterRecordStatus") Integer meterRecordStatus, @Param("currAmount") Integer currAmount);
	
	/**
	 * @Title: searchMeterRecord
	 * @Description: 查询抄表记录
	 * @param period
	 * @param traceIds
	 * @param readType
	 * @param searchCond
	 * @return 
	 */
	public List<MeterRecord> searchMeterRecord(@Param("period") String period, @Param("traceIds") String traceIds, @Param("readType") Integer readType, @Param("searchCond") String searchCond);
	
	/**
	 * @Title: getListGroupByCustomerAndPeriod
	 * @Description: 根据客户ID、期间和条件查询集合并按钮客户和期间分组，客户ID和期间为null时，只根据条件查询
	 * @param customerId
	 * @param period
	 * @param searchCond
	 * @param readType		抄表类型：0=正常抄表；1=估抄；2=预抄；
	 * @param operatorId	记录操作员ID
	 * @return
	 * 		[{"RECORD_IDS":"1,2","METER_IDS":"3,4","CUSTOMER_ID":"1","PERIOD":"2019-06","PROP":"张三"},...] 
	 */
	public List<Map<String, Object>> getListGroupByCustomerAndPeriod(@Param("customerId") Long customerId, @Param("period") String period, @Param("searchCond") String searchCond, @Param("readType") Integer readType, @Param("operatorId") Long operatorId, @Param("traceIds") String traceIds, @Param("isAddSubWater") Integer isAddSubWater, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate);
	
	/**
	 * @Title: getListByRecordIdList
	 * @Description: 根据抄表记录ID集合查询查询抄表记录
	 * @param recordIdArr
	 * @return 
	 */
	public List<MeterRecord> getListByRecordIdList(@Param("recordIdArr") String[] recordIdArr);
	
	/**
     * @Title: getTotalWaterAmount
     * @Description: 查询年总用水量
     * @param customerId
     * @param meterId
     * @param year
     * @return 
     */
    public String getTotalWaterAmount(@Param("customerId") Long customerId, @Param("meterId") Long meterId, @Param("year") String year);
    

    /**
 	 * @Title: getMeterBookIdByName
 	 * @Description: 查询小区水费通知单
 	 * @param name
 	 * @return 
 	 */
 	public List<Map<String, Object>> getMeterAmountInfo(@Param("traceIds") String traceIds, @Param("period") String period, @Param("debitCredit") String debitCredit, @Param("creditSubject") String creditSubject, @Param("arrearsStatus") Integer arrearsStatus);
 	
 	
 	 /**
 	 * @Title: getMeterBookIdByName
 	 * @Description:查询单位水费通知单
 	 * @param name
 	 * @return 
 	 */
 	public List<Map<String, Object>> getMeterAmountCompanyInfo(@Param("accountItemId") Long accountItemId, @Param("debitCredit") String debitCredit, @Param("creditSubject") String creditSubject, @Param("arrearsStatus") Integer arrearsStatus);
 	
 	
 	/**
	 * @Title: getListGroupByCustomerAndPeriod
	 * @Description: 根据客户ID、期间和条件查询集合并按钮客户和期间分组，客户ID和期间为null时，只根据条件查询，异常水量提醒（固定值）
	 * @param customerId
	 * @param period
	 * @param searchCond
	 * @param readType		抄表类型：0=正常抄表；1=估抄；2=预抄；
	 * @param operatorId	记录操作员ID
	 * @return
	 * 		[{"RECORD_IDS":"1,2","METER_IDS":"3,4","CUSTOMER_ID":"1","PERIOD":"2019-06","PROP":"张三"},...] 
	 */
	public List<Map<String, Object>> getListGroupByCustomerAndPeriodErrorAmount(@Param("customerId") Long customerId, @Param("searchCond") String searchCond, @Param("readType") Integer readType, @Param("operatorId") Long operatorId, @Param("traceIds") String traceIds, @Param("period") String period, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate, @Param("meterRecordStatus") Integer meterRecordStatus, @Param("currAmount") Integer currAmount);
	
	 /**
     * @Title: selectNewestMeterRecord
     * @Description: 查询最新的抄表记录
     * @param customerId
     * @param meterId
     * @return 
     */
    public MeterRecord selectNewestMeterRecord(@Param("customerId") Long customerId, @Param("meterId") Long meterId);
    
    /**
     * @Title: getLastMeterRecord
     * @Description: 查询最后一次抄表记录
     * @param customerId
     * @param period
     * @param meterId
     * @return 
     */
    public MeterRecord getLastMeterRecord(@Param("customerId") Long customerId, @Param("period") String period, @Param("meterId") Long meterId);
    
    /**
	 * @Title: getListByCustomerIdAndPeriod
	 * @Description: 获取异常水量的抄表记录
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<MeterRecord> getListByCustomerIdAndPeriodErrorAmount(@Param("customerId") Long customerId, @Param("period") String period);
	
	/**
	 * @Title: getExportMeterRecordData
	 * @Description: 抄表记录导出时查询数据
	 * @param searchCond
	 * @param readType
	 * @param traceIds
	 * @param period
	 * @return 
	 */
	public List<Map<String, Object>> getExportMeterRecordData(@Param("searchCond") String searchCond, @Param("readType") Integer readType, @Param("traceIds") String traceIds, @Param("period") String period, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate,@Param("currAmount") Integer currAmount);
	
	public List<MeterRecord> getMeterRecordData(@Param("searchCond") String searchCond, @Param("readType") Integer readType, @Param("traceIds") String traceIds, @Param("period") String period, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate);
    
	/**
	 * @Title: getListByCustomerIdAndPeriod
	 * @Description: 根据客户和地理位置获取抄表记录
	 * @param customerId
	 * @param period
	 * @param traceIds
	 * @return 
	 */
	public List<MeterRecord> getListByCustomerIdAndPeriod(@Param("customerId") Long customerId, @Param("period") String period, @Param("traceIds") String traceIds);
	
	public List<MeterRecord> getMeterRecordByMeter(@Param("period") String period, @Param("meterId") Long meterId);
	
	/**
	 * @Title: getCustomerMeterRecord
	 * @Description: 生成通知单时查询抄表记录
	 * @param meterIdList
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<MeterRecord> getCustomerMeterRecord(@Param("meterIdList") List<Long> meterIdList , @Param("customerId") Long customerId, @Param("period") String period);
	
	
	/**
	 * @Title: getMeterRecordMaxPeriod
	 * @Description: 获取抄表记录中的最大期间
	 * @param searchCond
	 * @param traceIds
	 * @param meterId
	 * @return 
	 */
	public String getMeterRecordMaxPeriod(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("meterId") Long meterId);
	
	public List<Map<String, Object>> getExportMeterRecordErrorAmountData(@Param("searchCond") String searchCond, @Param("readType") Integer readType, @Param("traceIds") String traceIds, @Param("period") String period, @Param("isPartWater") Integer isPartWater, @Param("startDate") String startDate, @Param("endDate") String endDate,@Param("currAmount") Integer currAmount);
}