package com.space.water.iot.api.rocketmq;

import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.space.water.iot.api.test.Tester;
import com.space.water.iot.api.util.LogUtil;

@RestController
public class MQTestController {

	@Autowired
	private Producer producer;
	@Autowired
	RocketTopicConfig topicConfig;

	/**
	 * 初始化消息
	 */
	public MQTestController() {

	}

	@RequestMapping("/test/rocketmq")
	public Object callback(@RequestBody String data) throws Exception {
		JSONObject jsonObject = (JSONObject) JSONObject.parse(data);
		/**
		 * String topic = jsonObject.getString("topic"); String tag =
		 * jsonObject.getString("tag"); String key = jsonObject.getString("key"); String
		 * dataStr = jsonObject.getString("data");
		 * 
		 * LogUtil.debug("--------------------------------------------------------------------------------------");
		 * LogUtil.debug("| Topic:" + topic); LogUtil.debug("| tag :" + tag);
		 * LogUtil.debug("| key :" + key); LogUtil.debug("| data :" +
		 * dataStr);
		 * LogUtil.debug("--------------------------------------------------------------------------------------");
		 * 
		 * // 创建生产信息 Message message = new Message(topic, tag, key, dataStr.getBytes());
		 */
		// 自动生成模拟数据
		String tag = jsonObject.getString("tag");
		String messageData = "{}";
		if (tag.equals(topicConfig.getTagAutoReport())) {
			messageData = Tester.autoReport();
		} else if (tag.equals(topicConfig.getTagConfigParmsSouth())) {
			messageData = Tester.configParamsSouth();
		} else if (tag.equals(topicConfig.getTagConfigParmsNorth())) {
			messageData = Tester.configParamsNorth();
		} else if (tag.equals(topicConfig.getTagConfigThresholdSouth())) {
			messageData = Tester.configThresholdSouth();
		} else if (tag.equals(topicConfig.getTagConfigThresholdNorth())) {
			messageData = Tester.configThresholdNorth();
		} else if (tag.equals(topicConfig.getTagControlValveSouth())) {
			messageData = Tester.controlValveSouth();
		} else if (tag.equals(topicConfig.getTagControlValveNorth())) {
			messageData = Tester.controlValveNorth();
		} else if (tag.equals(topicConfig.getTagDeviceRegisterSouth())) {
			messageData = Tester.deviceRegisterSouth();
		} else if (tag.equals(topicConfig.getTagDeviceRegisterNorth())) {
		} else if (tag.equals(topicConfig.getTagDeviceDeleteSouth())) {
			messageData = Tester.deviceDeleteSouth();
		} else if (tag.equals(topicConfig.getTagDeviceQuerySouth())) {
			messageData = Tester.deviceQuery();
		} else if (tag.equals(topicConfig.getTagQueryMonthDataSouth())) {
			messageData = Tester.queryMonthDataSouth();
		} else if (tag.equals(topicConfig.getTagQueryMonthdataNorth())) {
			messageData = Tester.queryMonthDataNorth();
		} else if (tag.equals(topicConfig.getTagQueryParmsSouth())) {
			messageData = Tester.queryParamsSouth();
		} else if (tag.equals(topicConfig.getTagQueryParmsNorth())) {
			messageData = Tester.queryParamsNorth();
		}

		// 发送
		SendResult sendResult = producer.getProducer().send(Tester.packMQMessage(tag, messageData));
		LogUtil.debug("输出生产者信息={}" + sendResult);
		return "成功";
	}
}
