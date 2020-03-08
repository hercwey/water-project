package com.learnbind.ai.service.iot.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.dao.WmCommandMapper;
import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.WmCommand;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.iot.WmCommandService;
import com.learnbind.ai.service.meters.MetersService;

import tk.mybatis.mapper.entity.Example;

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
	public WmCommand getWmCommand(Meters meter, Integer commandType, Integer commandSequence) {
		
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
//		JSONObject methodParamsOjb = new JSONObject();
//		methodParamsOjb.put("value", commandVal);
//		
//		String methodParams = JSON.toJSONString(methodParamsOjb);
		
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
		cmdObj.setMethodParams(null);
		cmdObj.setOperatorId(userBean.getId());
		cmdObj.setRemark(null);
		cmdObj.setServiceId(1l);
		cmdObj.setStatus(0);
		cmdObj.setCommandSequence(commandSequence);
		
		return cmdObj;
	}

	@Override
	public int updateWmCommandStatus(Long deviceId, Integer commandSequence, int status) {
		
		Example example = new Example(WmCommand.class);
		example.createCriteria().andEqualTo("deviceId", deviceId).andEqualTo("commandSequence", commandSequence);
		example.setOrderByClause(" ID DESC");
		List<WmCommand> commandList = wmCommandMapper.selectByExample(example);
		if(commandList!=null && commandList.size()>0) {
			WmCommand command = commandList.get(0);
			
			WmCommand updateObj = new WmCommand();
			updateObj.setId(command.getId());
			updateObj.setStatus(status);
			return wmCommandMapper.updateByPrimaryKeySelective(updateObj);
		}
		return 0;
	}

}
