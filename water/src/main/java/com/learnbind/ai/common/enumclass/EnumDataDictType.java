package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumBindStatus.java
 * @Description: 绑定状态枚举类
 *
 * @author Administrator
 * @date 2019年6月3日 上午10:26:18
 * @version V1.0 
 *
 */
public enum EnumDataDictType {

	METER_READ_MODE("READ_MODE", "抄表方式"),
	METER_FACTORY("METER_FACTORY", "生产厂家"),
	METER_MODEL("METER_MODEL", "水表型号"),
	METER_TYPE("METER_TYPE", "水表类型"),
	METER_USE("METER_USE", "水表用途"),
	METER_WATER_CALIBER("WARTER_CALIBER", "水表口径"),
	
	WATER_USE("WATER_USE", "用水性质"),
	WORK_ORDER_TYPE("WORK_ORDER_TYPE", "工单类型"),
	KNOW_LIBRARY_TYPE("KNOW_LIBRARY_TYPE", "知识库类型"),
	PROTOCOL_TYPE("PROTOCOL_TYPE", "水表协议类型"),
	SAMPLE_UNIT("SAMPLE_UNIT", "采样参数单位"),
	METER_USE_TYPE("METER_USE_TYPE", "水表用水类型"),
	METER_FACTORY_CODE("METER_FACTORY_CODE", "水表厂商代码"),
	CUSTOMER_TRADE_TYPE("TRADE_TYPE", "行业性质");

	/**
	 * @Fields value：值，用于数据库保存
	 */
	private String code;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDeletedStatus.
	 * @param value
	 * @param name
	 */
	private EnumDataDictType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	/**
	 * @Title: getValue
	 * @Description: 获取异常类型值，用于数据库保存
	 * @return 
	 */
	public String getCode() {
		return code;
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
	public static String getName(String code) {
		for (EnumDataDictType type : EnumDataDictType.values()) {
			if (type.getCode() == code) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
