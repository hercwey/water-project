package com.learnbind.ai.service.busoffice;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.BusinessOffice;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.busoffice
 *
 * @Title: BusinessOfficeService.java
 * @Description: 营业网点模块
 *
 * @author Thinkpad
 * @date 2020年2月21日 下午3:07:15
 * @version V1.0 
 *
 */
public interface BusinessOfficeService extends IBaseService<BusinessOffice, Long> {
	
	public List<BusinessOffice> searchCond(String searchCond);
}
