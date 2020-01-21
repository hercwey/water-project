package com.learnbind.ai.service.meters;

import java.util.List;

import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.service.common.IBaseService;
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterPartWaterRuleTraceService.java
 * @Description: 分水量规则配置日志表
 *
 * @author Thinkpad
 * @date 2019年10月5日 上午9:58:43
 * @version V1.0 
 *
 */
public interface MeterPartWaterRuleTraceService extends IBaseService<MeterPartWaterRuleTrace, Long> {
	
	/**
	 * @Title: insert
	 * @Description: 批量增加表计规则日志
	 * @param meterRuleTraceList
	 * @return 
	 */
	public int insert(List<MeterPartWaterRuleTrace> meterRuleTraceList);
	
	/**
	 * @Title: insertTrace
	 * @Description: 根据表计规则增加日志
	 * @param meterRecordId
	 * @param partWaterId
	 * @param meterRule
	 * @return 
	 */
	public int insertTrace(Long meterRecordId, Long partWaterId, MeterPartWaterRule meterRule);
	
	/**
	 * @Title: insertTrace
	 * @Description: 根据表计规则增加日志
	 * @param meterRecordId
	 * @param meterRule
	 * @return 
	 */
	public int insertTrace(Long meterRecordId, MeterPartWaterRule meterRule);
	
	/**
	 * @Title: insertTraceList
	 * @Description: 根据表计规则增加日志
	 * @param meterRecordId
	 * @param meterRuleList
	 * @return 
	 */
	public int insertTraceList(Long meterRecordId, List<MeterPartWaterRule> meterRuleList);
	
	/**
	 * @Title: updatePartWaterIdByRecordId
	 * @Description: 根据抄表记录ID修改表计规则日志表中的分水量ID
	 * @param meterRecordId
	 * @param partWaterId
	 * @return 
	 */
	public int updatePartWaterIdByRecordId(Long meterRecordId, Long partWaterId);
	
	/**
	 * @Title: updatePartWaterId
	 * @Description: 根据主键ID修改规则日志表中的分水量ID
	 * @param id
	 * @param partWaterId
	 * @return 
	 */
	public int updatePartWaterId(Long id, Long partWaterId);
	
	
	/**
	 * @Title: getPWaterRuleTraceByMeterRecordId
	 * @Description: 根据抄表记录ID获取分水量规则配置日志
	 * @param meterId
	 * @return 
	 */
	public List<MeterPartWaterRuleTrace> getPWaterRuleTraceByMeterRecordId(Long meterReocrdId);
	
	
	/**
	 * @Title: getPWaterRuleTraceByPartWaterId
	 * @Description: 根据分水量ID获取分水量规则配置日志
	 * @param partWaterId
	 * @return 
	 */
	public List<MeterPartWaterRuleTrace> getPWaterRuleTraceByPartWaterId(Long partWaterId);

}
