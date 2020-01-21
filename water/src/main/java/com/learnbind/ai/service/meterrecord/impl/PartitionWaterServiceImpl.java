package com.learnbind.ai.service.meterrecord.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumCustomerChargeMode;
import com.learnbind.ai.common.enumclass.EnumCustomerStatus;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.EnumMakeBillStatus;
import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumMeterUse;
import com.learnbind.ai.common.enumclass.EnumPartitionWaterStatus;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.constant.WaterPriceConstant;
import com.learnbind.ai.dao.PartitionWaterMapper;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.CustomerPeopleAdjust;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterPartWaterRuleTrace;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.SysDiscount;
import com.learnbind.ai.model.SysPeopleAdjust;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.model.UseDiscountTrace;
import com.learnbind.ai.model.UsePeopleAdjustTrace;
import com.learnbind.ai.model.UseWaterPriceTrace;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomerPeopleAdjustService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.discount.DiscountService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleTraceService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.peopleadjust.PeopleAdjustService;
import com.learnbind.ai.service.trace.UseDiscountTraceService;
import com.learnbind.ai.service.trace.UsePeopleAdjustTraceService;
import com.learnbind.ai.service.trace.UseWaterPriceTraceService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterrecord.impl
 *
 * @Title: PartitionWaterServiceImpl.java
 * @Description: 分水量记录服务实现类
 *
 * @author Administrator
 * @date 2019年6月4日 上午10:40:50
 * @version V1.0 
 *
 */
@Service
public class PartitionWaterServiceImpl extends AbstractBaseService<PartitionWater, Long> implements  PartitionWaterService {
	
	private static Log log = LogFactory.getLog(PartitionWaterServiceImpl.class);
	
	@Autowired
	public PartitionWaterMapper partitionWaterMapper;
	
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public PartitionWaterServiceImpl(PartitionWaterMapper mapper) {
		this.partitionWaterMapper=mapper;
		this.setMapper(mapper);
	}
	
	/**
	 * @Fields JMSHYS：居民生活用水key值
	 */
	final String JMSHYS = WaterPriceConstant.JMSHYS;
	/**
	 * @Fields JSON_USE_WATER_PRICE_TRACE：使用水价日志JSON
	 */
	final String JSON_USE_WATER_PRICE_TRACE = "useWaterPriceTraceJSON";
	/**
	 * @Fields JSON_USE_PEOPLE_ADJUST_TRACE：使用多人口调整日志JSON
	 */
	final String JSON_USE_PEOPLE_ADJUST_TRACE = "usePeopleAdjustTraceJSON";
	/**
	 * @Fields JSON_USE_DISCOUNT_TRACE：使用政策减免日志JSON
	 */
	final String JSON_USE_DISCOUNT_TRACE = "useDiscountTraceJSON";
	
	@Autowired
	public CustomersService customersService;
	@Autowired
	public MetersService metersService;
	@Autowired
	public MeterRecordService meterRecordService;
	@Autowired
	public WaterPriceService waterPriceService;
	@Autowired
	public CustomerPeopleAdjustService customerPeopleAdjustService;
	@Autowired
	public DiscountService discountService;
	@Autowired
	public PeopleAdjustService peopleAdjustService;//多人口调整配置
	@Autowired
	public CustomerAccountService customerAccountService;
	@Autowired
	public CustomerAccountItemService customerAccountItemService;
	@Autowired
	public UseWaterPriceTraceService useWaterPriceTraceService;//水价日志
	@Autowired
	public UsePeopleAdjustTraceService usePeopleAdjustTraceService;//多人口调整日志
	@Autowired
	public UseDiscountTraceService useDiscountTraceService;//政策减免日志
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;//表计分水量配置
	@Autowired
	private MeterPartWaterRuleTraceService meterPartWaterRuleTraceService;//表计规则日志
	@Autowired
	private CustomerMeterService customerMeterService;//客户-表计关系

	@Override
	@Transactional
	public int saveList(String recordIds, String meterIds, Long customerId, String period, List<PartitionWater> pwList) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//删除原来的分水量记录
		Long pid = this.delOriginalPartWater(customerId, period, meterIds);
		int rows = 1;
		if(rows>0) {
			for(PartitionWater pw : pwList) {
				
				pw.setCustomerId(customerId);
				pw.setPeriod(period);
				pw.setRecordId(recordIds);
				pw.setMeterId(meterIds);
				
				pw.setOperationTime(new Date());
				pw.setOperatorId(userBean.getId());
				pw.setOperatorName(userBean.getRealname());
				
				rows = partitionWaterMapper.insertSelective(pw);
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
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
		//分水量成功后修改抄表记录分水量状态
		for(Long recordId : recordIdList) {
			MeterRecord meterRecord = meterRecordService.selectByPrimaryKey(recordId);
			//分水量状态改为已分
			meterRecord.setIsPartWater(EnumPartitionWaterStatus.PARTITION_YES.getValue());
			meterRecordService.updateByPrimaryKeySelective(meterRecord);
		}
		return rows;
	}
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: saveList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param recordIds
	 * @param meterIds
	 * @param customerId
	 * @param period
	 * @param jsonArr //json array 格式：[{waterPriceId:waterPriceId, waterAmount:waterAmount, waterPrice:waterPrice, waterUse:waterUse}]
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#saveList(java.lang.String, java.lang.String, java.lang.Long, java.lang.String, com.alibaba.fastjson.JSONArray)
	 */
	@Override
	@Transactional
	public int saveList(String recordIds, String meterIds, Long customerId, String period, JSONArray jsonArr) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Date sysDate = new Date();//系统时间
		
		//删除原来的分水量记录
		Long pid = this.delOriginalPartWater(customerId, period, meterIds);
		int rows = 1;
		if(rows>0) {
			for(int i=0; i<jsonArr.size(); i++) {
				JSONObject pwObj = jsonArr.getJSONObject(i);
				String waterAmount = pwObj.getString("waterAmount");//水量
				String waterPriceId = pwObj.getString("waterPriceId");//水价ID
				BigDecimal waterAmountBd = new BigDecimal(waterAmount);//水量
				
				SysWaterPrice price = waterPriceService.selectByPrimaryKey(Long.valueOf(waterPriceId));
				
				BigDecimal waterFee = BigDecimalUtils.multiply(price.getTotalPrice(), waterAmountBd);//水费=水价单价*水量
				
				PartitionWater pw = new PartitionWater();
				pw.setCustomerId(customerId);
				pw.setPeriod(period);
				pw.setRecordId(recordIds);
				pw.setMeterId(meterIds);
				
				pw.setPid(pid);
				pw.setBasePrice(price.getBasePrice());
				pw.setTreatmentFee(price.getTreatmentFee());
				pw.setWaterAmount(waterAmountBd);
				pw.setWaterPrice(price.getTotalPrice());
				pw.setWaterFee(waterFee);
				pw.setWaterUse(price.getPriceTypeCode());
				pw.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
				pw.setCreateTime(sysDate);
				
				pw.setOperationTime(new Date());
				pw.setOperatorId(userBean.getId());
				pw.setOperatorName(userBean.getRealname());
				
				pw.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
				
				rows = partitionWaterMapper.insertSelective(pw);
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
				
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
		//分水量成功后修改抄表记录分水量状态
		for(Long recordId : recordIdList) {
			MeterRecord meterRecord = meterRecordService.selectByPrimaryKey(recordId);
			//分水量状态改为已分
			meterRecord.setIsPartWater(EnumPartitionWaterStatus.PARTITION_YES.getValue());
			meterRecordService.updateByPrimaryKeySelective(meterRecord);
		}
		return rows;
	}
	
	/**
	 * @Title: delOriginalPartWater
	 * @Description: 删除原来的分水量记录，并返回最后一条记录的主键ID
	 * @param customerId
	 * @param period
	 * @return 
	 */
	private Long delOriginalPartWater(Long customerId, String period, String meterIds) {
		Long id = 0l;
		PartitionWater pwData = new PartitionWater();
		pwData.setCustomerId(customerId);
		pwData.setPeriod(period);
		pwData.setMeterId(meterIds);
		pwData.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());//生成账单状态-未生成
		pwData.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());//开账状态-未开账
		List<PartitionWater> pwList = partitionWaterMapper.select(pwData);
		for(int i=0; i<pwList.size(); i++) {
			PartitionWater pw = pwList.get(i);
			id = pw.getId();
			PartitionWater record = new PartitionWater();
			record.setId(id);
			record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			partitionWaterMapper.updateByPrimaryKeySelective(record);
		}
		return id;
	}

	@Override
	public BigDecimal getYearWaterAmount(Long customerId, String year, String period) {
		return partitionWaterMapper.getYearWaterAmount(customerId, year, period);
	}

	@Override
	public List<Map<String,Object>> getRecentlySixMonthAmount(Long customerId) {
		return partitionWaterMapper.getRecentlySixMonthAmount(customerId);
	}

	@Override
	public List<Map<String, Object>> getPartitionWaterMapList(Long operatorId, String searchCond, String traceIds, Integer isMakeBill, Integer isPartWater, String startDate, String endDate, String period) {
		return partitionWaterMapper.getPartitionWaterMapList(operatorId, searchCond, traceIds, isMakeBill, isPartWater, startDate, endDate, period);
	}

	@Override
	public List<PartitionWater> getPartitionWaterList(Long operatorId, String searchCond, String traceIds, Integer isMakeBill) {
		return partitionWaterMapper.getPartitionWaterList(operatorId, searchCond, traceIds, isMakeBill);
	}

	@Override
	public BigDecimal getCurrWaterAmount(Long customerId, String period) {
		return partitionWaterMapper.getCurrWaterAmount(customerId, period);
	}

	//-------------------------------计算水价并增加分水量---------------------------------------------------------------------------------------
	
