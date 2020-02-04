package com.learnbind.ai.dao;

import com.learnbind.ai.model.iot.MeterBean;
import com.learnbind.ai.model.iot.WmMeter;
import tk.mybatis.mapper.common.Mapper;

public interface WmMeterMapper extends Mapper<WmMeter> {
	
	int save(MeterBean meter);
    int updateById(MeterBean meter);
}