package com.learnbind.ai.service.iot.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.dao.WmCommandMapper;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.WmCommand;
import com.learnbind.ai.model.iot.WmDevice;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.iot.WmCommandService;
import com.learnbind.ai.service.meters.MetersService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.iot.impl
 *
 * @Title: WmMeterServiceImpl.java
 * @Description: 设备上报数据服务实现类
 *
 * @author SRD
 * @date 2020年2月23日 下午2:33:19
 * @version V1.0 
 *
 */
@Service
public class WmCommandServiceImpl extends AbstractBaseService<WmCommand, Long> implements WmCommandService {

	public WmCommandMapper wmCommandMapper;

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
	public WmCommandServiceImpl(WmCommandMapper mapper) {
		this.wmCommandMapper = mapper;
		this.setMapper(mapper);
	}

	@Autowired
	private MetersService metersService;//表计
	
	@Override
	public WmCommand getWmCommand(Meters meter, Integer commandType, String commandVal) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		JSONObject methodParamsOjb = new JSONObject();
		methodParamsOjb.put("value", commandVal);
		
		String methodParams = JSON.toJSONString(methodParamsOjb);
		
		WmCommand cmdObj = new WmCommand();
		cmdObj.setAppId(null);
		cmdObj.setCommandId(null);
		cmdObj.setCommandType(commandType);
		cmdObj.setCreateTime(new Date());
		cmdObj.setDeviceId(meter.getId());
		cmdObj.setExpireTime(0l);
		cmdObj.setId(null);
		cmdObj.setIssuedTimes(0);
		cmdObj.setMethodId(null);
		cmdObj.setMethodParams(methodParams);
		cmdObj.setOperatorId(userBean.getId());
		cmdObj.setRemark(null);
		cmdObj.setServiceId(1l);
		cmdObj.setStatus(0);
		
		return cmdObj;
	}

}
