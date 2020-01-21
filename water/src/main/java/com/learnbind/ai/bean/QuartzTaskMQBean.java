package com.learnbind.ai.bean;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.pivas.bean
 *
 * @Title: QuartzTaskMQBean.java
 * @Description: 定时任务同步bean
 *
 * @author Administrator
 * @date 2018年11月10日 下午10:03:01
 * @version V1.0 
 *
 */
public class QuartzTaskMQBean {

	/**
	 * @Fields success：成功状态：true=成功；false=失败
	 */
	private Boolean success;
	/**
	 * @Fields operation：同步任务动作：create=创建任务；update=修改任务；delete=删除任务
	 */
	private String operation;
	/**
	 * @Fields data：操作数据
	 */
	private String data;
	
	/**
	 * @Title: getSuccess
	 * @Description: 成功状态：true=成功；false=失败
	 * @return 
	 */
	public Boolean getSuccess() {
		return success;
	}
	/**
	 * @Title: setSuccess
	 * @Description: 成功状态：true=成功；false=失败
	 * @param success 
	 */
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	/**
	 * @Title: getOperation
	 * @Description: 同步任务动作：create=创建任务；update=修改任务；delete=删除任务
	 * @return 
	 */
	public String getOperation() {
		return operation;
	}
	/**
	 * @Title: setOperation
	 * @Description: 同步任务动作：create=创建任务；update=修改任务；delete=删除任务
	 * @param operation 
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	/**
	 * @Title: getData
	 * @Description: 操作数据
	 * @return 
	 */
	public String getData() {
		return data;
	}
	/**
	 * @Title: setData
	 * @Description: 操作数据
	 * @param data 
	 */
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "RabbitMQSyncBean [success=" + success + ", operation=" + operation + ", data=" + data + "]";
	}
	
}
