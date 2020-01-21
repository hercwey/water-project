package com.learnbind.ai.service.checkmeter.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.dao.SysCheckMeterMapper;
import com.learnbind.ai.dao.SysCheckMeterResultMapper;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.model.SysCheckMeterResult;
import com.learnbind.ai.service.checkmeter.CheckMeterResultService;
import com.learnbind.ai.service.common.AbstractBaseService;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.checkmeter.impl
 *
 * @Title: CheckMeterServiceImpl.java
 * @Description: 服务实现
 *
 * @author Thinkpad
 * @date 2019年5月15日 下午5:35:59
 * @version V1.0 
 *
 */
@Service
public class CheckMeterResultServiceImpl extends AbstractBaseService<SysCheckMeterResult, Long> implements  CheckMeterResultService {
	
	@Autowired
	public SysCheckMeterResultMapper sysCheckMeterResultMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CheckMeterResultServiceImpl(SysCheckMeterResultMapper mapper) {
		this.sysCheckMeterResultMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCheckMeter
	 * @Description: 根据条件查询检测配置
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.checkmeter.CheckMeterService#searchCheckMeter(java.lang.String)
	 */
	@Override
	public List<SysCheckMeterResult> searchCond(String searchCond) {
		return sysCheckMeterResultMapper.searchCond(searchCond);
	}

	@Override
	public List<SysCheckMeterResult> getList(String searchCond, Long meterId, Integer checkType) {
		return sysCheckMeterResultMapper.getList(searchCond, meterId, checkType);
	}

	
}
