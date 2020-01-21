package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumElectronicArchiveType.java
 * @Description: 电子档案类型-枚举类
 *
 * @author Administrator
 * @date 2019年8月6日 上午11:38:48
 * @version V1.0 
 *
 */
public enum EnumElectronicArchiveType {

	/**
	 * @Fields ACCOUNT_OPEN：电子档案类型-1=立户单
	 */
	ACCOUNT_OPEN(1, "立户单"),
	/**
	 * @Fields ACCOUNT_TRANSFER：电子档案类型-2=过户单
	 */
	ACCOUNT_TRANSFER(2, "过户单"),
	/**
	 * @Fields ACCOUNT_CLOSE：电子档案类型-3=销户单
	 */
	ACCOUNT_CLOSE(3, "销户单"),
	/**
	 * @Fields ACCOUNT_RECOVER：电子档案类型-4=销户恢复单
	 */
	ACCOUNT_RECOVER(4, "销户恢复单"),
	/**
	 * @Fields WATER_USE：电子档案类型-5=用水性质调整单
	 */
	WATER_USE(5, "用水性质调整单"),
	/**
	 * @Fields PEOPLE_ADJUST：电子档案类型-6=人口调整单
	 */
	PEOPLE_ADJUST(6, "人口调整单"),
	/**
	 * @Fields RENAME：电子档案类型-7=更名单
	 */
	RENAME(7, "更名单"),
	/**
	 * @Fields AGREEMENT_WATER：电子档案类型-8=供水协议
	 */
	AGREEMENT_WATER(8, "供水协议"),
	/**
	 * @Fields AGREEMENT_WITHHOLD：电子档案类型-9=代扣协议
	 */
	AGREEMENT_WITHHOLD(9, "代扣协议");

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
	private EnumElectronicArchiveType(Integer value, String name) {
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
		for (EnumElectronicArchiveType type : EnumElectronicArchiveType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
