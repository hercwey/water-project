package com.learnbind.ai.service.customers.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumCustomerStatus;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMeterStatus;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.dao.CustomersMapper;
import com.learnbind.ai.dao.LocationMapper;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.customers.impl
 *
 * @Title: CustomersServiceImpl.java
 * @Description: 客户档案
 *
 * @author Thinkpad
 * @date 2019年5月20日 下午12:29:27
 * @version V1.0 
 *
 */
@Service
public class CustomersServiceImpl extends AbstractBaseService<Customers, Long> implements  CustomersService {
	
	@Autowired
	public CustomersMapper customersMapper;
	@Autowired
	public LocationMapper locationMapper;
	@Autowired
	public MetersService metersService;
	@Autowired
	public CustomerMeterService customerMeterService;//客户-表计服务
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户服务
	@Autowired
	private LocationMeterService locationMeterService;//地理位置-表计服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private MeterBookService meterBookService;//表册服务
	@Autowired
	private MeterBookMeterService meterBookMeterService;//地理位置-表计服务
	@Autowired
	private CustomerAccountService customerAccountService;//客户-账户服务
	@Autowired
	private MeterRecordService meterRecordService;//抄笔记录
	
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public CustomersServiceImpl(CustomersMapper mapper) {
		this.customersMapper=mapper;
		this.setMapper(mapper);
	}


	/** 
	 * @Title: searchCustomers
	 * @Description: 查询客户档案
	 * @param searchCond
	 * @param statusList
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#searchCustomers(java.lang.String, java.util.List)
	 */
	@Override
	public List<Customers> searchCustomers(String searchCond, List<Integer> statusList, Integer customerType) {
		return customersMapper.searchCustomers(searchCond, statusList, customerType);
	}

