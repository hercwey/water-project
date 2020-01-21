package com.learnbind.ai.ccbwechat.entity;

import java.math.BigDecimal;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.controller.ccbwechat
 *
 * @Title: CCBRquestOrderParam.java
 * @Description: 请求下单参数
 *
 * @author lenovo
 * @date 2019年12月27日 下午1:16:34
 * @version V1.0 
 *
 */

/*
MERCHANTID	商户代码	CHAR(15)	Y	由建行统一分配
POSID	商户柜台代码	CHAR(9)	Y	由建行统一分配，
BRANCHID	分行代码	CHAR(9)	Y	由建行统一指定
ORDERID	定单号	CHAR(30)	Y	由商户提供，最长30位
PAYMENT	付款金额	NUMBER(16,2)	Y	由商户提供，按实际金额给出
支付完成后，请商户与收到的商户通知中的付款金额比对，确认两者金额一致；
CURCODE	币种	CHAR(2)	Y	缺省为01－人民币
（只支持人民币支付）
REMARK1	备注1	CHAR(30)	N	一般作为商户自定义备注信息使用，可在对账单中显示。
REMARK2	备注2	CHAR(30)	N	一般作为商户自定义备注信息使用，可在对账单中显示。
TXCODE	交易码	CHAR(6)	Y	由建行统一分配为530590
MAC	MAC校验域	CHAR(32)	Y	采用标准MD5算法，由商户实现
TYPE	接口类型	CHAR(1)	Y	分行业务人员在P2员工渠道后台设置防钓鱼的开关。
1-	防钓鱼接口
PUB	公钥后30位	CHAR(30)	Y	商户从建行商户服务平台下载，截取后30位。
仅作为源串参加MD5摘要，不作为参数传递
GATEWAY	网关类型	VARCHAR(100)	Y	默认送0 
CLIENTIP	客户端IP	CHAR(40)	N	客户在商户系统中的IP，即客户登陆（访问）商户系统时使用的ip）
REGINFO	客户注册信息
	CHAR(256)	N	客户在商户系统中注册的信息，中文需使用escape编码
PROINFO	商品信息	CHAR(256)	N	客户购买的商品
中文需使用escape编码
REFERER	商户URL	CHAR(100)	N	商户送空值即可；
具体请看REFERER设置说明
TIMEOUT	订单超时时间	CHAR(14)	N	格式：
YYYYMMDDHHMMSS如：20120214143005
银行系统时间> TIMEOUT时拒绝交易，若送空值则不判断超时。
当该字段有值时参与MAC校验，否则不参与MAC校验。

TRADE_TYPE	交易类型	CHAR(16)	Y	JSAPI--公众号支付、APP--app支付、MINIPRO--小程序、H5--H5跳转支付
SUB_APPID	小程序的APPID	CHAR(32)	Y	当前调起支付的小程序APPID
SUB_OPENID	用户子标识	CHAR(128)	Y	用户在小程序appid下的唯一标识，小程序通过wx.login获取，接口文档地址：https://developers.weixin.qq.com/miniprogram/dev/api/api-login.html?t=20161122
二级商户信息,若上送二级商户信息则八个二级商户信息字段必须都送值，当该字段有值时参与MAC校验，否则不参与MAC校验。
SMERID	二级商户代码	CHAR(15)	N	二级商户代码
SMERNAME	二级商户名称	中文（33）英文CHAR(100)	N	二级商户名称，中文需使用escape编码
SMERTYPEID	二级商户类别代码	CHAR(15)	N	二级商户类别代码
SMERTYPE	二级商户类别名称	中文（27）英文CHAR(81)	N	二级商户类别名称，汉字最长27个，中文需使用escape编码
TRADECODE	交易类型代码	CHAR(15)	N	交易类型代码
TRADENAME	交易类型名称	中文（10）英文CHAR(30)	N	如消费、投资理财、信用卡还款等，中文需使用escape编码
SMEPROTYPE	商品类别代码	CHAR(24)	N	商品类别代码
PRONAME	商品类别名称	中文（15）英文CHAR(50)	N	商品类别名称，中文需使用escape编码
 
 */
public class CCBRquestOrderParam {