	/** 
	 * @Title: insertPartitionWater
	 * @Description: 单独收费-增加本期分水量
	 * @param customerId
	 * @param period
	 * @param meterId
	 * @param waterAmount
	 * @param startTime
	 * @param endTime
	 * @param sysDate
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#insertPartitionWater(java.lang.Long, java.lang.String, java.lang.Long, java.math.BigDecimal, java.util.Date, java.util.Date, java.util.Date)
	 */
	@Override
	//@Transactional
	//public int insertPartitionWater(Long customerId, String period, Long meterId, BigDecimal waterAmount, Date startTime, Date endTime, Date sysDate) {
	public int insertPartitionWater(MeterRecord meterRecord, Date sysDate) {
		
		Long recordId = meterRecord.getId();//抄表记录ID
		Long customerId = meterRecord.getCustomerId();//客户ID
		String period = meterRecord.getPeriod();//期间
		Long meterId = meterRecord.getMeterId();//表计ID
		BigDecimal waterAmount = meterRecord.getCurrAmount();//本期水量
		Date startTime = meterRecord.getPreDate();//上期抄表日期
		Date endTime = meterRecord.getCurrDate();//基本抄表日期
		
		//查询客户
		Customers customer = customersService.selectByPrimaryKey(customerId);//获取客户信息
		Meters meter = metersService.selectByPrimaryKey(meterId);//查询表计信息
		
		List<Map<String, Object>> waterPriceMapList = new ArrayList<>();
		
		Integer isPartWater = customer.getIsPartWater();//是否分水量
		//如果是否分水量状态不为空，且是否分水量状态为是， 且该表计是否已配置分水量
		if(isPartWater!=null && isPartWater==EnumPartitionWaterStatus.PARTITION_YES.getValue() && this.isCfgPartWater(meterRecord.getId())) {
			//判断该表计是否已配置分水量，如果已配置分水量则直接进行分水量，否则根据价格分类和价格确定水价，并增加分水量
			//if(this.isCfgPartWater(meter.getId())) {
				return this.insertPartWater(meterRecord);
				//修改抄表记录是否已分水量状态为是
				//TODO 
				//this.updateMeterRecordPartWaterStatus(meterRecord.getId(), EnumPartitionWaterStatus.PARTITION_YES.getValue());
			//}
		}else {
		
		//判断该表计是否已配置分水量，如果已配置分水量则直接进行分水量，否则根据价格分类和价格确定水价
		//if(this.isCfgPartWater(meter.getId())) {
			//waterPriceMapList = this.getPartitionWaterAndPirce(meter.getId(), waterAmount);
		//}else {
			//获取价格分类和价格
			String waterUse = null;
			String priceCode = null;
			//如果表计价格分类不为空，且价格分类为居民生活用水或水价不为空，则使用表计价格
			if(StringUtils.isNotBlank(meter.getWaterUse()) && (meter.getWaterUse().equalsIgnoreCase(JMSHYS) || StringUtils.isNotBlank(meter.getPriceCode()))) {
				waterUse = meter.getWaterUse();
				priceCode = meter.getPriceCode();
			}else {
				waterUse = customer.getWaterUse();
				priceCode = customer.getPriceCode();
			}
			
			//（1）获取本期总水量
			//BigDecimal totalWaterAmount = this.getCurrTotalWaterAmount(customerId, period, waterAmount);
			
			//获取最终水价信息(无阶梯信息)
			waterPriceMapList = this.getLastWaterPrice(customerId, customer.getDiscountType(), period, waterUse, priceCode, waterAmount, EnumCustomerChargeMode.ALONE_CHARGE.getValue());
			return this.insertBatchPartitionWater(meterId, recordId, period, sysDate, startTime, endTime, customerId, waterPriceMapList);
		}
		
	}
	
	/**
	 * @Title: insertPartitionWater
	 * @Description: 增加分水量
	 * @param meterId
	 * @param recordId
	 * @param period
	 * @param sysDate
	 * @param startTime
	 * @param endTime
	 * @param customerId
	 * @param waterPriceMap
	 * @return 
	 */
	private Long insertPartitionWater(Long meterId, Long recordId, String period, Date sysDate, Date startTime, Date endTime, Long customerId, Map<String, Object> waterPriceMap) {
		
		String useWaterPriceTraceJSON = (String)waterPriceMap.get(this.JSON_USE_WATER_PRICE_TRACE);//使用水价日志（json）
		String usePeopleAdjustTraceJSON = (String)waterPriceMap.get(this.JSON_USE_PEOPLE_ADJUST_TRACE);//使用多人口调整日志（json）
		String useDiscountTraceJSON = (String)waterPriceMap.get(this.JSON_USE_DISCOUNT_TRACE);//使用政策减免日志（json）
		
		
		String priceCode = (String)waterPriceMap.get("priceCode");//水价编码
		
		BigDecimal basePrice = new BigDecimal(waterPriceMap.get("basePrice").toString());//基础水价
		BigDecimal treatmentFee = new BigDecimal(waterPriceMap.get("treatmentFee").toString());//污水处理费
		BigDecimal totalPrice = new BigDecimal(waterPriceMap.get("totalPrice").toString());//总单价
		BigDecimal currWaterAmount = new BigDecimal(waterPriceMap.get("waterAmount").toString());//水量
		//本期水费=水单价*总水量
		BigDecimal waterFee = BigDecimalUtils.multiply(totalPrice, currWaterAmount); 
		
		//获取分水量实体
		//PartitionWater pw = this.getPartitionWater(meterId.toString(), recordId.toString(), period, customer.getWaterUse(), sysDate, startTime, endTime, customerId, basePrice, treatmentFee, totalPrice, currWaterAmount, waterFee);
		String recordIdStr = null;
		if(recordId!=null) {
			recordIdStr = recordId.toString();
		}
		PartitionWater pw = this.getPartitionWater(meterId.toString(), recordIdStr, period, priceCode, sysDate, startTime, endTime, customerId, basePrice, treatmentFee, totalPrice, currWaterAmount, waterFee);
		
		//增加分水量记录
		int rows = partitionWaterMapper.insertSelective(pw);
		if(rows>0) {
			//增加日志（水价日志，多人口调整日志和政策减免日志）
			this.insertTrace(pw.getId(), useWaterPriceTraceJSON, usePeopleAdjustTraceJSON, useDiscountTraceJSON);
		}
			
		return pw.getId();
	}
	
