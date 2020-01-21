package com.learnbind.ai.service.statistic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.DrainWaterRecordMapper;
import com.learnbind.ai.model.DrainWaterRecord;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.statistic.DrainWaterRecordService;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     WaterPriceServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class DrainWaterRecordServiceImpl extends AbstractBaseService<DrainWaterRecord, Long> implements  DrainWaterRecordService {
	
	@Autowired
	public DrainWaterRecordMapper drainWaterRecordMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public DrainWaterRecordServiceImpl(DrainWaterRecordMapper mapper) {
		this.drainWaterRecordMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchPrinter
	 * @Description: 根据条件查询打印机
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.printer.PrinterService#searchPrinter(java.lang.String)
	 */
	@Override
	public List<DrainWaterRecord> searchCond(String searchCond, String period) {
		return drainWaterRecordMapper.searchCond(searchCond, period);
	}
	


}
