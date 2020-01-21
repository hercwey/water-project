package com.learnbind.ai.service.tax.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.dao.TaxRedInfoMapper;
import com.learnbind.ai.model.TaxRedInfo;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.tax.TaxRedInfoService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.taxinvoice.impl
 *
 * @Title: TaxRedInfoServiceImpl.java
 * @Description: 红字信息表service实现类
 *
 * @author Administrator
 * @date 2019年12月1日 上午12:15:07
 * @version V1.0 
 *
 */
@Service
public class TaxRedInfoServiceImpl extends AbstractBaseService<TaxRedInfo, Long> implements  TaxRedInfoService {
	
	@Autowired
	public TaxRedInfoMapper taxRedInfoMapper;
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public TaxRedInfoServiceImpl(TaxRedInfoMapper mapper) {
		this.taxRedInfoMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public TaxRedInfo getTaxRedMessage(String fpdm, String fphm) {
		Example example = new Example(TaxRedInfo.class);
		example.createCriteria().andEqualTo("dylpdm", fpdm).andEqualTo("dylphm", fphm).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<TaxRedInfo> redInfoList = this.selectByExample(example);
		return redInfoList.get(0);
	}

}
