package com.learnbind.ai.service.meterbook.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.dao.UserMeterBookMapper;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.UserMeterBook;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meterbook.UserMeterBookService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterbook.impl
 *
 * @Title: UserMeterBookServiceImpl.java
 * @Description: 用户-表册关系服务实现类
 *
 * @author Administrator
 * @date 2019年6月11日 下午12:25:28
 * @version V1.0
 *
 */
@Service
public class UserMeterBookServiceImpl extends AbstractBaseService<UserMeterBook, Long> implements UserMeterBookService {

	@Autowired
	public UserMeterBookMapper userMeterBookMapper;
	@Autowired
	public MeterBookService meterBookService;

	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public UserMeterBookServiceImpl(UserMeterBookMapper mapper) {
		this.userMeterBookMapper = mapper;
		this.setMapper(mapper);
	}

	@Override
	@Transactional
	public int insertBatch(List<UserMeterBook> userMeterBookList) {
		int rows = 0;
		for (UserMeterBook temp : userMeterBookList) {
			rows = userMeterBookMapper.insertSelective(temp);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

	@Override
	@Transactional
	public int deleteBatch(List<UserMeterBook> userMeterBookList) {
		int rows = 0;
		for (UserMeterBook temp : userMeterBookList) {
			rows = userMeterBookMapper.delete(temp);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

	/**
	 * (非 Javadoc)
	 * 
	 * @Title: deleteByMeterBookIdList
	 * @Description: 根据表册ID集合删除
	 * @param meterBookIdList
	 * @return
	 * @see com.learnbind.ai.service.meterbook.UserMeterBookService#deleteByMeterBookIdList(java.util.List)
	 */
	@Override
	public Integer deleteByMeterBookIdList(List<Long> meterBookIdList, Long readerId) {
		return userMeterBookMapper.deleteByMeterBookIdList(meterBookIdList, readerId);
		
	}

	/**
	 * (非 Javadoc)
	 * 
	 * @Title: insertUserMeterBookByBlockCode
	 * @Description: 根据小区分配表册
	 * @param readerId
	 * @param locationBlockCode
	 * @return
	 * @see com.learnbind.ai.service.meterbook.UserMeterBookService#insertUserMeterBookByBlockCode(java.lang.Long,
	 *      java.lang.String)
	 */
	@Override
	@Transactional
	public int insertUserMeterBookByBlockCode(Long readerId, String locationBlockCode) {
		int rows = 1;
		List<MeterBook> meterBookList = meterBookService.getMeterBookIdByCode(locationBlockCode);
		List<Long> meterBookIdList = new ArrayList<Long>();
		for (MeterBook mb : meterBookList) {
			Long meterBookId = mb.getId();
			meterBookIdList.add(meterBookId);
		}
		// 删除已存在的数据
		this.deleteByMeterBookIdList(meterBookIdList, readerId);
		
		for (MeterBook mb : meterBookList) {
			UserMeterBook userMeterBook = new UserMeterBook();
			userMeterBook.setUserId(readerId);
			userMeterBook.setMeterBookId(mb.getId());
			rows = userMeterBookMapper.insertSelective(userMeterBook);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}

		}
		return rows;

	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertUserMeterBookByBuildingCode
	 * @Description: 根据楼号分配表册
	 * @param readerId
	 * @param locationBuiledingCode
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.UserMeterBookService#insertUserMeterBookByBuildingCode(java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public int insertUserMeterBookByBuildingCode(Long readerId, String locationBuiledingCode) {
		int rows = 1;
		List<MeterBook> meterBookList = meterBookService.getMeterBookIdByCode(locationBuiledingCode);
		// 删除已存在的数据
		List<Long> meterBookIdList = new ArrayList<Long>();
		for (MeterBook mb : meterBookList) {
			Long meterBookId = mb.getId();
			meterBookIdList.add(meterBookId);
		}
		
		this.deleteByMeterBookIdList(meterBookIdList, readerId);
		
		for (MeterBook mb : meterBookList) {
			UserMeterBook userMeterBook = new UserMeterBook();
			userMeterBook.setUserId(readerId);
			userMeterBook.setMeterBookId(mb.getId());
			rows = userMeterBookMapper.insertSelective(userMeterBook);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}

		}
		return rows;
	}

	@Override
	@Transactional
	public int insertUserMeterBookByUnitCode(Long readerId, String locationUnitCode) {
		int rows = 1;
		List<MeterBook> meterBookList = meterBookService.getMeterBookIdByCode(locationUnitCode);
		// 删除已存在的数据
		List<Long> meterBookIdList = new ArrayList<Long>();
		for (MeterBook mb : meterBookList) {
			Long meterBookId = mb.getId();
			meterBookIdList.add(meterBookId);
		}
		
		this.deleteByMeterBookIdList(meterBookIdList, readerId);
		if(meterBookList!=null && meterBookList.size()>0) {
			MeterBook mb = meterBookList.get(0);
			UserMeterBook userMeterBook = new UserMeterBook();
			userMeterBook.setUserId(readerId);
			userMeterBook.setMeterBookId(mb.getId());
			rows = userMeterBookMapper.insertSelective(userMeterBook);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}
		
		return rows;
	}

}
