package com.learnbind.ai.mq.north;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.mq.MQConstant;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north
 *
 * @Title: QueryMonthDataConsumer.java
 * @Description: 消费者-查询月冻结返回数据
 *
 * @author SRD
 * @date 2020年2月22日 上午10:57:23
 * @version V1.0
 *
 */
@Component
public class QueryMonthDataConsumer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(QueryMonthDataConsumer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;

	/**
	 * 通过构造函数 实例化对象
	 */
	public QueryMonthDataConsumer() {
		
	}
	
	/**
	 * @Title: start
	 * @Description: 启动消费者监听
	 * @throws MQClientException 
	 */
	public void start() throws MQClientException {
		try {

			String charsetName = MQConstant.CHARSET_NAME;//字符集
			String consumerGroup = MQConstant.C_G_NORTH_QUERY_MONTH_DATA;// 消费者分组
			String topicName = rocketTopicConfig.getTopicName();// 主题名称
			String tag = rocketTopicConfig.getTagQueryMonthdataNorth();// tag

			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
			consumer.setNamesrvAddr(MQConstant.NAME_SERVER);
			// 消费模式:一个新的订阅组第一次启动从队列的最后位置开始消费 后续再启动接着上次消费的进度开始消费
			// consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			// set to broadcast mode
			consumer.setMessageModel(MessageModel.BROADCASTING);
			// 订阅主题和 标签（ * 代表所有标签)下信息
			consumer.subscribe(topicName, tag);
			// //注册消费的监听 并在此监听中消费信息，并返回消费的状态信息
			consumer.registerMessageListener(new MessageListenerConcurrently() {

				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {

					log.debug("Receive Message is " + "--" + msgs);
					log.debug("Receive context is " + "--" + context);

					// msgs中只收集同一个topic，同一个tag，并且key相同的message
					// 会把不同的消息分别放置到不同的队列中
					try {
						for (Message msg : msgs) {
							// 消费者获取消息 这里只输出 不做后面逻辑处理
							String body = new String(msg.getBody(), charsetName);
							log.debug("消费者分组-查询月冻结返回数据【" + consumerGroup + "】，主题topic【" + msg.getTopic() + "】，tag【" + tag
									+ "】，消费消息【" + body + "】");
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						log.debug("----------消费异常");
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
					log.debug("----------消费成功");
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

				}
			});
			consumer.start();
			log.debug("----------消费者-监听查询月冻结返回数据启动成功");
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}

}
