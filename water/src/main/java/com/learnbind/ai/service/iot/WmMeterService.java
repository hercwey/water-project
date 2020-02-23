package com.learnbind.ai.service.iot;

import com.learnbind.ai.model.iot.WmMeter;
import com.learnbind.ai.model.iotbean.report.AutoReport;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.iot
 *
 * @Title: WmMeterService.java
 * @Description: 设备上报数据服务接口类
 *
 * @author SRD
 * @date 2020年2月22日 下午2:31:31
 * @version V1.0 
 *
 */
public interface WmMeterService extends IBaseService<WmMeter, Long> {
	
	/**
	 * @Title: save
	 * @Description: 保存上报数据到数据库
	 * @param reportData
	 * @return 
	 */
	public int save(AutoReport reportData);
	
}