	/**
	 * @Title: insertBatchPartitionWater
	 * @Description: 批量增加分水量
	 * @param meterId
	 * @param recordId
	 * @param period
	 * @param sysDate
	 * @param startTime
	 * @param endTime
	 * @param customerId
	 * @param waterPriceMapList
	 * @return 
	 */
	@Transactional
	private int insertBatchPartitionWater(Long meterId, Long recordId, String period, Date sysDate, Date startTime, Date endTime, Long customerId, List<Map<String, Object>> waterPriceMapList) {
		//删除本期分水量（只删除未开账的分水量）
		//this.delete(customerId, period);
		
		int rows = 0;
		for(Map<String, Object> waterPriceMap : waterPriceMapList) {
			
			String useWaterPriceTraceJSON = (String)waterPriceMap.get(this.JSON_USE_WATER_PRICE_TRACE);//使用水价日志（json）
			String usePeopleAdjustTraceJSON = (String)waterPriceMap.get(this.JSON_USE_PEOPLE_ADJUST_TRACE);//使用多人口调整日志（json）
			String useDiscountTraceJSON = (String)waterPriceMap.get(this.JSON_USE_DISCOUNT_TRACE);//使用政策减免日志（json）
			
			
			String priceCode = (String)waterPriceMap.get("priceCode");//水价编码
			
			BigDecimal basePrice = new BigDecimal(waterPriceMap.get("basePrice").toString());//基础水价
			BigDecimal treatmentFee = new BigDecimal(waterPriceMap.get("treatmentFee").toString());//污水处理费
			BigDecimal totalPrice = new BigDecimal(waterPriceMap.get("totalPrice").toString());//总单价
			BigDecimal currWaterAmount = new BigDecimal(waterPriceMap.get("waterAmount").toString());//水量
			//本期水费=水单价*总水量
			BigDecimal waterFee = BigDecimalUtils.multiply(totalPrice, currWaterAmount); 
			
			//获取分水量实体
			//PartitionWater pw = this.getPartitionWater(meterId.toString(), recordId.toString(), period, customer.getWaterUse(), sysDate, startTime, endTime, customerId, basePrice, treatmentFee, totalPrice, currWaterAmount, waterFee);
			PartitionWater pw = this.getPartitionWater(meterId.toString(), recordId.toString(), period, priceCode, sysDate, startTime, endTime, customerId, basePrice, treatmentFee, totalPrice, currWaterAmount, waterFee);
			
			//增加分水量记录
			rows = partitionWaterMapper.insertSelective(pw);
			if(rows>0) {
				//增加日志（水价日志，多人口调整日志和政策减免日志）
				this.insertTrace(pw.getId(), useWaterPriceTraceJSON, usePeopleAdjustTraceJSON, useDiscountTraceJSON);
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			
		}
		
		return rows;
	}
	
	/**
	 * @Title: updateMeterRecordPartWaterStatus
	 * @Description: 修改抄表记录是否已分水量状态
	 * @param recordId
	 * @param isPartWater 
	 */
//	private void updateMeterRecordPartWaterStatus(Long recordId, Integer isPartWater) {
//		MeterRecord meterRecord = new MeterRecord();
//		meterRecord.setId(recordId);
//		//分水量状态改为已分
//		meterRecord.setIsPartWater(isPartWater);
//		meterRecordService.updateByPrimaryKeySelective(meterRecord);
//	}
	
	/**
	 * @Title: insertPartWater
	 * @Description: 根据抄表记录增加分水量
	 * @param meterRecord
	 * @return 
	 */
	@Transactional
	private int insertPartWater(MeterRecord meterRecord){
		
		Date sysDate = new Date();//系统日期
		
		Long recordId = meterRecord.getId();//抄表记录ID
		Long customerId = meterRecord.getCustomerId();//客户ID
		String period = meterRecord.getPeriod();//期间
		Long meterId = meterRecord.getMeterId();//表计ID
		//BigDecimal waterAmount = meterRecord.getCurrAmount();//本期水量
		Date startTime = meterRecord.getPreDate();//上期抄表日期
		Date endTime = meterRecord.getCurrDate();//基本抄表日期
		
		//List<Map<String, Object>> retWaterPriceMapList = new ArrayList<>();//返回水价和水量集合
		
		int rows = 0;
		//获取表计分水量配置
		List<MeterPartWaterRuleTrace> partWaterRuleList = this.getMeterPartWaterRuleList(meterRecord.getId());
		for(int i=0; i<partWaterRuleList.size(); i++) {
			MeterPartWaterRuleTrace record = partWaterRuleList.get(i);
			String rule = record.getRuleReal();//表计规则
			//根据期间和规则获取水量
			BigDecimal realWaterAmount = meterRecordService.getWaterAmount(meterRecord.getPeriod(), rule);
			
			//判断水量是否是整数 TODO 
//			if(!this.isInteger(realWaterAmount)) {//如果不是整数
//				if(i%2==0) {//如果当前循环的是偶数，直接进位；如果当前循环的的奇数，直接去掉多余位数
//					realWaterAmount.setScale(0, BigDecimal.ROUND_HALF_UP);//直接进位处理
//				}else {
//					realWaterAmount.setScale(0, BigDecimal.ROUND_DOWN);//直接去掉多余的位数
//				}
//			}
			//水量取整，四舍五入
			realWaterAmount = realWaterAmount.setScale(0, BigDecimal.ROUND_HALF_UP);//四舍五入（若舍弃部分>=.5，就进位）
			
			Long waterPriceId = record.getWaterPriceId();//分水量对应的水价ID
			SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(waterPriceId);//获取水价
			
			Map<String, Object> waterPriceMap = EntityUtils.entityToMap(waterPrice);
			waterPriceMap.put("waterAmount", realWaterAmount);
			waterPriceMap.put("useWaterPriceTraceJSON", JSON.toJSONString(this.getUseWaterPriceTrace(waterPrice)));
			waterPriceMap.put("usePeopleAdjustTraceJSON", null);
			waterPriceMap.put("useDiscountTrace", null);
			//把水价和水量加入到返回集合
			//retWaterPriceMapList.add(waterPriceMap);
			Long partWaterId = this.insertPartitionWater(meterId, recordId, period, sysDate, startTime, endTime, customerId, waterPriceMap);
			if(partWaterId!=null && partWaterId>0) {
				rows = 1;
				//根据抄表记录ID修改表计规则日志表中的分水量ID
				meterPartWaterRuleTraceService.updatePartWaterIdByRecordId(record.getId(), partWaterId);
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		
		return rows;
	}
	
	/**
	 * @Title: isInteger
	 * @Description: 是否是整数
	 * @param number
	 * @return 
	 * 		null 或 整数时返回true，否则返回false
	 */
//	private boolean isInteger(BigDecimal number){
//        if (!"".equals(number) && number!=null){
//            if (new BigDecimal(number.intValue()).compareTo(number)==0){
//                //整数
//                //return String.valueOf(number.intValue());
//            	return true;
//            }else {
//                //小数
//                //return String.valueOf(round(number.toString(), scale));
//            	return false;
//            }
//        }
//        return true;
//    }
	
	/**
	 * @Title: getMeterPartWaterList
	 * @Description: 获取表计分水量配置
	 * 				TODO 
	 * @param meterId
	 * @return 
	 */
	private List<MeterPartWaterRuleTrace> getMeterPartWaterRuleList(Long meterRecordId) {
//		MeterPartWaterRule record = new MeterPartWaterRule();
//		record.setMeterId(meterId);
//		record.setIsDefault(EnumDefaultStatus.DEFAULT_YES.getValue());
//		List<MeterPartWaterRule> partWaterRuleList = meterPartWaterRuleService.select(record);
//		return partWaterRuleList;
		MeterPartWaterRuleTrace record = new MeterPartWaterRuleTrace();
		record.setMeterRecordId(meterRecordId);
		record.setIsDefault(EnumDefaultStatus.DEFAULT_YES.getValue());
		List<MeterPartWaterRuleTrace> partWaterRuleList = meterPartWaterRuleTraceService.select(record);
		return partWaterRuleList;
	}
	
	/**
	 * @Title: isCfgPartWater
	 * @Description: 判断表计是否配置过分水量 TODO 
	 * @param meterId
	 * @return 
	 * 		true=已配置分水量；false=未配置分水量；
	 */
	private boolean isCfgPartWater(Long meterRecordId) {
		MeterPartWaterRuleTrace record = new MeterPartWaterRuleTrace();
		record.setMeterRecordId(meterRecordId);
		record.setIsDefault(EnumDefaultStatus.DEFAULT_YES.getValue());
		int count = meterPartWaterRuleTraceService.selectCount(record);
		if(count>0) {
			return true;
		}
		return false;
	}
	
	/** 
	 * @Title: insertMergePartitionWater
	 * @Description: 合并收费-增加本期分水量(增加前会删除其他本期分水量)
	 * @param customerId
	 * @param period
	 * @param waterAmount
	 * @param startTime
	 * @param endTime
	 * @param sysDate
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#insertMergePartitionWater(java.lang.Long, java.lang.String, java.math.BigDecimal, java.util.Date, java.util.Date, java.util.Date)
	 */
	@Override
	@Transactional
	public int insertMergePartitionWater(Long customerId, String period, BigDecimal waterAmount, Date startTime, Date endTime, Date sysDate) {
		
		//String[] periodArr = period.split("-");
		//String year = periodArr[0];//从期中获取年
		
		//查询客户
		Customers customer = customersService.selectByPrimaryKey(customerId);//获取客户信息
		//获取抄表记录和表计分组后的ID集合
		Map<String, Object> groupIds = this.getGroupMeterRecordAndMeter(customerId, period);
		//this.getPartitionWaterBean(record, groupIds, customer.getWaterUse(), totalWaterAmount, operatorId, operatorName, sysDate);
		String recordIds = groupIds.get("RECORD_IDS").toString();//表计ID，多个表计ID用逗号分隔
		String meterIds = groupIds.get("METER_IDS").toString();//表计ID，多个表计ID用逗号分隔
		
		//（1）获取本期总水量
		//BigDecimal totalWaterAmount = this.getCurrTotalWaterAmount(customerId, period, waterAmount);
		
		//获取最终水价信息(无阶梯信息)
		List<Map<String, Object>> waterPriceMapList = this.getLastWaterPrice(customerId, customer.getDiscountType(), period, customer.getWaterUse(), customer.getPriceCode(), waterAmount, EnumCustomerChargeMode.COMBINE_CHARGE.getValue());
		
		//删除本期分水量（只删除未开账的分水量）
		this.delete(customerId, period);
		
		int rows = 0;
		for(Map<String, Object> waterPriceMap : waterPriceMapList) {
			
			String useWaterPriceTraceJSON = (String)waterPriceMap.get(this.JSON_USE_WATER_PRICE_TRACE);//使用水价日志（json）
			String usePeopleAdjustTraceJSON = (String)waterPriceMap.get(this.JSON_USE_PEOPLE_ADJUST_TRACE);//使用多人口调整日志（json）
			String useDiscountTraceJSON = (String)waterPriceMap.get(this.JSON_USE_DISCOUNT_TRACE);//使用政策减免日志（json）
			
			String priceCode = (String)waterPriceMap.get("priceCode");//水价编码
			
			BigDecimal basePrice = new BigDecimal(waterPriceMap.get("basePrice").toString());//基础水价
			BigDecimal treatmentFee = new BigDecimal(waterPriceMap.get("treatmentFee").toString());//污水处理费
			BigDecimal totalPrice = new BigDecimal(waterPriceMap.get("totalPrice").toString());//总单价
			BigDecimal currWaterAmount = new BigDecimal(waterPriceMap.get("waterAmount").toString());//水量
			//本期水费=水单价*总水量
			BigDecimal waterFee = BigDecimalUtils.multiply(totalPrice, currWaterAmount); 
			
			//获取分水量实体
			PartitionWater pw = this.getPartitionWater(meterIds, recordIds, period, priceCode, sysDate, startTime, endTime, customerId, basePrice, treatmentFee, totalPrice, currWaterAmount, waterFee);
			
			rows = partitionWaterMapper.insertSelective(pw);
			if(rows>0) {
				//增加日志（水价日志，多人口调整日志和政策减免日志）
				this.insertTrace(pw.getId(), useWaterPriceTraceJSON, usePeopleAdjustTraceJSON, useDiscountTraceJSON);
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			
		}
		
		return rows;
	}
	
	@Override
	public boolean isInsertPartitionWater(Long customerId, Long meterId) {
		Customers customer = customersService.selectByPrimaryKey(customerId);
		Integer customerStatus = customer.getStatus();//客户状态：-1=未立户；0=已立户；1=已销户；2=已过户；3=过户；
		log.debug("----------客户账户状态:"+EnumCustomerStatus.getName(customerStatus));
		Integer waterStatus = customer.getWaterStatus();//用水状态:0=正常;1=停水
		log.debug("----------客户用水状态:"+EnumWaterStatus.getName(waterStatus));
		//Integer chargeMode = customer.getChargeMode();//收费方式，0：合并收费，1单独收费；针对一表多户
		//log.debug("----------客户收费方式:"+EnumCustomerChargeMode.getName(chargeMode));
		//查询表计信息
		Meters meter = metersService.selectByPrimaryKey(meterId);
		//Integer virtualReal = meter.getVirtualReal();//虚/实表，0：实表（默认）/1：虚表
		//log.debug("----------表计虚/实状态:"+EnumMeterVirtualReal.getName(virtualReal));
		Integer cycleStatus = meter.getCycleStatus();//记录水表的生命周期状态 0：待用（待使用）（默认值）；1：领用；2：待启用；3：使用中；4：待检测；5：检测中；6：待检修；7：检修中；8：报废；9：退库；
		log.debug("----------表计的生命周期状态 :"+EnumMeterCycleStatus.getName(cycleStatus));
		Integer meterStatus = meter.getStatus();//表计状态 0复装 1暂拆
		log.debug("----------表计复装/暂拆状态:"+EnumMeterSettingStatus.getName(meterStatus));
		String meterUse = meter.getMeterUse();//水表用途：计费表、非计费表、计量表等；
		log.debug("----------水表用途状态:"+EnumMeterUse.getValue(meterUse));
		
		Integer accountOpen = EnumCustomerStatus.ACCOUNT_OPEN.getValue();//客户状态:0=已立户
		Integer waterStatusOk = EnumWaterStatus.ENABLED_NO.getValue();//用水状态:0=正常
		
		//Integer chargeModeMerge = EnumCustomerChargeMode.COMBINE_CHARGE.getValue();//收费方式（针对一表多户） 0=合并收费
		
		//Integer realMeter = EnumMeterVirtualReal.REAL_METER.getValue();//虚/实表，0：实表（默认）
		Integer cycleStatusEnabled = EnumMeterCycleStatus.ENABLED.getValue();//记录水表的生命周期状态 3：使用中；
		Integer settingStatus = EnumMeterSettingStatus.ZERO.getValue();//表计状态 0复装
		String meterUseCharge = EnumMeterUse.CHARGE_METER.getKey();//水表用途：计费表、非计费表、计量表等；
		
		//如果客户状态为0=已立户,且客户用水状态为0=正常,且表计虚/实状态为0=实表,且表计生命周期状态为3=使用中,且表计复装/暂拆状态为0=复装时,且表计用途为计费表,可创建分水量记录,否则不增加分水量记录;
//		if(customerStatus==accountOpen && waterStatus==waterStatusOk
//				&& virtualReal==realMeter && cycleStatus==cycleStatusEnabled && meterStatus==settingStatus && meterUse.equalsIgnoreCase(meterUseCharge)) {
		if(customerStatus==accountOpen && waterStatus==waterStatusOk
				&& cycleStatus==cycleStatusEnabled && meterStatus==settingStatus && meterUse.equalsIgnoreCase(meterUseCharge)) {
			return true;
		}
		return false;
	}
	/**
	 * @Title: getPartitionWater
	 * @Description: 获取分水量实体
	 * @param meterIds
	 * @param recordIds
	 * @param period
	 * @param waterUse
	 * @param sysDate
	 * @param startTime
	 * @param endTime
	 * @param customerId
	 * @param basePrice
	 * @param treatmentFee
	 * @param totalPrice
	 * @param currWaterAmount
	 * @param waterFee
	 * @return 
	 */
	private PartitionWater getPartitionWater(String meterIds, String recordIds, String period, String waterUse, Date sysDate, 
												Date startTime, Date endTime, Long customerId, BigDecimal basePrice, BigDecimal treatmentFee,
												BigDecimal totalPrice, BigDecimal currWaterAmount, BigDecimal waterFee) {
		
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		PartitionWater pw = new PartitionWater();
		pw.setMeterId(meterIds);
		pw.setRecordId(recordIds);
		pw.setPeriod(period);
		pw.setWaterUse(waterUse);
		pw.setOperatorId(userBean.getId());
		pw.setOperatorName(userBean.getRealname());
		pw.setOperationTime(sysDate);
		pw.setStartTime(startTime);
		pw.setEndTime(endTime);
		pw.setCustomerId(customerId);
		pw.setBasePrice(basePrice);
		pw.setTreatmentFee(treatmentFee);
		pw.setWaterPrice(totalPrice);
		pw.setWaterAmount(currWaterAmount);
		pw.setRealWaterAmount(currWaterAmount);
		pw.setWaterFee(waterFee);
		pw.setCreateTime(new Date());
		pw.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());//设置是否开账状态为未开账
		pw.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return pw;
	}
	
	/**
	 * @Title: insertTrace
	 * @Description: 增加日志（水价日志，多人口调整日志和政策减免日志）
	 * @param partitionWaterId
	 * @param wpTraceJSON	水价日志
	 * @param paTraceJSON	多人口调整日志
	 * @param dTraceJSON 	政策减免日志
	 */
	private void insertTrace(Long partitionWaterId, String wpTraceJSON, String paTraceJSON, String dTraceJSON) {
		
		if(StringUtils.isNotBlank(wpTraceJSON)) {
			UseWaterPriceTrace wpTrace = JSON.parseObject(wpTraceJSON, UseWaterPriceTrace.class);////把json转为实体类
			wpTrace.setPartitionWaterId(partitionWaterId);//设置分水量ID
			useWaterPriceTraceService.insertSelective(wpTrace);//增加日志
		}
		
		if(StringUtils.isNotBlank(paTraceJSON)) {
			UsePeopleAdjustTrace paTrace = JSON.parseObject(paTraceJSON, UsePeopleAdjustTrace.class);//把json转为实体类
			paTrace.setPartitionWaterId(partitionWaterId);//设置分水量ID
			usePeopleAdjustTraceService.insertSelective(paTrace);//增加日志
		}
		
		if(StringUtils.isNotBlank(dTraceJSON)) {
			UseDiscountTrace dTrace = JSON.parseObject(dTraceJSON, UseDiscountTrace.class);//把json转为实体类
			dTrace.setPartitionWaterId(partitionWaterId);//设置分水量ID
			useDiscountTraceService.insertSelective(dTrace);//增加日志
		}
		
	}
	
	/**
	 * @Title: getGroupMeterRecordAndMeter
	 * @Description: 获取分组后的抄表记录和表计
	 * 			TODO 后面需要再次确认一个月抄多次表时的处理方式
	 * @param customerId
	 * @param period
	 * @return 
	 * 		{"RECORD_IDS":"1,2","METER_IDS":"3,4"}
	 */
	private Map<String, Object> getGroupMeterRecordAndMeter(Long customerId, String period){
		//根据客户ID、期间和条件查询集合并按钮客户和期间分组，客户ID和期间为null时，只根据条件查询
		//返回 [{"RECORD_IDS":"1,2","METER_IDS":"3,4","CUSTOMER_ID":"1","PERIOD":"2019-06","PROP":"张三"},...]
		List<Map<String, Object>> customerList = meterRecordService.getListGroupByCustomerAndPeriod(customerId, period, null, null, null, null,null,null, null, null);
		Map<String, Object> groupIds = new HashMap<>();
		if(customerList!=null && customerList.size()>0) {
			groupIds = customerList.get(0);
		}
		return groupIds;
	}
	
	/**
	 * @Title: getCurrTotalWaterAmount
	 * @Description: 获取本期总水量
	 * @param customerId
	 * @param period
	 * @param waterAmount
	 * @return 
	 */
	private BigDecimal getCurrTotalWaterAmount(Long customerId, String period, BigDecimal waterAmount) {
		
		//获取本期未开账的分水量
		BigDecimal currWaterAmount = this.getCurrWaterAmount(customerId, period);
		//本期总水量=本期未开账的分水量+本次水量
		waterAmount = BigDecimalUtils.add(currWaterAmount, waterAmount);
		return waterAmount;
	}
	
	//------------------------------------获取水价(非居民或有政策减免时阶梯信息无效)-----------------------------------------------------------------------------------------------------
	/**
	 * @Title: getLastWaterPrice
	 * @Description: 获取最终水价(非居民或有政策减免时阶梯信息无效)
	 * @param customerId	客户ID
	 * @param discountType	政策减免ID
	 * @param period		期间
	 * @param waterUse		用水性质
	 * @param waterAmount	本期用水量
	 * @return 
	 * 			水价表信息
	 * 			水量
	 * 			使用水价日志（JSON）
	 * 			使用多人口调整日志（JSON）
	 * 			使用政策减免日志（JSON）
	 */
	private List<Map<String, Object>> getLastWaterPrice(Long customerId, Long discountType, String period, String waterUse, String priceCode, BigDecimal waterAmount, Integer chargeMode) {
		
		//非居民用水处理
		//根据用水性质查询水价列表，如果返回集合数>1，说明是居民用水
		
		//-----------------------处理非阶梯水价部分----------------------------------------------------------------
		//List<SysWaterPrice> wpList = null; 
		if(StringUtils.isNotBlank(priceCode)) {//水价不为空时表示非阶梯水价
			SysWaterPrice wp = new SysWaterPrice();
			wp.setPriceTypeCode(waterUse);
			wp.setPriceCode(priceCode);
			List<SysWaterPrice> wpList = waterPriceService.select(wp);
			if(wpList!=null && wpList.size()>0) {
				//非阶梯水价确定本期总水量和价格
				List<Map<String, Object>> retWaterPriceMapList = new ArrayList<>();//返回水价和水量集合
				SysWaterPrice record = wpList.get(0);
				Map<String, Object> waterPriceMap = EntityUtils.entityToMap(record);
				waterPriceMap.put("waterAmount", waterAmount);
				waterPriceMap.put("useWaterPriceTraceJSON", JSON.toJSONString(this.getUseWaterPriceTrace(record)));
				waterPriceMap.put("usePeopleAdjustTraceJSON", null);
				waterPriceMap.put("useDiscountTrace", null);
				//把水价和水量加入到返回集合
				retWaterPriceMapList.add(waterPriceMap);
				return retWaterPriceMapList;
			}
		}
		
//		if(wpList!=null && wpList.size()==1) {
//			//确定阶梯为当前循环的阶梯，确定水量为本期总水量
//			List<Map<String, Object>> retWaterPriceMapList = new ArrayList<>();//返回水价和水量集合
//			SysWaterPrice record = wpList.get(0);
//			Map<String, Object> waterPriceMap = EntityUtils.entityToMap(record);
//			waterPriceMap.put("waterAmount", waterAmount);
//			waterPriceMap.put("useWaterPriceTraceJSON", JSON.toJSONString(this.getUseWaterPriceTrace(record)));
//			waterPriceMap.put("usePeopleAdjustTraceJSON", null);
//			waterPriceMap.put("useDiscountTrace", null);
//			//把水价和水量加入到返回集合
//			retWaterPriceMapList.add(waterPriceMap);
//			return retWaterPriceMapList;
//		}		

		//------------------------处理阶梯水价部分----------------------------------------------------
		//居民用水处理
		//(1)获取所有阶梯水价集合
		//List<SysWaterPrice> waterPriceList = this.getLadderWaterPrice(waterUse);
		//(2)确定阶梯和水价
		List<Map<String, Object>> waterPriceMapList = this.judgeLadder(customerId, period, waterAmount, chargeMode);
		log.debug("----------计算水价----------  阶梯水价及水量:"+waterPriceMapList);
		//(3)确定最终水价(判断政策减免)
		List<Map<String, Object>> lastWpMapList = this.getLastLadderWaterPrice(waterPriceMapList, discountType);
		log.debug("----------计算水价----------  最终水价及水量:"+lastWpMapList);
		return lastWpMapList;
	}
	
	/**
	 * @Title: getLastLadderWaterPrice
	 * @Description: 获取最终的阶梯水价(判断政策减免)
	 * @param waterPriceMapList
	 * @param discountId
	 * @return 
	 */
	private List<Map<String, Object>> getLastLadderWaterPrice(List<Map<String, Object>> waterPriceMapList, Long discountId) {
		
		BigDecimal zero = new BigDecimal("0");//BigDecimal类型的0值，用于后面的判断
		
		//判断政策减免
		SysDiscount discount = this.getDiscount(discountId);
		log.debug("----------计算水价----------  政策减免:"+discount);
		log.debug("----------计算水价----------  政策减免【前】水价及水量:"+waterPriceMapList);
		
		for(Map<String, Object> waterPriceMap : waterPriceMapList) {
			
			//this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
			//this.putUsePeopleAdjustTrace(waterPriceMap, pa, cfgWaterAmount);//增加多人口调整日志到Map
			this.putUseDiscountTrace(waterPriceMap, discount);//增加政策减免日志到Map
			log.debug("----------计算水价----------  政策减免【前】水价及水量:"+waterPriceMap);
			
			//水价表配置的基础水价和污水处理费
			BigDecimal basePrice = new BigDecimal(waterPriceMap.get("basePrice").toString());
			BigDecimal treatmentFee = new BigDecimal(waterPriceMap.get("treatmentFee").toString());
			
			if(discount!=null) {
				BigDecimal discountBasePrice = discount.getBasePrice();//政策减免基础水价
				BigDecimal discountTreatmentFee = discount.getTreatmentFee();//政策减免污水处理费
				
				boolean flag1 = BigDecimalUtils.greaterThan(discountBasePrice, zero);//政策减免基础水价>0
				boolean flag2 = BigDecimalUtils.greaterThan(discountTreatmentFee, zero);//政策减免污水处理费>0
				if(flag1 && flag2) {
					BigDecimal totalPrice = BigDecimalUtils.add(discountBasePrice, discountTreatmentFee);
					waterPriceMap.put("basePrice", discountBasePrice);//基础水价
					waterPriceMap.put("treatmentFee", discountTreatmentFee);//污水处理费
					waterPriceMap.put("totalPrice", totalPrice);//总单价=基础水价+污水处理费
					log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMap);
				}else if(flag1) {
					BigDecimal totalPrice = BigDecimalUtils.add(discountBasePrice, treatmentFee);
					waterPriceMap.put("basePrice", discountBasePrice);//基础水价
					waterPriceMap.put("treatmentFee", discountTreatmentFee);//污水处理费
					waterPriceMap.put("totalPrice", totalPrice);//总单价=基础水价+污水处理费
					log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMap);
				}else if(flag2) {
					BigDecimal totalPrice = BigDecimalUtils.add(basePrice, discountTreatmentFee);
					waterPriceMap.put("basePrice", basePrice);//基础水价
					waterPriceMap.put("treatmentFee", discountTreatmentFee);//污水处理费
					waterPriceMap.put("totalPrice", totalPrice);//总单价=基础水价+污水处理费
					log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMap);
				}
			}
		}
		
		log.debug("----------计算水价----------  政策减免【后】水价及水量:"+waterPriceMapList);
		return waterPriceMapList;
	}
	
