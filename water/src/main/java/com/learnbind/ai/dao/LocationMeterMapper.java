package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.LocationMeter;

import tk.mybatis.mapper.common.Mapper;

public interface LocationMeterMapper extends Mapper<LocationMeter> {
	
	/**
	 * @Title: getListByMeterIdList
	 * @Description: 根据表计ID集合查询地理位置-表计
	 * @param meterIdList
	 * @return 
	 */
	public List<LocationMeter> getListByMeterIdList(@Param("meterIdList") List<Long> meterIdList);
	
	/**
	 * @param meterId
	 * @return
	 * 			根据表计id查询地理位置
	 */
	public LocationMeter getLocationByMeterId(@Param("meterId") Long meterId);
	
	/**
	 * @Title: updateTraceIds
	 * @Description: 更新地理位置表计表
	 * @return 
	 */
	public int updateTraceIds();
	
}