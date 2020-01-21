package com.learnbind.ai.service.statclassify.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.learnbind.ai.bean.StatClassifyBean;
import com.learnbind.ai.dao.StatClassifyMapper;
import com.learnbind.ai.model.Location;
import com.learnbind.ai.model.StatClassify;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.statclassify.StatClassifyService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.statclassify.impl
 *
 * @Title: StatClassifyServiceImpl.java
 * @Description: 统计分类服务实现类
 *
 * @author Administrator
 * @date 2019年12月27日 下午11:13:52
 * @version V1.0 
 *
 */
@Service
public class StatClassifyServiceImpl extends AbstractBaseService<StatClassify, Long> implements StatClassifyService {

	public StatClassifyMapper statClassifyMapper;
	/**
	 * <p>
	 * Title:采用构造函数注入
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param mapper
	 */
	public StatClassifyServiceImpl(StatClassifyMapper mapper) {
		this.statClassifyMapper = mapper;
		this.setMapper(mapper);
	}
	
	@Override
	public List<StatClassifyBean> getChildListById(Long id) {
		return statClassifyMapper.getChildListById(id);
	}

	@Override
	public List<StatClassify> getChildList(Long id) {
		return statClassifyMapper.getChildList(id);
	}

	@Override
	public int updateDropClassify(Long classifyId, Long parentClassifyId) {
		
		StatClassify classify = statClassifyMapper.selectByPrimaryKey(classifyId);
		classify.setPid(parentClassifyId);
		return statClassifyMapper.updateByPrimaryKeySelective(classify);
	
	}

}