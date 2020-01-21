package com.learnbind.ai.tax.consts;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax
 *
 * @Title: ErrorCode.java
 * @Description: 错误编码表
 *
 * @author lenovo
 * @date 2019年9月29日 下午6:03:32
 * @version V1.0 
 *
 */
public class ErrorCode {
	
	public static final String ERROR_NEG_1="-1";  	//-1WebService操作出现异常！	
	public static final String ERROR_NEG_2="-2";  	//-2连接控制台异常！
	public static final String ERROR_NEG_3="-3";  	//-3控制台ip或端口号为空！
	public static final String ERROR_NEG_4="-4";   //-4卷式发票的明细必须为含税
	public static final String ERROR_NEG_5="-5";  	//-5库存查询发票种类不能为空
	public static final String ERROR_NEG_6="-6";   //-6卷式发票开具时，卷式发票规格填写错误
	public static final String ERROR_NONE="0";  	// 0 操作成功
	

}
