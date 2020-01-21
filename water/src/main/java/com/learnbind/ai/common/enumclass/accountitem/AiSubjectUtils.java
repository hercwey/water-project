package com.learnbind.ai.common.enumclass.accountitem;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.enumclass.accountitem
 *
 * @Title: AiSubjectUtils.java
 * @Description: 账目科目工具类
 *
 * @author Administrator
 * @date 2019年8月31日 上午10:48:27
 * @version V1.0 
 *
 */
public class AiSubjectUtils {

	/**
	 * @Fields log：日志
	 */
	private static Log log = LogFactory.getLog(AiSubjectUtils.class);
	
	/**
	 * @Fields DEBIT_CODE：借/贷状态-借方编码
	 */
	private static final String DEBIT_CODE = EnumAiDebitCreditStatus.DEBIT.getKey();
	/**
	 * @Fields CREDIT_CODE：借/贷状态-贷方编码
	 */
	//private static final String CREDIT_CODE = EnumAiDebitCreditStatus.CREDIT.getKey();
	
	/**
	 * @Title: getAiSubjectStr
	 * @Description: 获取账目科目字符串
	 * @param aiSubjectCode
	 * @return 
	 */
	public static String getAiSubjectStr(String aiSubjectCode) {
		
		log.debug("输入的科目编码："+aiSubjectCode);
		if(StringUtils.isBlank(aiSubjectCode)) {
			return "";
		}
		
		//获取借/贷编码
		String debitCredit = AiSubjectUtils.getDebitCredit(aiSubjectCode);
		if(StringUtils.isBlank(debitCredit)) {
			log.debug("返回的科目字符串：");
			return "";
		}
		
		//获取科目编码集合
		List<String> subjectList = AiSubjectUtils.getSubjectList(aiSubjectCode);
		
		if(debitCredit.equalsIgnoreCase(DEBIT_CODE)) {
			return AiSubjectUtils.getDebitSubjectStr(subjectList);//获取借方科目字符串
		}else {
			return AiSubjectUtils.getCreditSubjectStr(subjectList);//获取贷方科目字符串
		}
	}
	
	/**
	 * @Title: getDebitCredit
	 * @Description: 获取借/贷
	 * @param aiSubjectCode
	 * @return 
	 */
	private static String getDebitCredit(String aiSubjectCode) {
		if(StringUtils.isBlank(aiSubjectCode)) {
			return null;
		}
		
		char first = aiSubjectCode.charAt(0);
		String debitCredit = String.valueOf(first);
		log.debug("借/贷："+debitCredit);
		return debitCredit;
	}
	
	/**
	 * @Title: getSubjectList
	 * @Description: 获取科目集合
	 * @param aiSubjectCode
	 * @return 
	 */
	private static List<String> getSubjectList(String aiSubjectCode) {
		if(StringUtils.isBlank(aiSubjectCode)) {
			return null;
		}
		
		//String debitCredit = "";
		List<String> subjectList = new ArrayList<>();
		
		String action = aiSubjectCode.substring(1, 3);
		subjectList.add(action);
		String payment = aiSubjectCode.substring(3, aiSubjectCode.length());
		subjectList.add(payment);
		
//		for(int i=0; i<aiSubjectCode.length(); i++) {
//			if(i==0) {
//				//char first = aiSubjectCode.charAt(i);
//				//debitCredit = String.valueOf(first);
//			}else {
//				if(i%2!=0) {
//					char one = aiSubjectCode.charAt(i);
//					char two = aiSubjectCode.charAt((i+1));
//					String subject = String.valueOf(one)+String.valueOf(two);
//					subjectList.add(subject);
//				}
//			}
//			
//		}
		log.debug("科目集合："+subjectList);
		return subjectList;
	}
	
	/**
	 * @Title: getDebitSubjectStr
	 * @Description: 获取借方科目字符串
	 * @param subjectList
	 * @return 
	 */
	private static String getDebitSubjectStr(List<String> subjectList) {
		if(subjectList==null || subjectList.size()<=0) {
			log.debug("返回的科目字符串：");
			return "";
		}
		String subjectStr = "";
		for(String subject : subjectList) {
			String str = AiSubjectUtils.getDebitSubjectStr(subject);
			if(StringUtils.isNotBlank(str)) {
				subjectStr += str;
			}
		}
		log.debug("返回的科目字符串："+subjectStr);
		return subjectStr;
	}
	
	/**
	 * @Title: getDebitSubjectStr
	 * @Description: 获取借方科目字符串
	 * @param subject
	 * @return 
	 */
	private static String getDebitSubjectStr(String subject) {
		String subjectStr = EnumAiDebitSubjectAction.getValue(subject);
		if(StringUtils.isNotBlank(subjectStr)) {
			return subjectStr;
		}
		return EnumAiDebitSubjectPayment.getValue(subject);
	}
	
	/**
	 * @Title: getCreditSubjectStr
	 * @Description: 获取贷方科目字符串
	 * @param subjectList
	 * @return 
	 */
	private static String getCreditSubjectStr(List<String> subjectList) {
		if(subjectList==null || subjectList.size()<=0) {
			log.debug("返回的科目字符串：");
			return "";
		}
		String subjectStr = "";
		for(String subject : subjectList) {
			String str = AiSubjectUtils.getCreditSubjectStr(subject);
			if(StringUtils.isNotBlank(str)) {
				subjectStr += str;
			}
		}
		log.debug("返回的科目字符串："+subjectStr);
		return subjectStr;
	}
	
	/**
	 * @Title: getCreditSubjectStr
	 * @Description: 获取贷方科目字符串
	 * @param subject
	 * @return 
	 */
	private static String getCreditSubjectStr(String subject) {
		return EnumAiCreditSubjectAction.getValue(subject);
	}
	
	/**
	 * @Title: main
	 * @Description: main方法
	 * @param args 
	 */
	public static void main(String[] args) {
//		AiSubjectUtils.getAiSubjectStr("201");
//		AiSubjectUtils.getAiSubjectStr("11101");
//		AiSubjectUtils.getAiSubjectStr("111020101");//微信支付
//		AiSubjectUtils.getAiSubjectStr("111020102");//微信扫码
//		
//		AiSubjectUtils.getAiSubjectStr("111020201");//支付宝支付
//		AiSubjectUtils.getAiSubjectStr("111020202");//支付宝扫码
		
//		AiSubjectUtils.getAiSubjectStr("1110301");//聚合支付/建设银行
//		AiSubjectUtils.getAiSubjectStr("1110401");//建设银行代扣
//		AiSubjectUtils.getAiSubjectStr("1110402");//民生银行代扣
		
//		AiSubjectUtils.getAiSubjectStr("1110502");//支票/民生银行
		
		AiSubjectUtils.getAiSubjectStr("1110601");//转账/建设银行
		
//		String code = "111020101";//现金
//		String action = code.substring(1, 3);
//		String payment = code.substring(3, code.length());
//		System.out.println("1:"+action);
//		System.out.println("2:"+payment);
		
	}
	
}
