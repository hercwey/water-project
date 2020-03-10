package com.learnbind.ai.mq.north;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.model.iotbean.command.AccountStatusReadResponse;
import com.learnbind.ai.mq.MQConstant;
import com.learnbind.ai.mq.north.service.AccountStatusReadResponseProcessService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.mq.north
 *
 * @Title: AccountStatusReadConsumer.java
 * @Description: 消费者-读开户状态返回数据
 *
 * @author SRD
 * @date 2020年3月3日 下午10:05:38
 * @version V1.0 
 *
 */
@Component
public class AccountStatusReadConsumer extends BaseConsumer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(AccountStatusReadConsumer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;
	@Autowired
	private AccountStatusReadResponseProcessService accountStatusReadResponseProcessService;

	/**
	 * 通过构造函数 实例化对象
	 */
	public AccountStatusReadConsumer() {
		
	}

	@Override
	public void initParams() {
		this.consumerGroup = MQConstant.C_G_NORTH_ACCOUNT_STATUS_READ;// 消费者分组
		this.topicName = rocketTopicConfig.getTopicName();// 主题名称
		this.tag = rocketTopicConfig.getTagAccountStatusReadNorth();// tag
	}

	@Override
	public void processMessage(String msg) {
		AccountStatusReadResponse statusReadRsp = AccountStatusReadResponse.fromJson(msg);
		accountStatusReadResponseProcessService.processResponseData(statusReadRsp);
		
	}

}
