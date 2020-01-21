package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumCustomerAccountStatus.java
 * @Description: 客户-账户状态枚举类
 *
 * @author Administrator
 * @date 2019年5月30日 下午6:18:34
 * @version V1.0 
 *
 */
public enum EnumCustomerStatus {

	/**
	 * @Fields ACCOUNT_NO：未立户
	 */
	ACCOUNT_NO(-1, "未立户"),
	/**
	 * @Fields ACCOUNT_OPEN：已立户
	 */
	ACCOUNT_OPEN(0, "已立户"),
	/**
	 * @Fields ACCOUNT_CLOSE：已销户
	 */
	ACCOUNT_CLOSE(1, "已销户"),
	/**
	 * @Fields ACCOUNT_TRANSFER：已过户（需要过户的客户的状态）
	 */
	ACCOUNT_TRANSFER(2, "已过户"),
	/**
	 * @Fields ACCOUNT_TRANSFER：过户（过户后新的客户状态，等同于立户）
	 */
	ACCOUNT_TRANSFER_OPEN(3, "过户");

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
	private EnumCustomerStatus(Integer value, String name) {
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
		for (EnumCustomerStatus type : EnumCustomerStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
