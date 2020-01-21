package com.learnbind.ai.service.addsubwater.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumAddSubWaterStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerChargeMode;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.dao.AddSubWaterMapper;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.service.addsubwater.AddSubWaterService;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;

import tk.mybatis.mapper.entity.Example;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.addsubwater.impl
 *
 * @Title: AddSubWaterServiceImpl.java
 * @Description: 增加减免水量
 *
 * @author Thinkpad
 * @date 2019年6月5日 下午12:10:20
 * @version V1.0 
 *
 */
@Service
public class AddSubWaterServiceImpl extends AbstractBaseService<AddSubWater, Long> implements  AddSubWaterService {
	
	private static Log log = LogFactory.getLog(AddSubWaterServiceImpl.class);
	
	@Autowired
	public AddSubWaterMapper addSubWaterMapper;
	@Autowired
	public PartitionWaterService partitionWaterService;
	@Autowired
	public MeterRecordService meterRecordService;
	@Autowired
	public CustomersService customersService;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public AddSubWaterServiceImpl(AddSubWaterMapper mapper) {
		this.addSubWaterMapper=mapper;
		this.setMapper(mapper);
	}
	
	/**
	 * @Title: saveList
	 * @Description: 保存追加减免水量
	 * @param customerId
	 * @param meterId
	 * @param awList
	 * @return 
	 */
	@Override
	@Transactional
	public int saveList(Long customerId, String period, String meterIds, List<AddSubWater> awList, String recordIds) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int rows = 0;
		
		for(AddSubWater aw : awList) {
			
			BigDecimal waterAfter = aw.getWaterBefore().add(aw.getCompensation());
			
			aw.setMeterId(meterIds);
			aw.setCustomerId(customerId);
			aw.setPeriod(period);
			aw.setWaterAfter(waterAfter);
			aw.setOperationTime(new Date());
			aw.setOperatorId(userBean.getId());
			aw.setOperatorName(userBean.getRealname());
			
			rows = addSubWaterMapper.insertSelective(aw);//记录增加减免水量日志
			if(rows>0) {
				rows = this.updatePartitionWaterAmount(aw.getPartitionWaterId(), waterAfter);
			} 
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			
		}
		
