package com.learnbind.ai.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.bean.StatClassifyBean;
import com.learnbind.ai.model.StatClassify;

import tk.mybatis.mapper.common.Mapper;

public interface StatClassifyMapper extends Mapper<StatClassify> {
	
	/**
	 * @Title: getChildListById
	 * @Description: 根据ID查询子节点
	 * @param id
	 * @return 
	 * 		返回统计分类集合（统计分类实体类数据和IS_PARENT属性）
	 */
	public List<StatClassifyBean> getChildListById(@Param("id") Long id);
	
	public List<StatClassify> getChildList(@Param("id") Long id);
	
}