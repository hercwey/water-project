package com.learnbind.ai.service.meterbook.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.common.enumclass.EnumMeterBookGenerateStatus;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.dao.MeterBookMapper;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterBook;
import com.learnbind.ai.model.MeterBookMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.UserMeterBook;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meterbook.UserMeterBookService;
import com.learnbind.ai.service.meters.MetersService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.meterbook.impl
 *
 * @Title: MeterBookServiceImpl.java
 * @Description: 表册服务实现类
 *
 * @author Administrator
 * @date 2019年6月10日 下午7:33:41
 * @version V1.0 
 *
 */
@Service
public class MeterBookServiceImpl extends AbstractBaseService<MeterBook, Long> implements  MeterBookService {
	
	/**
	 * @Fields LOCATION_BLOCK：根据小区获取客户-表计关系
	 */
	private static final String LOCATION_BLOCK = "LOCATION_BLOCK";
	/**
	 * @Fields LOCATION_ROOM：根据房间号获取客户-表计关系
	 */
	private static final String LOCATION_ROOM = "LOCATION_ROOM";
	
	
	@Autowired
	public MeterBookMapper meterBookMapper;
	@Autowired
	public MeterBookMeterService meterBookMeterService;//表册-表计关系服务
	@Autowired
	public CustomerMeterService customerMeterService;//客户-表计关系服务
	@Autowired
	public MetersService metersService;//表计关系服务
	@Autowired
	private LocationService locationService;//地理位置
	@Autowired
	private LocationMeterService locationMeterService;//地理位置-表计关系
	@Autowired
	private UserMeterBookService userMeterBookervice;//客户-表册关系位置
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterBookServiceImpl(MeterBookMapper mapper) {
		this.meterBookMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<MeterBook> getListByCondition(String searchCond) {
		return meterBookMapper.getListByCondition(searchCond);
	}
	
	@Override
	public List<MeterBook> getUnAllotBookListByCondition(String searchCond, String traceIds, Long readerId) {
		return meterBookMapper.getUnAllotBookListByCondition(searchCond, traceIds, readerId);
	}
	
	@Override
	public List<MeterBook> getReaderBookListByCondition(Long userId, String searchCond) {
		return meterBookMapper.getReaderBookListByCondition(userId, searchCond);
	}
	
	@Override
	public List<MeterBook> getMyMeterBookList(Long userId, String searchCond, String traceIds) {
		return meterBookMapper.getMyMeterBookList(userId, searchCond, traceIds);
	}
	
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: createMeterBookByBlockId
	 * @Description: 通过小区创建表册
	 * @param operatorId
	 * @param operatorName
	 * @param typeCode
	 * @param typeName
	 * @param locationBlockId
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.MeterBookService#createMeterBookByBlockId(java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public int createMeterBookByBlockId(Long operatorId, String operatorName, String typeCode, String typeName, Long locationBlockId) {
		
		Location location = locationService.selectByPrimaryKey(locationBlockId);//查询地理位置-小区
		
		//创建小区表册
		this.createBlockMeterBook(operatorId, operatorName, typeCode, typeName, locationBlockId, location.getName(), location.getCode());
		
		List<Location> locationList = locationService.getBuildingListByPid(locationBlockId);
		
		int rows = 0;
		
		for(Location tempLocation : locationList) {
			String code = location.getCode();
			String name = location.getName();
			rows = this.createMeterBookByBuildingId(operatorId, operatorName, typeCode, typeName, tempLocation.getId(), name, code);
		}
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: createMeterBookByBuildingId
	 * @Description: 通过楼号创建表册
	 * @param operatorId
	 * @param operatorName
	 * @param typeCode
	 * @param typeName
	 * @param locationBuildingId
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.MeterBookService#createMeterBookByBuildingId(java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public int createMeterBookByBuildingId(Long operatorId, String operatorName, String typeCode, String typeName, Long locationBuildingId, String name, String code) {
		
		Location location = locationService.selectByPrimaryKey(locationBuildingId);//查询地理位置-楼号
		
		List<Location> locationList = locationService.getUnitListByPid(locationBuildingId);//查询某楼号下的所有单元的地理位置
		
		int rows = 0;
		for(Location tempLocation : locationList) {
			String codeNew = code+"-"+location.getCode();
			String nameNew = name+"-"+location.getName();
			//rows = this.createMeterBookByUnitId(operatorId, operatorName, typeCode, typeName, tempLocation.getId(), nameNew, codeNew);
		}
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: createMeterBookByUnitId
	 * @Description: 通过单元id创建表册
	 * @param operatorId
	 * @param operatorName
	 * @param typeCode
	 * @param typeName
	 * @param locationUnitId
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.MeterBookService#createMeterBookByUnitId(java.lang.Long, java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public int createMeterBookByUnitId(Long operatorId, String operatorName, List<Location> locationList, String blockName) {
		
		
		
		int rows=1;
		for(Location location : locationList) {
			Long locationUnitId = location.getId();
			String pycode = location.getPycode();
			
			//获取客户-表计关系集合
			List<CustomerMeter> customerMeterList = this.getCustomerMeterList(locationUnitId, LOCATION_ROOM);
			if(customerMeterList==null || customerMeterList.size()<=0) {
				//未查询到绑定关系，不需要创建表册
				//return 1;
				continue;
			}
			//获取小区-楼号-单元名称
			String traceIds = location.getTraceIds();
			String[] traceIdArr = traceIds.split("-");
			String name = locationService.getPlaceNotRoom(traceIds);
			//获取小区信息
			Location locationBlock = this.getLocationBlock(traceIdArr);
			
			Date sysDate = new Date();
			MeterBook meterBook = new MeterBook();
			meterBook.setCode(pycode);
			meterBook.setName(name);
			
			//表册typeCode及typeName为小区简拼及名称
			meterBook.setTypeCode(locationBlock.getPycode());
			meterBook.setTypeName(locationBlock.getName());
			
			meterBook.setGenerateStatus(EnumMeterBookGenerateStatus.AUTO.getValue());
			meterBook.setTraceIds(location.getTraceIds());
			//判断该表册是否已存在
			List<MeterBook> mkList = meterBookMapper.select(meterBook);
			if(mkList!=null && mkList.size()>0) {
				//return 1;
				continue;
			}
			
			meterBook.setOperatorId(operatorId);
			meterBook.setOperatorName(operatorName);
			meterBook.setCreateTime(sysDate);
			
			meterBook.setModifiedDate(sysDate);
			rows = this.insertMeterBook(meterBook, customerMeterList);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
		
	}
	
	/**
	 * @Title: getLocationBlock
	 * @Description: 获取小区地理位置
	 * @param traceIdArr
	 * @return 
	 */
	private Location getLocationBlock(String[] traceIdArr) {
		
		final String BLOCK = "BLOCK";
		List<Location> locationBlockList = locationService.getLocation(BLOCK, traceIdArr);
		if(locationBlockList!=null && locationBlockList.size()>0) {
			return locationBlockList.get(0);
		}
		return null;
	}
	
	/**
	 * @Title: createBlockMeterBook
	 * @Description: 创建小区表册
	 * @param operatorId
	 * @param operatorName
	 * @param typeCode
	 * @param typeName
	 * @param locationBlockId
	 * @param name
	 * @param code
	 * @return 
	 */
	private int createBlockMeterBook(Long operatorId, String operatorName, String typeCode, String typeName, Long locationBlockId, String name, String code) {
		
		String codeNew = code+"-0-0";
		String nameNew = name+"-0-0";
		
		//获取客户-表计关系集合
		List<CustomerMeter> customerMeterList = this.getCustomerMeterList(locationBlockId, LOCATION_BLOCK);
		if(customerMeterList==null || customerMeterList.size()<=0) {
			//未查询到绑定关系，不需要创建表册
			return 1;
		}
		
		Date sysDate = new Date();
		MeterBook meterBook = new MeterBook();
		meterBook.setTypeCode(typeCode);
		meterBook.setTypeName(typeName);
		
		meterBook.setCode(codeNew);
		meterBook.setName(nameNew);
		
		meterBook.setOperatorId(operatorId);
		meterBook.setOperatorName(operatorName);
		meterBook.setCreateTime(sysDate);
		meterBook.setModifiedDate(sysDate);
		
		return this.insertMeterBook(meterBook, customerMeterList);
	}
	
	/**
	 * @Title: getCustomerMeterList
	 * @Description: 获取客户-表计关系集合
	 * @param locationId
	 * @param locationNodeType 
	 */
	private List<CustomerMeter> getCustomerMeterList(Long locationId, String locationNodeType) {
		
		List<CustomerMeter> customerMeterList = null;
		
		switch (locationNodeType) {
		case LOCATION_BLOCK:
			
			customerMeterList = new ArrayList<>();
			
			List<Long> meterIdList = locationMeterService.getMeterIdListByLocationId(locationId);//根据地理位置ID查询表计ID集合
			for(Long meterId : meterIdList) {
				CustomerMeter cm = new CustomerMeter();
				cm.setMeterId(meterId);
				customerMeterList.add(cm);
			}
			
			break;
		case LOCATION_ROOM:
			
			List<Long> locationIdList = new ArrayList<>();
			List<Location> locationList = locationService.getRoomListByPid(locationId);//查询某单元下的所有房间号地理位置
			if(locationList==null || locationList.size()<=0) {
				break;
			}
			for(Location tempLocation : locationList) {
				locationIdList.add(tempLocation.getId());
			}
			
			//查询客户-表计
			customerMeterList = customerMeterService.getListByLocationIdList(locationIdList);
			
			break;
		default:
			break;
		}
		return customerMeterList;
	}
	
	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: insertMeterBook
	 * @Description: 添加表册表数据同时在meter_book_meter添加数据
	 * @param meterBook
	 * @param customerMeterList
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.MeterBookService#insertMeterBook(com.learnbind.ai.model.MeterBook, java.util.List)
	 */
	@Override
	@Transactional
	public int insertMeterBook(MeterBook meterBook, List<CustomerMeter> customerMeterList) {
		
		String factory = this.getMeterFactory(customerMeterList);//获取表计生产厂家
		String readMode = this.getMeterReadMode(customerMeterList);//获取表计抄表方式
		meterBook.setFactory(factory);
		meterBook.setReadMode(readMode);
		
		int rows = meterBookMapper.insertSelective(meterBook);
		if(rows>0) {
			//Long meterBookId = this.getMeterBookId(meterBook.getCode());//表册ID
			Long meterBookId = meterBook.getId();
			
			for(CustomerMeter customerMeter : customerMeterList) {
				MeterBookMeter mbm = new MeterBookMeter();
				Meters m = metersService.selectByPrimaryKey(customerMeter.getMeterId());
				//虚表不添加到表册当中
				if(m.getVirtualReal() == EnumMeterVirtualReal.REAL_METER.getValue()) {
					mbm.setMeterBookId(meterBookId);
					mbm.setCustomerId(customerMeter.getCustomerId());
					mbm.setMeterId(customerMeter.getMeterId());
					rows = meterBookMeterService.insertSelective(mbm);
				}
				
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
		}
		return rows;
	}
	
	/**
	 * @Title: getMeterFactory
	 * @Description: 获取生产厂家
	 * @param customerMeterList
	 * @return 
	 */
	private String getMeterFactory(List<CustomerMeter> customerMeterList) {
		if(customerMeterList!=null && customerMeterList.size()>0) {
			List<Long> cmIdList = new ArrayList<>();
			for(CustomerMeter cm : customerMeterList) {
				cmIdList.add(cm.getId());
			}
			return customerMeterService.getMeterFactory(cmIdList);
		}
		return null;
	}
	/**
	 * @Title: getMeterReadMode
	 * @Description: 获取表计抄表方式
	 * @param customerMeterList
	 * @return 
	 */
	private String getMeterReadMode(List<CustomerMeter> customerMeterList) {
		if(customerMeterList!=null && customerMeterList.size()>0) {
			List<Long> cmIdList = new ArrayList<>();
			for(CustomerMeter cm : customerMeterList) {
				cmIdList.add(cm.getId());
			}
			return customerMeterService.getMeterReadMode(cmIdList);
		}
		return null;
	}
	
	/**
	 * @Title: getMeterBookId
	 * @Description: 根据表册编码获取表册ID
	 * @param code
	 * @return 
	 */
	private Long getMeterBookId(String code) {
		MeterBook record = new MeterBook();
		record.setCode(code);
		MeterBook meterBook = meterBookMapper.selectOne(record);
		if(meterBook!=null) {
			return meterBook.getId();
		}
		return 0l;
	}

	@Override
	public int deleteMeterBook(Long meterBookId) {
		int rows = meterBookMapper.deleteByPrimaryKey(meterBookId);//删除表册
		if(rows>0) {
			MeterBookMeter mbm = new MeterBookMeter();
			mbm.setMeterBookId(meterBookId);
			meterBookMeterService.delete(mbm);
			UserMeterBook umb = new UserMeterBook();
			umb.setMeterBookId(meterBookId);
			userMeterBookervice.delete(umb);
		}
		
		return rows;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getMeterBookTypeNameList
	 * @Description: 获取表册小区列表
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.MeterBookService#getMeterBookTypeNameList()
	 */
	@Override
	public List<MeterBook> getMeterBookTypeNameList() {
		return meterBookMapper.getMeterBookTypeNameList();
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: getMeterBookIdByName
	 * @Description:根据表名称查询表册id
	 * @param name
	 * @return 
	 * @see com.learnbind.ai.service.meterbook.MeterBookService#getMeterBookIdByName(java.lang.String)
	 */
	@Override
	public List<MeterBook> getMeterBookIdByCode(String code) {
		return meterBookMapper.getMeterBookIdByCode(code);
	}

	@Override
	public List<MeterBook> getMeterBookByTraceIdsAndSearchCond(String traceIds, String searchCond, Integer generateStatus) {
		return meterBookMapper.getMeterBookByTraceIdsAndSearchCond(traceIds, searchCond, generateStatus);
	}
	
	@Override
	public List<MeterBook> getAllotBookListByCondition(Long userId, String traceIds,  String searchCond) {
		return meterBookMapper.getAllotBookListByCondition(userId, traceIds, searchCond);
	}

}
