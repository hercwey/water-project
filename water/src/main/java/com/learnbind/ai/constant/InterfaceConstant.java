package com.learnbind.ai.constant;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.constant
 *
 * @Title: InterfaceConstant.java
 * @Description: 接口配置常量
 *
 * @author Administrator
 * @date 2019年7月8日 下午5:20:48
 * @version V1.0 
 *
 */
public class InterfaceConstant {
	
	//-----------------------接口类型编码-----------------------
	/**
	 * @Fields INTERFACE_TYPE_CODE：接口类型编码
	 */
	public static final String INTERFACE_TYPE_CODE = "INTERFACE_TYPE";

	/**
	 * @Fields INTERFACE_TYPE_CCB：接口类型-建设银行接口
	 */
	public static final String INTERFACE_TYPE_CCB = "CCB_INTERFACE";
	
	/**
	 * @Fields INTERFACE_TYPE_CMBC：接口类型-民生银行接口
	 */
	public static final String INTERFACE_TYPE_CMBC = "CMBC_INTERFACE";
	
	/**
	 * @Fields INTERFACE_TYPE_WECHAT：接口类型-微信接口
	 */
	public static final String INTERFACE_TYPE_WECHAT = "WECHAT_INTERFACE";
	
	
	//------------------------建设银行接口配置-------------------------	
	/**
	 * @Fields CCB_KEY_URL：建设银行密钥交换url
	 */
	public static final String CCB_KEY_URL = "KEY_URL";
	/**
	 * 与CCB正常业务交互的URL路径
	 */
	public static final String CCB_BUSINESS_URL="CCB_BUSINESS_URL";
	
	/**
	 * @Fields 营业收费系统本机IP地址  (自营业收费系统发起交易时使用)
	 */
	public static final String CCB_LOCAL_HOST_IP = "LOCAL_HOST_IP";	
	
	/**
	 * 向CCB上传文件时的URL路径
	 */
	public static final String CCB_FILE_UPLOAD_URL="CCB_FILE_UPLOAD_URL";
	/**
	 * 自CCB下载文件时的URL路径
	 */
	public static final String CCB_FILE_DOWNLOAD_URL="CCB_FILE_DOWNLOAD_URL";	
	
	/**
	 * @Fields CCB_EB_CONTRACT_NO：客户的电子银行合约编号  不再使用
	 */
	public static final String CCB_EB_CONTRACT_NO = "EB_CONTRACT_NO";
	/**
	 * @Fields CCB_COMPANY_CUSTOMER_NO：企业客户号 由于电子银行合约编号与企业客户号相同.只使用此值
	 */
	public static final String CCB_COMPANY_CUSTOMER_NO = "COMPANY_CUSTOMER_NO";
	/**
	 * @Fields CCB_RSA_PUBLIC_KEY_FILE_PATH：客户端RSA公钥文件全路径	包括文件名称
	 */
	public static final String LOCAL_RSA_PUBLIC_KEY_FILE_PATH = "RSA_PUBLIC_KEY_FILE_PATH";
	/**
	 * @Fields CCB_RSA_PRIVATE_KEY_FILE_PATH：客户端RSA私钥文件全路径	包括文件名称
	 */
	public static final String LOCAL_RSA_PRIVATE_KEY_FILE_PATH = "RSA_PRIVATE_KEY_FILE_PATH";
	/**
	 * 自CCB获取得到的RSA公钥文件所在路径
	 */
	public static final String CCB_RSA_PUBLIC_KEY_FILE_PATH_CCB="RSA_PUBLIC_KEY_FILE_PATH_CCB";
	/**
	 * @Fields CCB_DES_KEY：自CCB获取的DES Key
	 */
	public static final String CCB_DES_KEY = "DES_KEY";
	
	/**
	 * 交易人员编号
	 */
	public static final String CCB_STUFF_ID="STUFF_ID";
	
	//added by hz  2019/07/11
	/**
	 * 委托单位帐号
	 */
	public static final String CCB_ENTRUST_UNIT_ACCOUNT_NO="ENTRUST_UNIT_ACCOUNT_NO";
	
	/**
	 * 委托项目编号
	 */
	public static final String CCB_ENTRUST_PROJECT_ID="ENTRUST_PROJECT_ID";
	
	/**
	 * 项目用途编号  
	 */
	public static final String CCB_PROJECT_USE_ID="PROJECT_USE_ID" ;
	
	public static final String CASH_TREE_NO="CASH_TREE_NO" ;//现金管理树编号
	
	public static final String CASH_TREE_NODE_NO="CASH_TREE_NODE_NO" ;//现金管理树节点编号
	
	
	
	/**
	 * 批量代扣金额
	 */
	public static final String WITHHOLD_AMOUNT="WITHHOLD_AMOUNT" ;
	/**
	 * 测试转账交易号
	 */
	public static final String YYYY_MM_DD_="YYYY_MM_DD_" ;
	
	
	//---------------------微信配置常量-----------------------
	//键(key)在使用时不区分大小写
	/**
	 * @Fields WECHAT_APPID：微信公众号的开发者ID(AppID)
	 */
	public static final String WECHAT_APPID="APPID" ;
	
	/**
	 * @Fields WECHAT_APPID：微信公众号的开发者密码(AppSecret)
	 */
	public static final String WECHAT_APPSECRET="APPSECRET";
	
	/**
	 * @Fields WECHAT_PAY_KEY：支付时使用的密钥key
	 */
	public static final String WECHAT_PAY_KEY="PAY_KEY";
	
	/**
	 * @Fields WECHAT_pay_notify_url：支付成功后支付服务器通知 客户方所调用的URL
	 */
	public static final String WECHAT_PAY_NOTIFY_URL="PAY_NOTIFY_URL";
	
	/**
	 * @Fields WECHAT_MCH_ID：商户号.向Tencent申请的商户号
	 */
	public static final String WECHAT_MCH_ID="MCH_ID";
		
	
	
}
