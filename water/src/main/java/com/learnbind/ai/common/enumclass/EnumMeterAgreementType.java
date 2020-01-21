package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumMeterAgreementType.java
 * @Description:表计单据类型
 *
 * @author Thinkpad
 * @date 2019年10月22日 上午11:54:21
 * @version V1.0 
 *
 */
public enum EnumMeterAgreementType {

	STORAGE(1, "入库单"),
	
	RECEIVE(2, "安装领用单"),
	
	CHANGE(3, "更换领用单"),
	
	DETECT(4, "检测单"),
	
	SCRAP(5, "报废单"),
	
	RETURN(6, "退库单"),
	
	CONSTRUCTION(7, "施工单");

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
	private EnumMeterAgreementType(Integer value, String name) {
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
		for (EnumMeterAgreementType type : EnumMeterAgreementType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
