package com.learnbind.ai.dao;

import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.WmDevice;
import tk.mybatis.mapper.common.Mapper;

public interface WmDeviceMapper extends Mapper<WmDevice> {
	
	int save(DeviceBean device);
    int updateById(DeviceBean device);
    int updateByDeviceId(DeviceBean device);
    WmDevice getDeviceByDeviceId(DeviceBean device);
}