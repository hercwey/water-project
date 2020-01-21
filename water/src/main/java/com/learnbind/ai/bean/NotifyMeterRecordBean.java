package com.learnbind.ai.bean;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.bean
 *
 * @Title: AssistantBean.java
 * @Description: 账目辅助核算保存内容Bean
 *
 * @author Administrator
 * @date 2019年8月21日 下午12:16:22
 * @version V1.0 
 *
 */
public class NotifyMeterRecordBean {


	
	/**
	 * 创建一个新的实例 AssistantBean.
	 */
	public NotifyMeterRecordBean() {
		super();
	}
	
	/**
	 * 创建一个新的实例 AssistantBean.
	 * @param id
	 * @param amount
	 * @param desc
	 * @param subject
	 * @param date
	 * @param operator
	 */
	public NotifyMeterRecordBean(Long meterId, String preRead, String currRead, BigDecimal currAmount, String description) {
		super();
		this.meterId = meterId;
		this.preRead = preRead;
		this.currRead = currRead;
		this.currAmount = currAmount;
		this.description = description;
	}
	
	/**
	 * @Fields id：
	 */
	private Long meterId;
	/**
	 * @Fields amount：
	 */
	private String preRead;
	/**
	 * @Fields desc：描述
	 */
	private String currRead;
	/**
	 * @Fields subject：科目
	 */
	private BigDecimal currAmount;
	/**
	 * @Fields date：日期
	 */
	private String description;
	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public String getPreRead() {
		return preRead;
	}

	public void setPreRead(String preRead) {
		this.preRead = preRead;
	}

	public String getCurrRead() {
		return currRead;
	}

	public void setCurrRead(String currRead) {
		this.currRead = currRead;
	}

	public BigDecimal getCurrAmount() {
		return currAmount;
	}

	public void setCurrAmount(BigDecimal currAmount) {
		this.currAmount = currAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "NotifyMeterRecordBean [meterId=" + meterId + ", preRead=" + preRead + ", currRead=" + currRead
				+ ", currAmount=" + currAmount + ", description=" + description + "]";
	}
	
	
	
	
}
