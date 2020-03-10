package com.learnbind.ai.mq.north;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.model.iotbean.command.OrderStatusResponse;
import com.learnbind.ai.mq.MQConstant;
import com.learnbind.ai.mq.north.service.OrderStatusResponseProcessService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north
 *
 * @Title: OrderStatusConsumer.java
 * @Description: 消费者-命令执行状态返回数据
 *
 * @author SRD
 * @date 2020年2月22日 上午10:57:23
 * @version V1.0
 *
 */
@Component
public class OrderStatusConsumer extends BaseConsumer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(OrderStatusConsumer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;
	@Autowired
	private OrderStatusResponseProcessService orderStatusResponseProcessService;
	
	/**
	 * 通过构造函数 实例化对象
	 */
	public OrderStatusConsumer() {
		
	}

	@Override
	public void initParams() {
		this.consumerGroup = MQConstant.C_G_NORTH_ORDER_STATUS;// 消费者分组
		this.topicName = rocketTopicConfig.getTopicName();// 主题名称
		this.tag = rocketTopicConfig.getTagOrderStatus();// tag
	}
	@Override
	public void processMessage(String msg) {
		OrderStatusResponse orderStatusRsp = OrderStatusResponse.fromJson(msg);
		orderStatusResponseProcessService.processResponseData(orderStatusRsp);
	}

}
