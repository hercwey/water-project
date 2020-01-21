package com.learnbind.ai.ccbwechat.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.ccbwechat
 *
 * @Title: CCBWechatContants.java
 * @Description: CCB聚合支付
 *
 * @author lenovo
 * @date 2019年12月27日 下午2:05:57
 * @version V1.0 
 *
 */

/*
 * 
SUCCESS	返回状态码	此字段是通信标识，表示通信成功
ERRCODE	错误码	000000表示交易成功，非000000表示交易失败，错误信息可以查看ERRMSG字段
ERRMSG	错误信息	错误信息描述
TXCODE	交易码	530590
appId	微信分配的APPID	参考微信对应的调起支付API
timeStamp	时间戳	参考微信对应的调起支付API
nonceStr	随机串	参考微信对应的调起支付API
package	数据包	参考微信对应的调起支付API
signType	签名方式	参考微信对应的调起支付API
paySign	签名数据	参考微信对应的调起支付API
partnerid	子商户的商户号	参考微信对应的调起支付API
prepayid	预支付交易会话ID	参考微信对应的调起支付API
mweb_url	微信H5支付中间页面URL	参考微信对应的调起支付API
 * 
 * 
 */

public class CCBWechatContants {
	
	//下单时url
	public static String CCB_WECHAT_URL="https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain?CCB_IBSVersion=V6";
	
	//---------------常量定义,来自于建行提供的开发文档.---------------
	public static final String MERCHANT_ID="105000049005119";  //商户代码常量
	
	//需要截取后30个字符
	static final String PUBLIC_KEY="30819c300d06092a864886f70d010101050003818a0030818602818076a7193d825c7abb3dc5cded57e072c220de4f82dc48061d74fd93ff5b349ddeaae3130ae37891915969cbd438029c0d7df63da33e9282413a4d6e9493e062f885e1b7ce252a3b67f66cfae6e45ec0e2bce1e4e963e067fb9039c4cb10f440593febf6df81ab190ce2e087a4adbb573493a23dae3304424cbfff4f8894d817a5020111";
	static final String POS_ID="041692036";  	//柜台号常量
	static final String BRANCH_ID="130000000";  //分行代码
	static final String TX_CODE="530590";  //交易码
	static final String PUBLIC_KEY_SUB_STR=StringUtils.substring(PUBLIC_KEY, PUBLIC_KEY.length()-30);
	static final String TRADE_TYPE_JSAPI="JSAPI";  //调用类型
	static final String CUR_CODE="01";  //币种,人民币  
	
	
}
