package com.learnbind.ai.common.enumclass;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumNotifyUseLocation.java
 * @Description: 通知单查询是否使用地理位置
 *
 * @author Thinkpad
 * @date 2019年12月8日 上午9:35:27
 * @version V1.0 
 *
 */
public enum EnumNotifyUseLocation {

	LOCATION_NO(0, "不使用"),
	LOCATION_YES(1, "使用");

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
	private EnumNotifyUseLocation(Integer value, String name) {
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
		for (EnumNotifyUseLocation type : EnumNotifyUseLocation.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
