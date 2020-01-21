package com.learnbind.ai.dao;

import com.learnbind.ai.model.LocationCustomer;
import tk.mybatis.mapper.common.Mapper;

public interface LocationCustomerMapper extends Mapper<LocationCustomer> {
	
	/**
	 * @Title: updateTraceIds
	 * @Description: 更新地理位置表计表
	 * @return 
	 */
	public int updateTraceIds();
	
}