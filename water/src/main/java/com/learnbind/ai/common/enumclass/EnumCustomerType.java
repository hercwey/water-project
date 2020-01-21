package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumCustomerType.java
 * @Description: 客户类型-枚举类
 *
 * @author Administrator
 * @date 2019年9月11日 上午9:31:04
 * @version V1.0 
 *
 */
public enum EnumCustomerType {
	
	/**
	 * @Fields CUSTOMER_PEOPLE：客户类型-个人
	 */
	CUSTOMER_PEOPLE(1, "个人"),
	/**
	 * @Fields CUSTOMER_UNIT：客户类型-单位
	 */
	CUSTOMER_UNIT(2, "单位"),
	/**
	 * @Fields WATER_DIVISION：客户类型-水司
	 */
	WATER_DIVISION(3, "水司");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 AbnormityTypeConstant.
	 * @param value
	 * @param name
	 */
	private EnumCustomerType(Integer value, String name) {
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
		for (EnumCustomerType type : EnumCustomerType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
