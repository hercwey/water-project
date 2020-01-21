package com.learnbind.ai.common.enumclass.accountitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass.accountitem
 *
 * @Title: EnumAiDebitSubjectPayment.java
 * @Description: 账目借方科目-支付方式枚举类
 *
 * @author Administrator
 * @date 2019年8月26日 下午4:12:23
 * @version V1.0 
 *
 */
public enum EnumAiDebitSubjectPayment {

//	1	借  
//	
//	01	现金
//	02	第三方支付
//			01	微信
//					01	支付
//					02	扫码
//			02	支付宝
//					01	支付
//					02	扫码
//			
//	03	聚合支付
//			01	建设银行
//	04	代扣					
//			01	建设银行代扣
//			02	民生银行代扣
//	05 支票
//		01	CCB
//		02	民生银生
//		03	农业银行
//		04	工商银行
//		05	........
//	06 转账
//		01 CCB
//		02 其它银行
//	07 划卡
//		01 CCB
//		02 其它银行
//	08减免
//	09余额扣费

	/**
	 * @Fields PAYMENT_CASH：01=现金支付
	 */
	PAYMENT_CASH("01", "现金支付"),
	
	/**
	 * @Fields PAYMENT_WECHAT：020101=微信支付
	 */
	PAYMENT_WECHAT("020101", "微信支付"),
	/**
	 * @Fields PAYMENT_WECHAT_QRCODE：020102=微信扫码支付
	 */
	PAYMENT_WECHAT_QRCODE("020102", "微信扫码支付"),
	
	/**
	 * @Fields PAYMENT_ALIPAY：020201=支付宝支付
	 */
	PAYMENT_ALIPAY("020201", "支付宝支付"),
	/**
	 * @Fields PAYMENT_ALIPAY_QRCODE：020202=支付宝扫码支付
	 */
	PAYMENT_ALIPAY_QRCODE("020202", "支付宝扫码支付"),
	
	/**
	 * @Fields PAYMENT_AGGREGATE_CCB：0301=聚合支付/建设银行
	 */
	PAYMENT_AGGREGATE_CCB("0301", "聚合支付/建设银行"),
	
	/**
	 * @Fields PAYMENT_WITHHOLD_CCB：0401=建设银行代扣
	 */
	PAYMENT_WITHHOLD_CCB("0401", "建设银行代扣"),
	/**
	 * @Fields PAYMENT_WITHHOLD_CMBC：0402=民生银行代扣
	 */
	PAYMENT_WITHHOLD_CMBC("0402", "民生银行代扣"),
	
	/**
	 * @Fields PAYMENT_CHECK_CCB：0501=支票/建设银行
	 */
	PAYMENT_CHECK_CCB("0501", "支票/建设银行"),
	/**
	 * @Fields PAYMENT_CHECK_CMBC：0502=支票/民生银行
	 */
	PAYMENT_CHECK_CMBC("0502", "支票/民生银行"),
	
	/**
	 * @Fields PAYMENT_TRANSFER_CCB：0601=转账/建设银行
	 */
	PAYMENT_TRANSFER_CCB("0601", "转账/建设银行"),
	/**
	 * @Fields PAYMENT_TRANSFER_CMBC：0602=转账/民生银行
	 */
	PAYMENT_TRANSFER_CMBC("0602", "转账/民生银行"),
	
	/**
	 * @Fields PAYMENT_CARD_CCB：0701=划卡/建设银行
	 */
	PAYMENT_CARD_CCB("0701", "划卡/建设银行"),
	/**
	 * @Fields PAYMENT_CARD_CMBC：0702=划卡/民生银行
	 */
	PAYMENT_CARD_CMBC("0702", "划卡/民生银行"),
	
	/**
	 * @Fields PAYMENT_DISCOUNT：08=减免
	 */
	PAYMENT_DISCOUNT("08", "减免"),
	
	/**
	 * @Fields PAYMENT_BALANCE：09=余额扣费
	 */
	PAYMENT_BALANCE("09", "余额扣费");

	/**
	 * @Fields key：键名称
	 */
	private String key;
	
	/**
	 * @Fields value：键值
	 */
	private String value;
	
	
	/**
	 * 创建一个新的实例 EnumAiDebitSubjectPayment.
	 * @param key
	 * @param value
	 */
	private EnumAiDebitSubjectPayment(String key, String value) {
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
		for (EnumAiDebitSubjectPayment status : EnumAiDebitSubjectPayment.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status.getValue();
			}
		}
		return null;
	}
	
	/**
	 * @Title: getSubjectPayment
	 * @Description: 根据键名称获取枚举
	 * @param key
	 * @return 
	 */
	public static EnumAiDebitSubjectPayment getSubjectPayment(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		for (EnumAiDebitSubjectPayment status : EnumAiDebitSubjectPayment.values()) {
			if (status.getKey().equalsIgnoreCase(key)) {
				return status;
			}
		}
		return null;
	}
	
	/**
	 * @Title: getSettlePayment
	 * @Description: 获取结算时支付方式
	 * @return 
	 */
	public static List<EnumAiDebitSubjectPayment> getSettlePayment(){
		
		List<EnumAiDebitSubjectPayment> subjectPaymentList = new ArrayList<>();
		
		for (EnumAiDebitSubjectPayment status : EnumAiDebitSubjectPayment.values()) {
			if (!status.getKey().equalsIgnoreCase(EnumAiDebitSubjectPayment.PAYMENT_DISCOUNT.getKey())) {
				subjectPaymentList.add(status);
			}
		}
		return subjectPaymentList;
	}
	
}
