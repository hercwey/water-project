package com.learnbind.ai.common.enumclass;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumAbnormityType.java
 * @Description: 异常报警类型枚举类
 *
 * @author Administrator
 * @date 2019年5月15日 下午5:37:30
 * @version V1.0 
 *
 */
public enum EnumAbnormityType {

	ABNORMITY_TYPE_FIXED(1, "固定值"),
	ABNORMITY_TYPE_AVG(2, "平均值");

	/**
	 * @Fields value：异常类型值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：异常类型名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 AbnormityTypeConstant.
	 * @param value
	 * @param name
	 */
	private EnumAbnormityType(Integer value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/**
	 * @Title: getValue
	 * @Description: 获取异常类型值，用于数据库保存
	 * @return 
	 */
	public Integer getValue() {
		return value;
	}
	
	/**
	 * @Title: getName
	 * @Description: 获取异常类型名称，用于页面显示
	 * @return 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @Title: getName
	 * @Description: 根据异常类型值获取名称
	 * @param value
	 * @return 
	 */
	public static String getName(int value) {
		for (EnumAbnormityType type : EnumAbnormityType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
	
	
}
