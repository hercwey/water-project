package com.learnbind.ai.common.enumclass.tax;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass.tax
 *
 * @Title: EnumTaxQdbz.java
 * @Description: 清单标志
 *
 * @author Thinkpad
 * @date 2019年12月22日 下午5:51:51
 * @version V1.0 
 *
 */
public enum EnumTaxQdbz {

	NO(0, "不开具清单"),
	YES(1, "开具清单");

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
	private EnumTaxQdbz(Integer value, String name) {
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
		for (EnumTaxQdbz type : EnumTaxQdbz.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
