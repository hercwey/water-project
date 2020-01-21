package com.learnbind.ai.service.importexcel.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumCustomerStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumMeterStatus;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.common.enumclass.EnumNotifyMode;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;
import com.learnbind.ai.common.util.dict.DataDictType;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.importexcel.ImportBigMeterExcelService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.waterprice.WaterPriceService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.importexcel.impl
 *
 * @Title: ImportBigMeterExcelServiceImpl.java
 * @Description: 导入大表Excel服务实现类
 *
 * @author Administrator
 * @date 2019年6月28日 上午9:30:52
 * @version V1.0 
 *
 */
@Service
public class ImportBigMeterExcelServiceImpl implements ImportBigMeterExcelService {

	@Autowired
	private LocationService locationService;
	@Autowired
	private MetersService metersService;
	@Autowired
	private LocationMeterService locationMeterService;
	@Autowired
	private CustomersService customersService;
	@Autowired
	private BankService bankService;
	@Autowired
	private CustomerAccountService customerAccountService;
	@Autowired
	private LocationCustomerService locationCustomerService;
	@Autowired
	private CustomerMeterService customerMeterService;
	@Autowired
	private WaterPriceService waterPriceService;
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;
	@Autowired
	private DataDictService dataDictService;
	@Autowired
	private MeterRecordService meterRecordService;
	
