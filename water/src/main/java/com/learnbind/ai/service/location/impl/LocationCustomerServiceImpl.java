package com.learnbind.ai.service.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.dao.LocationCustomerMapper;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.location.impl
 *
 * @Title: LocationCustomerServiceImpl.java
 * @Description: 地理位置-客户关系服务实现类
 *
 * @author Administrator
 * @date 2019年5月23日 上午10:03:28
 * @version V1.0 
 *
 */
@Service
public class LocationCustomerServiceImpl extends AbstractBaseService<LocationCustomer, Long> implements LocationCustomerService {

	public LocationCustomerMapper locationCustomerMapper;
	@Autowired
	private LocationMeterService locationMeterService;//地理位置-表计关系服务接口类
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计关系服务接口类
	@Autowired
	private LocationService locationService;//地理位置
	
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
	public LocationCustomerServiceImpl(LocationCustomerMapper mapper) {
		this.locationCustomerMapper = mapper;
		this.setMapper(mapper);
	}

	@Override
	@Transactional
	public int bind(Long locationId, String traceIds, Long customerId) {
		
		//建立地理位置与客户的绑定关系
		int rows = this.insertByLocationIdAndCustId(locationId, traceIds, customerId);
		if(rows>0) {
			//查询地理位置是否与表计绑定
			List<Long> meterIdList = locationMeterService.getMeterIdListByLocationId(locationId);
			//如果有绑定表计，则建立客户与表计的绑定关系
			if(meterIdList!=null && meterIdList.size()>0) {
				this.setDefaultStatusByMeterIdList(meterIdList);//根据表计ID集合设置客户-表计关系的默认默认状态为非默认状态
				for(Long meterId : meterIdList) {
					rows = customerMeterService.insertByCustIdAndMeterId(customerId, meterId);
					if(rows<=0) {
						break;
					}
				}
			}
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	/**
	 * @Title: setDefaultStatusByMeterIdList
	 * @Description: 根据表计ID集合设置客户-表计关系的默认默认状态为非默认状态
	 * @param locationId
	 * @return 
	 */
	private int setDefaultStatusByMeterIdList(List<Long> meterIdList) {
		Example example = new Example(CustomerMeter.class);
		example.createCriteria().andIn("meterId", meterIdList).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerMeter> cmList = customerMeterService.selectByExample(example);
		List<Long> cmIdList = new ArrayList<>();
		for(CustomerMeter cm : cmList) {
			cmIdList.add(cm.getId());
		}
		if(cmIdList!=null && cmIdList.size()>0) {
			example = new Example(CustomerMeter.class);
			example.createCriteria().andIn("id", cmIdList);
			CustomerMeter updateData = new CustomerMeter();
			updateData.setDefaultCustomer(EnumDefaultStatus.DEFAULT_NO.getValue());
			return customerMeterService.updateByExampleSelective(updateData, example);
		}
		return 1;
	}
	
	@Override
	@Transactional
	public int TransactionCheckBind(Long locationId, String traceIds, String[] customerIdArr) {
		int row = 0;
		for(String custIdStr : customerIdArr) {
			Long customerId = Long.valueOf(custIdStr);
			if(StringUtils.isNotBlank(custIdStr)) {
				row = this.bind(locationId, traceIds, customerId);
				if(row<=0) {
					break;
				}
			}
		}
		
		if(row<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return row;
	}
	
	@Override
	@Transactional
	public int TransactionCheckUnBind(Long locationId, String[] customerIdArr) {
		int row = 0;
		for(String custIdStr : customerIdArr) {
			Long customerId = Long.valueOf(custIdStr);
			if(StringUtils.isNotBlank(custIdStr)) {
				row = this.unbind(locationId, customerId);
				if(row<=0) {
					break;
				}
			}
		}
		
		if(row<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return row;
	}

	@Override
	@Transactional
	public int unbind(Long locationId, Long customerId) {
		
		//解除地理位置与客户的关系
		int rows = this.deleteByLocationIdAndCustId(locationId, customerId);
		if(rows>0) {
			//查询地理位置是否与表计绑定
			List<Long> meterIdList = locationMeterService.getMeterIdListByLocationId(locationId);
			//如果有绑定表计，则解除客户与表计的绑定关系
			if(meterIdList!=null && meterIdList.size()>0) {
				for(Long meterId : meterIdList) {
					rows = customerMeterService.deleteByCustIdAndMeterId(customerId, meterId);
					if(rows<=0) {
						break;
					}
				}
			}
		}
		
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	@Override
	public boolean isBinding(Long locationId, Long customerId) {
		LocationCustomer record = new LocationCustomer();
		//record.setLocationId(locationId);
		record.setCustomerId(customerId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> lcList = locationCustomerMapper.select(record);
		if(lcList!=null && lcList.size()>0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isBinding(Long customerId) {
		LocationCustomer record = new LocationCustomer();
		record.setCustomerId(customerId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> lcList = locationCustomerMapper.select(record);
		if(lcList!=null && lcList.size()>0) {
			return true;
		}
		return false;
	}
	
	/** 
	 * @Title: insertByLocationIdAndCustId
	 * @Description: 根据地理位置ID和客户ID增加，其他属性已设置
	 * @param locationId
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationCustomerService#insertByLocationIdAndCustId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public int insertByLocationIdAndCustId(Long locationId, String traceIds, Long customerId) {
		
		//this.setDefaultStatusByLocationId(locationId);//根据地理位置ID设置地理位置-客户关系的默认客户状态为非默认状态
		
		//查询客户与地理位置是否已绑定
		LocationCustomer lc = new LocationCustomer();
		lc.setCustomerId(customerId);
		lc.setLocationId(locationId);
		lc.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		int count = locationCustomerMapper.selectCount(lc);
		if(count<=0) {//如果客户与地理位置未绑定，则建立客户-地理位置关系
			LocationCustomer record = new LocationCustomer();
			record.setLocationId(locationId);
			record.setTraceIds(traceIds);
			record.setCustomerId(customerId);
			record.setBindTime(DateUtils.getSysDate());
			record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			//record.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
			return locationCustomerMapper.insertSelective(record);
		}
		
		return count;
		
	}
	
	/**
	 * @Title: setDefaultCustomer
	 * @Description: 根据地理位置ID设置地理位置-客户关系的默认客户状态为非默认状态
	 * @param locationId
	 * @return 
	 */
	
	
	/** 
	 * @Title: deleteByLocationIdAndCustId
	 * @Description: 根据地理位置ID和客户ID删除，逻辑删除
	 * @param locationId
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationCustomerService#deleteByLocationIdAndCustId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public int deleteByLocationIdAndCustId(Long locationId, Long customerId) {
		
		int rows = 1;
		//判断是否需要解绑地理位置-客户
		boolean isUnbind = this.isUnbind(locationId, customerId);
		if(isUnbind) {
			
			//解除地理位置与当前客户的绑定关系
			//条件
			Example example = new Example(LocationCustomer.class);
			example.createCriteria().andEqualTo("locationId", locationId).andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
			//更新内容
			LocationCustomer lc = new LocationCustomer();
			lc.setUnbindTime(DateUtils.getSysDate());
			lc.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = locationCustomerMapper.updateByExampleSelective(lc, example);
		}
		return rows;
	}
	
	/**
	 * @Title: isUnbind
	 * @Description: 判断是否需要解绑地理位置-客户
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	private boolean isUnbind(Long locationId, Long customerId) {
		//查询客户绑定的表计
		List<CustomerMeter> cmList = customerMeterService.getListByCustomerId(customerId);
		List<Long> meterIdList = new ArrayList<>();
		for(CustomerMeter cm : cmList) {
			meterIdList.add(cm.getMeterId());
		}
		boolean isUnbind = true;
		if(meterIdList!=null && meterIdList.size()>0) {
			List<LocationMeter> lmList = locationMeterService.getListByMeterIdList(meterIdList);
			for(LocationMeter lm : lmList) {
				if(lm.getLocationId().equals(locationId)) {
					isUnbind = false;
					break;
				}
			}
		}
		return isUnbind;
	}
	
	/** 
	 * @Title: getCustIdListByLocationId
	 * @Description: 根据地理位置ID查询客户ID集合
	 * @param locationId
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationCustomerService#getCustIdListByLocationId(java.lang.Long)
	 */
	@Override
	public List<Long> getCustIdListByLocationId(Long locationId) {
		LocationCustomer record = new LocationCustomer();
		record.setLocationId(locationId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		//record.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());//默认客户
		List<LocationCustomer> lcList = locationCustomerMapper.select(record);
		if(lcList!=null && lcList.size()>0) {
			List<Long> customerIdList = new ArrayList<>();
			for(LocationCustomer lc : lcList) {
				customerIdList.add(lc.getCustomerId());
			}
			return customerIdList;
		}
		return null;
	}

	@Override
	public List<Long> getLocationIdListByCustId(Long customerId) {
		LocationCustomer record = new LocationCustomer();
		record.setCustomerId(customerId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		//record.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());//默认客户
		List<LocationCustomer> lcList = locationCustomerMapper.select(record);
		if(lcList!=null && lcList.size()>0) {
			List<Long> locationIdList = new ArrayList<>();
			for(LocationCustomer lc : lcList) {
				locationIdList.add(lc.getLocationId());
			}
			return locationIdList;
		}
		return null;
	}

	@Override
	public String getTraceIds(Long customerId) {
		LocationCustomer record = new LocationCustomer();
		record.setCustomerId(customerId);
		//record.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> lcList = locationCustomerMapper.select(record);
		if(lcList!=null && lcList.size()>0) {
			return lcList.get(0).getTraceIds();
		}
		return null;
	}

	@Override
	public int updateTraceIds() {
		return locationCustomerMapper.updateTraceIds();
	}

	@Override
	public LocationCustomer getLocationByCustomerId(Long customerId) {
		Example example = new Example(LocationCustomer.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> lmList = this.selectByExample(example);
		if(lmList!=null && lmList.size()>0) {
			return lmList.get(0);
		}
		return null;
	}

	@Override
	public String getPlace(Long customerId) {
		LocationCustomer lm = this.getLocationByCustomerId(customerId);
		if(lm!=null) {
			return locationService.getPlace(lm.getTraceIds());
		}
		return null;
	}

}
