package com.learnbind.ai.service.customers;

import java.util.List;

import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.customers
 *
 * @Title: CustomerMeterService.java
 * @Description: 客户-表计关系服务接口类
 *
 * @author Administrator
 * @date 2019年5月23日 上午10:06:28
 * @version V1.0 
 *
 */
public interface CustomerMeterService extends IBaseService<CustomerMeter, Long> {
	
	/**
	 * @Title: insertByCustIdAndMeterId
	 * @Description: 根据客户ID和表计ID增加，其他属性在实现类中设置
	 * @param customerId
	 * @param meterId
	 * @return 
	 */
	public int insertByCustIdAndMeterId(Long customerId, Long meterId);
	
	/**
	 * @Title: deleteByCustIdAndMeterId
	 * @Description: 根据客户ID和水表ID删除
	 * @param customerId
	 * @param meterId
	 * @return 
	 */
	public int deleteByCustIdAndMeterId(Long customerId, Long meterId);
	
	/**
	 * @Title: getListByLocationIdList
	 * @Description: 根据地理位置ID集合查询
	 * 			客户-表计关系表（customer_meter）和地理位置-表计关系表（location_meter）多表关联查询
	 * @param locationIdList
	 * @return 
	 */
	public List<CustomerMeter> getListByLocationIdList(List<Long> locationIdList);
	
	/**
	 * @Title: selectCustomerCountByMeter
	 * @Description: 查询该客户是否为一表多户
	 * @param customerId
	 * @return 
	 */
	public List<CustomerMeter> selectCustomerCountByMeter(Long customerId);
	
	/**
	 * @Title: getSubAccountCustomerId
	 * @Description: 获取分账客户
	 * @param customerId
	 * @return 
	 */
	public List<Long> getSubAccountCustomerId(Long customerId);
	
	/**
	 * @Title: getListByCustomerId
	 * @Description: 根据客户ID查询客户-表计关系
	 * @param customerId
	 * @return 
	 */
	public List<CustomerMeter> getListByCustomerId(Long customerId);
	
	/**
	 * @Title: getListByMeterId
	 * @Description: 根据表计ID查询客户-表计关系
	 * @param meterId
	 * @return 
	 */
	public List<CustomerMeter> getListByMeterId(Long meterId);
	
	/**
	 * @Title: getCustomerByMeterId
	 * @Description: 根据表计ID查询绑定的默认客户
	 * @param meterId
	 * @return 
	 */
	public CustomerMeter getCustomerByMeterId(Long meterId);
	
	
	/**
	 * @Title: getListByMeterId
	 * @Description: 根据地理位置ID获取客户已绑定的水表
	 * @param meterId
	 * @return 
	 */
	public int getCustomerBindedMeter(Long locationId);
	
	/**
	 * @Title: setDefaultCustomerStatus
	 * @Description: 设置默认客户状态
	 * @param id
	 * @param status
	 * @return 
	 */
	public int setDefaultCustomerStatusById(Long id, Integer status);
	
	/**
	 * @Title: setDefaultCustomerStatus
	 * @Description: 设置默认客户状态为否
	 * @param idList
	 * @param status
	 * @return 
	 */
	public int setDefaultCustomerStatusNo(List<Long> idList);
	
	/**
	 * @Title: setDefaultCustomerStatus
	 * @Description: 设置默认客户状态为否
	 * @param meterId
	 * @param status
	 * @return 
	 */
	public int setDefaultCustomerStatusNo(Long meterId);
	
	/**
	 * @Title: bind
	 * @Description: 建立客户与表计的关系
	 * @param customerId
	 * @param meterId
	 * @param status
	 * @return 
	 */
	public int bind(Long customerId, Long meterId, Integer status);
	
	/**
	 * @Title: unbind
	 * @Description: 解除客户-表计关系，如果为一表多户，先验证是否是默认客户，如果是默认客户则重新设置默认客户，否则不设置
	 * @param customerId
	 * @param meterId
	 * @return 
	 */
	public int unbind(Long customerId, Long meterId);
	
	/**
	 * @Title: setDefaultCustomer
	 * @Description: 设置默认客户
	 * @param customerId
	 * @param meterId
	 * @return 
	 */
	public int setDefaultCustomer(Long customerId, Long meterId);
	
	/**
	 * @Title: getMeterMessage
	 * @Description: 获取客户绑定的历史表计
	 * @param customerId
	 * @param searchCond
	 * @return 
	 */
	public List<CustomerMeter> getMeterHistoryMessage(Long customerId, String searchCond);
	
	/**
	 * @Title: getCustomerHistoryMessage
	 * @Description: 获取表计的客户历史
	 * @param meterId
	 * @param searchCond
	 * @return 
	 */
	public List<CustomerMeter> getCustomerHistoryMessage(Long meterId, String searchCond);
	
	/**
	 * @Title: getMeterFactory
	 * @Description: 获取表计生产厂家
	 * 					取值是表册中表计最多的一个生产厂家
	 * @param cmIdList
	 * @return 
	 */
	public String getMeterFactory(List<Long> cmIdList);
	
	/**
	 * @Title: getMeterReadMode
	 * @Description: 表计的抄表方式
	 * 					取值是表册中表计最多的一个抄表方式
	 * @param cmIdList
	 * @return 
	 */
	public String getMeterReadMode(List<Long> cmIdList);
	
}
