package com.learnbind.ai.service.abnormalcfg.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.AbnormalFeeMapper;
import com.learnbind.ai.model.AbnormalFee;
import com.learnbind.ai.service.abnormalcfg.AbnormalFeeService;
import com.learnbind.ai.service.common.AbstractBaseService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.abnormalcfg.impl
 *
 * @Title: AbnormalFeeServiceImpl.java
 * @Description: 异常水费报警配置服务实现类
 *
 * @author Administrator
 * @date 2019年5月16日 上午10:14:02
 * @version V1.0 
 *
 */
@Service
public class AbnormalFeeServiceImpl extends AbstractBaseService<AbnormalFee, Long> implements AbnormalFeeService {
	
	@Autowired
	public AbnormalFeeMapper abnormalFeeMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public AbnormalFeeServiceImpl(AbnormalFeeMapper mapper) {
		this.abnormalFeeMapper=mapper;
		this.setMapper(mapper);
	}

}
