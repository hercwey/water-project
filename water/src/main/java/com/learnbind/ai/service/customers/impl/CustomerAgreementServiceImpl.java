package com.learnbind.ai.service.customers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.dao.CustomerAgreementMapper;
import com.learnbind.ai.model.CustomerAgreement;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAgreementService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomerAgreementServiceImpl.java
 * @Description: 客户-协议关系服务实现类
 *
 * @author Administrator
 * @date 2019年5月30日 上午10:35:49
 * @version V1.0 
 *
 */
@Service
public class CustomerAgreementServiceImpl extends AbstractBaseService<CustomerAgreement, Long> implements  CustomerAgreementService {
	
	@Autowired
	public CustomerAgreementMapper customerAgreementMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerAgreementServiceImpl(CustomerAgreementMapper mapper) {
		this.customerAgreementMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	@Transactional
	public int insert(List<CustomerAgreement> agreementList) {
		int rows = 0;
		for(CustomerAgreement agreement : agreementList) {
			rows = customerAgreementMapper.insertSelective(agreement);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

}
