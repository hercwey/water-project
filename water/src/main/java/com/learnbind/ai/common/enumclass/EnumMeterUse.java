package com.learnbind.ai.common.enumclass;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumMeterUse.java
 * @Description: 表计用途-枚举类
 *
 * @author Administrator
 * @date 2019年9月1日 下午6:09:16
 * @version V1.0 
 *
 */
public enum EnumMeterUse {

	/**
	 * @Fields CHARGE_METER：计费表
	 */
	CHARGE_METER("CHARGE_METER", "计费表"),
	/**
	 * @Fields NO_CHARGE_METER：非计费表
	 */
	NO_CHARGE_METER("NO_CHARGE_METER", "非计费表"),
	/**
	 * @Fields MEASUREMENT_METER：计量表
	 */
	MEASUREMENT_METER("MEASUREMENT_METER", "计量表");
	

	/**
	 * @Fields key：键名称
	 */
	private String key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiCreditSubjectAction.
	 * @param key
	 * @param value
	 */
	private EnumMeterUse(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @Title: getKey
	 * @Description: 获取键名称
	 * @return 
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @Title: setKey
	 * @Description: 设置键名称
	 * @param key 
	 */
	public void setKey(String key) {
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
	public static String getValue(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		for (EnumMeterUse status : EnumMeterUse.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status.getValue();
			}
		}
		return null;
	}  
	
}
