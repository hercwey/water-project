package com.learnbind.ai.common.enumclass;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumSettleType.java
 * @Description: 结算类型枚举类
 *
 * @author Administrator
 * @date 2019年9月30日 下午5:55:29
 * @version V1.0 
 *
 */
public enum EnumSettleType {

	SETTLE_TYPE_BILL(1, "按账单结算"),
	SETTLE_TYPE_CUSTOMER(2, "按客户结算");
	
	/**
	 * @Fields key：键名称
	 */
	private Integer key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumSettleType.
	 * @param key
	 * @param value
	 */
	private EnumSettleType(Integer key, String value) {
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
		for (EnumSettleType type : EnumSettleType.values()) {
			if (type.getKey()==key) {
				return type.getValue();
			}
		}
		return null;
	}
	
}