	/**
	 * @Title: processDiscount
	 * @Description: 获取政策减免 
	 */
	private SysDiscount getDiscount(Long discountId) {
		return discountService.selectByPrimaryKey(discountId);
	}
	
	/**
	 * @Title: getLadderWaterPrice
	 * @Description: 获取阶梯水价
	 * @param waterUse
	 * @return 
	 */
//	private List<SysWaterPrice> getLadderWaterPrice(String waterUse) {
//		
//		Example example = new Example(SysWaterPrice.class);
//		example.createCriteria().andEqualTo("priceTypeCode", waterUse);
//		example.setOrderByClause(" LADDER_START ASC");
//		List<SysWaterPrice> wpList = waterPriceService.selectByExample(example);
//		if(wpList!=null && wpList.size()>0) {
//			return wpList;
//		}
//		return null;
//	}
	
	/**
	 * @Title: judgeLadder
	 * @Description: 确定阶梯水价的阶梯和水量
	 * @param customerId		客户ID
	 * @param period			期间
	 * @param waterPriceList	水价列表集合
	 * @param waterAmount		本期水量
	 * @return 
	 * 		返回map集合中包含阶梯水价的阶梯和对应的水量
	 */
	private List<Map<String, Object>> judgeLadder(Long customerId, String period, BigDecimal waterAmount, Integer chargeMode) {
		
		List<Map<String, Object>> retWaterPriceMapList = new ArrayList<>();//返回水价和水量集合
		
		String[] periodArr = period.split("-");
		String year = periodArr[0];//从期中获取年
		
		BigDecimal zero = new BigDecimal("0");//BigDecimal类型的0值，用于后面的判断
		
		//(1)获取所有阶梯水价集合
		//List<SysWaterPrice> waterPriceList = this.getLadderWaterPrice(waterUse);
		List<SysWaterPrice> waterPriceList = waterPriceService.getJmshysPriceList(); 
				
		//年总的用水量
		BigDecimal yearWaterAmount = new BigDecimal(0.00);
		if(chargeMode==EnumCustomerChargeMode.COMBINE_CHARGE.getValue()) {//合并收费
			//获取年总的用水量(返回不包含本期的年总用水量)
			yearWaterAmount = this.getYearWaterAmount(customerId, year, period);
		}else {//单独收费
			//获取年总的用水量(包含本期的年总用水量)
			yearWaterAmount = this.getYearWaterAmount(customerId, year, null);
		}
		
		//yearWaterAmount = BigDecimalUtils.add(yearWaterAmount, waterAmount);//不包含本期的年总用水量+本期用水量=年总用水量
		
		//判断多人口调整，获取所增加的总水量
		BigDecimal addWaterAmount = new BigDecimal(0.00);

		//获取有效的多人口调整记录
		CustomerPeopleAdjust pa = this.getPeopleAdjustRecord(customerId);
		//获取多人口调整中配置的每增加一人所增加的水量（默认值：36）
		BigDecimal cfgWaterAmount = this.getPeopleAdjustCfgWaterAmount();
		if(pa!=null) {
			int addNum = pa.getAddNum();//多人口调整记录中增加的人数
			
			BigDecimal peopleNum = new BigDecimal(addNum);//多人口调整记录中增加的人数(转为BigDecimal类型)
			addWaterAmount = BigDecimalUtils.multiply(cfgWaterAmount, peopleNum);//计算所增加的水量,增加的水量=系统配置的一人所增加的水量*增加人数
		}
		
		//判断多人口调整所增加的总水量是否>0
		boolean isGreaterZero = BigDecimalUtils.greaterThan(addWaterAmount, zero);
		if(addWaterAmount!=null && isGreaterZero) {//如果多人口调整所增加的总水量不为null，并且>0
			//年总用水量-多人口调整所增加的总水量=实际年用水量
			yearWaterAmount = BigDecimalUtils.subtract(yearWaterAmount, addWaterAmount);
		}
		
		//获取本期总水量
		BigDecimal totalWaterAmount = new BigDecimal(0.00);
		if(chargeMode==EnumCustomerChargeMode.COMBINE_CHARGE.getValue()) {//合并收费
			//（1）获取本期总水量=本期未开账的分水量+本次水量
			totalWaterAmount = this.getCurrTotalWaterAmount(customerId, period, waterAmount);
		}else {//单独怫
			//获取本期总水量=本次水量
			totalWaterAmount = waterAmount;
		}
		
		
		//年总的用水量(返回不包含本期的年总用水量)+本期总用水量=年总用水量
		BigDecimal tempYearTotalWaterAmount = BigDecimalUtils.add(yearWaterAmount, totalWaterAmount);
		//判断年总的用水量是否<=0,如果<=0则直接返回第一阶梯
		boolean lessOrEquals = BigDecimalUtils.lessOrEquals(tempYearTotalWaterAmount, zero);
		if(lessOrEquals) {//如果年总的用水量<=0，则直接返回第一阶梯水价
			
			SysWaterPrice record = waterPriceList.get(0);//获取第一阶梯水价信息
			//确定阶梯为当前循环的阶梯，确定水量为本期总水量
			Map<String, Object> waterPriceMap = EntityUtils.entityToMap(record);
			waterPriceMap.put("waterAmount", totalWaterAmount);
			this.putUseWaterPriceTrace(waterPriceMap, record);//增加水价日志到Map
			this.putUsePeopleAdjustTrace(waterPriceMap, pa, cfgWaterAmount);//增加多人口调整日志到Map
			//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
			//把水价和水量加入到返回集合
			retWaterPriceMapList.add(waterPriceMap);
			return retWaterPriceMapList;
		}
		
		//-----------------------准备循环水价列表---------------------------------------------------------------------------------------
		//循环阶梯水价列表确定阶梯
		int size = waterPriceList.size();
		int lastSize = waterPriceList.size()-1;
		for(int i=0; i<size; i++) {
			//获取当前阶梯的初值和终值
			SysWaterPrice tempWaterPrice = waterPriceList.get(i);
			//String ladderStart = tempWaterPrice.getLadderStart();//当前阶梯初值
			String ladderEnd = tempWaterPrice.getLadderEnd();//当前阶梯终值

			if(i==lastSize) {//如果遍历的是最后一个阶梯价格，则直接返回最后阶梯的价格,水量为本期总水量
				//确定阶梯为第后一阶梯，确定水量为本期总水量
				Map<String, Object> waterPriceMap = EntityUtils.entityToMap(tempWaterPrice);
				waterPriceMap.put("waterAmount", totalWaterAmount);
				this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
				this.putUsePeopleAdjustTrace(waterPriceMap, pa, cfgWaterAmount);//增加多人口调整日志到Map
				//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
				//把水价和水量加入到返回集合
				retWaterPriceMapList.add(waterPriceMap);
				break;
			}
			
			
//			if(i==lastSize) {//如果遍历的是最后一个阶梯价格，则直接返回最后阶梯的价格,水量为本期总水量
//				//确定阶梯为第后一阶梯，确定水量为本期总水量
//				Map<String, Object> waterPriceMap = EntityUtils.entityToMap(tempWaterPrice);
//				waterPriceMap.put("waterAmount", totalWaterAmount);
//				//把水价和水量加入到返回集合
//				retWaterPriceMapList.add(waterPriceMap);
//				break;
//			}
			
			//BigDecimal ladderStartBd = new BigDecimal(ladderStart);//阶梯初值
			BigDecimal ladderEndBd = new BigDecimal(ladderEnd);//阶梯终值
			//boolean flag1 = BigDecimalUtils.greaterThan(yearWaterAmount, ladderStartBd);//年总的用水量>阶梯初值
			//boolean flag2 = BigDecimalUtils.lessOrEquals(yearWaterAmount, ladderEndBd);//年总的用水量<=阶梯终值
			//if(flag1 && flag2) {
				//加上本期用水量后的年总用水量=年总用水量+本期用水量
				BigDecimal yearTotalWaterAmount = BigDecimalUtils.add(yearWaterAmount, totalWaterAmount);
				//加上本期用水量后的年总用水量<=阶梯终值,则直接返回阶梯;否则拆分水量,并计算超出阶梯后的水量和价格
				boolean flag3 = BigDecimalUtils.lessOrEquals(yearTotalWaterAmount, ladderEndBd);//加上本期用水量后的年总用水量<=当前阶梯终值
				//boolean flag4 = BigDecimalUtils.greaterOrEquals(yearTotalWaterAmount, nextLadderEndBd);//加上本期用水量后的年总用水量>=下一阶梯终值
				if(flag3) {
					//确定阶梯为当前循环的阶梯，确定水量为本期总水量
					Map<String, Object> waterPriceMap = EntityUtils.entityToMap(tempWaterPrice);
					waterPriceMap.put("waterAmount", totalWaterAmount);
					this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
					this.putUsePeopleAdjustTrace(waterPriceMap, pa, cfgWaterAmount);//增加多人口调整日志到Map
					//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
					//把水价和水量加入到返回集合
					retWaterPriceMapList.add(waterPriceMap);
					break;
				}else{//加上本期用水量后的年总用水量>大于当前阶梯终值
					//获取下一阶梯水价信息
					SysWaterPrice nextWaterPrice = waterPriceList.get(i+1);
					String nextLadderEnd = nextWaterPrice.getLadderEnd();//获取下一阶梯水价信息的阶梯终值
					BigDecimal nextLadderEndBd = new BigDecimal(nextLadderEnd);//把下一阶梯水价信息的阶梯终值转为decimal类型
					boolean flag5 = BigDecimalUtils.lessOrEquals(yearWaterAmount, nextLadderEndBd);//年总的用水量(返回不包含本期的年总用水量)<=下一阶梯终值
					if(flag5) {
						//(1)计算到阶梯终值还差多少水量
						BigDecimal subWaterAmount = BigDecimalUtils.subtract(ladderEndBd, yearWaterAmount);
						boolean flag6 = BigDecimalUtils.greaterOrEquals(subWaterAmount, zero);
						if(flag6) {
							//确定阶梯为当前循环的阶梯，确定水量为到阶梯终值所差的水量（H）
							Map<String, Object> waterPriceMap = EntityUtils.entityToMap(tempWaterPrice);
							waterPriceMap.put("waterAmount", subWaterAmount);
							this.putUseWaterPriceTrace(waterPriceMap, tempWaterPrice);//增加水价日志到Map
							this.putUsePeopleAdjustTrace(waterPriceMap, pa, cfgWaterAmount);//增加多人口调整日志到Map
							//this.putUseDiscountTrace(waterPriceMap, null);//增加政策减免日志到Map
							//把水价和水量加入到返回集合
							retWaterPriceMapList.add(waterPriceMap);
							
							//年用水量+阶梯终值所差的水量=最新的年总用水量
							yearWaterAmount = BigDecimalUtils.add(yearWaterAmount, subWaterAmount);
							
							//计算本期水量-到阶梯终值所差的水量还剩多少水量,剩下的水量为最新的本期总水量
							totalWaterAmount = BigDecimalUtils.subtract(totalWaterAmount, subWaterAmount);
						}
						
					}
					
				}
				
			//}
		}
		
		return retWaterPriceMapList;
	}
	
