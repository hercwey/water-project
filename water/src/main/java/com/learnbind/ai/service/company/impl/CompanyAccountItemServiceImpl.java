package com.learnbind.ai.service.company.impl;

import org.springframework.stereotype.Service;

import com.learnbind.ai.dao.CompanyAccountItemMapper;
import com.learnbind.ai.model.CompanyAccountItem;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.company.CompanyAccountItemService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.company.impl
 *
 * @Title: CompanyAccountItemServiceImpl.java
 * @Description: 公司账目service实现类
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:04:04
 * @version V1.0 
 *
 */
@Service
public class CompanyAccountItemServiceImpl extends AbstractBaseService<CompanyAccountItem, Long> implements CompanyAccountItemService {

	public CompanyAccountItemMapper companyAccountItemMapper;

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
	public CompanyAccountItemServiceImpl(CompanyAccountItemMapper mapper) {
		this.companyAccountItemMapper = mapper;
		this.setMapper(mapper);
	}

}
