package com.learnbind.ai.service.location.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.dao.LocationMeterMapper;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.location.impl
 *
 * @Title: LocationMeterServiceImpl.java
 * @Description: 地理位置-表计关系服务实现类
 *
 * @author Administrator
 * @date 2019年5月23日 上午10:01:38
 * @version V1.0 
 *
 */
@Service
public class LocationMeterServiceImpl extends AbstractBaseService<LocationMeter, Long> implements LocationMeterService {

	public LocationMeterMapper locationMeterMapper;
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户关系服务接口类
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计关系服务接口类
	
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
	public LocationMeterServiceImpl(LocationMeterMapper mapper) {
		this.locationMeterMapper = mapper;
		this.setMapper(mapper);
	}

	@Override
	@Transactional
	public int bind(Long locationId, String traceIds, Long meterId) {
		
		//建立地理位置与表计的绑定关系
		int rows = this.insertByLocationIdAndMeterId(locationId, traceIds, meterId);
		if(rows>0) {
			//查询地理位置是否与客户绑定
			List<Long> customerIdList = locationCustomerService.getCustIdListByLocationId(locationId);
			//如果有绑定客户，则建立客户与表计的绑定关系，并增加一条抄表记录
			if(customerIdList!=null && customerIdList.size()>0) {
				for(Long customerId : customerIdList) {
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

	@Override
	@Transactional
	public int unbind(Long locationId, Long meterId) {
		
		//解除地理位置与表计的关系
		int rows = this.deleteByLocationIdAndMeterId(locationId, meterId);
		if(rows>0) {
			//查询地理位置是否与客户绑定
			List<Long> customerIdList = locationCustomerService.getCustIdListByLocationId(locationId);
			//如果有绑定客户，则解除客户与表计的绑定关系
			if(customerIdList!=null && customerIdList.size()>0) {
				for(Long customerId : customerIdList) {
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
	public boolean isBinding(Long locationId, Long meterId) {
		LocationMeter record = new LocationMeter();
		record.setLocationId(locationId);
		record.setMeterId(meterId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationMeter> lmList = locationMeterMapper.select(record);
		if(lmList!=null && lmList.size()>0) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isBinding(Long meterId) {
		LocationMeter record = new LocationMeter();
		record.setMeterId(meterId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationMeter> lmList = locationMeterMapper.select(record);
		if(lmList!=null && lmList.size()>0) {
			return true;
		}
		return false;
	}

	/** 
	 * @Title: insertByLocationIdAndMeterId
	 * @Description: 根据地理位置ID和表计ID增加，其他属性已设置
	 * @param locationId
	 * @param meterId
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationMeterService#insertByLocationIdAndMeterId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public int insertByLocationIdAndMeterId(Long locationId, String traceIds, Long meterId) {
		LocationMeter lm = new LocationMeter();
		lm.setLocationId(locationId);
		lm.setMeterId(meterId);
		lm.setTraceIds(traceIds);
		lm.setBindTime(DateUtils.getSysDate());
		lm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		lm.setMeterStatus(EnumMeterSettingStatus.ZERO.getValue());
		return locationMeterMapper.insertSelective(lm);
	}
	
	/** 
	 * @Title: deleteByLocationIdAndMeterId
	 * @Description: 根据地理位置ID和表计ID删除，逻辑删除
	 * @param locationId
	 * @param meterId
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationMeterService#deleteByLocationIdAndMeterId(java.lang.Long, java.lang.Long)
	 */
	@Override
	public int deleteByLocationIdAndMeterId(Long locationId, Long meterId) {
		//根据Example条件更新实体`record`包含的不是null的属性值
		Example example = new Example(LocationMeter.class);
		Criteria criteria = example.createCriteria();
		if(locationId!=null && locationId>0) {
			criteria.andEqualTo("locationId", locationId);
		}
		if(meterId!=null && meterId>0) {
			criteria.andEqualTo("meterId", meterId);
		}
		criteria.andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		LocationMeter record = new LocationMeter();
		record.setUnbindTime(DateUtils.getSysDate());
		record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		return locationMeterMapper.updateByExampleSelective(record, example);
	}

	/** 
	 * @Title: getMeterIdListByLocationId
	 * @Description: 根据地理位置ID查询表计ID集合
	 * @param locationId
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationMeterService#getMeterIdListByLocationId(java.lang.Long)
	 */
	@Override
	public List<Long> getMeterIdListByLocationId(Long locationId) {
		LocationMeter record = new LocationMeter();
		record.setLocationId(locationId);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationMeter> lmList = locationMeterMapper.select(record);
		if(lmList!=null && lmList.size()>0) {
			List<Long> meterIdList = new ArrayList<>();
			for(LocationMeter lc : lmList) {
				meterIdList.add(lc.getMeterId());
			}
			return meterIdList;
		}
		return new ArrayList<Long>();
	}

	@Override
	public List<LocationMeter> getListByMeterIdList(List<Long> meterIdList) {
		return locationMeterMapper.getListByMeterIdList(meterIdList);
	}
	

	@Override
	public LocationMeter getLocationByMeterId(Long meterId) {
		return locationMeterMapper.getLocationByMeterId(meterId);
	}

	@Override
	public int updateTraceIds() {
		return locationMeterMapper.updateTraceIds();
	}

}
