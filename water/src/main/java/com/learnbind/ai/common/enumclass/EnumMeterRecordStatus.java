package com.learnbind.ai.common.enumclass;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumMeterRecordStatus.java
 * @Description: 抄表记录状态
 *
 * @author Thinkpad
 * @date 2019年11月17日 上午11:03:57
 * @version V1.0 
 *
 */
public enum EnumMeterRecordStatus {

	/**
	 * @Fields DEFAULT_YES：是（默认状态）
	 */
	NORMAL(0, "正常"),
	/**
	 * @Fields DEFAULT_NO：否（非默认状态）
	 */
	MANUAL_EDIT(1, "手工修改");

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
	private EnumMeterRecordStatus(Integer value, String name) {
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
		for (EnumMeterRecordStatus type : EnumMeterRecordStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
