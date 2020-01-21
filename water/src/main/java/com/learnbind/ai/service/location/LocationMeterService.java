package com.learnbind.ai.service.location;

import java.util.List;

import com.learnbind.ai.model.LocationMeter;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.location
 *
 * @Title: LocationMeterService.java
 * @Description: 地理位置-表计关系服务接口类
 *
 * @author Administrator
 * @date 2019年5月23日 上午9:58:40
 * @version V1.0 
 *
 */
public interface LocationMeterService extends IBaseService<LocationMeter, Long> {
	
	/**
	 * @Title: bind
	 * @Description: 建立地理位置与表计绑定关系
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	public int bind(Long locationId, String traceIds, Long meterId);
	
	/**
	 * @Title: unbind
	 * @Description: 解除地理位置与表计绑定关系
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	public int unbind(Long locationId, Long meterId);
	
	/**
	 * @Title: isBinding
	 * @Description: 验证地理位置与表计是否已绑定
	 * @param locationId
	 * @param meterId
	 * @return 
	 * 		true：已绑定；false：未绑定
	 */
	public boolean isBinding(Long locationId, Long meterId);
	
	/**
	 * @Title: isBinding
	 * @Description: 验证表计是否已绑定
	 * @param locationId
	 * @param meterId
	 * @return 
	 * 		true：已绑定；false：未绑定
	 */
	public boolean isBinding(Long meterId);
	
	/**
	 * @Title: insertByLocationIdAndMeterId
	 * @Description: 根据地理位置ID和表计ID增加，其他属性在实现类中设置
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	public int insertByLocationIdAndMeterId(Long locationId, String traceIds, Long meterId);
	
	/**
	 * @Title: deleteByLocationIdAndMeterId
	 * @Description: 根据地理位置ID和表计ID删除，逻辑删除
	 * @param locationId
	 * @param meterId
	 * @return 
	 */
	public int deleteByLocationIdAndMeterId(Long locationId, Long meterId);
	
	/**
	 * @Title: getMeterIdListByLocationId
	 * @Description: 根据地理位置ID查询表计ID集合
	 * @param locationId
	 * @return 
	 */
	public List<Long> getMeterIdListByLocationId(Long locationId);
	
	/**
	 * @Title: getListByMeterIdList
	 * @Description: 根据表计ID集合查询地理位置-表计
	 * @param meterIdList
	 * @return 
	 */
	public List<LocationMeter> getListByMeterIdList(List<Long> meterIdList);
	
	/**
	 * @param meterId
	 * @return
	 * 			根据表计id查询地理位置
	 */
	public LocationMeter getLocationByMeterId(Long meterId);
	
	/**
	 * @Title: updateTraceIds
	 * @Description: 更新地理位置表计表的traceIds
	 * @return 
	 */
	public int updateTraceIds();
	
}