	/**
	 * @Title: putUseWaterPriceTrace
	 * @Description: put使用水价日志json字符串到水价Map中
	 * @param waterPriceMap
	 * @param wp 
	 */
	private void putUseWaterPriceTrace(Map<String, Object> waterPriceMap, SysWaterPrice wp) {
		UseWaterPriceTrace wpTrace = this.getUseWaterPriceTrace(wp);
		String json = JSON.toJSONString(wpTrace);
		waterPriceMap.put(this.JSON_USE_WATER_PRICE_TRACE, json);
	}
	/**
	 * @Title: getUseWaterPriceTrace
	 * @Description: 获取使用水价日志
	 * @param wp		水价日志表
	 * @return 
	 */
	private UseWaterPriceTrace getUseWaterPriceTrace(SysWaterPrice wp) {
		wp.setId(null);
		Map<String, Object> wpMap = EntityUtils.entityToMap(wp);
		UseWaterPriceTrace wpTrace = EntityUtils.mapToEntity(wpMap, UseWaterPriceTrace.class);
		return wpTrace;
	}
	
	/**
	 * @Title: putUseDiscountTrace
	 * @Description: put使用政策减免日志json字符串到水价Map中
	 * @param waterPriceMap
	 * @param wp 
	 */
	private void putUseDiscountTrace(Map<String, Object> waterPriceMap, SysDiscount discount) {
		if(discount!=null) {
			UseDiscountTrace discountTrace = this.getUseDiscountTrace(discount);
			String json = JSON.toJSONString(discountTrace);
			waterPriceMap.put(this.JSON_USE_DISCOUNT_TRACE, json);
		}else {
			waterPriceMap.put(this.JSON_USE_DISCOUNT_TRACE, null);
		}
		
	}
	/**
	 * @Title: getUseDiscountTrace
	 * @Description: 获取使用政策减免日志
	 * @param discount	政策减免记录
	 * @return 
	 */
	private UseDiscountTrace getUseDiscountTrace(SysDiscount discount) {
		discount.setId(null);
		Map<String, Object> discountMap = EntityUtils.entityToMap(discount);
		UseDiscountTrace discountTrace = EntityUtils.mapToEntity(discountMap, UseDiscountTrace.class);
		return discountTrace;
	}
	
