package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumIdType.java
 * @Description: 抄表类型-枚举类
 *
 * @author Thinkpad
 * @date 2019年5月16日 下午4:53:24
 * @version V1.0 
 *
 */
public enum EnumReadType {
	
	/**
	 * @Fields NORMAL_READ：抄表类型-正常抄表
	 */
	NORMAL_READ(0, "正常抄表"),
	/**
	 * @Fields PREDIT_READ：抄表类型-预抄
	 */
	PREDIT_READ(2, "预抄"),
	/**
	 * @Fields ESTIMATE_READ：抄表类型-估抄
	 */
	ESTIMATE_READ(1, "估抄"),
	
	/**
	 * @Fields INIT_READ：抄表类型-初始化抄表记录
	 */
	INIT_READ(3, "初始化抄表");
	

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
	private EnumReadType(Integer value, String name) {
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
		for (EnumReadType type : EnumReadType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
