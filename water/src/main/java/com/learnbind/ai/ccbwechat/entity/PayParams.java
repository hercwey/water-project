package com.learnbind.ai.ccbwechat.entity;

import com.alibaba.fastjson.annotation.JSONField;

public class PayParams {

	private String SUCCESS="";	//返回状态码 此字段是通信标识，表示通信成功
	private String ERRCODE="";	//错误码	000000表示交易成功，非000000表示交易失败，错误信息可以查看ERRMSG字段
	private String ERRMSG="";	//错误信息	错误信息描述
	private String TXCODE="";	//交易码	530590
	private String appId="";	//微信分配的APPID	参考微信对应的调起支付API  (1)
	private String timeStamp="";	//时间戳 参考微信对应的调起支付API  (2)
	private String nonceStr="";		//随机串 参考微信对应的调起支付API	  (3)
	@JSONField(name="package")
	private String packages="";		//数据包 参考微信对应的调起支付API (4)
	private String signType="";		//签名方式 参考微信对应的调起支付API (5)
	private String paySign="";		//签名数据	参考微信对应的调起支付API (6)
	private String partnerid="";	//子商户的商户号	参考微信对应的调起支付API
	private String prepayid="";		//预支付交易会话ID	参考微信对应的调起支付API
	private String mweb_url="";		//微信H5支付中间页面URL	参考微信对应的调起支付API	
	
	public String getSUCCESS() {
		return SUCCESS;
	}
	public void setSUCCESS(String sUCCESS) {
		SUCCESS = sUCCESS;
	}
	public String getERRCODE() {
		return ERRCODE;
	}
	public void setERRCODE(String eRRCODE) {
		ERRCODE = eRRCODE;
	}
	public String getERRMSG() {
		return ERRMSG;
	}
	public void setERRMSG(String eRRMSG) {
		ERRMSG = eRRMSG;
	}
	public String getTXCODE() {
		return TXCODE;
	}
	public void setTXCODE(String tXCODE) {
		TXCODE = tXCODE;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getPackages() {
		return packages;
	}
	public void setPackages(String packages) {
		this.packages = packages;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getMweb_url() {
		return mweb_url;
	}
	public void setMweb_url(String mweb_url) {
		this.mweb_url = mweb_url;
	}
	
	
}
