package com.learnbind.ai.util.wx.pay;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.util.wx.pay
 *
 * @Title: WxOrder.java
 * @Description: 微信订单
 *
 * @author Administrator
 * @date 2019年8月5日 下午6:44:53
 * @version V1.0 
 *
 */
public class WxOrder {
	
	private String appid;
	
	private String mch_id;
	
	private String transaction_id;
	
	private String out_trade_no;
	
	private String nonce_str;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	@Override
	public String toString() {
		return "QueryOrder [appid=" + appid + ", mch_id=" + mch_id + ", transaction_id=" + transaction_id
				+ ", out_trade_no=" + out_trade_no + ", nonce_str=" + nonce_str + "]";
	}
	
}
