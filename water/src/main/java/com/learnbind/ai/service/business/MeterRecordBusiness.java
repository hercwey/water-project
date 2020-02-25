package com.learnbind.ai.service.business;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.model.iotbean.report.MeterReportBean;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.business
 *
 * @Title: MeterRecordBusiness.java
 * @Description: 抄表记录业务层处理
 *
 * @author SRD
 * @date 2020年2月23日 下午5:08:00
 * @version V1.0 
 *
 */
@Service
public class MeterRecordBusiness {

	/**
	 * @Title: getCurrReadMeter
	 * @Description: 获取本次表底
	 * @param meterReport
	 * @return 
	 */
	public BigDecimal getCurrReadMeter(MeterReportBean meterReport) {
		Integer totalVolume = meterReport.getTotalVolume();//累计使用量整数, (用水量(M3) = totalVolume * sampleUnit)
		String sampleUnit = meterReport.getSampleUnit();//采样参数：单位M3
		
		BigDecimal readMeter = BigDecimalUtils.multiply(new BigDecimal(totalVolume), new BigDecimal(sampleUnit));
		return readMeter;
	}
	
}
