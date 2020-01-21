package com.learnbind.ai.service.trace.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.UseWaterPriceTraceMapper;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.UseWaterPriceTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.trace.UseWaterPriceTraceService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.trace.impl
 *
 * @Title: UseWaterPriceTraceServiceImpl.java
 * @Description: 使用水价日志服务实现类
 *
 * @author Administrator
 * @date 2019年8月28日 上午10:33:15
 * @version V1.0 
 *
 */
@Service
public class UseWaterPriceTraceServiceImpl extends AbstractBaseService<UseWaterPriceTrace, Long> implements  UseWaterPriceTraceService {
	
	@Autowired
	public UseWaterPriceTraceMapper useWaterPriceTraceMapper;
	@Autowired
	public WaterPriceService waterPriceService;//水价管理表

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public UseWaterPriceTraceServiceImpl(UseWaterPriceTraceMapper mapper) {
		this.useWaterPriceTraceMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public int updateAccountItemId(Long partitionWaterId, Long accountItemId) {
		//条件
		Example example = new Example(UseWaterPriceTrace.class);
		example.createCriteria().andEqualTo("partitionWaterId", partitionWaterId);
		//更新内容
		UseWaterPriceTrace record = new UseWaterPriceTrace();
		record.setAccountItemId(accountItemId);
		return useWaterPriceTraceMapper.updateByExampleSelective(record, example);
	}

	@Override
	public int inserTrace(Long waterPriceId, Long accountItemId) {
		SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(waterPriceId);
		//添加日志
		UseWaterPriceTrace trace = new UseWaterPriceTrace();
		trace.setPriceTypeCode(waterPrice.getPriceTypeCode());//价格类型编码
		trace.setPriceTypeName(waterPrice.getPriceTypeName());//价格类型
		trace.setPriceCode(waterPrice.getPriceCode());//价格编码
		trace.setLadderName(waterPrice.getLadderName());//价格名称
		trace.setLadderStart(waterPrice.getLadderStart());//阶梯初值
		trace.setLadderEnd(waterPrice.getLadderEnd());//阶梯终值
		trace.setBasePrice(waterPrice.getBasePrice());//基础水价
		trace.setTreatmentFee(waterPrice.getTreatmentFee());//污水处理费
		trace.setTotalPrice(waterPrice.getTotalPrice());//合计水价
		trace.setRemark(waterPrice.getRemark());//备注
		trace.setAccountItemId(accountItemId);//账单id
		
		int rows = this.insertSelective(trace);
		return rows;
	}
	
}
