package com.learnbind.ai.service.iot.impl;

import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WmMeterMapper;
import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.report.AutoReport;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.iot.WmMeterService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.iot.impl
 *
 * @Title: WmMeterServiceImpl.java
 * @Description: 设备上报数据服务实现类
 *
 * @author SRD
 * @date 2020年2月23日 下午2:33:19
 * @version V1.0 
 *
 */
@Service
public class WmMeterServiceImpl extends AbstractBaseService<WmMeter, Long> implements WmMeterService {

	public WmMeterMapper wmMeterMapper;

	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public WmMeterServiceImpl(WmMeterMapper mapper) {
		this.wmMeterMapper = mapper;
		this.setMapper(mapper);
	}

	@Override
	public int save(AutoReport reportData) {
		return 0;
	}
	
}
