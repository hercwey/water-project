package com.learnbind.ai.common.enumclass;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumCustomerAccountItemLogType.java
 * @Description: 客户账单日志类型
 *
 * @author Thinkpad
 * @date 2019年11月25日 下午10:34:07
 * @version V1.0 
 *
 */
public enum EnumCustomerAccountItemLogType {
	
	/**
	 * @Fields RECHARGE：充值
	 */
	RECHARGE(1, "充值"),
	/**
	 * @Fields ARREARS：消费
	 */
	ARREARS(2, "消费"),
	/**
	 * @Fields CANCEL_RECHARGE：撤销充值
	 */
	CANCEL_RECHARGE(3, "撤销充值"),
	
	DELETE_RECHARGE(4, "删除充值");

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
	private EnumCustomerAccountItemLogType(Integer value, String name) {
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
		for (EnumCustomerAccountItemLogType type : EnumCustomerAccountItemLogType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
