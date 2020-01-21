package com.learnbind.ai.service.engineering.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.LocationEngineeringMapper;
import com.learnbind.ai.model.LocationEngineering;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.engineering.LocationEngineeringService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.engineering.impl
 *
 * @Title: LocationEngineeringServiceImpl.java
 * @Description: 地理位置-工程关系服务实现类
 *
 * @author Administrator
 * @date 2019年8月8日 下午5:32:40
 * @version V1.0 
 *
 */
@Service
public class LocationEngineeringServiceImpl extends AbstractBaseService<LocationEngineering, Long> implements LocationEngineeringService {
	
	@Autowired
	public LocationEngineeringMapper locationEngineeringMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public LocationEngineeringServiceImpl(LocationEngineeringMapper mapper) {
		this.locationEngineeringMapper=mapper;
		this.setMapper(mapper);
	}

}
