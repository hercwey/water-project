package com.learnbind.ai.common.enumclass;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumBindStatus.java
 * @Description: 地理位置节点类型（小区/楼号/单元）枚举类
 *
 * @author Administrator
 * @date 2019年6月3日 上午10:26:18
 * @version V1.0 
 *
 */
public enum EnumLocalNodeType {

	/**
	 * @Fields LOCAL_NODE_TYPE_BLOCK：小区
	 */
	LOCAL_NODE_TYPE_BLOCK("BLOCK", "小区"),
	/**
	 * @Fields LOCAL_NODE_TYPE_BUILDING：楼号
	 */
	LOCAL_NODE_TYPE_BUILDING("BUILDING", "楼号"),
	/**
	 * @Fields LOCAL_NODE_TYPE_UNIT：单元
	 */
	LOCAL_NODE_TYPE_UNIT("UNIT", "单元"),
	/**
	 * @Fields LOCAL_NODE_TYPE_ROOM：室
	 */
	LOCAL_NODE_TYPE_ROOM("ROOM", "室");

	/**
	 * @Fields code：编码，用于数据库保存
	 */
	private String code;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 EnumDeletedStatus.
	 * @param code
	 * @param name
	 */
	private EnumLocalNodeType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	/**
	 * @Title: getCode
	 * @Description: 获取编码，用于数据库保存
	 * @return 
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * @Title: getName
	 * @Description: 获取名称，用于页面显示
	 * @return 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @Title: getName
	 * @Description: 根据编码获取名称
	 * @param code
	 * @return 
	 */
	public static String getName(String code) {
		if(StringUtils.isBlank(code)) {
			return null;
		}
		for (EnumLocalNodeType type : EnumLocalNodeType.values()) {
			if (type.getCode().equals(code)) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
