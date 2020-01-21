package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumAiDebitCreditStatus.java
 * @Description: 账目借/贷状态枚举类
 *
 * @author Administrator
 * @date 2019年8月20日 下午12:39:13
 * @version V1.0 
 *
 */
public enum EnumAiDebitCreditStatus {

	/**
	 * @Fields DEBIT：借/贷状态：1=借
	 */
	DEBIT("1", "借"),
	/**
	 * @Fields CREDIT：借/贷状态：2=贷
	 */
	CREDIT("2", "贷");

	/**
	 * @Fields key：键名称
	 */
	private String key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiDebitCreditStatus.
	 * @param key
	 * @param value
	 */
	private EnumAiDebitCreditStatus(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @Title: getKey
	 * @Description: 获取键名称
	 * @return 
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @Title: setKey
	 * @Description: 设置键名称
	 * @param key 
	 */
	public void setKey(String key) {
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
	public static String getValue(String key) {
		for (EnumAiDebitCreditStatus status : EnumAiDebitCreditStatus.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status.getValue();
			}
		}
		return null;
	}  
	
}
