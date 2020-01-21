package com.learnbind.ai.service.meters;

import java.util.List;

import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterPartWaterRuleService.java
 * @Description: 表计分水量规则service服务类
 *
 * @author Administrator
 * @date 2019年9月5日 上午9:38:21
 * @version V1.0 
 *
 */
public interface MeterPartWaterRuleService extends IBaseService<MeterPartWaterRule, Long> {
	
	/**
	 * @Title: delete
	 * @Description: 根据表计删除分水量规则
	 * @param meterId
	 * @return 
	 */
	public int delete(Long meterId);
	
	/**
	 * @Title: insert
	 * @Description: 批量增加
	 * @param meterId
	 * @param partWaterRuleList
	 * @return 
	 */
	public int insert(Long meterId, List<MeterPartWaterRule> partWaterRuleList);
	
	/**
	 * @Title: getMeterRule
	 * @Description: 获取表计规则
	 * @param meterId
	 * @return 
	 */
	public List<MeterPartWaterRule> getMeterRule(Long meterId);
	
	/**
	 * @Title: getPWaterRuleTraceByMeterId
	 * @Description: 根据表计ID获取分水量规则配置日志
	 * @param meterId
	 * @return 
	 */
	public List<MeterPartWaterRule> getPWaterRuleTraceByMeterId(Long meterId);
	
	/**
	 * @Title: getListByRuleReal
	 * @Description: 查询RULE_REAL字段包含meterIdRule的所有记录
	 * @param meterIdRule
	 * @return 
	 */
	public List<MeterPartWaterRule> getListByRuleReal(String meterIdRule);
	
}
