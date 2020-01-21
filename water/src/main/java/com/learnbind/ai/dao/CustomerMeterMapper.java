package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Meters;

import tk.mybatis.mapper.common.Mapper;

public interface CustomerMeterMapper extends Mapper<CustomerMeter> {
	
	/**
	 * @Title: getListByLocationId
	 * @Description: 根据地理位置ID查询
	 * 			客户-表计关系表（customer_meter）和地理位置-表计关系表（location_meter）多表关联查询
	 * @param locationId
	 * @return 
	 */
	public List<CustomerMeter> getListByLocationIdList(@Param("locationIdList") List<Long> locationIdList);
	
	
	/**
	 * @Title: selectCustomerCountByMeter
	 * @Description: 查询该客户是否为一表多户
	 * @param customerId
	 * @return 
	 */
	public List<CustomerMeter>  selectCustomerCountByMeter(@Param("customerId")Long customerId);
	
	/**
	 * @Title: getSubAccountCustomerId
	 * @Description: 获取分账客户ID集合
	 * @param customerId
	 * @return 
	 */
	public List<Long>  getSubAccountCustomerId(@Param("customerId")Long customerId);
	
	
	/**
	 * @Title: getListByMeterId
	 * @Description: 根据地理位置ID获取客户已绑定的水表
	 * @param meterId
	 * @return 
	 */
	public int getCustomerBindedMeter(@Param("locationId") Long locationId);
	
	/**
	 * @Title: getCustomerByMeterId
	 * @Description: 根据表计ID查询绑定的默认客户
	 * @param meterId
	 * @return 
	 */
	public CustomerMeter getCustomerByMeterId(@Param("meterId") Long meterId);
	
	/**
	 * @Title: getMeterMessage
	 * @Description: 获取客户绑定的历史表计
	 * @param customerId
	 * @param searchCond
	 * @return 
	 */
	public List<CustomerMeter> getMeterHistoryMessage(@Param("customerId") Long customerId, @Param("searchCond") String searchCond);
	
	/**
	 * @Title: getCustomerHistoryMessage
	 * @Description: 获取表计的客户历史
	 * @param meterId
	 * @param searchCond
	 * @return 
	 */
	public List<CustomerMeter> getCustomerHistoryMessage(@Param("meterId") Long meterId, @Param("searchCond") String searchCond);
	
	/**
	 * @Title: getMeterFactory
	 * @Description: 获取表计生产厂家
	 * 					取值是表册中表计最多的一个生产厂家
	 * @param cmIdList
	 * @return 
	 */
	public String getMeterFactory(@Param("cmIdList") List<Long> cmIdList);
	
	/**
	 * @Title: getMeterReadMode
	 * @Description: 表计的抄表方式
	 * 					取值是表册中表计最多的一个抄表方式
	 * @param cmIdList
	 * @return 
	 */
	public String getMeterReadMode(@Param("cmIdList") List<Long> cmIdList);
	
}