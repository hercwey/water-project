package com.learnbind.ai.common.enumclass;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumWeChatOrderType.java
 * @Description: 微信预支付订单类型-枚举类
 *
 * @author Administrator
 * @date 2020年1月10日 下午2:56:46
 * @version V1.0 
 *
 */
public enum EnumWeChatOrderType {

	/**
	 * @Fields TYPE_PAY_WATER_FEE：1=交水费
	 */
	TYPE_PAY_WATER_FEE(1, "交水费"),
	/**
	 * @Fields TYPE_PREPAYMENT：2=预付水费
	 */
	TYPE_PREPAYMENT(2, "预付水费");
	
	/**
	 * @Fields key：键名称
	 */
	private Integer value;
	
	/**
	 * @Fields value：键值
	 */
	private String name;
	
	/**
	 * 	创建一个新的实例 EnumWeChatOrderType.
	 * @param value
	 * @param name
	 */
	private EnumWeChatOrderType(Integer value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/**
	 * @Title: getValue
	 * @Description: 获取value
	 * @return 
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * @Title: setName
	 * @Description: 设置value
	 * @param name 
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * @Title: getName
	 * @Description: 获取name
	 * @return 
	 */
	public String getName() {
		return name;
	}

	/**
	 * @Title: setName
	 * @Description: 设置name
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @Title: getName
	 * @Description: 根据value获取name
	 * @param value
	 * @return 
	 */
	public static String getName(Integer value) {
		if(value==null) {
			return null;
		}
		for (EnumWeChatOrderType type : EnumWeChatOrderType.values()) {
			if (type.getValue()==value) {
				return type.getName();
			}
		}
		return null;
	}
	
}
