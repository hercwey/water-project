package com.learnbind.ai.service.knowlibrary;

import java.util.List;

import com.learnbind.ai.model.KnowLibrary;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.service.knowlibrary
 *
 * @Title: KnowLibraryService.java
 * @Description: 知识库管理
 *
 * @author Thinkpad
 * @date 2019年7月27日 上午9:09:10
 * @version V1.0 
 *
 */
public interface KnowLibraryService extends IBaseService<KnowLibrary, Long> {
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<KnowLibrary> searchKnowLibrary(String searchCond, String knowType);
	
	/**
	 * 根据条件查询
	 * @param searchCond
	 * @return
	 */
	public List<KnowLibrary> searchAllKnowLibrary(String searchCond, String label);
	
	
	/**
	 * 根据文章类型查询文章列表(但不包含文章内容)
	 * @param knowType
	 * @return
	 */
	public List<KnowLibrary> searchArticlebyType(String knowType);
	
}
