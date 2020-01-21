package com.learnbind.ai.service.customers.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.dao.CustomerMeterMapper;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meters.MeterTreeService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomerMeterServiceImpl.java
 * @Description: 客户-表计关系服务实现类
 *
 * @author Administrator
 * @date 2019年5月23日 上午10:08:08
 * @version V1.0 
 *
 */
@Service
public class CustomerMeterServiceImpl extends AbstractBaseService<CustomerMeter, Long> implements  CustomerMeterService {
	
	@Autowired
	public CustomerMeterMapper customerMeterMapper;
	@Autowired
	public MeterRecordService meterRecordService;
	@Autowired
	public MetersService metersService;
	@Autowired
	public MeterTreeService meterTreeService;

		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomerMeterServiceImpl(CustomerMeterMapper mapper) {
		this.customerMeterMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * @Title: insertByCustIdAndMeterId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param customerId
	 * @param meterId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerMeterService#insertByCustIdAndMeterId(java.lang.Long, java.lang.Long)
	 */
	@Override
	@Transactional
	public int insertByCustIdAndMeterId(Long customerId, Long meterId) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date sysDate = new Date();
		CustomerMeter record = new CustomerMeter();
		record.setCustomerId(customerId);
		record.setMeterId(meterId);
		record.setBindTime(DateUtils.getSysDate());
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setMeterStatus(EnumMeterSettingStatus.ZERO.getValue());
		record.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		int rows = customerMeterMapper.insertSelective(record);
		if(rows > 0) {//增加一条抄表记录，初始化表读数
			Meters meters = metersService.selectByPrimaryKey(meterId);
			MeterRecord meterRecord = new MeterRecord();
			meterRecord.setCustomerId(customerId);
			meterRecord.setMeterId(meterId);
			//期间为系统日期-一个月
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
			String period = formatter.format(DateUtils.addMonths(sysDate, -1));
			meterRecord.setPeriod(period);
			//本期抄表日期系统日期-一个月
			meterRecord.setCurrDate(DateUtils.addMonths(sysDate, -1));
			//本期表底数=表计档案的新表表底数
			meterRecord.setCurrRead(meters.getNewMeterBottom());
			//表计父子关系表ID=根据表计ID从表计父子关系表中查询
			//rows = meterRecordService.insertMeterRecord(meterRecord, userBean.getId(), userBean.getRealname());
			rows = meterRecordService.insertSelective(meterRecord);
		}
		
		
		return rows;
	}
	
	
	@Override
	public int deleteByCustIdAndMeterId(Long customerId, Long meterId) {
		//根据Example条件更新实体`record`包含的不是null的属性值
		Example example = new Example(CustomerMeter.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("meterId", meterId);
		CustomerMeter cm = new CustomerMeter();
		cm.setUnbindTime(DateUtils.getSysDate());
		cm.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		return customerMeterMapper.updateByExampleSelective(cm, example);
	}

	@Override
	public List<CustomerMeter> getListByLocationIdList(List<Long> locationIdList) {
		return customerMeterMapper.getListByLocationIdList(locationIdList);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: selectCustomerCountByMeter
	 * @Description: 查询该客户是否为一表多户
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomerMeterService#selectCustomerCountByMeter(java.lang.Long)
	 */
	@Override
	public List<CustomerMeter>  selectCustomerCountByMeter(Long customerId) {
		return customerMeterMapper.selectCustomerCountByMeter(customerId);
	}
	
	@Override
	public List<Long>  getSubAccountCustomerId(Long customerId) {
		return customerMeterMapper.getSubAccountCustomerId(customerId);
	}
	
	@Override
	public List<CustomerMeter> getListByCustomerId(Long customerId) {
		CustomerMeter cm = new CustomerMeter();
		cm.setCustomerId(customerId);
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return customerMeterMapper.select(cm);
	}

	@Override
	public List<CustomerMeter> getListByMeterId(Long meterId) {
		CustomerMeter cm = new CustomerMeter();
		cm.setMeterId(meterId);
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return customerMeterMapper.select(cm);
	}

	@Override
	public int getCustomerBindedMeter(Long locationId) {
		return customerMeterMapper.getCustomerBindedMeter(locationId);
	}

	@Override
	public CustomerMeter getCustomerByMeterId(Long meterId) {
		return customerMeterMapper.getCustomerByMeterId(meterId);
	}

	@Override
	public int setDefaultCustomerStatusById(Long id, Integer status) {
		CustomerMeter cm = new CustomerMeter();
		cm.setId(id);
		cm.setDefaultCustomer(status);
		return customerMeterMapper.updateByPrimaryKeySelective(cm);
	}

	@Override
	public int setDefaultCustomerStatusNo(List<Long> idList) {
		int rows = 0;
		for(Long id : idList) {
			rows = this.setDefaultCustomerStatusNo(id);
			if(rows<=0) {
				break;
			}
		}
		return rows;
	}

	@Override
	public int setDefaultCustomerStatusNo(Long meterId) {
		
		Example example = new Example(CustomerMeter.class);
		example.createCriteria().andEqualTo("meterId", meterId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		//更新内容
		CustomerMeter cm = new CustomerMeter();
		cm.setDefaultCustomer(EnumDefaultStatus.DEFAULT_NO.getValue());
		return customerMeterMapper.updateByExampleSelective(cm, example);
	}

	@Override
	public int bind(Long customerId, Long meterId, Integer status) {
		CustomerMeter cm = new CustomerMeter();
		cm.setCustomerId(customerId);
		cm.setMeterId(meterId);
		cm.setBindTime(new Date());
		cm.setDefaultCustomer(status);
		cm.setMeterStatus(EnumMeterSettingStatus.ZERO.getValue());
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return customerMeterMapper.insertSelective(cm);
	}

	@Override
	public int unbind(Long customerId, Long meterId) {
		
		//解除客户-表计关系
		int rows = this.deleteByCustIdAndMeterId(customerId, meterId);
		
		//查询是否是一表多户
		CustomerMeter searchObj = new CustomerMeter();
		//searchObj.setCustomerId(customerId);
		searchObj.setMeterId(meterId);
		searchObj.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerMeter> cmList = customerMeterMapper.select(searchObj);
		if(cmList!=null && cmList.size()>0) {
			boolean isSetDefault = true;
			for(CustomerMeter cm : cmList) {
				int defaultCustomer = cm.getDefaultCustomer();//默认客户状态
				if(defaultCustomer==EnumDefaultStatus.DEFAULT_YES.getValue()) {
					isSetDefault = false;
					break;
				}
			}
			if(isSetDefault) {
				//设置第一个为默认客户
				CustomerMeter cm = cmList.get(0);
				this.setDefaultCustomerStatusById(cm.getId(), EnumDefaultStatus.DEFAULT_YES.getValue());
			}
		}
		return rows;
	}

	@Override
	public int setDefaultCustomer(Long customerId, Long meterId) {
		//设置此表的所有客户默认状态为否
		this.setDefaultCustomerStatusNo(meterId);
		
		//设置默认客户
		Example example = new Example(CustomerMeter.class);
		example.createCriteria().andEqualTo("meterId", meterId).andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		//更新内容
		CustomerMeter cm = new CustomerMeter();
		cm.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		return customerMeterMapper.updateByExampleSelective(cm, example);
	}
	
	@Override
	public List<CustomerMeter> getMeterHistoryMessage(Long customerId, String searchCond) {
		return customerMeterMapper.getMeterHistoryMessage(customerId, searchCond);
	}
	
	@Override
	public List<CustomerMeter> getCustomerHistoryMessage(Long meterId, String searchCond) {
		return customerMeterMapper.getCustomerHistoryMessage(meterId, searchCond);
	}

	@Override
	public String getMeterFactory(List<Long> cmIdList) {
		return customerMeterMapper.getMeterFactory(cmIdList);
	}

	@Override
	public String getMeterReadMode(List<Long> cmIdList) {
		return customerMeterMapper.getMeterReadMode(cmIdList);
	}
	
}
