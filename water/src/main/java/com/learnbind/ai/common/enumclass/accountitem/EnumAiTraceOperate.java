package com.learnbind.ai.common.enumclass.accountitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass.accountitem
 *
 * @Title: EnumAiTraceOperate.java
 * @Description: 账目日志操作-枚举类
 *
 * @author Administrator
 * @date 2019年9月8日 上午10:21:12
 * @version V1.0 
 *
 */
public enum EnumAiTraceOperate {

	/**
	 * @Fields RECHARGE_SETTLEMENT：充值销账
	 */
	RECHARGE_SETTLEMENT("303", "充值销账"),
	/**
	 * @Fields AUTO_SETTLEMENT：余额自动销账
	 */
	AUTO_SETTLEMENT("301", "余额自动销账"),
	/**
	 * @Fields BILL_SETTLEMENT：账单冲账单
	 */
	CCB_SETTLEMENT("304", "账单冲账单"),
	/**
	 * @Fields CMBC_SETTLEMENT：民生银行代扣销账
	 */
	CMBC_SETTLEMENT("306", "民生银行代扣销账"),
	/**
	 * @Fields BILL_SETTLEMENT：307-建设银行代扣销账
	 */
	BILL_SETTLEMENT("307", "建设银行代扣销账");
	
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
	private EnumAiTraceOperate(String key, String value) {
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
		for (EnumAiTraceOperate status : EnumAiTraceOperate.values()) {
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
	public static EnumAiTraceOperate getSubjectAction(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		for (EnumAiTraceOperate status : EnumAiTraceOperate.values()) {
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
	public static List<EnumAiTraceOperate> getSettleType(){
		List<EnumAiTraceOperate> subjectAction = new ArrayList<>();
		subjectAction.add(AUTO_SETTLEMENT);
		subjectAction.add(BILL_SETTLEMENT);
		return subjectAction;
	}
	
}
