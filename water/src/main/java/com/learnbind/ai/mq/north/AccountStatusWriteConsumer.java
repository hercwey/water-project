package com.learnbind.ai.mq.north;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.model.iotbean.command.AccountStatusWriteResponse;
import com.learnbind.ai.mq.MQConstant;
import com.learnbind.ai.mq.north.service.AccountStatusWriteResponseProcessService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north
 *
 * @Title: AccountStatusWriteConsumer.java
 * @Description: 消费者-写开户状态返回数据
 *
 * @author SRD
 * @date 2020年3月7日 下午10:07:26
 * @version V1.0 
 *
 */
@Component
public class AccountStatusWriteConsumer extends BaseConsumer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AccountStatusWriteConsumer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;
	@Autowired
	private AccountStatusWriteResponseProcessService accountStatusWriteResponseProcessService;

	/**
	 * 通过构造函数 实例化对象
	 */
	public AccountStatusWriteConsumer() {
		
	}

	@Override
	public void initParams() {
		this.consumerGroup = MQConstant.C_G_NORTH_ACCOUNT_STATUS_WRITE;// 消费者分组
		this.topicName = rocketTopicConfig.getTopicName();// 主题名称
		this.tag = rocketTopicConfig.getTagAccountStatusWriteNorth();// tag
	}
	@Override
	public void processMessage(String msg) {
		AccountStatusWriteResponse statusWriteRsp = AccountStatusWriteResponse.fromJson(msg);
		accountStatusWriteResponseProcessService.processResponseData(statusWriteRsp);
	}

}
