package com.learnbind.ai.service.dict.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.DataDictMapper;
import com.learnbind.ai.model.DataDict;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.dict.DataDictService;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.service.dict.impl
 *
 * @Title: DataDictServiceImpl.java
 * @Description: 数据字典服务实现类
 *
 * @author Administrator
 * @date 2019年5月14日 上午9:52:23
 * @version V1.0 
 *
 */
@Service
public class DataDictServiceImpl extends AbstractBaseService<DataDict, Long> implements  DataDictService {
	
	@Autowired
	public DataDictMapper dataDictMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public DataDictServiceImpl(DataDictMapper mapper) {
		this.dataDictMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<DataDict> getListByTypeCode(String typeCode) {
		DataDict dict = new DataDict();
		dict.setTypeCode(typeCode);
		return dataDictMapper.select(dict);
	}

	@Override
	public List<Map<String, String>> searchDictTypeCond(String searchCond) {
		return dataDictMapper.searchDictTypeCond(searchCond);
	}

	@Override
	public List<Map<String, Object>> searchLabelList(String searchCond) {
		return dataDictMapper.searchLabelList(searchCond);
	}

	@Override
	public DataDict getDataDict(String typeCode, String key) {
		
		DataDict dict = new DataDict();
		dict.setTypeCode(typeCode);
		dict.setKey(key);
		List<DataDict> dictList = dataDictMapper.select(dict);
		if(dictList!=null && dictList.size()>0) {
			return dictList.get(0);
		}
		
		return null;
	}
	
	@Override
	public DataDict getDataDictOne(String typeCode, String value) {
		
		if(StringUtils.isBlank(value)) {
			return null;
		}
		
		DataDict dict = new DataDict();
		dict.setTypeCode(typeCode);
		dict.setValue(value);
		List<DataDict> dictList = dataDictMapper.select(dict);
		if(dictList!=null && dictList.size()>0) {
			return dictList.get(0);
		}
		
		return null;
	}

}
