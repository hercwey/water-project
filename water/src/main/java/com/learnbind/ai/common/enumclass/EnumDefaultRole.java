package com.learnbind.ai.common.enumclass;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumDefaultRole.java
 * @Description: 系统默认角色枚举类
 *
 * @author Administrator
 * @date 2019年6月13日 上午9:52:54
 * @version V1.0 
 *
 */
public enum EnumDefaultRole {

	/**
	 * @Fields ROLE_ADMIN：系统管理员
	 */
	ROLE_ADMIN("ADMIN", "系统管理员"),
	/**
	 * @Fields ROLE_METER_READER：抄表员
	 */
	ROLE_METER_READER("METER_READER", "抄表员");

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
	private EnumDefaultRole(String code, String name) {
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
		for (EnumDefaultRole type : EnumDefaultRole.values()) {
			if (type.getCode().equals(code)) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
