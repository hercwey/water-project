package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumCustomerChargeMode.java
 * @Description: 收费方式（针对一表多户）
 *
 * @author Thinkpad
 * @date 2019年8月29日 下午12:55:57
 * @version V1.0 
 *
 */
public enum EnumCustomerChargeMode {

	/**
	 * @Fields COMBINE_CHARGE：0=合并收费
	 */
	COMBINE_CHARGE(0, "合并收费"),
	/**
	 * @Fields ALONE_CHARGE：1=单独收费
	 */
	ALONE_CHARGE(1, "单独收费");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDeletedStatus.
	 * @param value
	 * @param name
	 */
	private EnumCustomerChargeMode(Integer value, String name) {
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
	public static String getName(Integer value) {
		if(value==null) {
			return null;
		}
		for (EnumCustomerChargeMode type : EnumCustomerChargeMode.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
