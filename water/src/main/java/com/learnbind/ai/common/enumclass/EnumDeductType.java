package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumIdType.java
 * @Description: 扣费方式 1=其他；2=建行自动扣费；3=民生银行自动扣费-枚举类
 *
 * @author Thinkpad
 * @date 2019年5月16日 下午4:53:24
 * @version V1.0 
 *
 */
public enum EnumDeductType {
	
	/**
	 * @Fields 其他
	 */
	OTHER(1, "其他"),
	/**
	 * @Fields CCB：中国建设银行自动扣费
	 */
	CCB(2, "中国建设银行自动扣费"),
	/**
	 * @Fields CMBC：中国民生银行自动扣费
	 */
	CMBC(3, "中国民生银行自动扣费"),
	
	/**
	 * @Fields NO_CARD：无卡用户扣预存水费
	 */
	NO_CARD(4, "无卡用户扣预存水费");

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
	private EnumDeductType(Integer value, String name) {
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
		for (EnumDeductType type : EnumDeductType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
