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
import com.learnbind.ai.model.iotbean.report.AutoReport;
import com.learnbind.ai.mq.MQConstant;
import com.learnbind.ai.mq.north.service.AutoReportDataProcessService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north
 *
 * @Title: AutoReportConsumer.java
 * @Description: 消费者-设备自动上报数据
 *
 * @author SRD
 * @date 2020年2月22日 上午10:57:23
 * @version V1.0
 *
 */
@Component
public class AutoReportConsumer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AutoReportConsumer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;
	@Autowired
	private AutoReportDataProcessService autoReportDataProcessService;

	/**
	 * 通过构造函数 实例化对象
	 */
	public AutoReportConsumer() {
		
	}
	
	/**
	 * @Title: start
	 * @Description: 启动消费者监听
	 * @throws MQClientException 
	 */
	public void start() throws MQClientException {
		try {

			String charsetName = MQConstant.CHARSET_NAME;//字符集
			String consumerGroup = MQConstant.C_G_NORTH_AUTO_REPORT;// 消费者分组
			String topicName = rocketTopicConfig.getTopicName();// 主题名称
			String tag = rocketTopicConfig.getTagAutoReport();// tag

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
							log.debug("消费者分组-设备自动上报数据【" + consumerGroup + "】，主题topic【" + msg.getTopic() + "】，tag【" + tag
									+ "】，消费消息【" + body + "】");
							
							AutoReport reportData = AutoReport.fromJson(body);//把接收到的数据转成对象
							autoReportDataProcessService.processAutoReportData(reportData);//处理接收到的数据
							
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
			log.debug("----------消费者-监听设备自动上报数据启动成功");
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}
	
	public static AutoReport getTest() {
		String test = "{\r\n" + 
				" \"checksum\": -97,\r\n" + 
				" \"ctrlCode\": \"81\",\r\n" + 
				" \"data\": \"{\\\"batteryVoltage\\\":3219,\\\"meterNumber\\\":\\\"434045073936\\\",\\\"meterStatus\\\":{\\\"batteryLow\\\":0,\\\"magneticAlarmOn\\\":0,\\\"magneticOn\\\":0,\\\"maxReportOn\\\":0,\\\"periodOn\\\":1,\\\"sampleLineCut\\\":0,\\\"valveAbnormal\\\":0,\\\"valveOpen\\\":1},\\\"meterTime\\\":1581374157000,\\\"pressure\\\":\\\"0.0\\\",\\\"sampleUnit\\\":\\\"0.1\\\",\\\"signal\\\":\\\"13\\\",\\\"totalVolume\\\":34}\",\r\n" + 
				" \"dataBasic\": \"681036390745404358811d1f90b0363907454043573522100102200e0000000019320900130000009f16\",\r\n" + 
				" \"dataDI\": -28641,\r\n" + 
				" \"dataType\": 1,\r\n" + 
				" \"deviceId\": \"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\",\r\n" + 
				" \"eventTime\": 1581374164000,\r\n" + 
				" \"factoryCode\": \"5843\",\r\n" + 
				" \"gatewayId\": \"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\",\r\n" + 
				" \"jsonData\": \"{\\\"notifyType\\\":\\\"deviceDatasChanged\\\",\\\"requestId\\\":null,\\\"deviceId\\\":\\\"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\\\",\\\"gatewayId\\\":\\\"b7c99b36-3f23-4d27-bd6d-bb0603c6fbcb\\\",\\\"services\\\":[{\\\"serviceId\\\":\\\"JRprotocol\\\",\\\"serviceType\\\":\\\"JRprotocol\\\",\\\"data\\\":{\\\"JRprotocolXY\\\":\\\"681036390745404358811d1f90b0363907454043573522100102200e0000000019320900130000009f16\\\"},\\\"eventTime\\\":\\\"20200210T223604Z\\\"}]}\",\r\n" + 
				" \"meterAddr\": \"4045073936\",\r\n" + 
				" \"meterType\": 16,\r\n" + 
				" \"reportData\": {\r\n" + 
				"  \"batteryVoltage\": 3219,\r\n" + 
				"  \"meterNumber\": \"434045073936\",\r\n" + 
				"  \"meterStatus\": {\r\n" + 
				"   \"batteryLow\": 0,\r\n" + 
				"   \"magneticAlarmOn\": 0,\r\n" + 
				"   \"magneticOn\": 0,\r\n" + 
				"   \"maxReportOn\": 0,\r\n" + 
				"   \"periodOn\": 1,\r\n" + 
				"   \"sampleLineCut\": 0,\r\n" + 
				"   \"valveAbnormal\": 0,\r\n" + 
				"   \"valveOpen\": 1\r\n" + 
				"  },\r\n" + 
				"  \"meterTime\": 1581374157000,\r\n" + 
				"  \"pressure\": \"0.0\",\r\n" + 
				"  \"sampleUnit\": \"0.1\",\r\n" + 
				"  \"signal\": \"13\",\r\n" + 
				"  \"totalVolume\": 34\r\n" + 
				" },\r\n" + 
				" \"sequence\": 176,\r\n" + 
				" \"serviceId\": \"JRprotocol\",\r\n" + 
				" \"serviceType\": \"JRprotocol\"\r\n" + 
				"}";
		AutoReport report = AutoReport.fromJson(test);
		System.out.println(report.toString());
		System.out.println(report.getReportData().toString());
		return report;
	}
	public static void main(String[] args) {
		//test();
	}

}
