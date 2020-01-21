package com.learnbind.ai.service.location;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.bean.LocationBean;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.location
 *
 * @Title: LocationService.java
 * @Description: 地理位置服务接口类
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:01:28
 * @version V1.0 
 *
 */
public interface LocationService extends IBaseService<Location, Long> {

	/**
	 * @Title: getChildListById
	 * @Description: 根据ID查询子节点
	 * @param id
	 * @return 
	 * 		返回地理位置集合（地理位置实体类数据和IS_PARENT属性）
	 */
	public List<LocationBean> getChildListById(Long id);
	
	/**
	 * @Title: getListBySearchCond
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<Location> getListBySearchCond(String searchCond);
	
	/**
	 * @Title: getOneBySearchCond
	 * @Description: 查询符合条件的记录（返回第一条数据）
	 * @param searchCond	条件（小区-楼号-单元-门牌号）
	 * @return 
	 * 		返回location对象
	 */
	public Location getFirstBySearchCond(String searchCond);
	
	/**
	 * @Title: getOneBySearchCond
	 * @Description: 查询符合条件的记录
	 * @param searchCond	条件（小区-楼号-单元-门牌号）
	 * @param id	地理位置ID
	 * @param action	-1=上一个；1=下一个
	 * @return 
	 * 		返回location对象
	 */
	public Location getOneBySearchCond(String searchCond, Long id, Integer action);
	
	/**
	 * @Title: updateDropLocation
	 * @Description: 拖拽后更新拼音编码和traceIds
	 * 					地理位置表
	 * 					地理位置-表计表
	 *					地理位置-客户表
	 * @param locationId
	 * @param parentLocationId
	 * @return 
	 */
	public int updateDropLocation(Long locationId, Long parentLocationId);
	
	/**
	 * @Title: updateById
	 * @Description: 修改当前节点和子节点的地理位置信息（不包含traceIds）
	 * @param location			需要更新的地理位置节点
	 * @param isUpdateTraceIds	是否更新节点中的traceIds
	 * @return 
	 */
	public int updateById(Location location, boolean isUpdateTraceIds);
	
	/**
	 * 	根据ID删除
	 * @param id
	 * @return
	 */
	public int deleteById(Long id);
	
	/**
	 * @Title: updateListById
	 * @Description: 批量更新
	 * @param locationList
	 * @return 
	 */
	public int updateListById(List<Location> locationList);
	
	/**
	 * @Title: getBlockListByPid
	 * @Description: 在地理位置中查询节点类型为小区的地理位置
	 * @param pid	pid可能会为null
	 * @return 
	 */
	public List<Location> getBlockListByPid(Long pid);
	
	/**
	 * @Title: getBuildingListByPid
	 * @Description: 在地理位置中查询节点类型为楼号的地理位置
	 * @param pid
	 * @return 
	 */
	public List<Location> getBuildingListByPid(Long pid);
	
	/**
	 * @Title: getUnitListByPid
	 * @Description: 在地理位置中查询节点类型为单元的地理位置
	 * @param pid
	 * @return 
	 */
	public List<Location> getUnitListByPid(Long pid);
	
	/**
	 * @Title: getUnitListByPid
	 * @Description: 在地理位置中查询节点类型为室的地理位置
	 * @param pid
	 * @return 
	 */
	public List<Location> getRoomListByPid(Long pid);
	
	
	/**
	 * @Title: getLongCode
	 * @Description: 查询拼接地理位置编码（小区-楼号-单元-房间号）
	 * @param pid
	 * @param code
	 * @return 
	 */
	public String getLongCode(Long pid, String code);
	
	/**
	 * @Title: getPyCode
	 * @Description: 获取拼音的简码长编码（小区-楼号-单元-房间号）
	 * @param pid
	 * @param pycode
	 * @return 
	 */
	public String getPyCode(Long pid, String pycode);
	
	/**
	 * @Title: getPyLongCode
	 * @Description: 获取拼音的长编码（小区-楼号-单元-房间号）
	 * @param pid
	 * @param pycode
	 * @return 
	 */
	public String getPyLongCode(Long pid, String pycode);
	
	/**
	 * @Title: setDefaultCustomer
	 * @Description: 设置默认客户
	 * @param locationId
	 * @param customerId
	 * @return 
	 */
	public int setDefaultCustomer(Long locationId, Long customerId);
	
	/**
	 * @Title: insertLocation
	 * @Description: 增加地理位置
	 * @param location
	 * @return 
	 */
	public Long insertLocation(Location location);
	
	public List<Map<String, Object>> getUnitList(String traceIds, String localNodeType);
	
	public String getBlockNameByTraceIds(String locaBlockTraceIds);
	
	public String getUnitLongCode(String traceIds);	
	
	/**
	 * @Title: getSortValue
	 * @Description: 获取排序的值
	 * @param pid
	 * @return 
	 * 		返回最数据库中数据的最大行号+1
	 */
	public Long getSortValue(Long pid);	
	
	
	/**
	 * @Title: getUnitListByTraceIds
	 * @Description: 根据traceIds获取单元
	 * @param traceIds
	 * @return 
	 */
	public List<Location> getUnitListByTraceIds(String traceIds);
	
	/**
	 * @Title: getPlace
	 * @Description: 获取地理位置信息（小区 楼号-单元-门牌号）
	 * @param traceIds
	 * @return 
	 */
	public String getPlace(String traceIds);
	
	/**
	 * @Title: getPlaceNotRoom
	 * @Description: 获取地理位置信息（小区 楼号-单元）
	 * @param traceIds
	 * @return 
	 */
	public String getPlaceNotRoom(String traceIds);
	
	/**
	 * @Title: getLocation
	 * @Description: 根据地理位置ID集合查询某个节点类型的地理位置信息
	 * @param nodeType
	 * @param traceIdArr
	 * @return 
	 */
	public List<Location> getLocation(String nodeType, String[] traceIdArr);
	
	/**
	 * added by hz   2019/10/07
	 * @Title: geLocationByTraceIds
	 * @Description:根据traceIds获取结点及所有子节点
	 * @param traceIds 父结点到此节点的路径
	 * @return 等于traceIds union 以此结点为祖先结点的所有子节点.
	 * 		按树的层级排序.
	 */
	public List<Location> getLocationByTraceIds(String traceIds);	
	
}
