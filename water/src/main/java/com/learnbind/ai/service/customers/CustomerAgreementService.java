package com.learnbind.ai.service.customers;

import java.util.List;

import com.learnbind.ai.model.CustomerAgreement;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.customers
 *
 * @Title: CustomerAgreementService.java
 * @Description: 客户-协议关系服务接口类
 *
 * @author Administrator
 * @date 2019年5月30日 上午10:33:54
 * @version V1.0 
 *
 */
public interface CustomerAgreementService extends IBaseService<CustomerAgreement, Long> {
	
	/**
	 * @Title: insert
	 * @Description: 批量增加
	 * @param agreementList
	 * @return 
	 */
	public int insert(List<CustomerAgreement> agreementList);
	
}
