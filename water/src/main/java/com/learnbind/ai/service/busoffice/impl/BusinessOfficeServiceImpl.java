package com.learnbind.ai.service.busoffice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.BusinessOfficeMapper;
import com.learnbind.ai.model.BusinessOffice;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.service.busoffice.BusinessOfficeService;
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
public class BusinessOfficeServiceImpl extends AbstractBaseService<BusinessOffice, Long> implements  BusinessOfficeService {
	
	@Autowired
	public BusinessOfficeMapper businessOfficeMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public BusinessOfficeServiceImpl(BusinessOfficeMapper mapper) {
		this.businessOfficeMapper=mapper;
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
	public List<BusinessOffice> searchCond(String searchCond) {
		return businessOfficeMapper.searchCond(searchCond);
	}


}