	/**
	 * @Title: putUsePeopleAdjustTrace
	 * @Description: put使用多人口调整日志json字符串到水价Map中
	 * @param waterPriceMap
	 * @param wp 
	 */
	private void putUsePeopleAdjustTrace(Map<String, Object> waterPriceMap, CustomerPeopleAdjust pa, BigDecimal waterAmount) {
		if(pa!=null) {//如果多人口调整记录不为空时，增加使用多人口调整日志
			UsePeopleAdjustTrace paTrace = this.getUsePeopleAdjustTrace(pa, waterAmount);
			String json = JSON.toJSONString(paTrace);
			waterPriceMap.put(this.JSON_USE_PEOPLE_ADJUST_TRACE, json);
		}else {
			waterPriceMap.put(this.JSON_USE_PEOPLE_ADJUST_TRACE, null);
		}
		
	}
	/**
	 * @Title: getUsePeopleAdjustTrace
	 * @Description: 获取使用多人口调整日志
	 * @param pa			多人口调整记录
	 * @param waterAmount	系统配置的每增加一个所增加的水量
	 * @return 
	 */
	private UsePeopleAdjustTrace getUsePeopleAdjustTrace(CustomerPeopleAdjust pa, BigDecimal waterAmount) {
		pa.setId(null);
		Map<String, Object> paMap = EntityUtils.entityToMap(pa);
		UsePeopleAdjustTrace paTrace = EntityUtils.mapToEntity(paMap, UsePeopleAdjustTrace.class);
		paTrace.setWaterAmount(waterAmount);
		return paTrace;
	}
	
	/**
	 * @Title: getPeopleAdjustRecord
	 * @Description: 获取有效的多人口调整记录
	 * @param customerId
	 * @return 
	 */
	private CustomerPeopleAdjust getPeopleAdjustRecord(Long customerId) {
		
		//查询多人口调整记录,按创建时间倒序排序
		Example example = new Example(CustomerPeopleAdjust.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("enabled", EnumEnabledStatus.ENABLED_NO.getValue());
		example.setOrderByClause(" CREATE_TIME DESC, ID DESC");
		List<CustomerPeopleAdjust> adjustList = customerPeopleAdjustService.selectByExample(example);
		
		CustomerPeopleAdjust pa = null;//有效的多人口调整记录
		
		//循环多人口调整记录,如果有满足条件的则获取此条记录中的增加人数,并退出循环.
		for(CustomerPeopleAdjust adjust : adjustList) {
			Date nowTime = new Date();
			Date startTime = adjust.getStartTime();
			Date endTime = adjust.getEndTime();
			if(startTime==null) {//如果开始时间为空，则直接过滤此条记录
				continue;
			}
			if(endTime==null) {//如果结束时间为空，表示当前记录有效，并直接返回当前记录
				pa = adjust;
				break;
			}
			//判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
			boolean flag = DateUtils.isEffectiveDate(nowTime, startTime, endTime);
			if(flag) {
				pa = adjust;
				break;
			}
		}
		
		return pa;
		
	}
	
	/**
	 * @Title: getPeopleAdjustCfgWaterAmount
	 * @Description: 获取多人口调整中配置的每增加一人所增加的水量（默认值：36）
	 * @return 
	 */
	private BigDecimal getPeopleAdjustCfgWaterAmount() {
		
		BigDecimal waterAmount = new BigDecimal("36");//多人口调整中配置的每增加一人所增加的水量（默认值：36）
		
		//获取多人口调整配置中设置的每增加一人所增加的水量
		List<SysPeopleAdjust> adjustCfgList = peopleAdjustService.selectAll();
		if(adjustCfgList!=null && adjustCfgList.size()>0) {
			SysPeopleAdjust adjustCfg = adjustCfgList.get(0);
			BigDecimal waterAmountTemp = adjustCfg.getWaterAmount();
			if(waterAmountTemp!=null) {
				return waterAmountTemp;
			}
		}
		return waterAmount;
	}
	
	/**
	 * @Title: delete
	 * @Description: 根据客户和期间删除分水量记录
	 * @param customerId
	 * @param period
	 * @return 
	 */
	private int delete(Long customerId, String period) {
		
		//根据客户和期间，删除分水量记录
		PartitionWater pwData = new PartitionWater();
		pwData.setCustomerId(customerId);
		pwData.setPeriod(period);
		pwData.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());//只删除未开账的分水量记录
		return partitionWaterMapper.delete(pwData);
	}

	//------------------------------------------开账处理-----------------------------------------------------------------------
	
