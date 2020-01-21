package com.learnbind.ai.tax.packet.request;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.packet.request
 *
 * @Title: PrintParam.java
 * @Description: 打印参数-业务实体
 *
 * @author lenovo
 * @date 2019年10月18日 上午12:48:24
 * @version V1.0 
 *
 */
public class PrintParam {
	/*
	 	DYJMC	打印机名称	字符	92	否	为空时使用开票软件默认打印机
		QDDYFS	清单打印方式	字符	2	是	固定值：
									1：全打
									0：套打
		LEFT	左右偏移量	数值	16,2	否	为空时默认为 0
		TOP	上下偏移量	数值	10,6	否	为空时默认为 0
	 */
	
	private String DYJMC;
	private String QDDYFS;
	private String LEFT;
	private String TOP;
	
	public String getDYJMC() {
		return DYJMC;
	}
	public void setDYJMC(String dYJMC) {
		DYJMC = dYJMC;
	}
	public String getQDDYFS() {
		return QDDYFS;
	}
	public void setQDDYFS(String qDDYFS) {
		QDDYFS = qDDYFS;
	}
	public String getLEFT() {
		return LEFT;
	}
	public void setLEFT(String lEFT) {
		LEFT = lEFT;
	}
	public String getTOP() {
		return TOP;
	}
	public void setTOP(String tOP) {
		TOP = tOP;
	}
	
	
	
}
