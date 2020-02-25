package com.learnbind.ai.service.meterrecord;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface MeterRecordService extends IBaseService<MeterRecord, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
//	public List<MeterRecord> searchMeterRecord(String searchCond);
	
	/**
	 * @Title: getMeterRecord
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
	public List<Map<String, Object>> getMeterRecordList(Long customerId, String searchCond, Integer readType, Long operatorId, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer meterRecordStatus, Integer currAmount);
	
	/**
	 * @Title: getMeterRecorStatisticMap
	 * @Description: 获取统计数据
	 * @param customerId
	 * @param searchCond
	 * @param readType
	 * @param operatorId
	 * @param traceIds
	 * @param period
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @param meterRecordStatus
	 * @return 
	 */
	public Map<String, Object> getMeterRecorStatisticMap(Long customerId, String searchCond, Integer readType, Long operatorId, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer meterRecordStatus, Integer currAmount);
	
	/**
	 * @Title: searchMeterRecord
	 * @Description: 查询抄表记录
	 * @param period
	 * @param traceIds
	 * @param readType
	 * @param searchCond
	 * @return 
	 */
	public List<MeterRecord> searchMeterRecord(String period, String traceIds, Integer readType, String searchCond);
	
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
	public List<Map<String, Object>> getListGroupByCustomerAndPeriod(Long customerId, String period, String searchCond, Integer readType, Long operatorId, String traceIds, Integer isAddSubWater, Integer isPartWater, String startDate, String endDate);
	
	/**
	 * @Title: getListByCustomerIdAndPeriod
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<MeterRecord> getListByCustomerIdAndPeriod(Long customerId, String period, String traceIds);
	
	/**
	 * @Title: getListByRecordIdList
	 * @Description: 根据抄表记录ID集合查询查询抄表记录
	 * @param recordIdArr
	 * @return 
	 */
	public List<MeterRecord> getListByRecordIdList(String[] recordIdArr);
	
	/**
	 * @Title: getLastMeterRecord
	 * @Description: 根据条件查询最后一次抄表记录
	 * @param customerId	客户ID，不能为null
	 * @param period	期间，表计ID为null时，此值不能为null；表计ID不为null时，此值可以为null；此值不为null时表示查询此期间的最后一条抄表记录
	 * @param meterId	表计ID，期间为null时，此值不能为null；期间不为null时，此值可以为null
	 * @return 
	 * 		返回查询到的最后一条抄表记录
	 */
	public MeterRecord getLastMeterRecord(Long customerId, String period, Long meterId);
	
    /**
     * @Title: insertMeterRecord
     * @Description: 增加抄表记录
     * @param record
     * @param operatorId	操作人ID
     * @param operatorName	操作人名称
     * @return 
     */
    public int insertMeterRecord(MeterRecord record, Long operatorId, String operatorName);
    
    /**
     * @Title: getMeterUse
     * @Description: 获取表计用途
     * @param meterId
     * @return 
     */
    public String getMeterUse(Long meterId);
    
    /**
     * @Title: getMeterTreeId
     * @Description: 获取表计父子关系ID
     * @param meterId
     * @return 
     */
    public Long getMeterTreeId(Long meterId);
    
    /**
     * 修改
     * @param record
     * @return
     */
    public int updateMeterRecord(MeterRecord record);
    
    /**
     * 删除
     * @param recordId
     * @return
     */
    public int deleteMeterRecord(long recordId);
    
    /**
     * @Title: deleteBatch
     * @Description: 批量删除
     * @param recordIdsJSON	删除抄表记录ID的JSON字符串
     * @return 
     */
    public int deleteBatch(String recordIdsJSON);

    /** 
    	* @Title: findAllMeterRecord
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<MeterRecord> findAllMeterRecord(int pageNum, int pageSize);
    
    /**
     * @Title: initMeterRecord
     * @Description: 初始化抄表记录
     * @param operatorId
     * @param operatorName
     * @return 
     */
    public int initMeterRecord(Long operatorId, String operatorName);
    
    /**
     * @Title: getTotalWaterAmount
     * @Description: 查询年总水量
     * @param customerId
     * @param meterId
     * @param year
     * @return 
     */
    public String getTotalWaterAmount(Long customerId, Long meterId, String year);
    
    /**
   	 * @Title: getMeterBookIdByName
   	 * @Description: 查询水费单(小区)
   	 * @param name
   	 * @return 
   	 */
   	public List<Map<String, Object>> getMeterAmountInfo(String traceIds, String period);
   	
   	/**
   	 * @Title: getMeterBookIdByName
   	 * @Description: 查询水费单(单位)
   	 * @param name
   	 * @return 
   	 */
   	public List<Map<String, Object>> getMeterAmountCompanyInfo(Long accountItemId);
   	
   	
   	/**
	 * @Title: getListGroupByCustomerAndPeriod
	 * @Description: 根据客户ID、期间和条件查询集合并按钮客户和期间分组，客户ID和期间为null时，异常水量提醒（固定值）
	 * @param customerId
	 * @param period
	 * @param searchCond
	 * @param readType		抄表类型：0=正常抄表；1=估抄；2=预抄；
	 * @param operatorId	记录操作员ID
	 * @return 
	 * 		[{"RECORD_IDS":"1,2","METER_IDS":"3,4","CUSTOMER_ID":"1","PERIOD":"2019-06","PROP":"张三"},...]
	 */
	public List<Map<String, Object>> getListGroupByCustomerAndPeriodErrorAmount(Long customerId, String searchCond, Integer readType, Long operatorId, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer meterRecordStatus, Integer currAmount);
	
	
	 /**
     * @Title: insertInitMeterRecord
     * @Description: 初始化抄表记录（立户、过户、换表时调用）
     * @param record
     * @param operatorId	操作人ID
     * @param operatorName	操作人名称
     * @return 
     */
    public int insertInitMeterRecord(Long meterId, Long customerId, Long operatorId, String operatorName, String currRead);
    
    /**
     * @Title: insertInitMeterRecord
     * @Description: 增加初始化抄表记录，增加时删除当前时间以前且状态为初始化的抄表记录
     * @param meterId
     * @param customerId
     * @return 
     */
    public int insertInitMeterRecord(Long meterId, Long customerId);
    
    /**
     * @Title: insertInitMeterRecord
     * @Description: 增加初始化抄表记录（主要应用于数据导入）
     * @param meter
     * @param customerId
     * @param operatorId
     * @param operatorName
     * @param currRead
     * @return 
     */
    public int insertInitMeterRecord(Meters meter, Long customerId, Long operatorId, String operatorName, String currRead);
    
    /**
     * @Title: selectNewestMeterRecord
     * @Description: 查询最新的抄表记录
     * @param customerId
     * @param meterId
     * @return 
     */
    public MeterRecord selectNewestMeterRecord(Long customerId, Long meterId);
    
    /**
	 * @Title: getListByCustomerIdAndPeriod
	 * @Description: 获取异常水量的抄表记录
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<MeterRecord> getListByCustomerIdAndPeriodErrorAmount(Long customerId, String period);
	
	/**
	 * @Title: deleteMeterRecord
	 * @Description: 删除某客户某表计本次抄表日期之前的所有初始化记录
	 * @param customerId
	 * @param meterId
	 * @param currDate
	 * 			currDate为空时，默认为当前系统日期
	 * @return 
	 */
	public int deleteMeterRecord(Long customerId, Long meterId, Date currDate);
    
	
	/**
	 * @Title: getExportMeterRecordData
	 * @Description: 抄表记录导出时查询数据
	 * @param searchCond
	 * @param readType
	 * @param traceIds
	 * @param period
	 * @return 
	 */
	public List<Map<String, Object>> getExportMeterRecordData(String searchCond, Integer readType, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer currAmount);
	
	/**
	 * @Title: generatorPartWater
	 * @Description: 生成分水量
	 * @param recordList
	 * @return 
	 */
	public int generatorPartWater(List<MeterRecord> recordList);
	
	
	public List<MeterRecord> getMeterRecordData(String searchCond, Integer readType, String traceIds, String period, Integer isPartWater, String startDate, String endDate);
	/**
	 * @Title: batchGeneratorPartWater
	 * @Description: 批量生成分水量记录
	 * @param recordMapList
	 * @return 
	 * 		0=异常；1=成功；
	 */
	public int batchGeneratorPartWater(List<MeterRecord> recordList);
	
	/**
	 * @Title: getWaterAmount
	 * @Description: 获取水量
	 * @param period
	 * @param meterId
	 * @return 
	 */
	public BigDecimal getWaterAmount(String period, Long meterId);
	
	/**
	 * @Title: confirmVirtualMeter
	 * @Description: 确认虚表，生成分水量
	 * @param period
	 * @param traceIds
	 * @return 
	 */
	public int confirmVirtualMeter(String period, String traceIds);
	
	/**
	 * @Title: getWaterAmount
	 * @Description: 根据分水量规则表达式获取水量
	 * @param period
	 * @param partWaterRule
	 * @return 
	 */
	public BigDecimal getWaterAmount(String period, String partWaterRule);
	
	/**
	 * @Title: changeMeterVerify
	 * @Description: 换表验证
	 * @param meterId
	 * @return 
	 * 		true=允许换表，false=不允许换表
	 */
	public boolean changeMeterVerify(Long meterId);
	
	/**
	 * @Title: getMeterRecordByMeter
	 * @Description: 根据表计id期间获取所有的抄表记录
	 * @param period
	 * @param meterId
	 * @return 
	 */
	public List<MeterRecord> getMeterRecordByMeter(String period, Long meterId);
	
	
	/**
	 * @Title: getCustomerMeterRecord
	 * @Description: 生成通知单时查询抄表记录
	 * @param meterIdList
	 * @param customerId
	 * @param period
	 * @return 
	 */
	public List<MeterRecord> getCustomerMeterRecord(List<Long> meterIdList , Long customerId, String period);
	
	/**
	 * @Title: getMeterRecordMaxPeriod
	 * @Description: 获取抄表记录中的最大期间
	 * @param searchCond
	 * @param traceIds
	 * @param meterId
	 * @return 
	 */
	public String getMeterRecordMaxPeriod(String searchCond, String traceIds, Long meterId);
	
	/**
	 * @Title: getExportMeterRecordErrorAmountData
	 * @Description: 导出异常水量抄表记录
	 * @param searchCond
	 * @param readType
	 * @param traceIds
	 * @param period
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @param currAmount
	 * @return 
	 */
	public List<Map<String, Object>> getExportMeterRecordErrorAmountData(String searchCond, Integer readType, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer currAmount);
	
	//-------------------------------------------------------------------------------------------
	public MeterRecord saveMeterRecord(MeterRecord record, Long operatorId, String operatorName);
	
}
