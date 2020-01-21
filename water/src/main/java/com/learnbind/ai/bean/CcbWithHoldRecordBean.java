package com.learnbind.ai.bean;

import java.math.BigDecimal;

/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: CcbWithHoldRecordBean.java
 * @Description:回盘文件bean
 *
 * @author Thinkpad
 * @date 2019年8月23日 下午7:35:11
 * @version V1.0 
 *
 */
public class CcbWithHoldRecordBean {
	
	/**
     * @Fields withHoldNo：代扣文件Id
     */
    private Long withRecordId;//代扣文件Id
	
    /**
     * @Fields withHoldNo：在回盘文件中的序号
     */
    private int withHoldNo;//在回盘文件中的序号
    /**
     * @Fields cardNo：客户银行卡号
     */
    private String cardNo;//客户银行卡号
    
    /**
     * @Fields customerName：客户名称
     */
    private String customerName;//客户名称
    
    /**
     * @Fields accountAmount：扣费金额
     */
    private BigDecimal accountAmount;//扣费金额
    /**
     * @Fields withHoldStatus：代扣结果
     */
    private int withHoldStatus;//代扣结果
    
    /**
     * @Fields withholdFailReason：代扣失败原因
     */
    private String withholdFailReason;//代扣失败原因
    /**
     * @Fields remark：备注
     */
    private String remark;//备注
    
    //getter and setter
	
	public Long getWithRecordId() {
		return withRecordId;
	}

	public void setWithRecordId(Long withRecordId) {
		this.withRecordId = withRecordId;
	}

	public int getWithHoldNo() {
		return withHoldNo;
	}

	public void setWithHoldNo(int withHoldNo) {
		this.withHoldNo = withHoldNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public BigDecimal getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(BigDecimal accountAmount) {
		this.accountAmount = accountAmount;
	}

	public int getWithHoldStatus() {
		return withHoldStatus;
	}

	public void setWithHoldStatus(int withHoldStatus) {
		this.withHoldStatus = withHoldStatus;
	}

	public String getWithholdFailReason() {
		return withholdFailReason;
	}

	public void setWithholdFailReason(String withholdFailReason) {
		this.withholdFailReason = withholdFailReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	//toString方法
	@Override
	public String toString() {
		return "CcbWithHoldRecordBean [withRecordId=" + withRecordId + ", withHoldNo=" + withHoldNo + ", cardNo="
				+ cardNo + ", customerName=" + customerName + ", accountAmount=" + accountAmount + ", withHoldStatus="
				+ withHoldStatus + ", withholdFailReason=" + withholdFailReason + ", remark=" + remark + "]";
	}
	
}
