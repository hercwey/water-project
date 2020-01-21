package com.learnbind.ai.service.abnormalcfg.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.AbnormalQuantityMapper;
import com.learnbind.ai.model.AbnormalQuantity;
import com.learnbind.ai.service.abnormalcfg.AbnormalQuantityService;
import com.learnbind.ai.service.common.AbstractBaseService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.abnormalcfg.impl
 *
 * @Title: AbnormalQuantityServiceImpl.java
 * @Description: 异常水量报警配置服务实现类
 *
 * @author Administrator
 * @date 2019年5月16日 上午10:14:02
 * @version V1.0 
 *
 */
@Service
public class AbnormalQuantityServiceImpl extends AbstractBaseService<AbnormalQuantity, Long> implements AbnormalQuantityService {
	
	@Autowired
	public AbnormalQuantityMapper abnormalQuantityMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public AbnormalQuantityServiceImpl(AbnormalQuantityMapper mapper) {
		this.abnormalQuantityMapper=mapper;
		this.setMapper(mapper);
	}

}
