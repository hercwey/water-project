package com.learnbind.ai.ccbwechat.entity;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.ccbwechat.entity
 *
 * @Title: CCBCallbackParam.java
 * @Description: CCB支付成功回调时所传递的参数
 *
 * @author lenovo
 * @date 2020年1月7日 下午3:20:43
 * @version V1.0 
 *
 */

/* 
	POSID 商户柜台代码 	CHAR(9) 	从商户传送的信息中获得
	BRANCHID 分行代码	 CHAR(9)	 从商户传送的信息中获得
	ORDERID 定单号	 CHAR(30) 		从商户传送的信息中获得
	PAYMENT 付款金额 NUMBER(16,2) 	从商户传送的信息中获得
									请商户务必与订单支付金额比对 ，确认两者金额一致；
	CURCODE 币种 CHAR(2) 从商户传送的信息中获得
	REMARK1 备注一 CHAR(30) 从商户传送的信息中获得
	REMARK2 备注二 CHAR(30) 从商户传送的信息中获得
	ACC_TYPE 账户类型 CHAR(2) 服务器通知中有此字段返回且参与验签
	SUCCESS 成功标志 CHAR(1) 成功－Y，失败－N
	TYPE 接口类型 CHAR(1) 分行业务人员在 P2 员工渠道后台设置防钓鱼的开关。
							1.开关关闭时，无此字段返回且不参与验签。
							2.开关打开时，有此字段返回且参与验签。参数值为防钓鱼接口
	REFERER Referer 信息 CHAR(100) 分行业务人员在 P2 员工渠道后台设置防钓鱼开关。
							1.开关关闭时，无此字段返回且不参与验签。
							2.开关打开时，有此字段返回且参与验签。
	CLIENTIP 客户端 IP CHAR(40) 客户在商户系统中的 IP，即客户登陆（访问）商户系统时使用的 ip）
	ACCDATE 系统记账日期 CHAR(8) 商户登陆商户后台设置返回记账日期的开关
							1.开关关闭时，无此字段返回且不参与验签。
							2.开关打开时，有此字段返回且参与验签。参数值格式 为 YYYYMMDD （ 如20100907）。
	USRMSG 支付账户信息 CHAR(100) 分行业务人员在 P2 员工渠道后台设置防钓鱼开关和返回账户信息的开关。
							1.开关关闭时，无此字段返回且不参与验签。
							2.开关打开但支付失败时，无此字段返回且不参与验签。
							3.开关打开且支付成功时，有此字段返回且参与验签。参数值格式如下：“姓名|账号加密后的密文”。
							解密方法请参考“商户通知验签包“文件夹下的《USERMSG》压缩包INSTALL
	NUM 	分期期数 CHAR(2) 从商户传送的信息中获得;当分期期数为空或无此字段上送时，无此字段返回且不参与验签，否则有此字段返回且参与验签。
	ERRMSG 错误信息 CHAR(12) 该值默认返回为空，商户无需处理，仅需参与验签即可。当有分期期数返回时，则有 ERRMSG 字段返回且参与验签，否则无此字段返回且不参与验签。
	USRINFO 客户加密信息 CHAR(256) 分行业务人员在 P2 员工渠道后台设置防钓鱼开关和客户信息加密返回的开关。
			1.开关关闭时，无此字段返回且不参与验签
			2.开关打开时，有此字段返回且参数验签。参数值格式如下：“证件号密文|手机号密文”。该字段不可解密。
	SIGN 数字签名 CHAR(256) 签名字段
 */
public class CCBCallbackParam {
	public static final String CCB_CALLBACK_STATUS_SUCCESS="Y";
	public static final String CCB_CALLBACK_STATUS_FAIL="N";
	
	private String POSID=""; // 商户柜台代码 	CHAR(9) 	从商户传送的信息中获得
	private String BRANCHID=""; //分行代码	 CHAR(9)	 从商户传送的信息中获得
	private String ORDERID=""; //定单号	 CHAR(30) 		从商户传送的信息中获得
	private String PAYMENT=""; //付款金额 NUMBER(16,2) 	从商户传送的信息中获得
	   						   //请商户务必与订单支付金额比对 ，确认两者金额一致；
	private String CURCODE="";  // 币种 CHAR(2) 从商户传送的信息中获得
	private String REMARK1="";  // 备注一 CHAR(30) 从商户传送的信息中获得
	private String REMARK2="";  // 备注二 CHAR(30) 从商户传送的信息中获得
	private String ACC_TYPE=""; // 账户类型 CHAR(2) 服务器通知中有此字段返回且参与验签
	private String SUCCESS="";  // 成功标志 CHAR(1) 成功－Y，失败－N
	
