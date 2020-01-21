package com.learnbind.ai.service.importexcel.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.LocationDataBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.enumclass.EnumSettlementStatus;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.EnumAppReadResult;
import com.learnbind.ai.common.enumclass.EnumCustomerStatus;
import com.learnbind.ai.common.enumclass.EnumCustomerType;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.common.enumclass.EnumLocalNodeType;
import com.learnbind.ai.common.enumclass.EnumMeterCycleStatus;
import com.learnbind.ai.common.enumclass.EnumMeterSettingStatus;
import com.learnbind.ai.common.enumclass.EnumMeterStatus;
import com.learnbind.ai.common.enumclass.EnumMeterVirtualReal;
import com.learnbind.ai.common.enumclass.EnumNotifyMode;
import com.learnbind.ai.common.enumclass.EnumReadMode;
import com.learnbind.ai.common.enumclass.EnumSubAccountStatus;
import com.learnbind.ai.common.enumclass.EnumWaterStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.constant.MeterFactoryConstant;
import com.learnbind.ai.model.CustomerAccount;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.MeterRecordTemp;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.SysWaterPrice;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.customers.CustomerMeterService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.dict.DataDictService;
import com.learnbind.ai.service.importexcel.ImportExcelService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationMeterService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.MeterRecordService;
import com.learnbind.ai.service.meterrecord.MeterRecordTempService;
import com.learnbind.ai.service.meters.MetersService;
import com.learnbind.ai.service.trace.AccountItemTraceService;
import com.learnbind.ai.service.waterprice.WaterPriceService;
import com.learnbind.ai.util.pinyin4j.Pinyin4jUtils;

import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.importexcel.impl
 *
 * @Title: ImportExcelServiceImpl.java
 * @Description: 导入Excel服务实现类
 *
 * @author Administrator
 * @date 2019年6月28日 上午9:30:52
 * @version V1.0 
 *
 */
@Service
public class ImportExcelServiceImpl implements ImportExcelService {

	private final Log log = LogFactory.getLog(ImportExcelServiceImpl.class);	
	
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
	private DataDictService dataDictService;
	@Autowired
	private MeterRecordService meterRecordService;
	@Autowired
	private MeterRecordTempService meterRecordTempService;  //app抄表记录服务
	@Autowired
	private CustomerAccountItemService customerAccountItemService;//账目
	@Autowired
	private AccountItemTraceService accountItemTraceService;//账目日志
	
	//------------------------------	解析Excel上传的地理位置字符串并保存到数据库	开始	------------------------------
	@Override
	@Transactional
	public int parseLocationExcelDataToDb(Long locationId, List<String> locationStrList) {
		Location location = locationService.selectByPrimaryKey(locationId);//查询当前地理位置节点
		Long blockId = location.getId();
		String block = location.getName();
		String traceIds = location.getTraceIds();
		
		//String locationBlockCode = location.getCode();

		List<Map<String, Object>> buildingList = new ArrayList<>();//地理位置-楼号集合
		List<Map<String, Object>> unitList = new ArrayList<>();//地理位置-单元集合
		List<Map<String, Object>> roomList = new ArrayList<>();//地理位置-房间号集合
		
		List<String> newLocationStrList = this.parseLocationStrList(locationStrList);//解析地理位置字符串，把xx小区xx楼号xx单元xxx解析为楼号-单元-房间号
		
		for(String roomStr : newLocationStrList) {
			
			String[] roomArr = roomStr.split("-");
			
			if(roomArr.length<3) {
				System.out.println(JSON.toJSONString(roomArr));
			}
			
			String building = roomArr[0];
			String unit = roomArr[1];
			String room = roomArr[2];
			
			Map<String, Object> buildingMap = new HashMap<>();
			buildingMap.put("nodeType", EnumLocalNodeType.LOCAL_NODE_TYPE_BUILDING.getCode());
			buildingMap.put("name", building);
			buildingMap.put("parent", block);
			buildingMap.put("current", block+"-"+building);
			//buildingMap.put("parentLongCode", locationBlockCode);
			buildingMap.put("uniqueCode", building);
			//buildingMap.put("code", building);
			//buildingMap.put("longCode", locationBlockCode+"-"+building);
			buildingList.add(buildingMap);
			
			Map<String, Object> unitMap = new HashMap<>();
			unitMap.put("nodeType", EnumLocalNodeType.LOCAL_NODE_TYPE_UNIT.getCode());
			unitMap.put("name", unit);
			unitMap.put("parent", block+"-"+building);
			unitMap.put("current", block+"-"+building+"-"+unit);
			//unitMap.put("parentLongCode", locationBlockCode+"-"+building);
			unitMap.put("uniqueCode", building+"-"+unit);
			//unitMap.put("code", unit);
			//unitMap.put("longCode", locationBlockCode+"-"+building+"-"+unit);
			unitList.add(unitMap);
			
			Map<String, Object> roomMap = new HashMap<>();
			roomMap.put("nodeType", EnumLocalNodeType.LOCAL_NODE_TYPE_ROOM.getCode());
			roomMap.put("name", room);
			roomMap.put("parent", block+"-"+building+"-"+unit);
			roomMap.put("current", block+"-"+building+"-"+unit+"-"+room);
			//roomMap.put("parentLongCode", locationBlockCode+"-"+building+"-"+unit);
			roomMap.put("uniqueCode", building+"-"+unit+"-"+room);
			//roomMap.put("code", room);
			//roomMap.put("longCode", locationBlockCode+"-"+building+"-"+unit+"-"+room);
			roomList.add(roomMap);
			
		}
		System.out.println("building去重前："+buildingList.size()+"---"+buildingList.toString());
		buildingList = this.removeRepeatMapByKey(buildingList, "uniqueCode");
		System.out.println("building去重后："+buildingList.size()+"---"+buildingList.toString());
		
		System.out.println("unit去重前："+unitList.size()+"---"+unitList.toString());
		unitList = this.removeRepeatMapByKey(unitList, "uniqueCode");
		System.out.println("unit去重后："+unitList.size()+"---"+unitList.toString());
		
		System.out.println("room去重前："+roomList.size()+"---"+roomList.toString());
		roomList = this.removeRepeatMapByKey(roomList, "uniqueCode");
		System.out.println("room去重后："+roomList.size()+"---"+roomList.toString());
		
		List<LocationDataBean> list = this.getLocationDataBeanList(buildingList, unitList, roomList);
		
		return this.insertBatchLocation(blockId, traceIds, list);
	}
	
