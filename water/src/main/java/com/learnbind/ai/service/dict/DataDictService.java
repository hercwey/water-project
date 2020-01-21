package com.learnbind.ai.service.dict;

import java.util.List;
import java.util.Map;

import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.dict
 *
 * @Title: DataDictService.java
 * @Description: 数据字典服务接口类
 *
 * @author Administrator
 * @date 2019年5月14日 上午9:51:15
 * @version V1.0 
 *
 */
public interface DataDictService extends IBaseService<DataDict, Long> {
	
	/**
	 * @Title: getListByTypeCode
	 * @Description: 根据字典类型查询数据字典
	 * @param typeCode
	 * @return 
	 */
	public List<DataDict> getListByTypeCode(String typeCode);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<Map<String, String>> searchDictTypeCond(String searchCond);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<Map<String, Object>> searchLabelList(String searchCond);
	
	/**
	 * @Title: getDataDict
	 * @Description: 根据类型和key查询数据字典
	 * @param typeCode
	 * @param key
	 * @return 
	 */
	public DataDict getDataDict(String typeCode, String key);
	public DataDict getDataDictOne(String typeCode, String value);
	
}
