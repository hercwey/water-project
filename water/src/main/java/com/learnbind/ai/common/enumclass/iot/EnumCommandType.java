package com.learnbind.ai.common.enumclass.iot;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass.iot
 *
 * @Title: EnumCommandType.java
 * @Description: 下发指令类型-枚举类
 *
 * @author SRD
 * @date 2020年2月11日 上午1:25:25
 * @version V1.0 
 *
 */
public enum EnumCommandType {

	//指令类型：0=未知指令（默认值）；1=表配置指令；2=开/关阀指令；3=设置水量阀值指令；4=读月冻结指令；5=读表配置指令；
	/**
	 * @Fields TYPE_OTHER：0=未知指令
	 */
	TYPE_OTHER(0, "未知指令"),
	/**
	 * @Fields TYPE_METER_CONFIG：1=表配置指令
	 */
	TYPE_METER_CONFIG(1, "表配置指令"),
	/**
	 * @Fields TYPE_OPEN_CLOSE：2=开/关阀指令
	 */
	TYPE_OPEN_CLOSE(2, "开/关阀指令"),
	/**
	 * @Fields TYPE_WATER_AMOUNT：设置水量阀值指令
	 */
	TYPE_WATER_AMOUNT(3, "设置水量阀值指令"),
	/**
	 * @Fields TYPE_READ_MONTH_FREEZE：4=读月冻结指令
	 */
	TYPE_READ_MONTH_FREEZE(4, "读月冻结指令"),
	/**
	 * @Fields TYPE_READ_METER_CONFIG：5=读表配置指令
	 */
	TYPE_READ_METER_CONFIG(5, "读表配置指令");
	
	/**
	 * @Fields key：键名称
	 */
	private Integer key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiDebitSubjectAction.
	 * @param key
	 * @param value
	 */
	private EnumCommandType(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @Title: getKey
	 * @Description: 获取键名称
	 * @return 
	 */
	public Integer getKey() {
		return key;
	}

	/**
	 * @Title: setKey
	 * @Description: 设置键名称
	 * @param key 
	 */
	public void setKey(Integer key) {
		this.key = key;
	}

	/**
	 * @Title: getValue
	 * @Description: 获取键值
	 * @return 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @Title: setValue
	 * @Description: 设置键值
	 * @param value 
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @Title: getValue
	 * @Description: 根据键名称获取键值
	 * @param key
	 * @return 
	 */
	public static String getValue(Integer key) {
		if(key==null) {
			return null;
		}
		for (EnumCommandType status : EnumCommandType.values()) {
			if (status.getKey()==key) {
				return status.getValue();
			}
		}
		return null;
	}
	
}
