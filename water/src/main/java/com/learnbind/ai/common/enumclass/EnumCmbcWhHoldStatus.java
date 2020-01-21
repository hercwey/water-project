package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by ZYC
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumIdType.java
 * @Description: CMBC文件代扣状态-枚举类
 *
 * @author Thinkpad
 * @date 2019年5月16日 下午4:53:24
 * @version V1.0 
 *
 */
public enum EnumCmbcWhHoldStatus {

	/**
	 * @Fields WITHHOLDING_NO：0=未代扣
	 */
	WITHHOLDING_NO(0, "未代扣"),
	/**
	 * @Fields WITHHOLDING_SUCESS：1=代扣成功
	 */
	WITHHOLDING_SUCESS(1, "代扣成功"),
	/**
	 * @Fields WITHHOLDING_FAIL：2=代扣失败
	 */
	WITHHOLDING_FAIL(2, "代扣失败");

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
	private EnumCmbcWhHoldStatus(Integer value, String name) {
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
		for (EnumCmbcWhHoldStatus type : EnumCmbcWhHoldStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
