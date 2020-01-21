package com.learnbind.ai.service.meterrecord.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.common.enumclass.EnumAppReadResult;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterRecordStatus;
import com.learnbind.ai.common.enumclass.EnumReadType;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.dao.MeterRecordTempMapper;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.MeterRecordPhoto;
import com.learnbind.ai.model.MeterRecordTemp;
import com.learnbind.ai.model.MeterRecordTempPhoto;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.meterrecord.MeterRecordPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.meterrecord.impl
 *
 * @Title: MeterRecordTempServiceImpl.java
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 * @author Thinkpad
 * @date 2019年6月20日 下午1:33:34
 * @version V1.0 
 *
 */
@Service
public class MeterRecordTempServiceImpl extends AbstractBaseService<MeterRecordTemp, Long> implements  MeterRecordTempService {
	
	@Autowired
	public MeterRecordTempMapper meterRecordTempMapper;
	
	@Autowired
	public CustomerMeterService customerMeterService;
	@Autowired
	public MeterRecordService meterRecordService;
	@Autowired
	private MeterRecordPhotoService meterRecordPhotoService;
	@Autowired
	public MeterRecordTempPhotoService meterRecordTempPhotoService;
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;//表计规则服务
	@Autowired
	private MeterPartWaterRuleTraceService meterPartWaterRuleTraceService;//表计规则日志服务
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterRecordTempServiceImpl(MeterRecordTempMapper mapper) {
		this.meterRecordTempMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	@Transactional
	public int insertBatch(List<MeterRecordTemp> recordTempList) {
		int rows = 0;
		for(MeterRecordTemp recordTemp : recordTempList) {
			//设置删除状态为未删除
			recordTemp.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			
			rows = meterRecordTempMapper.insertSelective(recordTemp);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCustomers
	 * @Description: 查询客户档案
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#searchCustomers(java.lang.String)
	 */
	@Override
	public List<MeterRecordTemp> searchAppMeterRecord(String searchCond, Long operatorId) {
		return meterRecordTempMapper.searchAppMeterRecord(searchCond, operatorId);
	}
	
	
	@Override
	@Transactional
	public int deleteAppMeterRecord(long id) {
		try {
			int rows = meterRecordTempMapper.deleteByPrimaryKey(id);
			if(rows>0) {
				MeterRecordTemp meterRecordTemp = new MeterRecordTemp();
				meterRecordTemp.setId(id);
				}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: confirmMeterRecord
	 * @Description: 确认app抄表记录
	 * @param operatorId
	 * @param operatorName
	 * @param period
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.MeterRecordTempService#confirmMeterRecord(java.lang.Long, java.lang.String, java.lang.String)
	 */
	@Override
	//@Transactional
	public int confirmMeterRecord(Long operatorId, String operatorName, List<MeterRecordTemp> appRecordList, String period) {
		return this.appRecordSaveToRecord(operatorId, operatorName,appRecordList, period);//转存APP抄表记录表数据到抄表记录表
	}
	/**
	 * @Title: appRecordSaveToRecord
	 * @Description: 转存APP抄表记录表数据到抄表记录表（包括抄表记录和抄表记录照片）
	 * @param operatorId
	 * @param operatorName
	 * @param period
	 * @return 
	 */
	//@Transactional
	private int appRecordSaveToRecord(Long operatorId, String operatorName, List<MeterRecordTemp> appRecordList, String period) {
		
		//Date sysDate = new Date();//系统时间
		
		int rows = -1;
		for(MeterRecordTemp mrt : appRecordList) {
			
			Integer readResult = mrt.getReadResult();//抄表结果
			if(readResult==EnumAppReadResult.RESULT_NORMAL.getValue() || readResult==EnumAppReadResult.RESULT_MANUAL.getValue() || readResult==EnumAppReadResult.RESULT_MANUAL_EDIT.getValue()) {
				
				rows = this.appRecordSaveToRecord(mrt, period, operatorId, operatorName);
				if(rows<=0) {
					//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
			
		}
		return rows;
	}
	
	/**
	 * @Title: appRecordSaveToRecord
	 * @Description: 保存单条APP抄表记录
	 * @param mrt
	 * @param period
	 * @param operatorId
	 * @param operatorName
	 * @return 
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	private int appRecordSaveToRecord(MeterRecordTemp mrt, String period, Long operatorId, String operatorName) {
		
		try {
			int rows = 0;
			//删除APP抄表记录
			meterRecordTempMapper.deleteByPrimaryKey(mrt.getId());
			
			//把APP抄表记录转存到抄表记录中
			MeterRecord record = this.getMeterRecord(mrt, period, operatorId, operatorName);
			rows = meterRecordService.insertSelective(record);
			//rows = meterRecordService.insertMeterRecord(record, operatorId, operatorName);
			if(rows>0) {
				
				//增加使用表计规则日志
				List<MeterPartWaterRule> meterRuleList = meterPartWaterRuleService.getMeterRule(record.getMeterId());
				if(meterRuleList!=null && meterRuleList.size()>0) {
					meterPartWaterRuleTraceService.insertTraceList(record.getId(), meterRuleList);
				}
				
				//把APP抄表记录照片保存到抄表记录照片
				MeterRecordPhoto photo = this.getMeterRecordPhoto(mrt.getCustomerId(), mrt.getMeterId(), mrt.getOperatorId());
				if(photo!=null) {
					meterRecordTempPhotoService.deleteByPrimaryKey(photo.getId());//删除原APP抄表记录照片
					photo.setId(null);
					photo.setPeriod(period);
					photo.setRecordId(record.getId());
					rows = meterRecordPhotoService.insertSelective(photo);
				}
				
			}
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: getMeterRecord
	 * @Description: 获取抄表记录实体类
	 * @param mrt
	 * @param period
	 * @param operatorId
	 * @param operatorName
	 * @return 
	 */
	private MeterRecord getMeterRecord(MeterRecordTemp mrt, String period, Long operatorId, String operatorName) {
		MeterRecord meterRecord = new MeterRecord();
		meterRecord.setCustomerId(mrt.getCustomerId());
		meterRecord.setMeterId(mrt.getMeterId());
		meterRecord.setPeriod(period);
		meterRecord.setCurrDate(mrt.getCurrDate());
		meterRecord.setCurrRead(mrt.getCurrRead());
		meterRecord.setPreDate(mrt.getPreDate());
		meterRecord.setPreRead(mrt.getPreRead());
		meterRecord.setCurrAmount(mrt.getCurrAmount());
		meterRecord.setRemark(mrt.getRemark());
		meterRecord.setReadMode(mrt.getReadMode());
		//如果APP抄表记录的结果为手工修改，设置抄表记录为手工修改，否则设置为正常。
		if(mrt.getReadResult() == EnumAppReadResult.RESULT_MANUAL_EDIT.getValue()) {
			meterRecord.setStatus(EnumMeterRecordStatus.MANUAL_EDIT.getValue());
		} else {
			meterRecord.setStatus(EnumMeterRecordStatus.NORMAL.getValue());
		}
		meterRecord.setReadType(EnumReadType.NORMAL_READ.getValue());
		meterRecord.setOperationTime(new Date());
		meterRecord.setOperatorId(operatorId);
		meterRecord.setOperatorName(operatorName);
		meterRecord.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return meterRecord;
	}
	/**
	 * @Title: getMeterRecordPhoto
	 * @Description: 获取抄表记录照片实体类
	 * @param customerId
	 * @param meterId
	 * @param operatorId
	 * @return 
	 */
	private MeterRecordPhoto getMeterRecordPhoto(Long customerId, Long meterId, Long operatorId) {
		List<MeterRecordTempPhoto> tempPhotoList = meterRecordTempPhotoService.getList(customerId, meterId, operatorId);
		if(tempPhotoList!=null && tempPhotoList.size()>0) {
			MeterRecordTempPhoto tempPhoto = tempPhotoList.get(0);
			Map<String, Object> tempPhotoMap = EntityUtils.entityToMap(tempPhoto);
			MeterRecordPhoto photo = EntityUtils.mapToEntity(tempPhotoMap, MeterRecordPhoto.class);
			return photo;
		}
		
		return null;
	}

	@Override
	public Integer deleteAppMeterRecordList(List<MeterRecordTemp> appMeterRecordList) {
		int rows = 0;
		for (MeterRecordTemp temp : appMeterRecordList) {
			rows = meterRecordTempMapper.delete(temp);
			if (rows <= 0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}

	@Override
	public List<Map<String, Object>> getListGroupByCustomer(Long customerId, String searchCond, Integer readResult, String readMode,
			Long operatorId, String traceIds, String startDate, String endDate) {
		return meterRecordTempMapper.getListGroupByCustomer(customerId, searchCond, readResult, readMode, operatorId, traceIds, startDate, endDate);
	}

	@Override
	public List<MeterRecordTemp> getListByCustomerId(Long customerId) {
		MeterRecordTemp record = new MeterRecordTemp();
		record.setCustomerId(customerId);
		return meterRecordTempMapper.select(record);
	}

	@Override
	public List<MeterRecordTemp> getConfirmAppMeterRecord(Long customerId, String searchCond, Integer readResult,String readMode,
			Long operatorId, String traceIds) {
		return meterRecordTempMapper.getConfirmAppMeterRecord(customerId, searchCond, readResult, readMode, operatorId, traceIds);
	}

	@Override
	public List<Map<String, Object>> getExportMeterRecordTempData(String searchCond, Integer readResult,
			String traceIds, Integer readMode) {
		return meterRecordTempMapper.getExportMeterRecordTempData(searchCond, readResult, traceIds, readMode);
	}
	
	 @Override
		public List<Map<String, Object>> getRepeatListGroupByCustomer(Long customerId, String searchCond, Integer readResult, String readMode,
				Long operatorId, String traceIds, String startDate, String endDate) {
			return meterRecordTempMapper.getRepeatListGroupByCustomer(customerId, searchCond, readResult, readMode, operatorId, traceIds, startDate, endDate);
		}

	@Override
	public List<Map<String, Object>> getExportMoreMeterRecordTempData(String searchCond, Integer readResult,
			String traceIds, Integer readMode) {
		return meterRecordTempMapper.getExportMoreMeterRecordTempData(searchCond, readResult, traceIds, readMode);
	}

}
