package com.learnbind.ai.service.statistic.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.controller.location.CleanDataController;
import com.learnbind.ai.dao.CleanDataTempMapper;
import com.learnbind.ai.model.CleanDataTempTable;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.statistic.CleanDataTempService;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.service.statistic.impl
 *
 * @Title: StatisticServiceImpl.java
 * @Description: 统计服务
 *
 * @author lenovo
 * @date 2019年9月3日 上午12:00:05
 * @version V1.0 
 *
 */
@Service
public class CleanDataTempServiceImpl extends AbstractBaseService<CleanDataTempTable, Long> implements  CleanDataTempService {
	private static Log log = LogFactory.getLog(CleanDataTempServiceImpl.class);
	@Autowired
	public CleanDataTempMapper cleanDataTempMapper;
	@Autowired
	public CustomersService customersService;
	@Autowired
	public MetersService metersService;

	@Override
	public int batchCleanCustomerData(List<Customers> customersList) {
		int rows = 0;
		int result = 0;
		//2.1获取客户ID查询关联表信息
		for(Customers customer : customersList) {
			if(customer.getId().equals(57604l)) {
				continue;
			}
			log.debug("---------------------------------客户id： "+customer.getId());
			//2.2删除客户银行信息customer_bank
			//rows = this.deleteBank(customer.getId());
			//2.3删除客户开票信息customer_bill_info
			//rows = this.deleteBillInfo(customer.getId());
			//2.4删除客户押金信息CUSTOMER_PLEDGE
			//rows = this.deletePledge(customer.getId());
			//2.5删除客户多人口信息CUSTOMER_PEOPLE
			//rows = this.deletePeople(customer.getId());
			//2.6删除客户协议信息CUSTOMER_AGREEMENT
			//rows = this.deletdAgreeMent(customer.getId());
			//2.7删除客户违约金信息CUSTOMER_OVERDUE_FINE
			//rows = this.deletdOverdueFine(customer.getId());
			//2.8删除客户账户信息CUSTOMER_ACCOUNT
			//rows = this.deleteAccount(customer.getId());
			//2.9删除客户账单信息CUSTOMER_ACCOUNT_ITEM
			rows = this.deleteAccountItem(customer.getId());
			//2.10删除客户日志信息CUSTOMERS_TRACE
			//rows = this.deleteTrace(customer.getId());
			//2.9删除分水量信息PARTITION_WATER
			rows = this.deletedPartWater(customer.getId());
			//2.11删除客户表计关联表信息
			//rows = this.deleteMeter(customer.getId());
			//2.12删除客户地理位置关联表信息
			//rows = this.deleteLocation(customer.getId());
			//2.13删除客户表信息
			//rows = customersService.deleteByPrimaryKey(customer.getId());
			if(rows > 0) {
				result++;
			}
		}
			
		
		return result;
	}

	@Override
	public int batchCleanMeterData(List<Meters> metersList, String traceIds) {
		int rows = 0;
		int result = 0;
		//2.1获取表计ID查询关联表信息
		for(Meters meter : metersList) {
			if(meter.getId().equals(63757l) || meter.getId().equals(63758l) || meter.getId().equals(63759l)) {
				continue;
			}
			log.debug("---------------------------------表计id： "+meter.getId());
			//2.2删除表册-表计关联表信息METER_BOOK_METER
			//this.deleteMeterBookMeter(meter.getId());
			//2.3删除表册-用户关系表信息USER_METER_BOOK
			//userMeterBookService.deleted(meter.getId());
			//2,4删除表册信息METER_BOOK
			//meterBookService.deleted(meter.getId());
			//2.5删除APP抄表记录信息METER_RECORD_TEMP
			//this.deletedMeterRecordTemp(meter.getId());
			//2.6删除APP抄表记录照片信息METER_RECORD_TEMP_PHOTO
			//this.deletedMeterRecordTempPhoto(meter.getId());
			//2.7删除抄表记录照片信息METER_RECORD_PHOTO
			//this.deletedMeterRecordPhoto(meter.getId());
			//2.8删除抄表记录信息METER_RECORD
			rows = this.deletedMeterRecord(meter.getId());
			//2.11删除表计分水量规则信息METER_PART_WATER_RULE
			//this.deletedPartWaterRule(meter.getId());
			//2.12删除表计父子关系表信息METER_TREE
			//this.deletedMeterTree(meter.getId());
			//删除分水量规则日志表信息
			//rows = this.deletedPartWaterRuleTrace(meter.getId());
			//2.13删除表计地理位置关联表信息
			//this.deletedMeterLocation(meter.getId());
			//2.14删除表计信息
			//rows = metersService.deleteByPrimaryKey(meter.getId());
			if(rows > 0) {
				result++;
			}
		}
			
		
		return result;
	}

	@Override
	public int deleteBank(Long customerId) {
		return cleanDataTempMapper.deleteBank(customerId);
	}

	@Override
	public int deleteBillInfo(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deleteBillInfo(customerId);
	}

	@Override
	public int deletePledge(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletePledge(customerId);
	}

	@Override
	public int deletePeople(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletePeople(customerId);
	}

	@Override
	public int deletdAgreeMent(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletdAgreeMent(customerId);
	}

	@Override
	public int deletdOverdueFine(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletdOverdueFine(customerId);
	}

	@Override
	public int deleteAccount(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deleteAccount(customerId);
	}

	@Override
	public int deleteAccountItem(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deleteAccountItem(customerId);
	}

	@Override
	public int deleteTrace(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deleteTrace(customerId);
	}

	@Override
	public int deleteMeter(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deleteMeter(customerId);
	}

	@Override
	public int deleteLocation(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deleteLocation(customerId);
	}


	@Override
	public int deleteMeterBookMeter(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deleteMeterBookMeter(meterId);
	}

	@Override
	public int deletedMeterRecordTemp(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedMeterRecordTemp(meterId);
	}

	@Override
	public int deletedMeterRecordTempPhoto(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedMeterRecordTempPhoto(meterId);
	}

	@Override
	public int deletedMeterRecordPhoto(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedMeterRecordPhoto(meterId);
	}

	@Override
	public int deletedMeterRecord(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedMeterRecord(meterId);
	}

	@Override
	public int deletedPartWater(Long customerId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedPartWater(customerId);
	}

	@Override
	public int deletedPartWaterRule(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedPartWaterRule(meterId);
	}
	
	@Override
	public int deletedPartWaterRuleTrace(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedPartWaterRuleTrace(meterId);
	}

	@Override
	public int deletedMeterTree(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedMeterTree(meterId);
	}

	@Override
	public int deletedMeterLocation(Long meterId) {
		// TODO Auto-generated method stub
		return cleanDataTempMapper.deletedMeterLocation(meterId);
	}



		
	
	
}
