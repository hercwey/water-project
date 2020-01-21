package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass
 *
 * @Title: EnumAppReadResult.java
 * @Description: APP抄表结果状态-枚举类
 *
 * @author Administrator
 * @date 2019年7月20日 下午6:08:13
 * @version V1.0 
 *
 */
public enum EnumAppReadResult {
	
	//0:尚未抄表;1:自动:正确读表  2:自动:表计读数错误  3:自动:未抄到(超时引起)  手工:4:手工抄表
	/**
	 * @Fields RESULT_NO：未抄表
	 */
	RESULT_NO(0, "未抄"),
	/**
	 * @Fields RESULT_NORMAL：自动:正确读表
	 */
	RESULT_NORMAL(1, "A-正常"),
	/**
	 * @Fields RESULT_READ_ERROR：自动:表计读数错误
	 */
	RESULT_READ_ERROR(2, "A-错误"),
	/**
	 * @Fields RESULT_NO_TIMEOUT：自动:未抄到(超时引起)
	 */
	RESULT_NO_TIMEOUT(3, "A-超时"),
	/**
	 * @Fields RESULT_MANUAL：手工抄表，正确读表
	 */
	RESULT_MANUAL(4, "M-正常"),
	/**
	 * @Fields RESULT_MANUAL：手工抄表，正确读表
	 */
	RESULT_MANUAL_EDIT(5, "手工修改");

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
	private EnumAppReadResult(Integer value, String name) {
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
		for (EnumAppReadResult type : EnumAppReadResult.values()) {
			if (type.getValue() == value) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
