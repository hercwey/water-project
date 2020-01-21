package com.learnbind.ai.service.meters;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.MeterBean;
import com.learnbind.ai.model.CustomerMeter;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface MetersService extends IBaseService<Meters, Long> {
	
	/**
	 * @Title: transferMeter
	 * @Description: 换表
	 * @param oldMeterId
	 * @param newMeterId
	 * @return 
	 */
	public int transferMeter(Long oldMeterId, Long newMeterId);

	/**
	 * @Title: getListBySearchCond
	 * @Description: 根据条件查询
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getListBySearchCond(String searchCond);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<Meters> searchMeters(String searchCond);
	
	/**
	 * @Title: searchChangeMeters
	 * @Description: 获取可以换表的表计集合（只查询待用状态的表计信息）
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> searchChangeMeters(String searchCond);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @param searchCond
	 * @param status
	 * @return 
	 */
	public List<Meters> searchRemoveMeters(String searchCond, Integer status);
	
	/**
	 * @Title: getMeterList
	 * @Description: 根据条件查询表计
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getMeterList(String searchCond, String traceIds);
	
	/**
	 * @Title: getChangeMeterList
	 * @Description: 获取可以换表的表计集合（只获取待用状态的表计信息）
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getChangeMeterList(String searchCond, String traceIds);
	
	/**
	 * @Title: getMeterList
	 * @Description: 根据条件查询表计
	 * @param searchCond
	 * @param traceIds
	 * @param status
	 * @return 
	 */
	public List<Meters> getRemoveMeterList(String searchCond, String traceIds, Integer status);
	
    /**
     * @Title: insertMeters
     * @Description: 增加表计,并增加表计与地理位置的关系
     * @param meter
     * @param locationId
     * @return 
     */
    public int insertMeters(Meters meter, Long locationId);
    
    /**
     * 修改
     * @param Meters
     * @return
     */
    public int updateMeters(Meters meter, Long prevLocationId, Long locationId);
    
    /**
     * 删除
     * @param MetersId
     * @return
     */
    public int deleteMeters(long metersId);
    /**
     * 批量删除
     * @param meterIds
     * @return
     */
    public int deleteMeters(List<Long> meterIds);
    
    /** 
    	* @Title: findAllMeters
    	* @Description: 查询 
    	* @param @param pageNum
    	* @param @param pageSize
    	* @param @return     
    	* @return PageInfo<UserDomain>    返回类型 
    	* @throws 
    */
    PageInfo<Meters> findAllMeters(int pageNum, int pageSize);
    
    /**
	 * @Title: updateListById
	 * @Description: 批量更新
	 * @param meterList
	 * @return 
	 */
	public int updateListById(List<Meters> meterList);
	
	/**
     * @Title: updateMeterStatus
     * @Description: 修改水表的用水状态
     * @param meterId
     * @param meterStatus
     * @return 
     */
    public int updateMeterStatus(Long meterId, Integer meterStatus);
    

	/**
	 * @Title: getMeterListByLocationId
	 * @Description: 根据地理位置id查询表计信息
	 * @param locationId
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getMeterListByLocationId(Long locationId, String searchCond);
	
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
	public List<Map<String, Object>> getBindMeterList(Long meterId);
	
    /**
     * @Title: destoryMeters
     * @Description: 销户拆表
     * @param meterId
     * @return 
     */
    public int destoryMeters(long meterId);
    
    /**
     * @Title: updateDestoryStatus
     * @Description: 修改表计状态（暂拆/复装）
     * @param meterId
     * @param status
     * @return 
     */
    public int updateRemoveStatus(Long meterId, Integer status);
    
	/**
	 * @Title: searchCheckMeterRemindMeters
	 * @Description: 查询需要检测的表计
	 * @param searchCond
	 * @return 
	 */
	public List<Map<String, Object>> searchCheckMeterRemindMeters(String searchCond, String caliber, List<Integer> cycleStatusList, Integer checkMonth);
	
	/**
	 * @Title: getMeterList
	 * @Description: 查询需要检测的表计(地理位置)
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getCheckMeterRemindList(String searchCond, String traceIds, String caliber, List<Integer> cycleStatusList, Integer checkMonth);
	
	/**
	 * @Title: destoryMeter
	 * @Description: 销户拆表
	 * @param cmList
	 * @return 
	 */
	public int destoryMeter(List<CustomerMeter> cmList);
	
	/**
	 * @Title: changeMeter
	 * @Description: 换表
	 * @param oldMeterId
	 * @param oldMeterRead
	 * @param newMeterId
	 * @param newMeterRead
	 * @return 
	 */
	public int changeMeter(Long oldMeterId, String oldMeterRead, Long newMeterId, String newMeterRead);
	
	
	/**
	 * @Title: getChildListById
	 * @Description: 根据ID查询子节点
	 * @param id
	 * @return 
	 * 		返回地理位置集合（地理位置实体类数据和IS_PARENT属性）
	 */
	public List<MeterBean> getChildListById(Long id);
	
	
	/**
	 * @Title: getMeterMessage
	 * @Description:根据水表钢印号查找水表信息
	 * @param id
	 * @return 
	 * 		根据水表钢印号查找水表信息
	 */
	public Meters getMeterMessage(String steelSealNo);
	
	
	/**
	 * @Title: getMeterList
	 * @Description: 根据条件查询未绑定表计()
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getBindBigMeterList(String searchCond, String traceIds, String meterUse, Long customerId);
	
	/**
	 * @Title: getMeterList
	 * @Description: 根据条件查询已绑定表计()
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getUnBindBigMeterList(String searchCond, String traceIds, String meterUse, Long customerId);
	
	
	/**
	 * @Title: updateCycleStatus
	 * @Description: 更新水表生命周期状态
	 * @param meterId
	 * @param cycleStatus
	 * @return 
	 */
	public int updateCycleStatus(Long meterId, Integer cycleStatus);
	
	
	/**
	 * @Title: getMeterBindCustomerList
	 * @Description: 获取已绑定客户的水表
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getMeterBindCustomerList(String searchCond, String traceIds, Integer searchType);
	
	/**
	 * @Title: getMetersBindCustomersList
	 * @Description: 条件查询已绑定客户、地理位置的水表
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getMetersBindCustomersList(String searchCond, String traceIds);
	
	/**
	 * @Title: getExportMeterData
	 * @Description: 导出表计时获取水表数据
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Map<String, Object>> getExportMeterData(String searchCond, String traceIds);
	
	
	/**
	 * @Title: getMeterListByTraceIds
	 * @Description: 根据traceIds
	 * @param traceIds
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> getMeterListByTraceIds(String traceIds, String searchCond);
	
	
	public List<Meters> getMeterData(String traceIds);
	
	/**
	 * @Title: getVirtualMeter
	 * @Description: 查询虚表
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getVirtualMeterList(String traceIds);
	
	/**
	 * @Title: getRealMeterList
	 * @Description: 配置表计分水量规则时查询表计（只查询实表）
	 * @param searchCond
	 * @param traceIds
	 * @return 
	 */
	public List<Meters> getRealMeterList(String searchCond, String traceIds, Integer virtualRealStatus);
	
	/**
	 * @Title: searchRealMeters
	 * @Description:  配置表计分水量规则时查询表计（只查询实表）
	 * @param searchCond
	 * @return 
	 */
	public List<Meters> searchRealMeters(String searchCond, Integer virtualRealStatus);
	
	
	/**
	 * @Title: getMeterListByCycleStatus
	 * @Description: 根据生命周期状态查询水表
	 * @param searchCond
	 * @param traceIds
	 * @param cycleStaus
	 * @return 
	 */
	public List<Meters> getMeterListByCycleStatus(String searchCond, String traceIds, List<Integer> cycleStatusList);
	
	/**
	 * @Title: searchMetersByCycleStatus
	 * @Description: 根据生命周期状态查询水表
	 * @param searchCond
	 * @param cycleStaus
	 * @return 
	 */
	public List<Meters> searchMetersByCycleStatus(String searchCond, List<Integer> cycleStatusList);
	
	
	/**
	 * @Title: getMeterDocList
	 * @Description:获取表计单据信息
	 * @param searchCond
	 * @param traceIds
	 * @param docType
	 * @return 
	 */
	public List<Meters> getMeterDocList(String searchCond, String traceIds, Integer docType, List<Integer> cycleStatusList);
	
	
	/**
	 * @Title: getMeterDocList
	 * @Description:获取表计单据信息
	 * @param searchCond
	 * @param traceIds
	 * @param docType
	 * @return 
	 */
	public List<Meters> searchMeterDocList(String searchCond, Integer docType, List<Integer> cycleStatusList);
	
	/**
	 * @Title: getDefaultMeterList
	 * @Description: 获取默认客户的表计列表
	 * @param traceIds
	 * @param searchCond
	 * @param customerType
	 * @return 
	 */
	public List<Meters> getDefaultMeterList(String traceIds, String searchCond, Integer customerType, String waterPrice);
    
	/**
	 * @Title: getStockMeter
	 * @Description: 查询库存中的表计
	 * @param meterVirtualReal
	 * @param cycleStatus
	 * @return 
	 */
	public List<Map<String, Object>> getStockMeterByFactory(Integer meterVirtualReal, Integer cycleStatus);
}
