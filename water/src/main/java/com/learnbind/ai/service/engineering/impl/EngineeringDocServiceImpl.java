package com.learnbind.ai.service.engineering.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.EngineeringDocMapper;
import com.learnbind.ai.model.EngineeringDoc;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.engineering.EngineeringDocService;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     WaterPriceServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.engineering.impl
 *
 * @Title: EngineeringDocServiceImpl.java
 * @Description: 工程单据服务实现类
 *
 * @author Administrator
 * @date 2019年8月4日 上午9:59:47
 * @version V1.0 
 *
 */
@Service
public class EngineeringDocServiceImpl extends AbstractBaseService<EngineeringDoc, Long> implements EngineeringDocService {
	
	@Autowired
	public EngineeringDocMapper engineeringDocMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public EngineeringDocServiceImpl(EngineeringDocMapper mapper) {
		this.engineeringDocMapper=mapper;
		this.setMapper(mapper);
	}

}
