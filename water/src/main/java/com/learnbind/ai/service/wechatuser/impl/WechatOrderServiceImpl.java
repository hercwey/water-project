package com.learnbind.ai.service.wechatuser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WechatOrderMapper;
import com.learnbind.ai.model.WechatOrder;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.wechatuser.WechatOrderService;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.wechatuser.impl
 *
 * @Title: WechatOrderServiceImpl.java
 * @Description: 微信订单服务实现类
 *
 * @author Administrator
 * @date 2019年8月13日 上午10:40:03
 * @version V1.0 
 *
 */
@Service
public class WechatOrderServiceImpl extends AbstractBaseService<WechatOrder, Long> implements WechatOrderService {

	@Autowired
	public WechatOrderMapper wechatOrderMapper;

	/** 
	* <p>Title:采用构造函数注入 </p> 
	* <p>Description: </p> 
	* @param mapper 
	*/
	public WechatOrderServiceImpl(WechatOrderMapper mapper) {
		this.wechatOrderMapper=mapper;
		this.setMapper(mapper);
	}

}
