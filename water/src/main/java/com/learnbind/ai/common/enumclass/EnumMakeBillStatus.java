package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumMakeBillStatus.java
 * @Description: 开账状态-枚举类
 *
 * @author Administrator
 * @date 2019年8月20日 上午9:16:16
 * @version V1.0 
 *
 */
public enum EnumMakeBillStatus {

	/**
	 * @Fields MAKE_BILL_NO：开账状态-否
	 */
	MAKE_BILL_NO(0, "否"),
	
	/**
	 * @Fields MAKE_BILL_YES：开账状态-是
	 */
	MAKE_BILL_YES(1, "是");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private Integer value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDefaultStatus.
	 * @param value
	 * @param name
	 */
	private EnumMakeBillStatus(Integer value, String name) {
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
		for (EnumMakeBillStatus type : EnumMakeBillStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
