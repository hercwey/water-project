package com.learnbind.ai.tax.distributeinvoice;

import java.math.BigDecimal;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.distributeinvoice
 *
 * @Title: DistAccountItem.java
 * @Description: 待分配账目对象
 *
 * @author lenovo
 * @date 2019年12月11日 下午3:28:51
 * @version V1.0 
 *
 */
public class SourceAccountItem {
	/*
	{
		
	    mergeAccountItem:true,  //true or false; 是否合并账单（按价格）
	    accountItemList:  //账目列表
		[
	       {
	       	  "accountItemId":"xxx",  //账目id
	          "basePrice": "7.54",
			  "sewagePrice": "0",
			  "totalPrice": "7.54",
			  "realWaterAmount ": "41",
			  "baseWaterFee": "309.14",
			  "sewageWaterFee": "0",
			  "totalWaterFee": "309.14"
	       },
			{}
	  	]
	}
	 */
	
	private final int BASE_PRICE=1;
	private final int SEWAGE_PRICE=2;
	private final int TOTAL_PRICE=3;
	
	//--------------接收数据时使用---------------------
	
	private Long 		accountItemId;    //账目ID
	private BigDecimal 	realWaterAmount;  //实收水量
	
	private BigDecimal basePrice;	  //基价
	private BigDecimal sewagePrice;  //污水处理费单价
	private BigDecimal totalPrice;   //综价
	
	private BigDecimal baseWaterFee;  //碁价水费
	private BigDecimal sewageWaterFee;  //污水处理费
	private BigDecimal totalWaterFee;  //综价水费
	
	
	public BigDecimal getPrice(int usePrice) {
		
		
		BigDecimal price=null;
		
		switch(usePrice) {
		case BASE_PRICE:
			price=getBasePrice();
			break;
		case SEWAGE_PRICE:
			price=getSewagePrice();
			break;
		case TOTAL_PRICE:
			price=getTotalPrice();
			break;
		}
		return price;
	}
	
	public BigDecimal getWaterFee(int usePrice) {
		
		BigDecimal waterFee=null;
		
		switch(usePrice) {
		case BASE_PRICE:
			waterFee=getBaseWaterFee();
			break;
		case SEWAGE_PRICE:
			waterFee=getSewageWaterFee();
			break;
		case TOTAL_PRICE:
			waterFee=getTotalWaterFee();
			break;
		}
		return waterFee;
	}
	
	//---------------合并账单时使用---------------------
	private String combinAccountItemIds="";  	//合并后的账单列表,以逗号进行分隔
	private BigDecimal combinRealWaterAmount;  	//合并后水量
	private BigDecimal combinPrice;  			//合并后所使用价格
	private BigDecimal combinWaterFee;			//合并后水费
	
	public BigDecimal getCombinWaterFee() {
		return combinWaterFee;
	}

	public void setCombinWaterFee(BigDecimal combinWaterFee) {
		this.combinWaterFee = combinWaterFee;
	}

	//--------------------------------
	public String getCombinAccountItemIds() {
		return combinAccountItemIds;
	}
	public void setCombinAccountItemIds(String combinAccountItemIds) {
		this.combinAccountItemIds = combinAccountItemIds;
	}
	public BigDecimal getCombinRealWaterAmount() {
		return combinRealWaterAmount;
	}
	public void setCombinRealWaterAmount(BigDecimal combinRealWaterAmount) {
		this.combinRealWaterAmount = combinRealWaterAmount;
	}
	public BigDecimal getCombinPrice() {
		return combinPrice;
	}
	public void setCombinPrice(BigDecimal combinPrice) {
		this.combinPrice = combinPrice;
	}
	
	//-----------------------------------
	
	public Long getAccountItemId() {
		return accountItemId;
	}
	public void setAccountItemId(Long accountItemId) {
		this.accountItemId = accountItemId;
	}
	public BigDecimal getRealWaterAmount() {
		return realWaterAmount;
	}
	public void setRealWaterAmount(BigDecimal realWaterAmount) {
		this.realWaterAmount = realWaterAmount;
	}
	public BigDecimal getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
	public BigDecimal getSewagePrice() {
		return sewagePrice;
	}
	public void setSewagePrice(BigDecimal sewagePrice) {
		this.sewagePrice = sewagePrice;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public BigDecimal getBaseWaterFee() {
		return baseWaterFee;
	}
	public void setBaseWaterFee(BigDecimal baseWaterFee) {
		this.baseWaterFee = baseWaterFee;
	}
	public BigDecimal getSewageWaterFee() {
		return sewageWaterFee;
	}
	public void setSewageWaterFee(BigDecimal sewageWaterFee) {
		this.sewageWaterFee = sewageWaterFee;
	}
	public BigDecimal getTotalWaterFee() {
		return totalWaterFee;
	}
	public void setTotalWaterFee(BigDecimal totalWaterFee) {
		this.totalWaterFee = totalWaterFee;
	}
	
	
	
	
	

}
