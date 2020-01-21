package com.learnbind.ai.service.location.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.LocationBean;
import com.learnbind.ai.common.enumclass.EnumDefaultStatus;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumLocalNodeType;
import com.learnbind.ai.dao.LocationMapper;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.LocationCustomer;
import com.learnbind.ai.model.LocationEngineering;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.BillService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerAgreementService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomerPeopleAdjustService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.customers.CustomersTraceService;
import com.learnbind.ai.service.customers.PledgeService;
import com.learnbind.ai.service.engineering.LocationEngineeringService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterbook.MeterBookMeterService;
import com.learnbind.ai.service.meterbook.MeterBookService;
import com.learnbind.ai.service.meterbook.UserMeterBookService;
import com.learnbind.ai.service.meterrecord.CustomerOverdueFineService;
import com.learnbind.ai.service.meterrecord.MeterRecordPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempPhotoService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.meters.MeterTreeService;
import com.learnbind.ai.util.pinyin4j.Pinyin4jUtils;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.location.impl
 *
 * @Title: LocationServiceImpl.java
 * @Description: 地理位置服务实现类
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:04:04
 * @version V1.0
 *
 */
@Service
public class LocationServiceImpl extends AbstractBaseService<Location, Long> implements LocationService {

	public LocationMapper locationMapper;
	@Autowired
	private CustomersService customersService;  //客户档案
	@Autowired
	public CustomerMeterService customerMeterService;//客户-表计关系
	@Autowired
	public BankService bankService;//客户-银行信息；
	@Autowired
	public BillService billService;//客户-开票信息；
	@Autowired
	public PledgeService pledgeService;//客户-押金信息；
	@Autowired
	public CustomerPeopleAdjustService peopleAdjustService;//客户-多人口信息；
	@Autowired
	public CustomerAgreementService customerAgreementService;//客户-协议信息；
	@Autowired
	public CustomerOverdueFineService customerOverdueFineService;//客户-违约金信息；
	@Autowired
	public CustomerAccountService customerAccountService;//客户-账户信息；
	@Autowired
	public CustomersTraceService customersTraceService;//客户-日志信息；
	@Autowired
	public CustomerAccountItemService customerAccountItemService;//客户-账单信息；
	@Autowired
	public LocationCustomerService locationCustomerService;//地理位置-客户关系
	@Autowired
	public LocationMeterService locationMeterService;//地理位置-表计关系
	@Autowired
	private LocationEngineeringService locationEngineeringService;//地理位置-工程关系

	@Autowired
	private MeterBookMeterService meterBookMeterService;
	@Autowired
	private UserMeterBookService userMeterBookService;
	@Autowired
	private MeterBookService meterBookService;
	@Autowired
	private MeterRecordTempService meterRecordTempService;
	@Autowired
	private MeterRecordTempPhotoService meterRecordTempPhotoService;
	@Autowired
	private MeterRecordPhotoService meterRecordPhotoService;
	@Autowired
	private MeterRecordService meterRecordService;
	@Autowired
	private PartitionWaterService partitionWaterService;
	@Autowired
	private MeterTreeService meterTreeService;
	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public LocationServiceImpl(LocationMapper mapper) {
		this.locationMapper = mapper;
		this.setMapper(mapper);
	}

	
	@Override
	public List<LocationBean> getChildListById(Long id) {
		return locationMapper.getChildListById(id);
	}
	
	@Override
	public List<Location> getListBySearchCond(String searchCond) {
		return locationMapper.getListBySearchCond(searchCond);
	}
	
	@Override
	public Location getFirstBySearchCond(String searchCond) {
		return locationMapper.getFirstBySearchCond(searchCond);
	}
	
	@Override
	public Location getOneBySearchCond(String searchCond, Long id, Integer action) {
		return locationMapper.getOneBySearchCond(searchCond, id, action);
	}
	
