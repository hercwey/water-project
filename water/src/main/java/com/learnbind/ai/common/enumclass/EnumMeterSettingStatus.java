package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumDeleted.java
 * @Description: 表计暂拆/复装状（暂拆、复装）
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:15:26
 * @version V1.0 
 *
 */
public enum EnumMeterSettingStatus {

	/**
	 * @Fields ZERO：表计暂拆/复装状：复装
	 */
	ZERO(0, "复装"),
	
	/**
	 * @Fields ONE：表计暂拆/复装状：暂拆
	 */
	ONE(1, "暂拆");

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
	private EnumMeterSettingStatus(Integer value, String name) {
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
		for (EnumMeterSettingStatus type : EnumMeterSettingStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
