package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumDeleted.java
 * @Description: 删除状态枚举类
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:15:26
 * @version V1.0 
 *
 */
public enum EnumDeletedStatus {

	/**
	 * @Fields DELETE_NO：删除状态：0=未删除
	 */
	DELETE_NO(0, "未删除"),
	/**
	 * @Fields DELETE_YES：删除状态：1=已删除
	 */
	DELETE_YES(1, "已删除");

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
	private EnumDeletedStatus(Integer value, String name) {
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
		for (EnumDeletedStatus type : EnumDeletedStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