	//---------------常量定义,来自于建行提供的开发文档.---------------
	//static final String MERCHANT_ID="105000049005119";  //商户代码常量
	
	//需要截取后30个字符
//	static final String PUBLIC_KEY="30819c300d06092a864886f70d010101050003818a0030818602818076a7193d825c7abb3dc5cded57e072c220de4f82dc48061d74fd93ff5b349ddeaae3130ae37891915969cbd438029c0d7df63da33e9282413a4d6e9493e062f885e1b7ce252a3b67f66cfae6e45ec0e2bce1e4e963e067fb9039c4cb10f440593febf6df81ab190ce2e087a4adbb573493a23dae3304424cbfff4f8894d817a5020111";
//	static final String POS_ID="014692036";  	//柜台号常量
//	static final String BRANCH_ID="130000000";  //分行代码
//	static final String TX_CODE="530590";  //交易码
//	static final String PUBLIC_KEY_SUB_STR=StringUtils.substring(PUBLIC_KEY, PUBLIC_KEY.length()-30);
	
	//-------------------------请求参数列表---------------------
	private String MERCHANTID=CCBWechatContants.MERCHANT_ID;	//商户代码	CHAR(15)	Y	由建行统一分配
	private String POSID=CCBWechatContants.POS_ID;	//商户柜台代码	CHAR(9)	Y	由建行统一分配，
	private String BRANCHID=CCBWechatContants.BRANCH_ID;	//分行代码	CHAR(9)	Y	由建行统一指定
	private String ORDERID="";   //定单号	CHAR(30)	Y	由商户提供，最长30位
	private String PAYMENT="";	//付款金额	NUMBER(16,2)	Y	由商户提供，按实际金额给出  	支付完成后，请商户与收到的商户通知中的付款金额比对，确认两者金额一致；
	private String CURCODE=CCBWechatContants.CUR_CODE;	//币种	CHAR(2)	Y	缺省为01－人民币	（只支持人民币支付）
	private String TXCODE=CCBWechatContants.TX_CODE;	//	交易码	CHAR(6)	Y	由建行统一分配为530590
	private String REMARK1="";	//备注1	CHAR(30)	N	一般作为商户自定义备注信息使用，可在对账单中显示。
	private String REMARK2=""; //备注2	CHAR(30)	N	一般作为商户自定义备注信息使用，可在对账单中显示。	
	//private String MAC="";		//	MAC校验域	CHAR(32)	Y	采用标准MD5算法，由商户实现
	private String TYPE="1";	//接口类型	CHAR(1)	Y	分行业务人员在P2员工渠道后台设置防钓鱼的开关。	1-	防钓鱼接口
	private String PUB=CCBWechatContants.PUBLIC_KEY_SUB_STR;		//公钥后30位	CHAR(30)	Y	商户从建行商户服务平台下载，截取后30位。  仅作为源串参加MD5摘要，不作为参数传递  
	private String GATEWAY="0";  //	网关类型	VARCHAR(100)	Y	默认送0 
	private String CLIENTIP="";  //	客户端IP	CHAR(40)	N	客户在商户系统中的IP，即客户登陆（访问）商户系统时使用的ip）  默认值采用""串即可.
	private String REGINFO="";	//客户注册信息	CHAR(256)	N	客户在商户系统中注册的信息，中文需使用escape编码
	private String PROINFO="";	//商品信息	CHAR(256)	N	客户购买的商品	中文需使用escape编码
	private String REFERER="";	//商户URL	CHAR(100)	N	商户送空值即可；具体请看REFERER设置说明
	//private String TIMEOUT="";//	订单超时时间	CHAR(14)	N	格式：	YYYYMMDDHHMMSS如：20120214143005 	银行系统时间> TIMEOUT时拒绝交易，若送空值则不判断超时。当该字段有值时参与MAC校验，否则不参与MAC校验。
	
