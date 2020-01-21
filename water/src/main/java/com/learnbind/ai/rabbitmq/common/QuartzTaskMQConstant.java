package com.learnbind.ai.rabbitmq.common;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.pivas.rabbitmq.common
 *
 * @Title: QuartzTaskMQConstant.java
 * @Description: RabbitMQ 定时任务 常量
 *
 * @author Administrator
 * @date 2018年11月12日 上午11:50:37
 * @version V1.0 
 *
 */
public class QuartzTaskMQConstant{
    
	/**
	 * @Fields SYNC_TOPIC_EXCHANGE：通配符模式（Topic Exchange）
	 */
	public static final String QUARTZ_TASK_TOPIC_EXCHANGE = "TOPIC_EXCHANGE_QUARTZ_TASK";
	/**
	 * @Fields SYNC_QUEUE：定时任务队列
	 */
	public static final String QUARTZ_TASK_QUEUE = "topic.quartz.task.queue";
	
	/**
	 * @Fields SYNC_TASK_ACTION_CREATE：定时任务操作：创建任务
	 */
	public static final String QUARTZ_TASK_OPERATION_CREATE = "create";
	
	/**
	 * @Fields SYNC_TASK_ACTION_UPDATE：定时任务操作：修改任务
	 */
	public static final String QUARTZ_TASK_OPERATION_UPDATE = "update";
	
	/**
	 * @Fields SYNC_TASK_ACTION_DELETE：定时任务操作：删除任务
	 */
	public static final String QUARTZ_TASK_OPERATION_DELETE = "delete";
	
}
