package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumMeterCheckResult.java
 * @Description: 检测结果
 *
 * @author Thinkpad
 * @date 2019年10月22日 下午10:46:20
 * @version V1.0 
 *
 */
public enum EnumMeterCheckResult {

	/**
	 * @Fields DELETE_NO：表计状态：正常
	 */
	CHECK_YES(1, "通过"),
	
	/**
	 * @Fields DELETE_YES：表计状态：停水
	 */
	CHECK_NO(2, "未通过");

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
	private EnumMeterCheckResult(Integer value, String name) {
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
		for (EnumMeterCheckResult type : EnumMeterCheckResult.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
