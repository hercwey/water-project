package com.learnbind.ai.tax.utils;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.utils
 *
 * @Title: TestBill.java
 * @Description: 模拟账单对象
 *
 * @author lenovo
 * @date 2019年12月9日 上午10:22:12
 * @version V1.0 
 *
 */
public class CustBill {
	/*
	 * 序号: serialNo 模拟ID 金额: amount 水量: waterNum 价格: price
	 */
	private Integer serialNo;
	private Integer waterNum;
	private Integer price;
	private Integer amount;
	public Integer getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(Integer serialNo) {
		this.serialNo = serialNo;
	}
	public Integer getWaterNum() {
		return waterNum;
	}
	public void setWaterNum(Integer waterNum) {
		this.waterNum = waterNum;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
	
	
	
	
	
}