	@Override
	@Transactional
	public int updateDropLocation(Long locationId, Long parentLocationId) {
		
		
		Location location = locationMapper.selectByPrimaryKey(locationId);
//		Location parentLocation = locationMapper.selectByPrimaryKey(parentLocationId);
//		String parentTraceIds = parentLocation.getTraceIds();
//		String traceIds = "";
//		if(StringUtils.isNotBlank(parentTraceIds)) {
//			parentTraceIds = parentTraceIds+"-"+location.getId();
//		}
		
		location.setPid(parentLocationId);
		//location.setTraceIds(traceIds);
		//Location parentLocation = locationMapper.selectByPrimaryKey(parentLocationId);
		int rows = this.updateById(location, true);
		if(rows>0) {
			//同步地理位置-客户表中的traceIds
			locationCustomerService.updateTraceIds();
			//同步地理位置-表计表中的traceIds
			locationMeterService.updateTraceIds();
			//TODO 更新表计父子关系（meterTree）
			return rows;
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/** 
	 * (非 Javadoc)
	 * 
	 * @Title: updateById
	 * @Description: 修改当前节点和子节点的地理位置信息（不包含traceIds）
	 * @param location			需要更新的地理位置节点
	 * @param isUpdateTraceIds	是否更新节点中的traceIds
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationService#updateById(com.learnbind.ai.model.Location, boolean)
	 */
	@Override
	@Transactional
	public int updateById(Location location, boolean isUpdateTraceIds) {
		
		
		String currPyCode = "";
		String currPyLongCode = "";
		try {
			//获取拼音简码
			String pyFirstCodeU = Pinyin4jUtils.toPyFirstUppercase(location.getName());
			currPyCode = pyFirstCodeU;
			//获取拼音全码
			String pyCodeU = Pinyin4jUtils.toPyUppercase(location.getName());
			//拼接拼音简码和拼音全码
			currPyLongCode = pyFirstCodeU+"@&&@"+pyCodeU;
			
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		} 
		
		//获取当前拼音编码
		String pyCode = this.getPyCode(location.getPid(), currPyCode);
		location.setPycode(pyCode);
		//获取拼音longCode编码
		//String pyLongCode = this.getPyLongCode(location.getPid(), location.getPycode());
		//location.setPylongcode(pyLongCode);
		//获取地理位置longCode编码
		String longCode = this.getLongCode(location.getPid(), currPyLongCode);
		location.setPylongcode(longCode);
		
		//判断是否更新traceIds
		if(isUpdateTraceIds) {
			//获取当前节点的traceIds
			String traceIds = this.getTraceIds(location.getId(), location.getPid());
			//设置当前节点的traceIds
			location.setTraceIds(traceIds);
		}
		
		int rows = locationMapper.updateByPrimaryKeySelective(location);
		if(rows>0) {
			
			this.updateLocation(location.getId(), isUpdateTraceIds);//更新当前节点下所有子节点的自动编码（拼音码，长拼音码）
			
//			Long locationId = location.getId();
//			Location locationTemp = locationMapper.selectByPrimaryKey(locationId);
//			String traceIds = locationTemp.getTraceIds();
			
//			if(oldEngineeringId==null && engineeringId!=null) {//如果原工程ID为空，新工程ID不为空时，增加地理位置-工程关系
//				this.insertLocationEngineering(locationId, traceIds, engineeringId);
//			}else if(oldEngineeringId!=null && engineeringId==null) {//如果原工程ID不为空，新工程ID为空时，删除地理位置-工程关系
//				this.deleteLocationEngineering(locationId);
//			}else if(oldEngineeringId!=null && engineeringId!=null) {//如果原工程ID不为空，新工程ID不为空时，判断原工程ID是否与新工程ID相等，如果不相等则删除原来的并增加新地理位置-工程关系；如果相等不做处理
//				if(!oldEngineeringId.equals(engineeringId)) {
//					this.deleteLocationEngineering(locationId);
//					this.insertLocationEngineering(locationId, traceIds, engineeringId);
//				}
//			}
		}
		return rows;
	}
	
	/**
	 * @Title: getTraceIds
	 * @Description: 获取当前节点的traceIds
	 * @param locationId
	 * @param parentId
	 * @return 
	 */
	private String getTraceIds(Long locationId, Long parentId) {
		
		if(parentId==null || parentId<=0) {
			return locationId.toString();
		}
		
		//查询父节点的traceIds
		Location parentLocation = locationMapper.selectByPrimaryKey(parentId);
		String parentTraceIds = parentLocation.getTraceIds();
		String traceIds = "";
		if(StringUtils.isNotBlank(parentTraceIds)) {
			traceIds = parentTraceIds+"-"+locationId;
		}
		return traceIds;
	}
	
	/**
	 * @Title: updateLocation
	 * @Description: 更新当前节点下所有子节点的自动编码（code编码，拼音码，长拼音码）
	 * @param id 
	 */
	private int updateLocation(Long id, boolean isUpdateTraceIds) {
		Location record = new Location();
		record.setPid(id);
		List<Location> locationList = locationMapper.select(record);
		if(locationList!=null && locationList.size()>0) {
			int rows = 0;
			for(Location location : locationList) {
				
				String currPyCode = "";
				String currPyLongCode = "";
				try {
					//获取拼音简码
					String pyFirstCodeU = Pinyin4jUtils.toPyFirstUppercase(location.getName());
					currPyCode = pyFirstCodeU;
					//获取拼音全码
					String pyCodeU = Pinyin4jUtils.toPyUppercase(location.getName());
					//拼接拼音简码和拼音全码
					currPyLongCode = pyFirstCodeU+"@&&@"+pyCodeU;
					
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				} 
				
				//获取拼音编码
				String pyCode = this.getPyCode(location.getPid(), currPyCode);
				location.setPycode(pyCode);
				//获取拼音longCode编码
				String pyLongCode = this.getPyLongCode(location.getPid(), currPyLongCode);
				location.setPylongcode(pyLongCode);
				//获取地理位置longCode编码
				//String longCode = this.getLongCode(location.getPid(), location.getCode());
				//location.setLongCode(longCode);
				
				//判断是否更新traceIds
				if(isUpdateTraceIds) {
					//获取当前节点的traceIds
					String traceIds = this.getTraceIds(location.getId(), location.getPid());
					//设置当前节点的traceIds
					location.setTraceIds(traceIds);
				}
				
				rows = locationMapper.updateByPrimaryKeySelective(location);
				if(rows>0) {
					rows = this.updateLocation(location.getId(), isUpdateTraceIds);
				}
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
			return rows;
		}
		return 1;
	}

	/**
	 * @Title: deleteById
	 * @Description: 根据主键删除（如果删除节点中有子节点一起删除）（逻辑删除）
	 * @param id
	 * @return
	 * @see com.learnbind.ai.service.location.LocationService#deleteById(java.lang.Long)
	 */
	@Override
	@Transactional
	public int deleteById(Long id) {
		try {
			Location location = locationMapper.selectByPrimaryKey(id);

			if (getLeafNodeSize(location.getId()) > 0) {// 如果有子节点
				return deleteNodes(id);
			} else {// 如果没有子节点直接删除当前节点
				Location record = new Location();
				record.setId(id);
				record.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				int rows = locationMapper.updateByPrimaryKeySelective(record);
				if (rows > 0) {
					// TODO 此处可删除与地理位置相关的关系表
					return rows;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	/**
	 * 获取叶子节点个数
	 * 
	 * @param id
	 * @return
	 */
	private int getLeafNodeSize(Long id) {
		Location record = new Location();
		record.setPid(id);
		List<Location> locationList = locationMapper.select(record);
		if (locationList != null) {
			return locationList.size();
		}
		return 0;
	}

	/**
	 * 循环删除子节点
	 * 
	 * @param id
	 * @return
	 */
	@Transactional
	private int deleteNodes(Long pid) throws Exception {
		// System.out.println("开始查询并删除子节点 主节点ID："+pid);
		Location record = new Location();
		record.setPid(pid);
		List<Location> locationList = locationMapper.select(record);
		int rows = 0;
		for (int i = 0; i < locationList.size(); i++) {
			Location location = locationList.get(i);
			// System.out.println("for循环 节点："+location.toString());
			if (getLeafNodeSize(location.getId()) <= 0) {// 如果没有子节点
				Location temp = new Location();
				temp.setId(location.getId());
				temp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
				rows = locationMapper.updateByPrimaryKeySelective(temp);
				// System.out.println("for循环 删除节点："+location.toString());
				if (rows > 0) {
					// TODO 此处可删除与地理位置相关的关系表
				}
			} else {
				rows = deleteNodes(location.getId());
			}
		}
		if (rows > 0) {
			Location temp = new Location();
			temp.setId(pid);
			temp.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
			rows = locationMapper.updateByPrimaryKeySelective(temp);
			// System.out.println("删除 主节点ID："+pid);
			if (rows > 0) {
				// TODO 此处可删除与地理位置相关的关系表
			}
			return rows;
		}

		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	@Override
	@Transactional
	public int updateListById(List<Location> locationList) {
		int rows = 0;
		try {
			for (Location location : locationList) {
				int row = locationMapper.updateByPrimaryKeySelective(location);
				rows = rows + row;
			}
			if (rows > 0) {
				return rows;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	@Override
	public List<Location> getBlockListByPid(Long pid) {
		
		Location record = new Location();
		if (pid != null) {
			record.setPid(pid);
		}
		record.setLocalNodeType(EnumLocalNodeType.LOCAL_NODE_TYPE_BLOCK.getCode());
		
		return locationMapper.select(record);
	}

	@Override
	public List<Location> getBuildingListByPid(Long pid) {
		Example example  = new Example(Location.class);
		Criteria criteria = example.createCriteria();
		if (pid != null) {
			criteria.andEqualTo("pid", pid);
		}
		example.createCriteria().andEqualTo("localNodeType", EnumLocalNodeType.LOCAL_NODE_TYPE_BUILDING.getCode());
		example.setOrderByClause(" SORT_VALUE ASC, ID ASC, TRACE_IDS ASC");
		
		
		return locationMapper.selectByExample(example);
	}

	@Override
	public List<Location> getUnitListByPid(Long pid) {
		Example example  = new Example(Location.class);
		Criteria criteria = example.createCriteria();
		if (pid != null) {
			criteria.andEqualTo("pid", pid);
		}
		example.createCriteria().andEqualTo("localNodeType", EnumLocalNodeType.LOCAL_NODE_TYPE_UNIT.getCode());
		example.setOrderByClause(" SORT_VALUE ASC, ID ASC, TRACE_IDS ASC");
		
		
		return locationMapper.selectByExample(example);
	}

	@Override
	public List<Location> getRoomListByPid(Long pid) {
		Example example  = new Example(Location.class);
		Criteria criteria = example.createCriteria();
		if (pid != null) {
			criteria.andEqualTo("pid", pid);
		}
		example.createCriteria().andEqualTo("localNodeType", EnumLocalNodeType.LOCAL_NODE_TYPE_ROOM.getCode());
		example.setOrderByClause(" SORT_VALUE ASC, ID ASC, TRACE_IDS ASC");
		
		
		return locationMapper.selectByExample(example);
	}

	/** 
	 * @Title: getLongCodeByPid
	 * @Description: 根据pid和code获取longcode
	 * @param pid
	 * @param code
	 * @return 
	 * @see com.learnbind.ai.service.location.LocationService#getLongCode(java.lang.Long, java.lang.String)
	 */
	@Override
	public String getLongCode(Long pid, String code) {
		
		String longCode = "";
		Location location = this.selectByPrimaryKey(pid);
		if(location==null || StringUtils.isBlank(location.getLongCode())) {
			return code;
		}else {
			if(StringUtils.isBlank(code)) {
				return location.getLongCode();
			}
			longCode = location.getLongCode();
			longCode +="-"+ code;
		}
		return longCode;

	}
	
	@Override
	public String getPyCode(Long pid, String pycode) {
		
		Location location = this.selectByPrimaryKey(pid);
		if(location==null || StringUtils.isBlank(location.getPycode())) {
			return pycode;
		}else {
			if(StringUtils.isBlank(pycode)) {
				return location.getPycode();
			}
			String pyCodeTemp = location.getPycode();
			pyCodeTemp = pyCodeTemp+"-"+pycode;
			return pyCodeTemp;
		}

	}
	
	@Override
	public String getPyLongCode(Long pid, String pycode) {
		
		Location location = this.selectByPrimaryKey(pid);
		if(location==null || StringUtils.isBlank(location.getPylongcode())) {
			return pycode;
		}else {
			if(StringUtils.isBlank(pycode)) {
				return location.getPylongcode();
			}
			String[] pyCodeArr = pycode.split("@&&@");
			String[] pyLongCodeArr = location.getPylongcode().split("@&&@");
			String pyLongCode1 = pyLongCodeArr[0]+"-"+pyCodeArr[0];
			String pyLongCode2 = pyLongCodeArr[1]+"-"+pyCodeArr[1];
			return pyLongCode1+"@&&@"+pyLongCode2;
		}

	}

	@Override
	@Transactional
	public int setDefaultCustomer(Long locationId, Long customerId) {
		int rows = 1;
		//根据地理位置ID设置地理位置-客户关系的默认客户状态为非默认状态
//		int rows = this.setDefaultStatusByLocationId(locationId);
//		if(rows>0) {
//			//设置地理位置-客户关系表的默认客户
//			rows = this.setLocationCustomerDefault(locationId, customerId);
//			if(rows>0) {
		
		//查询地理位置是否与表计绑定
		List<Long> meterIdList = locationMeterService.getMeterIdListByLocationId(locationId);
		//如果有绑定表计，则设置客户-表计的默认客户状态
		if(meterIdList!=null && meterIdList.size()>0) {
			//根据表计ID集合设置客户-表计关系的默认默认状态为非默认状态
			this.setDefaultStatusByMeterIdList(meterIdList);
			
			//设置客户-表计关系表的默认客户
			rows = this.setCustomerMeterDefault(customerId, meterIdList);
		}
		
//			}
//		}
		if(rows<=0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return rows;
	}
	
	/**
	 * @Title: setLocationCustomerDefault
	 * @Description: 设置地理位置-客户关系表的默认客户
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	private int setLocationCustomerDefault(Long locationId, Long customerId) {
		//条件
		Example example = new Example(LocationCustomer.class);
		example.createCriteria().andEqualTo("locationId", locationId).andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		//修改内容
		LocationCustomer lc = new LocationCustomer();
		//lc.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		
		return locationCustomerService.updateByExampleSelective(lc, example);
	}
	
	/**
	 * @Title: setCustomerMeterDefault
	 * @Description: 设置客户-表计关系表的默认客户
	 * @param customerId
	 * @param meterIdList
	 * @return 
	 */
	private int setCustomerMeterDefault(Long customerId, List<Long> meterIdList) {
		//条件
		Example example = new Example(CustomerMeter.class);
		example.createCriteria().andIn("meterId", meterIdList).andEqualTo("customerId", customerId).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		//修改内容
		CustomerMeter cm = new CustomerMeter();
		cm.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		
		return customerMeterService.updateByExampleSelective(cm, example);
	}
	
	/**
	 * @Title: setDefaultCustomer
	 * @Description: 根据地理位置ID设置地理位置-客户关系的默认客户状态为非默认状态
	 * @param locationId
	 * @return 
	 */
	private int setDefaultStatusByLocationId(Long locationId) {
		LocationCustomer lcTemp = new LocationCustomer();
		lcTemp.setLocationId(locationId);
		lcTemp.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		List<LocationCustomer> lcList = locationCustomerService.select(lcTemp);
		List<Long> lcIdList = new ArrayList<>();
		for(LocationCustomer lc : lcList) {
			lcIdList.add(lc.getId());
		}
		if(lcIdList!=null && lcIdList.size()>0) {
			Example example = new Example(LocationCustomer.class);
			example.createCriteria().andIn("id", lcIdList);
			LocationCustomer updateData = new LocationCustomer();
			//updateData.setDefaultCustomer(EnumDefaultStatus.DEFAULT_NO.getValue());
			return locationCustomerService.updateByExampleSelective(updateData, example);
		}
		return 1;
	}
	/**
	 * @Title: setDefaultStatusByMeterIdList
	 * @Description: 根据表计ID集合设置客户-表计关系的默认状态为非默认状态
	 * @param locationId
	 * @return 
	 */
	private int setDefaultStatusByMeterIdList(List<Long> meterIdList) {
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

	@Override
	@Transactional
	public Long insertLocation(Location location) {
		try {
			int rows = locationMapper.insertSelective(location);
			if(rows>0) {
				//获取当前地理位置节点的traceIds
				String traceIds = this.getCurrNodeTraceIds(location.getId(), location.getPid());
				this.updateCurrNodeTraceIds(location.getId(), traceIds);//更新当前地理位置节点的traceIds
				
//				//如果工程ID不为空，则增加地理位置-工程关系
//				if(engineeringId!=null) {
//					this.insertLocationEngineering(location.getId(), traceIds, engineeringId);
//				}
			}
			return location.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0l;
	}
	
	/**
	 * @Title: getCurrNodeTraceIds
	 * @Description: 获取当前地理位置节点的traceIds
	 * @param locationId
	 * @param locationPid
	 * @return 
	 */
	private String getCurrNodeTraceIds(Long locationId, Long locationPid) {
		//父节点地理位置
		String pTraceIds = null;//父节点痕迹地理位置ID
		Location locationTemp = locationMapper.selectByPrimaryKey(locationPid);
		if(locationTemp!=null) {
			pTraceIds = locationTemp.getTraceIds();
		}
		
		String traceIds = locationId.toString();
		//修改痕迹地理位置ID（用“-”号分隔）
		if(StringUtils.isNotBlank(pTraceIds)) {
			traceIds = pTraceIds+"-"+locationId;
		}
		
		return traceIds;
	}
	/**
	 * @Title: updateCurrNodeTraceIds
	 * @Description: 设置当前地理位置节点的traceIds
	 * @param locationId
	 * @param traceIds 
	 */
	private void updateCurrNodeTraceIds(Long locationId, String traceIds) {
		//修改痕迹地理位置ID（用“-”号分隔）
		Location updateTraceIds = new Location();
		updateTraceIds.setId(locationId);
		updateTraceIds.setTraceIds(traceIds);
		
		locationMapper.updateByPrimaryKeySelective(updateTraceIds);
	}
	
	/**
	 * @Title: insertLocationEngineering
	 * @Description: 增加地理位置-工程关系
	 * @param locationId
	 * @param locationTraceIds
	 * @param engineeringId 
	 */
	private void insertLocationEngineering(Long locationId, String locationTraceIds, Long engineeringId) {
		LocationEngineering le = new LocationEngineering();
		le.setLocationId(locationId);
		le.setLocationTraceIds(locationTraceIds);
		le.setEngineeringId(engineeringId);
		locationEngineeringService.insertSelective(le);
	}
	
	/**
	 * @Title: deleteLocationEngineering
	 * @Description: 根据地理位置ID删除地理位置-工程关系记录
	 * @param locationId 
	 */
	private void deleteLocationEngineering(Long locationId) {
		LocationEngineering le = new LocationEngineering();
		le.setLocationId(locationId);
		locationEngineeringService.delete(le);
	}
	
	@Override
	public List<Map<String, Object>> getUnitList(String traceIds, String localNodeType) {
		return locationMapper.getUnitList(traceIds, localNodeType);
	}


	@Override
	public String getBlockNameByTraceIds(String locaBlockTraceIds) {
		return locationMapper.getBlockNameByTraceIds(locaBlockTraceIds);
	}

	@Override
	public String getUnitLongCode(String traceIds) {
		return locationMapper.getUnitLongCode(traceIds);
	}

	@Override
	public Long getSortValue(Long pid) {
		return locationMapper.getSortValue(pid);
	}

	@Override
	public List<Location> getUnitListByTraceIds(String traceIds) {
		return locationMapper.getUnitListByTraceIds(traceIds);
	}

	@Override
	public String getPlace(String traceIds) {
		
		final String BLOCK = "BLOCK";
		final String BUILDING = "BUILDING";
		final String UNIT = "UNIT";
		final String ROOM = "ROOM";
		
		String place = null;
		if(StringUtils.isNotBlank(traceIds)) {
			String[] locationIdArr = traceIds.split("-");
			
			List<String> nodeTypeList = new ArrayList<>();
			nodeTypeList.add(BLOCK);
			nodeTypeList.add(BUILDING);
			nodeTypeList.add(UNIT);
			nodeTypeList.add(ROOM);
			
			place = locationMapper.getPlace(nodeTypeList, locationIdArr);
			
//			List<Location> locationBlockList = locationMapper.getLocation(BLOCK, locationIdArr);
//			List<Location> locationBuildingList = locationMapper.getLocation(BUILDING, locationIdArr);
//			List<Location> locationUnitList = locationMapper.getLocation(UNIT, locationIdArr);
//			List<Location> locationRoomList = locationMapper.getLocation(ROOM, locationIdArr);
//			if(locationBlockList!=null && locationBlockList.size()>0) {
//				Location locationBlock = locationBlockList.get(0);
//				String name = locationBlock.getName();
//				place.append(name);
//			}
//			if(locationBuildingList!=null && locationBuildingList.size()>0) {
//				Location locationBuilding = locationBuildingList.get(0);
//				String name = locationBuilding.getName();
//				place.append("-").append(name);
//			}
//			if(locationUnitList!=null && locationUnitList.size()>0) {
//				Location locationUnit = locationUnitList.get(0);
//				String name = locationUnit.getName();
//				place.append("-").append(name);
//			}
//			if(locationRoomList!=null && locationRoomList.size()>0) {
//				Location locationRoom = locationRoomList.get(0);
//				String name = locationRoom.getName();
//				place.append("-").append(name);
//			}
			
			
//			if(traceIdArr!=null && traceIdArr.length>0) {
//				for(int i=0; i<traceIdArr.length; i++) {
//					String traceId = traceIdArr[i];
//					if(StringUtils.isNotBlank(traceId)) {
//						Location location = locationMapper.selectByPrimaryKey(Long.valueOf(traceId));
//						String name = location.getName();
//						if(i==0) {
//							place.append(name);
//						}else {
//							place.append("-").append(name);
//						}
//					}
//				}
//			}
		}
		return place;
	}
	
	@Override
	public String getPlaceNotRoom(String traceIds) {
		
		final String BLOCK = "BLOCK";
		final String BUILDING = "BUILDING";
		final String UNIT = "UNIT";
		final String ROOM = "ROOM";
		
		String place = null;
		if(StringUtils.isNotBlank(traceIds)) {
			String[] locationIdArr = traceIds.split("-");
			
			List<String> nodeTypeList = new ArrayList<>();
			nodeTypeList.add(BLOCK);
			nodeTypeList.add(BUILDING);
			nodeTypeList.add(UNIT);
			//nodeTypeList.add(ROOM);
			
			place = locationMapper.getPlace(nodeTypeList, locationIdArr);
			
//			List<Location> locationBlockList = locationMapper.getLocation(BLOCK, traceIdArr);
//			List<Location> locationBuildingList = locationMapper.getLocation(BUILDING, traceIdArr);
//			List<Location> locationUnitList = locationMapper.getLocation(UNIT, traceIdArr);
//			//List<Location> locationRoomList = locationMapper.getLocation(ROOM, traceIdArr);
//			if(locationBlockList!=null && locationBlockList.size()>0) {
//				Location locationBlock = locationBlockList.get(0);
//				String name = locationBlock.getName();
//				place.append(name);
//			}
//			if(locationBuildingList!=null && locationBuildingList.size()>0) {
//				Location locationBuilding = locationBuildingList.get(0);
//				String name = locationBuilding.getName();
//				place.append("-").append(name);
//			}
//			if(locationUnitList!=null && locationUnitList.size()>0) {
//				Location locationUnit = locationUnitList.get(0);
//				String name = locationUnit.getName();
//				place.append("-").append(name);
//			}
//			if(locationRoomList!=null && locationRoomList.size()>0) {
//				Location locationRoom = locationRoomList.get(0);
//				String name = locationRoom.getName();
//				place.append("-").append(name);
//			}
			
			
//			if(traceIdArr!=null && traceIdArr.length>0) {
//				for(int i=0; i<traceIdArr.length; i++) {
//					String traceId = traceIdArr[i];
//					if(StringUtils.isNotBlank(traceId)) {
//						Location location = locationMapper.selectByPrimaryKey(Long.valueOf(traceId));
//						String name = location.getName();
//						if(i==0) {
//							place.append(name);
//						}else {
//							place.append("-").append(name);
//						}
//					}
//				}
//			}
		}
		return place;
	}

	@Override
	public List<Location> getLocation(String nodeType, String[] traceIdArr) {
		return locationMapper.getLocation(nodeType, traceIdArr);
	}


	@Override
	public List<Location> getLocationByTraceIds(String traceIds) {
		return locationMapper.getLocationByTraceIds(traceIds);
	}



}
