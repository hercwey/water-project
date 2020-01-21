package com.learnbind.ai.service.knowlibrary.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.KnowLibraryMapper;
import com.learnbind.ai.model.KnowLibrary;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.knowlibrary.KnowLibraryService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.knowlibrary.impl
 *
 * @Title: KnowLibraryServiceImpl.java
 * @Description: 知识库管理
 *
 * @author Thinkpad
 * @date 2019年7月27日 上午9:10:36
 * @version V1.0 
 *
 */
@Service
public class KnowLibraryServiceImpl extends AbstractBaseService<KnowLibrary, Long> implements  KnowLibraryService {
	
	@Autowired
	public KnowLibraryMapper knowLibraryMapper;
		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public KnowLibraryServiceImpl(KnowLibraryMapper mapper) {
		this.knowLibraryMapper=mapper;
		this.setMapper(mapper);
	}

	@Override
	public List<KnowLibrary> searchKnowLibrary(String searchCond, String knowType) {
		return knowLibraryMapper.searchKnowLibrary(searchCond,knowType);
	}

	@Override
	public List<KnowLibrary> searchArticlebyType(String knowType) {
		return knowLibraryMapper.searchArticlebyType(knowType);
	}
	
	@Override
	public List<KnowLibrary> searchAllKnowLibrary(String searchCond, String label) {
		return knowLibraryMapper.searchAllKnowLibrary(searchCond,label);
	}

}
