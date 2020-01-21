package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.bean.MeterBean;
import com.learnbind.ai.model.Meters;

import tk.mybatis.mapper.common.Mapper;

public interface MetersMapper extends Mapper<Meters> {
	
	/**
	 * @Title: getListBySearchCond
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getListBySearchCond(@Param("searchCond") String searchCond);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<Meters> searchMeters(@Param("searchCond") String searchCond);
	
	/**
	 * @Title: searchChangeMeters
	 * @Description: 查询可以换表的表计集合（只查询待用状态的表计信息）
	 * @param searchCond
	 * @param cycleStatusList
	 * 			cycleStatus:记录水表的生命周期状态 0：待用（待使用）（默认值）；1：领用；2：待启用；3：使用中；4：待检测；5：检测中；6：待检修；7：检修中；8：报废；9：退库；
	 * @return 
	 */
	public List<Meters> searchChangeMeters(@Param("searchCond") String searchCond, @Param("cycleStatusList") List<Integer> cycleStatusList);
	
	/**
	 * @Title: searchMetersByStatus
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @param status
	 * @return 
	 */
	public List<Meters> searchMetersByStatus(@Param("searchCond") String searchCond, @Param("status") Integer status);
	
	/**
	 * @Title: getMeterList
	 * @Description: 根据条件查询表计
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getMeterList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds);
	
	/**
	 * @Title: getMeterList
	 * @Description: 获取可以换表的表计集合
	 * @param searchCond
	 * @param traceIds
	 * @param cycleStatusList
	 * 			cycleStatus:记录水表的生命周期状态 0：待用（待使用）（默认值）；1：领用；2：待启用；3：使用中；4：待检测；5：检测中；6：待检修；7：检修中；8：报废；9：退库；
	 * @return 
	 */
	public List<Meters> getChangeMeterList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("cycleStatusList") List<Integer> cycleStatusList);
	
	/**
	 * @Title: getMeterListByStatus
	 * @Description: 根据条件查询表计
	 * @param searchCond
	 * @param traceIds
	 * @param status
	 * @return 
	 */
	public List<Meters> getMeterListByStatus(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("status") Integer status);
	
	/**
	 * @Title: getMeterListByLocationId
	 * @Description: 根据地理位置查询表计信息
	 * @param locationId
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getMeterListByLocationId(@Param("locationId") Long locationId, @Param("searchCond") String searchCond);
	
	/**
	 * @Title: getMeterList
	 * @Description: 获取已绑定客户的表计集合（包含客户信息）
	 * @return 
	 */
	/**
	 * @Title: getBindMeterList
	 * @Description: 获取已绑定客户的表计集合（包含客户信息）
	 * @param meterId	meterId为null时查询所有
	 * 			查询条件中pid=meterId，根据表计ID查询子节点表计
	 * @return 
	 * 		M.ID AS METER_ID,
			M.PID AS METER_PID,
			M.STEEL_SEAL_NO,
			M.PLACE,
			C.ID AS CUSTOMER_ID,
			C.PROP_NAME
	 */
	public List<Map<String, Object>> getBindMeterList(@Param("meterId") Long meterId);
	
	/**
	 * @Title: searchCheckMeterRemindMeters
	 * @Description: 查询需要检测的表计
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String, Object>> searchCheckMeterRemindMeters(@Param("searchCond") String searchCond, @Param("caliber") String caliber, @Param("cycleStatusList") List<Integer> cycleStatusList, @Param("checkMonth") Integer checkMonth);
	
	/**
	 * @Title: getMeterList
	 * @Description: 查询需要检测的表计(地理位置)
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getCheckMeterRemindList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("caliber") String caliber, @Param("cycleStatusList") List<Integer> cycleStatusList, @Param("checkMonth") Integer checkMonth);
	
	
	/**
	 * @Title: getChildListById
	 * @Description: 根据ID查询子节点
	 * @param id
	 * @return 
	 * 		返回地理位置集合（地理位置实体类数据和IS_PARENT属性）
	 */
	public List<MeterBean> getChildListById(@Param("id") Long id);
	
	
	/**
	 * @Title: getMeterMessage
	 * @Description:根据水表钢印号查找水表信息
	 * @param id
	 * @return 
	 * 		根据水表钢印号查找水表信息
	 */
	public Meters getMeterMessage(@Param("steelSealNo") String steelSealNo);
	
	
	/**
	 * @Title: getMeterList
	 * @Description: 根据条件查询为绑定表计()
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getBindBigMeterList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("meterUse") String meterUse, @Param("customerId") Long customerId);
	

	/**
	 * @Title: getMeterList
	 * @Description: 根据条件查询已绑定表计()
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getUnBindBigMeterList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("meterUse") String meterUse, @Param("customerId") Long customerId);
	
	
	/**
	 * @Title: getMeterBindCustomerList
	 * @Description: 获取已绑定的水表
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getMeterBindCustomerList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("searchType") Integer searchType);
	
	/**
	 * @Title: getMetersBindCustomersList
	 * @Description: 条件查询已绑定客户、地理位置的水表
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getMetersBindCustomersList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds);
	
	
	/**
	 * @Title: getExportMeterData
	 * @Description: 导出表计时获取水表数据
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getExportMeterData(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds);
	
	
	public List<Meters> getMeterListByTraceIds(@Param("traceIds") String traceIds, @Param("searchCond") String searchCond);
	
	public List<Meters> getMeterData(@Param("traceIds") String traceIds);
	
	/**
	 * @Title: getVirtualMeterList
	 * @Description: 获取虚表集合
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getVirtualMeterList(@Param("traceIds") String traceIds);
	
	/**
	 * @Title: getRealMeterList
	 * @Description: 配置表计分水量规则时查询表计（只查询实表）
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getRealMeterList(@Param("searchCond") String searchCond,@Param("traceIds") String traceIds,@Param("virtualRealStatus") Integer virtualRealStatus);
	
	/**
	 * @Title: searchRealMeters
	 * @Description:  配置表计分水量规则时查询表计（只查询实表）
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> searchRealMeters(@Param("searchCond") String searchCond, @Param("virtualRealStatus") Integer virtualRealStatus);
	
	
	/**
	 * @Title: getMeterListByCycleStatus
	 * @Description: 根据生命周期状态查询水表
	 * @param searchCond
	 * @param traceIds
	 * @param cycleStaus
	 * @return 
	 */
	public List<Meters> getMeterListByCycleStatus(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("cycleStatusList") List<Integer> cycleStatusList);
	
	/**
	 * @Title: searchMetersByCycleStatus
	 * @Description: 根据生命周期状态查询水表
	 * @param searchCond
	 * @param cycleStaus
	 * @return 
	 */
	public List<Meters> searchMetersByCycleStatus(@Param("searchCond") String searchCond, @Param("cycleStatusList") List<Integer> cycleStatusList);
	
	/**
	 * @Title: getMeterDocList
	 * @Description:获取表计单据信息
	 * @param searchCond
	 * @param traceIds
	 * @param docType
	 * @return 
	 */
	public List<Meters> getMeterDocList(@Param("searchCond") String searchCond, @Param("traceIds") String traceIds, @Param("docType") Integer docType, @Param("cycleStatusList") List<Integer> cycleStatusList);
	
	/**
	 * @Title: getMeterDocList
	 * @Description:获取表计单据信息
	 * @param searchCond
	 * @param traceIds
	 * @param docType
	 * @return 
	 */
	public List<Meters> searchMeterDocList(@Param("searchCond") String searchCond, @Param("docType") Integer docType, @Param("cycleStatusList") List<Integer> cycleStatusList);
	
	/**
	 * @Title: getDefaultMeterList
	 * @Description: 获取默认客户的表计列表
	 * @param traceIds
	 * @param searchCond
	 * @param customerType
	 * @return 
	 */
	public List<Meters> getDefaultMeterList(@Param("traceIds") String traceIds, @Param("searchCond") String searchCond, @Param("customerType") Integer customerType, @Param("waterPrice") String waterPrice);
	
	/**
	 * @Title: getStockMeter
	 * @Description: 查询库存中的表计
	 * @param meterVirtualReal
	 * @param cycleStatus
	 * @return 
	 */
	public List<Map<String, Object>> getStockMeterByFactory(@Param("meterVirtualReal") Integer meterVirtualReal, @Param("cycleStatus") Integer cycleStatus);
}