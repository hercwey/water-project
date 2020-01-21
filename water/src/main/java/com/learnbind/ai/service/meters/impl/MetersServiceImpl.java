package com.learnbind.ai.service.meters.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.MeterBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.common.enumclass.EnumAddSubWaterStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumMakeBillStatus;
import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterRecordStatus;
import com.learnbind.ai.common.enumclass.EnumPartitionWaterStatus;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.EnumReadType;
import com.learnbind.ai.dao.MetersMapper;
import com.learnbind.ai.jsengine.PartitionRuleUtil;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.MeterPartWaterRule;
import com.learnbind.ai.model.MeterRecord;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meters.MeterPartWaterRuleService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;



/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MetersServiceImpl.java
 * @Description: 表计档案
 *
 * @author Thinkpad
 * @date 2019年5月17日 下午6:28:04
 * @version V1.0 
 *
 */
@Service
public class MetersServiceImpl extends AbstractBaseService<Meters, Long> implements  MetersService {
	
	@Autowired
	public MetersMapper metersMapper;
	@Autowired
	public LocationService locationService;
	@Autowired
	public CustomerMeterService customerMeterService;
	@Autowired
	public CustomersService customersService;
	@Autowired
	private LocationCustomerService locationCustomerService;//地理位置-客户服务
	@Autowired
	private LocationMeterService locationMeterService;//地理位置-表计服务
	@Autowired
	private MeterRecordService meterRecordService;//抄表记录
	@Autowired
	private MeterPartWaterRuleService meterPartWaterRuleService;//表计分水量规则
	@Autowired
	private MeterBookMeterService meterBookMeterService;//表册-表计关系
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MetersServiceImpl(MetersMapper mapper) {
		this.metersMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<Meters> getListBySearchCond(String searchCond) {
		return metersMapper.getListBySearchCond(searchCond);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchMeters
	 * @Description: 查询表计档案
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.Meters.MetersService#searchMeters(java.lang.String)
	 */
	@Override
	public List<Meters> searchMeters(String searchCond) {
		return metersMapper.searchMeters(searchCond);
	}
	
	@Override
	public List<Meters> searchChangeMeters(String searchCond) {
		List<Integer> cycleStatusList = new ArrayList<>();
		cycleStatusList.add(EnumMeterCycleStatus.INACTIVE.getValue());//待用
		cycleStatusList.add(EnumMeterCycleStatus.RECEIVE.getValue());//领用
		cycleStatusList.add(EnumMeterCycleStatus.NOT_ENABLED.getValue());//待启用
		return metersMapper.searchChangeMeters(searchCond, cycleStatusList);
	}

	@Override
	public List<Meters> searchRemoveMeters(String searchCond, Integer status) {
		return metersMapper.searchMetersByStatus(searchCond, status);
	}

	@Override
	public List<Meters> getMeterList(String searchCond, String traceIds) {
		return metersMapper.getMeterList(searchCond, traceIds);
	}
	
	@Override
	public List<Meters> getChangeMeterList(String searchCond, String traceIds) {
		List<Integer> cycleStatusList = new ArrayList<>();
		cycleStatusList.add(EnumMeterCycleStatus.INACTIVE.getValue());//待用
		cycleStatusList.add(EnumMeterCycleStatus.RECEIVE.getValue());//领用
		cycleStatusList.add(EnumMeterCycleStatus.NOT_ENABLED.getValue());//待启用
		return metersMapper.getChangeMeterList(searchCond, traceIds, cycleStatusList);
	}

	@Override
	public List<Meters> getRemoveMeterList(String searchCond, String traceIds, Integer status) {
		return metersMapper.getMeterListByStatus(searchCond, traceIds, status);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertMeters
	 * @Description: 添加表计档案
	 * @param Meters
	 * @return 
	 * @see com.learnbind.ai.service.Meters.MetersService#insertMeters(com.learnbind.ai.model.Meters)
	 */
	@Override
	@Transactional
	public int insertMeters(Meters meters, Long locationId) {
		int rows = metersMapper.insertSelective(meters);
		if(rows>0) {
			
			if(locationId!=null) {
				//增加地理位置与表计的关系
				this.bindLocation(locationId, meters.getId());
				
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
	 * @Title: updateMeters
	 * @Description: 修改客户档案
	 * @param Meters
	 * @return 
	 * @see com.learnbind.ai.service.Meters.MetersService#updateMeters(com.learnbind.ai.model.Meters)
	 */
	@Override
	@Transactional
	public int updateMeters(Meters meter, Long prevLocationId, Long locationId) {
		
		//修改表计信息
		int rows = metersMapper.updateByPrimaryKeySelective(meter);
		if(rows>0) {
			
			//如果原来地理位置与表计有绑定关系,且当前地理位置与表计没有绑定关系时
			if(prevLocationId!=null && locationId==null) {
				//解除表计与原地理位置的绑定关系
				this.unbindLocation(prevLocationId, meter.getId());
			}else if(prevLocationId==null && locationId!=null) {//如果原来地理位置与表计没有绑定关系,且当前地理位置与表计有绑定关系时
				//增加地理位置与表计的关系
				this.bindLocation(locationId, meter.getId());
			}else if(prevLocationId!=null && locationId!=null) {//如果原来地理位置与表计有绑定关系,且当前地理位置与表计也有绑定关系时
				if(!prevLocationId.equals(locationId)) {
					//解除表计与原地理位置的绑定关系
					this.unbindLocation(prevLocationId, meter.getId());
					this.bindLocation(locationId, meter.getId());
				}
			}
			
		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	/**
	 * @Title: bindLocation
	 * @Description: 建立表计-地理位置关系
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	private int bindLocation(Long locationId, Long meterId) {
		//增加地理位置与表计的关系
		Location location = locationService.selectByPrimaryKey(locationId);
		//rows = locationMeterService.bind(locationId, location.getTraceIds(), meters.getId());
		//建立地理位置-表计关系
		int rows = locationMeterService.insertByLocationIdAndMeterId(locationId, location.getTraceIds(), meterId);
		
		//查询表计绑定的客户
		List<CustomerMeter> cmList = customerMeterService.getListByMeterId(meterId);
		for(CustomerMeter cm : cmList) {
			Long customerId = cm.getCustomerId();//绑定的客户ID
			
			//建立地理位置-客户的关系（建立关系前先验证是否已绑定，未绑定则绑定，已绑定则不处理）
			locationCustomerService.insertByLocationIdAndCustId(locationId, location.getTraceIds(), customerId);
			
		}
		return rows;
	}
	/**
	 * @Title: unbindLocation
	 * @Description: 解除表计-地理位置关系
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	private int unbindLocation(Long locationId, Long meterId) {
		//解除表计与原地理位置的绑定关系
		//rows = locationMeterService.unbind(prevLocationId, meter.getId());
		int rows = locationMeterService.deleteByLocationIdAndMeterId(locationId, meterId);
		//查询表计绑定的客户
		List<CustomerMeter> cmList = customerMeterService.getListByMeterId(meterId);
		for(CustomerMeter cm : cmList) {
			Long customerId = cm.getCustomerId();//绑定的客户ID
			
			//解除地理位置-客户的关系
			locationCustomerService.deleteByLocationIdAndCustId(locationId, customerId);
			
		}
		return rows;
	}
	

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: deleteMeters
	 * @Description: 删除表计档案
	 * @param MetersId
	 * @return 
	 * @see com.learnbind.ai.service.Meters.MetersService#deleteMeters(long)
	 */
	@Override
	@Transactional
	public int deleteMeters(long meterId) {
		try {
			
			Date sysDate = new Date();//系统日期
			
			Meters record = new Meters();
			record.setId(meterId);
			record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			int rows = metersMapper.updateByPrimaryKeySelective(record);
			if(rows>0) {
				//解除地理位置与表计的关系
				Example example = new Example(LocationMeter.class);
				example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("meterId", meterId);
				LocationMeter lmTemp = new LocationMeter();
				lmTemp.setUnbindTime(sysDate);
				lmTemp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				locationMeterService.updateByExampleSelective(lmTemp, example);
				//解除客户与表计的关系
				example = new Example(LocationMeter.class);
				example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andEqualTo("meterId", meterId);
				CustomerMeter cmTemp = new CustomerMeter();
				cmTemp.setUnbindTime(sysDate);
				cmTemp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				customerMeterService.updateByExampleSelective(cmTemp, example);
				return rows;
			}
//			Meters meter = metersMapper.selectByPrimaryKey(metersId);
//			
//			if(getLeafNodeSize(meter.getId())>0){//如果有子节点
//				return deleteNodes(metersId);
//			}else {//如果没有子节点直接删除当前节点
//				Meters record = new Meters();
//				record.setId(metersId);
//				record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
//				int rows = metersMapper.updateByPrimaryKeySelective(record);
//				if(rows>0) {
//					//TODO 此处可删除与地理位置相关的关系表
//					return rows;
//				}
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	@Override
	@Transactional
	public int deleteMeters(List<Long> meterIds) {
		try {
			int rows = 0;
			for(Long meterId : meterIds) {
				rows = this.deleteMeters(meterId);
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
			return rows;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * 	获取叶子节点个数
	 * @param id
	 * @return
	 */
	private int getLeafNodeSize(Long id) {
		Meters record = new Meters();
		record.setPid(id);
		List<Meters> meterList = metersMapper.select(record);
		if(meterList!=null) {
			return meterList.size();
		}
		return 0;
	}
	
	/**
	 * 	循环删除子节点
	 * @param id
	 * @return
	 */
	@Transactional
	private int deleteNodes(Long pid) throws Exception{
		//System.out.println("开始查询并删除子节点 主节点ID："+pid);
		Meters record = new Meters();
		record.setPid(pid);
		List<Meters> meterList = metersMapper.select(record);
		int rows = 0;
		for(int i=0; i<meterList.size(); i++){
			Meters meter = meterList.get(i);
			//System.out.println("for循环 节点："+meter.toString());
			if(getLeafNodeSize(meter.getId())<=0){//如果没有子节点
				Meters temp = new Meters();
				temp.setId(meter.getId());
				temp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				rows = metersMapper.updateByPrimaryKeySelective(temp);
				//System.out.println("for循环 删除节点："+meter.toString());
				if(rows>0) {
					//TODO 此处可删除与地理位置相关的关系表
				}
			}else {
				rows = deleteNodes(meter.getId());
			}
		}
		if(rows>0) {
			Meters temp = new Meters();
			temp.setId(pid);
			temp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = metersMapper.updateByPrimaryKeySelective(temp);
			//System.out.println("删除 主节点ID："+pid);
			if(rows>0) {
				//TODO 此处可删除与地理位置相关的关系表
			}
			return rows;
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/**
	 * 这个方法中用到了我们开头配置依赖的分页插件pagehelper 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
	 * pageNum 开始页数 pageSize 每页显示的数据条数
	 */
	@Override
	public PageInfo<Meters> findAllMeters(int pageNum, int pageSize) {
		// 将参数传给这个方法就可以实现物理分页了，非常简单。
		PageHelper.startPage(pageNum, pageSize);
		List<Meters> metersList = metersMapper.selectAll();
		PageInfo<Meters> result = new PageInfo<Meters>(metersList);
		return result;
	}

	@Override
	@Transactional
	public int updateListById(List<Meters> meterList) {
		int rows = 0;
		try {
			for(Meters meter : meterList) {
				int row = metersMapper.updateByPrimaryKeySelective(meter);
				rows = rows + row;
			}
			if(rows>0) {
				return rows;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateWaterStatus
	 * @Description: 修改表计用水状态
	 * @param customerId
	 * @param waterStatus
	 * @return 
	 * @see com.learnbind.ai.service.customers.CustomersService#updateWaterStatus(java.lang.Long, java.lang.Integer)
	 */
	@Override
	@Transactional
	public int updateMeterStatus(Long meterId, Integer meterStatus) {
		Meters meter = new Meters();
		meter.setId(meterId);
		meter.setMeterStatus(meterStatus);
		int rows = metersMapper.updateByPrimaryKeySelective(meter);
		if(rows>0) {
			CustomerMeter record = new CustomerMeter();
			record.setMeterId(meterId);
			record.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			List<CustomerMeter> cmList = customerMeterService.select(record);
			for(CustomerMeter cm : cmList) {
				Long customerId = cm.getCustomerId();
				
				Customers customer = new Customers();
				customer.setId(customerId);
				customer.setWaterStatus(meterStatus);
				rows = customersService.updateByPrimaryKeySelective(customer);
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
	
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getMeterListByLocationId
	 * @Description: 根据地位置位置查找表计
	 * @param locationId
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.meters.MetersService#getMeterListByLocationId(java.lang.Long, java.lang.String)
	 */
	@Override
	public List<Meters> getMeterListByLocationId(Long locationId, String searchCond) {
		return metersMapper.getMeterListByLocationId(locationId, searchCond);
	}


	@Override
	public List<Map<String, Object>> getBindMeterList(Long meterId) {
		return metersMapper.getBindMeterList(meterId);
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: transferMeter
	 * @Description: 换表
	 * @param oldMeterId
	 * @param newMeterId
	 * @return 
	 * @see com.learnbind.ai.service.meters.MetersService#transferMeter(java.lang.Long, java.lang.Long)
	 */
	@Override
	public int transferMeter(Long oldMeterId, Long newMeterId) {
		int rows = 0;
		
		
		//处理客户-表计关系表
		CustomerMeter cm = new CustomerMeter();
		cm.setMeterId(oldMeterId);
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
			cmTemp.setMeterId(newMeterId);
			rows = customerMeterService.insertSelective(cmTemp);
			if(rows<=0) {
				break;
			}
		}
		
		//TODO 地理位置-表计关系表	
		LocationMeter lm = new LocationMeter();
		lm.setMeterId(oldMeterId);
		List<LocationMeter> lmList = locationMeterService.select(lm);
		for(LocationMeter lmTemp : lmList) {
			
			lm = new LocationMeter();
			lm.setId(lmTemp.getId());
			lm.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = locationMeterService.updateByPrimaryKeySelective(lm);
			if(rows<=0) {
				break;
			}
			lmTemp.setId(null);
			lmTemp.setMeterId(newMeterId);
			rows = locationMeterService.insertSelective(lmTemp);
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
	 * (非 Javadoc)
	 * 
	 * @Title: destoryMeters
	 * @Description: 销户拆表
	 * @param meterId
	 * @return 
	 * @see com.learnbind.ai.service.meters.MetersService#destoryMeters(long)
	 */
	@Override
	public int destoryMeters(long meterId) {
		int rows = 0;
		//解除表计-客户绑定关系
		CustomerMeter cm = new CustomerMeter();
		cm.setMeterId(meterId);
		List<CustomerMeter> cmList = customerMeterService.select(cm);
		for(CustomerMeter cmtemp : cmList) {
			cmtemp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = customerMeterService.updateByPrimaryKeySelective(cmtemp);
			if(rows<=0) {
				break;
			}
		}
		
		//解除表计-客户绑定关系
		LocationMeter lm = new LocationMeter();
		lm.setMeterId(meterId);
		List<LocationMeter> lmList = locationMeterService.select(lm);
		for(LocationMeter lmtemp : lmList) {
			lmtemp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = locationMeterService.updateByPrimaryKeySelective(lmtemp);
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
	 * (非 Javadoc)
	 * 
	 * @Title: updateDestoryStatus
	 * @Description:  修改表计状态（暂拆/复装）
	 * @param meterId
	 * @param status
	 * @return 
	 * @see com.learnbind.ai.service.meters.MetersService#updateDestoryStatus(java.lang.Long, java.lang.Integer)
	 */
	@Override
	public int updateRemoveStatus(Long meterId, Integer status) {
		Meters meter = new Meters();
		meter.setId(meterId);
		meter.setStatus(status);
		int rows = metersMapper.updateByPrimaryKeySelective(meter);
		if(rows>0) {
			//修改表计-客户绑定关系表表计状态
			CustomerMeter cm = new CustomerMeter();
			cm.setMeterId(meterId);
			List<CustomerMeter> cmList = customerMeterService.select(cm);
			for(CustomerMeter cmtemp : cmList) {
				cmtemp.setMeterStatus(status);
				rows = customerMeterService.updateByPrimaryKeySelective(cmtemp);
				if(rows<=0) {
					break;
				}
			}
			
			//修改表计-地理位置绑定关系表计状态
			LocationMeter lm = new LocationMeter();
			lm.setMeterId(meterId);
			List<LocationMeter> lmList = locationMeterService.select(lm);
			for(LocationMeter lmtemp : lmList) {
				lmtemp.setMeterStatus(status);;
				rows = locationMeterService.updateByPrimaryKeySelective(lmtemp);
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

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: searchCheckMeterRemindMeters
	 * @Description:查询需要检测的表计
	 * @param searchCond
	 * @return 
	 * @see com.learnbind.ai.service.meters.MetersService#searchCheckMeterRemindMeters(java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> searchCheckMeterRemindMeters(String searchCond, String caliber, List<Integer> cycleStatusList, Integer checkMonth) {
		return metersMapper.searchCheckMeterRemindMeters(searchCond, caliber, cycleStatusList, checkMonth);
	}

	@Override
	public List<Map<String, Object>> getCheckMeterRemindList(String searchCond, String traceIds, String caliber, List<Integer> cycleStatusList, Integer checkMonth) {
		return metersMapper.getCheckMeterRemindList(searchCond, traceIds, caliber, cycleStatusList, checkMonth);
	}

	@Override
	public int destoryMeter(List<CustomerMeter> cmList) {
		int rows = 1;
		for(CustomerMeter cm : cmList) {
			Long id = cm.getId();
			//Long customerId = cm.getCustomerId();
			Long meterId = cm.getMeterId();
			
			//解除客户-表计绑定关系
			CustomerMeter record = new CustomerMeter();
			record.setId(id);
			record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = customerMeterService.updateByPrimaryKeySelective(record);
			if(rows>0) {
//				//删除解除地理位置-客户绑定关系
//				Example example = new Example(LocationCustomer.class);
//				example.createCriteria().andEqualTo("customerId", customerId);
//				LocationCustomer lc = new LocationCustomer();
//				lc.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
//				locationCustomerService.updateByExampleSelective(lc, example);
				//删除解除地理位置-表计绑定关系
				Example example = new Example(LocationMeter.class);
				example.createCriteria().andEqualTo("meterId", meterId);
				LocationMeter lm = new LocationMeter();
				lm.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				locationMeterService.updateByExampleSelective(lm, example);
			}else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			
		}
		return rows;
	}

	@Override
	@Transactional
	public int changeMeter(Long oldMeterId, String oldMeterRead, Long newMeterId, String newMeterRead) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			Date sysDate = new Date();//系统时间
			
			int rows = -1;
			
			//（1）表计-地理位置关系
			this.changeMeterLocaitonMeter(oldMeterId, newMeterId, sysDate);//换表：处理地理位置-表计关系
			//（2）表计-客户关系
			rows = this.changeMeterCustomerMeter(oldMeterId, newMeterId, sysDate);//换表：处理客户-表计关系
			if(rows>0) {
				//（3）分水量规则
				this.changeMeterPartWater(oldMeterId, newMeterId);//换表：处理分水量规则
				//（4）用水性质
				this.changeMeterWaterUse(oldMeterId, newMeterId);//换表：处理用水性质
				//（5）表计状态
				this.changeMeterCycleStatus(oldMeterId, newMeterId);//换表：处理状态
				//（6）表册
				this.changeMeterMeterBook(oldMeterId, newMeterId);//换表：处理更新表册
				//（7）增加旧表抄表记录和新表抄表记录
				//CustomerMeter cm = customerMeterService.getCustomerByMeterId(oldMeterId);//查询客户-表计关系
				CustomerMeter cm = customerMeterService.getCustomerByMeterId(newMeterId);//查询客户-表计关系
				if(cm!=null) {
					
					//String oldMeterUse = meterRecordService.getMeterUse(oldMeterId);//旧表表计用途
					//String newMeterUse = meterRecordService.getMeterUse(newMeterId);//新表表计用途
					//Long oldMeterTreeId = meterRecordService.getMeterTreeId(oldMeterId);//旧表表计父子关系ID
					//Long newMeterTreeId = meterRecordService.getMeterTreeId(oldMeterId);//新表表计父子关系ID
					
					//查询旧表最后一条抄表记录
					//MeterRecord record = meterRecordService.getLastMeterRecord(cm.getCustomerId(), null, oldMeterId);
					//增加旧表抄表记录
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
					String period = formatter.format(sysDate);	
					MeterRecord oldRecord = new MeterRecord();
					oldRecord.setPeriod(period);
					oldRecord.setCustomerId(cm.getCustomerId());
					oldRecord.setMeterId(oldMeterId);
					//oldRecord.setCurrDate(sysDate);
					oldRecord.setCurrRead(oldMeterRead.toString());
					//oldRecord.setMeterUse(oldMeterUse);
					//oldRecord.setMeterTreeId(oldMeterTreeId);
					oldRecord.setReadType(EnumReadType.INIT_READ.getValue());
					oldRecord.setReadMode(EnumReadMode.READ_MANUAL.getCode());
					oldRecord.setStatus(EnumMeterRecordStatus.MANUAL_EDIT.getValue());
					oldRecord.setIsAddSubWater(EnumAddSubWaterStatus.ADD_SUB_WATER_NO.getValue());
					oldRecord.setIsPartWater(EnumPartitionWaterStatus.PARTITION_NO.getValue());
					oldRecord.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
					oldRecord.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
					rows = meterRecordService.insertMeterRecord(oldRecord, userBean.getId(), userBean.getRealname());
					
					Meters newMeter = metersMapper.selectByPrimaryKey(newMeterId);
					//增加新表抄表记录
					MeterRecord newRecord = new MeterRecord();
					newRecord.setPeriod(period);
					newRecord.setCustomerId(cm.getCustomerId());
					newRecord.setMeterId(newMeterId);
					//newRecord.setCurrDate(sysDate);
					newRecord.setCurrRead(newMeterRead.toString());
					newRecord.setPreDate(sysDate);
					newRecord.setPreRead(newMeter.getNewMeterBottom());
					//newRecord.setMeterUse(newMeterUse);
					//newRecord.setMeterTreeId(newMeterTreeId);
					newRecord.setReadType(EnumReadType.INIT_READ.getValue());
					newRecord.setReadMode(EnumReadMode.READ_MANUAL.getCode());
					newRecord.setStatus(EnumMeterRecordStatus.MANUAL_EDIT.getValue());
					newRecord.setIsAddSubWater(EnumAddSubWaterStatus.ADD_SUB_WATER_NO.getValue());
					newRecord.setIsPartWater(EnumPartitionWaterStatus.PARTITION_NO.getValue());
					newRecord.setIsMakeBill(EnumMakeBillStatus.MAKE_BILL_NO.getValue());
					newRecord.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
					rows = meterRecordService.insertMeterRecord(newRecord, userBean.getId(), userBean.getRealname());
					//Meters newMeter = metersMapper.selectByPrimaryKey(newMeterId);
					//rows = meterRecordService.insertInitMeterRecord(newMeterId, cm.getCustomerId(), userBean.getId(), userBean.getRealname(), newMeterRead);
				}
			}
			
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: changeMeterLocaitonMeter
	 * @Description: 换表：处理地理位置-表计关系
	 * @param oldMeterId
	 * @param newMeterId
	 * @param sysDate
	 * @return 
	 */
	@Transactional
	private int changeMeterLocaitonMeter(Long oldMeterId, Long newMeterId, Date sysDate) {
		try {
			int rows = -1;
			//查询地理位置-表计关系
			List<Long> meterIdList = new ArrayList<>();
			meterIdList.add(oldMeterId);
			List<LocationMeter> lmList = locationMeterService.getListByMeterIdList(meterIdList);
			if(lmList!=null && lmList.size()>0) {
				LocationMeter lm = lmList.get(0);
				//解除原表计与地理位置的关系
				meterIdList = new ArrayList<>();
				if(oldMeterId!=null) {
					meterIdList.add(oldMeterId);
				}
				meterIdList.add(newMeterId);
				Example example = new Example(LocationMeter.class);
				example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andIn("meterId", meterIdList);
				//example.createCriteria().andEqualTo("deleted", EnumMeterDeletedStatus.DELETE_NO.getValue()).andIn("meterId", meterIdList);
				
				LocationMeter temp = new LocationMeter();
				temp.setUnbindTime(sysDate);
				//temp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				temp.setDeleted(2);//deleted=2表示换表
				rows = locationMeterService.updateByExampleSelective(temp, example);
				if(rows>0) {
					//绑定新表计与地理位置的关系
					lm.setId(null);
					lm.setBindTime(sysDate);
					lm.setMeterId(newMeterId);
					locationMeterService.insertSelective(lm);
					return rows;
				}
				
			}else {
				return rows;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: changeMeterCustomerMeter
	 * @Description: 换表：处理客户-表计关系
	 * @param oldMeterId
	 * @param newMeterId
	 * @param sysDate
	 * @return 
	 */
	@Transactional
	private int changeMeterCustomerMeter(Long oldMeterId, Long newMeterId, Date sysDate) {
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			int rows = -1;
			//查询客户-表计关系
			List<CustomerMeter> cmList = customerMeterService.getListByMeterId(oldMeterId);
			if(cmList==null || cmList.size()<=0) {
				return rows;
			}
			
			List<Long> meterIdList = new ArrayList<>();
			if(oldMeterId!=null) {
				meterIdList.add(oldMeterId);
			}
			meterIdList.add(newMeterId);
			Example example = new Example(LocationMeter.class);
			example.createCriteria().andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue()).andIn("meterId", meterIdList);
			//example.createCriteria().andEqualTo("deleted", EnumMeterDeletedStatus.DELETE_NO.getValue()).andIn("meterId", meterIdList);
			//解除客户与原表计的关系
			CustomerMeter temp = new CustomerMeter();
			temp.setUnbindTime(sysDate);
			temp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			//temp.setDeleted(EnumMeterDeletedStatus.DELETE_CHANGE_METER.getValue());
			rows = customerMeterService.updateByExampleSelective(temp, example);
			for(CustomerMeter cm : cmList) {
				Long cmId = cm.getId();
				//建立客户与新表计的关系
				cm.setId(null);
				cm.setBindTime(sysDate);
				cm.setMeterId(newMeterId);
				rows = customerMeterService.insertSelective(cm);
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
				//查询新表的表底读数 TODO 
				//MeterRecord meterRecord = meterRecordService.selectNewestMeterRecord(cm.getCustomerId(), newMeterId);
				//Meters newMeter = metersMapper.selectByPrimaryKey(newMeterId);
				//rows = meterRecordService.insertInitMeterRecord(newMeterId, temp.getCustomerId(), userBean.getId(), userBean.getRealname(), newMeter.getNewMeterBottom());
				
				
			}
			return rows;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}
	
	/**
	 * @Title: changeMeterPartWater
	 * @Description: 换表：处理分水量规则部分，把旧表上的规则移动到新表上
	 * @param oldMeterId
	 * @param newMeterId 
	 */
	private void changeMeterPartWater(Long oldMeterId, Long newMeterId) {
		
		String prefix = PartitionRuleUtil.METER_PARAM_PREFIX;//分水量规则中表计ID前缀
		String oldMeterIdRule = prefix+oldMeterId;
		String newMeterIdRule = prefix+newMeterId;
		
		//把规则中包含旧表的所有规则替换成新表，如：旧表ID是63033，新表ID是63039，规则是meter_63033,-,meter_63034，替换后的规则是meter_63039,-,meter_63034
		List<MeterPartWaterRule> ruleList = meterPartWaterRuleService.getListByRuleReal(oldMeterIdRule);
		for(MeterPartWaterRule rule : ruleList) {
			String ruleReal = rule.getRuleReal();
			ruleReal = ruleReal.replace(oldMeterIdRule, newMeterIdRule);
			rule.setRuleReal(ruleReal);
			meterPartWaterRuleService.updateByPrimaryKeySelective(rule);
		}
		
		//旧表分水量规则
		List<MeterPartWaterRule> meterRuleList = meterPartWaterRuleService.getMeterRule(oldMeterId);
		for(MeterPartWaterRule rule : meterRuleList) {
			rule.setMeterId(newMeterId);
			meterPartWaterRuleService.updateByPrimaryKeySelective(rule);
		}
		
	}
	
	/**
	 * @Title: changeMeterWaterUse
	 * @Description: 换表：处理用水性质部分，把旧表的用水性质拷贝到新表上
	 * @param oldMeterId
	 * @param newMeterId
	 * @return 
	 */
	private int changeMeterWaterUse(Long oldMeterId, Long newMeterId) {
		Meters oldMeter = metersMapper.selectByPrimaryKey(oldMeterId);
		Meters newMeter = metersMapper.selectByPrimaryKey(newMeterId);
		newMeter.setWaterUse(oldMeter.getWaterUse());
		newMeter.setPriceCode(oldMeter.getPriceCode());
		newMeter.setPlace(oldMeter.getPlace());
		newMeter.setDescription(oldMeter.getDescription());
		return metersMapper.updateByPrimaryKeySelective(newMeter);
	}
	
	/**
	 * @Title: changeMeterCycleStatus
	 * @Description: 换表：处理更新表计周期状态
	 * @param oldMeterId
	 * @param newMeterId 
	 */
	private void changeMeterCycleStatus(Long oldMeterId, Long newMeterId) {
		//设置旧表状态
		Meters oldMeter = new Meters();
		oldMeter.setId(oldMeterId);
		oldMeter.setCycleStatus(EnumMeterCycleStatus.NO_DETECTION.getValue());//换表时，旧表状态设置为待用
		metersMapper.updateByPrimaryKeySelective(oldMeter);
		
		//设置新表状态
		Meters newMeter = new Meters();
		newMeter.setId(newMeterId);
		newMeter.setCycleStatus(EnumMeterCycleStatus.ENABLED.getValue());//换表时，新表状态设置为待启用
		metersMapper.updateByPrimaryKeySelective(newMeter);
	}
	/**
	 * @Title: changeMeterMeterBook
	 * @Description: 换表：处理更新表册
	 * @param oldMeterId
	 * @param newMeterId 
	 */
	private void changeMeterMeterBook(Long oldMeterId, Long newMeterId) {
		//查询表册-表计关系
		MeterBookMeter searchObj = new MeterBookMeter();
		searchObj.setMeterId(oldMeterId);
		List<MeterBookMeter> bookList = meterBookMeterService.select(searchObj);
		for(MeterBookMeter book : bookList) {
			book.setMeterId(newMeterId);
			meterBookMeterService.updateByPrimaryKeySelective(book);//更新表册-表计，把旧表的表册-表计关系，调整为新表；没有备份，只是把原旧表-表册关系中的表计ID重置为新表ID
		}
	}
	
	@Override
	public List<MeterBean> getChildListById(Long id) {
		return metersMapper.getChildListById(id);
	}

	@Override
	public Meters getMeterMessage(String steelSealNo) {
		return metersMapper.getMeterMessage(steelSealNo);
	}

	@Override
	public List<Meters> getBindBigMeterList(String searchCond, String traceIds, String meterUse, Long customerId) {
		return metersMapper.getBindBigMeterList(searchCond, traceIds, meterUse, customerId);
	}

	@Override
	public List<Meters> getUnBindBigMeterList(String searchCond, String traceIds, String meterUse, Long customerId) {
		return metersMapper.getUnBindBigMeterList(searchCond, traceIds, meterUse, customerId);
	}

	@Override
	public int updateCycleStatus(Long meterId, Integer cycleStatus) {
		int rows = 1;
		Meters meter = this.selectByPrimaryKey(meterId);
		meter.setCycleStatus(cycleStatus);
		rows = this.updateByPrimaryKeySelective(meter);
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}

	@Override
	public List<Map<String, Object>> getMeterBindCustomerList(String searchCond, String traceIds, Integer searchType) {
		return metersMapper.getMeterBindCustomerList(searchCond, traceIds, searchType);
	}

	@Override
	public List<Map<String, Object>> getMetersBindCustomersList(String searchCond, String traceIds) {
		return metersMapper.getMetersBindCustomersList(searchCond, traceIds);
	}

	@Override
	public List<Map<String, Object>> getExportMeterData(String searchCond, String traceIds) {
		return metersMapper.getExportMeterData(searchCond, traceIds);
	}

	@Override
	public List<Meters> getMeterListByTraceIds(String traceIds, String searchCond) {
		return metersMapper.getMeterListByTraceIds(traceIds, searchCond);
	}

	@Override
	public List<Meters> getMeterData(String traceIds) {
		return metersMapper.getMeterData(traceIds);
	}

	@Override
	public List<Meters> getVirtualMeterList(String traceIds) {
		return metersMapper.getVirtualMeterList(traceIds);
	}

	@Override
	public List<Meters> getRealMeterList(String searchCond, String traceIds, Integer virtualRealStatus) {
		return metersMapper.getRealMeterList(searchCond, traceIds, virtualRealStatus);
	}

	@Override
	public List<Meters> searchRealMeters(String searchCond, Integer virtualRealStatus) {
		return metersMapper.searchRealMeters(searchCond, virtualRealStatus);
	}

	@Override
	public List<Meters> getMeterListByCycleStatus(String searchCond, String traceIds, List<Integer> cycleStatusList) {
		return metersMapper.getMeterListByCycleStatus(searchCond, traceIds, cycleStatusList);
	}

	@Override
	public List<Meters> searchMetersByCycleStatus(String searchCond, List<Integer> cycleStatusList) {
		return metersMapper.searchMetersByCycleStatus(searchCond, cycleStatusList);
	}

	@Override
	public List<Meters> getMeterDocList(String searchCond, String traceIds, Integer docType, List<Integer> cycleStatusList) {
		return metersMapper.getMeterDocList(searchCond, traceIds, docType, cycleStatusList);
	}

	@Override
	public List<Meters> searchMeterDocList(String searchCond, Integer docType, List<Integer> cycleStatusList) {
		return metersMapper.searchMeterDocList(searchCond, docType, cycleStatusList);
	}

	@Override
	public List<Meters> getDefaultMeterList(String traceIds, String searchCond, Integer customerType, String waterPrice) {
		return metersMapper.getDefaultMeterList(traceIds, searchCond, customerType, waterPrice);
	}
	

	@Override
	public List<Map<String, Object>> getStockMeterByFactory(Integer meterVirtualReal, Integer cycleStatus) {
		return metersMapper.getStockMeterByFactory(meterVirtualReal, cycleStatus);
	}
	
	public static void main(String[] args) {
		String newIdRule = "meter_63034";
		String rule = "meter_63033,-,meter_63034,+,meter_63034";
		System.out.println(rule);
		rule = rule.replace(newIdRule, "meter_63035");
		System.out.println(rule);
	}
	
}
