package com.learnbind.ai.common.enumclass.tax;

import org.apache.commons.lang3.StringUtils;

import com.learnbind.ai.common.enumclass.accountitem.EnumAiCreditSubjectAction;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass.tax
 *
 * @Title: EnumTaxFpzl.java
 * @Description: 发票种类
 *
 * @author Thinkpad
 * @date 2019年12月19日 下午5:53:04
 * @version V1.0 
 *
 */
/* 0：专用发票 2：普通发票12：机动车票51：电子发票 */
public enum EnumTaxFpzl {

	ZYFP("0", "专用发票"),
	PTFP("2", "普通发票"),
	JDCP("12", "机动车票"),
	DZFP("51", "电子发票");

	/**
	 * @Fields key：键名称
	 */
	private String key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiCreditSubjectAction.
	 * @param key
	 * @param value
	 */
	private EnumTaxFpzl(String key, String value) {
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
	
	public static String getValue(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		for (EnumTaxFpzl status : EnumTaxFpzl.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status.getValue();
			}
		}
		return null;
	}
	
}
