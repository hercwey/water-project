package com.learnbind.ai.service.meters.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.dao.MeterStockTraceMapper;
import com.learnbind.ai.model.MeterStockTrace;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meters.MeterStockTraceService;
import com.learnbind.ai.service.meters.MetersService;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MeterChangeReceiptServiceImpl.java
 * @Description: 水表安装领用单
 *
 * @author Thinkpad
 * @date 2019年10月26日 下午12:06:07
 * @version V1.0 
 *
 */
@Service
public class MeterStockTraceServiceImpl extends AbstractBaseService<MeterStockTrace, Long> implements MeterStockTraceService {
	
	@Autowired
	public MeterStockTraceMapper meterStockTraceMapper;

	@Autowired
	public MetersService metersService;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterStockTraceServiceImpl(MeterStockTraceMapper mapper) {
		this.meterStockTraceMapper=mapper;
		this.setMapper(mapper);
	}


	@Override
	public List<MeterStockTrace> searchCond(String searchCond, Integer operationType) {
		return meterStockTraceMapper.searchCond(searchCond, operationType);
	}


	@Override
	public int insertTrace(Long meterId, Integer operatorType) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Meters meter = metersService.selectByPrimaryKey(meterId);
		MeterStockTrace trace = new MeterStockTrace();
		trace.setMeterId(meterId);
		trace.setMeterNo(meter.getMeterNo());
		trace.setCaliber(meter.getCaliber());
		trace.setFactory(meter.getFactory());
		trace.setOperatorTime(new Date());
		trace.setOperationType(operatorType);
		trace.setOperatorId(userBean.getId());
		trace.setOperatorName(userBean.getRealname());
		
		int rows = this.insertSelective(trace);
		return rows;
	}
	


}
