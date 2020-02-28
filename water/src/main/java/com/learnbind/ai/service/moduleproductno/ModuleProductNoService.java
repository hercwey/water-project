package com.learnbind.ai.service.moduleproductno;

import java.util.Date;
import java.util.List;

import com.learnbind.ai.model.ModuleProductNo;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.moduleproductno
 *
 * @Title: ModuleProductNoService.java
 * @Description: 模组号-出厂编号对照表服务接口类
 *
 * @author SRD
 * @date 2020年2月27日 下午5:34:48
 * @version V1.0 
 *
 */
public interface ModuleProductNoService extends IBaseService<ModuleProductNo, Long> {
	
	/**
	 * @Title: searchList
	 * @Description: 查询
	 * @param operatorName
	 * @param operatorDate
	 * @param moduleNo
	 * @param productNo
	 * @return 
	 */
	public List<ModuleProductNo> searchList(String operatorName, Date operatorDate, String moduleNo, String productNo);
	
}
