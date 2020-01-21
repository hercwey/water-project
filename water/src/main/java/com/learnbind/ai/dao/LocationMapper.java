package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.bean.LocationBean;
import com.learnbind.ai.model.Location;

import tk.mybatis.mapper.common.Mapper;

public interface LocationMapper extends Mapper<Location> {
	
	/**
	 * @Title: getChildListById
	 * @Description: 根据ID查询子节点
	 * @param id
	 * @return 
	 * 		返回地理位置集合（地理位置实体类数据和IS_PARENT属性）
	 */
	public List<LocationBean> getChildListById(@Param("id") Long id);
	
	/**
	 * @Title: getListBySearchCond
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<Location> getListBySearchCond(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: getOneBySearchCond
	 * @Description: 查询符合条件的记录（返回第一条数据）
	 * @param searchCond	条件（小区-楼号-单元-门牌号）
	 * @return 
	 * 		返回location对象
	 */
	public Location getFirstBySearchCond(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: getOneBySearchCond
	 * @Description: 查询符合条件的记录
	 * @param searchCond	条件（小区-楼号-单元-门牌号）
	 * @param id	地理位置ID
	 * @param action	-1=上一个；1=下一个
	 * @return 
	 * 		返回location对象
	 */
	public Location getOneBySearchCond(@Param("searchCond") String searchCond, @Param("id") Long id, @Param("action") Integer action);
	
	/**
	 * @Title: getUnitListByTraceIds
	 * @Description: 查询所有单元
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getUnitList(@Param("traceIds") String traceIds, @Param("localNodeType") String localNodeType);
	
	public String getBlockNameByTraceIds(@Param("locaBlockTraceIds") String locaBlockTraceIds);
	
	public String getUnitLongCode(@Param("traceIds") String traceIds);
	
	/**
	 * @Title: getSortValue
	 * @Description: 获取排序的值
	 * @param pid
	 * @return 
	 * 		返回最数据库中数据的最大行号+1
	 */
	public Long getSortValue(@Param("pid") Long pid);
	
	/**
	 * @Title: getUnitListByTraceIds
	 * @Description: 根据traceIds获取单元
	 * @param traceIds
	 * @return 
	 */
	public List<Location> getUnitListByTraceIds(@Param("traceIds") String traceIds);
	
	/**
	 * @Title: getLocation
	 * @Description: 根据地理位置ID集合查询某个节点类型的地理位置信息
	 * @param nodeType
	 * @param traceIdArr
	 * @return 
	 */
	public List<Location> getLocation(@Param("nodeType") String nodeType, @Param("locationIdArr") String[] locationIdArr);
	
	/**
	 * @Title: getPlace
	 * @Description: 获取地理位置字符器（小区-楼号-单元-门牌号）
	 * @param nodeTypeList 节点类型集合
	 * @param traceIdArr	
	 * @return 
	 */
	public String getPlace(@Param("nodeTypeList") List<String> nodeTypeList, @Param("locationIdArr") String[] locationIdArr);
	
	public List<Location> getLocationByTraceIds(@Param("traceIds") String traceIds);
	
}