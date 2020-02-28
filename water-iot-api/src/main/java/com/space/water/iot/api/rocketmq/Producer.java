package com.space.water.iot.api.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.config.MQTags;
import com.space.water.iot.api.util.LogUtil;

@Component
public class Producer {
	private String producerGroup = "example_group_name";
	private DefaultMQProducer producer;

	@Autowired
	RocketTopicConfig topicConfig;

	public Producer() {
		// 示例生产者
		producer = new DefaultMQProducer(producerGroup);
		producer.setSendMsgTimeout(10000);
		// 不开启vip通道 开通口端口会减2
		producer.setVipChannelEnabled(false);
		// 绑定name server
		producer.setNamesrvAddr(MQConstant.NAME_SERVER);
		start();
	}

	/**
	 * 对象在使用之前必须要调用一次，只能初始化一次
	 */
	public void start() {
		try {
			this.producer.start();
		} catch (MQClientException e) {
			e.printStackTrace();
		}
	}

	public DefaultMQProducer getProducer() {
		return this.producer;
	}

	/**
	 * 一般在应用上下文，使用上下文监听器，进行关闭
	 */
	public void shutdown() {
		this.producer.shutdown();
	}

	public SendResult sendNorth(String response, String tag) {
		SendResult sendResult = null;
		// 创建生产信息
		Message message = new Message(topicConfig.getTopicName(), tag, "keys", response.getBytes());
		// 发送
		try {
			sendResult = getProducer().send(message);
			LogUtil.debug("----------------------------------------");
			LogUtil.debug("| 消息发送成功");
			LogUtil.debug("| tag :" + tag);
			LogUtil.debug("| data:" + response);
			LogUtil.debug("----------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResult;
	}

	public SendResult sendNorthAsync(String response, String tag) {
		SendResult sendResult = null;
		// 创建生产信息
		Message message = new Message(topicConfig.getTopicName(), tag, "keys", response.getBytes());
		// 发送
		try {
			getProducer().send(message, new SendCallback() {

				@Override
				public void onSuccess(SendResult sendResult) {
					LogUtil.debug("----------------------------------------");
					LogUtil.debug("| 消息发送成功");
					LogUtil.debug("| tag :" + tag);
					LogUtil.debug("| data:" + response);
					LogUtil.debug("----------------------------------------");
				}

				@Override
				public void onException(Throwable e) {
					LogUtil.error("----------------------------------------");
					LogUtil.error("| 消息发送失败");
					LogUtil.error("| tag :" + tag);
					LogUtil.error("| data:" + response);
					LogUtil.error("| " + e.getMessage());
					LogUtil.error("----------------------------------------");

				}
			});
//    		LogUtil.debug("输出生产者信息={}" + sendResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sendResult;
	}
}