	/**
	 * @Title: parseLocationStrList
	 * @Description: 解析地理位置字符串，把【楼号-单元-房间号】、【xx楼号-xx单元-xxx】、【xx小区xx楼号xx单元xxx】或【xx楼号xx单元xxx】解析为【楼号-单元-房间号】
	 * @param locationStrList
	 * @return 
	 */
	private List<String> parseLocationStrList(List<String> locationStrList){
		
		List<String> newLocationStrList = new ArrayList<>();
		
		//把地理位置字符串格式为【楼号-单元-房间号】的字符串解析为【xx楼号xx单元xxx】
		for(String locationStr : locationStrList) {
			//String str = "1-1-301";
			log.debug("----------"+locationStr);
			String[] tempStr = locationStr.split("-");
			if(tempStr.length==1) {
				newLocationStrList = locationStrList;
				break;
			}
			String newLocationStr = locationStr.replaceFirst("\\-+", "楼号").replaceAll("\\-+", "单元");
			newLocationStr = newLocationStr.replaceAll("\\楼号+", "楼号").replaceAll("\\单元+", "单元");
			newLocationStrList.add(newLocationStr);
		}
		
		List<String> new2LocationStrList = new ArrayList<>();
		
		//把地理位置字符串格式为【xx小区xx楼号xx单元xxx】或【xx楼号xx单元xxx】的字符串解析为【楼号-单元-房间号】
		for(String locationStr : newLocationStrList) {
			//String str = "南庄新村1号楼1单元301";
			String reg = "[\u4e00-\u9fa5]";
			Pattern pat = Pattern.compile(reg);  
			Matcher mat=pat.matcher(locationStr); 
			String repickStr = mat.replaceAll("-");
			if(repickStr.startsWith("-")) {
				repickStr = repickStr.replaceFirst("\\-+", "");
			}
			repickStr = repickStr.replaceAll("\\-+", "-");
			new2LocationStrList.add(repickStr);
		}
		return new2LocationStrList;
	}
	
	/**
	 * @Title: getLocationDataBeanList
	 * @Description: 获取地理位置的Excel数据封装的Bean
	 * @param buildingList
	 * @param unitList
	 * @param roomList
	 * @return 
	 */
	private List<LocationDataBean> getLocationDataBeanList(List<Map<String, Object>> buildingList, List<Map<String, Object>> unitList, List<Map<String, Object>> roomList){
		List<LocationDataBean> list1 = new ArrayList<>();
		for(Map<String, Object> map1 : buildingList) {
			//String code1 = map1.get("code").toString();
			//String longCode1 = map1.get("longCode").toString();
			//String parentLongCode1 = map1.get("parentLongCode").toString();
			String nodeType1 = map1.get("nodeType").toString();
			String name1 = map1.get("name").toString();
			String parent1 = map1.get("parent").toString();
			String current1 = map1.get("current").toString();
			
			LocationDataBean bean1 = new LocationDataBean();
			bean1.setName(name1);
			//bean1.setCode(code1);
			//bean1.setLongCode(longCode1);
			bean1.setNodeType(nodeType1);
			
			List<LocationDataBean> list2 = new ArrayList<>();
			for(Map<String, Object> map2 : unitList) {
				
				//String code2 = map2.get("code").toString();
				//String longCode2 = map2.get("longCode").toString();
				//String parentLongCode2 = map2.get("parentLongCode").toString();
				String nodeType2 = map2.get("nodeType").toString();
				String name2 = map2.get("name").toString();
				String parent2 = map2.get("parent").toString();
				String current2 = map2.get("current").toString();
				
				if(parent2.equals(current1)) {
					LocationDataBean bean2 = new LocationDataBean();
					//bean2.setCode(code2);
					//bean2.setLongCode(longCode2);
					bean2.setNodeType(nodeType2);
					bean2.setName(name2);
					
					List<LocationDataBean> list3 = new ArrayList<>();
					for(Map<String, Object> map3 : roomList) {
						//String code3 = map3.get("code").toString();
						//String longCode3 = map3.get("longCode").toString();
						//String parentLongCode3 = map3.get("parentLongCode").toString();
						String nodeType3 = map3.get("nodeType").toString();
						String name3 = map3.get("name").toString();
						String parent3 = map3.get("parent").toString();
						String current3 = map3.get("current").toString();
						if(parent3.equals(current2)) {
							LocationDataBean bean3 = new LocationDataBean();
							//bean3.setCode(code3);
							//bean3.setLongCode(longCode3);
							bean3.setNodeType(nodeType3);
							bean3.setName(name3);
							bean3.setChildren(null);
							list3.add(bean3);
						}
					}
					bean2.setChildren(list3);
					list2.add(bean2);
				}
				
				
			}
			bean1.setChildren(list2);
			list1.add(bean1);
		}
		//System.out.println("JSON:"+JSON.toJSONString(list1));
		return list1;
	}
	
	/**
	 * @Title: insertBatchLocation
	 * @Description: 递归增加地理位置信息
	 * @param id	上级节点ID
	 * @param traceIds	上级节点的痕迹地理位置ID
	 * @param locationDataBeanList	地理位置数据集合
	 * @return 
	 */
	@Transactional
	private int insertBatchLocation(Long pid, String traceIds, List<LocationDataBean> locationDataBeanList) {
		int rows = 1;
		for(LocationDataBean bean : locationDataBeanList) {
			//String code = bean.getCode();
			//String longCode = bean.getLongCode();
			
			//longCode = replaceCnToNull(longCode);//替换中文，把中文替换成空字符串
			String name = bean.getName();
			List<LocationDataBean> children = bean.getChildren();
			
			String nodeType = bean.getNodeType();//获取节点类型
			Location location = null;
			if(nodeType.equals(EnumLocalNodeType.LOCAL_NODE_TYPE_BUILDING.getCode())) {
				location = this.getLocationBuilding(pid, name);//获取地理位置-楼号
			}else if(nodeType.equals(EnumLocalNodeType.LOCAL_NODE_TYPE_UNIT.getCode())) {
				location = this.getLocationUnit(pid, name);//获取地理位置-单元
			}else {
				location = this.getLocationRoom(pid, name);//获取地理位置-房间号
			}
			//location.setTraceIds(traceIds);
			
			location = this.insertLocation(location);;//增加地理位置信息
			//Long locationId = location.getId();
			//traceIds = traceIds+"-"+locationId;
			if(rows>0) {
				//更新痕迹地理位置ID
				//this.updateLocationTraceIds(id, traceIds);
			}
			
			if(children!=null && children.size()>0) {
				rows = this.insertBatchLocation(location.getId(), location.getTraceIds(), children);
			}
			
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
		}
		return rows;
	}
	
	/**
	 * @Title: updateLocationTraceIds
	 * @Description: 更新痕迹地理位置ID
	 * @param id
	 * @param traceIds 
	 */
	private void updateLocationTraceIds(Long id, String traceIds) {
		Location location = new Location();
		location.setId(id);
		location.setTraceIds(traceIds);
		locationService.updateByPrimaryKeySelective(location);
	}
	
