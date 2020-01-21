package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumIsLadderPrice.java
 * @Description: 是否阶梯水价状态
 *
 * @author Administrator
 * @date 2019年9月9日 下午3:17:20
 * @version V1.0 
 *
 */
public enum EnumIsLadderPrice {

	//是否是阶梯水价 0 ：否，1：是
	/**
	 * @Fields LADDER_PRICE_YES：阶梯水价
	 */
	LADDER_PRICE_YES(1, "是"),

	/**
	 * @Fields LADDER_PRICE_NO：非阶梯水价
	 */
	LADDER_PRICE_NO(0, "否");

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
	private EnumIsLadderPrice(Integer value, String name) {
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
		for (EnumIsLadderPrice type : EnumIsLadderPrice.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