	private String TRADE_TYPE=CCBWechatContants.TRADE_TYPE_JSAPI;  //	交易类型	CHAR(16)	Y	JSAPI--公众号支付、APP--app支付、MINIPRO--小程序、H5--H5跳转支付
	//注:此参数可以是公众号appId
	private String SUB_APPID="";   //	小程序的APPID	CHAR(32)	Y	当前调起支付的小程序APPID
	//在此公众号下的客户的OPEN_ID
	private String SUB_OPENID="";  //	用户子标识	CHAR(128)	Y	用户在小程序appid下的唯一标识，小程序通过wx.login获取，接口文档地址：https://developers.weixin.qq.com/miniprogram/dev/api/api-login.html?t=20161122
	public String getMERCHANTID() {
		return MERCHANTID;
	}
	public void setMERCHANTID(String mERCHANTID) {
		MERCHANTID = mERCHANTID;
	}
	public String getPOSID() {
		return POSID;
	}
	public void setPOSID(String pOSID) {
		POSID = pOSID;
	}
	public String getBRANCHID() {
		return BRANCHID;
	}
	public void setBRANCHID(String bRANCHID) {
		BRANCHID = bRANCHID;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}
	public String getPAYMENT() {
		return PAYMENT;
	}
	public void setPAYMENT(String pAYMENT) {
		PAYMENT = pAYMENT;
	}
	public String getCURCODE() {
		return CURCODE;
	}
	public void setCURCODE(String cURCODE) {
		CURCODE = cURCODE;
	}
	public String getREMARK1() {
		return REMARK1;
	}
	public void setREMARK1(String rEMARK1) {
		REMARK1 = rEMARK1;
	}
	public String getREMARK2() {
		return REMARK2;
	}
	public void setREMARK2(String rEMARK2) {
		REMARK2 = rEMARK2;
	}
	public String getTXCODE() {
		return TXCODE;
	}
	public void setTXCODE(String tXCODE) {
		TXCODE = tXCODE;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getPUB() {
		return PUB;
	}
	public void setPUB(String pUB) {
		PUB = pUB;
	}
	public String getGATEWAY() {
		return GATEWAY;
	}
	public void setGATEWAY(String gATEWAY) {
		GATEWAY = gATEWAY;
	}
	public String getCLIENTIP() {
		return CLIENTIP;
	}
	public void setCLIENTIP(String cLIENTIP) {
		CLIENTIP = cLIENTIP;
	}
	public String getREGINFO() {
		return REGINFO;
	}
	public void setREGINFO(String rEGINFO) {
		REGINFO = rEGINFO;
	}
	public String getPROINFO() {
		return PROINFO;
	}
	public void setPROINFO(String pROINFO) {
		PROINFO = pROINFO;
	}
	public String getREFERER() {
		return REFERER;
	}
	public void setREFERER(String rEFERER) {
		REFERER = rEFERER;
	}
	public String getTRADE_TYPE() {
		return TRADE_TYPE;
	}
	public void setTRADE_TYPE(String tRADE_TYPE) {
		TRADE_TYPE = tRADE_TYPE;
	}
	public String getSUB_APPID() {
		return SUB_APPID;
	}
	public void setSUB_APPID(String sUB_APPID) {
		SUB_APPID = sUB_APPID;
	}
	public String getSUB_OPENID() {
		return SUB_OPENID;
	}
	public void setSUB_OPENID(String sUB_OPENID) {
		SUB_OPENID = sUB_OPENID;
	}
	
	//二级商户信息,若上送二级商户信息则八个二级商户信息字段必须都送值，当该字段有值时参与MAC校验，否则不参与MAC校验。
//	private String SMERID="";//	二级商户代码	CHAR(15)	N	二级商户代码
//	private String SMERNAME="";//	二级商户名称	中文（33）英文CHAR(100)	N	二级商户名称，中文需使用escape编码
//	private String SMERTYPEID="";//	二级商户类别代码	CHAR(15)	N	二级商户类别代码
//	private String SMERTYPE="";//	二级商户类别名称	中文（27）英文CHAR(81)	N	二级商户类别名称，汉字最长27个，中文需使用escape编码
//	private String TRADECODE="";//	交易类型代码	CHAR(15)	N	交易类型代码
//	private String TRADENAME="";//	交易类型名称	中文（10）英文CHAR(30)	N	如消费、投资理财、信用卡还款等，中文需使用escape编码
//	private String SMEPROTYPE="";//	商品类别代码	CHAR(24)	N	商品类别代码
//	private String PRONAME="";//	商品类别名称	中文（15）英文CHAR(50)	N	商品类别名称，中文需使用escape编码

	
	
		
	
	
	
}
