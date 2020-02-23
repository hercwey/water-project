package com.learnbind.ai.service.busoffice;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.model.AddSubWater;
import com.learnbind.ai.model.BusinessOffice;
import com.learnbind.ai.model.SysCheckMeter;
import com.learnbind.ai.model.UsersBusOffice;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.busoffice
 *
 * @Title: UserBusOfficeService.java
 * @Description: 营业网点用户关系表
 *
 * @author Thinkpad
 * @date 2020年2月21日 下午3:11:50
 * @version V1.0 
 *
 */
public interface UserBusOfficeService extends IBaseService<UsersBusOffice, Long> {
	
	public List<UsersBusOffice> searchCond(String searchCond);
	
	/**
	 * @Title: getBusOfficeMessage
	 * @Description: 根据客户ID查询网点信息
	 * @param userId
	 * @return 
	 */
	public UsersBusOffice getBusOfficeMessage(Long userId);
}