	/** 
	 * @Title: insertCustomers
	 * @Description: 增加客户信息，并绑定客户与地理位置的关系
	 * @param customers
	 * @param locationId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#insertCustomers(com.learnbind.ai.model.Customers, java.lang.Long)
	 */
	@Override
	@Transactional
	public int insertCustomers(Customers customers, Long locationId) {
		
		//保存客户信息到数据库
		int rows = customersMapper.insertSelective(customers);
		if(rows>0) {
			if(locationId!=null) {
				//绑定客户与地理位置的关系
				Location location = locationService.selectByPrimaryKey(locationId);
				String traceIds = location.getTraceIds();
				Long customerId = customers.getId();
				rows = locationCustomerService.bind(locationId, traceIds, customerId);
			}
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateCustomers
	 * @Description: 修改客户档案
	 * @param customers
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#updateCustomers(com.learnbind.ai.model.Customers)
	 */
	@Override
	@Transactional
	public int updateCustomers(Customers customer, Long prevLocationId, Long locationId) {
		int rows = customersMapper.updateByPrimaryKeySelective(customer);
		if(rows>0) {
			//如果原来地理位置与表计有绑定关系,且当前地理位置与表计没有绑定关系时
			if(prevLocationId!=null && locationId==null) {
				//解除表计与原地理位置的绑定关系
				rows = locationCustomerService.unbind(prevLocationId, customer.getId());
			}else if(prevLocationId==null && locationId!=null) {//如果原来地理位置与表计没有绑定关系,且当前地理位置与表计有绑定关系时
				//增加表计与当前地理位置的关系
				Location location = locationService.selectByPrimaryKey(locationId);
				rows = locationCustomerService.bind(locationId, location.getTraceIds(), customer.getId());
			}else if(prevLocationId!=null && locationId!=null) {//如果原来地理位置与表计有绑定关系,且当前地理位置与表计也有绑定关系时
				if(!prevLocationId.equals(locationId)) {
					//解除表计与原地理位置的绑定关系
					rows = locationCustomerService.unbind(prevLocationId, customer.getId());
					if(rows>0) {
						//增加表计与当前地理位置的关系
						Location location = locationService.selectByPrimaryKey(locationId);
						rows = locationCustomerService.bind(locationId, location.getTraceIds(), customer.getId());
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
	 * (非 Javadoc)
	 * 
	 * @Title: deleteCustomers
	 * @Description: 删除客户档案
	 * @param customersId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#deleteCustomers(long)
	 */
	@Override
	public int deleteCustomers(long customersId) {
		try {
			Customers customer = new Customers();
			customer.setId(customersId);
			customer.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			int rows = customersMapper.updateByPrimaryKeySelective(customer);
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<Customers> findAllCustomers(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<Customers> discountList = customersMapper.selectAll();
		PageInfo result = new PageInfo(discountList);
		return result;
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateWaterStatus
	 * @Description: 修改客户用水状态
	 * @param customerId
	 * @param waterStatus
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#updateWaterStatus(java.lang.Long, java.lang.Integer)
	 */
	@Override
	@Transactional
	public int updateWaterStatus(Long customerId, Integer waterStatus) {
		
		Customers customer = new Customers();
		customer.setId(customerId);
		customer.setWaterStatus(waterStatus);
		
		int rows = customersMapper.updateByPrimaryKeySelective(customer);
		if(rows>0) {
			CustomerMeter record = new CustomerMeter();
			record.setCustomerId(customerId);
			record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			List<CustomerMeter> cmList = customerMeterService.select(record);
			for(CustomerMeter cm : cmList) {
				Long meterId = cm.getMeterId();
				
				Meters meter = new Meters();
				meter.setId(meterId);
				meter.setMeterStatus(waterStatus);
				rows = metersService.updateByPrimaryKeySelective(meter);
				if(rows<=0) {
					break;
				}
			}
			
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}


	@Override
	public List<Map<String, Object>> getCustomerListByMeterId(Long meterId, String searchCond) {
		return customersMapper.getCustomerListByMeterId(meterId, searchCond);
	}
	
	@Override
	public List<Map<String, Object>> getCustomerListByLocationId(Long locationId, String searchCond) {
		return customersMapper.getCustomerListByLocationId(locationId, searchCond);
	}

	@Override
	@Transactional
	public int transferAccount(Long oldCustomerId, Long newCustomerId) {
		
		int rows = 1;
		this.updateCustomerStatus(oldCustomerId, EnumCustomerStatus.ACCOUNT_TRANSFER.getValue());//设置过户前客户的状态为已过户
		this.updateCustomerStatus(newCustomerId, EnumCustomerStatus.ACCOUNT_TRANSFER_OPEN.getValue());//设置新客户的状态为过户
		//处理地理位置-客户关系表
		LocationCustomer lc = new LocationCustomer();
		lc.setCustomerId(oldCustomerId);
		lc.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> lcList = locationCustomerService.select(lc);
		for(LocationCustomer lcTemp : lcList) {
			lc = new LocationCustomer();
			lc.setId(lcTemp.getId());
			lc.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = locationCustomerService.updateByPrimaryKeySelective(lc);
			if(rows<=0) {
				break;
			}
			lcTemp.setId(null);
			lcTemp.setCustomerId(newCustomerId);
			rows = locationCustomerService.insertSelective(lcTemp);
			if(rows<=0) {
				break;
			}
		}
		
		//处理客户-表计关系表
		CustomerMeter cm = new CustomerMeter();
		cm.setCustomerId(oldCustomerId);
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<CustomerMeter> cmList = customerMeterService.select(cm);
		for(CustomerMeter cmTemp : cmList) {
			
			cm = new CustomerMeter();
			cm.setId(cmTemp.getId());
			cm.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = customerMeterService.updateByPrimaryKeySelective(cm);
			if(rows<=0) {
				break;
			}
			cmTemp.setId(null);
			cmTemp.setCustomerId(newCustomerId);
			rows = customerMeterService.insertSelective(cmTemp);
			if(rows<=0) {
				break;
			}
			//查询旧客户的最新抄表记录
			MeterRecord meterRecord = meterRecordService.selectNewestMeterRecord(oldCustomerId, cmTemp.getMeterId());
			meterRecord.setId(null);
			meterRecord.setCustomerId(newCustomerId);
			rows = meterRecordService.insertSelective(meterRecord);
			
		}
		
		//TODO 地理位置-表计关系表	此关系是否需要重新设置？
		/** List<LocationMeter> lmList = locationMeterService.getListByMeterIdList(meterIdList);
		for(LocationMeter lmTemp : lmList) {
			
			LocationMeter lm = new LocationMeter();
			lm.setId(lmTemp.getId());
			lm.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			int rows = locationMeterService.updateByPrimaryKeySelective(lm);
			if(rows<=0) {
				break;
			}
			lmTemp.setId(null);
			rows = locationMeterService.insertSelective(lmTemp);
			if(rows<=0) {
				break;
			}
		} **/
		
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}

	/**
	 * @Title: setCustomerStatus
	 * @Description: 修改客户状态（同步更新客户-账户状态）
	 * @param customerId
	 * @param customerStatus 
	 */
	private void updateCustomerStatus(Long customerId, Integer customerStatus) {
		
		//修改客户状态
		Customers customer = new Customers();
		customer.setId(customerId);
		customer.setStatus(customerStatus);
		customersMapper.updateByPrimaryKeySelective(customer);
		
		//修改客户-账户状态
		//条件
		Example example = new Example(CustomerAccount.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		//需要修改的内容
		CustomerAccount ca = new CustomerAccount();
		ca.setStatus(customerStatus);
		customerAccountService.updateByExampleSelective(ca, example);
	}

	@Override
	public List<Customers> searchCustomerAgreement(String searchCond, Integer agreementType) {
		return customersMapper.searchCustomerAgreement(searchCond,agreementType);
	}


	@Override
	public List<Customers> searchTripartAgreement(String searchCond) {
		return customersMapper.searchTripartAgreement(searchCond);
	}
	
	/**
	 * @Title: getCustomerNameById
	 * @Description: 根据id获取客户姓名
	 * @param id
	 * @return 
	 */
	public String getCustomerNameById(Long id) {
		return customersMapper.getCustomerNameById(id);
	}
	
	/** 
	 * @Title: getCustomersList
	 * @Description: 根据条件和地理位置痕迹ID（ID-ID-ID-ID）查询客户
	 * @param searchCond
	 * @param traceIds
	 * @param statusList
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#getCustomersList(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public List<Customers> getCustomersList(String searchCond, String traceIds, List<Integer> statusList, Integer customerType) {
		List<Customers> customerList = customersMapper.getCustomersList(searchCond, traceIds, statusList, customerType);
		return customerList;
	}


	@Override
	public List<Customers> searchCustomerNo(String telNo, String customerName) {
		List<Customers> customerList = customersMapper.searchCustomerNo(telNo, customerName);
		return customerList;
	}


	/**
	 *	绑定水表
	 */
	@Override
	@Transactional
	public int bindBigMeter(List<Long> meterIdList, Long customerId) {
		//根据表计ID集合设置客户-表计关系的默认客户状态为非默认状态
		this.setCMDefaultStatus(meterIdList);
		Date sysdate = new Date();
		int rows = 0;
		for(Long meterId : meterIdList) {
			//添加客户水表绑定关系
			CustomerMeter cm = new CustomerMeter();
			cm.setCustomerId(customerId);
			cm.setMeterId(meterId);
			cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			cm.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
			cm.setMeterStatus(EnumMeterStatus.DELETE_NO.getValue());
			cm.setBindTime(sysdate);
			rows = customerMeterService.insertSelective(cm);
			//增加抄表记录
			rows = this.insertMeterRecord(meterId, customerId);
			if(rows<0) {
				break;
			}
			
			//根据水表id查询绑定的地理位置
			LocationMeter location = locationMeterService.getLocationByMeterId(meterId); 
			
			LocationCustomer lc = new LocationCustomer();
			lc.setCustomerId(customerId);
			lc.setLocationId(location.getLocationId());
			lc.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			List<LocationCustomer> lcList = locationCustomerService.select(lc);
			if(lcList.size()<=0) {
				lc.setBindTime(sysdate);
				lc.setTraceIds(location.getTraceIds());
				//TODO 
				rows = locationCustomerService.insertSelective(lc);
				
			}
		}

		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	/**
	 * @Title: setCMDefaultStatus
	 * @Description: 根据表计ID集合设置客户-表计关系的默认客户状态为非默认状态
	 * @param meterIdList
	 * @return 
	 */
//	private int setCMDefaultStatus(List<Long> meterIdList) {
//		Example example = new Example(CustomerMeter.class);
//		example.createCriteria().andIn("meterId", meterIdList).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//		List<CustomerMeter> cmList = customerMeterService.selectByExample(example);
//		List<Long> cmIdList = new ArrayList<>();
//		for(CustomerMeter cm : cmList) {
//			cmIdList.add(cm.getId());
//		}
//		if(cmIdList!=null && cmIdList.size()>0) {
//			example = new Example(CustomerMeter.class);
//			example.createCriteria().andIn("id", cmIdList);
//			CustomerMeter updateData = new CustomerMeter();
//			updateData.setDefaultCustomer(EnumDefaultStatus.DEFAULT_NO.getValue());
//			return customerMeterService.updateByExampleSelective(updateData, example);
//		}
//		return 1;
//	}
	
	/**
	 * @Title: setCMDefaultStatus
	 * @Description: 根据表计ID集合设置客户-表计关系的默认客户状态为非默认状态
	 * @param meterIdList
	 * @return 
	 */
	private int setCMDefaultStatus(List<Long> meterIdList) {
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
	
	/**
	 * @Title: insertMeterRecord
	 * @Description: 增加抄表记录
	 * @param meterId
	 * @param customerId
	 * @return 
	 */
	@Transactional
	private int insertMeterRecord(Long meterId, Long customerId) {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Meters meter = metersService.selectByPrimaryKey(meterId);
		int rows = 1;
		Date sysdate = new Date();
		//增加抄表记录
		MeterRecord meterRecord = new MeterRecord();
		if(StringUtils.isNotBlank(meter.getNewMeterBottom())) {//判断新表读数是否为空
			meterRecord.setCurrRead(meter.getNewMeterBottom());
		} else {
			meterRecord.setCurrRead("0");
		}
		meterRecord.setCustomerId(customerId);
		meterRecord.setMeterId(meterId);
		//期间为系统日期-一个月
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
		String period = formatter.format(DateUtils.addMonths(sysdate, -1));
		meterRecord.setPeriod(period);
		//本期抄表日期系统日期-一个月
		meterRecord.setCurrDate(DateUtils.addMonths(sysdate, -1));
		rows = meterRecordService.insertMeterRecord(meterRecord, userBean.getId(), userBean.getRealname());
		if(rows<0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	@Override
	@Transactional
	public int unbindBigMeter(List<Long> meterIdList, Long customerId) {
		int rows = 1;
		
		for(Long meterId : meterIdList) {
			//查询客户水表绑定关系
			CustomerMeter cm = new CustomerMeter();
			cm.setCustomerId(customerId);
			cm.setMeterId(meterId);
			cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			List<CustomerMeter> tempCustomerList =  customerMeterService.select(cm);
			for(CustomerMeter cumes : tempCustomerList) {
				cumes.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				rows = customerMeterService.updateByPrimaryKeySelective(cumes);
				if(rows<0) {
					break;
				}
			}
			//根据水表id查询绑定的地理位置
			LocationMeter location = locationMeterService.getLocationByMeterId(meterId); 
			
			int count = customerMeterService.getCustomerBindedMeter(location.getLocationId());
			if(count<1) {
				
				//查询地理位置客户绑定关系
				LocationCustomer lc = new LocationCustomer();
				lc.setCustomerId(customerId);
				lc.setLocationId(location.getLocationId());
				lc.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
				
				List<LocationCustomer> tempLocationList =  locationCustomerService.select(lc);
				for(LocationCustomer lcus : tempLocationList) {
					lcus.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
					rows = locationCustomerService.updateByPrimaryKeySelective(lcus);
					if(rows<0) {
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
	 * @Title: bindMeter
	 * @Description: 绑定表计
	 * @param meterIdList
	 * @param customerId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#bindMeter(java.util.List, java.lang.Long)
	 */
	@Override
	public int bindMeter(List<Long> meterIdList, Long customerId) {
		
		try {
			int rows = 0;
			for(Long meterId : meterIdList) {
				//设置客户-表计关系表中客户状态为否
				customerMeterService.setDefaultCustomerStatusNo(meterId);
				//建立客户-表计关系
				rows = customerMeterService.bind(customerId, meterId, EnumDefaultStatus.DEFAULT_YES.getValue());
				if(rows<=0) {
					break;
				}
				//查询表计是否绑定地理位置
				LocationMeter lm = locationMeterService.getLocationByMeterId(meterId);
				if(lm!=null) {//表计已绑定地理位置
					//建立地理位置与客户的关系
					Long locationId = lm.getLocationId();//地理位置ID
					Location location = locationService.selectByPrimaryKey(locationId);
					
					//建立地理位置-客户的关系（建立关系前先验证是否已绑定，未绑定则绑定，已绑定则不处理）
					locationCustomerService.insertByLocationIdAndCustId(locationId, location.getTraceIds(), customerId);
				}
				//增加初始化抄表记录
				meterRecordService.insertInitMeterRecord(meterId, customerId);
			}
			
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	@Transactional
	public int unbindMeter(List<Long> meterIdList, Long customerId) {
		int rows = 0;
		
		for(Long meterId : meterIdList) {
			
			//解除客户-表计关系(并重新设置默认客户)
			rows = customerMeterService.unbind(customerId, meterId);
			if(rows<=0) {
				break;
			}
			
			//查询表计是否已绑定地理位置
			LocationMeter lm = locationMeterService.getLocationByMeterId(meterId);
			if(lm!=null) {//表计已绑定地理位置
				Long locationId = lm.getLocationId();//地理位置ID
				//解除地理位置-表计的绑定关系
				//locationMeterService.deleteByLocationIdAndMeterId(locationId, meterId);
				//解除地理位置-客户的绑定关系
				locationCustomerService.deleteByLocationIdAndCustId(locationId, customerId);
			}
			
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: selectCustomerBidMeterInfo
	 * @Description: 根据表计id查询绑定的默认客户信息
	 * @param meterId
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#selectCustomerBidMeterInfo(java.lang.Long)
	 */
	@Override
	public Customers selectCustomerBidMeterInfo(Long meterId) {
		return customersMapper.selectCustomerBidMeterInfo(meterId);
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getCustomerMessage
	 * @Description: 根据地理位置id获取客户信息
	 * @param traceIds
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#getCustomerMessage(java.lang.String)
	 */
	@Override
	public LocationCustomer getCustomerMessage(String traceIds) {
		LocationCustomer lcTemp = new LocationCustomer();
		lcTemp.setTraceIds(traceIds);
		//lcTemp.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		List<LocationCustomer> lcList = locationCustomerService.select(lcTemp);
		lcTemp = lcList.get(0);
		return lcTemp;
	}


	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getInsertAccountCustomerList
	 * @Description: 获取增加账单时客户列表
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#getInsertAccountCustomerList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> getInsertAccountCustomerList(String searchCond, String traceIds) {
		return customersMapper.getInsertAccountCustomerList(searchCond, traceIds);
	}


	@Override
	public List<Customers> getCustomerListByRoom(String traceIds, String room) {
		return customersMapper.getCustomerListByRoom(traceIds, room);
	}


	@Override
	public List<Map<String, Object>> getCustomerListByTraceIds(String traceIds, String searchCond) {
		return customersMapper.getCustomerListByTraceIds(traceIds, searchCond);
	}


	@Override
	public List<Customers> getCustomerData(String traceIds) {
		return customersMapper.getCustomerData(traceIds);
	}


	@Override
	public List<Customers> getDefaultCustomerList(String traceIds, String searchCond, Integer customerType, Integer oweMonth) {
		return customersMapper.getDefaultCustomerList(traceIds, searchCond, customerType, oweMonth);
	}
	

	@Override
	public List<Customers> getDefaultUnitCustomerList(String traceIds, String searchCond, Integer customerType) {
		return customersMapper.getDefaultUnitCustomerList(traceIds, searchCond, customerType);
	}


	@Override
	public List<Customers> getCustomerList(String customerName) {
		return customersMapper.getList(customerName);
	}


	@Override
	public List<Customers> getNotifyCustomerList(String traceIds, String searchCond, Integer customerType, String period) {
		return customersMapper.getNotifyCustomerList(traceIds, searchCond, customerType, period);
	}

	@Override
	public List<Customers> getPrepaymentCustomerList(String searchCond, String traceIds) {
		return customersMapper.getPrepaymentCustomerList(searchCond, traceIds);
	}

}
