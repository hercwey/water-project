package com.learnbind.ai.common.enumclass.wateruse;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass.wateruse
 *
 * @Title: EnumFJMWater.java
 * @Description: 非居民用水
 *
 * @author Thinkpad
 * @date 2019年9月4日 下午4:41:05
 * @version V1.0 
 *
 */
public enum EnumFJMWater {

	JG_WATER("JG_YS", "机关用水"),
	XX_WATER("XX_YS", "学校用水"),
	YL_WATER("YL_YS", "园林用水"),
	YS_WATER("YS_YS", "饮食用水"),
	QY_WATER("QY_YS", "企业用水"),
	SG_WATER("SG_YS", "施工用水"),
	SYZJ_WATER("SYZJ_YS", "宋营驻军");

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
	private EnumFJMWater(String key, String value) {
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