		//获取抄表记录ID集合
		List<Long> recordIdList = new ArrayList<>();
		if(StringUtils.isNotBlank(recordIds)) {
			String[] recordIdArr = recordIds.split(",");
			for(String recordId : recordIdArr) {
				if(StringUtils.isNotBlank(recordId)) {
					recordIdList.add(Long.valueOf(recordId));
				}
			}
		}
		//追加减免水量成功后修改抄表记录追加减免水量水量状态
		for(Long recordId : recordIdList) {
			MeterRecord meterRecord = meterRecordService.selectByPrimaryKey(recordId);
			//追加减免水量状态改为已增加/减免
			meterRecord.setIsAddSubWater(EnumAddSubWaterStatus.ADD_SUB_WATER_YES.getValue());
			meterRecordService.updateByPrimaryKeySelective(meterRecord);
		}
		return rows;
	}
	
	@Override
	@Transactional
	public int saveList(Long customerId, String period, String meterIds, String recordIds, JSONObject pwJsonOjb) {
		
		int rows = 0;
		Date sysDate = new Date();//系统日期
		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型的值0，主要用于判断
		
		String compensation = pwJsonOjb.getString("compensation");//追加减免水量
		String waterBefore = pwJsonOjb.getString("waterBefore");//追加减免前的水量
		String partitionWaterId = pwJsonOjb.getString("partitionWaterId");//水价ID
		BigDecimal compensationBd = new BigDecimal(compensation);//追加减免水量
		BigDecimal waterBeforeBd = new BigDecimal(waterBefore);//追加减免前的水量
		BigDecimal waterAfterBd = BigDecimalUtils.add(waterBeforeBd, compensationBd);//追加减免后的水量
		
		//获取表计ID
		//Long meterId = this.getMeterId(meterIds);
		//获取抄表记录
		MeterRecord record = this.getMeterRecord(recordIds, waterAfterBd);
		
		//查询客户信息
		Customers customer = customersService.selectByPrimaryKey(customerId);
		Integer chargeMode = customer.getChargeMode();//收费方式，0：合并收费，1单独收费；针对一表多户
		log.debug("----------客户收费方式:"+EnumCustomerChargeMode.getName(chargeMode));
		
		if(compensationBd!=null && !BigDecimalUtils.equals(compensationBd, zero)) {
			//rows = this.insertNotMergePartitionWater(customerId, period, meterIds, recordIds, record, compensationBd, sysDate);
			rows = this.insertNotMergePartitionWater(customerId, period, meterIds, recordIds, record, sysDate);
			
//			boolean isInsertFlag = partitionWaterService.isInsertPartitionWater(customerId, meterId);
//			//如果客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,可创建分水量记录,否则不增加分水量记录;
//			if(isInsertFlag) {
//				//客户类型-个人
//				Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
//				if(customer.getCustomerType()==customerType) {
//					Integer chargeModeMerge = EnumCustomerChargeMode.COMBINE_CHARGE.getValue();//收费方式（针对一表多户） 0=合并收费
//					//如果收费方式为合并收费,多条抄表记录合并生成一个分水量记录
//					if(chargeMode==chargeModeMerge) {//收费方式为合并收费
//						//合并收费-增加本期分水量
//						rows = partitionWaterService.insertMergePartitionWater(customerId, period, compensationBd, record.getPreDate(), record.getCurrDate(), sysDate);
//					}else {//收费方式为单独收费
//						//rows = partitionWaterService.insertPartitionWater(record, sysDate);
//						rows = this.insertNotMergePartitionWater(customerId, period, meterIds, recordIds, record, compensationBd, sysDate);
//					}
//				}else {
//					rows = this.insertNotMergePartitionWater(customerId, period, meterIds, recordIds, record, compensationBd, sysDate);
//				}
//				
//			}else {
//				log.debug("本次抄表记录不满足创建本期分水量记录的条件!");
//				log.debug("创建本期分水量的条件:客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,可创建分水量记录");
//				return -1;
//			}
		}else {
			log.info("追加减免的水量为空或等于零！");
			return -1;
		}
		
		if(rows>0) {
			//增加追加/减免水量日志
			rows = this.insertAddSubWaterTrace(waterBeforeBd, compensationBd, record.getId(), customerId, meterIds, period, Long.valueOf(partitionWaterId));
			
			//修改追加/减免水量状态为已追加/减免
			rows = this.updateAddSubWaterYes(recordIds);
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return rows;
	}
	
	@Override
	@Transactional
	public int saveAddSubWater(Long partWaterId, JSONObject pwJsonOjb) {
		
		String compensation = pwJsonOjb.getString("compensation");//追加减免水量
		String waterBefore = pwJsonOjb.getString("waterBefore");//追加减免前的水量
		//String partitionWaterId = pwJsonOjb.getString("partitionWaterId");//水价ID
		BigDecimal compensationBd = new BigDecimal(compensation);//追加减免水量
		BigDecimal waterBeforeBd = new BigDecimal(waterBefore);//追加减免前的水量
		BigDecimal waterAfterBd = BigDecimalUtils.add(waterBeforeBd, compensationBd);//追加减免后的水量
		
		PartitionWater pw = partitionWaterService.selectByPrimaryKey(partWaterId);
		BigDecimal waterPrice = pw.getWaterPrice();
		BigDecimal waterFee = BigDecimalUtils.multiply(waterPrice, waterAfterBd);
		pw.setRealWaterAmount(waterAfterBd);
		pw.setWaterFee(waterFee);
		int rows = partitionWaterService.updateByPrimaryKeySelective(pw);
		if(rows>0) {
			String recordIdStr = pw.getRecordId();
			Long recordId = null;
			if(StringUtils.isNotBlank(recordIdStr)) {
				recordId = Long.valueOf(recordIdStr);
			}
			//增加追加/减免水量日志
			rows = this.insertAddSubWaterTrace(waterBeforeBd, compensationBd, recordId, pw.getCustomerId(), pw.getMeterId(), pw.getPeriod(), partWaterId);
			
			//修改追加/减免水量状态为已追加/减免
			rows = this.updateAddSubWaterYes(recordIdStr);
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return rows;
	}

	/**
	 * @Title: insertNotMergePartitionWater
	 * @Description: 增加单独收费分水量
	 * @param customerId
	 * @param period
	 * @param meterIds
	 * @param recordIds
	 * @param record
	 * @param compensation
	 * @param sysDate
	 * @return 
	 */
	//private int insertNotMergePartitionWater(Long customerId, String period, String meterIds, String recordIds, MeterRecord record, BigDecimal compensation, Date sysDate) {
	private int insertNotMergePartitionWater(Long customerId, String period, String meterIds, String recordIds, MeterRecord record, Date sysDate) {
		
		this.deletePartitionWater(customerId, recordIds, meterIds, period);
		
//		BigDecimal currAmount = record.getCurrAmount();
//		currAmount = BigDecimalUtils.add(currAmount, compensation);//本期水量+增加减免水量
//		record.setCurrAmount(currAmount);
		
		return partitionWaterService.insertPartitionWater(record, sysDate);
	}
	
	/**
	 * @Title: deletePartitionWater
	 * @Description: 删除本期分水量
	 * @param customerId
	 * @param recordId
	 * @param meterId
	 * @param period 
	 */
	private void deletePartitionWater(Long customerId, String recordId, String meterId, String period) {
		PartitionWater pw = new PartitionWater();
		pw.setCustomerId(customerId);
		pw.setRecordId(recordId);
		pw.setMeterId(meterId);
		pw.setPeriod(period);
		pw.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<PartitionWater> pwList = partitionWaterService.select(pw);
		if(pwList!=null && pwList.size()>0) {
			pw = pwList.get(0);
			pw.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			partitionWaterService.updateByPrimaryKeySelective(pw);
		}
	}
	
	/**
	 * @Title: getMeterId
	 * @Description: 获取表计ID
	 * @param meterIds
	 * @return 
	 */
//	private Long getMeterId(String meterIds) {
//		//获取表计ID
//		String[] meterIdArr = meterIds.split(",");
//		Long meterId = 0l;
//		if(meterIdArr!=null && meterIdArr.length>0) {
//			String meterIdStr = meterIdArr[0];
//			if(StringUtils.isNotBlank(meterIdStr)) {
//				meterId = Long.valueOf(meterIdStr);
//			}
//		}
//		return meterId;
//	}
	/**
	 * @Title: getMeterRecord
	 * @Description: 获取抄表记录
	 * @param recordIds
	 * @return 
	 */
	private MeterRecord getMeterRecord(String recordIds, BigDecimal compensationBd) {
		//获取抄表记录ID
		String[] recordIdArr = recordIds.split(",");
		Long recordId = 0l;
		if(recordIdArr!=null && recordIdArr.length>0) {
			String recordIdStr = recordIdArr[0];
			if(StringUtils.isNotBlank(recordIdStr)) {
				recordId = Long.valueOf(recordIdStr);
			}
		}
		if(recordId<=0) {
			return null;
		}
		MeterRecord record = meterRecordService.selectByPrimaryKey(recordId);
		record.setPreRead(null);
		record.setCurrRead(null);
		
		record.setCurrAmount(compensationBd);
		return record;
	}
	
	/**
	 * @Title: insertAddSubWaterTrace
	 * @Description: 增加追加/减免水量日志
	 * @param waterBeforeBd
	 * @param compensationBd
	 * @param recordId
	 * @param customerId
	 * @param meterIds
	 * @param period
	 * @return 
	 */
	private int insertAddSubWaterTrace(BigDecimal waterBeforeBd, BigDecimal compensationBd, Long recordId, Long customerId, String meterIds, String period, Long partitionWaterId) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		AddSubWater aw = new AddSubWater();
		BigDecimal waterAfter = BigDecimalUtils.add(waterBeforeBd, compensationBd);//补偿后的水量
		
		aw.setCompensation(compensationBd);//补偿量

		aw.setCustomerId(customerId);
		aw.setMeterId(meterIds);
		aw.setPeriod(period);
		aw.setWaterBefore(waterBeforeBd);//补偿前的水量
		aw.setWaterAfter(waterAfter);//补偿后的水量
		aw.setOperationTime(new Date());
		aw.setOperatorId(userBean.getId());
		aw.setOperatorName(userBean.getRealname());
		aw.setPartitionWaterId(partitionWaterId);
		aw.setMeterRecordId(recordId);//抄表记录ID，一户多表时显示其中一个抄表ID
		
		return addSubWaterMapper.insertSelective(aw);//记录增加减免水量日志
	}
	
	/**
	 * @Title: updateAddSubWaterYes
	 * @Description: 修改追加/减免水量状态为已追加/减免
	 * @param recordIds
	 * @return 
	 */
	private int updateAddSubWaterYes(String recordIds) {
		//获取抄表记录ID集合
		List<Long> recordIdList = new ArrayList<>();
		if(StringUtils.isNotBlank(recordIds)) {
			String[] recordIdArr = recordIds.split(",");
			for(String recordId : recordIdArr) {
				if(StringUtils.isNotBlank(recordId)) {
					recordIdList.add(Long.valueOf(recordId));
				}
			}
		}
		int rows = 0;
		//追加减免水量成功后修改抄表记录追加减免水量水量状态
		for(Long recordId : recordIdList) {
			MeterRecord meterRecord = meterRecordService.selectByPrimaryKey(recordId);
			//追加减免水量状态改为已增加/减免
			meterRecord.setIsAddSubWater(EnumAddSubWaterStatus.ADD_SUB_WATER_YES.getValue());
			rows = meterRecordService.updateByPrimaryKeySelective(meterRecord);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}
	
	/**
	 * @Title: updatePartitionWaterAmount
	 * @Description: 修改分水量记录表的总水量
	 * @param partitionWaterId
	 * @param waterAmount
	 * @return 
	 */
	@Transactional
	private int updatePartitionWaterAmount(Long partitionWaterId, BigDecimal waterAmount) {
		
		
		
		PartitionWater partitionWater = new PartitionWater();
		partitionWater.setId(partitionWaterId);
		partitionWater.setWaterAmount(waterAmount);
		
		return partitionWaterService.updateByPrimaryKeySelective(partitionWater);
	}

	@Override
	public AddSubWater getAddSubLog(Long customerId, String period, Long partwaterId) {
		Example example = new Example(AddSubWater.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("period", period).andEqualTo("partitionWaterId", partwaterId);
		List<AddSubWater> waterList = this.selectByExample(example);
		if(waterList.size() > 0) {
			return waterList.get(0);
		}
		return null;
	}

}
