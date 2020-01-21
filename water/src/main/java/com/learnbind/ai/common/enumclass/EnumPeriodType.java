package com.learnbind.ai.common.enumclass;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumPeriodType.java
 * @Description: 期间类型枚举类
 *
 * @author Administrator
 * @date 2019年12月31日 下午5:38:19
 * @version V1.0 
 *
 */
public enum EnumPeriodType {

	/**
	 * @Fields WATER_FEE：01=水费
	 */
	PERIOD_CURR(1, "本期"),
	/**
	 * @Fields OVERDUE_FINE：02=违约金
	 */
	PERIOD_PAST(2, "往期");
	
	/**
	 * @Fields key：键名称
	 */
	private Integer key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	/**
	 * 创建一个新的实例 EnumAiCreditSubjectAction.
	 * @param key
	 * @param value
	 */
	private EnumPeriodType(Integer key, String value) {
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
		for (EnumPeriodType type : EnumPeriodType.values()) {
			if (type.getKey()==key) {
				return type.getValue();
			}
		}
		return null;
	}  
	
}
