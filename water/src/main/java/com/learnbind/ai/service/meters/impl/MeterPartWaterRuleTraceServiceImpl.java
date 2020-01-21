package com.learnbind.ai.service.meters.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.dao.MeterPartWaterRuleTraceMapper;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MeterPartWaterRuleTraceServiceImpl.java
 * @Description: 分水量规则配置日志表
 *
 * @author Thinkpad
 * @date 2019年10月5日 上午9:59:50
 * @version V1.0 
 *
 */
@Service
public class MeterPartWaterRuleTraceServiceImpl extends AbstractBaseService<MeterPartWaterRuleTrace, Long> implements  MeterPartWaterRuleTraceService {
	
	@Autowired
	public MeterPartWaterRuleTraceMapper meterPartWaterRuleTraceMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterPartWaterRuleTraceServiceImpl(MeterPartWaterRuleTraceMapper mapper) {
		this.meterPartWaterRuleTraceMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	@Transactional
	public int insert(List<MeterPartWaterRuleTrace> meterRuleTraceList) {
		int rows = 0;
		for(MeterPartWaterRuleTrace trace : meterRuleTraceList) {
			rows = meterPartWaterRuleTraceMapper.insertSelective(trace);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

	@Override
	public int insertTrace(Long meterRecordId, Long partWaterId, MeterPartWaterRule meterRule) {
		MeterPartWaterRuleTrace trace = this.getMeterPartWaterRuleTrace(meterRecordId, meterRule);
		trace.setPartWaterId(partWaterId);
		int rows = meterPartWaterRuleTraceMapper.insertSelective(trace);
		return rows;
	}
	
	@Override
	public int insertTrace(Long meterRecordId, MeterPartWaterRule meterRule) {
		MeterPartWaterRuleTrace trace = this.getMeterPartWaterRuleTrace(meterRecordId, meterRule);
		int rows = meterPartWaterRuleTraceMapper.insertSelective(trace);
		return rows;
	}
	
	@Override
	public int insertTraceList(Long meterRecordId, List<MeterPartWaterRule> meterRuleList) {
		int rows = 0;
		for(MeterPartWaterRule meterRule : meterRuleList) {
			rows = this.insertTrace(meterRecordId, meterRule);
			if(rows<=0) {
				break;
			}
		}
		return rows;
	}
	
	/**
	 * @Title: getMeterPartWaterRuleTrace
	 * @Description: 获取表计规则实体类
	 * @param meterRecordId
	 * @param meterRule
	 * @return 
	 */
	private MeterPartWaterRuleTrace getMeterPartWaterRuleTrace(Long meterRecordId, MeterPartWaterRule meterRule) {
		MeterPartWaterRuleTrace trace = new MeterPartWaterRuleTrace();
		trace.setRuleType(meterRule.getRuleType());
		trace.setRuleShow(meterRule.getRuleShow());
		trace.setRuleReal(meterRule.getRuleReal());
		trace.setMeterId(meterRule.getMeterId());
		trace.setWaterPriceId(meterRule.getWaterPriceId());
		trace.setIsDefault(meterRule.getIsDefault());
		trace.setMeterRecordId(meterRecordId);
		//trace.setPartWaterId(partWaterId);
		return trace;
	}

	@Override
	public int updatePartWaterIdByRecordId(Long meterRecordId, Long partWaterId) {
		//条件
		Example example = new Example(MeterPartWaterRuleTrace.class);
		example.createCriteria().andEqualTo("meterRecordId", meterRecordId);
		//更新内容
		MeterPartWaterRuleTrace trace = new MeterPartWaterRuleTrace();
		trace.setPartWaterId(partWaterId);
		return meterPartWaterRuleTraceMapper.updateByExampleSelective(trace, example);
	}
	
	@Override
	public int updatePartWaterId(Long id, Long partWaterId) {
		MeterPartWaterRuleTrace trace = new MeterPartWaterRuleTrace();
		trace.setId(id);
		trace.setPartWaterId(partWaterId);
		return meterPartWaterRuleTraceMapper.updateByPrimaryKeySelective(trace);
	}
	
	@Override
	public List<MeterPartWaterRuleTrace> getPWaterRuleTraceByMeterRecordId(Long meterReocrdId) {
		Example example = new Example(MeterPartWaterRuleTrace.class);
		example.createCriteria().andEqualTo("meterRecordId", meterReocrdId).andEqualTo("isDefault", EnumDefaultStatus.DEFAULT_YES.getValue());
		List<MeterPartWaterRuleTrace> tempList = this.selectByExample(example);
		return tempList;
	}

	@Override
	public List<MeterPartWaterRuleTrace> getPWaterRuleTraceByPartWaterId(Long partWaterId) {
		Example example = new Example(MeterPartWaterRuleTrace.class);
		example.createCriteria().andEqualTo("partWaterId", partWaterId).andEqualTo("isDefault", EnumDefaultStatus.DEFAULT_YES.getValue());
		List<MeterPartWaterRuleTrace> tempList = this.selectByExample(example);
		return tempList;
	}

}
