package com.learnbind.ai.mq.north;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnbind.ai.config.rocketmq.RocketTopicConfig;
import com.learnbind.ai.model.iotbean.command.QueryMonthDataResponse;
import com.learnbind.ai.mq.MQConstant;
import com.learnbind.ai.mq.north.service.QueryMonthDataResponseProcessService;

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
public class QueryMonthDataConsumer extends BaseConsumer {

	/**
	 * @Fields log：日志
	 */
	private static final Logger log = LoggerFactory.getLogger(QueryMonthDataConsumer.class);

	/**
	 * @Fields rocketTopicConfig：MQ topic tag 读取properties文件
	 */
	@Autowired
	private RocketTopicConfig rocketTopicConfig;
	@Autowired
	private QueryMonthDataResponseProcessService queryMonthDataResponseProcessService;
	
	/**
	 * 通过构造函数 实例化对象
	 */
	public QueryMonthDataConsumer() {
		
	}

	@Override
	public void initParams() {
		this.consumerGroup = MQConstant.C_G_NORTH_QUERY_MONTH_DATA;// 消费者分组
		this.topicName = rocketTopicConfig.getTopicName();// 主题名称
		this.tag = rocketTopicConfig.getTagQueryMonthdataNorth();// tag
	}
	@Override
	public void processMessage(String msg) {
		QueryMonthDataResponse queryMonthDataRsp = QueryMonthDataResponse.fromJson(msg);
		queryMonthDataResponseProcessService.processResponseData(queryMonthDataRsp);
	}

}
