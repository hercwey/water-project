package com.learnbind.ai.mq.south;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Component;

import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.mq.MQConstant;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north
 *
 * @Title: DeviceUpdateProducer.java
 * @Description: 生产者-调用IOT电信平台修改设备接口
 *
 * @author SRD
 * @date 2020年2月22日 上午10:57:23
 * @version V1.0
 *
 */
//@Component
@AutoConfigureAfter
public class DeviceUpdateProducer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(DeviceUpdateProducer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;

	/**
	 * @Fields producer：生产者实例
	 */
	private DefaultMQProducer producer;
	
	/**
	 * 通过构造函数 实例化对象
	 */
	public DeviceUpdateProducer() throws MQClientException {
		
		String producerGroup = MQConstant.P_G_SORTH_DEVICE_UPDATE;//生产者分组
		//示例生产者
		producer = new DefaultMQProducer(producerGroup);
        //producer.setSendMsgTimeout(10000);
        //不开启vip通道 开通口端口会减2
        producer.setVipChannelEnabled(false);
        //绑定name server
        producer.setNamesrvAddr(MQConstant.NAME_SERVER);
        producer.start();

	}
	
	/**
	 * @Title: sendMsg
	 * @Description: 发送消息
	 * @param keys
	 * @param body	消息内容
	 * @return
	 * @throws MQClientException
	 * @throws RemotingException
	 * @throws MQBrokerException
	 * @throws InterruptedException 
	 */
	public SendResult sendMsg(String keys, String body) throws MQClientException, RemotingException, MQBrokerException, InterruptedException {
		
		String topicName = rocketTopicConfig.getTopicName();// 主题名称
		String tag = rocketTopicConfig.getTagDeviceUpdateSouth();// tag
		
		//创建生产信息
		Message message = null;
		if(StringUtils.isBlank(keys)) {
			message = new Message(topicName, tag, body.getBytes());
		}else {
			message = new Message(topicName, tag, keys, body.getBytes());
		}
        //发送
        SendResult sendResult = producer.send(message);
        log.debug("调用IOT电信平台修改设备接口到MQ，发送结果是："+sendResult);
        return sendResult;
	}

}
