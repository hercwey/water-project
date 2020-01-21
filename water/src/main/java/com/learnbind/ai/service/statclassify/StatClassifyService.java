package com.learnbind.ai.service.statclassify;

import java.util.List;

import com.learnbind.ai.bean.StatClassifyBean;
import com.learnbind.ai.model.StatClassify;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.statclassify
 *
 * @Title: StatClassifyService.java
 * @Description: 统计分类服务接口类
 *
 * @author Administrator
 * @date 2019年12月27日 下午11:12:27
 * @version V1.0 
 *
 */
public interface StatClassifyService extends IBaseService<StatClassify, Long> {
	
	/**
	 * @Title: getChildListById
	 * @Description: 根据ID查询子节点
	 * @param id
	 * @return 
	 * 		返回统计分类集合（统计分类实体类数据和IS_PARENT属性）
	 */
	public List<StatClassifyBean> getChildListById(Long id);
	
	public List<StatClassify> getChildList(Long id);
	
	/**
	 * @Title: updateDropClassify
	 * @Description: 拖拽后更新
	 * @param classifyId
	 * @param parentClassifyId
	 * @return 
	 */
	public int updateDropClassify(Long classifyId, Long parentClassifyId);
	
}