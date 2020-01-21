package com.learnbind.ai.common.enumclass.tax;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass.tax
 *
 * @Title: EnumTaxBsbz.java
 * @Description: 发票报送标志
 *
 * @author Thinkpad
 * @date 2019年12月19日 下午6:03:45
 * @version V1.0 
 *
 */
/* 0：未报送1：已报送2：报送失败3：报送中4：验签失败空为全部 */
public enum EnumTaxBsbz {

	WBS("0", "未报送"),
	YBS("1", "已报送"),
	BSSB("2", "报送失败"),
	BSZ("3", "报送中"),
	YQSB("4", "验签失败");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private String value;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDeletedStatus.
	 * @param value
	 * @param name
	 */
	private EnumTaxBsbz(String value, String name) {
		this.value = value;
		this.name = name;
	}
	
	/**
	 * @Title: getValue
	 * @Description: 获取异常类型值，用于数据库保存
	 * @return 
	 */
	public String getValue() {
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
	public static String getName(String value) {
		for (EnumTaxBsbz type : EnumTaxBsbz.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
