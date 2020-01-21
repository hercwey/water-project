package com.learnbind.ai.service.customers;

import java.util.List;

import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerInfoCollect;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers
 *
 * @Title: CustomerInfoColllectService.java
 * @Description: 客户信息汇总
 *
 * @author Thinkpad
 * @date 2019年8月2日 上午10:09:37
 * @version V1.0 
 *
 */
public interface CustomerInfoCollectService extends IBaseService<CustomerInfoCollect, Long> {

	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<CustomerInfoCollect> searchCond(String searchCond);
	
    
    
}
