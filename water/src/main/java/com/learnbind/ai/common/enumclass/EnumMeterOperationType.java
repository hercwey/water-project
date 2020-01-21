package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumBindStatus.java
 * @Description: 水表日志操作类型枚举类
 *
 * @author Administrator
 * @date 2019年6月3日 上午10:26:18
 * @version V1.0 
 *
 */
public enum EnumMeterOperationType {

	OUT_STORAGE(1, "出库"),
	IN_STORAGE(2, "入库"),
	RETURN_STORAGE(3, "退库"),
	SCRAP(4, "报废"),
	DESTORY_METER(5, "销户拆表"),
	RECOVER_METER(6, "复装"),
	CYCLE_CHANGE_METER(7, "周期换表"),
	BREAK_CHANGE_METER(8, "故障换表"),
	REMOVE_METER(9, "暂拆");
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
	private EnumMeterOperationType(Integer value, String name) {
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
		for (EnumMeterOperationType type : EnumMeterOperationType.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
