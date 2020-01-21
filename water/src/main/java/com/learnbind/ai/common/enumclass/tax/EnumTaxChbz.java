package com.learnbind.ai.common.enumclass.tax;

 
/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass.tax
 *
 * @Title: EnumTaxChbz.java
 * @Description: 冲红标志
 *
 * @author Thinkpad
 * @date 2019年12月22日 下午5:56:51
 * @version V1.0 
 *
 */
public enum EnumTaxChbz {

	WCH(0, "未冲红"),
	YCH(1, "已冲红");

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
	private EnumTaxChbz(Integer value, String name) {
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
		for (EnumTaxChbz type : EnumTaxChbz.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
