package com.learnbind.ai.service.meterbook.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.dao.MeterBookMeterMapper;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.UserMeterBook;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterbook.impl
 *
 * @Title: MeterBookMeterServiceImpl.java
 * @Description: 表册-表计关系服务实现类
 *
 * @author Administrator
 * @date 2019年6月11日 下午12:28:14
 * @version V1.0 
 *
 */
@Service
public class MeterBookMeterServiceImpl extends AbstractBaseService<MeterBookMeter, Long> implements  MeterBookMeterService {
	
	@Autowired
	public MeterBookMeterMapper meterBookMeterMapper;
	@Autowired
	public CustomersService customersService;
	@Autowired
	public LocationService locationService;
	@Autowired
	public MeterBookService meterBookService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterBookMeterServiceImpl(MeterBookMeterMapper mapper) {
		this.meterBookMeterMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchMeterBookMeterList
	 * @Description: 获取表册的表计列表
	 * @param searchCond
	 * @param meterBookId
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.MeterBookMeterService#searchMeterBookMeterList(java.lang.String, java.lang.Long)
	 */
	@Override
	public List<MeterBookMeter> searchMeterBookMeterList(String searchCond, Long meterBookId) {
		return meterBookMeterMapper.searchMeterBookMeterList(searchCond, meterBookId);
	}

	@Override
	@Transactional
	public int insertBatch(List<MeterBookMeter> meterBookMeterList) {
		int rows = 0;
		for (MeterBookMeter temp : meterBookMeterList) {
			rows = meterBookMeterMapper.insertSelective(temp);
			
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}
	
	@Override
	@Transactional
	public int deleteBatch(List<MeterBookMeter> meterBookMeterList) {
		int rows = 0;
		for (MeterBookMeter temp : meterBookMeterList) {
			rows = meterBookMeterMapper.delete(temp);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

	@Override
	public int moveBatch(List<MeterBookMeter> meterBookMeterList, String meterBookId) {
		int rows = 0;
		for (MeterBookMeter temp : meterBookMeterList) {
			temp.setMeterBookId(Long.valueOf(meterBookId));
			rows = meterBookMeterMapper.updateByPrimaryKeySelective(temp);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

}
