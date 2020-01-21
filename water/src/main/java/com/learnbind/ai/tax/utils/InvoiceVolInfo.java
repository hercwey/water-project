package com.learnbind.ai.tax.utils;

public class InvoiceVolInfo {
	/*
	 * 发票起始号码:startNo 发票库存数: inventory 单张发票最大值:10
	 */
	private Integer startNo;  //起始号码
	private Integer inventory;  //发票库存数量
	private Integer maxAmount;  //单张发票最大值
	
	public Integer getStartNo() {
		return startNo;
	}
	public void setStartNo(Integer startNo) {
		this.startNo = startNo;
	}
	public Integer getInventory() {
		return inventory;
	}
	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}
	public Integer getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Integer maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	
	
	
}
