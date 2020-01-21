package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumDeleted.java
 * @Description: 协议类型枚举类
 *
 * @author Administrator
 * @date 2019年5月18日 上午10:15:26
 * @version V1.0 
 *
 */
public enum EnumCustomerAgreementType {

	OPEN_ACCOUNT(0, "立户单"),
	
	WATER_SUPPLY(1, "供水协议"),
	
	DUCT(2, "代扣协议"),
	
	TRANSFER_ACCOUNT(3, "过户单"),
	
	DESTROY_ACCOUNT(4, "销户单"),
	
	RECOVER_ACCOUNT(5, "销户恢复单"),
	
	UPDATE_WATER_USE(6, "用水性质调整单"),
	
	PROPLE_ADJUST(7, "人口调整单"),
	
	RENAME(8, "更名单");

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
	private EnumCustomerAgreementType(Integer value, String name) {
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
		for (EnumCustomerAgreementType type : EnumCustomerAgreementType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
