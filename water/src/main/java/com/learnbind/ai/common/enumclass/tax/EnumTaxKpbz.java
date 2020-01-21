package com.learnbind.ai.common.enumclass.tax;




/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass.tax
 *
 * @Title: EnumTaxKpbz.java
 * @Description:开票标志
 *
 * @author Thinkpad
 * @date 2019年12月22日 下午5:53:24
 * @version V1.0 
 *
 */
public enum EnumTaxKpbz {

	KP("0", "开票"),
	JY("1", "校验");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private String value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDeletedStatus.
	 * @param value
	 * @param name
	 */
	private EnumTaxKpbz(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/**
	 * @Title: getValue
	 * @Description: 获取异常类型值，用于数据库保存
	 * @return 
	 */
	public String getValue() {
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
	public static String getName(String value) {
		for (EnumTaxKpbz type : EnumTaxKpbz.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
