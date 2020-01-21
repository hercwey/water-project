package com.learnbind.ai.common.enumclass;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: BookUserType.java
 * @Description: 显示模式字段(表册用户类型)枚举类
 *
 * @author Administrator
 * @date 2019年11月26日 下午11:59:31
 * @version V1.0 
 *
 */
public enum EnumBookUserType {
	
	/**
	 * @Fields TYPE_SMALL_METER_ONE：1=一户一表（户表）
	 */
	TYPE_SMALL_METER_ONE(1, "1户1表（户表）"),
	
	/**
	 * @Fields TYPE_SMALL_METER_MORE：2=一户多表（户表）
	 */
	TYPE_SMALL_METER_MORE(2, "一户多表（户表）"),
	
	/**
	 * @Fields TYPE_BIG_METER_COMPANY：3=单位大表
	 */
	TYPE_BIG_METER_COMPANY(3, "单位大表"),
	
	/**
	 * @Fields OTHER：4=其他
	 */
	OTHER(4, "其它");
	
	/**
	 * @Fields key：键名称
	 */
	private Integer key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumBookUserType.
	 * @param key
	 * @param value
	 */
	private EnumBookUserType(Integer key, String value) {
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
		for (EnumBookUserType status : EnumBookUserType.values()) {
			if (status.getKey()==key) {
				return status.getValue();
			}
		}
		return null;
	}
	
}
