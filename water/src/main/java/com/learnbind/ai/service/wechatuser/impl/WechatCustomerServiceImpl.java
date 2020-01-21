package com.learnbind.ai.service.wechatuser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WechatCustomerMapper;
import com.learnbind.ai.model.WechatCustomer;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.wechatuser.WechatCustomerService;
import com.learnbind.ai.service.wechatuser.WechatUserService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechatuser.impl
 *
 * @Title: WechatCustomerServiceImpl.java
 * @Description: 微信用户-客户的关系实现类
 *
 * @author Administrator
 * @date 2019年8月8日 上午9:39:58
 * @version V1.0 
 *
 */
@Service
public class WechatCustomerServiceImpl extends AbstractBaseService<WechatCustomer, Long> implements  WechatCustomerService {
	
	@Autowired
	public WechatCustomerMapper wechatCustomerMapper;
	@Autowired
	public WechatUserService wechatUserService;
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public WechatCustomerServiceImpl(WechatCustomerMapper mapper) {
		this.wechatCustomerMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public WechatUser getWechatCustomer(Long customerId) {
		WechatCustomer searchObj = new WechatCustomer();
		searchObj.setCustomerId(customerId);
		WechatCustomer wc = wechatCustomerMapper.selectOne(searchObj);
		if(wc!=null) {
			WechatUser wu = wechatUserService.selectByPrimaryKey(wc.getWechatId());
			return wu;
		}
		return null;
	}

	@Override
	public int updateCustomerCode(Long customerId, String customerCode) {
		
		Example example = new Example(WechatCustomer.class);
		example.createCriteria().andEqualTo("customerId", customerId);
		WechatCustomer wc = new WechatCustomer();
		wc.setCustomerCode(customerCode);
		return wechatCustomerMapper.updateByExampleSelective(wc, example);
	}

}
