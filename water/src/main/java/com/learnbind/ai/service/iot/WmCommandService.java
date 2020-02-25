package com.learnbind.ai.service.iot;

import com.learnbind.ai.model.Meters;
import com.learnbind.ai.model.iot.WmCommand;
import com.learnbind.ai.service.common.IBaseService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.service.iot
 *
 * @Title: WmMeterService.java
 * @Description: 设备下发命令记录服务接口类
 *
 * @author SRD
 * @date 2020年2月22日 下午2:31:31
 * @version V1.0 
 *
 */
public interface WmCommandService extends IBaseService<WmCommand, Long> {
	
	/**
	 * @Title: getWmCommand
	 * @Description: 根据IOT设备ID获取命令对象
	 * @param meter
	 * @param commandType	指令类型 1=水表配置指令；2=开/关阀指令；3=水量阀值指令；4=读月冻结指令；5=读表配置指令
	 * @param commandVal
	 * @return 
	 */
	public WmCommand getWmCommand(Meters meter, Integer commandType, String commandVal);
	
}
