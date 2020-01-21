package com.learnbind.ai.service.meterrecord;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.MeterRecordTemp;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meterrecord
 *
 * @Title: MeterRecordTempService.java
 * @Description: 抄表记录临时表服务接口类
 *
 * @author Administrator
 * @date 2019年6月19日 下午5:06:12
 * @version V1.0 
 *
 */
public interface MeterRecordTempService extends IBaseService<MeterRecordTemp, Long> {
	
	/**
	 * @Title: insertBatch
	 * @Description: 批量增加
	 * @param recordTempList
	 * @return 
	 */
	public int insertBatch(List<MeterRecordTemp> recordTempList);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<MeterRecordTemp> searchAppMeterRecord(String searchCond, Long operatorId);
    
    /**
     * @Title: deleteAppMeterRecord
     * @Description: 删除记录
     * @param id
     * @return 
     */
    public int deleteAppMeterRecord(long id);
    
    /**
     * @Title: confirmMeterRecord
     * @Description: 确认抄表记录
     * @param operatorId
     * @param operatorName
     * @param period
     * @return 
     */
    public int confirmMeterRecord(Long operatorId, String operatorName, List<MeterRecordTemp> appRecordList, String period);
    
	/**
	 * @Title: deleteAppMeterRecordList
	 * @Description: 删除app抄表记录数据
	 * @return 
	 */
	public Integer deleteAppMeterRecordList(List<MeterRecordTemp> appMeterRecordList);
	
	
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
	public List<Map<String, Object>> getListGroupByCustomer(Long customerId, String searchCond, Integer readResult,String readMode, Long operatorId, String traceIds, String startDate, String endDate);
	
	
	/**
	 * @Title: getListByCustomerIdAndPeriod
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<MeterRecordTemp> getListByCustomerId(Long customerId);
	
	/**
	 * @Title: getConfirmAppMeterRecord
	 * @Description: 条件app抄表记录
	 * @param customerId
	 * @param searchCond
	 * @param readType
	 * @param operatorId
	 * @param traceIds
	 * @return 
	 */
	public List<MeterRecordTemp> getConfirmAppMeterRecord(Long customerId, String searchCond, Integer readResult, String readMode, Long operatorId, String traceIds);
	
	
	/**
	 * @Title: getExportMeterRecordTempData
	 * @Description: 查询要导出的APP抄表数据
	 * @param searchCond
	 * @param readResult
	 * @param traceIds
	 * @param readMode
	 * @return 
	 */
	public List<Map<String, Object>> getExportMeterRecordTempData(String searchCond, Integer readResult, String traceIds, Integer readMode);
	
	/**
	 * @Title: getRepeatListGroupByCustomer
	 * @Description: 查询重复的app抄表记录数据
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
	public List<Map<String, Object>> getRepeatListGroupByCustomer(Long customerId, String searchCond, Integer readResult,String readMode, Long operatorId, String traceIds, String startDate, String endDate);
	
	
	
	/**
	 * @Title: getExportMoreMeterRecordTempData
	 * @Description: 导出一户多表客户数据
	 * @param searchCond
	 * @param readResult
	 * @param traceIds
	 * @param readMode
	 * @return 
	 */
	public List<Map<String, Object>> getExportMoreMeterRecordTempData(String searchCond, Integer readResult, String traceIds, Integer readMode);
}