	/**
	 * @Title: getLocationBuilding
	 * @Description: 获取地理位置-楼号
	 * @param longCode
	 * @param pid
	 * @param building
	 * @return 
	 */
	private Location getLocationBuilding(Long pid, String name) {
		
		//String code = replaceCnToNull(building);
		
		Location location = new Location();
		//location.setLongCode(longCode);
		//location.setCode(code);
		location.setName(name);
		location.setPid(pid);
		//location.setLocationLevel(1);
		String sort = this.replaceEnglish(name);
		if(StringUtils.isNotBlank(sort)) {
			location.setSortValue(Long.valueOf(sort));	
		}
		location.setLocalNodeType(EnumLocalNodeType.LOCAL_NODE_TYPE_BUILDING.getCode());
		location.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return location;
	}
	
	/**
	 * @Title: getLocationUnit
	 * @Description: 获取地理位置-单元
	 * @param longCode
	 * @param pid
	 * @param unit
	 * @return 
	 */
	private Location getLocationUnit(Long pid, String unit) {
		
		//String code = replaceCnToNull(unit);
		
		Location location = new Location();
		//location.setLongCode(longCode);
		//location.setCode(code);
		location.setName(unit);
		location.setPid(pid);
		//location.setLocationLevel(2);
		location.setSortValue(Long.valueOf(unit));
		location.setLocalNodeType(EnumLocalNodeType.LOCAL_NODE_TYPE_UNIT.getCode());
		location.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return location;
	}

	/**
	 * @Title: getLocationRoom
	 * @Description: 获取地理位置-房间号
	 * @param longCode
	 * @param pid
	 * @param room
	 * @return 
	 */
	private Location getLocationRoom(Long pid, String room) {
		
		//String code = replaceCnToNull(room);
		
		Location location = new Location();
		//location.setLongCode(longCode);
		//location.setCode(code);
		location.setName(room);
		location.setPid(pid);
		//location.setLocationLevel(3);
		location.setSortValue(Long.valueOf(room));
		location.setLocalNodeType(EnumLocalNodeType.LOCAL_NODE_TYPE_ROOM.getCode());
		location.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		return location;
	}
	
	/**
	 * @Title: insertLocation
	 * @Description: 设置name的拼音码和排序，增加并更新traceIds
	 * @param location
	 * @return 
	 */
	private Location insertLocation(Location location) {
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
		
		//获取拼音longCode编码
		String pyCode = locationService.getPyCode(location.getPid(), currPyCode);
		location.setPycode(pyCode);
		//获取拼音longCode编码
		String pyLongCode = locationService.getPyLongCode(location.getPid(), currPyLongCode);
		location.setPylongcode(pyLongCode);
		
		//获取当前节点下行数+1做为排序的序号
		//Long sortValue = locationService.getSortValue(location.getPid());
		//location.setSortValue(sortValue);
		
		//获取地理位置longCode编码
		//String longCode = locationService.getLongCode(location.getPid(), location.getCode());
		//location.setLongCode(longCode);
		
		location.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		Long locationId = locationService.insertLocation(location);//增加并更新traceIds
		return location;
	}
	
	/**
	 * @Title: replaceCnToNull
	 * @Description: 替换中文，把中文替换成空字符串
	 * @param str
	 * @return 
	 */
	private String replaceCnToNull(String str) {
		//String str = "南庄新村1号楼1单元301";
		String reg = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(reg);  
		Matcher mat=pat.matcher(str); 
		String repickStr = mat.replaceAll("");
		return repickStr;
	}
	
	/**
	 * @Title: removeRepeatMapByKey
	 * @Description: 根据List集合中Map中的某个key去重
	 * @param list
	 * @param mapKey
	 * @return 
	 */
	private List<Map<String, Object>> removeRepeatMapByKey(List<Map<String, Object>> list, String mapKey) {
		if (CollectionUtils.isEmpty(list))
			return null;

		// 把list中的数据转换成msp,去掉同一id值多余数据，保留查找到第一个id值对应的数据
		List<Map<String, Object>> listMap = new ArrayList<>();
		Map<String, Map<String, Object>> msp = new HashMap<>();
		for (int i=0; i<list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String id = (String)map.get(mapKey);
			map.remove(mapKey);
			msp.put(id, map);
		}
		// 把msp再转换成list,就会得到根据某一字段去掉重复的数据的List<Map>
		Set<String> mspKey = msp.keySet();
		for (String key : mspKey) {
			Map<String, Object> newMap = msp.get(key);
			newMap.put(mapKey, key);
			listMap.add(newMap);
		}
		return listMap;
	}
	
	//------------------------------	解析Excel上传的地理位置字符串并保存到数据库	结束	------------------------------

	//------------------------------	解析表计Excel数据并保存到数据库	开始	------------------------------
	@Override
	@Transactional
	public int parseMeterExcelDataToDb(String locationBlockCode, Long pid, List<Map> meterList) {
		
//		Meters defaultMeter = this.getDefaultMeters(locationBlockCode);//小区默认水表（虚表不计费）
//		if(defaultMeter!=null) {
//			metersService.insertSelective(defaultMeter);
//			Long defaultMeterId = defaultMeter.getId();
//			if(defaultMeterId!=null) {
//				pid = defaultMeterId;
//			}
//		}
		
		int rows = 0;
		for(Map<String, Object> meterMap : meterList) {
			
			//门牌号
			String room = (String)meterMap.get("room");
			
			//获取地理位置-房间的长编码
			String pycode = this.getRoomLongCode(locationBlockCode, room);
			//获取地理位置ID
			Location location = this.getLocationIdByLongCode(pycode);
			if(location==null) {
				log.error("------------------------------导入表计信息时，查询地理位置为空-门牌号："+room);
				continue;
			}
			
			Meters meter = this.getMeterBean(pid, meterMap);
			meter.setCycleStatus(EnumMeterCycleStatus.ENABLED.getValue());
			//查询地理位置
			String place = locationService.getPlace(location.getTraceIds());
			meter.setPlace(place);
			//增加表计
			rows = metersService.insertSelective(meter);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			Long meterId = meter.getId();
			if(meterId!=null && meterId>0) {
				rows = locationMeterService.bind(location.getId(), location.getTraceIds(), meterId);
			}
		}
		return rows;
	}