	//暂时不使用
	/*
	 * private String TYPE=""; // 接口类型 CHAR(1) 分行业务人员在 P2 员工渠道后台设置防钓鱼的开关。
	 * //1.开关关闭时，无此字段返回且不参与验签。 //2.开关打开时，有此字段返回且参与验签。参数值为防钓鱼接口 //暂时不使用 private
	 * String REFERER=""; // Referer 信息 CHAR(100) 分行业务人员在 P2 员工渠道后台设置防钓鱼开关。
	 * //1.开关关闭时，无此字段返回且不参与验签。 //2.开关打开时，有此字段返回且参与验签。 //暂时不使用 private String
	 * CLIENTIP=""; //客户端 IP CHAR(40) 客户在商户系统中的 IP，即客户登陆（访问）商户系统时使用的 ip）
	 */	
	
	//使用此字段
	private String ACCDATE="";  // 系统记账日期 CHAR(8) 商户登陆商户后台设置返回记账日期的开关
								//1.开关关闭时，无此字段返回且不参与验签。
								//2.开关打开时，有此字段返回且参与验签。参数值格式 为 YYYYMMDD （ 如20100907）。

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

	public String getACC_TYPE() {
		return ACC_TYPE;
	}

	public void setACC_TYPE(String aCC_TYPE) {
		ACC_TYPE = aCC_TYPE;
	}

	public String getSUCCESS() {
		return SUCCESS;
	}

	public void setSUCCESS(String sUCCESS) {
		SUCCESS = sUCCESS;
	}

	public String getACCDATE() {
		return ACCDATE;
	}

	public void setACCDATE(String aCCDATE) {
		ACCDATE = aCCDATE;
	}

	@Override
	public String toString() {
		return "CCBCallbackParam [POSID=" + POSID + ", BRANCHID=" + BRANCHID + ", ORDERID=" + ORDERID + ", PAYMENT="
				+ PAYMENT + ", CURCODE=" + CURCODE + ", REMARK1=" + REMARK1 + ", REMARK2=" + REMARK2 + ", ACC_TYPE="
				+ ACC_TYPE + ", SUCCESS=" + SUCCESS + ", ACCDATE=" + ACCDATE + "]";
	}
	
	//-----以下内容暂时不使用--------
	/*
	 * private String USRMSG="";// 支付账户信息 CHAR(100) 分行业务人员在 P2
	 * 员工渠道后台设置防钓鱼开关和返回账户信息的开关。 //1.开关关闭时，无此字段返回且不参与验签。 //2.开关打开但支付失败时，无此字段返回且不参与验签。
	 * //3.开关打开且支付成功时，有此字段返回且参与验签。参数值格式如下：“姓名|账号加密后的密文”。
	 * //解密方法请参考“商户通知验签包“文件夹下的《USERMSG》压缩包INSTALL private String NUM=""; //分期期数
	 * CHAR(2) 从商户传送的信息中获得;当分期期数为空或无此字段上送时，无此字段返回且不参与验签，否则有此字段返回且参与验签。 private
	 * String ERRMSG=""; // 错误信息 CHAR(12) 该值默认返回为空，商户无需处理，仅需参与验签即可。当有分期期数返回时，则有
	 * ERRMSG 字段返回且参与验签，否则无此字段返回且不参与验签。 private String USRINFO="";// 客户加密信息
	 * CHAR(256) 分行业务人员在 P2 员工渠道后台设置防钓鱼开关和客户信息加密返回的开关。 //1.开关关闭时，无此字段返回且不参与验签
	 * //2.开关打开时，有此字段返回且参数验签。参数值格式如下：“证件号密文|手机号密文”。该字段不可解密。 private String SIGN="";
	 * // 数字签名 CHAR(256) 签名字段
	 */	
	
	
	
	
}
