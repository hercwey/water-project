package com.learnbind.ai.common.enumclass.accountitem;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass.accountitem
 *
 * @Title: EnumPaymentType.java
 * @Description: 缴费类型-枚举类
 *
 * @author Administrator
 * @date 2020年1月8日 上午9:04:08
 * @version V1.0 
 *
 */
public enum EnumPaymentType {

	/**
	 * @Fields PAYMENT_RECHARGE：1=充值缴费
	 */
	PAYMENT_RECHARGE(1, "充值缴费"),
	/**
	 * @Fields PAYMENT_DEBT：2=清欠缴费
	 */
	PAYMENT_DEBT(2, "清欠缴费");
	
	/**
	 * @Fields key：键名称
	 */
	private Integer key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiDebitSubjectAction.
	 * @param key
	 * @param value
	 */
	private EnumPaymentType(Integer key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @Title: getKey
	 * @Description: 获取键名称
	 * @return 
	 */
	public Integer getKey() {
		return key;
	}

	/**
	 * @Title: setKey
	 * @Description: 设置键名称
	 * @param key 
	 */
	public void setKey(Integer key) {
		this.key = key;
	}

	/**
	 * @Title: getValue
	 * @Description: 获取键值
	 * @return 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @Title: setValue
	 * @Description: 设置键值
	 * @param value 
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @Title: getValue
	 * @Description: 根据键名称获取键值
	 * @param key
	 * @return 
	 */
	public static String getValue(Integer key) {
		if(key==null) {
			return null;
		}
		for (EnumPaymentType status : EnumPaymentType.values()) {
			if (status.getKey()==key) {
				return status.getValue();
			}
		}
		return null;
	}
	
}
