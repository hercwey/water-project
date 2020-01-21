package com.learnbind.ai.common.enumclass.wateruse;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass.wateruse
 *
 * @Title: EnumJMWater.java
 * @Description: 居民生活用水-阶梯水价
 *
 * @author Thinkpad
 * @date 2019年9月4日 下午4:50:49
 * @version V1.0 
 *
 */
public enum EnumJMWater {
	LADDER_FIRST("LADDER_FIRST_YS", "第一阶梯"),
	LADDER_SECOND("LADDER_SECOND_YS", "第二阶梯"),
	LADDER_THIRD("LADDER_THIRD_YS", "第三阶梯");

	/**
	 * @Fields value：异常类型值，用于数据库保存
	 */
	private String key;
	/**
	 * @Fields name：异常类型名称，用于页面显示
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 AbnormityTypeConstant.
	 * @param value
	 * @param name
	 */
	private EnumJMWater(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	
	/**
	 * @Title: getValue
	 * @Description: 获取异常类型值，用于数据库保存
	 * @return 
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @Title: getName
	 * @Description: 获取异常类型名称，用于页面显示
	 * @return 
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * @Title: getName
	 * @Description: 根据异常类型值获取名称
	 * @param value
	 * @return 
	 */
	public static String getValue(String key) {
		for (EnumDJWater type : EnumDJWater.values()) {
			if (type.getKey() == key) {
				return type.getValue();
			}
		}
		return null;
	}   
	
	
	
}
