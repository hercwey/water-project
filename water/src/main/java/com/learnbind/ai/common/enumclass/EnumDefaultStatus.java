package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumDefaultStatus.java
 * @Description: 默认状态
 *
 * @author Administrator
 * @date 2019年7月8日 上午10:50:34
 * @version V1.0 
 *
 */
public enum EnumDefaultStatus {

	/**
	 * @Fields DEFAULT_YES：是（默认状态）
	 */
	DEFAULT_YES(0, "是"),
	/**
	 * @Fields DEFAULT_NO：否（非默认状态）
	 */
	DEFAULT_NO(1, "否");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDefaultStatus.
	 * @param value
	 * @param name
	 */
	private EnumDefaultStatus(Integer value, String name) {
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
		for (EnumDefaultStatus type : EnumDefaultStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
