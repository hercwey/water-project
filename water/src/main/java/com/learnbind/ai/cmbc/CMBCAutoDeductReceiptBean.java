package com.learnbind.ai.cmbc;

import java.math.BigDecimal;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.cmbc
 *
 * @Title: CMBCAutoDeductReceiptBean.java
 * @Description: 中国民生银行扣费回执结果类
 *
 * @author Administrator
 * @date 2019年7月12日 下午3:52:27
 * @version V1.0 
 *
 */
public class CMBCAutoDeductReceiptBean {

	/**
	 * @Fields SUCCESS_LENGTH：扣费成功时，数组长度
	 */
	public static final int LENGTH_SUCCESS = 3;//扣费成功时，数组长度
	/**
	 * @Fields FAIL_LENGTH：扣费失败时，数组长度
	 */
	public static final int LENGTH_FAIL = 4;//扣费失败时，数组长度
	
	private String cardNo;//卡号
	private String accountName;//用户名
	private BigDecimal deductAmount;//扣费金额
	private Integer status;//扣费成功/失败状态 0=成功；1=失败；
	private String errMsg;//扣费失败原因
	
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
	public BigDecimal getDeductAmount() {
		return deductAmount;
	}
	public void setDeductAmount(BigDecimal deductAmount) {
		this.deductAmount = deductAmount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	
	@Override
	public String toString() {
		return "CMBCAutoDeductReceiptBean [cardNo=" + cardNo + ", accountName=" + accountName + ", deductAmount="
				+ deductAmount + ", status=" + status + ", errMsg=" + errMsg + "]";
	}
	
}
