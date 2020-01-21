package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumMeterCycleStatus.java
 * @Description: 表计生成周期状态-枚举类
 *
 * @author Administrator
 * @date 2019年8月6日 下午7:32:07
 * @version V1.0 
 *
 */
public enum EnumMeterCycleStatus {

//	0：待用（待使用）（默认值）
//	1：领用
//	2：待启用
//	3：使用中
//	4：待检测
//	5：检测中
//	6：待检修
//	7：检修中
//	8：报废
//	9：退库
	
	
	/**
	 * @Fields INACTIVE：表计生成周期状态-0=待用（待使用）（默认值）
	 */
	INACTIVE(0, "待用"),
	/**
	 * @Fields RECEIVE：表计生成周期状态-1=领用
	 */
	RECEIVE(1, "领用"),
	/**
	 * @Fields NOT_ENABLED：表计生成周期状态-2=待启用
	 */
	NOT_ENABLED(2, "待启用"),
	/**
	 * @Fields ENABLED：表计生成周期状态-3=使用中
	 */
	ENABLED(3, "使用中"),
	/**
	 * @Fields NO_DETECTION：表计生成周期状态-4=待检测
	 */
	NO_DETECTION(4, "待检测"),
	/**
	 * @Fields DETECTING：表计生成周期状态-5=检测中
	 */
	DETECTING(5, "检测中"),
	/**
	 * @Fields NO_MAINTENANCE：表计生成周期状态-6=待检修
	 */
	NO_MAINTENANCE(6, "待检修"),
	/**
	 * @Fields IN_MAINTENANCE：表计生成周期状态-7=检修中
	 */
	IN_MAINTENANCE(7, "检修中"),
	/**
	 * @Fields SCRAP：表计生成周期状态-8=报废
	 */
	SCRAP(8, "报废"),
	/**
	 * @Fields RETURN：表计生成周期状态-9=退库
	 */
	RETURN(9, "退库");

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
	private EnumMeterCycleStatus(Integer value, String name) {
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
		for (EnumMeterCycleStatus type : EnumMeterCycleStatus.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