	//==========================================================================================================================================
	//------------------------------	解析大表Excel数据并保存到数据库	开始	------------------------------
	@Override
	@Transactional
	public int parseBigMeterExcelDataToDb(String traceIds, List<Map> bigMeterMapList) {
		
		int rows = 1;
		
		for(Map<String, Object> bigMeterMap : bigMeterMapList) {
			
			//获取地理位置-房间的长编码
			//String longCode = this.getRoomLongCode(locationBlockCode, bigMeterMap.get("location").toString());
			//String serialNumber = (String)bigMeterMap.get("serialNumber");//抄表序号
			String locationStr = (String)bigMeterMap.get("locationName");//地理位置
			String partWaterRule = (String)bigMeterMap.get("partWaterRule");//分水量规则，如：学生生活用水:75,学校用水:25
			//获取地理位置
			//Location location = this.getLocationIdByLongCode(longCode);
			Location location = this.getLocation(traceIds, locationStr);
			if(location==null) {
				rows = 0;
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			
			//获取客户信息
			Customers customer = this.getCustomer(bigMeterMap);
			boolean isInsertCustomer = customer.getId()==null || customer.getId()<=0;//是否增加客户
			//获取表计信息
			Meters meter = this.getMeter(bigMeterMap);
			boolean isInsertMeter = meter.getId()==null || meter.getId()<=0;//是否增加表计
			
			if(isInsertCustomer && isInsertMeter) {
				
				//增加客户
				rows = customersService.insertSelective(customer);
				//增加地理位置-客户关系
				this.insertLocationCustomer(location.getId(), location.getTraceIds(), customer.getId());
				//增加客户-账户
				this.insertCustomerAccount(customer.getId(), customer.getCustomerName(), customer.getIdNo(), customer.getCustomerTel(), customer.getCustomerMobile());
				
				if(rows>0) {
					//增加表计，同时增加默认抄表记录
					rows = this.insertBigMeter(meter, customer.getId());
					//增加表计分水量规则
					//this.insertMeterPartWater(meter.getId(), partWaterRule);
					//增加地理位置-表计
					this.insertLocationMeter(location.getId(), location.getTraceIds(), meter.getId());
					//增加客户-表计关系
					this.insertCustomerMeter(customer.getId(), meter.getId());
				}
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
				
			}else if(isInsertCustomer && !isInsertMeter) {
				//增加客户
				rows = customersService.insertSelective(customer);
				//更新表计数据
				metersService.updateByPrimaryKeySelective(meter);
				
				//增加地理位置-客户关系
				this.insertLocationCustomer(location.getId(), location.getTraceIds(), customer.getId());
				//增加客户-账户
				this.insertCustomerAccount(customer.getId(), customer.getCustomerName(), customer.getIdNo(), customer.getCustomerTel(), customer.getCustomerMobile());
				
				//增加地理位置-表计
				this.insertLocationMeter(location.getId(), location.getTraceIds(), meter.getId());
				//增加客户-表计关系
				this.insertCustomerMeter(customer.getId(), meter.getId());
			}else if(!isInsertCustomer && isInsertMeter) {
				
				//增加表计，同时增加默认抄表记录
				rows = this.insertBigMeter(meter, customer.getId());
				//更新客户数据
				customersService.updateByPrimaryKeySelective(customer);
				
				//增加表计分水量规则
				this.insertMeterPartWater(meter.getId(), partWaterRule);
				//增加地理位置-表计
				this.insertLocationMeter(location.getId(), location.getTraceIds(), meter.getId());
				//增加客户-表计关系
				this.insertCustomerMeter(customer.getId(), meter.getId());
				
				//增加地理位置-客户关系
				this.insertLocationCustomer(location.getId(), location.getTraceIds(), customer.getId());
			}else {
				//更新客户数据
				customersService.updateByPrimaryKeySelective(customer);
				//更新表计数据
				metersService.updateByPrimaryKeySelective(meter);
			}
			
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			
		}
		return rows;
	}
	
	
	/**
	 * @Title: insertBigMeter
	 * @Description: 增加大表数据和默认抄表记录
	 * @param meter
	 * @param customerId 
	 */
	private int insertBigMeter(Meters meter, Long customerId) {
		//系统登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		int rows = metersService.insertSelective(meter);
		if(rows>0) {
			meterRecordService.insertInitMeterRecord(meter, customerId, userBean.getId(), userBean.getRealname(), meter.getNewMeterBottom());
		}
		return rows;
	}
	
	/**
	 * @Title: insertCustomerAccount
	 * @Description: 增加大表客户的账户信息
	 * @param customerId
	 * @param customerName
	 * @param customerIdNo
	 * @param customerTel
	 * @param customerMobile
	 * @return 
	 */
	private int insertCustomerAccount(Long customerId, String customerName, String customerIdNo, String customerTel, String customerMobile) {
		//账户信息
		CustomerAccount account = new CustomerAccount();
		account.setCustomerId(customerId);
		account.setAccountName(customerName);//客户名称
		account.setIdNo(customerIdNo);//证件号
		account.setTel(customerTel);//客户电话
		account.setMobile(customerMobile);//客户手机
		account.setStatus(EnumCustomerStatus.ACCOUNT_OPEN.getValue());//立户状态
		return customerAccountService.insertSelective(account);
	}
	
	/**
	 * @Title: insertCustomerMeter
	 * @Description: 增加客户与表计的关系
	 * @param customerId
	 * @param meterId
	 * @return 
	 */
	private void insertCustomerMeter(Long customerId, Long meterId) {
		
		//增加客户与表计的关系
		CustomerMeter cm = new CustomerMeter();
		cm.setCustomerId(customerId);
		cm.setMeterId(meterId);
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerMeter> cmList = customerMeterService.select(cm);
		if(cmList==null || cmList.size()<=0) {
			
			//增加客户与表计的关系
			cm = new CustomerMeter();
			cm.setCustomerId(customerId);
			cm.setMeterId(meterId);
			cm.setBindTime(new Date());
			cm.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
			cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			cm.setMeterStatus(EnumMeterSettingStatus.ZERO.getValue());
			customerMeterService.insertSelective(cm);
		}
		
	}
	
	/**
	 * @Title: insertLocationCustomer
	 * @Description: 增加地理位置与客户的关系
	 * @param locationId
	 * @param traceIds
	 * @param customerId
	 * @return 
	 */
	private void insertLocationCustomer(Long locationId, String traceIds, Long customerId) {
		
		//查询地理位置与客户的关系
		LocationCustomer lc = new LocationCustomer();
		lc.setLocationId(locationId);
		lc.setCustomerId(customerId);
		lc.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> lcList = locationCustomerService.select(lc);
		if(lcList==null || lcList.size()<=0) {
			//增加新地理位置与客户的关系
			lc = new LocationCustomer();
			lc.setLocationId(locationId);
			lc.setTraceIds(traceIds);
			lc.setCustomerId(customerId);
			lc.setBindTime(new Date());
			//lc.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
			lc.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			locationCustomerService.insertSelective(lc);
		}
		
	}
	
	/**
	 * @Title: insertLocationMeter
	 * @Description: 增加地理位置与表计的关系
	 * @param locationId
	 * @param traceIds
	 * @param meterId
	 * @return 
	 */
	private void insertLocationMeter(Long locationId, String traceIds, Long meterId) {
		
		//查询地理位置与表计的关系
		LocationMeter lm = new LocationMeter();
		lm.setLocationId(locationId);
		lm.setMeterId(meterId);
		lm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationMeter> lmList = locationMeterService.select(lm);
		if(lmList==null || lmList.size()<=0) {
			//增加新地理位置与表计的关系
			lm = new LocationMeter();
			lm.setLocationId(locationId);
			lm.setTraceIds(traceIds);
			lm.setMeterId(meterId);
			lm.setBindTime(new Date());
			lm.setMeterStatus(EnumMeterSettingStatus.ZERO.getValue());
			lm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			locationMeterService.insertSelective(lm);
		}
		
	}
	
	//------------------------------	解析大表Excel数据并保存到数据库	结束	------------------------------
	
	/**
	 * @Title: getLocation
	 * @Description: 获取地理位置节点
	 * @param traceIds
	 * @param locationName
	 * @return 
	 */
	private Location getLocation(String traceIds, String locationName) {
		
		Example example = new Example(Location.class);
		example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andLike("traceIds", traceIds+"%").andLike("pycode", "%"+locationName+"%");
		
		List<Location> locationList = locationService.selectByExample(example);
		if(locationList!=null && locationList.size()>0) {
			return locationList.get(0);
		}
		return null;
	}
	
	/**
	 * @Title: insertMeterPartWater
	 * @Description: 增加表计分水量规则
	 * @param meterId
	 * @param partWaterRule 
	 */
	private int insertMeterPartWater(Long meterId, String partWaterRule) {
		//分水量规则，如：学生生活用水:75,学校用水:25

		int rows = 1;
		
		if(StringUtils.isBlank(partWaterRule)) {
			return rows;
		}
		
		partWaterRule = StringUtils.replace(partWaterRule, "，", ",");//把中文逗号替换为英文逗号
		partWaterRule = StringUtils.replace(partWaterRule, "：", ":");//把中文冒号替换为英文冒号
		
		String[] partWaterRuleArr = partWaterRule.split(",");//把字符串 学生生活用水:75,学校用水:25 拆分 成学生生活用水:75 和 学校用水:25 
		
		//List<MeterPartWater> mpwList = new ArrayList<>();
		for(String partWaterRuleTemp : partWaterRuleArr){
			String[] partWaterRuleTempArr = partWaterRuleTemp.split(":");//把字符串 学生生活用水:75 拆分 成学生生活用水 和 75
			String priceCode = partWaterRuleTempArr[0];//水价
			String partWaterRate = partWaterRuleTempArr[1];//百分比
			
//			SysWaterPrice wp = waterPriceService.getWaterPrice(priceCode);//查询水价
//			
//			MeterPartWater mpw = new MeterPartWater();
//			mpw.setMeterId(meterId);
//			mpw.setPartWaterRate(new BigDecimal(partWaterRate));
//			mpw.setWaterPriceId(wp.getId());
//			//mpwList.add(mpw);
//			rows = meterPartWaterService.insertSelective(mpw);
//			if(rows<=0) {
//				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//				break;
//			}
		}
		return rows;
		
	}
	
	/**
	 * @Title: getCustomer
	 * @Description: 保存客户信息
	 * @param map
	 * @return 
	 */
	private Customers getCustomer(Map<String, Object> map) {
		
		String serialNumber = (String)map.get("serialNumber");//抄表序号
//		String locationName = (String)map.get("locationName");//地理位置
		String customerName = (String)map.get("customerName");//单位名称
//    	String contact = (String)map.get("contact");//联系人
    	String contactMobile = (String)map.get("contactMobile");//联系人电话
    	String customerPriceCode = (String)map.get("customerPriceCode");//客户用水性质（水价）
    	String isPartWater = (String)map.get("isPartWater");//是否分水量， 0=否；1=是；
    	
//    	String description = (String)map.get("description");//描述
    	
//    	String partWaterRule = (String)map.get("partWaterRule");//分水量规则，如：学生生活用水:75,学校用水:25
//    	String meterUse = (String)map.get("meterUse");//水表用途
    	String meterWaterPrice = (String)map.get("meterPriceCode");//水表用水性质（水价）
//    	String meterType = (String)map.get("meterType");//水表类型
//    	String steelSealNo = (String)map.get("steelSealNo");//水表钢印号（水表编号）
//    	String caliber = (String)map.get("caliber");//水表口径
//    	String factory = (String)map.get("factory");//生产厂家
    	//meterMap.put("meterWaterUse", meterWaterUse);//用水性质，无效列
//    	String newMeterBottom = (String)map.get("newMeterBottom");//表底
		
    	//根据客户名称查询，如果查询结果不为空则直接返回查询结果的第一条记录；如果查询结果为空，则创建封装客户信息，并返回
    	Customers searchData = new Customers();
    	searchData.setCustomerName(customerName);
    	searchData.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
    	List<Customers> customerList = customersService.select(searchData);
    	if(customerList!=null && customerList.size()>0) {
    		return customerList.get(0);
    	}
    	
    	
    	//查询水价
    	if(StringUtils.isBlank(customerPriceCode)) {
    		customerPriceCode = meterWaterPrice;
    	}
    	SysWaterPrice waterPrice = waterPriceService.getWaterPrice(customerPriceCode);
    	
		Customers customer = new Customers();
		customer.setRoom(serialNumber);
		
		customer.setCustomerName(customerName);
		customer.setCustomerTel(contactMobile);
		customer.setCustomerMobile(contactMobile);
		customer.setPropName(customerName);
		customer.setPropTel(contactMobile);
		customer.setPropMobile(contactMobile);
		
		//customer.setIndustry(industry);//行业性质
		if(waterPrice!=null) {
			customer.setWaterUse(waterPrice.getPriceTypeCode());
			customer.setPriceCode(waterPrice.getPriceCode());
		}
		
		customer.setIsPartWater(Integer.valueOf(isPartWater));
		
		customer.setCustomerType(EnumCustomerType.CUSTOMER_UNIT.getValue());//客户类型
		customer.setWaterStatus(EnumWaterStatus.ENABLED_NO.getValue());//用水状态
		customer.setNotifyMode(EnumNotifyMode.NOTIFY_ALL.getValue());//通知方式，0:全闭;1:微信打开,短信关闭2:短信打开,微信关闭3:全开
		//customer.setIdType(1);//证件类型,1=身份证/2=驾驶证
		customer.setStatus(EnumCustomerStatus.ACCOUNT_OPEN.getValue());//客户状态：-1=未立户；0=已立户；1=已销户；2=已过户；3=过户；
		customer.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		
		return customer;
	}
	
	/**
	 * @Title: getMeter
	 * @Description: 获取表计信息
	 * @param map
	 * @return 
	 */
	private Meters getMeter(Map<String, Object> map) {
		
		final String DN = "DN";
		
		String serialNumber = (String)map.get("serialNumber");//抄表序号
    	String customerName = (String)map.get("customerName");//单位名称
    	//String contact = (String)map.get("contact");//联系人
    	//String contactMobile = (String)map.get("contactMobile");//联系人电话
    	//String customerPriceCode = (String)map.get("customerPriceCode");//客户用水性质（水价）
    	//String isPartWater = (String)map.get("isPartWater");//是否分水量， 0=否；1=是；
    	
    	String description = (String)map.get("description");//描述
    	
    	//String partWaterRule = (String)map.get("partWaterRule");//分水量规则，如：学生生活用水:75,学校用水:25
    	String meterUse = (String)map.get("meterUse");//水表用途
    	String meterWaterPrice = (String)map.get("meterPriceCode");//水表用水性质（水价）
    	String meterType = (String)map.get("meterType");//水表类型
    	String steelSealNo = (String)map.get("steelSealNo");//水表钢印号（水表编号）
    	String caliber = (String)map.get("caliber");//水表口径
    	String factory = (String)map.get("factory");//生产厂家
    	//meterMap.put("meterWaterUse", meterWaterUse);//用水性质，无效列
    	String newMeterBottom = (String)map.get("newMeterBottom");//表底
		
    	description = StringUtils.replace(description, " ", "");//把空格替换为空串
    	
    	//表计位置
    	String place = customerName;
    	if(StringUtils.isNotBlank(description)) {
    		place = place+"（"+description+"）";
    	}
    	
    	if(StringUtils.isNotBlank(caliber) && caliber.indexOf(DN)==-1) {
    		caliber = DN+caliber;
		}
    	
    	//根据客户名称+描述查询表计位置，如果查询结果不为空则直接返回查询结果的第一条记录；如果查询结果为空，则创建封装表计信息，并返回
    	Meters searchData = new Meters();
    	//searchData.setPlace(place);
    	searchData.setMeterNo(steelSealNo);//水表编号
    	searchData.setSteelSealNo(steelSealNo);//钢印号
    	searchData.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
    	List<Meters> meterList = metersService.select(searchData);
    	if(meterList!=null && meterList.size()>0) {
    		Meters meterOriginal = meterList.get(0);
    		meterOriginal.setNewMeterBottom(newMeterBottom);
    		return meterOriginal;
    	}
    	
    	
    	//查询水价
    	SysWaterPrice waterPrice = waterPriceService.getWaterPrice(meterWaterPrice);
    	
    	//水表用途
    	DataDict meterUseDict = dataDictService.getDataDictOne(DataDictType.METER_USE, meterUse);
    	//水表类型
    	DataDict meterTypeDict = dataDictService.getDataDictOne(DataDictType.METER_TYPE, meterType);
    	//水表口径
    	//DataDict caliberDict = dataDictService.getDataDictOne(DataDictType.WARTER_CALIBER, caliber);
    	//水表生产厂家
    	DataDict factoryDict = dataDictService.getDataDictOne(DataDictType.METER_FACTORY, factory);
    	
		Meters meter = new Meters();
		meter.setMeterNo(steelSealNo);
		meter.setSteelSealNo(steelSealNo);
		if(meterTypeDict!=null) {
			meter.setMeterType(meterTypeDict.getKey());
		}
		
		meter.setCaliber(caliber);
		if(factoryDict!=null) {
			meter.setFactory(factoryDict.getKey());
		}
		if(waterPrice!=null) {
			meter.setWaterUse(waterPrice.getPriceTypeCode());
			meter.setPriceCode(waterPrice.getPriceCode());
		}
		
		meter.setNewMeterBottom(newMeterBottom);
		if(meterUseDict!=null) {
			meter.setMeterUse(meterUseDict.getKey());
		}
		
		meter.setPlace(place);
		meter.setDescription(description);
		
		meter.setVirtualReal(EnumMeterVirtualReal.REAL_METER.getValue());//虚/实表，0：实表（默认），1：虚表
		meter.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		meter.setMeterStatus(EnumMeterStatus.DELETE_NO.getValue());//表计状态-正常
		meter.setReadMode(EnumReadMode.READ_MANUAL.getCode());//抄表方式-人工抄表
		meter.setStatus(EnumMeterSettingStatus.ZERO.getValue());//复装/暂拆=0
		meter.setCycleStatus(EnumMeterCycleStatus.ENABLED.getValue());//记录水表的生命周期状态 0：待用（待使用）（默认值）；1：领用；2：待启用；3：使用中；4：待检测；5：检测中；6：待检修；7：检修中；8：报废；9：退库；
		
		meter.setChannelNo(serialNumber);
		
		return meter;
	}
	
	public static void main(String[] args) {
//		String addr = "1001";
//		System.out.println(addr.substring(0, addr.length()-3));
//		System.out.println(addr.substring(addr.length()-3));
		
		String a = "KQHmwcHWm81SKR+D4JL+2SkB5sHB1pvN";
		String b = "KQHmwcHWm81SKR+D4JL+2SkB5sHB1pvN";
		
		if(a.equals(b)) {
			System.out.println("true");
		}else {
			System.out.println("false");
		}
		
	}
	
}
