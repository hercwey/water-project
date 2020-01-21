package com.learnbind.ai.service.wechatuser;

import com.learnbind.ai.model.WechatCustomer;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.common.IBaseService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechatuser
 *
 * @Title: WechatCustomerService.java
 * @Description: 微信用户-客户的关系接口类
 *
 * @author Administrator
 * @date 2019年8月8日 上午9:38:14
 * @version V1.0 
 *
 */
public interface WechatCustomerService extends IBaseService<WechatCustomer, Long> {
	
	/**
	 * @Title: getWechatCustomer
	 * @Description: 根据客户ID查询微信用户
	 * @param customerId
	 * @return 
	 */
	public WechatUser getWechatCustomer(Long customerId);
	
	/**
	 * @Title: updateCustomerCode
	 * @Description: 更新客户编码
	 * @param customerId
	 * @param customerCode
	 * @return 
	 */
	public int updateCustomerCode(Long customerId, String customerCode);
	
}
