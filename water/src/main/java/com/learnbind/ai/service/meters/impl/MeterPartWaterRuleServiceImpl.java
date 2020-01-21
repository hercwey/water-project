package com.learnbind.ai.service.meters.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.dao.MeterPartWaterRuleMapper;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MeterPartWaterRuleServiceImpl.java
 * @Description: 表计分水量表service服务实现类
 *
 * @author Administrator
 * @date 2019年9月5日 上午9:39:33
 * @version V1.0 
 *
 */
@Service
public class MeterPartWaterRuleServiceImpl extends AbstractBaseService<MeterPartWaterRule, Long> implements  MeterPartWaterRuleService {
	
	@Autowired
	public MeterPartWaterRuleMapper meterPartWaterRuleMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterPartWaterRuleServiceImpl(MeterPartWaterRuleMapper mapper) {
		this.meterPartWaterRuleMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public int delete(Long meterId) {
		MeterPartWaterRule record = new MeterPartWaterRule();
		record.setMeterId(meterId);
		return meterPartWaterRuleMapper.delete(record);
	}

	@Override
	@Transactional
	public int insert(Long meterId, List<MeterPartWaterRule> partWaterRuleList) {
		
		this.delete(meterId);
		
		int rows = 0;
		for(MeterPartWaterRule partWaterRule : partWaterRuleList) {
			rows = meterPartWaterRuleMapper.insertSelective(partWaterRule);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

	@Override
	public List<MeterPartWaterRule> getMeterRule(Long meterId) {
		MeterPartWaterRule searchObj = new MeterPartWaterRule();
		searchObj.setMeterId(meterId);
		searchObj.setIsDefault(EnumDefaultStatus.DEFAULT_YES.getValue());
		return meterPartWaterRuleMapper.select(searchObj);
	}
	
	@Override
	public List<MeterPartWaterRule> getPWaterRuleTraceByMeterId(Long meterId) {
		Example example = new Example(MeterPartWaterRule.class);
		example.createCriteria().andEqualTo("meterId", meterId).andEqualTo("isDefault", EnumDefaultStatus.DEFAULT_YES.getValue());
		List<MeterPartWaterRule> tempList = this.selectByExample(example);
		return tempList;
	}

	@Override
	public List<MeterPartWaterRule> getListByRuleReal(String meterIdRule) {
		Example example = new Example(MeterPartWaterRule.class);
		example.createCriteria().andLike("ruleReal", meterIdRule);
		return meterPartWaterRuleMapper.selectByExample(example);
	}
	
}