	/**
	 * @Title: getMeterBean
	 * @Description: 把上传的表计信息（Map）转换成表计实体类
	 * @param pid
	 * @param meterMap
	 * @return 
	 */
	private Meters getMeterBean(Long pid, Map<String, Object> meterMap) {
		
		if(pid==null) {//如果pid为null时，设置pid为0
			pid = 0l;
		}
		
		Meters meter = EntityUtils.mapToEntity(meterMap, Meters.class);
		
		String readMode = meter.getReadMode();
		String faction = meter.getFactory();
		if(readMode.equals(EnumReadMode.READ_BLUETOOTH.getCode()) && faction.equals(MeterFactoryConstant.METER_FACTORY_EG)) {
			String meterAddr = meter.getMeterAddress();//表地址	艺高的表地址为1001/1002/11001/12001...	其中后三位为地址，其他部分为采集器地址
			if(StringUtils.isNotBlank(meterAddr)) {
				String meterAddress = meterAddr.substring(meterAddr.length()-3);//表地址
				String collectorAddr = meterAddr.substring(0, meterAddr.length()-3);//采集器地址
				meter.setMeterAddress(meterAddress);
				meter.setCollectorAddr(collectorAddr);
			}
			
		}
		
		//meter.setPid(pid);//父ID
		//meter.setMeterNo(meterNo);//表计编号
		meter.setVirtualReal(EnumMeterVirtualReal.REAL_METER.getValue());//虚/实表，0：实表（默认），1：虚表
		meter.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		meter.setMeterStatus(EnumMeterStatus.DELETE_NO.getValue());//表计状态-正常
		//meter.setReadMode(readMode);
		meter.setStatus(EnumMeterSettingStatus.ZERO.getValue());//复装/暂拆=0
		meter.setCycleStatus(EnumMeterCycleStatus.ENABLED.getValue());
		return meter;
	}
	
	//------------------------------	解析表计Excel数据并保存到数据库	结束	------------------------------
	
	//------------------------------	解析一户多表表计Excel数据并保存到数据库	开始	------------------------------
	@Override
	@Transactional
	public int parseMultiMeterExcelDataToDb(String locationBlockCode, Long pid, List<Map> meterMapList) {
		
//			Meters defaultMeter = this.getDefaultMeters(locationBlockCode);//小区默认水表（虚表不计费）
//			if(defaultMeter!=null) {
//				metersService.insertSelective(defaultMeter);
//				Long defaultMeterId = defaultMeter.getId();
//				if(defaultMeterId!=null) {
//					pid = defaultMeterId;
//				}
//			}
		
		int rows = 0;
		//for(Map<String, Object> meterMap : meterMapList) {
		for(int i=0; i<meterMapList.size(); i++) {
			
			Map<String, Object> meterMap = meterMapList.get(i);
			//门牌号
			String room = (String)meterMap.get("room");
			
			String meterRead1 = (String)meterMap.get("meterRead1");//厨房表底
			String meterRead2 = (String)meterMap.get("meterRead2");//客卫表底
			String meterRead3 = (String)meterMap.get("meterRead3");//卧卫表底
			String meterRead4 = (String)meterMap.get("meterRead4");//阳台表底
			List<String> meterReadList = new ArrayList<>();//一户多表表底集合
			meterReadList.add(meterRead1);
			meterReadList.add(meterRead2);
			meterReadList.add(meterRead3);
			meterReadList.add(meterRead4);
			List<String> descriptionList = new ArrayList<>();//一户多表描述集合
			descriptionList.add("厨房");
			descriptionList.add("客卫");
			descriptionList.add("卧卫");
			descriptionList.add("阳台");
			
			
			//获取地理位置-房间的长编码
			String pycode = this.getRoomLongCode(locationBlockCode, room);
			//获取地理位置ID
			Location location = this.getLocationIdByLongCode(pycode);
			if(location==null) {
				log.error("------------------------------导入表计信息时，查询地理位置为空-门牌号："+room);
				continue;
			}
			
			//Meters meter = this.getMeterBean(pid, meterMap);
			//meter.setCycleStatus(EnumMeterCycleStatus.ENABLED.getValue());
			//查询地理位置
			String place = locationService.getPlace(location.getTraceIds());
			//meter.setPlace(place);
			
			//获取一户多表表计列表
			List<Meters> meterList = this.getMeterBeanList(meterMap, meterReadList, descriptionList);
			
			//一户多表
			for(Meters meterTemp : meterList) {
				meterTemp.setPlace(place);
				//增加表计
				rows = metersService.insertSelective(meterTemp);
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
				Long meterId = meterTemp.getId();
				if(meterId!=null && meterId>0) {
					rows = locationMeterService.bind(location.getId(), location.getTraceIds(), meterId);
				}
			}
			
		}
		return rows;
	}

	/**
	 * @Title: getMeterBeanList
	 * @Description: 一户多表时把一条记录转存为多条记录
	 * @param meter
	 * @param meterReadList
	 * @param descriptionList
	 * @return 
	 */
	private List<Meters> getMeterBeanList(Map<String, Object> meterMap, List<String> meterReadList, List<String> descriptionList) {
		List<Meters> meterList = new ArrayList<>();
		for(int i=0; i<meterReadList.size(); i++) {
			String meterRead = meterReadList.get(i);
//			if(StringUtils.isBlank(meterRead)) {
//				continue;
//			}
			String desc = descriptionList.get(i);
			Meters meterTemp = this.getMeterBean(0l, meterMap);
			if(StringUtils.isNotBlank(meterRead)) {
				meterTemp.setNewMeterBottom(meterRead);	
			}else {
				meterTemp.setNewMeterBottom("0");
			}
			
			meterTemp.setDescription(desc);
			meterList.add(meterTemp);
		}
		return meterList;
	}
	
	//------------------------------	解析一户多表表计Excel数据并保存到数据库	结束	------------------------------
	
