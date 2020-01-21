package com.learnbind.ai.service.wechatuser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WechatUserMapper;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.wechatuser.WechatUserService;

/**
 * 微信用户实体服务类-实现
 * 
 * @author lenovo
 *
 */
@Service
public class WechatUserServiceImpl extends AbstractBaseService<WechatUser, Long> implements WechatUserService {

	@Autowired
	public WechatUserMapper wechatUserMapper;

	/** 
	* <p>Title:采用构造函数注入 </p> 
	* <p>Description: </p> 
	* @param mapper 
	*/
	public WechatUserServiceImpl(WechatUserMapper mapper) {
		this.wechatUserMapper=mapper;
		this.setMapper(mapper);
	}

}
