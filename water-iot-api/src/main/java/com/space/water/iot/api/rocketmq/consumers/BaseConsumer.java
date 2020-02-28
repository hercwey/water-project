package com.space.water.iot.api.rocketmq.consumers;

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
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.space.water.iot.api.command.CommandCache;
import com.space.water.iot.api.model.command.BaseCommandRequest;
import com.space.water.iot.api.model.iot.command.CommandBean;
import com.space.water.iot.api.rocketmq.MQConstant;
import com.space.water.iot.api.rocketmq.RocketTopicConfig;
import com.space.water.iot.api.util.LogUtil;

public abstract class BaseConsumer {
	@Autowired
	RocketTopicConfig topicConfig;
	/**
	 * 消费者组
	 */
	String topic = "";
	String group = "example_group_name";// "test_mq_consumer";
	String tag = "tag";

	/**
	 * 通过构造函数 实例化对象
	 */
	public BaseConsumer() throws MQClientException {
		
	}
	
	public void initConsumer() {
		initParams();
		try {
			topic = topicConfig.getTopicName();
			DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(group);
			consumer.setNamesrvAddr(MQConstant.NAME_SERVER);
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
			consumer.setMessageModel(MessageModel.BROADCASTING);
			// 订阅主题和 标签（ * 代表所有标签)下信息
			consumer.subscribe(topic, tag);
			// //注册消费的监听 并在此监听中消费信息，并返回消费的状态信息
			consumer.registerMessageListener(new MessageListenerConcurrently() {

				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
						ConsumeConcurrentlyContext context) {
					try {
						for (Message msg : msgs) {
							/**
							String body = new String(msg.getBody(), "utf-8");
							LogUtil.debug("-------------------------------------");
							LogUtil.debug("| Consumer获取消息");
							LogUtil.debug("| Time :" + new Date(System.currentTimeMillis()).toGMTString());
							LogUtil.debug("| Topic:" + msg.getTopic());
							LogUtil.debug("| Tags :" + msg.getTags());
							LogUtil.debug("| Data :" + body);
							*/
							// TODO G11 根据tags中的值调用对应接口
							processMessage(msg);
//							LogUtil.debug("-------------------------------------");
						}
					} catch (Exception e) {
						e.printStackTrace();
						return ConsumeConcurrentlyStatus.RECONSUME_LATER;
					}
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

				}
			});

			consumer.start();
			LogUtil.info("-------------------------------------");
			LogUtil.info("| IoT Api "+ getClass().getName() +" 启动成功=======");
			LogUtil.info("-------------------------------------");
		} catch (MQClientException e) {
			e.printStackTrace();
		}

	}
	
	abstract void initParams();
	abstract void processMessage(Message msg);
	
	protected void cacheCommand(BaseCommandRequest request, String cmdHexStr) {
		// TODO G11 生成CommandBean
		CommandBean commandBean = new CommandBean();
		commandBean.setId(request.getId());
		commandBean.setDeviceId(request.getDeviceId());
		commandBean.setServiceId(request.getServiceId());
		commandBean.setMethod(request.getMethod());
		JSONObject parasObject = new JSONObject();
		parasObject.put("value", cmdHexStr);
		commandBean.setParas(parasObject);

		CommandCache.getInstance().addCommand(request.getDeviceId(), JSON.toJSONString(commandBean));
	}
}
