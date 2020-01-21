package com.learnbind.ai.common.enumclass.accountitem;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass.accountitem
 *
 * @Title: EnumAiCreditSubjectAction.java
 * @Description: 账目贷方科目-动作枚举类
 *
 * @author Administrator
 * @date 2019年8月26日 下午4:12:23
 * @version V1.0 
 *
 */
public enum EnumAiCreditSubjectAction {

//账目科目
//	2-XX- 01-01
//新编码
//	2	贷
//	XX:action 此笔账目对应的外部动作
//	00-09费用   01水费；02违约金
//	10-19退款   11押金  12-退XX
//	20-29 分账   21水费分账  22:xx分账
//	50-59 非标抄表 。。。。。。   ？？？
//	60-69 清欠 。。。。。。   ？？？
	
	/**
	 * @Fields WATER_FEE：01=水费
	 */
	WATER_FEE("01", "水费"),
	/**
	 * @Fields OVERDUE_FINE：02=违约金
	 */
	OVERDUE_FINE("02", "违约金"),
	/**
	 * @Fields REFUND_DEPOSIT：11=退押金
	 */
	REFUND_DEPOSIT("11", "退押金"),
	/**
	 * @Fields SUB_ACCOUNT：21=水费分账
	 */
	SUB_ACCOUNT("21", "水费分账"),
	
	/**
	 * @Fields CLEAR_BILL：61=清欠账单
	 */
	CLEAR_BILL("61", "清欠账单");
	
	

	/**
	 * @Fields key：键名称
	 */
	private String key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiCreditSubjectAction.
	 * @param key
	 * @param value
	 */
	private EnumAiCreditSubjectAction(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	/**
	 * @Title: getKey
	 * @Description: 获取键名称
	 * @return 
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @Title: setKey
	 * @Description: 设置键名称
	 * @param key 
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @Title: getValue
	 * @Description: 获取键值
	 * @return 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @Title: setValue
	 * @Description: 设置键值
	 * @param value 
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @Title: getValue
	 * @Description: 根据键名称获取键值
	 * @param key
	 * @return 
	 */
	public static String getValue(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		for (EnumAiCreditSubjectAction status : EnumAiCreditSubjectAction.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status.getValue();
			}
		}
		return null;
	}  
	
}
