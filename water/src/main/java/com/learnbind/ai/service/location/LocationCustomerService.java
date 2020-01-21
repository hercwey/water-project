package com.learnbind.ai.service.location;

import java.util.List;

import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.location
 *
 * @Title: LocationCustomerService.java
 * @Description: 地理位置-客户关系服务接口类
 *
 * @author Administrator
 * @date 2019年5月23日 上午9:59:42
 * @version V1.0 
 *
 */
public interface LocationCustomerService extends IBaseService<LocationCustomer, Long> {
	
	/**
	 * @Title: bind
	 * @Description: 建立地理位置与客户绑定关系
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	public int bind(Long locationId, String traceIds, Long customerId);
	
	/**
	 * @Title: TransactionCheckBind
	 * @Description: 绑定
	 * @param locationId
	 * @param customerIdArr
	 * @return 
	 */
	public int TransactionCheckBind(Long locationId, String traceIds, String[] customerIdArr);
	
	/**
	 * @Title: TransactionCheckUnBind
	 * @Description: 解绑
	 * @param locationId
	 * @param customerIdArr
	 * @return 
	 */
	public int TransactionCheckUnBind(Long locationId, String[] customerIdArr);
	
	/**
	 * @Title: unbind
	 * @Description: 解除地理位置与客户绑定关系
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	public int unbind(Long locationId, Long customerId);
	
	/**
	 * @Title: isBinding
	 * @Description: 验证地理位置与客户是否已绑定
	 * @param locationId
	 * @param customerId
	 * @return 
	 * 		true：已绑定；false：未绑定
	 */
	public boolean isBinding(Long locationId, Long customerId);
	
	/**
	 * @Title: isBinding
	 * @Description: 验证客户是否已绑定
	 * @param locationId
	 * @param customerId
	 * @return 
	 * 		true：已绑定；false：未绑定
	 */
	public boolean isBinding(Long customerId);
	
	/**
	 * @Title: insertByLocationIdAndCustId
	 * @Description: 根据地理位置ID和客户ID增加，其他属性在实现类中设置
	 * 				建立客户-地理位置关系；建立关系时先验证客户-地理位置是否已绑定，已绑定不处理；未绑定则建立关系；
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	public int insertByLocationIdAndCustId(Long locationId, String traceIds, Long customerId);
	
	/**
	 * @Title: deleteByLocationIdAndCustId
	 * @Description: 根据地理位置ID和客户ID删除，逻辑删除
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	public int deleteByLocationIdAndCustId(Long locationId, Long customerId);
	
	/**
	 * @Title: getCustIdListByLocationId
	 * @Description: 根据地理位置ID查询客户ID集合
	 * @param locationId
	 * @return 
	 */
	public List<Long> getCustIdListByLocationId(Long locationId);
	
	/**
	 * @Title: getLocationIdListByCustId
	 * @Description: 根据客户ID查询地理位置ID集合（一个客户只能绑定一个地理位置，所以默认查询结果只有一个）
	 * @param locationId
	 * @return 
	 */
	public List<Long> getLocationIdListByCustId(Long customerId);
	
	/**
	 * @Title: getTraceIds
	 * @Description: 获取地理位置traceIds
	 * @param customerId
	 * @return 
	 */
	public String getTraceIds(Long customerId);
	
	/**
	 * @Title: updateTraceIds
	 * @Description: 更新地理位置客户表的traceIds
	 * @return 
	 */
	public int updateTraceIds();
	
	/**
	 * @Title: getLocationByCustomerId
	 * @Description: 根据客户id查询地理位置信息
	 * @param customerId
	 * @return 
	 */
	public LocationCustomer getLocationByCustomerId(Long customerId);
	
	/**
	 * @Title: getPlace
	 * @Description: 根据客户ID查询地理位置
	 * @param customerId
	 * @return 
	 * 		返回地理位置字符串（小区-楼号-单元-门牌号）
	 */
	public String getPlace(Long customerId);
}
