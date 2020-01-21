package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumBindStatus.java
 * @Description: 操作类型枚举类
 *
 * @author Administrator
 * @date 2019年6月3日 上午10:26:18
 * @version V1.0 
 *
 */
public enum EnumOperationType {

	SETTLE_ACCOUNT(1, "立户"),
	TRANSFER_ACCOUNT(2, "过户"),
	CANCEL_ACCOUNT(3, "销户"),
	RECOVER_ACCOUNT(4, "销户恢复"),
	WATER_NO(5, "停水"),
	WATER_YES(6, "复水"),
	USER_TYPE_CHANGE(7, "用户性质改变"),
	EDIT_AMOUNT(8, "修改表底"),
	INSERT(9, "新增用户档案"),
	DELETE(10, "删除用户档案"),
	UPDATE(11, "修改用户档案");
	/**
	 * @Fields value：值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDeletedStatus.
	 * @param value
	 * @param name
	 */
	private EnumOperationType(Integer value, String name) {
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
		for (EnumOperationType type : EnumOperationType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
