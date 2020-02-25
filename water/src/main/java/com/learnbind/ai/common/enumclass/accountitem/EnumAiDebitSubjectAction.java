package com.learnbind.ai.common.enumclass.accountitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass.accountitem
 *
 * @Title: EnumAiDebitSubjectAction.java
 * @Description: 账目借方科目-动作枚举类
 *
 * @author Administrator
 * @date 2019年8月26日 下午4:12:23
 * @version V1.0 
 *
 */
public enum EnumAiDebitSubjectAction {

//新编码
//	1-XX- 01-00-01
//	C1 C2    C3   C4  C5
//	C1:借/贷  按原来编码
//	C2:Action（动作）   
//					00-09 保留 
//	10-19水费  10-交水费（综价）11-交水费（基价） 12-交污水处理费
//	20-29缴押金  	20-XX押金
//	30-39清欠  		30-清欠水费（综价）；31-清欠水费（基价）；32-清欠污水处理费
//	40-49减免       40-水费 41-违约金 （减免金额约定为正值）
//	50-59	预存
	
//			    C3,C4,C5按原来编码

	/**
	 * @Fields WATER_FEE：10=交水费（综价）
	 */
	PAY_WATER_FEE("10", "交水费（综合水费）"),
	/**
	 * @Fields BASE_FEE：11=交水费（基价）
	 */
	PAY_BASE_FEE("11", "交水费（基础水费）"),
	/**
	 * @Fields TREATMENT_FEE：12=交污水处理费
	 */
	PAY_TREATMENT_FEE("12", "交水费（污水处理费）"),
	
	/**
	 * @Fields DEBTS_WATER_FEE：30=清欠水费（综价）
	 */
	DEBTS_WATER_FEE("30", "清欠水费（综价）"),
	/**
	 * @Fields DEBTS_BASE_FEE：31=清欠水费（基价）
	 */
	DEBTS_BASE_FEE("31", "清欠水费（基价）"),
	/**
	 * @Fields DEBTS_TREATMENT_FEE：32=清欠污水处理费
	 */
	DEBTS_TREATMENT_FEE("32", "清欠污水处理费"),
	
	/**
	 * @Fields DISCOUNT_WATER_FEE：40=减免水费
	 */
	DISCOUNT_WATER_FEE("40", "减免水费"),
	/**
	 * @Fields DISCOUNT_OVERDUE_FINE：41=减免违约金
	 */
	DISCOUNT_OVERDUE_FINE("41", "减免违约金"),
	
	/**
	 * @Fields ADVANCE_WATER_FEE：51=预存水费
	 */
	ADVANCE_WATER_FEE("51", "交水费（综合水费）预存水费，余额扣费"),
	/**
	 * @Fields ADVANCE_SETTLE_WATER_FEE：52=预存销账（综合水费）
	 */
	ADVANCE_SETTLE_WATER_FEE("52", "预存销账（综合水费）"),
	/**
	 * @Fields ADVANCE_SETTLE_BASE_FEE：53=预存销账（基础水费）
	 */
	ADVANCE_SETTLE_BASE_FEE("53", "预存销账（基础水费）"),
	/**
	 * @Fields ADVANCE_SETTLE_TREATMENT_FEE：54=预存销账（污水处理费）
	 */
	ADVANCE_SETTLE_TREATMENT_FEE("54", "预存销账（污水处理费）");
	
	/**
	 * @Fields key：键名称
	 */
	private String key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiDebitSubjectAction.
	 * @param key
	 * @param value
	 */
	private EnumAiDebitSubjectAction(String key, String value) {
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
		for (EnumAiDebitSubjectAction status : EnumAiDebitSubjectAction.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status.getValue();
			}
		}
		return null;
	}
	
	/**
	 * @Title: getSubjectAction
	 * @Description: 根据键名称获取枚举
	 * @param key
	 * @return 
	 */
	public static EnumAiDebitSubjectAction getSubjectAction(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		for (EnumAiDebitSubjectAction status : EnumAiDebitSubjectAction.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status;
			}
		}
		return null;
	}
	
	/**
	 * @Title: getSettleType
	 * @Description: 获取结算类型
	 * @return 
	 */
	public static List<EnumAiDebitSubjectAction> getSettleType(){
		List<EnumAiDebitSubjectAction> subjectAction = new ArrayList<>();
		subjectAction.add(PAY_WATER_FEE);
		subjectAction.add(PAY_BASE_FEE);
		subjectAction.add(PAY_TREATMENT_FEE);
		return subjectAction;
	}
	
}
