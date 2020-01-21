package com.learnbind.ai.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.model
 *
 * @Title: StatisticTempTable.java
 * @Description: 用于清除数据服务. 无需在数据库中定义此表.
 *
 * @author lenovo
 * @date 2019年8月28日 下午7:30:36
 * @version V1.0 
 *
 */
//@Table(name = "StatisticTempTable")
public class CleanDataTempTable {
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "JDBC")
	private Long id;

	@Override
	public String toString() {
		return "CleanDataTempTable [id=" + id + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
