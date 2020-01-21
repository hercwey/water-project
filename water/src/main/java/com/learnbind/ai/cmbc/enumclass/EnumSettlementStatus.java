package com.learnbind.ai.cmbc.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.cmbc.enumclass
 *
 * @Title: EnumSettlementStatus.java
 * @Description: CMBC自动扣费结算状态枚举类
 *
 * @author Administrator
 * @date 2019年7月12日 下午4:11:57
 * @version V1.0 
 *
 */
public enum EnumSettlementStatus {

	/**
	 * @Fields SETTLEMENT_NORMAL：0=默认值
	 */
	SETTLEMENT_NORMAL(0, "未结算"),
	/**
	 * @Fields SETTLEMENT_SUCCESS：1=扣费结算成功
	 */
	SETTLEMENT_SUCCESS(1, "结算成功"),
	/**
	 * @Fields SETTLEMENT_FAIL：2=扣费结算失败
	 */
	SETTLEMENT_FAIL(2, "结算失败"),
	/**
	 * @Fields SETTLEMENT_PART：3=部分结算
	 */
	SETTLEMENT_PART(3, "部分结算");

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
	private EnumSettlementStatus(Integer value, String name) {
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
	public static String getName(Integer value) {
		if(value==null) {
			return null;
		}
		for (EnumSettlementStatus type : EnumSettlementStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
