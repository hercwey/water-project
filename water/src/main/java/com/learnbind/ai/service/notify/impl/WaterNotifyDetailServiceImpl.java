package com.learnbind.ai.service.notify.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.WaterNotifyDetailMapper;
import com.learnbind.ai.model.WaterNotifyDetail;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.notify.WaterNotifyDetailService;

import tk.mybatis.mapper.entity.Example;


/**
	* Copyright (c) 2018 by srd
	* @ClassName:     WaterPriceServiceImpl.java
	* @Description:   用户服务的实现 
	* 
	* @author:        lenovo
	* @version:       V1.0  
	* @Date:          2018年7月23日 下午7:32:10 
*/
@Service
public class WaterNotifyDetailServiceImpl extends AbstractBaseService<WaterNotifyDetail, Long> implements  WaterNotifyDetailService {
	
	@Autowired
	public WaterNotifyDetailMapper waterNotifyDetailMapper;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public WaterNotifyDetailServiceImpl(WaterNotifyDetailMapper mapper) {
		this.waterNotifyDetailMapper=mapper;
		this.setMapper(mapper);
	}


	@Override
	public List<WaterNotifyDetail> getNotifyDetail(Long notifyId) {
		Example example = new Example(WaterNotifyDetail.class);
		example.createCriteria().andEqualTo("waterNotifyId", notifyId);
		List<WaterNotifyDetail> detailList = this.selectByExample(example);
		return detailList;
	}



	



}
