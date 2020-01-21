package com.learnbind.ai.service.cmbc;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.cmbc
 *
 * @Title: CmbcExcelFileRowBean.java
 * @Description: 民生银行导出excel一行数据的bean
 *
 * @author Administrator
 * @date 2019年8月25日 上午10:22:10
 * @version V1.0 
 *
 */
@SuppressWarnings("serial")
public class CmbcExcelFileRowBean implements Serializable {

	private Long accountItemId;//账单ID，只在创建文件与账单关系时使用
	private Integer sn;//序号
	private String cardNo;//账号
	private String accountName;//户名
	private BigDecimal totalAmount;//金额
	
	public Long getAccountItemId() {
		return accountItemId;
	}
	public void setAccountItemId(Long accountItemId) {
		this.accountItemId = accountItemId;
	}
	public Integer getSn() {
		return sn;
	}
	public void setSn(Integer sn) {
		this.sn = sn;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Override
	public String toString() {
		return "CmbcExcelRowBean [accountItemId=" + accountItemId + ", sn=" + sn + ", cardNo=" + cardNo
				+ ", accountName=" + accountName + ", totalAmount=" + totalAmount + "]";
	}
	
}
