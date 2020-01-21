package com.learnbind.ai.util.wx.entity;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.util.wx.entity
 *
 * @Title: Signature.java
 * @Description: 验证消息的确来自微信服务器，微信发送的消息
 *
 * @author Administrator
 * @date 2019年8月1日 下午7:38:18
 * @version V1.0 
 *
 */
public class Signature {
	
	private String signature;
	
	private String timestamp;
	
	private String nonce;
	
	private String echostr;

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getEchostr() {
		return echostr;
	}

	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}

	@Override
	public String toString() {
		return "Signature [signature=" + signature + ", timestamp=" + timestamp
				+ ", nonce=" + nonce + ", echostr=" + echostr + "]";
	}
	
	

}
