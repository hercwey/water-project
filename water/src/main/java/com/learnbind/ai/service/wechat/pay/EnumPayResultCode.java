package com.learnbind.ai.service.wechat.pay;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.service.wechat.pay
 *
 * @Title: EnumPayResultCode.java
 * @Description: 支付状态常量.
 *
 * @author lenovo
 * @date 2020年1月8日 下午2:55:34
 * @version V1.0 
 *
 */
public enum EnumPayResultCode {
	RESULT_CODE_NO_PAY(0,"尚未支付"),
	RESULT_CODE_SUCCESS(1,"接收到通知-支付成功"),
	RESULT_CODE_FAIL(2,"接收到通知-支付失败");
	
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
	private EnumPayResultCode(Integer value, String name) {
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
		for (EnumPayResultCode type : EnumPayResultCode.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}
}