	/** 
	 * @Title: batchGeneratorBill
	 * @Description: 批量开账
	 * @param pwList
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#batchGeneratorBill(java.util.List)
	 */
	@Override
	@Transactional
	public int batchGeneratorBill(List<PartitionWater> pwList) {
		int rows = 0;
//		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型 0
		
		for(PartitionWater pw : pwList) {
			
//			BigDecimal waterAmount = pw.getWaterAmount();//水量
//			Long accountItemId = 0l;//设置账目ID为0
			
//			//如果水量<0，则生成非标抄表账单
//			if(BigDecimalUtils.lessThan(waterAmount, zero)) {
				//生成标准抄表账单（如果有账单欠费金额有为负值时，自动结算本账单）
				Long accountItemId = customerAccountItemService.generatorStandardBill(pw);
				if(accountItemId!=null && accountItemId>0) {
					//修改分水量表的账目ID
					//this.updateAccountItemId(pw.getId(), accountItemId);
					//修改水价日志表的账目ID
					//useWaterPriceTraceService.updateAccountItemId(pw.getId(), accountItemId);
					//修改抄表记录的生成账单状态
//					this.updateRecordCreateBillStatus(pw.getRecordId());
					
					rows=1;//如果生成账单时返回的账目ID>0，则表示生成账单成功，否则表示生成账单失败
				}
//			}else {//如果>=0，则正常生成账单
//				accountItemId = customerAccountItemService.generatorBill(pw);
//				if(accountItemId!=null && accountItemId>0) {
//					//修改分水量表的账目ID
//					this.updateAccountItemId(pw.getId(), accountItemId);
//					//修改水价日志表的账目ID
//					useWaterPriceTraceService.updateAccountItemId(pw.getId(), accountItemId);
//					
//					rows=1;//如果生成账单时返回的账目ID>0，则表示生成账单成功，否则表示生成账单失败
//				}
//			}
			
			if(accountItemId<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}
	
	/** 
	 * @Title: generatorBill
	 * @Description: 单个开账
	 * @param partitionWaterId
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#generatorBill(java.lang.Long)
	 */
	@Override
	@Transactional
	public int generatorBill(Long partitionWaterId) {
		
//		BigDecimal zero = new BigDecimal(0.00);//初始化BigDecimal类型 0
		
		PartitionWater pw = partitionWaterMapper.selectByPrimaryKey(partitionWaterId);
		
//		BigDecimal waterAmount = pw.getWaterAmount();//水量
		//如果水量<0，则生成非标抄表账单
//		if(BigDecimalUtils.lessThan(waterAmount, zero)) {
//			Long accountItemId =  customerAccountItemService.generatorNonstandardBill(pw);
//			if(accountItemId!=null && accountItemId>0) {
//				//修改分水量表的账目ID
//				this.updateAccountItemId(pw.getId(), accountItemId);
//				//修改水价日志表的账目ID
//				useWaterPriceTraceService.updateAccountItemId(pw.getId(), accountItemId);
//				return 1;//如果生成账单时返回的账目ID>0，则表示生成账单成功，否则表示生成账单失败
//			}
//		}
		
		//Long accountItemId = customerAccountItemService.generatorBill(pw);
		//生成标准抄表账单（如果有账单欠费金额有为负值时，自动结算本账单）
		Long accountItemId =  customerAccountItemService.generatorStandardBill(pw);
		if(accountItemId!=null && accountItemId>0) {
			//修改分水量表的账目ID
			this.updateAccountItemId(pw.getId(), accountItemId);
			//修改水价日志表的账目ID
			useWaterPriceTraceService.updateAccountItemId(pw.getId(), accountItemId);
			//修改抄表记录的生成账单状态
//			this.updateRecordCreateBillStatus(pw.getRecordId());
			
			return 1;//如果生成账单时返回的账目ID>0，则表示生成账单成功，否则表示生成账单失败
		}
		return 0;
	}
	
//	private void updateRecordCreateBillStatus(String recordIds) {
//		if(StringUtils.isNotBlank(recordIds)) {
//			String[] recordIdArr = recordIds.split(",");
//			for(String recordId : recordIdArr) {
//				if(StringUtils.isNotBlank(recordId)) {
//					MeterRecord record = new MeterRecord();
//					record.setId(id);
//					record.set
//				}
//			}
//		}
//	}
	
	@Override
	public int updateAccountItemId(Long partitionWaterId, Long accountItemId) {
		PartitionWater record = new PartitionWater();
		record.setId(partitionWaterId);
		record.setAccountItemId(accountItemId);
		return partitionWaterMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public PartitionWater getPartitionWater(Long accountItemId) {
		PartitionWater pw = new PartitionWater();
		pw.setAccountItemId(accountItemId);
		List<PartitionWater> pwList = partitionWaterMapper.select(pw);
		if(pwList!=null && pwList.size()>0) {
			return pwList.get(0);
		}
		return null;
	}
	
	@Override
	public int confirmVirtualMeter(String period, List<Meters> meterList) {
		
		int rows = -1;
		for(Meters meter : meterList) {
			
			PartitionWater pw = new PartitionWater();
			pw.setPeriod(period);
			pw.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			pw.setMeterId(meter.getId().toString());
			int count = partitionWaterMapper.selectCount(pw);
			if(count>0) {
				continue;
			}
			rows = this.insertVirtualMeterPartWater(period, meter);
			if(rows<=0) {
				break;
			}
		}
		return rows;
	}
	
	/**
	 * @Title: insertVirtualMeterPartWater
	 * @Description: 增加虚表分水量
	 * @param period
	 * @param meter
	 * @return 
	 */
	@Transactional
	private int insertVirtualMeterPartWater(String period, Meters meter) {
		
		Date sysDate = new Date();//系统日期
		
		//查询客户-表计关系
		CustomerMeter cm = customerMeterService.getCustomerByMeterId(meter.getId());
		if(cm==null) {
			log.debug("此表计未绑定客户，不需要处理分水量规则，表计信息："+meter);
			return 1;
		}
		//Long recordId = meterRecord.getId();//抄表记录ID
		Long customerId = cm.getCustomerId();//客户ID
		//String period = meterRecord.getPeriod();//期间
		Long meterId = meter.getId();//表计ID
		//BigDecimal waterAmount = meterRecord.getCurrAmount();//本期水量
		Date startTime = null;//上期抄表日期
		Date endTime = null;//基本抄表日期
		
		//List<Map<String, Object>> retWaterPriceMapList = new ArrayList<>();//返回水价和水量集合
		
		int rows = 0;
		//获取表计分水量配置
		List<MeterPartWaterRule> partWaterRuleList = meterPartWaterRuleService.getMeterRule(meterId);
		for(MeterPartWaterRule record : partWaterRuleList) {
			
			String rule = record.getRuleReal();//表计规则
			//根据期间和规则获取水量
			BigDecimal realWaterAmount = meterRecordService.getWaterAmount(period, rule);
			
			Long waterPriceId = record.getWaterPriceId();//分水量对应的水价ID
			SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(waterPriceId);//获取水价
			
			Map<String, Object> waterPriceMap = EntityUtils.entityToMap(waterPrice);
			waterPriceMap.put("waterAmount", realWaterAmount);
			waterPriceMap.put("useWaterPriceTraceJSON", JSON.toJSONString(this.getUseWaterPriceTrace(waterPrice)));
			waterPriceMap.put("usePeopleAdjustTraceJSON", null);
			waterPriceMap.put("useDiscountTrace", null);
			//把水价和水量加入到返回集合
			//retWaterPriceMapList.add(waterPriceMap);
			Long partWaterId = this.insertPartitionWater(meterId, null, period, sysDate, startTime, endTime, customerId, waterPriceMap);
			if(partWaterId!=null && partWaterId>0) {
				rows = 1;
				//增加表计规则日志的分水量ID
				meterPartWaterRuleTraceService.insertTrace(record.getId(), partWaterId, record);
				//meterPartWaterRuleTraceService.updatePartWaterId(record.getId(), partWaterId);
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		
		return rows;
	}
	
	@Override
	public List<MeterPartWaterRuleTrace> getMeterPartWaterRuleTraceList(Long customerId, String period,
			String recordIds) {
		PartitionWater searchObj = new PartitionWater();
		searchObj.setCustomerId(customerId);
		searchObj.setPeriod(period);
		searchObj.setRecordId(recordIds);
		//pw.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
		searchObj.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<PartitionWater> pwList = partitionWaterMapper.select(searchObj);
		List<Long> partWaterIdList = new ArrayList<>();
		for(PartitionWater pw : pwList) {
			partWaterIdList.add(pw.getId());
		}
		
		Example example = new Example(MeterPartWaterRuleTrace.class);
		example.createCriteria().andIn("partWaterId", partWaterIdList);
		return meterPartWaterRuleTraceService.selectByExample(example);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: savePartitionWater
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param partWaterId
	 * @param ruleTraceList
	 * 			var obj = new Object();
				//obj.meterId = meterId;//表计ID
				obj.ruleReal = ruleRealStr;//实际使用的规则
				obj.waterPriceId = waterPriceId;//水价ID
				obj.isDefault = 0;//是否默认，0=是；1=否；
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#savePartitionWater(java.lang.Long, java.util.List)
	 */
	@Override
	@Transactional
	public int savePartitionWater(Long partWaterId, List<MeterPartWaterRuleTrace> ruleTraceList) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//查询初始分水量记录
		PartitionWater originalPartWater = partitionWaterMapper.selectByPrimaryKey(partWaterId);//当前分水量ID
		if(!originalPartWater.getPid().equals(0l)) {//如果分水量记录的pid!=0，则表示此分水量记录是用户分水量后的记录，获取原始记录需要通过PID获取
			originalPartWater = partitionWaterMapper.selectByPrimaryKey(originalPartWater.getPid());//当前分水量ID
		}
		
		Long originalPwId = originalPartWater.getId();//原分水量记录ID
		Long customerId = originalPartWater.getCustomerId();//客户ID
		String recordIdStr = originalPartWater.getRecordId();//抄表记录ID
		String period = originalPartWater.getPeriod();//期间
		String meterIdStr = originalPartWater.getMeterId();//表计ID
		
		//删除所有原分水量记录
		this.delOriginalPartWater(customerId, period, meterIdStr);
		int rows = 1;
		
		//获取抄表记录ID和表计ID
		Long recordId = this.getMeterRecordId(recordIdStr);//获取抄表记录ID
		Long meterId = this.getMeterId(meterIdStr);//获取表计ID
		
		for(MeterPartWaterRuleTrace ruleTrace : ruleTraceList) {
			
			//设置抄表记录ID和表计ID
			ruleTrace.setMeterRecordId(recordId);
			ruleTrace.setMeterId(meterId);
			
			String ruleReal = ruleTrace.getRuleReal();//表达式
			Long waterPriceId = ruleTrace.getWaterPriceId();//水价
			
			//根据期间和规则获取水量
			BigDecimal realWaterAmount = meterRecordService.getWaterAmount(period, ruleReal);
			//水量取整，四舍五入
			realWaterAmount = realWaterAmount.setScale(0, BigDecimal.ROUND_HALF_UP);//四舍五入（若舍弃部分>=.5，就进位）
			
			//获取水价
			SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(waterPriceId);
			//计算水费
			BigDecimal waterFee = BigDecimalUtils.multiply(realWaterAmount, waterPrice.getTotalPrice());
			
			PartitionWater pw = originalPartWater;
			pw.setId(null);
			pw.setPid(originalPwId);
			
			pw.setWaterUse(waterPrice.getPriceCode());//用水
			
			pw.setBasePrice(waterPrice.getBasePrice());//基础水价
			pw.setTreatmentFee(waterPrice.getTreatmentFee());//污水处理费
			pw.setWaterPrice(waterPrice.getTotalPrice());//综合水价
			pw.setWaterAmount(realWaterAmount);//水量
			pw.setRealWaterAmount(realWaterAmount);//实际水量
			pw.setWaterFee(waterFee);//水费
			
			pw.setOperationTime(new Date());
			pw.setOperatorId(userBean.getId());
			pw.setOperatorName(userBean.getRealname());
			
			pw.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			
			rows = partitionWaterMapper.insertSelective(pw);
			if(rows>0) {
				//增加水价日志
				UseWaterPriceTrace waterPriceTrace = this.getUseWaterPriceTrace(waterPrice, pw.getId());
				useWaterPriceTraceService.insertSelective(waterPriceTrace);
				
				//增加规则日志
				ruleTrace.setPartWaterId(pw.getId());
				meterPartWaterRuleTraceService.insertSelective(ruleTrace);
			}
			
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
	 * @Title: getMeterId
	 * @Description: 获取表计ID
	 * @param meterIds
	 * @return 
	 * 		如果meterIds不为空时则返回一个不为空的表计ID
	 */
	private Long getMeterId(String meterIds) {
		if(StringUtils.isBlank(meterIds)) {
			return null;
		}
		String[] recordIdArr = meterIds.split(",");
		Long meterId = null;
		if(recordIdArr!=null && recordIdArr.length>0) {
			for(String meterIdStr : recordIdArr) {
				if(StringUtils.isNotBlank(meterIdStr)) {
					meterId = Long.valueOf(meterIdStr);
					break;
				}
			}
		}
		return meterId;
	}
	/**
	 * @Title: getMeterRecordId
	 * @Description: 获取抄表记录ID
	 * @param recordIds
	 * @return 
	 * 		如果recordIds不为空时则反回一个不为空的抄表记录ID
	 */
	private Long getMeterRecordId(String recordIds) {
		if(StringUtils.isBlank(recordIds)) {
			return null;
		}
		String[] recordIdArr = recordIds.split(",");
		Long recordId = null;
		for(String recordIdStr : recordIdArr) {
			if(StringUtils.isNotBlank(recordIdStr)) {
				recordId = Long.valueOf(recordIdStr);
			}
		}
		return recordId;
	}
	
	/**
	 * @Title: getUseWaterPriceTrace
	 * @Description: 获取使用水价日志
	 * @param waterPrice
	 * @param partWaterId
	 * @return 
	 */
	private UseWaterPriceTrace getUseWaterPriceTrace(SysWaterPrice waterPrice, Long partWaterId) {
		UseWaterPriceTrace trace = new UseWaterPriceTrace();
		trace.setPriceTypeCode(waterPrice.getPriceTypeCode());
		trace.setPriceTypeName(waterPrice.getPriceTypeName());
		trace.setPriceCode(waterPrice.getPriceCode());
		trace.setLadderName(waterPrice.getLadderName());
		trace.setLadderStart(waterPrice.getLadderStart());
		trace.setLadderEnd(waterPrice.getLadderEnd());
		trace.setBasePrice(waterPrice.getBasePrice());
		trace.setTreatmentFee(waterPrice.getTreatmentFee());
		trace.setTotalPrice(waterPrice.getTotalPrice());
		trace.setRemark(waterPrice.getRemark());
		trace.setPartitionWaterId(partWaterId);
		return trace;
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getPartitionWaterStatisticMap
	 * @Description: 获取统计的水量及水费
	 * @param operatorId
	 * @param searchCond
	 * @param traceIds
	 * @param isMakeBill
	 * @param isPartWater
	 * @param startDate
	 * @param endDate
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#getPartitionWaterStatisticMap(java.lang.Long, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, Object> getPartitionWaterStatisticMap(Long operatorId, String searchCond, String traceIds,
			Integer isMakeBill, Integer isPartWater, String startDate, String endDate, String period) {
		return partitionWaterMapper.getPartitionWaterStatisticMap(operatorId, searchCond, traceIds, isMakeBill, isPartWater, startDate, endDate, period);
	}
	
	public static void main(String[] args) {
		
		BigDecimal a = new BigDecimal("109");
		BigDecimal b = new BigDecimal("0.75");
		BigDecimal c = new BigDecimal("0.25");
		
		BigDecimal d = a.multiply(b);
		System.out.println("d="+d);
		BigDecimal e = a.multiply(c);
		System.out.println("e="+e);
		d = d.setScale(0, BigDecimal.ROUND_HALF_UP);//四舍五入（若舍弃部分>=.5，就进位）
		System.out.println("d="+d); //2.23  四舍五入（若舍弃部分>=.5，就进位）
		//System.out.println("ROUND_HALF_UP"+d); //2.23  四舍五入（若舍弃部分>=.5，就进位）
			
		e = e.setScale(0, BigDecimal.ROUND_HALF_UP);//四舍五入（若舍弃部分>=.5，就进位）
		//e = e.setScale(0, BigDecimal.ROUND_HALF_DOWN);
		System.out.println("e="+e);//2.22  四舍五入（若舍弃部分>.5,就进位）
		//System.out.println("ROUND_HALF_DOWN"+e);//2.22  四舍五入（若舍弃部分>.5,就进位）
		
		System.out.println(BigDecimalUtils.equals(a, d.add(e)));
			
	}
	@Override
	public List<PartitionWater> getPartitionWaterByMeterList(Long meterId, String period) {
		Example example = new Example(PartitionWater.class);
		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("meterId", meterId).andEqualTo("period", period);
		example.setOrderByClause(" ID DESC");
		return this.selectByExample(example);
	}
	
	@Override
	public List<PartitionWater> getPartitionWaterByCustomerList(Long customerId, String period) {
		Example example = new Example(PartitionWater.class);
		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("customerrId", customerId).andEqualTo("period", period);
		return this.selectByExample(example);
	}
	@Override
	public List<PartitionWater> getPartWaterMessage(String period, Long customerId, String traceIds) {
		return partitionWaterMapper.getPartWaterMessage(period,customerId,traceIds);
	}
	
	@Override
	public int delete(List<Long> partWaterIdList) {
		int rows = -1;
		for(Long partWaterId : partWaterIdList) {
			//(1)查询分水量
			PartitionWater pw = partitionWaterMapper.selectByPrimaryKey(partWaterId);
			Integer isMakeBill = pw.getIsMakeBill();
			//(2)判断是否已开账，如果状态为空或状态为未开账，则删除分水量，否则无操作
			if(isMakeBill==null || isMakeBill==EnumMakeBillStatus.MAKE_BILL_NO.getValue()) {
				//(3)删除分水量
				String meterRecordIds = pw.getRecordId();
				rows = this.deletePartWater(partWaterId, meterRecordIds);
			}
		}
		
		return rows;
	}
	
	/**
	 * @Title: delete
	 * @Description: 根据分水量ID删除分水量记录，并设置对应的抄表记录为未开账
	 * @param partWaterId
	 * @return 
	 */
	private int deletePartWater(Long partWaterId, String meterRecordIds) {
		PartitionWater pwTemp = new PartitionWater();
		pwTemp.setId(partWaterId);
		pwTemp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
		int rows = partitionWaterMapper.updateByPrimaryKeySelective(pwTemp);
		if(rows>0) {
			if(StringUtils.isNotBlank(meterRecordIds)) {
				Long meterRecordId = this.getMeterRecordId(meterRecordIds);
				MeterRecord record = new MeterRecord();
				record.setId(meterRecordId);
				record.setIsPartWater(EnumPartitionWaterStatus.PARTITION_NO.getValue());
				record.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
				meterRecordService.updateByPrimaryKeySelective(record);
			}
		}
		return rows;
	}
	
	@Override
	public List<PartitionWater> getCustomerPartWater(List<Long> meterIdList, Long customerId, String period) {
		return partitionWaterMapper.getCustomerPartWater(meterIdList, customerId, period);
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getCustomerPastPartWater
	 * @Description: 查询额客户的往期分水量
	 * @param meterIdList
	 * @param customerId
	 * @param period
	 * @return 
	 * @see com.learnbind.ai.service.meterrecord.PartitionWaterService#getCustomerPastPartWater(java.util.List, java.lang.Long, java.lang.String)
	 */
	@Override
	public List<PartitionWater> getCustomerPastPartWater(List<Long> meterIdList, Long customerId, String period) {
		return partitionWaterMapper.getCustomerPastPartWater(meterIdList, customerId, period);
	}
	
	@Override
	public List<Map<String, Object>> searchCustomerAccountItemErrorFee(Long operatorId, String searchCond, String traceIds, Integer isMakeBill, Integer isPartWater, String startDate, String endDate, String period) {
		return partitionWaterMapper.searchCustomerAccountItemErrorFee(operatorId, searchCond, traceIds, isMakeBill, isPartWater, startDate, endDate, period);
	}
	
//	@Override
//	public BigDecimal getCardMeterAmount(String period) {
//		BigDecimal cardMeterAmount =  new BigDecimal(0.00);
//		BigDecimal amount =  partitionWaterMapper.getCardMeterAmount(period);
//		if(amount!=null) {
//			cardMeterAmount = amount;
//		}
//		return cardMeterAmount;
//	}
	
}
