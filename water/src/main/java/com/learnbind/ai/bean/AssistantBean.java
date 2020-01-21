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
public class AssistantBean {

//	id  --->相关联的账目ID
//	amount --->相关金额
//	desc --->描述
//	subject --->科目
//	date --->日期
//	operator--->操作员ID
	
	/**
	 * 创建一个新的实例 AssistantBean.
	 */
	public AssistantBean() {
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
	public AssistantBean(Long id, BigDecimal amount, String desc, String subject, Date date, Long operator) {
		super();
		this.id = id;
		this.amount = amount;
		this.desc = desc;
		this.subject = subject;
		this.date = date;
		this.operator = operator;
	}
	
	/**
	 * @Fields id：相关联的账目ID
	 */
	private Long id;
	/**
	 * @Fields amount：相关金额
	 */
	private BigDecimal amount;
	/**
	 * @Fields desc：描述
	 */
	private String desc;
	/**
	 * @Fields subject：科目
	 */
	private String subject;
	/**
	 * @Fields date：日期
	 */
	private Date date;
	/**
	 * @Fields operator：操作员ID
	 */
	private Long operator;
	
	
	/**
	 * @Title: getId
	 * @Description: 获取相关联的账目ID
	 * @return 
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @Title: setId
	 * @Description: 设置相关联的账目ID
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @Title: getAmount
	 * @Description: 获取相关金额
	 * @return 
	 */
	public BigDecimal getAmount() {
		return amount;
	}
	/**
	 * @Title: setAmount
	 * @Description: 设置相关金额
	 * @param amount 
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	/**
	 * @Title: getDesc
	 * @Description: 获取描述
	 * @return 
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @Title: setDesc
	 * @Description: 设置描述
	 * @param desc 
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	/**
	 * @Title: getSubject
	 * @Description: 获取科目
	 * @return 
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * @Title: setSubject
	 * @Description: 设置科目
	 * @param subject 
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @Title: getDate
	 * @Description: 获取日期
	 * @return 
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * @Title: setDate
	 * @Description: 设置日期
	 * @param date 
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @Title: getOperator
	 * @Description: 获取操作员ID
	 * @return 
	 */
	public Long getOperator() {
		return operator;
	}
	/**
	 * @Title: setOperator
	 * @Description: 设置操作员ID
	 * @param operator 
	 */
	public void setOperator(Long operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		return "AssistantBean [id=" + id + ", amount=" + amount + ", desc=" + desc + ", subject=" + subject + ", date="
				+ date + ", operator=" + operator + "]";
	}
	
}
