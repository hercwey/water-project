package com.learnbind.ai.common.enumclass;


/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: EnumReadMode.java
 * @Description: 抄表方式枚举类
 *
 * @author Administrator
 * @date 2019年6月19日 下午5:59:30
 * @version V1.0 
 *
 */
public enum EnumReadMode {
	
	/**
	 * @Fields READ_REMOTE：抄表方式-远程抄表
	 */
	READ_REMOTE("REMOTE", "远程抄表"),
	/**
	 * @Fields READ_MANUAL：抄表方式-人工抄表
	 */
	READ_MANUAL("MANUAL", "人工抄表"),
	/**
	 * @Fields READ_BLUETOOTH：抄表方式-蓝牙抄表
	 */
	READ_BLUETOOTH("BLUETOOTH", "蓝牙抄表");
	

	/**
	 * @Fields code：编码，用于数据库保存
	 */
	private String code;
	/**
	 * @Fields name：名称，用于页面显示
	 */
	private String name;
	
	
	/**
	 * 创建一个新的实例 AbnormityTypeConstant.
	 * @param code
	 * @param name
	 */
	private EnumReadMode(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	/**
	 * @Title: getCode
	 * @Description: 获取异常编码，用于数据库保存
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
		for (EnumReadMode type : EnumReadMode.values()) {
			if (type.getCode().equals(code)) {
				return type.getName();
			}
		}
		return null;
	}  
	
}