	//------------------------------	解析客户Excel数据并保存到数据库	开始	------------------------------
	@Override
	@Transactional
	public int parseCustomerExcelDataToDb(String locationBlockCode, List<Map> customerList) {
		
		int rows = 0;
		
		//for(Map<String, Object> customerMap : customerList) {
		for(int i=0; i<customerList.size(); i++) {
			
			Map<String, Object> customerMap = customerList.get(i);
			
			//门牌号
			String room = (String)customerMap.get("room");
			//客户类型
			String customerTypeStr = (String)customerMap.get("customerType");
			//扣费方式
			String deductTypeStr = (String)customerMap.get("deductType");
			
			//获取地理位置-房间的长编码
			String longCode = this.getRoomLongCode(locationBlockCode, room);
			//获取地理位置ID
			Location location = this.getLocationIdByLongCode(longCode);
			if(location==null) {
				log.error("------------------------------导入客户信息时，查询地理位置为空-门牌号："+room);
				continue;
			}
			
			//客户信息
			Customers customer = EntityUtils.mapToEntity(customerMap, Customers.class);
			//如果业主名称为空时，设置业主名称为房间号
			if(StringUtils.isBlank(customer.getPropName())) {
				customer.setPropName(customer.getRoom());
			}
			if(StringUtils.isBlank(customer.getPropTel())) {
				customer.setPropTel(customer.getPropMobile());
			}
			if(StringUtils.isBlank(customer.getPropMobile())) {
				customer.setPropMobile(customer.getPropTel());
			}
			if(StringUtils.isBlank(customer.getCustomerName())) {
				customer.setCustomerName(customer.getPropName());
			}
			if(StringUtils.isBlank(customer.getCustomerTel())) {
				customer.setCustomerTel(customer.getPropTel());
			}
			if(StringUtils.isBlank(customer.getCustomerMobile())) {
				customer.setCustomerMobile(customer.getPropMobile());
			}
			if(StringUtils.isBlank(customer.getCustomerEmail())) {
				customer.setCustomerEmail(customer.getPropEmail());
			}
			
			Integer customerType = EnumCustomerType.CUSTOMER_PEOPLE.getValue();//客户类型-个人
			if(StringUtils.isNotBlank(customerTypeStr)) {
				customerType = Integer.valueOf(customerTypeStr);
			}
			
			Integer deductType = EnumDeductType.OTHER.getValue();//扣费方式-个人
			if(StringUtils.isNotBlank(deductTypeStr)) {
				deductType = Integer.valueOf(deductTypeStr);
			}

			customer.setCustomerType(customerType);//客户类型
			customer.setDeductType(deductType);//扣费方式
			
			customer.setUseNum(3);//用水人数，默认3人
			customer.setWaterStatus(EnumWaterStatus.ENABLED_NO.getValue());//用水状态
			customer.setNotifyMode(EnumNotifyMode.NOTIFY_ALL.getValue());//通知方式，0:全闭;1:微信打开,短信关闭2:短信打开,微信关闭3:全开
			customer.setIdType(1);//证件类型,1=身份证/2=驾驶证
			customer.setStatus(EnumCustomerStatus.ACCOUNT_OPEN.getValue());//客户状态：-1=未立户；0=已立户；1=已销户；2=已过户；3=过户；
			customer.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			rows = customersService.insertSelective(customer);
			if(rows<=0) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				break;
			}
			Long customerId = customer.getId();
			if(customerId!=null) {
				//账户信息
				CustomerAccount account = new CustomerAccount();
				account.setCustomerId(customerId);
				account.setAccountName(customer.getPropName());//业主名称
				account.setIdNo(customer.getIdNo());//证件号
				account.setTel(customer.getPropTel());//业主电话
				account.setMobile(customer.getPropMobile());//业主手机
				account.setStatus(EnumCustomerStatus.ACCOUNT_OPEN.getValue());//立户状态
				rows = customerAccountService.insertSelective(account);
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
				//银行信息
				CustomerBanks bank = EntityUtils.mapToEntity(customerMap, CustomerBanks.class);
				if(StringUtils.isNotBlank(bank.getCardNo())) {
					bank.setCustomerId(customerId);
					bank.setEnabled(0);//是否有效：0=有效（默认）；1=无效
					rows = bankService.insertSelective(bank);
					if(rows<=0) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						break;
					}
				}
				
				locationCustomerService.bind(location.getId(), location.getTraceIds(), customerId);//绑定地理位置-客户
			}
			
		}
		return rows;
	}
	//------------------------------	解析客户Excel数据并保存到数据库	结束	------------------------------
	
	
	
	/**
	 * @Title: getRoomLongCode
	 * @Description: 获取房间号的LongCode
	 * @param block
	 * @param str
	 * @return 
	 */
	private String getRoomLongCode(String block, String str) {
		//String str = "南庄新村1号楼1单元301";
		String reg = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(reg);  
		Matcher mat=pat.matcher(str); 
		String repickStr = mat.replaceAll("-");
		if(repickStr.startsWith("-")) {
			repickStr = repickStr.replaceFirst("\\-+", "");
		}
		if(repickStr.endsWith("-")) {
			repickStr = repickStr.substring(0, (repickStr.length()-1));
		}
		repickStr = repickStr.replaceAll("\\-+", "-");
		return block+"-"+repickStr;
	}
	
	/**
	 * @Title: getLocationIdByLongCode
	 * @Description: 根据长编码获取地理位置-房间的地理位置ID
	 * @param longCode
	 * @return 
	 */
	private Location getLocationIdByLongCode(String longCode) {
		Example example = new Example(Location.class);
		example.createCriteria().andEqualTo("pycode", longCode).andEqualTo("localNodeType", EnumLocalNodeType.LOCAL_NODE_TYPE_ROOM.getCode()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
		example.setOrderByClause(" ID DESC");
		List<Location> locationList = locationService.selectByExample(example);
		if(locationList!=null && locationList.size()>0) {
			return locationList.get(0);
		}
		return null;
	}
	
	//---------------------------------解析更新数据Excel数据到数据库-------------------------------------------------------------------------------------------------
	/** 
	 * 
	 * @Title: parseUpdateDataExcelDataToDb
	 * @Description: 解析更新数据Excel数据到数据库
	 * @param traceIds
	 * @param excelDataList
	 * @return 
	 * @see com.learnbind.ai.service.importexcel.ImportExcelService#parseUpdateDataExcelDataToDb(java.lang.String, java.util.List)
	 */
	@Override
	public int parseUpdateDataExcelDataToDb(String traceIds, List<Map> excelDataList) {
		
		try {
			for(Map<String, Object> excelDataMap : excelDataList) {
				
				String room = (String)excelDataMap.get("room");//地理位置（楼号-单元-门牌号）
				room = room.trim();//去掉前后空格
				//room = this.replaceChinese(room);
				String cardNo = (String)excelDataMap.get("cardNo");//卡号
				cardNo = this.replaceChinese(cardNo);
				cardNo = this.replaceEnglish(cardNo);
				String customerName = (String)excelDataMap.get("customerName");//客户名称
				//customerName = this.replaceEnglish(customerName);
				String mobile = (String)excelDataMap.get("mobile");//手机号
				mobile = this.replaceChinese(mobile);
				mobile = this.replaceEnglish(mobile);
				String meterRead = (String)excelDataMap.get("meterRead");//表计表底
				String description = (String)excelDataMap.get("decription");//表计描述
				String meterAddr = (String)excelDataMap.get("meterAddr");//表地址
				
				//meterRead = this.replaceChinese(meterRead);
				//meterRead = this.replaceEnglish(meterRead);
				
				//获取客户信息
				List<Customers> customerList = this.getCustomerListByRoom(traceIds, room);
				if(customerList!=null && customerList.size()>0) {
					
					Customers customer = customerList.get(0);//获取客户信息
					
					try {
						Long customerId = customer.getId();//客户ID
						
						//更新客户信息
						this.updateCustomerInfo(customerId, customerName, mobile);
						//查询客户账户ID
						Long customerAccountId = this.getCustomerAccountId(customerId);
						if(customerAccountId==null) {
							break;
						}
						//更新客户-账户信息
						this.updateCustomerAccountInfo(customerAccountId, customerName, mobile);
						//更新客户-银行卡信息
						this.updateCustomerBankInfo(customerId, customerName, cardNo);
						//查询表计ID
						//Long meterId = this.getMeterId(customerId);
						List<Long> meterIdList = this.getMeterIdList(customerId);
						for(Long meterId : meterIdList) {
							try {
								//更新表计信息
								this.updateMeterInfo(meterId, description, meterRead, meterAddr);
								if(StringUtils.isNotBlank(meterRead)) {
									//重新初始化抄表记录
									this.reInitMeterRecord(meterId, customerId, meterRead);
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/** 
	 * @Title: parseAppMeterRecordToDb
	 * @Description: 解析APP抄表记录Excel，并保存到数据库
	 * @param traceIds
	 * @param excelDataList
	 * @return 
	 * @see com.learnbind.ai.service.importexcel.ImportExcelService#parseAppMeterRecordToDb(java.lang.String, java.util.List)
	 */
	@Override
	public int parseAppMeterRecordToDb(String traceIds, List<Map> excelDataList) {
		
		try {
			for(Map<String, Object> excelDataMap : excelDataList) {
				
				String room = (String)excelDataMap.get("room");//地理位置（楼号-单元-门牌号）
				room = room.trim();//去掉前后空格
				//room = this.replaceChinese(room);
				String preRead = (String)excelDataMap.get("preRead");//上期表底
				String currRead = (String)excelDataMap.get("currRead");//本期表底
				String description = (String)excelDataMap.get("description");//描述
				//customerName = this.replaceEnglish(customerName);
				
				
				//获取客户信息
				List<Customers> customerList = this.getCustomerListByRoom(traceIds, room);
				if(customerList!=null && customerList.size()>0) {
					
					Customers customer = customerList.get(0);//获取客户信息
					
					try {
						Long customerId = customer.getId();//客户ID
						List<Long> meterIdList = this.getMeterIdList(customerId);
						for(Long meterId : meterIdList) {
							
							if(StringUtils.isBlank(description)) {
								MeterRecordTemp appMeterRecord = this.getMeterRecordTempBean(customerId, meterId, preRead, currRead);
								meterRecordTempService.insertSelective(appMeterRecord);
							}else {
								Meters meter = metersService.selectByPrimaryKey(meterId);
								if(meter.getDescription().equalsIgnoreCase(description)) {
									MeterRecordTemp appMeterRecord = this.getMeterRecordTempBean(customerId, meterId, preRead, currRead);
									meterRecordTempService.insertSelective(appMeterRecord);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				
				
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * @Title: getMeterRecordTempBean
	 * @Description: 获取App抄表记录Bean
	 * @param customerId
	 * @param meterId
	 * @param preRead
	 * @param currRead
	 * @return 
	 */
	private MeterRecordTemp getMeterRecordTempBean(Long customerId, Long meterId, String preRead, String currRead) {
		
		//系统登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date sysDate = new Date();
		Date preDate = DateUtils.addMonths(sysDate, -1);
		
		MeterRecordTemp temp = new MeterRecordTemp();
		temp.setCustomerId(customerId);
		temp.setMeterId(meterId);
		
		if(StringUtils.isNotBlank(preRead)) {
			temp.setPreRead(preRead);
		}else {
			if(StringUtils.isNotBlank(currRead)) {
				temp.setPreRead(currRead);
			}else {
				temp.setPreRead("0");
			}
		}
		temp.setPreDate(preDate);
		
		if(StringUtils.isNotBlank(currRead)) {
			
			temp.setCurrRead(currRead);
			temp.setCurrDate(sysDate);
			
			//获取本期水量=本期读数-上期读数
			BigDecimal currWaterAmount = BigDecimalUtils.subtract(new BigDecimal(temp.getCurrRead()), new BigDecimal(temp.getPreRead()));
			temp.setCurrAmount(currWaterAmount);
			
			//设置抄表方式和抄表结果状态
			temp.setReadMode(EnumReadMode.READ_MANUAL.getCode());
			temp.setReadResult(EnumAppReadResult.RESULT_MANUAL.getValue());
			
		}else {
			
			if(StringUtils.isNotBlank(preRead)) {
				temp.setCurrRead(preRead);
			}else {
				temp.setCurrRead("0");
			}
			temp.setCurrDate(sysDate);
			
			log.debug("本期读数："+temp.getCurrRead()+"，上期读数："+temp.getPreRead());
			//获取本期水量=本期读数-上期读数
			BigDecimal currWaterAmount = BigDecimalUtils.subtract(new BigDecimal(temp.getCurrRead()), new BigDecimal(temp.getPreRead()));
			temp.setCurrAmount(currWaterAmount);
			
			//设置抄表方式和抄表结果状态
			temp.setReadMode(EnumReadMode.READ_MANUAL.getCode());
			temp.setReadResult(EnumAppReadResult.RESULT_MANUAL.getValue());
		}
		
		temp.setOperatorId(userBean.getId());
		temp.setOperatorName(userBean.getRealname());
		temp.setOperationTime(sysDate);
		
		//设置删除状态为未删除
		temp.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		
		temp.setRemark("PC导入数据");
		return temp;
	}
	
	/**
	 * @Title: getCustomer
	 * @Description: 获取客户信息
	 * @param room
	 * @return 
	 */
	private List<Customers> getCustomerListByRoom(String traceIds, String room) {
		
		List<Customers> customerList = null;
		
		if(StringUtils.isNotBlank(room)) {
			customerList = customersService.getCustomerListByRoom(traceIds, room);
		}
		
		return customerList;
	}
	
	/**
	 * @Title: getMeterIdList
	 * @Description: 获取表计集合
	 * @param customerId
	 * @return 
	 */
	private List<Long> getMeterIdList(Long customerId) {
		
		CustomerMeter cm = new CustomerMeter();
		cm.setCustomerId(customerId);
		//cm.setDefaultCustomer(EnumDefaultStatus.DEFAULT_YES.getValue());
		cm.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
		
		List<CustomerMeter> cmList = customerMeterService.select(cm);
		
		List<Long> meterIdList = new ArrayList<>();
		if(cmList!=null && cmList.size()>0) {
			for(CustomerMeter cmTemp : cmList) {
				Long meterId = cmTemp.getMeterId();
				if(meterId!=null) {
					meterIdList.add(meterId);
				}
			}
		}
		return meterIdList;
	}
	
	/**
	 * @Title: getCustomerAccount
	 * @Description: 查询客户-账户
	 * @param customerId
	 * @return 
	 */
	private Long getCustomerAccountId(Long customerId) {
		CustomerAccount ca = new CustomerAccount();
		ca.setCustomerId(customerId);
		List<CustomerAccount> caList =  customerAccountService.select(ca);
		if(caList!=null && caList.size()>0) {
			ca = caList.get(0);
			return ca.getId();
		}
		return null;
	}
	
	/**
	 * @Title: updateCustomerInfo
	 * @Description: 更新客户信息
	 * @param customerId
	 * @param customerName
	 * @param mobile
	 * @return 
	 */
	private void updateCustomerInfo(Long customerId, String customerName, String mobile) {
		
		if(StringUtils.isBlank(customerName) && StringUtils.isBlank(mobile)) {
			return;
		}
		Customers customer = new Customers();
		customer.setId(customerId);
		if(StringUtils.isNotBlank(customerName)) {
			customer.setPropName(customerName);
			customer.setCustomerName(customerName);
		}
		if(StringUtils.isNotBlank(mobile)) {
			customer.setPropMobile(mobile);
			customer.setPropTel(mobile);
			
			customer.setCustomerMobile(mobile);
			customer.setCustomerTel(mobile);
		}
		
		customersService.updateByPrimaryKeySelective(customer);
		
	}
	
	/**
	 * @Title: updateCustomerBankInfo
	 * @Description: 更新客户银行卡信息
	 * @param customerId
	 * @param customerName
	 * @param cardNo 
	 */
	private void updateCustomerBankInfo(Long customerId, String customerName, String cardNo) {
		
		if(StringUtils.isBlank(customerName) && StringUtils.isBlank(cardNo)) {
			return;
		}
		
		CustomerBanks bank = new CustomerBanks();
		bank.setCustomerId(customerId);
		List<CustomerBanks> bankList = bankService.select(bank);
		if(bankList!=null && bankList.size()>0) {
			bank = bankList.get(0);
			bank.setEnabled(EnumEnabledStatus.ENABLED_NO.getValue());
			if(StringUtils.isNotBlank(customerName)) {
				bank.setAccountName(customerName);
			}
			if(StringUtils.isNotBlank(cardNo)) {
				bank.setCardNo(cardNo);
			}
			bankService.updateByPrimaryKeySelective(bank);
			
		}
		
	}
	
	/**
	 * @Title: updateCustomerAccountInfo
	 * @Description: 更新客户账户信息
	 * @param customerAccountId
	 * @param customerName
	 * @param mobile 
	 */
	private void updateCustomerAccountInfo(Long customerAccountId, String customerName, String mobile) {
		
		if(StringUtils.isBlank(customerName) && StringUtils.isBlank(mobile)) {
			return;
		}
		
		CustomerAccount ca = new CustomerAccount();
		ca.setId(customerAccountId);
		if(StringUtils.isNotBlank(customerName)) {
			ca.setAccountName(customerName);
		}
		if(StringUtils.isNotBlank(mobile)) {
			ca.setMobile(mobile);
		}
		
		customerAccountService.updateByPrimaryKeySelective(ca);
	}
	
	/**
	 * @Title: updateMeterInfo
	 * @Description: 更新表计信息
	 * @param meterId
	 * @param meterRead
	 * @return 
	 */
	private void updateMeterInfo(Long meterId, String description, String meterRead, String meterAddr) {
		
		if(StringUtils.isBlank(meterRead) && StringUtils.isBlank(meterAddr)) {
			return;
		}
		
		Example example = new Example(Meters.class);
		//example.createCriteria().andEqualTo("id", meterId).andEqualTo("description", description);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("id", meterId);
		if(StringUtils.isNotBlank(description)) {
			criteria.andEqualTo("description", description);
		}
		
		Meters meter = new Meters();
		//meter.setId(meterId);
		if(StringUtils.isNotBlank(meterRead)) {
			meter.setNewMeterBottom(meterRead);
		}
		if(StringUtils.isNotBlank(meterAddr)) {
			
			Meters currMeter = metersService.selectByPrimaryKey(meterId);
			if(currMeter.getFactory().equalsIgnoreCase(MeterFactoryConstant.METER_FACTORY_EG)) {
				String meterAddress = meterAddr.substring(meterAddr.length()-3);//表地址
				String collectorAddr = meterAddr.substring(0, meterAddr.length()-3);//采集器地址
				meter.setMeterAddress(meterAddress);
				meter.setCollectorAddr(collectorAddr);
				log.info("更新表地址信息，采集器地址："+meter.getCollectorAddr()+"，表地址："+meter.getMeterAddress());
			}else {
				meter.setMeterAddress(meterAddr);
				log.info("更新表地址信息，表地址："+meter.getMeterAddress());
			}
		}
		
		metersService.updateByExampleSelective(meter, example);
	}
	
	/**
	 * @Title: reInitMeterRecord
	 * @Description: 重新增加初始化抄表记录
	 * @param meterId
	 * @param customerId
	 * @param meterRead 
	 */
	private void reInitMeterRecord(Long meterId, Long customerId, String meterRead) {
		
		if(StringUtils.isBlank(meterRead)) {
			return;
		}
		
		//系统登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		//查询表计
		Meters meter = metersService.selectByPrimaryKey(meterId);
		//增加初始化抄表记录
		meterRecordService.insertInitMeterRecord(meter, customerId, userBean.getId(), userBean.getRealname(), meterRead);
	}
	
	
	//-------------------------------------导入往期欠费部分--------------------------------------------------------------------------------------------------
	@Override
	@Transactional
	public int parsePastOweDataToDb(String traceIds, List<Map> excelDataList) {
		
		try {
			int rows = 0;
			for(Map<String, Object> excelDataMap : excelDataList) {
				
				String room = (String)excelDataMap.get("room");//地理位置（楼号-单元-门牌号）
				room = room.trim();//去掉前后空格
				//room = this.replaceChinese(room);
				String oweAmount = (String)excelDataMap.get("oweAmount");//欠费金额
				//customerName = this.replaceEnglish(customerName);
				
				if(StringUtils.isBlank(oweAmount)) {
					continue;
				}
				BigDecimal oweAmountBd = new BigDecimal(oweAmount);
				oweAmountBd = oweAmountBd.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入（若舍弃部分>=.5，就进位）
				
				//获取客户信息
				List<Customers> customerList = this.getCustomerListByRoom(traceIds, room);
				if(customerList!=null && customerList.size()>0) {
					
					Customers customer = customerList.get(0);//获取客户信息
					
					String period = "2019-11";
					Long pwId = 136l;//居民生活用水-第一阶梯，阿里云/生产环境相同
					SysWaterPrice waterPrice = waterPriceService.selectByPrimaryKey(pwId);
					rows = this.insertBill(customer, period, oweAmountBd, waterPrice);
					if(rows<=0) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						break;
					}
				}else {
					log.debug("未找到客户，门牌号："+room);
				}
				
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	//-------------------------------------导入预存金额部分--------------------------------------------------------------------------------------------------
	@Override
	@Transactional
	public int parsePrepaymentDataToDb(String traceIds, List<Map> excelDataList) {
		
		try {
			
			//登录用户
			UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			int rows = 0;
			for(Map<String, Object> excelDataMap : excelDataList) {
				
				String room = (String)excelDataMap.get("room");//地理位置（楼号-单元-门牌号）
				room = room.trim();//去掉前后空格
				//room = this.replaceChinese(room);
				String prepayment = (String)excelDataMap.get("prepayment");//预存金额
				//customerName = this.replaceEnglish(customerName);
				
				if(StringUtils.isBlank(prepayment)) {
					continue;
				}
				BigDecimal rechargeAmountBd = new BigDecimal(prepayment);
				rechargeAmountBd = rechargeAmountBd.setScale(2, BigDecimal.ROUND_HALF_UP);//四舍五入（若舍弃部分>=.5，就进位）
				if(BigDecimalUtils.lessOrEquals(rechargeAmountBd, new BigDecimal(0.00))) {
					continue;
				}
				//获取客户信息
				List<Customers> customerList = this.getCustomerListByRoom(traceIds, room);
				if(customerList!=null && customerList.size()>0) {
					
					Customers customer = customerList.get(0);//获取客户信息
					
					//增加充值账单
					Long billId = customerAccountItemService.generatorRechargeBill(customer.getId(), rechargeAmountBd, EnumAiDebitSubjectAction.ADVANCE_WATER_FEE, EnumAiDebitSubjectPayment.PAYMENT_CASH, userBean.getId());
					if(billId==null || billId<=0) {
						rows = 0;
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						break;
					}else {
						rows = 1;
					}
				}else {
					log.debug("未找到客户，门牌号："+room);
				}
				
			}
			return rows;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * @Title: insertBill
	 * @Description: 增加账单
	 * @param customer
	 * @param period
	 * @param oweAmount
	 * @param waterPrice
	 * @return 
	 * 		返回账单ID
	 */
	public int insertBill(Customers customer, String period, BigDecimal oweAmount, SysWaterPrice waterPrice) {
		
		//贷方科目
		String creditSubject = EnumAiDebitCreditStatus.CREDIT.getKey()+EnumAiCreditSubjectAction.WATER_FEE.getKey();
		//贷方摘要
		//String creditDigest = EnumAiDebitCreditStatus.CREDIT.getValue()+EnumAiCreditSubjectAction.WATER_FEE.getValue();
		String creditDigest = EnumAiCreditSubjectAction.WATER_FEE.getValue();
		
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		BigDecimal zero = new BigDecimal("0.00");//初始化BigDecimal类型的0
		
		Long customerId = customer.getId();//客户ID
		CustomerAccount ca = this.getCustomerAccount(customerId);//客户-账户信息
		
		CustomerAccountItem item = new CustomerAccountItem();//账目
		item.setCustomerId(customerId);//客户ID
		item.setAccountId(ca.getId());//账户ID
		item.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
//		item.setDebitDigest();//借方摘要
//		item.setDebitSubject();//借方科目
//		item.setDebitAssistant(debitAssistant);//借方辅助核算
		item.setDebitAmount(zero);//借方金额
//		
		item.setCreditSubject(creditSubject);//贷方科目
		item.setCreditDigest(creditDigest);//贷方摘要
		//item.setCreditAssistant(AccountItemConstant.CREDIT_ASSISTANT_SYS_AUTO);//贷方辅助核算
		item.setCreditAmount(oweAmount);//贷方金额
//		
		item.setDebitCredit(EnumAiDebitCreditStatus.CREDIT.getKey());//借/贷;
//		item.setOverdueDate(overdueDate);//违约金计算日期
		
		item.setAccounter(userBean.getId().toString());//记账人
		item.setAccountDate(new Date());//记账日期
		
		//item.setStartTime(pw.getStartTime());//账单产生时间段的开始时间
		//item.setEndTime(pw.getEndTime());//账单产生时间段的结束时间
		
		item.setPeriod(period);//期间
		
		item.setRemark(creditDigest);//备注
		
		//获取水量
		BigDecimal waterAmount = BigDecimalUtils.divide(oweAmount, waterPrice.getTotalPrice());
		
		BigDecimal baseWaterFee = BigDecimalUtils.multiply(waterPrice.getBasePrice(), waterAmount);//计算基础水费
		BigDecimal sewageWaterFee = BigDecimalUtils.multiply(waterPrice.getTreatmentFee(), waterAmount);//计算污水处理费
		
		item.setBaseWaterFee(baseWaterFee);//基础水费
		item.setSewageWaterFee(sewageWaterFee);//污水处理费水费
		
		
		//结算状态 0=未结算（默认值）；1=结算成功；2=结算失败；3=部分结算；
		item.setSettlementStatus(EnumSettlementStatus.SETTLEMENT_NORMAL.getValue());
		
		//获取分账状态
		item.setAccountStatus(EnumSubAccountStatus.NO_NEED_SUB_ACCOUNT.getValue());//设置分账状态
		
		int rows = customerAccountItemService.insertSelective(item);
		if(rows>0) {
			//增加水费账单日志
			accountItemTraceService.insert(item.getId(), item.getCreditSubject(), userBean.getId().toString(), oweAmount);
			
			return rows;//生成账单成功后返回账单ID
		}
		return 0;
	}
	
	/**
	 * @Title: getCustomerAccount
	 * @Description: 查询客户-账户
	 * @param customerId
	 * @return 
	 */
	private CustomerAccount getCustomerAccount(Long customerId) {
		CustomerAccount ca = new CustomerAccount();
		ca.setCustomerId(customerId);
		return customerAccountService.selectOne(ca);
	}
	
	//----------------------------------导入往期欠费------------------------------------------------------------------------------------
	
	/**
	 * @Title: replaceEnglish
	 * @Description: 把中文汉字[\u4e00-\u9fa5]替换成空字符串
	 * @param str
	 * @return 
	 */
	private String replaceChinese(String str) {
		
		if(StringUtils.isBlank(str)) {
			return "";
		}
		
		String reg = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(reg);  
		Matcher mat=pat.matcher(str); 
		String repickStr = mat.replaceAll("");
		return repickStr;
	}

	/**
	 * @Title: replaceEnglish
	 * @Description: 把英文字母[a-zA-Z]替换成空字符串
	 * @param str
	 * @return 
	 */
	private String replaceEnglish(String str) {
		
		if(StringUtils.isBlank(str)) {
			return "";
		}
		
		String reg = "[a-zA-Z]";
		Pattern pat = Pattern.compile(reg);  
		Matcher mat=pat.matcher(str); 
		String repickStr = mat.replaceAll("");
		return repickStr;
	}
	
	public static void main(String[] args) {
		String addr = "1001";
		System.out.println(addr.substring(0, addr.length()-3));
		System.out.println(addr.substring(addr.length()-3));
	}
	
}
