package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: IssueControlRequest.java
 * @Description: 开票控制请求数据包
 *
 * @author lenovo
 * @date 2019年11月9日 下午3:59:24
 * @version V1.0 
 *
 */
public class IssueControlRequest {
	/*
	 * 固定值： 0：允许该客户端开票 1：不允许该客户端开票
	 */
	private String kpkz="";  //开票控制  

	public String getKpkz() {
		return kpkz;
	}

	public void setKpkz(String kpkz) {
		this.kpkz = kpkz;
	}

	
	
	
}
