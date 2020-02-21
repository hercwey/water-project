package com.learnbind.ai.service.busoffice.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.UsersBusOfficeMapper;
import com.learnbind.ai.model.UsersBusOffice;
import com.learnbind.ai.service.busoffice.UserBusOfficeService;
import com.learnbind.ai.service.common.AbstractBaseService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.checkmeter.impl
 *
 * @Title: CheckMeterServiceImpl.java
 * @Description: 服务实现
 *
 * @author Thinkpad
 * @date 2019年5月15日 下午5:35:59
 * @version V1.0 
 *
 */
@Service
public class UserBusOfficeServiceImpl extends AbstractBaseService<UsersBusOffice, Long> implements  UserBusOfficeService {
	
	@Autowired
	public UsersBusOfficeMapper usersBusOfficeMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public UserBusOfficeServiceImpl(UsersBusOfficeMapper mapper) {
		this.usersBusOfficeMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCheckMeter
	 * @Description: 根据条件查询检测配置
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.checkmeter.CheckMeterService#searchCheckMeter(java.lang.String)
	 */
	@Override
	public List<UsersBusOffice> searchCond(String searchCond) {
		return usersBusOfficeMapper.searchCond(searchCond);
	}

	@Override
	public UsersBusOffice getBusOfficeMessage(Long userId) {
		Example example = new Example(UsersBusOffice.class);
		example.createCriteria().andEqualTo("userId", userId);
		List<UsersBusOffice> officeList = this.selectByExample(example);
		if(officeList.size() < 1) {
			return null;
		} else {
			return officeList.get(0);
		}
		
	}


}
