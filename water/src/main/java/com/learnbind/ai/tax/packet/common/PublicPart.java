package com.learnbind.ai.tax.packet.common;
/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.common
 *
 * @Title: PublicPacketData.java
 * @Description: 公共报文数据
 *
 * @author lenovo
 * @date 2019年10月15日 上午1:25:43
 * @version V1.0 
 *
 */
public class PublicPart {
	/*
	 * <sid>0</sid> 
	 * <ip></ip> 
	 * <port></port>
	 */
	
	/**
	 * @Fields sid：标识功能类别
	 */
	private String sid;  
	/**
	 * @Fields ip：客户端控制台(开票服务器)ip地址
	 */
	private String ip=PublicPartConsts.TAX_SERVER_IP;
	/**
	 * @Fields port：客户端控制台(开票服务器)端口
	 */
	private String port=PublicPartConsts.TAX_SERVER_PORT;
	
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
}
