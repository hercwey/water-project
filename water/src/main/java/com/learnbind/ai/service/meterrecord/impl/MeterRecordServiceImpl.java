package com.learnbind.ai.service.meterrecord.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMakeBillStatus;
import com.learnbind.ai.common.enumclass.EnumPartitionWaterStatus;
import com.learnbind.ai.common.enumclass.EnumReadType;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.dao.MeterRecordMapper;
import com.learnbind.ai.jsengine.JSEngineUtils;
import com.learnbind.ai.jsengine.PartitionRuleUtil;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.MeterTree;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomerPeopleAdjustService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;
import com.learnbind.ai.service.meters.MeterTreeService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.peopleadjust.PeopleAdjustService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.meterrecord.impl
 *
 * @Title: MeterRecordServiceImpl.java
 * @Description: 抄表记录服务实现
 *
 * @author Administrator
 * @date 2019年6月17日 下午9:25:14
 * @version V1.0 
 *
 */
@Service
public class MeterRecordServiceImpl extends AbstractBaseService<MeterRecord, Long> implements  MeterRecordService {
	
	private static Log log = LogFactory.getLog(MeterRecordServiceImpl.class);
	
	@Autowired
	public MeterRecordMapper meterRecordMapper;
	@Autowired
	private CustomerMeterService customerMeterService;  //客户-表计关系服务
	@Autowired
	private PartitionWaterService partitionWaterService;  //分水量服务
	@Autowired
	public MetersService metersService;//表计
	@Autowired
	public MeterTreeService meterTreeService;//表计父子关系
	@Autowired
	public WaterPriceService waterPriceService;//水价
	@Autowired
	public CustomerAccountItemService customerAccountItemService;//客户账目
	@Autowired
	public CustomerPeopleAdjustService customerPeopleAdjustService;//多人口调整记录
	@Autowired
	public PeopleAdjustService peopleAdjustService;//多人口调整配置
	@Autowired
	public DiscountService discountService;//政策减免配置
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;//表计规则服务
	@Autowired
	private MeterPartWaterRuleTraceService meterPartWaterRuleTraceService;//表计规则日志服务
	@Autowired
	private PartitionRuleUtil partitionRuleUtil;//分水量规则工具类
	@Autowired
	private JSEngineUtils jSEngineUtils;//执行JS SCRIPT脚本工具类
	
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterRecordServiceImpl(MeterRecordMapper mapper) {
		this.meterRecordMapper=mapper;
		this.setMapper(mapper);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchMeterRecord
	 * @Description: 查询抄表记录
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.MeterRecordService#searchMeterRecord(java.lang.String)
	 */
//	@Override
//	public List<MeterRecord> searchMeterRecord(String searchCond) {
//		return meterRecordMapper.searchMeterRecord(searchCond);
//	}
	
	@Override
	public List<Map<String, Object>> getMeterRecordList(Long customerId, String searchCond, Integer readType,
			Long operatorId, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer meterRecordStatus, Integer currAmount) {
		return meterRecordMapper.getMeterRecordList(customerId, searchCond, readType, operatorId, traceIds, period, isPartWater, startDate, endDate, meterRecordStatus, currAmount);
	}
	
	@Override
	public Map<String, Object> getMeterRecorStatisticMap(Long customerId, String searchCond, Integer readType,
			Long operatorId, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer meterRecordStatus, Integer currAmount) {
		return meterRecordMapper.getMeterRecorStatisticMap(customerId, searchCond, readType, operatorId, traceIds, period, isPartWater, startDate, endDate, meterRecordStatus, currAmount);
	}
	
	@Override
	public List<MeterRecord> searchMeterRecord(String period, String traceIds, Integer readType, String searchCond) {
		return meterRecordMapper.searchMeterRecord(period, traceIds, readType, searchCond);
	}

	@Override
	public List<Map<String, Object>> getListGroupByCustomerAndPeriod(Long customerId, String period, String searchCond, Integer readType, Long operatorId, String traceIds, Integer isAddSubWater, Integer isPartWater, String startDate, String endDate) {
		return meterRecordMapper.getListGroupByCustomerAndPeriod(customerId, period, searchCond, readType, operatorId, traceIds, isAddSubWater, isPartWater, startDate, endDate);
	}

	@Override
	public List<MeterRecord> getListByCustomerIdAndPeriod(Long customerId, String period, String traceIds) {
		return meterRecordMapper.getListByCustomerIdAndPeriod(customerId, period, traceIds);
	}
	
	@Override
	public List<MeterRecord> getListByRecordIdList(String[] recordIdArr) {
		return meterRecordMapper.getListByRecordIdList(recordIdArr);
	}

	@Override
	public MeterRecord getLastMeterRecord(Long customerId, String period, Long meterId) {
		MeterRecord meterRecord = meterRecordMapper.getLastMeterRecord(customerId, period, meterId);
		return meterRecord;
	}

	/** 
	 * @Title: insertMeterRecord
	 * @Description: 增加抄表记录，同时增加分水量
	 * @param record
	 * @param operatorId
	 * @param operatorName
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.MeterRecordService#insertMeterRecord(com.learnbind.ai.model.MeterRecord, java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public int insertMeterRecord(MeterRecord record, Long operatorId, String operatorName) {
		
		Date sysDate = new Date();//系统日期
		
		Date currDate = record.getCurrDate();//本期抄表日期
		if(currDate==null) {
			record.setCurrDate(sysDate);//本期抄表日期为null时，设置默认值为系统日期
		}
		
		Long customerId = record.getCustomerId();//客户ID
//		String period = record.getPeriod();//抄表期间
		Long meterId = record.getMeterId();//表计ID
		//查询到的最后一条抄表记录
		MeterRecord preRecord = this.getLastMeterRecord(customerId, null, meterId);
		if(preRecord!=null) {//如果有上期抄表记录则设置本期抄表记录中的上期抄表日期和上期抄表读数
			record.setPreDate(preRecord.getCurrDate());
			record.setPreRead(preRecord.getCurrRead());
		}//如果没有上期抄表记录则为空
			
		//获取本期水量	本期水量=本期抄表读数-上期抄表读数
		BigDecimal currAmount = this.getCurrWaterAmount(record.getCurrRead(), record.getPreRead());
		record.setCurrAmount(currAmount);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setOperatorId(operatorId);
		record.setOperatorName(operatorName);
		record.setOperationTime(sysDate);
		
		String meterUse = this.getMeterUse(meterId);//获取表计用途
		Long meterTreeId = this.getMeterTreeId(meterId);//获取表计父子关系ID
		record.setMeterUse(meterUse);//水表用途
		record.setMeterTreeId(meterTreeId);//表计父子关系ID
		
		int rows = meterRecordMapper.insertSelective(record);//增加抄表记录
		if(rows>0) {

			//增加使用表计规则日志
			List<MeterPartWaterRule> meterRuleList = meterPartWaterRuleService.getMeterRule(record.getMeterId());
			if(meterRuleList!=null && meterRuleList.size()>0) {
				meterPartWaterRuleTraceService.insertTraceList(record.getId(), meterRuleList);
			}
			
//			//查询客户信息
//			Customers customer = customersService.selectByPrimaryKey(customerId);
//			Integer chargeMode = customer.getChargeMode();//收费方式，0：合并收费，1单独收费；针对一表多户
//			log.debug("----------客户收费方式:"+EnumCustomerChargeMode.getName(chargeMode));
//			
//			if(StringUtils.isNotBlank(record.getPreRead()) && record.getCurrAmount()!=null) {
//				
//				boolean isInsertFlag = partitionWaterService.isInsertPartitionWater(customerId, meterId);
//				//如果客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,可创建分水量记录,否则不增加分水量记录;
//				if(isInsertFlag) {
//					//客户类型-个人
//					Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
//					if(customer.getCustomerType()==customerType) {
//						Integer chargeModeMerge = EnumCustomerChargeMode.COMBINE_CHARGE.getValue();//收费方式（针对一表多户） 0=合并收费
//						//如果收费方式为合并收费,多条抄表记录合并生成一个分水量记录
//						if(chargeMode==chargeModeMerge) {//收费方式为合并收费
//							//合并收费-增加本期分水量
//							rows = partitionWaterService.insertMergePartitionWater(customerId, period, currAmount, record.getPreDate(), record.getCurrDate(), sysDate);
//						}else {//收费方式为单独收费
//							rows = partitionWaterService.insertPartitionWater(record, sysDate);
//						}
//					}else {
//						rows = partitionWaterService.insertPartitionWater(record, sysDate);
//					}
//					
//				}else {
//					log.debug("本次抄表记录不满足创建本期分水量记录的条件!");
//					log.debug("创建本期分水量的条件:客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,可创建分水量记录");
//				}
//			}else {
//				log.info("本期抄表记录未记录分水量，原因为上期抄表读数为空！");
//			}
//			if(rows<=0) {
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//			}
			
		}
		
		return rows;
	}
	
	/**
	 * @Title: getMeterUse
	 * @Description: 获取表计用途
	 * @param meterId
	 * @return 
	 */
	@Override
	public String getMeterUse(Long meterId) {
		Meters meter = metersService.selectByPrimaryKey(meterId);
		if(meter!=null) {
			return meter.getMeterUse();
		}
		return null;
	}
	
	/**
	 * @Title: getMeterTreeId
	 * @Description: 获取表计父子关系ID
	 * @param meterId
	 * @return 
	 */
	@Override
	public Long getMeterTreeId(Long meterId) {
		MeterTree meterTree = new MeterTree();
		meterTree.setMeterId(meterId);
		meterTree.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<MeterTree> recordList = meterTreeService.select(meterTree);
		if(recordList!=null && recordList.size()>0) {
			MeterTree record = recordList.get(0);
			return record.getId();
		}
		return null;
	}
	
	/**
	 * @Title: isInsertPartitionWater
	 * @Description: 判断是否需要增加分水量
	 * @param meterId
	 * @param customerStatus
	 * @param waterStatus
	 * @return 
	 */
//	private boolean isInsertPartitionWater(Long meterId, Integer customerStatus, Integer waterStatus) {
//		//Integer customerStatus = customer.getStatus();//客户状态：-1=未立户；0=已立户；1=已销户；2=已过户；3=过户；
//		log.debug("----------客户账户状态:"+EnumCustomerStatus.getName(customerStatus));
//		//Integer waterStatus = customer.getWaterStatus();//用水状态:0=正常;1=停水
//		log.debug("----------客户用水状态:"+EnumWaterStatus.getName(waterStatus));
//		//Integer chargeMode = customer.getChargeMode();//收费方式，0：合并收费，1单独收费；针对一表多户
//		//log.debug("----------客户收费方式:"+EnumCustomerChargeMode.getName(chargeMode));
//		//查询表计信息
//		Meters meter = metersService.selectByPrimaryKey(meterId);
//		Integer virtualReal = meter.getVirtualReal();//虚/实表，0：实表（默认）/1：虚表
//		log.debug("----------表计虚/实状态:"+EnumMeterVirtualReal.getName(virtualReal));
//		Integer cycleStatus = meter.getCycleStatus();//记录水表的生命周期状态 0：待用（待使用）（默认值）；1：领用；2：待启用；3：使用中；4：待检测；5：检测中；6：待检修；7：检修中；8：报废；9：退库；
//		log.debug("----------表计的生命周期状态 :"+EnumMeterCycleStatus.getName(cycleStatus));
//		Integer meterStatus = meter.getStatus();//表计状态 0复装 1暂拆
//		log.debug("----------表计复装/暂拆状态:"+EnumMeterSettingStatus.getName(meterStatus));
//		String meterUse = meter.getMeterUse();//水表用途：计费表、非计费表、计量表等；
//		log.debug("----------水表用途状态:"+EnumMeterUse.getValue(meterUse));
//		
//		Integer accountOpen = EnumCustomerStatus.ACCOUNT_OPEN.getValue();//客户状态:0=已立户
//		Integer waterStatusOk = EnumWaterStatus.ENABLED_NO.getValue();//用水状态:0=正常
//		
//		//Integer chargeModeMerge = EnumCustomerChargeMode.COMBINE_CHARGE.getValue();//收费方式（针对一表多户） 0=合并收费
//		
//		Integer realMeter = EnumMeterVirtualReal.REAL_METER.getValue();//虚/实表，0：实表（默认）
//		Integer cycleStatusEnabled = EnumMeterCycleStatus.ENABLED.getValue();//记录水表的生命周期状态 3：使用中；
//		Integer settingStatus = EnumMeterSettingStatus.ZERO.getValue();//表计状态 0复装
//		String meterUseCharge = EnumMeterUse.CHARGE_METER.getKey();//水表用途：计费表、非计费表、计量表等；
//		
//		//如果客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,且表计用途为计费表,可创建分水量记录,否则不增加分水量记录;
//		if(customerStatus==accountOpen && waterStatus==waterStatusOk
//				&& virtualReal==realMeter && cycleStatus==cycleStatusEnabled && meterStatus==settingStatus && meterUse.equalsIgnoreCase(meterUseCharge)) {
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * @Title: getRecordIds
	 * @Description: 获取抄表记录ID
	 * @param customerId
	 * @param period
	 * @param meterId
	 * @return 
	 */
//	private String getRecordIds(Long customerId, String period, Long meterId) {
//		
//		List<Long> recordIdList = new ArrayList<>();
//		
//		MeterRecord record = new MeterRecord();
//		record.setCustomerId(customerId);
//		record.setPeriod(period);
//		record.setMeterId(meterId);
//		List<MeterRecord> recordList = meterRecordMapper.select(record);
//		for(MeterRecord tempRecord : recordList) {
//			recordIdList.add(tempRecord.getId());
//		}
//		return recordIdList.toString();
//	}
	
	/**
	 * @Title: getCurrWaterAmount
	 * @Description: 获取本期水量	本期水量=本期抄表读数-上期抄表读数
	 * @param currRead
	 * @param preRead
	 * @return 
	 */
	private BigDecimal getCurrWaterAmount(String currRead, String preRead) {
		if(StringUtils.isBlank(preRead) || StringUtils.isBlank(currRead)) {
			return null;
		}
		BigDecimal currRead1 = new BigDecimal(currRead);
		BigDecimal preRead2 = new BigDecimal(preRead);
		BigDecimal currAmount = currRead1.subtract(preRead2);
		return currAmount;
	}

	/** 
	 * @Title: updateMeterRecord
	 * @Description: 修改抄表记录
	 * @param discount
	 * @return 
	 * @see com.learnbind.ai.service.discount.DiscountService#updateDiscount(com.learnbind.ai.model.MeterRecord)
	 */
	@Override
	public int updateMeterRecord(MeterRecord record) {
		return meterRecordMapper.updateByPrimaryKeySelective(record);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteMeterRecord
	 * @Description: 删除减免政策
	 * @param discountId
	 * @return 
	 * @see com.learnbind.ai.service.discount.DiscountService#deleteDiscount(long)
	 */
	@Override
	@Transactional
	public int deleteMeterRecord(long recordId) {
		try {
			MeterRecord record = new MeterRecord();
			record.setId(recordId);
			record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			return meterRecordMapper.updateByPrimaryKeySelective(record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	@Override
	@Transactional
	public int deleteBatch(String recordIdsJSON) {
		
		List<Long> recordIdList = JSON.parseArray(recordIdsJSON, Long.class);
		
		int rows = -1;
		
		for(Long recordId : recordIdList) {
			
			PartitionWater searchObj = new PartitionWater();
			searchObj.setRecordId(recordId.toString());
			List<PartitionWater> pwList = partitionWaterService.select(searchObj);
			boolean isDeleted = true;//是否删除
			List<Long> pwIdList = new ArrayList<>();//分水量ID集合
			List<Long> billIdList = new ArrayList<>();//账单ID集合
			if(pwList!=null && pwList.size()>0) {
				for(PartitionWater pw : pwList) {
					pwIdList.add(pw.getId());//增加分水量ID到集合
					Integer isMakeBill = pw.getIsMakeBill();
					if(isMakeBill==EnumMakeBillStatus.MAKE_BILL_YES.getValue()) {
						Long billId = pw.getAccountItemId();//账单ID
						billIdList.add(billId);//增加账单ID到集合
						CustomerAccountItem item = customerAccountItemService.selectByPrimaryKey(billId);
						Integer settleStatus = item.getSettlementStatus();//结算状态
						if(settleStatus!=EnumSettlementStatus.SETTLEMENT_NORMAL.getValue()) {//如果账单状态!=未结算时不可以删除账单，否则可以删除账单
							log.debug("客户ID："+pw.getCustomerId()+",抄表记录ID："+pw.getRecordId()+",账单ID："+pw.getAccountItemId()+" 此账单结算状态是 "+item.getSettlementStatusStr()+" ，此账单不可以删除。");
							isDeleted = false;//是否删除
							break;
						}
					}
				}
			}
			if(isDeleted) {

				//删除账单
				this.deleteBill(billIdList);
				//删除分水量
				this.deletePartitionWater(pwIdList);
				
				//删除抄表记录
				MeterRecord record = new MeterRecord();
				record.setId(recordId);
				record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				rows = meterRecordMapper.updateByPrimaryKeySelective(record);
				
			}
			
		}
		
		return rows;
	}
	
	/**
	 * @Title: deleteBill
	 * @Description: 删除账单（逻辑删除）
	 * @param billIdList
	 * @return 
	 */
	private int deleteBill(List<Long> billIdList) {
		if(billIdList==null || billIdList.size()<=0) {
			return 0;
		}
		//更新条件
		Example example = new Example(CustomerAccountItem.class);
		example.createCriteria().andIn("id", billIdList);
		//更新内容
		CustomerAccountItem updateObj = new CustomerAccountItem();
		updateObj.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		return customerAccountItemService.updateByExampleSelective(updateObj, example);
	}
	/**
	 * @Title: deletePartitionWater
	 * @Description: 删除分水量（逻辑删除）
	 * @param pwIdList
	 * @return 
	 */
	private int deletePartitionWater(List<Long> pwIdList) {
		if(pwIdList==null || pwIdList.size()<=0) {
			return 0;
		}
		//更新条件
		Example example = new Example(PartitionWater.class);
		example.createCriteria().andIn("id", pwIdList);
		//更新内容
		PartitionWater updateObj = new PartitionWater();
		updateObj.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		return partitionWaterService.updateByExampleSelective(updateObj, example);
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<MeterRecord> findAllMeterRecord(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<MeterRecord> recordList = meterRecordMapper.selectAll();
		PageInfo<MeterRecord> result = new PageInfo<>(recordList);
		return result;
	}

	@Override
	@Transactional
	public int initMeterRecord(Long operatorId, String operatorName) {
		
		int rows = 0;
		Date operationTime = new Date();//操作时间
		
		//查询客户与表计关系表，并进行初始化抄表记录
		CustomerMeter cm = new CustomerMeter();
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerMeter> cmList = customerMeterService.select(cm);
		for(CustomerMeter cmTemp : cmList) {
			MeterRecord record = new MeterRecord();
			record.setCustomerId(cmTemp.getCustomerId());
			record.setMeterId(cmTemp.getMeterId());
			record.setPeriod(this.getPeriod());
			record.setCurrDate(this.getCurrRecordDate());
			record.setCurrRead("0");//本期读表数
			record.setCurrAmount(new BigDecimal("0"));//本期水量
			record.setReadMode("ARTIFYCIAL");//人工抄表
			record.setReadType(EnumReadType.NORMAL_READ.getValue());
			record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			record.setOperatorId(operatorId);
			record.setOperatorName(operatorName);
			record.setOperationTime(operationTime);
			rows = meterRecordMapper.insertSelective(record);
			if(rows<=0) {
				break;
			}
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	
	/**
	 * @Title: getPeriod
	 * @Description: 获取本期抄表期间（系统日期的上个月日期，返回yyyy-MM）
	 * @return 
	 */
	private String getPeriod() {
		Calendar c=Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String period = sdf.format(c.getTime()); //上月
		return period;
	}
	/**
	 * @Title: getCurrDate
	 * @Description: 获取本期抄表日期（系统日期的上个月日期）
	 * @return 
	 */
	private Date getCurrRecordDate() {
		Calendar c=Calendar.getInstance();
		c.add(Calendar.MONTH, -1);//上个月
		return c.getTime();
	}

	@Override
	public String getTotalWaterAmount(Long customerId, Long meterId, String year) {
		return meterRecordMapper.getTotalWaterAmount(customerId, meterId, year);
	}
	
	@Override
	public List<Map<String, Object>> getMeterAmountInfo(String traceIds, String period) {
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		Integer arrearsStatus = EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue();
		return meterRecordMapper.getMeterAmountInfo(traceIds, period, EnumAiDebitCreditStatus.CREDIT.getKey(), waterFeeCode, arrearsStatus);
	}

	@Override
	public List<Map<String, Object>> getMeterAmountCompanyInfo(Long accountItemId) {
		//水费编码
		String waterFeeCode = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		Integer arrearsStatus = EnumSettlementStatus.SETTLEMENT_SUCCESS.getValue();
		
		return meterRecordMapper.getMeterAmountCompanyInfo(accountItemId, EnumAiDebitCreditStatus.CREDIT.getKey(), waterFeeCode, arrearsStatus);
	}
	
	@Override
	public List<Map<String, Object>> getListGroupByCustomerAndPeriodErrorAmount(Long customerId, String searchCond, Integer readType,
			Long operatorId, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer meterRecordStatus, Integer currAmount) {
		return meterRecordMapper.getListGroupByCustomerAndPeriodErrorAmount(customerId, searchCond, readType, operatorId, traceIds, period, isPartWater, startDate, endDate, meterRecordStatus, currAmount);
	}
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertInitMeterRecord
	 * @Description:初始化抄表记录，不增加分水量（立户、过户、换表时调用）
	 * @param record
	 * @param operatorId
	 * @param operatorName
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.MeterRecordService#insertMeterRecord(com.learnbind.ai.model.MeterRecord, java.lang.Long, java.lang.String)
	 */
	@Override
	@Transactional
	public int insertInitMeterRecord(Long meterId, Long customerId, Long operatorId, String operatorName, String currRead) {
		
		Meters meter = metersService.selectByPrimaryKey(meterId);
		
		Date sysDate = new Date();//系统日期
		Date currDate = DateUtils.addMonths(sysDate, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String period = formatter.format(currDate);	
		
		//删除某客户某表计 上期抄表记录为空，且小于当前时间的所有抄表记录
		this.deleteMeterRecord(customerId, meter.getId(), currDate);
		
		MeterRecord record = new MeterRecord();
		record.setCustomerId(customerId);
		record.setMeterId(meterId);
		record.setMeterUse(meter.getMeterUse());
		record.setReadMode(meter.getReadMode());
		record.setReadType(EnumReadType.INIT_READ.getValue());
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setOperatorId(operatorId);
		record.setOperatorName(operatorName);
		record.setOperationTime(sysDate);
		record.setCurrDate(currDate);
		record.setPeriod(period);
		record.setCurrRead(currRead);
		int rows = meterRecordMapper.insertSelective(record);//增加抄表记录
		
		return rows;
	}
	
	@Override
	public int insertInitMeterRecord(Long meterId, Long customerId) {
		
		//系统登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Meters meter = metersService.selectByPrimaryKey(meterId);
		
		Date sysDate = new Date();//系统日期
		Date currDate = DateUtils.addMonths(sysDate, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String period = formatter.format(currDate);	
		
		//删除某客户某表计 上期抄表记录为空，且小于当前时间的所有抄表记录
		this.deleteMeterRecord(customerId, meter.getId(), currDate);
		
		//设置抄表记录本期读数，默认值是表计的新表表底数，如果值为空则设置为0
		String currRead = meter.getNewMeterBottom();
		if(StringUtils.isBlank(currRead)) {
			currRead = "0";
		}
		
		MeterRecord record = new MeterRecord();
		record.setCustomerId(customerId);
		record.setMeterId(meterId);
		record.setMeterUse(meter.getMeterUse());
		record.setReadMode(meter.getReadMode());
		record.setReadType(EnumReadType.INIT_READ.getValue());
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setOperatorId(userBean.getId());
		record.setOperatorName(userBean.getRealname());
		record.setOperationTime(sysDate);
		record.setCurrDate(currDate);
		record.setPeriod(period);
		record.setCurrRead(currRead);
		return meterRecordMapper.insertSelective(record);//增加抄表记录
		
	}
	
	@Override
	@Transactional
	public int insertInitMeterRecord(Meters meter, Long customerId, Long operatorId, String operatorName, String currRead) {
		
		Date sysDate = new Date();//系统日期
		Date currDate = DateUtils.addMonths(sysDate, -1);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String period = formatter.format(currDate);	
		
		//删除某客户某表计 上期抄表记录为空，且小于当前时间的所有抄表记录
		this.deleteMeterRecord(customerId, meter.getId(), currDate);
		
		MeterRecord record = new MeterRecord();
		record.setCustomerId(customerId);
		record.setMeterId(meter.getId());
		record.setMeterUse(meter.getMeterUse());
		record.setReadMode(meter.getReadMode());
		record.setReadType(EnumReadType.INIT_READ.getValue());
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setOperatorId(operatorId);
		record.setOperatorName(operatorName);
		record.setOperationTime(sysDate);
		record.setCurrDate(currDate);
		record.setPeriod(period);
		record.setCurrRead(currRead);
		int rows = meterRecordMapper.insertSelective(record);//增加抄表记录
		
		return rows;
	}

	@Override
	public MeterRecord selectNewestMeterRecord(Long customerId, Long meterId) {
		return meterRecordMapper.selectNewestMeterRecord(customerId, meterId);
	}
	
	@Override
	public List<MeterRecord> getListByCustomerIdAndPeriodErrorAmount(Long customerId, String period) {
		return meterRecordMapper.getListByCustomerIdAndPeriodErrorAmount(customerId, period);
	}
	
	@Override
	public int deleteMeterRecord(Long customerId, Long meterId, Date currDate) {
		
		if(currDate==null) {
			currDate = new Date();
		}
		
		String strDateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
        String currDateStr = sdf.format(currDate);
		
		//条件
		Example example = new Example(MeterRecord.class);
		example.createCriteria()
			//.andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue())
			.andEqualTo("customerId", customerId).andEqualTo("meterId", meterId)
			.andIsNull("preRead")
			.andCondition(" CURR_DATE < to_date('"+currDateStr+"','yyyy-MM-dd HH24:Mi:ss')");
		
		//修改内容
		MeterRecord record = new MeterRecord();
		record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		
		return meterRecordMapper.updateByExampleSelective(record, example);
	}
	
	public static void main(String[] args) {
		Calendar c=Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String gtimelast = sdf.format(c.getTime()); //上月
		System.out.println(gtimelast);
		int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
		System.out.println(lastMonthMaxDay);
		c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);
		
		//按格式输出
		String gtime = sdf.format(c.getTime()); //上月最后一天
		System.out.println(gtime);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01  00:00:00");
		String gtime2 = sdf2.format(c.getTime()); //上月第一天
		System.out.println(gtime2);
		
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getExportMeterRecordData
	 * @Description:  抄表记录导出时查询数据
	 * @param searchCond
	 * @param readType
	 * @param traceIds
	 * @param period
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.MeterRecordService#getExportMeterRecordData(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getExportMeterRecordData(String searchCond, Integer readType, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer currAmount) {
		return meterRecordMapper.getExportMeterRecordData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate, currAmount);
	}

	@Override
	public int generatorPartWater(List<MeterRecord> recordList) {
		
		if(recordList==null || recordList.size()<=0) {
			return -1;
		}
		int result = 1;
		for(MeterRecord record : recordList) {
			boolean flag = this.generatorPartWater(record);
			if(!flag) {
				log.info("抄表记录生成分水量异常，抄表记录信息："+record);
			}
		}
		return result;
	}
	
	/**
	 * @Title: generatorPartWater
	 * @Description: 生成分水量
	 * @param record
	 * @return 
	 */
	@Transactional(propagation= Propagation.REQUIRES_NEW)
	private boolean generatorPartWater(MeterRecord record) {
		
		try {
			Date sysDate = new Date();//系统时间
			Long customerId = record.getCustomerId();
			Long meterId = record.getMeterId();
//			String period = record.getPeriod();
			
			
			//查询客户信息
//			Customers customer = customersService.selectByPrimaryKey(customerId);
//			Integer chargeMode = customer.getChargeMode();//收费方式，0：合并收费，1单独收费；针对一表多户
//			log.debug("----------客户收费方式:"+EnumCustomerChargeMode.getName(chargeMode));
			
			int rows = 0;
			//如果抄表记录上期表底不为空，且本期水量不为空，且是否已生成分水量状态=未生成分水量
			if(StringUtils.isNotBlank(record.getPreRead()) && record.getCurrAmount()!=null && record.getIsPartWater()==EnumPartitionWaterStatus.PARTITION_NO.getValue()) {
				
				boolean isInsertFlag = partitionWaterService.isInsertPartitionWater(customerId, meterId);
				//如果客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,可创建分水量记录,否则不增加分水量记录;
				if(isInsertFlag) {
					//客户类型-个人
//					Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();
//					if(customer.getCustomerType()==customerType) {
//						Integer chargeModeMerge = EnumCustomerChargeMode.COMBINE_CHARGE.getValue();//收费方式（针对一表多户） 0=合并收费
//						如果收费方式为合并收费,多条抄表记录合并生成一个分水量记录
//						if(chargeMode==chargeModeMerge) {//收费方式为合并收费
//							合并收费-增加本期分水量
//							rows = partitionWaterService.insertMergePartitionWater(customerId, period, record.getCurrAmount(), record.getPreDate(), record.getCurrDate(), sysDate);
//						}else {//收费方式为单独收费
//							rows = partitionWaterService.insertPartitionWater(record, sysDate);
//						}
//					}else {
						rows = partitionWaterService.insertPartitionWater(record, sysDate);
//					}
					
				}else {
					log.debug("本次抄表记录不满足创建本期分水量记录的条件!");
					log.debug("创建本期分水量的条件:客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,可创建分水量记录");
					return true;
				}
			}else {
				log.info("本期抄表记录未记录分水量，原因是上期抄表读数为空或已生成分水量记录！");
				return true;
			}
			if(rows>0) {
				//更新抄表记录分水量状态为已生成分水量（1=是）
				MeterRecord recordTemp = new MeterRecord();
				recordTemp.setId(record.getId());
				recordTemp.setIsPartWater(EnumPartitionWaterStatus.PARTITION_YES.getValue());
				meterRecordMapper.updateByPrimaryKeySelective(recordTemp);
				return true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		//TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	
	@Override
	public List<MeterRecord> getMeterRecordData(String searchCond, Integer readType, String traceIds, String period,
			Integer isPartWater, String startDate, String endDate) {
		return meterRecordMapper.getMeterRecordData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate);
	}
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: batchGeneratorPartWater
	 * @Description: 批量生成分水量记录
	 * @param recordMapList
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.MeterRecordService#batchGeneratorPartWater(java.util.List)
	 */
	@Override
	//@Transactional(propagation= Propagation.REQUIRES_NEW)
	//@Transactional
	public int batchGeneratorPartWater(List<MeterRecord> recordList) {
		if(recordList==null || recordList.size()<=0) {
			return -1;
		}
		
		for(MeterRecord record : recordList) {
			try {
				boolean flag = this.generatorPartWater(record);//生成分水量记录
				if(!flag) {
					log.info("抄表记录生成分水量异常，抄表记录信息："+record);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 1;
	}

	/** 
	 * @Title: getWaterAmount
	 * @Description: 返回最新一条抄表记录的水量
	 * @param period	可为空，为空时查询表计最新一条抄表记录的水量
	 * @param meterId
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.MeterRecordService#getWaterAmount(java.lang.String, java.lang.Long)
	 */
	@Override
	public BigDecimal getWaterAmount(String period, Long meterId) {
//		MeterRecord searchObj = new MeterRecord();
//		searchObj.setPeriod(period);
//		searchObj.setMeterId(meterId);
		//searchObj.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
		
		Example example = new Example(MeterRecord.class);
		Criteria criteria = example.createCriteria();
		if(StringUtils.isNotBlank(period)) {
			criteria.andEqualTo("period", period);
		}
		criteria.andEqualTo("meterId", meterId);
		example.setOrderByClause(" PERIOD DESC, ID DESC");
		List<MeterRecord> recordList = meterRecordMapper.selectByExample(example);
		if(recordList!=null && recordList.size()>0) {
			MeterRecord record = recordList.get(0);
			return record.getCurrAmount();
		}
		return null;
	}

	@Override
	public int confirmVirtualMeter(String period, String traceIds) {
		List<Meters> meterList = metersService.getVirtualMeterList(traceIds);//查询虚表
		return partitionWaterService.confirmVirtualMeter(period, meterList);
	}

	@SuppressWarnings("unused")
	@Override
	public BigDecimal getWaterAmount(String period, String partWaterRule) {
		String formalParamRule = partitionRuleUtil.getRealExpression(partWaterRule);//去规则中的分隔符号，返回表达式
		List<String> formalParamList = partitionRuleUtil.getRuleFormalParams(formalParamRule);//获取表达式中的形参
		
		//获取实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
		Map<String, String> actualParamMap = this.getActualParamMap(period, formalParamList);
		String realExpression = partitionRuleUtil.setRuleActualParams(actualParamMap, formalParamRule);
		BigDecimal realWaterAmount = new BigDecimal(0.00);//水量
		try {
			realWaterAmount = new BigDecimal(jSEngineUtils.start(realExpression).toString());
			if(realWaterAmount==null) {
				realWaterAmount = new BigDecimal(0.00);//水量
			}
		} catch (ScriptException e) {
			e.printStackTrace();
		}
		return realWaterAmount;
	}
	/**
	 * @Title: getActualParamMap
	 * @Description: 获取实际参数列表 形式参数名称1----->参数值1 形式参数名称2----->参数值2
	 * @param period
	 * @param formalParamList
	 * @return 
	 */
	private Map<String, String> getActualParamMap(String period, List<String> formalParamList){
		Map<String, String> actualParamMap = new HashMap<>();
		for(String meterParam : formalParamList) {
			String meterId = partitionRuleUtil.getMeterIndentify(meterParam);
			if(StringUtils.isNotBlank(meterId)) {
				BigDecimal waterAmount = this.getWaterAmount(period, Long.valueOf(meterId));
				actualParamMap.put(meterParam, waterAmount.toString());
			}
		}
		return actualParamMap;
	}

	@Override
	public boolean changeMeterVerify(Long meterId) {
		CustomerMeter cm = new CustomerMeter();
		cm.setMeterId(meterId);
		cm.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerMeter> cmList = customerMeterService.select(cm);
		if(cmList!=null && cmList.size()>0) {
			cm = cmList.get(0);
			Long customerId = cm.getCustomerId();//绑定表计的客户ID
			
			boolean verifyMeterRecord = this.verifyMeterRecord(customerId, meterId);//验证是否有未生成分水量的抄表记录
			
			boolean verifyPartWater = this.verifyPartWater(customerId, meterId);//验证是否有未开账的分水量记录
			
			if(verifyMeterRecord && verifyPartWater) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @Title: verifyMeterRecord
	 * @Description: 验证是否有未生成分水量的抄表记录
	 * @param customerId
	 * @param meterId
	 * @return 
	 * 		true=验证抄表记录通过；false=验证抄表记录未通过
	 */
	private boolean verifyMeterRecord(Long customerId, Long meterId) {
		Example example = new Example(MeterRecord.class);
		example.createCriteria()
		.andEqualTo("customerId", customerId)//客户ID
		.andEqualTo("meterId", meterId)//表计ID
		.andEqualTo("isPartWater", EnumPartitionWaterStatus.PARTITION_NO.getValue())//是否已分水量-是
		.andIsNotNull("preRead").andIsNotNull("currAmount")//上期表底和本期水量均不为空
		.andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());//删除状态-未删除
		List<MeterRecord> recordList = meterRecordMapper.selectByExample(example);
		boolean verifyMeterRecord = true;
		if(recordList!=null && recordList.size()>0) {
			verifyMeterRecord = false;
		}
		return verifyMeterRecord;
	}
	
	/**
	 * @Title: verifyPartWater
	 * @Description: 验证是否有未开账的分水量记录
	 * @param customerId
	 * @param meterId
	 * @return 
	 * 		true=验证分水量通过；false=验证分水量未通过
	 */
	private boolean verifyPartWater(Long customerId, Long meterId) {
		Example example = new Example(PartitionWater.class);
		example.createCriteria()
		.andEqualTo("customerId", customerId)//客户ID
		.andEqualTo("meterId", meterId)//表计ID
		.andEqualTo("isMakeBill", EnumMakeBillStatus.MAKE_BILL_NO.getValue())//开账状态-未开账
		.andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());//删除状态-未删除
		List<PartitionWater> pwList = partitionWaterService.selectByExample(example);
		boolean verifyPartWater = true;
		if(pwList!=null && pwList.size()>0) {
			verifyPartWater = false;
		}
		return verifyPartWater;
	}

	@Override
	public List<MeterRecord> getMeterRecordByMeter(String period, Long meterId) {
		return meterRecordMapper.getMeterRecordByMeter(period, meterId);
	}

	@Override
	public List<MeterRecord> getCustomerMeterRecord(List<Long> meterIdList, Long customerId, String period) {
		// TODO Auto-generated method stub
		return meterRecordMapper.getCustomerMeterRecord(meterIdList, customerId, period);
	}

	@Override
	public String getMeterRecordMaxPeriod(String searchCond, String traceIds, Long meterId) {
		return meterRecordMapper.getMeterRecordMaxPeriod(searchCond, traceIds, meterId);
	}
	
	@Override
	public List<Map<String, Object>> getExportMeterRecordErrorAmountData(String searchCond, Integer readType, String traceIds, String period, Integer isPartWater, String startDate, String endDate, Integer currAmount) {
		return meterRecordMapper.getExportMeterRecordErrorAmountData(searchCond, readType, traceIds, period, isPartWater, startDate, endDate, currAmount);
	}
	
	//-------------------------------------------------------------------------------------------
	@Override
	public MeterRecord saveMeterRecord(MeterRecord record, Long operatorId, String operatorName) {
		
		Date sysDate = new Date();//系统日期
		
		Date currDate = record.getCurrDate();//本期抄表日期
		if(currDate==null) {
			record.setCurrDate(sysDate);//本期抄表日期为null时，设置默认值为系统日期
		}
		
		Long customerId = record.getCustomerId();//客户ID
//		String period = record.getPeriod();//抄表期间
		Long meterId = record.getMeterId();//表计ID
		//查询到的最后一条抄表记录
		MeterRecord preRecord = this.getLastMeterRecord(customerId, null, meterId);
		if(preRecord!=null) {//如果有上期抄表记录则设置本期抄表记录中的上期抄表日期和上期抄表读数
			record.setPreDate(preRecord.getCurrDate());
			record.setPreRead(preRecord.getCurrRead());
		}//如果没有上期抄表记录则为空
			
		//获取本期水量	本期水量=本期抄表读数-上期抄表读数
		BigDecimal currAmount = this.getCurrWaterAmount(record.getCurrRead(), record.getPreRead());
		record.setCurrAmount(currAmount);
		record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		record.setOperatorId(operatorId);
		record.setOperatorName(operatorName);
		record.setOperationTime(sysDate);
		
		String meterUse = this.getMeterUse(meterId);//获取表计用途
		Long meterTreeId = this.getMeterTreeId(meterId);//获取表计父子关系ID
		record.setMeterUse(meterUse);//水表用途
		record.setMeterTreeId(meterTreeId);//表计父子关系ID
		
		int rows = meterRecordMapper.insertSelective(record);//增加抄表记录
		if(rows>0) {

			//增加使用表计规则日志
			List<MeterPartWaterRule> meterRuleList = meterPartWaterRuleService.getMeterRule(record.getMeterId());
			if(meterRuleList!=null && meterRuleList.size()>0) {
				meterPartWaterRuleTraceService.insertTraceList(record.getId(), meterRuleList);
			}
			return record;
		}
		
		return null;
	}
	
}
